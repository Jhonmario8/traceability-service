package com.pragma.traceability.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTraceabilityTest {

    private static final LocalDateTime START = LocalDateTime.of(2026, 1, 15, 12, 0);

    private final OrderTraceability traceability = new OrderTraceability();

    @Test
    @DisplayName("calcula los minutos completos entre inicio y fin")
    void calculatesWholeMinutes() {
        assertThat(traceability.calculateDurationInMinutes(START, START.plusMinutes(90).plusSeconds(59)))
                .isEqualTo(90L);
    }

    @Test
    @DisplayName("devuelve null si falta inicio o fin")
    void returnsNullWhenMissingDates() {
        assertThat(traceability.calculateDurationInMinutes(null, START)).isNull();
        assertThat(traceability.calculateDurationInMinutes(START, null)).isNull();
    }
}
