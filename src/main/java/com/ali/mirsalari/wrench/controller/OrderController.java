package com.ali.mirsalari.wrench.controller;

import com.ali.mirsalari.wrench.controller.dto.*;
import com.ali.mirsalari.wrench.controller.mapper.OrderResponseMapper;
import com.ali.mirsalari.wrench.entity.Order;
import com.ali.mirsalari.wrench.service.OrderService;
import io.springboot.captcha.SpecCaptcha;
import io.springboot.captcha.base.Captcha;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    @GetMapping
    public List<OrderResponse> getOrders() {
        List<Order> orders = orderService.findAll();
        return orders.stream()
                .map(orderResponseMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<?> registerOrder(@Valid @RequestBody RegisterOrderRequest request) {
        Order order = orderService.save(
                request.description(),
                request.suggestedPrice(),
                request.address(),
                request.serviceId(),
                request.customerId());
        return ResponseEntity.ok(orderResponseMapper.toDto(order));
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<?> updateOrder(
                                         @PathVariable("id") Long id,
                                         @Valid @RequestBody RegisterOrderRequest request) {
        Order order = orderService.update(
                id,
                request.description(),
                request.suggestedPrice(),
                request.address(),
                request.serviceId(),
                request.customerId());
        return ResponseEntity.ok(orderResponseMapper.toDto(order));
    }

    @DeleteMapping(path = "{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable("orderId") Long orderId) {
        orderService.remove(orderId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<OrderResponse> findById(@PathVariable Long id) {
        Optional<Order> order = orderService.findById(id);

        return order.map(orderResponseMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/findRelatedOrders/{id}")
    public List<OrderResponse> findRelatedOrders(@PathVariable Long id) {
        List<Order> orders = orderService.findRelatedOrders(id);
        return orders.stream()
                .map(orderResponseMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/changeOrderStatusToStarted")
    public ResponseEntity<?> changeOrderStatusToStarted(@Valid @RequestBody ChangeOrderStatusRequest request) {
        orderService.changeOrderStatusToStarted(request.orderId(), request.bidId());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/changeOrderStatusToDone")
    public ResponseEntity<?> changeOrderStatusToDone(@Valid @RequestBody ChangeOrderStatusRequest request) {
        orderService.changeOrderStatusToDone(request.orderId(), request.bidId());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/payWithCredit/{id}")
    public ResponseEntity<?> payWithCredit(
            @PathVariable Long id) {
        orderService.payWithCredit(id);
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

    @GetMapping("/getOrderPrice")
    public Long getOrderPrice(@RequestParam Long orderId){
        return orderService.getOrderPriceById(orderId);
    }

}
