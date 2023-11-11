package com.ali.mirsalari.wrench.service;

import com.ali.mirsalari.wrench.entity.Admin;
import com.ali.mirsalari.wrench.entity.User;
import com.ali.mirsalari.wrench.controller.dto.response.UserSearchCriteriaResponse;

import java.util.List;

public interface UserService {
    List<User> searchUsers(UserSearchCriteriaResponse searchCriteria);
    User findByEmail(String email);

}
