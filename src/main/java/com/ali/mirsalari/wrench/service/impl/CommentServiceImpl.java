package com.ali.mirsalari.wrench.service.impl;

import com.ali.mirsalari.wrench.entity.Comment;
import com.ali.mirsalari.wrench.entity.Customer;
import com.ali.mirsalari.wrench.entity.Expert;
import com.ali.mirsalari.wrench.entity.User;
import com.ali.mirsalari.wrench.exception.NotFoundException;
import com.ali.mirsalari.wrench.repository.CommentRepository;
import com.ali.mirsalari.wrench.service.CommentService;
import com.ali.mirsalari.wrench.service.CustomerService;
import com.ali.mirsalari.wrench.service.ExpertService;
import com.ali.mirsalari.wrench.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CustomerService customerService;
    private final ExpertService expertService;
    private final UserService userService;

    @Override
    public Comment save(byte rate, String verdict, Long expertId, String email) {
        Customer customer = customerService.findByEmail(email);
        Expert expert = expertService.findById(expertId);
        expert.setScore(expert.getScore()+rate);
        expertService.updateWithEntity(expert);
        Comment comment = new Comment(rate, verdict, customer, expert);
        return commentRepository.save(comment);
    }

    @Override
    public Comment update(Long id, byte rate, String verdict, String email) {
        Comment comment = findById(id);
        Customer customer = customerService.findByEmail(email);
        if (comment.getCustomer() == customer) {
            comment.setRate(rate);
            comment.setVerdict(verdict);
        }
        return commentRepository.save(comment);
    }

    @Override
    public Comment updateWithEntity(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public void remove(Long id, String email) {
        User user = userService.findByEmail(email);
        if (Objects.equals(user.getUserType(), "ADMIN")) {
            commentRepository.deleteById(id);
            return;
        }
        Customer customer = customerService.findByEmail(email);
        Comment comment = findById(id);
        if (comment.getCustomer() != customer){
            throw new IllegalArgumentException("You can not remove this comment!");
        }
        commentRepository.deleteById(id);
    }

    @Override
    public Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comment with ID " + id + " is not found!"));
    }

    @Override
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Override
    public List<Comment> findCommentsByExpertId(Long expertId) {
        return commentRepository.findCommentsByExpertId(expertId);
    }
}
