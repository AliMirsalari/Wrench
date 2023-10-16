package com.ali.mirsalari.wrench.service.impl;

import com.ali.mirsalari.wrench.entity.Bid;
import com.ali.mirsalari.wrench.exception.NotFoundException;
import com.ali.mirsalari.wrench.repository.BidRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BidServiceImplTest {

    @Mock
    private BidRepository bidRepository;

    @InjectMocks
    private BidServiceImpl underTest;
    private Bid bid;

    @BeforeEach
    void setUp() {
        bid = new Bid(
                Instant.now(),
                9_000_000L,
                Instant.now().plus(Duration.ofDays(2)),
                Instant.now().plus(Duration.ofDays(10)),
                null
        );
    }

    @Test
    void itShouldSaveABid() {
        //Arrange
        when(bidRepository.save(any())).thenReturn(bid);
        //Act
        Bid tempBid = underTest.save(bid);
        //Assert
        assertNotNull(tempBid);
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
    void itShouldUpdateAnBid() {
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
    void itShouldDeleteBidById() {
        //Act
        underTest.remove(1L);
        //Assert
        verify(bidRepository, times(1)).deleteById(1L);
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
}