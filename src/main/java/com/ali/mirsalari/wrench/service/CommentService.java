package com.ali.mirsalari.wrench.service;

import com.ali.mirsalari.wrench.entity.Comment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {
    Comment save(byte rate, String verdict, Long expertId, String email);

    Comment update(Long id, byte rate, String verdict, String email);

    Comment updateWithEntity(Comment comment);

    void remove(Long id, String email);

    Comment findById(Long id);

    List<Comment> findAll();

    List<Comment> findCommentsByExpertId(Long expertId);
}
