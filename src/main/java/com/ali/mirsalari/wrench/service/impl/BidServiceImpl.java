package com.ali.mirsalari.wrench.service.impl;

import com.ali.mirsalari.wrench.entity.Bid;
import com.ali.mirsalari.wrench.entity.Expert;
import com.ali.mirsalari.wrench.entity.Order;
import com.ali.mirsalari.wrench.entity.enumeration.OrderStatus;
import com.ali.mirsalari.wrench.exception.DuplicateException;
import com.ali.mirsalari.wrench.exception.NotFoundException;
import com.ali.mirsalari.wrench.exception.NotValidPriceException;
import com.ali.mirsalari.wrench.exception.NotValidTimeException;
import com.ali.mirsalari.wrench.repository.BidRepository;
import com.ali.mirsalari.wrench.service.BidService;
import com.ali.mirsalari.wrench.service.ExpertService;
import com.ali.mirsalari.wrench.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {
    private final BidRepository bidRepository;
    private final ExpertService expertService;
    private final OrderService orderService;

    @Override
    @Transactional
    public Bid save(Bid bid) {
        if (expertService.findById(bid.getExpert().getId()).isEmpty()){
            throw new NotFoundException("Expert is not found!");
        }
        if (!bid.getExpert().getSkills().contains(bid.getOrder().getService())){
            throw new IllegalArgumentException("You can not place a bid in this category!");
        }
        if (!(bid.getOrder().getOrderStatus().equals(OrderStatus.WAITING_FOR_THE_SUGGESTION_OF_EXPERTS) ||
                bid.getOrder().getOrderStatus().equals(OrderStatus.WAITING_FOR_EXPERT_SELECTION))) {
            throw new IllegalArgumentException("You cannot place a bid in this order!");
        }
        if (bid.getSuggestedPrice() < bid.getOrder().getService().getBasePrice()) {
            throw new NotValidPriceException("Invalid price!");
        }
        if (bid.getStartTime().isBefore(Instant.now().minusSeconds(5))) {
            throw new NotValidTimeException("Bid's starting time cannot be in the past.");
        }
        bidRepository.save(bid);
        if (bid.getOrder().getOrderStatus().equals(OrderStatus.WAITING_FOR_THE_SUGGESTION_OF_EXPERTS)){
            bid.getOrder().setOrderStatus(OrderStatus.WAITING_FOR_EXPERT_SELECTION);
            orderService.uncheckedUpdate(bid.getOrder());
        }
        return bid;
    }

    @Override
    @Transactional
    public Bid update(Bid bid) {
        if (bid.getId() == null || bidRepository.findById(bid.getId()).isEmpty()) {
            throw new NotFoundException("Bid with id: " + bid.getId() + " is not found.");
        }
        return bidRepository.save(bid);
    }
    @Override
    @Transactional
    public Bid uncheckedUpdate(Bid bid) {
        return  bidRepository.save(bid);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        bidRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<Bid> findById(Long id) {
        return bidRepository.findById(id);
    }

    @Override
    @Transactional
    public List<Bid> findAll() {
        return bidRepository.findAll();
    }

    @Override
    @Transactional
    public List<Bid> findRelatedBidsOrderByPriceAsc(Order order) {
        return bidRepository.findRelatedBidsOrderByPriceAsc(order);
    }

    @Override
    @Transactional
    public List<Bid> findRelatedBidsOrderByPriceDesc(Order order) {
        return bidRepository.findRelatedBidsOrderByPriceDesc(order);
    }
    @Override
    @Transactional
    public List<Bid> findRelatedBidsOrderByScoreAsc(Order order) {
        return bidRepository.findRelatedBidsOrderByScoreAsc(order);
    }
    @Override
    @Transactional
    public List<Bid> findRelatedBidsOrderByScoreDesc(Order order) {
        return bidRepository.findRelatedBidsOrderByScoreDesc(order);
    }

    @Override
    @Transactional
    public void selectBid(Bid bid) {
        if (findSelectedBid(bid.getOrder()).isPresent()) {
            throw new DuplicateException("You already have chosen a bid!");
        }
        bid.setSelectionDate(Instant.now());
        uncheckedUpdate(bid);
        bid.getOrder().setOrderStatus(OrderStatus.WAITING_FOR_THE_EXPERT_TO_COME_TO_YOUR_PLACE);
        orderService.uncheckedUpdate(bid.getOrder());
    }

    @Override
    @Transactional
    public Optional<Bid> findSelectedBid(Order order) {
        return bidRepository.findSelectedBid(order);
    }
}
