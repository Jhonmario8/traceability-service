package com.pragma.traceability.application.handler;

import com.pragma.traceability.application.dto.OrderTraceabilityDTO;
import com.pragma.traceability.application.mapper.IOrderTraceabilityMapper;
import com.pragma.traceability.domain.api.IOrderTraceabilityServicePort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderTraceabilityHandler implements IOrderTraceabilityHandler {

    private final IOrderTraceabilityServicePort orderTraceabilityServicePort;
    private final IOrderTraceabilityMapper mapper;
    @Override
    public void saveOrderTraceabilityRecord(OrderTraceabilityDTO orderTraceabilityDTO) {
        orderTraceabilityServicePort.saveOrderTraceabilityRecord(mapper.toDomain(orderTraceabilityDTO));
    }

    @Override
    public List<OrderTraceabilityDTO> findAllByClientId(Long clientId) {
        return orderTraceabilityServicePort.findAllByClientId(clientId).stream().map(mapper::toDTO).toList() ;
    }

    @Override
    public List<OrderTraceabilityDTO> findAllByEmployeeId(Long employeeId) {
        return orderTraceabilityServicePort.findAllByEmployeeId(employeeId).stream().map(mapper::toDTO).toList() ;
    }

    @Override
    public Integer findAverageTimeByEmployeeId(Long employeeId) {
        return orderTraceabilityServicePort.findAverageTimeByEmployeeId(employeeId);
    }

    @Override
    public OrderTraceabilityDTO findByOrderId(Long orderId) {
        return mapper.toDTO(orderTraceabilityServicePort.findById(orderId));
    }
}
