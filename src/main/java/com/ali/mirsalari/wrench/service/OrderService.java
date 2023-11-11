package com.ali.mirsalari.wrench.service;

import com.ali.mirsalari.wrench.entity.Order;
import com.ali.mirsalari.wrench.entity.enumeration.OrderStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public interface OrderService{
    Order save(String description, Long suggestedPrice, String address, Long serviceId, String email);
    Order update(Long id, String description, Long suggestedPrice, String address, Long serviceId, String email);
    Order updateWithEntity(Order order);

    void remove(Long id, String email);

    Order findById(Long id);

    List<Order> findAll();

    List<Order> findRelatedOrders(Long expertId);


    void changeOrderStatusToStarted(Long orderId, Long bidId);

    void changeOrderStatusToDone(Long orderId, Long bidId);

    void payWithCredit(Long orderId, String email);

    void payOnline(Long orderId, Long price);

    Long getOrderPriceById(Long orderId);

    List<Order> findOrderByCustomerIdAndStatus(Long customerId, OrderStatus orderStatus);

    List<Order> findOrderByExpertId(Long id);

    List<Order> findCustomerOrdersWithinTimeRange(Instant startTime, Instant endTime);

    List<Order> findCustomerOrdersWithStatus(OrderStatus orderStatus);

    List<Order> findOrderByServiceId(Set<Long> serviceId);

    List<Order> findOrderBySubServiceId(Set<Long> serviceId);

    Long countOrdersByCustomerId(Long id);

    Long countOrdersByExpertId(Long id);

    List<Order> findCustomerOrders(String email);

    List<Order> findCustomerOrdersByOrderStatus(String email, OrderStatus orderStatus);

    List<Order> findExpertOrdersByOrderStatus(String email, OrderStatus orderStatus);

}
