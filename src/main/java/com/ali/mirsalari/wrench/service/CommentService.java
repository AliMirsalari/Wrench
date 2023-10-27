package com.ali.mirsalari.wrench.service;

import com.ali.mirsalari.wrench.entity.Comment;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CommentService {
    @Transactional
    Comment save(byte rate, String verdict, Long customerId, Long expertId);

    @Transactional
    Comment update(Long id, byte rate, String verdict, Long customerId, Long expertId);

    @Transactional
    Comment uncheckedUpdate(Comment comment);

    @Transactional
    void remove(Long id);

    @Transactional
    Optional<Comment> findById(Long id);

    @Transactional
    List<Comment> findAll();

    @Transactional
    List<Comment> findCommentsByExpertId(Long expertId);
}
