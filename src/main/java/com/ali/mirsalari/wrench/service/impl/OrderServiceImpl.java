package com.ali.mirsalari.wrench.service.impl;

import com.ali.mirsalari.wrench.entity.Bid;
import com.ali.mirsalari.wrench.entity.Expert;
import com.ali.mirsalari.wrench.entity.Order;
import com.ali.mirsalari.wrench.entity.enumeration.OrderStatus;
import com.ali.mirsalari.wrench.exception.NotFoundException;
import com.ali.mirsalari.wrench.exception.NotValidPriceException;
import com.ali.mirsalari.wrench.exception.NotValidServiceException;
import com.ali.mirsalari.wrench.exception.NotValidTimeException;
import com.ali.mirsalari.wrench.repository.BidRepository;
import com.ali.mirsalari.wrench.repository.ExpertRepository;
import com.ali.mirsalari.wrench.repository.OrderRepository;
import com.ali.mirsalari.wrench.repository.ServiceRepository;
import com.ali.mirsalari.wrench.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ExpertRepository expertRepository;
    private final ServiceRepository serviceRepository;
    private final BidRepository bidRepository;

    @Override
    public Order save(String description, Long suggestedPrice, String address, Long serviceId) {
        com.ali.mirsalari.wrench.entity.Service service =
                serviceRepository.findById(serviceId)
                        .orElseThrow(()->new NotFoundException("Service is not found!"));
        Order order = new Order(description, suggestedPrice, Instant.now(), address,service);
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
    public Order update(Long id, String description, Long suggestedPrice, String address, Long serviceId) {
        com.ali.mirsalari.wrench.entity.Service service =
                serviceRepository.findById(serviceId)
                        .orElseThrow(()->new NotFoundException("Service is not found!"));
        Order order = findById(id).orElseThrow(()->new NotFoundException("Order is not found!"));
        order.setDescription(description);
        order.setSuggestedPrice(suggestedPrice);
        order.setAddress(address);
        order.setService(service);
        order.setDateOfExecution(Instant.now());
        if (order.getSuggestedPrice() < order.getService().getBasePrice()) {
            throw new NotValidPriceException("Invalid price!");
        }
        if (order.getDateOfExecution().isBefore(Instant.now().minusSeconds(5))) {
            throw new NotValidTimeException("Order execution date cannot be in the past.");
        }
        return orderRepository.save(order);
    }
    @Override
    public Order uncheckedUpdate(Order order) {
        return  orderRepository.save(order);
    }

    @Override
    public void remove(Long id) {
        orderRepository.deleteById(id);
    }
    @Override
    public Optional<Order> findById(Long id) {

        return orderRepository.findById(id);

    }

    @Override
    public List<Order> findAll() {

        return orderRepository.findAll();
    }

    @Override
    public List<Order> findRelatedOrders(Long expertId) {
        Expert expert = expertRepository
                .findById(expertId).orElseThrow(()->new NotFoundException("Expert is not found!"));

        EnumSet<OrderStatus> orderStatuses =
                EnumSet.of(
                        OrderStatus.WAITING_FOR_THE_SUGGESTION_OF_EXPERTS,
                        OrderStatus.WAITING_FOR_EXPERT_SELECTION);

        return orderRepository.findRelatedOrders(expert.getSkills(), orderStatuses);
    }

    @Override
    public void changeOrderStatusToStarted(Long orderId, Long bidId) {
        Order order = findById(orderId).orElseThrow(()->new NotFoundException("Order is not found!"));
        Bid bid = bidRepository.findById(bidId).orElseThrow(()->new NotFoundException("Bid is not found!"));

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
    public void changeOrderStatusToDone(Long orderId, Long bidId) {
        Order order = findById(orderId).orElseThrow(()->new NotFoundException("Order is not found!"));
        Bid bid = bidRepository.findById(bidId).orElseThrow(()->new NotFoundException("Bid is not found!"));

        if (order.getOrderStatus() != OrderStatus.STARTED){
            throw new IllegalStateException("You can not change the order status");
        }
        order.setOrderStatus(OrderStatus.DONE);
        uncheckedUpdate(order);
        if (bid.getEndTime().isBefore(Instant.now())) {
            int hoursBefore = (int) Duration.between(bid.getEndTime(), Instant.now()).toHours();
            int score = bid.getExpert().getScore();
            bid.getExpert().setScore((score-hoursBefore));
            expertRepository.save(bid.getExpert());
        }
    }
    @Override
    public void changeOrderStatusToPaid(Long orderId) {
        Order order = findById(orderId).orElseThrow(()->new NotFoundException("Order is not found!"));
        if (order.getOrderStatus() != OrderStatus.DONE){
            throw new IllegalStateException("You can not change the order status");
        }
        //TODO: open payment method
        order.setOrderStatus(OrderStatus.PAID);
        uncheckedUpdate(order);
    }
}
