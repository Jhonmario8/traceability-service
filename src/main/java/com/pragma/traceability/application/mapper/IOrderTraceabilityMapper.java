package com.pragma.traceability.application.mapper;

import com.pragma.traceability.application.dto.OrderTraceabilityDTO;
import com.pragma.traceability.domain.model.OrderTraceability;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IOrderTraceabilityMapper {

    OrderTraceability toDomain(OrderTraceabilityDTO orderTraceabilityDTO);

    OrderTraceabilityDTO toDTO(OrderTraceability orderTraceability);
}
