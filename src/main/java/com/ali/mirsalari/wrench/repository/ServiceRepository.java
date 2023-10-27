package com.ali.mirsalari.wrench.repository;

import com.ali.mirsalari.wrench.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository
        extends JpaRepository<Service,Long> {
    @Query("SELECT s FROM Service s WHERE s.serviceParent IS NULL")
    List<Service> findAllServices ();
    @Query("SELECT s FROM Service s WHERE s.serviceParent IS NOT NULL")
    List<Service> findAllSubservices ();
    Optional<Service> findServiceByName(String name);

}
