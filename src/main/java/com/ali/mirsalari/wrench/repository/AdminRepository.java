package com.ali.mirsalari.wrench.repository;

import com.ali.mirsalari.wrench.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository
        extends JpaRepository<Admin,Long> {
    void deleteByEmail(String email);
    Optional<Admin> findAdminByEmail(String email);
}
