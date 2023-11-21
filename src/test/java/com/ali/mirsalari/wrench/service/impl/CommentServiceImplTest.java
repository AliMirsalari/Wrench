package com.ali.mirsalari.wrench.service.impl;

import com.ali.mirsalari.wrench.entity.Comment;
import com.ali.mirsalari.wrench.entity.Customer;
import com.ali.mirsalari.wrench.entity.Expert;
import com.ali.mirsalari.wrench.entity.User;
import com.ali.mirsalari.wrench.repository.CommentRepository;
import com.ali.mirsalari.wrench.service.CustomerService;
import com.ali.mirsalari.wrench.service.ExpertService;
import com.ali.mirsalari.wrench.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CustomerService customerService;
    @Mock
    private ExpertService expertService;
    @Mock
    private UserService userService;
    @InjectMocks
    private CommentServiceImpl underTest;

    private byte rate;
    private String verdict;
    private Long expertId;
    private String email;

    @BeforeEach
    void setUp() {
        rate = 5;
        verdict = "Perfect!";
        expertId = 1L;
        email = "alimirsalari@hotmail.com";
    }

    @Test
    void itShouldSaveComment() {
        //Arrange
        when(customerService.findByEmail(any())).thenReturn(new Customer());
        when(expertService.findById(any())).thenReturn(new Expert());
        //Act
        underTest.save(rate, verdict, expertId, email);
        //Assert
        verify(expertService, times(1)).updateWithEntity(any());
        verify(commentRepository, times(1)).save(any());
    }

    @Test
    void itShouldUpdateComment() {
        //Arrange
        Customer customer = new Customer();
        Comment comment = new Comment();
        comment.setCustomer(customer);
        when(commentRepository.findById(any())).thenReturn(Optional.of(comment));
        when(customerService.findByEmail(any())).thenReturn(customer);
        //Act
        underTest.update(1L, rate, verdict, email);
        //Assert
        verify(commentRepository, times(1)).save(any());
    }
    @Test
    void itShouldUpdateCommentWithEntity() {
        //Arrange
        Comment comment = new Comment();
        when(commentRepository.save(any())).thenReturn(comment);
        //Act
        underTest.updateWithEntity(comment);
        //Assert
        verify(commentRepository, times(1)).save(any());
    }
    @Test
    void itShouldRemoveCommentIfRequestIsFromAdmin() {
        //Arrange
        User user = new User();
        user.setUserType("ADMIN");
        when(userService.findByEmail(any())).thenReturn(user);
        //Act
        underTest.remove(1L, email);
        //Assert
        verify(commentRepository).deleteById(any());
    }

    @Test
    void itShouldThrowIllegalStateExceptionWhileDeletingComment() {
        //Arrange
        User user = new User();
        user.setUserType("CUSTOMER");
        when(userService.findByEmail(any())).thenReturn(user);
        when(customerService.findByEmail(any())).thenReturn(new Customer());
        when(commentRepository.findById(any())).thenReturn(Optional.of(new Comment()));
        //Act and Assert
        assertThrows(IllegalStateException.class, () -> underTest.remove(1L, email));
    }

    @Test
    void itShouldRemoveCommentIfRequestIsFromExpert() {
        //Arrange
        Comment comment = new Comment();
        Customer customer = new Customer();
        User user = new User();
        user.setUserType("CUSTOMER");
        comment.setCustomer(customer);
        when(userService.findByEmail(any())).thenReturn(user);
        when(customerService.findByEmail(any())).thenReturn(customer);
        when(commentRepository.findById(any())).thenReturn(Optional.of(comment));
        //Act
        underTest.remove(1L, email);
        //Assert
        verify(commentRepository).deleteById(any());
    }
    @Test
    void itShouldFindAllComments() {
        //Arrange
        when(commentRepository.findAll()).thenReturn(List.of(new Comment()));
        //Act
        underTest.findAll();
        //Assert
        verify(commentRepository, times(1)).findAll();
    }
    @Test
    void itShouldFindCommentsByExpertId() {
        //Arrange
        when(commentRepository.findCommentsByExpertId(any())).thenReturn(List.of(new Comment()));
        //Act
        underTest.findCommentsByExpertId(any());
        //Assert
        verify(commentRepository, times(1)).findCommentsByExpertId(any());
    }
}