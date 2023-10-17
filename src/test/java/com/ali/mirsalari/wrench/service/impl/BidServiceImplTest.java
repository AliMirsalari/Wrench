package com.ali.mirsalari.wrench.service.impl;

import com.ali.mirsalari.wrench.entity.Bid;
import com.ali.mirsalari.wrench.entity.Expert;
import com.ali.mirsalari.wrench.entity.Order;
import com.ali.mirsalari.wrench.entity.Service;
import com.ali.mirsalari.wrench.entity.enumeration.OrderStatus;
import com.ali.mirsalari.wrench.exception.DuplicateException;
import com.ali.mirsalari.wrench.exception.NotFoundException;
import com.ali.mirsalari.wrench.exception.NotValidPriceException;
import com.ali.mirsalari.wrench.exception.NotValidTimeException;
import com.ali.mirsalari.wrench.repository.BidRepository;
import com.ali.mirsalari.wrench.service.ExpertService;
import com.ali.mirsalari.wrench.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BidServiceImplTest {

    @Mock
    private BidRepository bidRepository;
    @Mock
    private ExpertService expertService;
    @Mock
    private OrderService orderService;


    @InjectMocks
    private BidServiceImpl underTest;
    private Bid bid;
    private Expert expert;
    private Service service;
    private Service otherService;
    private Order order;

    @BeforeEach
    void setUp() {


        expert = new Expert(
                "Hashem",
                "Momen",
                "hashem@gmail.com",
                "4s6]5rf4FSDF#%#",
                0L,
                Instant.now(),
                0,
                new HashSet<>(),
                null,
                null
        );
        service = new Service(7L,
                "IT",
                1_000_000L,
                "IT services",
                null,
                null,
                null);
        Set<Service> services = expert.getSkills();
        services.add(service);
        expert.setSkills(services);
        otherService = new Service(8L,
                "Repairing",
                1_000_000L,
                "Reparing services",
                null,
                null,
                null);
        order = new Order(
                "It is a mock order",
                1_000_000L,
                Instant.now(),
                "Tehran",
                service
        );
        bid = new Bid(
                Instant.now(),
                9_000_000L,
                Instant.now().plus(Duration.ofDays(2)),
                Instant.now().plus(Duration.ofDays(10)),
                expert,
                order
        );
    }

    @Test
    void itShouldThrowNotFoundExceptionWhenExpertIsNotFound() {
        Bid tempBid = new Bid(
                Instant.now(),
                9_000_000L,
                Instant.now().plus(Duration.ofDays(2)),
                Instant.now().plus(Duration.ofDays(10)),
                new Expert(),
                order
        );
        //Arrange
        when(expertService.findById(any())).thenReturn(Optional.empty());
        //Act and Assert
        assertThrows(NotFoundException.class, () -> {
            underTest.save(tempBid);
        });
    }
    @Test
    void itShouldThrowIllegalArgumentExceptionWhenCategoryIsWrong() {
        //Arrange
        when(expertService.findById(any())).thenReturn(Optional.ofNullable(expert));
        order.setService(otherService);
        //Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            underTest.save(bid);
        });
    }

    @Test
    void itShouldThrowIllegalArgumentExceptionWhenOrderStatusIsWrong() {
        //Arrange
        when(expertService.findById(any())).thenReturn(Optional.ofNullable(expert));
        order.setOrderStatus(OrderStatus.DONE);
        //Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            underTest.save(bid);
        });
    }

    @Test
    void itShouldThrowNotValidPriceExceptionWhenSuggestedPriceIsLessThanBasePrice() {
        //Arrange
        when(expertService.findById(any())).thenReturn(Optional.ofNullable(expert));
        order.setOrderStatus(OrderStatus.WAITING_FOR_THE_SUGGESTION_OF_EXPERTS);
        bid.setSuggestedPrice(0L);
        //Act and Assert
        assertThrows(NotValidPriceException.class, () -> {
            underTest.save(bid);
        });
    }

    @Test
    void itShouldThrowNotValidTimeException() {
        //Arrange
        when(expertService.findById(any())).thenReturn(Optional.ofNullable(expert));
        order.setOrderStatus(OrderStatus.WAITING_FOR_THE_SUGGESTION_OF_EXPERTS);
        bid.setStartTime(Instant.now().minus(Duration.ofDays(1)));
        //Act and Assert
        assertThrows(NotValidTimeException.class, () -> {
            underTest.save(bid);
        });
    }

    @Test
    void itShouldSaveBid() {
        //Arrange
        when(expertService.findById(any())).thenReturn(Optional.ofNullable(expert));
        when(orderService.uncheckedUpdate(any())).thenReturn(order);
        order.setOrderStatus(OrderStatus.WAITING_FOR_THE_SUGGESTION_OF_EXPERTS);
        //Act
        underTest.save(bid);
        //Assert
        verify(bidRepository, times(1)).save(any());
    }

    @Test
    void itShouldThrowNotFoundException() {
//        //Arrange
        bid.setId(null);
        //Act and Assert
        assertThrows(NotFoundException.class, () -> {
            underTest.update(bid);
        });
    }
    @Test
    void itShouldUpdateBid() {
        //Arrange
        when(bidRepository.save(any())).thenReturn(bid);
        when(bidRepository.findById(any())).thenReturn(Optional.ofNullable(bid));
        bid.setId(1L);
        //Act
        Bid tempBid = underTest.update(bid);
        //Assert
        assertNotNull(tempBid);
    }
    @Test
    void itShouldUpdateBidWithoutChecking() {
        //Arrange
        when(bidRepository.save(any())).thenReturn(bid);
        //Act
        Bid tempBid = underTest.uncheckedUpdate(bid);
        //Assert
        assertNotNull(tempBid);
    }
    @Test
    void itShouldDeleteBidById() {
        //Act
        underTest.remove(1L);
        //Assert
        verify(bidRepository, times(1)).deleteById(any());
    }
    @Test
    void itShouldFindABidById() {
        //Arrange
        when(bidRepository.findById(any())).thenReturn(Optional.ofNullable(bid));
        //Act and Assert
        if (underTest.findById(1L).isPresent()) {
            Bid tempBid = underTest.findById(1L).get();
            assertNotNull(tempBid);
        }
    }
    @Test
    void itShouldFindBids() {
        //Arrange
        when(bidRepository.findAll()).thenReturn(List.of(bid,bid,bid));
        //Act
        List<Bid> bids = underTest.findAll();
        //Assert
        assertNotNull(bids);
    }
    @Test
    void itShouldFindRelatedBidsOrderByPriceAsc() {
        //Arrange
        when(bidRepository.findRelatedBidsOrderByPriceAsc(any())).thenReturn(List.of(bid,bid,bid));
        //Act
        List<Bid> bids = underTest.findRelatedBidsOrderByPriceAsc(order);
        //Assert
        assertNotNull(bids);
    }
    @Test
    void itShouldFindRelatedBidsOrderByPriceDesc() {
        //Arrange
        when(bidRepository.findRelatedBidsOrderByPriceDesc(any())).thenReturn(List.of(bid,bid,bid));
        //Act
        List<Bid> bids = underTest.findRelatedBidsOrderByPriceDesc(order);
        //Assert
        assertNotNull(bids);
    }
    @Test
    void itShouldFindRelatedBidsOrderByScoreAsc() {
        //Arrange
        when(bidRepository.findRelatedBidsOrderByScoreAsc(any())).thenReturn(List.of(bid,bid,bid));
        //Act
        List<Bid> bids = underTest.findRelatedBidsOrderByScoreAsc(order);
        //Assert
        assertNotNull(bids);
    }
    @Test
    void itShouldFindRelatedBidsOrderByScoreDesc() {
        //Arrange
        when(bidRepository.findRelatedBidsOrderByScoreDesc(any())).thenReturn(List.of(bid,bid,bid));
        //Act
        List<Bid> bids = underTest.findRelatedBidsOrderByScoreDesc(order);
        //Assert
        assertNotNull(bids);
    }

    @Test
    void itShouldThrowDuplicateException() {
        //Arrange
        when(underTest.findSelectedBid(any())).thenReturn(Optional.ofNullable(bid));
        //Act and Assert
        assertThrows(DuplicateException.class, () -> {
            underTest.selectBid(bid);
        });
    }

    @Test
    void itShouldSelectBid() {
        //Arrange
        when(underTest.findSelectedBid(any())).thenReturn(Optional.empty());
        //Act
        underTest.selectBid(bid);
        //Assert
        verify(orderService, times(1)).uncheckedUpdate(any());

    }
}