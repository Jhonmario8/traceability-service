package com.pragma.traceability.infrastructura.output.jpa.mapper;

import com.pragma.traceability.domain.model.OrderTraceability;
import com.pragma.traceability.infrastructura.output.jpa.entities.OrderTraceabilityDocument;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IOrderTraceabilityDocumentMapper {

    OrderTraceability toDomain(OrderTraceabilityDocument entity);

    OrderTraceabilityDocument toDocument(OrderTraceability domain);

}
