package com.ali.mirsalari.wrench.service.impl;

import com.ali.mirsalari.wrench.entity.*;
import com.ali.mirsalari.wrench.entity.enumeration.OrderStatus;
import com.ali.mirsalari.wrench.exception.NotFoundException;
import com.ali.mirsalari.wrench.exception.NotValidPriceException;
import com.ali.mirsalari.wrench.exception.NotValidServiceException;
import com.ali.mirsalari.wrench.exception.NotValidTimeException;
import com.ali.mirsalari.wrench.repository.*;
import com.ali.mirsalari.wrench.service.CustomerService;
import com.ali.mirsalari.wrench.service.ExpertService;
import com.ali.mirsalari.wrench.service.OrderService;
import com.ali.mirsalari.wrench.service.ServiceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ExpertService expertService;
    private final ServiceService serviceService;
    private final BidRepository bidRepository;
    private final CustomerService customerService;
    private final UserRepository userRepository;

    @Override
    public Order save(String description, Long suggestedPrice, String address, Long serviceId, String email) {
        com.ali.mirsalari.wrench.entity.Service service =
                serviceService.findById(serviceId);
        Customer customer = customerService
                .findByEmail(email);
        Order order = new Order(description, suggestedPrice, Instant.now(), address, service, customer);
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
    public Order update(Long id, String description, Long suggestedPrice, String address, Long serviceId, String email) {
        com.ali.mirsalari.wrench.entity.Service service =
                serviceService.findById(serviceId);
        Customer customer = customerService
                .findByEmail(email);
        Order order = findById(id);
        if (order.getCustomer() != customer) {
            throw new IllegalStateException("You can not edit this Order!");
        }
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
    public Order updateWithEntity(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public void remove(Long id, String email) {
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new IllegalStateException("User is not found!"));
        if (Objects.equals(user.getUserType(), "ADMIN")) {
            orderRepository.deleteById(id);
            return;
        }
        Customer customer = customerService
                .findByEmail(email);
        Order order = orderRepository.findById(id).orElseThrow(() -> new IllegalStateException("Order is not found!"));
        if (order.getCustomer() !=customer){
            throw new IllegalStateException("You can not remove this order!");
        }
        orderRepository.deleteById(id);
    }

    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order with ID " + id + " is not found!"));
    }

    @Override
    public List<Order> findAll() {

        return orderRepository.findAll();
    }

    @Override
    public List<Order> findRelatedOrders(Long expertId) {
        Expert expert = expertService
                .findById(expertId);

        EnumSet<OrderStatus> orderStatuses =
                EnumSet.of(
                        OrderStatus.WAITING_FOR_THE_SUGGESTION_OF_EXPERTS,
                        OrderStatus.WAITING_FOR_EXPERT_SELECTION);

        return orderRepository.findRelatedOrders(expert.getSkills(), orderStatuses);
    }

    @Override
    public void changeOrderStatusToStarted(Long orderId, Long bidId) {
        Order order = findById(orderId);
        Bid bid = bidRepository.findById(bidId).orElseThrow(() -> new NotFoundException("Bid is not found!"));

        if (order.getOrderStatus() != OrderStatus.WAITING_FOR_THE_EXPERT_TO_COME_TO_YOUR_PLACE) {
            throw new IllegalStateException("You can not change the order status");
        }
        if (bid.getStartTime().isAfter(Instant.now().minusSeconds(5))) {
            throw new NotValidTimeException("You can not start it before the suggested start time");
        }
        order.setOrderStatus(OrderStatus.STARTED);
        updateWithEntity(order);
    }

    @Override
    public void changeOrderStatusToDone(Long orderId, Long bidId) {
        Order order = findById(orderId);
        Bid bid = bidRepository.findById(bidId).orElseThrow(() -> new NotFoundException("Bid is not found!"));

        if (order.getOrderStatus() != OrderStatus.STARTED) {
            throw new IllegalStateException("You can not change the order status");
        }
        order.setOrderStatus(OrderStatus.DONE);
        updateWithEntity(order);
        Expert expert = bid.getExpert();
        if (bid.getEndTime().isBefore(Instant.now())) {
            int hoursBefore = (int) Duration.between(bid.getEndTime(), Instant.now()).toHours();
            int score = expert.getScore();
            expert.setScore((score - hoursBefore));
            expertService.updateWithEntity(expert);
        }
        if (expert.getScore() < 0) {
            expert.setActive(false);
            expertService.updateWithEntity(expert);
        }
    }

    @Override
    public void payWithCredit(Long orderId, String email) {
        Order order = findById(orderId);
        if (order.getOrderStatus() != OrderStatus.DONE) {
            throw new IllegalStateException("You can not change the order status");
        }
        Bid selectedBid = bidRepository.findSelectedBid(order).orElseThrow(() -> new NotFoundException("Bid is not found!"));
        Long price = selectedBid.getSuggestedPrice();
        Expert expert = selectedBid.getExpert();
        Customer customer = customerService
                .findByEmail(email);
        if (!Objects.equals(customer.getId(), order.getCustomer().getId())){
            throw new IllegalStateException("You can pay other people's orders!'");
        }
        if (customer.getCredit() < price) {
            throw new IllegalStateException("Credit is not enough to pay the order, please use another way!");
        }
        customer.setCredit(customer.getCredit() - price);
        expert.setCredit(expert.getCredit() + price);
        customerService.updateWithEntity(customer);
        expertService.updateWithEntity(expert);
        order.setOrderStatus(OrderStatus.PAID);
        updateWithEntity(order);
    }

    @Override
    public void payOnline(Long orderId, Long price) {
        Order order = findById(orderId);
        if (order.getOrderStatus() != OrderStatus.DONE) {
            throw new IllegalStateException("You can not change the order status");
        }
        Bid selectedBid = bidRepository.findSelectedBid(order).orElseThrow(() -> new NotFoundException("Bid is not found!"));
        Long suggestedPrice = selectedBid.getSuggestedPrice();
        if ((suggestedPrice) < price) {
            throw new IllegalStateException("The amount of money you paid is less than the suggested price!");
        }
        Expert expert = selectedBid.getExpert();
        expert.setCredit((long) (expert.getCredit() + (price * 0.7)));
        expertService.updateWithEntity(expert);
        order.setOrderStatus(OrderStatus.PAID);
        updateWithEntity(order);
    }

    @Override
    public Long getOrderPriceById(Long orderId) {
        Bid bid = orderRepository.findById(orderId)
                .map(order -> bidRepository.findSelectedBid(order)
                        .orElseThrow(() -> new NotFoundException("No such a Bid!")))
                .orElseThrow(() -> new NotFoundException("No such an Order!"));
        return bid.getSuggestedPrice();
    }

    public List<Order> findOrderByCustomerIdAndStatus(Long customerId, OrderStatus orderStatus) {
        customerService
                .findById(customerId);
        return orderRepository.findOrderByCustomerIdAndStatus(customerId, orderStatus);
    }

    @Override
    public List<Order> findOrderByExpertId(Long expertId) {
        expertService
                .findById(expertId);
        return orderRepository.findOrderByExpertId(expertId);
    }

    @Override
    public List<Order> findCustomerOrdersWithinTimeRange(Instant startTime, Instant endTime) {
        return orderRepository.findCustomerOrdersWithinTimeRange(startTime, endTime);
    }

    @Override
    public List<Order> findCustomerOrdersWithStatus(OrderStatus orderStatus) {
        return orderRepository.findOrderByOrderStatus(orderStatus);
    }
    @Override
    public List<Order> findOrderByServiceId(Set<Long> serviceId) {
        return orderRepository.findOrderByServiceId(serviceId);
    }
    @Override
    public List<Order> findOrderBySubServiceId(Set<Long> serviceId) {
        return orderRepository.findOrderBySubServiceId(serviceId);
    }

    @Override
    public Long countOrdersByCustomerId(Long id) {
        return orderRepository.countOrdersByCustomerId(id);
    }

    @Override
    public Long countOrdersByExpertId(Long id) {
        return orderRepository.countOrdersByExpertId(id);
    }

    @Override
    public List<Order> findCustomerOrders(String email) {
        Customer customer = customerService
                .findByEmail(email);
        return orderRepository.findOrderByCustomerId(customer.getId());
    }
    public List<Order> findCustomerOrdersByOrderStatus(String email, OrderStatus orderStatus) {
        Customer customer = customerService
                .findByEmail(email);
        return orderRepository.findCustomerOrdersByOrderStatus(customer.getId(), orderStatus);
    }

    public List<Order> findExpertOrdersByOrderStatus(String email, OrderStatus orderStatus) {
       Expert expert = expertService
                .findByEmail(email);
        return orderRepository.findExpertOrdersByOrderStatus(expert.getId(), orderStatus);
    }
}
