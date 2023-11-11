package com.ali.mirsalari.wrench.controller.mapper;

import com.ali.mirsalari.wrench.entity.Comment;
import com.ali.mirsalari.wrench.controller.dto.response.CommentResponse;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentResponseMapper {
    @Mapping(source = "expertScore", target = "expert.score")
    @Mapping(source = "expertLastName", target = "expert.lastName")
    @Mapping(source = "expertFirstName", target = "expert.firstName")
    @Mapping(source = "expertId", target = "expert.id")
    @Mapping(source = "customerLastName", target = "customer.lastName")
    @Mapping(source = "customerFirstName", target = "customer.firstName")
    @Mapping(source = "customerId", target = "customer.id")
    Comment toEntity(CommentResponse commentResponse);

    @InheritInverseConfiguration(name = "toEntity")
    CommentResponse toDto(Comment comment);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Comment partialUpdate(CommentResponse commentResponse, @MappingTarget Comment comment);
}