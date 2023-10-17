package com.ali.mirsalari.wrench.service;

import com.ali.mirsalari.wrench.entity.Bid;
import com.ali.mirsalari.wrench.entity.Expert;
import com.ali.mirsalari.wrench.entity.Order;
import com.ali.mirsalari.wrench.service.base.CrudService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService
        extends CrudService<Order, Long> {

    @Transactional
    List<Order> findRelatedOrders(Expert expert);

    @Transactional
    void changeOrderStatusToStarted(Order order, Bid bid);

    @Transactional
    void changeOrderStatusToDone(Order order);
}
