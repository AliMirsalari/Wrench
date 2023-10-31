package com.ali.mirsalari.wrench.service.impl;

import com.ali.mirsalari.wrench.entity.Bid;
import com.ali.mirsalari.wrench.entity.Expert;
import com.ali.mirsalari.wrench.entity.Order;
import com.ali.mirsalari.wrench.entity.enumeration.OrderStatus;
import com.ali.mirsalari.wrench.exception.*;
import com.ali.mirsalari.wrench.repository.BidRepository;
import com.ali.mirsalari.wrench.repository.ExpertRepository;
import com.ali.mirsalari.wrench.repository.OrderRepository;
import com.ali.mirsalari.wrench.service.BidService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {
    private final BidRepository bidRepository;
    private final ExpertRepository expertRepository;
    private final OrderRepository orderRepository;

    @Override
    public Bid save(Long suggestedPrice, Instant startTime, Instant endTime, Long expertId, Long orderId) {
        Expert expert = expertRepository.findById(expertId).orElseThrow(() -> new NotFoundException("Expert is not found!"));
        if (expert.getScore()<0){
            throw new DeactivatedAccountException("Account is deactivated due to score less than zero!");
        }
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order is not found!" ));
        validation(suggestedPrice, startTime, expert, order);
        Bid bid = new Bid(suggestedPrice, startTime, endTime, expert, order);
        bidRepository.save(bid);
        if (order.getOrderStatus().equals(OrderStatus.WAITING_FOR_THE_SUGGESTION_OF_EXPERTS)){
            order.setOrderStatus(OrderStatus.WAITING_FOR_EXPERT_SELECTION);
            orderRepository.save(order);
        }
        return bid;
    }

    @Override
    public Bid update(Long id, Long suggestedPrice, Instant startTime, Instant endTime, Long expertId, Long orderId) {
        Expert expert = expertRepository.findById(expertId).orElseThrow(() -> new NotFoundException("Expert is not found!"));
        if (expert.getScore()<0){
            throw new DeactivatedAccountException("Account is deactivated due to score less than zero!");
        }
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order is not found!" ));
        validation(suggestedPrice, startTime, expert, order);
        if (id == null || bidRepository.findById(id).isEmpty()) {
            throw new NotFoundException("Bid with id: " + id + " is not found.");
        }
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
    public void remove(Long id) {
        bidRepository.deleteById(id);
    }

    @Override
    public Optional<Bid> findById(Long id) {
        return bidRepository.findById(id);
    }

    @Override
    public List<Bid> findAll() {
        return bidRepository.findAll();
    }

    @Override
    public List<Bid> findRelatedBidsOrderByPriceAsc(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order is not found!"));
        return bidRepository.findRelatedBidsOrderByPriceAsc(order);
    }

    @Override
    public List<Bid> findRelatedBidsOrderByPriceDesc(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order is not found!"));
        return bidRepository.findRelatedBidsOrderByPriceDesc(order);
    }

    @Override
    public List<Bid> findRelatedBidsOrderByScoreAsc(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order is not found!"));
        return bidRepository.findRelatedBidsOrderByScoreAsc(order);
    }
    @Override
    public List<Bid> findRelatedBidsOrderByScoreDesc(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order is not found!"));
        return bidRepository.findRelatedBidsOrderByScoreDesc(order);
    }
    @Override
    public void selectBid(Long bidId) {
        Bid bid = findById(bidId).orElseThrow(()-> new NotFoundException("Bid is not found!"));
        if (findSelectedBid(bid.getOrder().getId()).isPresent()) {
            throw new DuplicateException("You already have chosen a bid!");
        }
        bid.setSelectionDate(Instant.now());
        updateWithEntity(bid);
        bid.getOrder().setOrderStatus(OrderStatus.WAITING_FOR_THE_EXPERT_TO_COME_TO_YOUR_PLACE);
        orderRepository.save(bid.getOrder());
    }

    @Override
    public Optional<Bid> findSelectedBid(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order is not found!"));
        return bidRepository.findSelectedBid(order);
    }

    private static void validation(Long suggestedPrice, Instant startTime, Expert expert, Order order) {
        if (!expert.getSkills().contains(order.getService())){
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
