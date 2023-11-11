package com.ali.mirsalari.wrench.controller.mapper;

import com.ali.mirsalari.wrench.entity.Bid;
import com.ali.mirsalari.wrench.controller.dto.response.BidResponse;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface BidResponseMapper {
    @Mapping(source = "orderOrderStatus", target = "order.orderStatus")
    @Mapping(source = "orderAddress", target = "order.address")
    @Mapping(source = "orderDateOfExecution", target = "order.dateOfExecution")
    @Mapping(source = "orderSuggestedPrice", target = "order.suggestedPrice")
    @Mapping(source = "orderDescription", target = "order.description")
    @Mapping(source = "orderId", target = "order.id")
    @Mapping(source = "expertScore", target = "expert.score")
    @Mapping(source = "expertLastName", target = "expert.lastName")
    @Mapping(source = "expertFirstName", target = "expert.firstName")
    @Mapping(source = "expertId", target = "expert.id")
    Bid toEntity(BidResponse bidResponse);

    @InheritInverseConfiguration(name = "toEntity")
    BidResponse toDto(Bid bid);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Bid partialUpdate(BidResponse bidResponse, @MappingTarget Bid bid);
}