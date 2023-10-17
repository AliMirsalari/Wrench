package com.ali.mirsalari.wrench.service.impl;

import com.ali.mirsalari.wrench.entity.Order;
import com.ali.mirsalari.wrench.entity.Service;
import com.ali.mirsalari.wrench.entity.enumeration.OrderStatus;
import com.ali.mirsalari.wrench.exception.NotFoundException;
import com.ali.mirsalari.wrench.exception.NotValidPriceException;
import com.ali.mirsalari.wrench.exception.NotValidServiceException;
import com.ali.mirsalari.wrench.exception.NotValidTimeException;
import com.ali.mirsalari.wrench.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderServiceImpl underTest;
    private Order order;
    private Service it;
    private Service computer;

    @BeforeEach
    void setUp() {
        order = new Order(
                "It is a mock order",
                1_000_000L,
                Instant.now(),
                "Tehran",
                null
        );
        it = new Service("IT");
        computer = new Service(7L,
                "IT",
                100L,
                "IT services",
                it,
                null,
                null);
    }


    @Test
    void itShouldThrowNotValidServiceException() {
        //Arrange
        Service service = new Service();
        order.setService(service);
        //Act and Assert
        assertThrows(NotValidServiceException.class, () -> {
            underTest.save(order);
        });
    }
    @Test
    void itShouldThrowNotValidPriceExceptionInSaveMethod() {
        order.setService(computer);
        order.setSuggestedPrice(99L);
        //Act and Assert
        assertThrows(NotValidPriceException.class, () -> {
            underTest.save(order);
        });
    }
    @Test
    void itShouldThrowNotValidTimeExceptionInSaveMethod() {
        order.setService(computer);
        order.setDateOfExecution(Instant.now().minusSeconds(10000));
        //Act and Assert
        assertThrows(NotValidTimeException.class, () -> {
            underTest.save(order);
        });
    }

    @Test
    void itShouldUpdateTheOrderStatus() {
        //Arrange
        order.setService(computer);
        //Act
        underTest.save(order);
        //Assert
        assertEquals(OrderStatus.WAITING_FOR_THE_SUGGESTION_OF_EXPERTS, order.getOrderStatus());
    }

    @Test
    void itShouldThrowNotFoundException() {
//        //Arrange
        order.setId(null);
        //Act and Assert
        assertThrows(NotFoundException.class, () -> {
            underTest.update(order);
        });
    }
    @Test
    void itShouldThrowNotValidPriceExceptionInUpdateMethod() {
        //Arrange
        when(orderRepository.findById(any())).thenReturn(Optional.ofNullable(order));
        order.setService(computer);
        order.setId(1L);
        order.setSuggestedPrice(99L);
        //Act and Assert
        assertThrows(NotValidPriceException.class, () -> {
            underTest.update(order);
        });
    }
    @Test
    void itShouldThrowNotValidTimeExceptionInUpdateMethod() {
        //Arrange
        when(orderRepository.findById(any())).thenReturn(Optional.ofNullable(order));
        order.setId(1L);
        order.setService(computer);
        order.setDateOfExecution(Instant.now().minusSeconds(10000));
        //Act and Assert
        assertThrows(NotValidTimeException.class, () -> {
            underTest.update(order);
        });
    }

    @Test
    void itShouldUpdateAnOrder() {
        //Arrange
        when(orderRepository.findById(any())).thenReturn(Optional.ofNullable(order));
        when(orderRepository.save(any())).thenReturn(order);
        order.setId(1L);
        order.setService(computer);
        //Act
        Order tempOrder = underTest.update(order);
        //Assert
        assertNotNull(tempOrder);
    }

    @Test
    void itShouldDeleteOrderById() {
        //Act
        underTest.remove(1L);
        //Assert
        verify(orderRepository, times(1)).deleteById(any());
    }
    @Test
    void itShouldFindAOrderById() {
        //Arrange
        when(orderRepository.findById(any())).thenReturn(Optional.ofNullable(order));
        //Act and Assert
        if (underTest.findById(1L).isPresent()) {
            Order tempOrder = underTest.findById(1L).get();
            assertNotNull(tempOrder);
        }
    }
    @Test
    void itShouldFindOrders() {
        //Arrange
        when(orderRepository.findAll()).thenReturn(List.of(order,order,order));
        //Act
        List<Order> orders = underTest.findAll();
        //Assert
        assertNotNull(orders);
    }
}