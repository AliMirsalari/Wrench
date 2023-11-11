package com.ali.mirsalari.wrench.controller;

import com.ali.mirsalari.wrench.controller.dto.request.UpdateCommentRequest;
import com.ali.mirsalari.wrench.entity.Comment;
import com.ali.mirsalari.wrench.service.CommentService;
import com.ali.mirsalari.wrench.controller.dto.response.CommentResponse;
import com.ali.mirsalari.wrench.controller.dto.request.RegisterCommentRequest;
import com.ali.mirsalari.wrench.controller.mapper.CommentResponseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping({"","/"})
    public List<CommentResponse> getComments() {
        List<Comment> comments = commentService.findAll();
        return comments.stream()
                .map(commentResponseMapper::toDto)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    public ResponseEntity<?> registerComment(@Valid @RequestBody RegisterCommentRequest request,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        Comment comment = commentService.save(request.rate(),
                request.verdict(),
                request.expertId(),
                userDetails.getUsername());
        return ResponseEntity.ok(commentResponseMapper.toDto(comment));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping(path = "{id}")
    public ResponseEntity<?> updateComment(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateCommentRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        Comment comment = commentService.update(
                id,
                request.rate(),
                request.verdict(),
                userDetails.getUsername()
        );
        return ResponseEntity.ok(commentResponseMapper.toDto(comment));
    }

    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @DeleteMapping(path = "{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("commentId") Long commentId,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        commentService.remove(commentId, userDetails.getUsername());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/findById/{id}")
    public ResponseEntity<CommentResponse> findById(@PathVariable Long id) {
        Optional<Comment> comment = Optional.ofNullable(commentService.findById(id));
        return comment.map(commentResponseMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER','EXPERT')")
    @GetMapping("/findCommentsByExpertId/{id}")
    public List<CommentResponse> findCommentsByExpertId(@PathVariable Long id) {
        List<Comment> comments = commentService.findCommentsByExpertId(id);
        return comments.stream()
                .map(commentResponseMapper::toDto)
                .collect(Collectors.toList());
    }
}
