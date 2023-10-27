package com.ali.mirsalari.wrench.service;

import com.ali.mirsalari.wrench.entity.Admin;
import com.ali.mirsalari.wrench.service.base.BaseUserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface AdminService
        extends BaseUserService<Admin, String> {
    @Transactional
    Admin save(String firstName,
               String lastName,
               String email,
               String password);

    @Transactional
    Admin update(Long id,
                 String firstName,
                 String lastName,
                 String email,
                 String password);

    @Transactional
    Admin uncheckedUpdate(Admin admin);

    @Transactional
    void remove(Long id);

    @Transactional
    Optional<Admin> findById(Long id);

    @Transactional
    List<Admin> findAll();
}
