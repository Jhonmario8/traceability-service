package com.pragma.traceability.application.handler;

import com.pragma.traceability.application.dto.OrderTraceabilityDTO;


import java.util.List;

public interface IOrderTraceabilityHandler {
        void saveOrderTraceabilityRecord(OrderTraceabilityDTO orderTraceabilityDTO);
        List<OrderTraceabilityDTO> findAllByClientId(Long clientId);
        List<OrderTraceabilityDTO> findAllByEmployeeId(Long employeeId);
        OrderTraceabilityDTO findByOrderId(Long orderId);
        Double findAverageTimeByEmployeeIdInMinutes(Long employeeId);
}
