package com.ali.mirsalari.wrench.service;

import com.ali.mirsalari.wrench.entity.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface OrderService{
    Order save(String description, Long suggestedPrice, String address, Long serviceId, Long customerId);
    Order update(Long id, String description, Long suggestedPrice, String address, Long serviceId, Long customerId);
    Order updateWithEntity(Order order);

    void remove(Long id);

    Optional<Order> findById(Long id);

    List<Order> findAll();

    List<Order> findRelatedOrders(Long expertId);


    void changeOrderStatusToStarted(Long orderId, Long bidId);

    void changeOrderStatusToDone(Long orderId, Long bidId);

    void payWithCredit(Long orderId);

    void payOnline(Long orderId, Long price);

    Long getOrderPriceById(Long orderId);
}
