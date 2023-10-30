package com.ali.mirsalari.wrench.service.impl;

import com.ali.mirsalari.wrench.entity.Admin;
import com.ali.mirsalari.wrench.exception.NotFoundException;
import com.ali.mirsalari.wrench.exception.NotValidPasswordException;
import com.ali.mirsalari.wrench.repository.AdminRepository;
import com.ali.mirsalari.wrench.service.AdminService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Service
@Transactional
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;


    @Override
    public Admin save(String firstName,
                      String lastName,
                      String email,
                      String password){
        Admin admin = new Admin(firstName, lastName, email, password);
        return adminRepository.save(admin);
    }

    @Override
    public Admin update(Long id,
                        String firstName,
                        String lastName,
                        String email,
                        String password) {
        Admin admin = findById(id).orElseThrow(()->new NotFoundException("Admin is not found!"));
        admin.setFirstName(firstName);
        admin.setLastName(lastName);
        admin.setEmail(email);
        admin.setPassword(password);
        return adminRepository.save(admin);
    }
    @Override
    public Admin updateWithEntity(Admin admin) {
        return  adminRepository.save(admin);
    }
    @Override
    public void remove(Long id) {
        adminRepository.deleteById(id);
    }

    @Override
    public Optional<Admin> findById(Long id) {
        return adminRepository.findById(id);
    }

    @Override
    public Optional<Admin> findByEmail(String email) {
        return adminRepository.findAdminByEmail(email);
    }


    @Override
    public List<Admin> findAll() {
        return adminRepository.findAll();
    }

    @Override
    public Admin changePassword(String newPassword, String oldPassword, Long userId) {
        Admin admin = adminRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Admin with ID " + userId + " is not found."));
        if (!Objects.equals(admin.getPassword(), oldPassword)) {
            throw new NotValidPasswordException("The entered password is not the same as the password!");
        }
        admin.setPassword(newPassword);
        return updateWithEntity(admin);
    }
}
