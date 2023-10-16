package com.ali.mirsalari.wrench.service.impl;

import com.ali.mirsalari.wrench.entity.Order;
import com.ali.mirsalari.wrench.entity.enumeration.OrderStatus;
import com.ali.mirsalari.wrench.exception.NotFoundException;
import com.ali.mirsalari.wrench.exception.NotValidPriceException;
import com.ali.mirsalari.wrench.exception.NotValidServiceException;
import com.ali.mirsalari.wrench.exception.NotValidTimeException;
import com.ali.mirsalari.wrench.repository.OrderRepository;
import com.ali.mirsalari.wrench.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;


    @Override
    @Transactional
    public Order save(Order order) throws NotValidPriceException, NotValidTimeException {
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
    public Order update(Order order) throws NotFoundException, NotValidPriceException, NotValidTimeException {
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
}
