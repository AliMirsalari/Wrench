package com.ali.mirsalari.wrench.repository;

import com.ali.mirsalari.wrench.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository
        extends JpaRepository<Comment,Long> {

    List<Comment> findCommentsByExpertId(Long expertId);
}
