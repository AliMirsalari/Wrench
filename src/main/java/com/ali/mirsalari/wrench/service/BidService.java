package com.ali.mirsalari.wrench.service;

import com.ali.mirsalari.wrench.entity.Bid;
import com.ali.mirsalari.wrench.service.base.CrudService;
import org.springframework.stereotype.Service;

@Service
public interface BidService
        extends CrudService<Bid,Long> {
}
