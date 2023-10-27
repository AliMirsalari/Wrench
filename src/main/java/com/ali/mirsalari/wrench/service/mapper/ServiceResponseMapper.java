package com.ali.mirsalari.wrench.service.mapper;

import com.ali.mirsalari.wrench.entity.Service;
import com.ali.mirsalari.wrench.service.dto.ServiceResponse;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ServiceResponseMapper {
    @Mapping(source = "serviceParentDescription", target = "serviceParent.description")
    @Mapping(source = "serviceParentBasePrice", target = "serviceParent.basePrice")
    @Mapping(source = "serviceParentName", target = "serviceParent.name")
    @Mapping(source = "serviceParentId", target = "serviceParent.id")
    Service toEntity(ServiceResponse serviceResponse);

    @InheritInverseConfiguration(name = "toEntity")
    ServiceResponse toDto(Service service);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Service partialUpdate(ServiceResponse serviceResponse, @MappingTarget Service service);
}