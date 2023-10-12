package com.ali.mirsalari.wrench.service;

import com.ali.mirsalari.wrench.entity.Admin;
import com.ali.mirsalari.wrench.service.base.CrudService;
import com.ali.mirsalari.wrench.service.base.UserService;
import org.springframework.stereotype.Service;

@Service
public interface AdminService
        extends CrudService<Admin, Long>,
        UserService<Admin, String> {
}
