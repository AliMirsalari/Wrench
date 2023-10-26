package com.ali.mirsalari.wrench.service.mapper;

import com.ali.mirsalari.wrench.entity.User;
import com.ali.mirsalari.wrench.service.dto.UserResponse;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserResponseMapper {
    User toEntity(UserResponse userResponse);

    UserResponse toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserResponse userResponse, @MappingTarget User user);
}