package com.pragma.traceability.domain.api;

import com.pragma.traceability.domain.model.OrderTraceability;

import java.util.List;
import java.util.Optional;

public interface IOrderTraceabilityServicePort {
        void saveOrderTraceabilityRecord(OrderTraceability orderTraceability);
        List<OrderTraceability> findAllByClientId(Long clientId);
        List<OrderTraceability> findAllByEmployeeId(Long employeeId);
        Integer findAverageTimeByEmployeeId(Long employeeId);
        OrderTraceability findById(Long id);
}
