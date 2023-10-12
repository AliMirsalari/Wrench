package com.ali.mirsalari.wrench.service;

import com.ali.mirsalari.wrench.entity.Order;
import com.ali.mirsalari.wrench.service.base.CrudService;
import org.springframework.stereotype.Service;

@Service
public interface OrderService
        extends CrudService<Order, Long> {
}
