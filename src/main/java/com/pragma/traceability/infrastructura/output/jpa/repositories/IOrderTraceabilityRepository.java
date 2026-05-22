package com.pragma.traceability.infrastructura.output.jpa.repositories;

import com.pragma.traceability.infrastructura.output.jpa.entities.OrderTraceabilityDocument;
import org.springframework.data.mongodb.repository.MongoRepository;


import java.util.List;
import java.util.Optional;


public interface IOrderTraceabilityRepository extends MongoRepository<OrderTraceabilityDocument, Long> {

    List<OrderTraceabilityDocument> findAllByClientId(Long clientId);
    List<OrderTraceabilityDocument> findAllByEmployeeId(Long employeeId);
    Integer findAverageTimeByEmployeeId (Long employeeId);
    List<OrderTraceabilityDocument> findAllByOrderId(Long orderId);
}
