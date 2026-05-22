package com.pragma.traceability.domain.spi;

import com.pragma.traceability.domain.model.OrderTraceability;

import java.util.List;
import java.util.Optional;

public interface IOrderTraceabilityPersistencePort {

    void saveOrderTraceabilityRecord(OrderTraceability orderTraceability);
    List<OrderTraceability> findAllByClientId(Long clientId);
    List<OrderTraceability> findAllByEmployeeId(Long employeeId);
    Optional<OrderTraceability> findById(Long id);
}
