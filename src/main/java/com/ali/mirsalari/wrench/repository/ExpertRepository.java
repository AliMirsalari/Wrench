package com.ali.mirsalari.wrench.repository;

import com.ali.mirsalari.wrench.entity.Expert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpertRepository
        extends JpaRepository<Expert,Long> {
    Optional<Expert> findExpertByEmail(String email);

}
