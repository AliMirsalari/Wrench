package com.ali.mirsalari.wrench.controller;

import com.ali.mirsalari.wrench.entity.Order;
import com.ali.mirsalari.wrench.service.OrderService;
import com.ali.mirsalari.wrench.service.dto.ChangeOrderStatusRequest;
import com.ali.mirsalari.wrench.service.dto.OrderResponse;
import com.ali.mirsalari.wrench.service.dto.RegisterOrderRequest;
import com.ali.mirsalari.wrench.service.mapper.OrderResponseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/order")
public class OrderController {
    private final OrderResponseMapper orderResponseMapper;
    private final OrderService orderService;

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
                request.serviceId());
        return ResponseEntity.ok(orderResponseMapper.toDto(order));
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<?> updateOrder(@Valid
                                         @PathVariable("id") Long id,
                                         @RequestBody RegisterOrderRequest request) {
        Order order = orderService.update(
                id,
                request.description(),
                request.suggestedPrice(),
                request.address(),
                request.serviceId());
        return ResponseEntity.ok(orderResponseMapper.toDto(order));
    }

    @DeleteMapping(path = "{orderId}")
    public HttpStatus deleteOrder(@PathVariable("orderId") Long orderId) {
        orderService.remove(orderId);
        return HttpStatus.OK;
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
    public HttpStatus changeOrderStatusToStarted(@RequestBody ChangeOrderStatusRequest request) {
        orderService.changeOrderStatusToStarted(request.orderId(), request.bidId());
        return HttpStatus.OK;
    }

    @PostMapping("/changeOrderStatusToDone")
    public HttpStatus changeOrderStatusToDone(@RequestBody ChangeOrderStatusRequest request) {
        orderService.changeOrderStatusToDone(request.orderId(), request.bidId());
        return HttpStatus.OK;
    }

    @GetMapping("/changeOrderStatusToPaid/{id}")
    public HttpStatus changeOrderStatusToPaid(@PathVariable Long id) {
        orderService.changeOrderStatusToPaid(id);
        return HttpStatus.OK;
    }
}
