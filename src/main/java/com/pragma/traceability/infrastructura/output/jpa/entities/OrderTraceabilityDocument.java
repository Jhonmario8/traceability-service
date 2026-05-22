package com.pragma.traceability.infrastructura.output.jpa.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Document(collection = "order_traceability")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderTraceabilityDocument {

    @Id
    private String id;
    private Long orderId;
    private Long clientId;
    private Long employeeId;
    private String previousState;
    private String newState;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long totalDurationInMinutes;

}