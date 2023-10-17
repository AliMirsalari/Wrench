package com.ali.mirsalari.wrench.repository;

import com.ali.mirsalari.wrench.entity.Order;
import com.ali.mirsalari.wrench.entity.Service;
import com.ali.mirsalari.wrench.entity.enumeration.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Repository
public interface OrderRepository
        extends JpaRepository<Order,Long> {

    @Query("""
from Order o where o.service in :expertSkills
and o.orderStatus in :orderStatuses""")
    List<Order> findRelatedOrders(Set<Service> expertSkills, EnumSet<OrderStatus> orderStatuses);


}
