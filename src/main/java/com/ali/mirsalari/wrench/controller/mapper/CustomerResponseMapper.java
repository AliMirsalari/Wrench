package com.ali.mirsalari.wrench.controller.mapper;

import com.ali.mirsalari.wrench.entity.Customer;
import com.ali.mirsalari.wrench.controller.dto.response.CustomerResponse;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerResponseMapper {
    Customer toEntity(CustomerResponse customerResponse);

    CustomerResponse toDto(Customer customer);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Customer partialUpdate(CustomerResponse customerResponse, @MappingTarget Customer customer);
}