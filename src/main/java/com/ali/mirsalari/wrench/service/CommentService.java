package com.ali.mirsalari.wrench.service;

import com.ali.mirsalari.wrench.entity.Comment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CommentService {
    Comment save(byte rate, String verdict, Long customerId, Long expertId);

    Comment update(Long id, byte rate, String verdict, Long customerId, Long expertId);

    Comment updateWithEntity(Comment comment);

    void remove(Long id);

    Optional<Comment> findById(Long id);

    List<Comment> findAll();

    List<Comment> findCommentsByExpertId(Long expertId);
}
