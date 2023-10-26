package com.ali.mirsalari.wrench.controller;

import com.ali.mirsalari.wrench.entity.Comment;
import com.ali.mirsalari.wrench.service.CommentService;
import com.ali.mirsalari.wrench.service.dto.CommentResponse;
import com.ali.mirsalari.wrench.service.dto.RegisterCommentRequest;
import com.ali.mirsalari.wrench.service.mapper.CommentResponseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/comment")
public class CommentController {
    private final CommentResponseMapper commentResponseMapper;
    private final CommentService commentService;

    @GetMapping
    public List<CommentResponse> getComments() {
        List<Comment> comments = commentService.findAll();
        return comments.stream()
                .map(commentResponseMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<?> registerComment(@Valid @RequestBody RegisterCommentRequest request) {
            Comment comment = commentService.save(request.rate(),
                    request.verdict(),
                    request.customerId(),
                    request.expertId());
            return ResponseEntity.ok(commentResponseMapper.toDto(comment));
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<?> updateComment(@Valid
                                       @PathVariable("id") Long id,
                                       @RequestBody RegisterCommentRequest request) {
            Comment comment = commentService.update(
                    id,
                    request.rate(),
                    request.verdict(),
                    request.customerId(),
                    request.expertId()
            );
            return ResponseEntity.ok(commentResponseMapper.toDto(comment));
    }
    @DeleteMapping(path = "{commentId}")
    public HttpStatus deleteComment(@PathVariable("commentId") Long commentId) {
        commentService.remove(commentId);
        return HttpStatus.OK;
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<CommentResponse> findById(@PathVariable Long id) {
        Optional<Comment> comment = commentService.findById(id);
        return comment.map(commentResponseMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/findCommentsByExpertId/{id}")
    public List<CommentResponse> findCommentsByExpertId(@PathVariable Long id) {
        List<Comment> comments = commentService.findCommentsByExpertId(id);
        return comments.stream()
                .map(commentResponseMapper::toDto)
                .collect(Collectors.toList());
    }
}
