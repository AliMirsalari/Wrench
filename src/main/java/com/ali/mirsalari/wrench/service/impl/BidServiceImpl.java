package com.ali.mirsalari.wrench.service.impl;

import com.ali.mirsalari.wrench.entity.Bid;
import com.ali.mirsalari.wrench.entity.Expert;
import com.ali.mirsalari.wrench.entity.Order;
import com.ali.mirsalari.wrench.entity.User;
import com.ali.mirsalari.wrench.entity.enumeration.OrderStatus;
import com.ali.mirsalari.wrench.exception.*;
import com.ali.mirsalari.wrench.repository.BidRepository;
import com.ali.mirsalari.wrench.repository.UserRepository;
import com.ali.mirsalari.wrench.service.BidService;
import com.ali.mirsalari.wrench.service.ExpertService;
import com.ali.mirsalari.wrench.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {
    private final BidRepository bidRepository;
    private final ExpertService expertService;
    private final OrderService orderService;
    private final UserRepository userRepository;

    @Override
    public Bid save(Long suggestedPrice, Instant startTime, Instant endTime, Long expertId, Long orderId) {
        Expert expert = expertService.findById(expertId);
        if (expert.getScore() < 0 || !expert.isActive()) {
            throw new DeactivatedAccountException("Account is deactivated due to score less than zero!");
        }
        Order order = orderService.findById(orderId);
        validation(suggestedPrice, startTime, expert, order);
        Bid bid = new Bid(suggestedPrice, startTime, endTime, expert, order);
        bidRepository.save(bid);
        if (order.getOrderStatus().equals(OrderStatus.WAITING_FOR_THE_SUGGESTION_OF_EXPERTS)) {
            order.setOrderStatus(OrderStatus.WAITING_FOR_EXPERT_SELECTION);
            orderService.updateWithEntity(order);
        }
        return bid;
    }

    @Override
    public Bid update(Long id, Long suggestedPrice, Instant startTime, Instant endTime, Long orderId, String email) {
        Expert expert = expertService.findByEmail(email);
        if (expert.getScore() < 0 || !expert.isActive()) {
            throw new DeactivatedAccountException("Account is deactivated due to score less than zero!");
        }
        Bid bid = findById(id);
        if (!expert.getBids().contains(bid)){
            throw new IllegalArgumentException("You can not remove this bid!");
        }
        Order order = orderService.findById(orderId);
        validation(suggestedPrice, startTime, expert, order);
        return bidRepository.save(
                new Bid(
                        id,
                        suggestedPrice,
                        startTime,
                        endTime,
                        expert,
                        order
                ));
    }

    @Override
    public Bid updateWithEntity(Bid bid) {
        return bidRepository.save(bid);
    }

    @Override
    public void remove(Long id, String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new NotFoundException("User is not found!"));
        if (Objects.equals(user.getUserType(), "ADMIN")) {
            bidRepository.deleteById(id);
            return;
        }
        Bid bid = findById(id);
        Expert expert = expertService.findByEmail(email);
        if (!expert.getBids().contains(bid)) {
            throw new IllegalStateException("You can not remove this Bid!");
        }
        bidRepository.deleteById(id);
    }


    @Override
    public Bid findById(Long id) {
        return bidRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Bid with ID " + id + " is not found!"));
    }

    @Override
    public List<Bid> findAll() {
        return bidRepository.findAll();
    }

    @Override
    public List<Bid> findRelatedBidsOrderByPriceAsc(Long orderId) {
        Order order = orderService.findById(orderId);
        return bidRepository.findRelatedBidsOrderByPriceAsc(order);
    }

    @Override
    public List<Bid> findRelatedBidsOrderByPriceDesc(Long orderId) {
        Order order = orderService.findById(orderId);
        return bidRepository.findRelatedBidsOrderByPriceDesc(order);
    }

    @Override
    public List<Bid> findRelatedBidsOrderByScoreAsc(Long orderId) {
        Order order = orderService.findById(orderId);
        return bidRepository.findRelatedBidsOrderByScoreAsc(order);
    }

    @Override
    public List<Bid> findRelatedBidsOrderByScoreDesc(Long orderId) {
        Order order = orderService.findById(orderId);
        return bidRepository.findRelatedBidsOrderByScoreDesc(order);
    }

    @Override
    public void selectBid(Long bidId) {
        Bid bid = findById(bidId);
        if (findSelectedBid(bid.getOrder().getId()).isPresent()) {
            throw new DuplicateException("You already have chosen a bid!");
        }
        bid.setSelectionDate(Instant.now());
        updateWithEntity(bid);
        bid.getOrder().setOrderStatus(OrderStatus.WAITING_FOR_THE_EXPERT_TO_COME_TO_YOUR_PLACE);
        orderService.updateWithEntity(bid.getOrder());
    }

    @Override
    public Optional<Bid> findSelectedBid(Long orderId) {
        Order order = orderService.findById(orderId);
        return bidRepository.findSelectedBid(order);
    }

    private static void validation(Long suggestedPrice, Instant startTime, Expert expert, Order order) {
        if (!expert.getSkills().contains(order.getService())) {
            throw new IllegalArgumentException("You can not place a bid in this category!");
        }
        if (!(order.getOrderStatus().equals(OrderStatus.WAITING_FOR_THE_SUGGESTION_OF_EXPERTS) ||
                order.getOrderStatus().equals(OrderStatus.WAITING_FOR_EXPERT_SELECTION))) {
            throw new IllegalArgumentException("You cannot place a bid in this order!");
        }
        if (suggestedPrice < order.getService().getBasePrice()) {
            throw new NotValidPriceException("Invalid price!");
        }
        if (startTime.isBefore(Instant.now().minusSeconds(5))) {
            throw new NotValidTimeException("Bid's starting time cannot be in the past.");
        }
    }
}
