package com.pragma.traceability.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderTraceability {

    private String id;
    private Long orderId;
    private Long clientId;
    private Long employeeId;
    private String previousState;
    private String newState;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long totalDurationInMinutes;

    public Long calculateDurationInMinutes(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime != null && endTime != null) {
            return Duration.between(startTime, endTime).toMinutes();
        }
        return null;
    }

}
