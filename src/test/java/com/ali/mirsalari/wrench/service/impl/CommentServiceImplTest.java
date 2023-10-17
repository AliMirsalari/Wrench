package com.ali.mirsalari.wrench.service.impl;

import com.ali.mirsalari.wrench.entity.Comment;
import com.ali.mirsalari.wrench.exception.NotFoundException;
import com.ali.mirsalari.wrench.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private CommentServiceImpl underTest;
    private Comment comment;

    @BeforeEach
    void setUp() {
        comment = new Comment(
                (byte) 5,
                "Perfect!",
                null,
                null
        );
    }

    @Test
    void itShouldSaveComment() {
        //Arrange
        when(commentRepository.save(any())).thenReturn(comment);
        //Act
        Comment tempComment = underTest.save(comment);
        //Assert
        assertNotNull(tempComment);
    }
    @Test
    void itShouldThrowNotFoundException() {
//        //Arrange
        comment.setId(null);
        //Act and Assert
        assertThrows(NotFoundException.class, () -> {
            underTest.update(comment);
        });
    }

    @Test
    void itShouldUpdateComment() {
        //Arrange
        when(commentRepository.save(any())).thenReturn(comment);
        when(commentRepository.findById(any())).thenReturn(Optional.ofNullable(comment));
        comment.setId(1L);
        //Act
        Comment tempComment = underTest.update(comment);
        //Assert
        assertNotNull(tempComment);
    }
    @Test
    void itShouldUpdateCommentWithoutChecking() {
        //Arrange
        when(commentRepository.save(any())).thenReturn(comment);
        //Act
        Comment tempComment = underTest.uncheckedUpdate(comment);
        //Assert
        assertNotNull(tempComment);
    }

    @Test
    void itShouldDeleteCommentById() {
        //Act
        underTest.remove(1L);
        //Assert
        verify(commentRepository, times(1)).deleteById(any());
    }
    @Test
    void itShouldFindACommentById() {
        //Arrange
        when(commentRepository.findById(any())).thenReturn(Optional.ofNullable(comment));
        //Act and Assert
        if (underTest.findById(1L).isPresent()) {
            Comment tempComment = underTest.findById(1L).get();
            assertNotNull(tempComment);
        }
    }
    @Test
    void itShouldFindComments() {
        //Arrange
        when(commentRepository.findAll()).thenReturn(List.of(comment,comment,comment));
        //Act
        List<Comment> comments = underTest.findAll();
        //Assert
        assertNotNull(comments);
    }
}