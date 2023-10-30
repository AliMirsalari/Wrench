package com.ali.mirsalari.wrench.controller.mapper;

import com.ali.mirsalari.wrench.entity.Expert;
import com.ali.mirsalari.wrench.controller.dto.ExpertResponse;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExpertResponseMapper {
    Expert toEntity(ExpertResponse expertResponse);

    @AfterMapping
    default void linkComments(@MappingTarget Expert expert) {
        expert.getComments().forEach(comment -> comment.setExpert(expert));
    }

    ExpertResponse toDto(Expert expert);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Expert partialUpdate(ExpertResponse expertResponse, @MappingTarget Expert expert);
}