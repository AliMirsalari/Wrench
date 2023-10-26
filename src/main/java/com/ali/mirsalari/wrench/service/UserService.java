package com.ali.mirsalari.wrench.service;

import com.ali.mirsalari.wrench.entity.User;
import com.ali.mirsalari.wrench.service.dto.UserSearchCriteria;

import java.util.List;

public interface UserService {
    List<User> searchUsers(UserSearchCriteria searchCriteria);
}
