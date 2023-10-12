package com.ali.mirsalari.wrench.service.impl;

import com.ali.mirsalari.wrench.entity.Bid;
import com.ali.mirsalari.wrench.exception.NotFoundException;
import com.ali.mirsalari.wrench.repository.BidRepository;
import com.ali.mirsalari.wrench.service.BidService;
import jakarta.persistence.EntityTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {
    private final BidRepository bidRepository;

    @Override
    @Transactional
    public Bid save(Bid bid) {
        return bidRepository.save(bid);
    }

    @Override
    @Transactional
    public Bid update(Bid bid) throws NotFoundException {
        if (bid.getId() == null || bidRepository.findById(bid.getId()) == null) {
            throw new NotFoundException("Bid with id: " + bid.getId() + " is not found.");
        }
        return bidRepository.save(bid);
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
}
