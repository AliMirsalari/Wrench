package com.ali.mirsalari.wrench.service.impl;

import com.ali.mirsalari.wrench.entity.Admin;
import com.ali.mirsalari.wrench.exception.DuplicateException;
import com.ali.mirsalari.wrench.exception.NotFoundException;
import com.ali.mirsalari.wrench.exception.NotValidEmailException;
import com.ali.mirsalari.wrench.exception.NotValidPasswordException;
import com.ali.mirsalari.wrench.repository.AdminRepository;
import com.ali.mirsalari.wrench.service.AdminService;
import com.ali.mirsalari.wrench.util.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;


    @Override
    @Transactional
    public Admin save(Admin admin){
        validation(admin);
        return adminRepository.save(admin);
    }

    @Override
    @Transactional
    public Admin update(Admin admin) {
        validation(admin);
        return adminRepository.save(admin);
    }
    @Override
    @Transactional
    public Admin uncheckedUpdate(Admin admin) {
        return  adminRepository.save(admin);
    }
    @Override
    @Transactional
    public void remove(Long id) {
        adminRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<Admin> findById(Long id) {
        return adminRepository.findById(id);
    }

    @Override
    @Transactional
    public Optional<Admin> findByEmail(String email) {
        return adminRepository.findAdminByEmail(email);
    }


    @Override
    @Transactional
    public List<Admin> findAll() {
        return adminRepository.findAll();
    }

    @Override
    @Transactional
    public Admin changePassword(String newPassword, Admin admin){
        if (admin.getId() == null || adminRepository.findById(admin.getId()).isEmpty()) {
            throw new NotFoundException("Admin with id: " + admin.getId() + " is not found.");
        }
        if (!Validator.isValidPassword(newPassword)) {
            throw new NotValidPasswordException("Password is not valid!");
        }
        admin.setPassword(newPassword);
        return update(admin);
    }

    @Override
    @Transactional
    public void validation(Admin admin){
        if (findByEmail(admin.getEmail()).isPresent()) {
            throw new DuplicateException("Email already exists");
        }
        if (!Validator.isValidPassword(admin.getPassword())) {
            throw new NotValidPasswordException("Password is not good!");
        }
        if (!Validator.isValidEmail(admin.getEmail())) {
            throw new NotValidEmailException("Email is not good!");
        }
    }
}
