package com.pragma.traceability.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pragma.traceability.application.constants.ApplicationConstants;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderTraceabilityDTO {

    @NotNull(message = ApplicationConstants.ORDER_ID_NOT_BLANK)
    private Long orderId;
    @NotNull(message = ApplicationConstants.CLIENT_ID_NOT_BLANK)
    private Long clientId;
    @NotNull(message = ApplicationConstants.EMPLOYEE_ID_NOT_BLANK)
    private Long employeeId;
    private String previousState;
    private String newState;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long totalDurationInMinutes;
}
