package com.ali.mirsalari.wrench.controller.mapper;

import com.ali.mirsalari.wrench.controller.dto.response.OrderResponse;
import com.ali.mirsalari.wrench.entity.Order;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderResponseMapper {
    @Mapping(source = "customerEmail", target = "customer.email")
    @Mapping(source = "customerLastName", target = "customer.lastName")
    @Mapping(source = "customerFirstName", target = "customer.firstName")
    @Mapping(source = "customerId", target = "customer.id")
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