package com.ali.mirsalari.wrench.service.impl;

import com.ali.mirsalari.wrench.entity.Admin;
import com.ali.mirsalari.wrench.exception.NotFoundException;
import com.ali.mirsalari.wrench.exception.NotValidPasswordException;
import com.ali.mirsalari.wrench.repository.AdminRepository;
import com.ali.mirsalari.wrench.service.AdminService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
@Service
@Transactional
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Admin save(String firstName,
                      String lastName,
                      String email,
                      String password){
        Admin admin = new Admin(firstName, lastName, email, passwordEncoder.encode(password));
        return adminRepository.save(admin);
    }

    @Override
    public Admin update(String firstName,
                        String lastName,
                        String email,
                        String password,
                        UserDetails userDetails) {
        Admin admin = findByEmail(userDetails.getUsername());
        admin.setFirstName(firstName);
        admin.setLastName(lastName);
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(password));
        return adminRepository.save(admin);
    }
    @Override
    public Admin updateWithEntity(Admin admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return adminRepository.save(admin);
    }
    @Override
    public void remove(String email) {
        adminRepository.deleteByEmail(email);
    }

    @Override
    public Admin findById(Long id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Admin with ID " + id + " is not found."));
    }

    @Override
    public Admin findByEmail(String email) {
        return adminRepository.findAdminByEmail(email)
                .orElseThrow(() -> new NotFoundException("Admin with Email " + email + " is not found."));

    }


    @Override
    public List<Admin> findAll() {
        return adminRepository.findAll();
    }

    @Override
    public Admin changePassword(String newPassword, String oldPassword, String email) {
        Admin admin = findByEmail(email);
        if (!Objects.equals(admin.getPassword(), oldPassword)) {
            throw new NotValidPasswordException("The entered password is not the same as the password!");
        }
        admin.setPassword(passwordEncoder.encode(newPassword));
        return updateWithEntity(admin);
    }
}
