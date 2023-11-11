package com.ali.mirsalari.wrench.repository;

import com.ali.mirsalari.wrench.entity.UnapprovedEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UnapprovedEmailRepository extends
        JpaRepository<UnapprovedEmail, Long> {
    Optional<UnapprovedEmail> findUnapprovedEmailByEmail(String email);
    void  removeByEmail(String email);
}