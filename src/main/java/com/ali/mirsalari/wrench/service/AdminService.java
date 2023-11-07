package com.ali.mirsalari.wrench.service;

import com.ali.mirsalari.wrench.entity.Admin;
import com.ali.mirsalari.wrench.service.base.BaseUserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface AdminService
        extends BaseUserService<Admin, String> {
    Admin save(String firstName,
               String lastName,
               String email,
               String password);

    Admin update(String firstName,
                 String lastName,
                 String email,
                 String password,
                 UserDetails userDetails);

    Admin updateWithEntity(Admin admin);

    void remove(String Email);

    Optional<Admin> findById(Long id);

    List<Admin> findAll();
}
