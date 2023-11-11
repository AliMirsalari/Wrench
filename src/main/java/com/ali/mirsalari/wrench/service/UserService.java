package com.ali.mirsalari.wrench.service;

import com.ali.mirsalari.wrench.controller.dto.response.UserSearchCriteriaResponse;
import com.ali.mirsalari.wrench.entity.User;

import java.util.List;

public interface UserService {
    List<User> searchUsers(UserSearchCriteriaResponse searchCriteria);
    User findByEmail(String email);

}
