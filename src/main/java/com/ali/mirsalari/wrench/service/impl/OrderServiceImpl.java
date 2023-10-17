package com.ali.mirsalari.wrench.service.impl;

import com.ali.mirsalari.wrench.entity.Bid;
import com.ali.mirsalari.wrench.entity.Expert;
import com.ali.mirsalari.wrench.entity.Order;
import com.ali.mirsalari.wrench.entity.enumeration.OrderStatus;
import com.ali.mirsalari.wrench.exception.NotFoundException;
import com.ali.mirsalari.wrench.exception.NotValidPriceException;
import com.ali.mirsalari.wrench.exception.NotValidServiceException;
import com.ali.mirsalari.wrench.exception.NotValidTimeException;
import com.ali.mirsalari.wrench.repository.OrderRepository;
import com.ali.mirsalari.wrench.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public Order save(Order order) {
        if (order.getService().getServiceParent() == null) {
            throw new NotValidServiceException("Invalid Service!");
        }
        if (order.getSuggestedPrice() < order.getService().getBasePrice()) {
            throw new NotValidPriceException("Invalid price!");
        }
        if (order.getDateOfExecution().isBefore(Instant.now().minusSeconds(5))) {
            throw new NotValidTimeException("Order execution date cannot be in the past.");
        }

        order.setOrderStatus(OrderStatus.WAITING_FOR_THE_SUGGESTION_OF_EXPERTS);
        return orderRepository.save(order);

    }
    @Override
    @Transactional
    public Order update(Order order) {
        if (order.getId() == null || orderRepository.findById(order.getId()).isEmpty()) {
            throw new NotFoundException("Order with id: " + order.getId() + " is not found.");
        }
        if (order.getSuggestedPrice() < order.getService().getBasePrice()) {
            throw new NotValidPriceException("Invalid price!");
        }
        if (order.getDateOfExecution().isBefore(Instant.now().minusSeconds(5))) {
            throw new NotValidTimeException("Order execution date cannot be in the past.");
        }
        return orderRepository.save(order);
    }
    @Override
    @Transactional
    public Order uncheckedUpdate(Order order) {
        return  orderRepository.save(order);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        orderRepository.deleteById(id);
    }
    @Override
    @Transactional
    public Optional<Order> findById(Long id) {

        return orderRepository.findById(id);

    }

    @Override
    @Transactional
    public List<Order> findAll() {

        return orderRepository.findAll();
    }

    @Override
    @Transactional
    public List<Order> findRelatedOrders(Expert expert) {
        EnumSet<OrderStatus> orderStatuses =
                EnumSet.of(
                        OrderStatus.WAITING_FOR_THE_SUGGESTION_OF_EXPERTS,
                        OrderStatus.WAITING_FOR_EXPERT_SELECTION);

        return orderRepository.findRelatedOrders(expert.getSkills(), orderStatuses);
    }

    @Override
    @Transactional
    public void changeOrderStatusToStarted(Order order, Bid bid) {
        if (order.getOrderStatus() != OrderStatus.WAITING_FOR_THE_EXPERT_TO_COME_TO_YOUR_PLACE){
            throw new IllegalStateException("You can not change the order status");
        }
        if (bid.getStartTime().isAfter(Instant.now().minusSeconds(5))){
            throw new NotValidTimeException("You can not start it before the suggested start time");
        }
        order.setOrderStatus(OrderStatus.STARTED);
        uncheckedUpdate(order);
    }

    @Override
    @Transactional
    public void changeOrderStatusToDone(Order order) {
        if (order.getOrderStatus() != OrderStatus.STARTED){
            throw new IllegalStateException("You can not change the order status");
        }
        order.setOrderStatus(OrderStatus.DONE);
        uncheckedUpdate(order);
    }
}
