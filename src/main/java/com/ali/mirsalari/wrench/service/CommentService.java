package com.ali.mirsalari.wrench.service;

import com.ali.mirsalari.wrench.entity.Comment;
import com.ali.mirsalari.wrench.service.base.CrudService;
import org.springframework.stereotype.Service;

@Service
public interface CommentService
        extends CrudService<Comment,Long> {
}
