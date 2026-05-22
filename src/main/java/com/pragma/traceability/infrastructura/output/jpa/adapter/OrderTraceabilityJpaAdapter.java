package com.pragma.traceability.infrastructura.output.jpa.adapter;

import com.pragma.traceability.domain.model.OrderTraceability;
import com.pragma.traceability.domain.spi.IOrderTraceabilityPersistencePort;
import com.pragma.traceability.infrastructura.output.jpa.mapper.IOrderTraceabilityDocumentMapper;
import com.pragma.traceability.infrastructura.output.jpa.repositories.IOrderTraceabilityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderTraceabilityJpaAdapter implements IOrderTraceabilityPersistencePort {

    private final IOrderTraceabilityRepository repository;
    private final IOrderTraceabilityDocumentMapper mapper;

    @Override
    public void saveOrderTraceabilityRecord(OrderTraceability orderTraceability) {
        repository.save(mapper.toDocument(orderTraceability));
    }

    @Override
    public List<OrderTraceability> findAllByClientId(Long clientId) {
        return repository.findAllByClientId(clientId).stream()
                .map(mapper::toDomain).toList();
    }

    @Override
    public List<OrderTraceability> findAllByEmployeeId(Long employeeId) {
        return  repository.findAllByEmployeeId(employeeId).stream()
                .map(mapper::toDomain).toList();
    }

    @Override
    public Integer findAverageTimeByEmployeeId(Long employeeId) {
        return repository.findAverageTimeByEmployeeId(employeeId);
    }

    @Override
    public Optional<OrderTraceability> findById(Long id) {
        return repository.findAllByOrderId(id).stream()
                .findFirst()
                .map(mapper::toDomain);
    }


}
