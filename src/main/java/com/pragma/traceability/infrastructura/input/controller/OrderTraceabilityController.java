package com.pragma.traceability.infrastructura.input.controller;


import com.pragma.traceability.application.dto.OrderTraceabilityDTO;
import com.pragma.traceability.application.handler.IOrderTraceabilityHandler;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("traceability/")
@AllArgsConstructor
public class OrderTraceabilityController {

    private final IOrderTraceabilityHandler orderTraceabilityHandler;

    @PostMapping
    public void saveTraceabilityRecord(@RequestBody OrderTraceabilityDTO orderTraceabilityDTO) {
        orderTraceabilityHandler.saveOrderTraceabilityRecord(orderTraceabilityDTO);
    }

    @GetMapping("/client/{clientId}")
    public List<OrderTraceabilityDTO> findAllByClientId(@PathVariable Long clientId) {
        return orderTraceabilityHandler.findAllByClientId(clientId);
    }

    @GetMapping("/employee/{employeeId}")
    public List<OrderTraceabilityDTO> findAllByEmployeeId(@PathVariable Long employeeId){
        return orderTraceabilityHandler.findAllByEmployeeId(employeeId);
    }

    @GetMapping("/employee/{employeeId}/media")
    public Integer findAverageTimeByEmployeeId(@PathVariable Long employeeId){
        return orderTraceabilityHandler.findAverageTimeByEmployeeId(employeeId);
    }

    @GetMapping("/{orderId}")
    public OrderTraceabilityDTO findByOrderId(@PathVariable Long orderId){
        return orderTraceabilityHandler.findByOrderId(orderId) ;
    }
}
