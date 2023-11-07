package com.ali.mirsalari.wrench.controller;

import com.ali.mirsalari.wrench.controller.dto.*;
import com.ali.mirsalari.wrench.controller.mapper.OrderResponseMapper;
import com.ali.mirsalari.wrench.entity.Order;
import com.ali.mirsalari.wrench.entity.enumeration.OrderStatus;
import com.ali.mirsalari.wrench.service.OrderService;
import io.springboot.captcha.SpecCaptcha;
import io.springboot.captcha.base.Captcha;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping(path = "/api/v1/order")
public class OrderController {
    private final OrderResponseMapper orderResponseMapper;
    private final OrderService orderService;
    private final Map<Integer, String> captchaMap = new ConcurrentHashMap<>();
    private final AtomicInteger lastId = new AtomicInteger(1);

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping({"","/"})
    public List<OrderResponse> getOrders() {
        List<Order> orders = orderService.findAll();
        return orders.stream()
                .map(orderResponseMapper::toDto)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    public ResponseEntity<?> registerOrder(@Valid @RequestBody RegisterOrderRequest request,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        Order order = orderService.save(
                request.description(),
                request.suggestedPrice(),
                request.address(),
                request.serviceId(),
                userDetails.getUsername());
        return ResponseEntity.ok(orderResponseMapper.toDto(order));
    }

    @PreAuthorize("hasRole('EXPERT')")
    @PutMapping(path = "{id}")
    public ResponseEntity<?> updateOrder(
                                         @PathVariable("id") Long id,
                                         @Valid @RequestBody RegisterOrderRequest request,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        Order order = orderService.update(
                id,
                request.description(),
                request.suggestedPrice(),
                request.address(),
                request.serviceId(),
                userDetails.getUsername());
        return ResponseEntity.ok(orderResponseMapper.toDto(order));
    }

    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @DeleteMapping(path = "{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable("orderId") Long orderId,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        orderService.remove(orderId, userDetails.getUsername());
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/findById/{id}")
    public ResponseEntity<OrderResponse> findById(@PathVariable Long id) {
        Optional<Order> order = orderService.findById(id);

        return order.map(orderResponseMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PreAuthorize("hasAnyRole('ADMIN','EXPERT','CUSTOMER')")
    @GetMapping("/findRelatedOrders/{id}")
    public List<OrderResponse> findRelatedOrders(@PathVariable Long id) {
        List<Order> orders = orderService.findRelatedOrders(id);
        return orders.stream()
                .map(orderResponseMapper::toDto)
                .collect(Collectors.toList());
    }
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @PostMapping("/changeOrderStatusToStarted")
    public ResponseEntity<?> changeOrderStatusToStarted(@Valid @RequestBody ChangeOrderStatusRequest request) {
        orderService.changeOrderStatusToStarted(request.orderId(), request.bidId());
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @PostMapping("/changeOrderStatusToDone")
    public ResponseEntity<?> changeOrderStatusToDone(@Valid @RequestBody ChangeOrderStatusRequest request) {
        orderService.changeOrderStatusToDone(request.orderId(), request.bidId());
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/payWithCredit/{id}")
    public ResponseEntity<?> payWithCredit(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        orderService.payWithCredit(id, userDetails.getUsername());
        return ResponseEntity.ok(HttpStatus.OK);
    }



    @RequestMapping("/captcha")
    public CaptchaResponse captcha() {
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        specCaptcha.setFont(new Font("Verdana", Font.PLAIN, 32));
        specCaptcha.setCharType(Captcha.TYPE_ONLY_NUMBER);
        int id = lastId.getAndIncrement();
        captchaMap.put(id, specCaptcha.text().toLowerCase());
        String base64 = specCaptcha.toBase64();
        return new CaptchaResponse(id, base64);
    }



    @PostMapping("/payOnline")
    public ResponseEntity<?> paymentPage(
            @Valid @RequestBody PaymentRequest request) {
        if (!captchaMap.getOrDefault(request.captchaId(), "").equals(request.captcha())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        captchaMap.remove(request.captchaId());
        orderService.payOnline(
                request.orderId(),
                request.price());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','EXPERT','CUSTOMER')")
    @GetMapping("/getOrderPrice")
    public Long getOrderPrice(@RequestParam Long orderId){
        return orderService.getOrderPriceById(orderId);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/findOrderByCustomerIdAndStatus/{id}")
    public List<OrderResponse> findOrderByCustomerIdAndStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus orderStatus) {
        List<Order> orders = orderService.findOrderByCustomerIdAndStatus(id, orderStatus);
        return orders.stream()
                .map(orderResponseMapper::toDto)
                .collect(Collectors.toList());
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/findOrderByExpertId/{id}")
    public List<OrderResponse> findOrderByExpertId(@PathVariable Long id) {
        List<Order> orders = orderService.findOrderByExpertId(id);
        return orders.stream()
                .map(orderResponseMapper::toDto)
                .collect(Collectors.toList());
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/findCustomerOrdersWithinTimeRange")
    public List<OrderResponse> findCustomerOrdersWithinTimeRange(@RequestBody TimeRangeRequest timeRangeRequest) {
        List<Order> orders = orderService.findCustomerOrdersWithinTimeRange(
                timeRangeRequest.startTime(),
                timeRangeRequest.endTime()
        );
        return orders.stream()
                .map(orderResponseMapper::toDto)
                .collect(Collectors.toList());
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/findCustomerOrdersWithStatus")
    public List<OrderResponse> findCustomerOrdersWithStatus(@RequestParam OrderStatus orderStatus) {
        List<Order> orders = orderService.findCustomerOrdersWithStatus(
                orderStatus
        );
        return orders.stream()
                .map(orderResponseMapper::toDto)
                .collect(Collectors.toList());
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/findOrderByServiceId")
    public List<OrderResponse> findOrderByServiceId(@RequestParam Set<Long> serviceId) {
        List<Order> orders = orderService.findOrderByServiceId(
                serviceId
        );
        return orders.stream()
                .map(orderResponseMapper::toDto)
                .collect(Collectors.toList());
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/findOrderBySubServiceId")
    public List<OrderResponse> findOrderBySubServiceId(@RequestParam Set<Long> serviceId) {
        List<Order> orders = orderService.findOrderBySubServiceId(
                serviceId
        );
        return orders.stream()
                .map(orderResponseMapper::toDto)
                .collect(Collectors.toList());
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/countOrdersByCustomerId/{id}")
    public ResponseEntity<?> countOrdersByCustomerId(@PathVariable Long id) {
        Long ordersCount = orderService.countOrdersByCustomerId(id);
        return ResponseEntity.ok(ordersCount);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/countOrdersByExpertId/{id}")
    public ResponseEntity<?> countOrdersByExpertId(@PathVariable Long id) {
        Long ordersCount = orderService.countOrdersByExpertId(id);
        return ResponseEntity.ok(ordersCount);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/findCustomerOrders")
    public List<OrderResponse> findCustomerOrders(@AuthenticationPrincipal UserDetails userDetails) {
        List<Order> orders = orderService.findCustomerOrders(userDetails.getUsername());
        return orders.stream()
                .map(orderResponseMapper::toDto)
                .collect(Collectors.toList());
    }
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/findCustomerOrdersByOrderStatus/")
    public List<OrderResponse> findCustomerOrdersByOrderStatus(
            @RequestParam OrderStatus orderStatus,
            @AuthenticationPrincipal UserDetails userDetails) {
        List<Order> orders = orderService.findCustomerOrdersByOrderStatus(userDetails.getUsername(), orderStatus);
        return orders.stream()
                .map(orderResponseMapper::toDto)
                .collect(Collectors.toList());
    }
    @PreAuthorize("hasRole('EXPERT')")
    @GetMapping("/findExpertOrdersByOrderStatus/")
    public List<OrderResponse> findExpertOrdersByOrderStatus(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam OrderStatus orderStatus) {
        List<Order> orders = orderService.findExpertOrdersByOrderStatus(userDetails.getUsername(), orderStatus);
        return orders.stream()
                .map(orderResponseMapper::toDto)
                .collect(Collectors.toList());
    }
}
