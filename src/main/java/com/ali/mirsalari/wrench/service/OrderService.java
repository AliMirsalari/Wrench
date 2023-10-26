package com.ali.mirsalari.wrench.service;

import com.ali.mirsalari.wrench.entity.Bid;
import com.ali.mirsalari.wrench.entity.Expert;
import com.ali.mirsalari.wrench.entity.Order;
import com.ali.mirsalari.wrench.service.base.CrudService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface OrderService{
    Order save(String description, Long suggestedPrice, String address, Long serviceId);
    Order update(Long id, String description, Long suggestedPrice, String address, Long serviceId);
    Order uncheckedUpdate(Order order);

    void remove(Long id);

    Optional<Order> findById(Long id);

    List<Order> findAll();

    List<Order> findRelatedOrders(Long expertId);


    void changeOrderStatusToStarted(Long orderId, Long bidId);

    void changeOrderStatusToDone(Long orderId, Long bidId);

    void changeOrderStatusToPaid(Long orderId);
}
