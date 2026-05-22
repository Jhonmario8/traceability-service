package com.pragma.traceability.application.handler;

import com.pragma.traceability.application.dto.OrderTraceabilityDTO;
import com.pragma.traceability.domain.model.OrderTraceability;

import java.util.List;

public interface IOrderTraceabilityHandler {
        void saveOrderTraceabilityRecord(OrderTraceabilityDTO orderTraceabilityDTO);
        List<OrderTraceabilityDTO> findAllByClientId(Long clientId);
        List<OrderTraceabilityDTO> findAllByEmployeeId(Long employeeId);
        Integer findAverageTimeByEmployeeId(Long employeeId);
        OrderTraceabilityDTO findByOrderId(Long orderId);
}
