package com.ali.mirsalari.wrench.service.mapper;

import com.ali.mirsalari.wrench.entity.Order;
import com.ali.mirsalari.wrench.service.dto.OrderResponse;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderResponseMapper {
    @Mapping(source = "serviceDescription", target = "service.description")
    @Mapping(source = "serviceBasePrice", target = "service.basePrice")
    @Mapping(source = "serviceName", target = "service.name")
    @Mapping(source = "serviceId", target = "service.id")
    Order toEntity(OrderResponse orderResponse);

    @InheritInverseConfiguration(name = "toEntity")
    OrderResponse toDto(Order order);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Order partialUpdate(OrderResponse orderResponse, @MappingTarget Order order);
}