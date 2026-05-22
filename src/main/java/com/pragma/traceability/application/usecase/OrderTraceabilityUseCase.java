package com.pragma.traceability.application.usecase;


import com.pragma.traceability.application.constants.ApplicationConstants;


import com.pragma.traceability.domain.api.IOrderTraceabilityServicePort;
import com.pragma.traceability.domain.exception.NotFoundException;
import com.pragma.traceability.domain.model.OrderTraceability;
import com.pragma.traceability.domain.spi.IOrderTraceabilityPersistencePort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderTraceabilityUseCase implements IOrderTraceabilityServicePort {

    private final IOrderTraceabilityPersistencePort orderTraceabilityPersistencePort;


    @Override
    public void saveOrderTraceabilityRecord(OrderTraceability orderTraceability) {
        if (orderTraceability.getEndTime() != null){
           Long total = orderTraceability.calculateDurationInMinutes(orderTraceability.getStartTime(), orderTraceability.getEndTime());
            orderTraceability.setTotalDurationInMinutes(total);
        }
        orderTraceabilityPersistencePort.saveOrderTraceabilityRecord(orderTraceability);
    }

    @Override
    public List<OrderTraceability> findAllByClientId(Long clientId) {
        return orderTraceabilityPersistencePort.findAllByClientId(clientId);
    }

    @Override
    public List<OrderTraceability> findAllByEmployeeId(Long employeeId) {
        return orderTraceabilityPersistencePort.findAllByEmployeeId(employeeId);
    }


    @Override
    public OrderTraceability findById(Long id) {
        return orderTraceabilityPersistencePort.findById(id)
                .orElseThrow(() -> new NotFoundException(ApplicationConstants.ORDER_NOT_FOUND));
    }

    @Override
    public Double findAverageTimeByEmployeeIdInMinutes(Long employeeId) {

        List<OrderTraceability> traceabilities = orderTraceabilityPersistencePort.findAllByEmployeeId(employeeId);
        if (traceabilities.isEmpty()) {
            return 0.0;
        }
        double totalDuration = traceabilities.stream()
                .filter(t -> t.getTotalDurationInMinutes() != null)
                .mapToDouble(OrderTraceability::getTotalDurationInMinutes)
                .sum();
        return totalDuration / traceabilities.size();

    }
}
