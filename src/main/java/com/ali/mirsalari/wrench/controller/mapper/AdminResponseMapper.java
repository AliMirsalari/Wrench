package com.ali.mirsalari.wrench.controller.mapper;

import com.ali.mirsalari.wrench.entity.Admin;
import com.ali.mirsalari.wrench.controller.dto.response.AdminResponse;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Component
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AdminResponseMapper {
    Admin toEntity(AdminResponse adminResponse);

    AdminResponse toDto(Admin admin);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Admin partialUpdate(AdminResponse adminResponse, @MappingTarget Admin admin);
}