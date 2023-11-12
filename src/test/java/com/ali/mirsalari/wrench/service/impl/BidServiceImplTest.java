package com.ali.mirsalari.wrench.service.impl;

import com.ali.mirsalari.wrench.entity.*;
import com.ali.mirsalari.wrench.entity.enumeration.OrderStatus;
import com.ali.mirsalari.wrench.exception.DeactivatedAccountException;
import com.ali.mirsalari.wrench.exception.DuplicateException;
import com.ali.mirsalari.wrench.exception.NotValidPriceException;
import com.ali.mirsalari.wrench.exception.NotValidTimeException;
import com.ali.mirsalari.wrench.repository.BidRepository;
import com.ali.mirsalari.wrench.service.ExpertService;
import com.ali.mirsalari.wrench.service.OrderService;
import com.ali.mirsalari.wrench.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
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
    @Mock
    private UserService userService;
    @InjectMocks
    private BidServiceImpl underTest;

    private Long suggestedPrice;
    private Instant startTime;
    private Instant endTime;
    private Long expertId;
    private Long orderId;
    private Expert expert;

    private Service skill;
    private Order order;
    private Bid bid;

    @BeforeEach
    void setUp() {
        suggestedPrice = 1_000_000L;
        startTime = Instant.parse("2023-11-22T12:00:00Z");
        endTime = Instant.parse("2023-12-22T14:00:00Z");
        expertId = 1L;
        orderId = 152L;
        Service service = new Service(1L, "Laptop", null, null, null, null, null);
        skill = new Service(2L, "hardware repair",
                100_000L,
                "Repair hardware of your laptop",
                service,
                null,
                null);
        Service subService = new Service(3L, "software repair",
                100_000L,
                "Repair software of your laptop",
                service,
                null,
                null);
        expert = new Expert();
        expert.setScore(10);
        expert.setActive(true);
        expert.setSkills(Set.of(skill));
        order = new Order();
        order.setService(subService);
        bid = new Bid(suggestedPrice, startTime, endTime, expert, order);
    }

    @Test
    void itShouldSaveThrowDeactivatedAccountExceptionWhileSavingBid() {
        //Arrange
        expert.setScore(-10);
        when(expertService.findById(any())).thenReturn(expert);
        //Act and Assert
        assertThrows(DeactivatedAccountException.class, () -> underTest.save(suggestedPrice, startTime, endTime, expertId, orderId));
    }

    @Test
    void itShouldThrowIllegalArgumentExceptionWhenSkillAndServiceAreNotTheSame() {
        //Arrange
        when(expertService.findById(any())).thenReturn(expert);
        when(orderService.findById(any())).thenReturn(order);
        //Act and Assert
        assertThrows(IllegalArgumentException.class, () -> underTest.save(suggestedPrice, startTime, endTime, expertId, orderId));
    }

    @Test
    void itShouldThrowIllegalArgumentExceptionWhenOrderStatusIsUnexpected() {
        //Arrange
        order.setOrderStatus(OrderStatus.DONE);
        order.setService(skill);
        when(expertService.findById(any())).thenReturn(expert);
        when(orderService.findById(any())).thenReturn(order);
        //Act and Assert
        assertThrows(IllegalArgumentException.class, () -> underTest.save(suggestedPrice, startTime, endTime, expertId, orderId));
    }

    @Test
    void itShouldThrowNotValidPriceException() {
        //Arrange
        order.setOrderStatus(OrderStatus.WAITING_FOR_THE_SUGGESTION_OF_EXPERTS);
        order.setService(skill);
        suggestedPrice = 10L;
        when(expertService.findById(any())).thenReturn(expert);
        when(orderService.findById(any())).thenReturn(order);
        //Act and Assert
        assertThrows(NotValidPriceException.class, () -> underTest.save(suggestedPrice, startTime, endTime, expertId, orderId));
    }

    @Test
    void itShouldThrowNotValidTimeException() {
        //Arrange
        order.setOrderStatus(OrderStatus.WAITING_FOR_THE_SUGGESTION_OF_EXPERTS);
        order.setService(skill);
        startTime = Instant.now().minusSeconds(100);
        when(expertService.findById(any())).thenReturn(expert);
        when(orderService.findById(any())).thenReturn(order);
        //Act and Assert
        assertThrows(NotValidTimeException.class, () -> underTest.save(suggestedPrice, startTime, endTime, expertId, orderId));
    }

    @Test
    void itShouldSaveBid() {
        //Arrange
        order.setOrderStatus(OrderStatus.WAITING_FOR_THE_SUGGESTION_OF_EXPERTS);
        order.setService(skill);
        startTime = Instant.now().plusSeconds(100);
        when(expertService.findById(any())).thenReturn(expert);
        when(orderService.findById(any())).thenReturn(order);
        //Act
        Bid savedBid = underTest.save(suggestedPrice, startTime, endTime, expertId, orderId);
        //Assert
        verify(bidRepository, times(1)).save(any());
        verify(orderService, times(1)).updateWithEntity(any());
        assertNotNull(savedBid);
    }

    @Test
    void itShouldThrowDeactivatedAccountExceptionWhileUpdatingBid() {
        //Arrange
        expert.setScore(-10);
        when(expertService.findByEmail(any())).thenReturn(expert);
        String email = "alimirsalari@outlook.com";
        //Act and Assert
        assertThrows(DeactivatedAccountException.class, () -> underTest.update(1L, suggestedPrice, startTime, endTime, orderId, email));
    }

    @Test
    void itShouldThrowIllegalArgumentExceptionWhileUpdatingBid() {
        //Arrange
        when(expertService.findByEmail(any())).thenReturn(expert);
        when(bidRepository.findById(any())).thenReturn(Optional.ofNullable(bid));
        String email = "alimirsalari@outlook.com";
        //Act and Assert
        assertThrows(IllegalArgumentException.class, () -> underTest.update(1L, suggestedPrice, startTime, endTime, orderId, email));
    }

    @Test
    void itShouldUpdateBid() {
        //Arrange
        expert.setBids(Set.of(bid));
        order.setOrderStatus(OrderStatus.WAITING_FOR_THE_SUGGESTION_OF_EXPERTS);
        order.setService(skill);
        when(expertService.findByEmail(any())).thenReturn(expert);
        when(bidRepository.findById(any())).thenReturn(Optional.ofNullable(bid));
        when(orderService.findById(any())).thenReturn(order);
        String email = "alimirsalari@outlook.com";
        long newSuggestedPrice = 2_000_000L;
        Instant newStartTime = Instant.parse("2024-11-22T12:00:00Z");
        Instant newEndTime = Instant.parse("2024-12-22T14:00:00Z");
        //Act
        underTest.update(1L, newSuggestedPrice, newStartTime, newEndTime, orderId, email);
        //Assert
        verify(bidRepository).save(any());
    }

    @Test
    void itShouldUpdateBidWithEntity() {
        //Arrange
        when(bidRepository.save(any())).thenReturn(bid);
        //Act
        underTest.updateWithEntity(bid);
        //Assert
        verify(bidRepository, times(1)).save(any());
    }

    @Test
    void itShouldRemoveBidIfRequestIsFromAdmin() {
        //Arrange
        String email = "alimirsalari@outlook.com";
        User user = new User("Seyyed Ali",
                "Mirsalari",
                email,
                "45fsd%#HG",
                100_000L);
        user.setUserType("ADMIN");
        when(userService.findByEmail(any())).thenReturn(user);
        //Act
        underTest.remove(1L, email);
        //Assert
        verify(bidRepository).deleteById(any());
    }

    @Test
    void itShouldThrowIllegalStateExceptionWhileDeletingBid() {
        //Arrange
        String email = "alimirsalari@outlook.com";
        User user = new User("Seyyed Ali",
                "Mirsalari",
                email,
                "45fsd%#HG",
                100_000L);
        user.setUserType("EXPERT");
        when(userService.findByEmail(any())).thenReturn(user);
        when(bidRepository.findById(any())).thenReturn(Optional.ofNullable(bid));
        when(expertService.findByEmail(any())).thenReturn(expert);
        //Act and Assert
        assertThrows(IllegalStateException.class, () -> underTest.remove(1L, email));
    }

    @Test
    void itShouldRemoveBidIfRequestIsFromExpert() {
        //Arrange
        String email = "alimirsalari@outlook.com";
        User user = new User("Seyyed Ali",
                "Mirsalari",
                email,
                "45fsd%#HG",
                100_000L);
        user.setUserType("EXPERT");
        expert.setBids(Set.of(bid));
        when(userService.findByEmail(any())).thenReturn(user);
        when(bidRepository.findById(any())).thenReturn(Optional.ofNullable(bid));
        when(expertService.findByEmail(any())).thenReturn(expert);
        //Act
        underTest.remove(1L, email);
        //Assert
        verify(bidRepository).deleteById(any());
    }

    @Test
    void itShouldFindAllBids() {
        //Arrange
        when(bidRepository.findAll()).thenReturn(List.of(bid));
        //Act
        underTest.findAll();
        //Assert
        verify(bidRepository, times(1)).findAll();
    }

    @Test
    void itShouldFindRelatedBidsOrderByPriceAsc() {
        //Arrange
        when(orderService.findById(any())).thenReturn(order);
        when(bidRepository.findRelatedBidsOrderByPriceAsc(any())).thenReturn(List.of(bid));
        //Act
        underTest.findRelatedBidsOrderByPriceAsc(1L);
        //Assert
        verify(bidRepository, times(1)).findRelatedBidsOrderByPriceAsc(any());
    }

    @Test
    void itShouldFindRelatedBidsOrderByPriceDesc() {
        //Arrange
        when(orderService.findById(any())).thenReturn(order);
        when(bidRepository.findRelatedBidsOrderByPriceDesc(any())).thenReturn(List.of(bid));
        //Act
        underTest.findRelatedBidsOrderByPriceDesc(1L);
        //Assert
        verify(bidRepository, times(1)).findRelatedBidsOrderByPriceDesc(any());
    }

    @Test
    void itShouldFindRelatedBidsOrderByScoreAsc() {
        //Arrange
        when(orderService.findById(any())).thenReturn(order);
        when(bidRepository.findRelatedBidsOrderByScoreAsc(any())).thenReturn(List.of(bid));
        //Act
        underTest.findRelatedBidsOrderByScoreAsc(1L);
        //Assert
        verify(bidRepository, times(1)).findRelatedBidsOrderByScoreAsc(any());
    }

    @Test
    void itShouldFindRelatedBidsOrderByScoreDesc() {
        //Arrange
        when(orderService.findById(any())).thenReturn(order);
        when(bidRepository.findRelatedBidsOrderByScoreDesc(any())).thenReturn(List.of(bid));
        //Act
        underTest.findRelatedBidsOrderByScoreDesc(1L);
        //Assert
        verify(bidRepository, times(1)).findRelatedBidsOrderByScoreDesc(any());
    }

    @Test
    void itShouldThrowDuplicateException() {
        //Arrange
        when(bidRepository.findById(any())).thenReturn(Optional.ofNullable(bid));
        when(bidRepository.findSelectedBid(any())).thenReturn(Optional.ofNullable(bid));
        //Act and Assert
        assertThrows(DuplicateException.class,
                () -> underTest.selectBid(1L));
    }

    @Test
    void itShouldSelectBid() {
        //Arrange
        when(bidRepository.findById(any())).thenReturn(Optional.ofNullable(bid));
        when(bidRepository.findSelectedBid(any())).thenReturn(Optional.empty());
        //Act
        underTest.selectBid(1L);
        //Assert
        verify(bidRepository).save(any());
        verify(orderService).updateWithEntity(any());
    }
}