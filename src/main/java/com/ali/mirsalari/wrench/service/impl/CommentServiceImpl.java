package com.ali.mirsalari.wrench.service.impl;

import com.ali.mirsalari.wrench.entity.Comment;
import com.ali.mirsalari.wrench.entity.Customer;
import com.ali.mirsalari.wrench.entity.Expert;
import com.ali.mirsalari.wrench.entity.User;
import com.ali.mirsalari.wrench.exception.NotFoundException;
import com.ali.mirsalari.wrench.repository.CommentRepository;
import com.ali.mirsalari.wrench.repository.CustomerRepository;
import com.ali.mirsalari.wrench.repository.ExpertRepository;
import com.ali.mirsalari.wrench.repository.UserRepository;
import com.ali.mirsalari.wrench.service.CommentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CustomerRepository customerRepository;
    private final ExpertRepository expertRepository;
    private final UserRepository userRepository;

    @Override
    public Comment save(byte rate, String verdict, Long expertId, String email) {
        Customer customer = customerRepository.findCustomerByEmail(email).orElseThrow(() -> new NotFoundException("Customer is not found!"));
        Expert expert = expertRepository.findById(expertId).orElseThrow(() -> new NotFoundException("Expert is not found!"));
        expert.setScore(expert.getScore()+rate);
        expertRepository.save(expert);
        Comment comment = new Comment(rate, verdict, customer, expert);
        return commentRepository.save(comment);
    }

    @Override
    public Comment update(Long id, byte rate, String verdict, String email) {
        Comment comment = findById(id).orElseThrow(() -> new NotFoundException("Comment is not found!"));
        Customer customer = customerRepository.findCustomerByEmail(email).orElseThrow(() -> new NotFoundException("Customer is not found!"));
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
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new NotFoundException("User is not found!"));
        if (Objects.equals(user.getUserType(), "ADMIN")) {
            commentRepository.deleteById(id);
            return;
        }
        Customer customer = customerRepository.findCustomerByEmail(email).orElseThrow(()-> new NotFoundException("Customer is not found!"));
        Comment comment = commentRepository.findById(id).orElseThrow(()-> new NotFoundException("Comment is not found!"));
        if (comment.getCustomer() != customer){
            throw new IllegalArgumentException("You can not remove this comment!");
        }
        commentRepository.deleteById(id);
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
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
