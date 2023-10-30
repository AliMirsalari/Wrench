package com.ali.mirsalari.wrench.controller;

import com.ali.mirsalari.wrench.entity.User;
import com.ali.mirsalari.wrench.service.UserService;
import com.ali.mirsalari.wrench.controller.dto.UserResponse;
import com.ali.mirsalari.wrench.controller.dto.UserSearchCriteria;
import com.ali.mirsalari.wrench.controller.mapper.UserResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserResponseMapper userResponseMapper;
    private final UserService userService;
    @GetMapping("/search")
    public ResponseEntity<List<UserResponse>> searchUsers(@ModelAttribute UserSearchCriteria searchCriteria) {
        List<User> users = userService.searchUsers(searchCriteria);
        return ResponseEntity.ok(users.stream()
                .map(userResponseMapper::toDto)
                .collect(Collectors.toList()));
    }
}
