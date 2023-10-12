package com.ali.mirsalari.wrench.service;

import com.ali.mirsalari.wrench.entity.Customer;
import com.ali.mirsalari.wrench.service.base.CrudService;
import com.ali.mirsalari.wrench.service.base.UserService;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService
        extends CrudService<Customer, Long>,
        UserService<Customer, String> {
}
