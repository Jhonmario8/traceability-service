package com.pragma.traceability.domain.api;

import com.pragma.traceability.domain.model.OrderTraceability;

import java.util.List;


public interface IOrderTraceabilityServicePort {
        void saveOrderTraceabilityRecord(OrderTraceability orderTraceability);
        List<OrderTraceability> findAllByClientId(Long clientId);
        List<OrderTraceability> findAllByEmployeeId(Long employeeId);
        OrderTraceability findById(Long id);
        Double findAverageTimeByEmployeeIdInMinutes(Long employeeId);
}
