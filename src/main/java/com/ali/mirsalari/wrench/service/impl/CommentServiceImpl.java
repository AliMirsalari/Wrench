package com.ali.mirsalari.wrench.service.impl;

import com.ali.mirsalari.wrench.entity.Comment;
import com.ali.mirsalari.wrench.exception.NotFoundException;
import com.ali.mirsalari.wrench.repository.CommentRepository;
import com.ali.mirsalari.wrench.service.CommentService;
import jakarta.persistence.EntityTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public Comment update(Comment comment) {
        if (comment.getId() == null || commentRepository.findById(comment.getId()).isEmpty()) {
            throw new NotFoundException("Comment with id: " + comment.getId() + " is not found.");
        }
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

    @Override
    @Transactional
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }
}
