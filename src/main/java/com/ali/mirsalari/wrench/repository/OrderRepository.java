package com.ali.mirsalari.wrench.repository;

import com.ali.mirsalari.wrench.entity.Order;
import com.ali.mirsalari.wrench.entity.Service;
import com.ali.mirsalari.wrench.entity.enumeration.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Repository
public interface OrderRepository
        extends JpaRepository<Order, Long> {

    @Query("""
                 FROM Order o WHERE o.service IN :expertSkills
                 AND o.orderStatus IN :orderStatuses
            """)
    List<Order> findRelatedOrders(Set<Service> expertSkills, EnumSet<OrderStatus> orderStatuses);

    @Query("""
                SELECT o
                FROM Order o
                WHERE o.customer.id = :id
                AND o.orderStatus = :orderStatus
            """)
    List<Order> findOrderByCustomerIdAndStatus(Long id, OrderStatus orderStatus);

    @Query("""
                SELECT o
                FROM Order o
                WHERE EXISTS (
                    SELECT b
                    FROM Bid b
                    WHERE b.expert.id = :expertId
                    AND b.selectionDate IS NOT NULL
                    AND b.order = o
                )
            """)
    List<Order> findOrderByExpertId(Long expertId);

    @Query("SELECT o FROM Order o WHERE o.dateOfExecution BETWEEN :startTime AND :endTime")
    List<Order> findCustomerOrdersWithinTimeRange(@Param("startTime") Instant startTime, @Param("endTime") Instant endTime);

    List<Order> findOrderByOrderStatus(OrderStatus orderStatus);

    @Query("""
                 FROM Order o WHERE o.service.serviceParent.id IN :serviceId
            """)
    List<Order> findOrderByServiceId(Set<Long> serviceId);

    @Query("""
                 FROM Order o WHERE o.service.id IN :serviceId
            """)
    List<Order> findOrderBySubServiceId(Set<Long> serviceId);

    Long countOrdersByCustomerId(Long customerId);

    @Query("""
                SELECT COUNT(o)
                FROM Order o
                WHERE EXISTS (
                    SELECT b
                    FROM Bid b
                    WHERE b.expert.id = :expertId
                    AND b.selectionDate IS NOT NULL
                    AND b.order = o
                )
            """)
    Long countOrdersByExpertId(Long expertId);

    List<Order> findOrderByCustomerId(Long id);

    @Query("""
                        SELECT o
                        FROM Order o
                        WHERE o.customer.id = :customerId
                        AND o.orderStatus = :orderStatus
            """)
    List<Order> findCustomerOrdersByOrderStatus(Long customerId, OrderStatus orderStatus);

    @Query("""
                SELECT o
                FROM Order o
                WHERE EXISTS (
                    SELECT b
                    FROM Bid b
                    WHERE b.expert.id = :expertId
                    AND b.selectionDate IS NOT NULL
                    AND b.order = o
                    AND o.orderStatus = :orderStatus
                )
            """)
    List<Order> findExpertOrdersByOrderStatus(Long expertId, OrderStatus orderStatus);

}
