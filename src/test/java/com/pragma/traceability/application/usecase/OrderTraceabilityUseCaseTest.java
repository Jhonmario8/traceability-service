package com.pragma.traceability.application.usecase;

import com.pragma.traceability.application.constants.ApplicationConstants;
import com.pragma.traceability.domain.exception.NotFoundException;
import com.pragma.traceability.domain.model.OrderTraceability;
import com.pragma.traceability.domain.spi.IOrderTraceabilityPersistencePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderTraceabilityUseCaseTest {

    private static final Long ORDER_ID = 1L;
    private static final Long CLIENT_ID = 10L;
    private static final Long EMPLOYEE_ID = 20L;
    private static final LocalDateTime START = LocalDateTime.of(2026, 1, 15, 12, 0);

    @Mock
    private IOrderTraceabilityPersistencePort persistencePort;

    @InjectMocks
    private OrderTraceabilityUseCase useCase;

    private static OrderTraceability record(String previousState, String newState, LocalDateTime start, LocalDateTime end) {
        OrderTraceability traceability = new OrderTraceability();
        traceability.setOrderId(ORDER_ID);
        traceability.setClientId(CLIENT_ID);
        traceability.setEmployeeId(EMPLOYEE_ID);
        traceability.setPreviousState(previousState);
        traceability.setNewState(newState);
        traceability.setStartTime(start);
        traceability.setEndTime(end);
        return traceability;
    }

    private static OrderTraceability withDuration(Long minutes) {
        OrderTraceability traceability = record("READY", "DELIVERED", START, null);
        traceability.setTotalDurationInMinutes(minutes);
        return traceability;
    }

    @Nested
    @DisplayName("saveOrderTraceabilityRecord")
    class Save {

        @Test
        @DisplayName("registra el inicio del pedido sin calcular duración")
        void savesStartRecordWithoutDuration() {
            // given
            OrderTraceability start = record(null, "PENDING", START, null);

            // when
            useCase.saveOrderTraceabilityRecord(start);

            // then
            assertThat(start.getTotalDurationInMinutes()).isNull();
            verify(persistencePort).saveOrderTraceabilityRecord(start);
        }

        @Test
        @DisplayName("registra el fin del pedido y calcula la duración total en minutos")
        void savesEndRecordWithDuration() {
            // given
            OrderTraceability end = record("READY", "DELIVERED", START, START.plusMinutes(45));

            // when
            useCase.saveOrderTraceabilityRecord(end);

            // then
            assertThat(end.getTotalDurationInMinutes()).isEqualTo(45L);
            verify(persistencePort).saveOrderTraceabilityRecord(end);
        }

        @Test
        @DisplayName("si hay endTime pero no startTime guarda sin duración")
        void savesEndRecordWithoutStart() {
            // given
            OrderTraceability end = record("READY", "DELIVERED", null, START);

            // when
            useCase.saveOrderTraceabilityRecord(end);

            // then
            assertThat(end.getTotalDurationInMinutes()).isNull();
            verify(persistencePort).saveOrderTraceabilityRecord(end);
        }
    }

    @Nested
    @DisplayName("findAverageTimeByEmployeeIdInMinutes")
    class Average {

        @Test
        @DisplayName("promedia la duración de varios pedidos terminados")
        void averagesSeveralRecords() {
            // given
            when(persistencePort.findAllByEmployeeId(EMPLOYEE_ID))
                    .thenReturn(List.of(withDuration(10L), withDuration(20L), withDuration(45L)));

            // when
            Double average = useCase.findAverageTimeByEmployeeIdInMinutes(EMPLOYEE_ID);

            // then
            assertThat(average).isEqualTo(25.0);
        }

        @Test
        @DisplayName("con un solo registro devuelve su duración")
        void singleRecord() {
            // given
            when(persistencePort.findAllByEmployeeId(EMPLOYEE_ID)).thenReturn(List.of(withDuration(30L)));

            // when / then
            assertThat(useCase.findAverageTimeByEmployeeIdInMinutes(EMPLOYEE_ID)).isEqualTo(30.0);
        }

        @Test
        @DisplayName("sin registros devuelve 0.0 (no divide por cero)")
        void noRecordsReturnsZero() {
            // given
            when(persistencePort.findAllByEmployeeId(EMPLOYEE_ID)).thenReturn(List.of());

            // when / then
            assertThat(useCase.findAverageTimeByEmployeeIdInMinutes(EMPLOYEE_ID)).isEqualTo(0.0);
        }
    }

    @Nested
    @DisplayName("consultas")
    class Queries {

        @Test
        @DisplayName("findById devuelve el registro más reciente del pedido")
        void findByIdReturnsRecord() {
            // given
            OrderTraceability traceability = record("PENDING", "IN_PREPARATION", START, null);
            when(persistencePort.findById(ORDER_ID)).thenReturn(Optional.of(traceability));

            // when / then
            assertThat(useCase.findById(ORDER_ID)).isSameAs(traceability);
        }

        @Test
        @DisplayName("findById lanza NotFoundException si el pedido no tiene trazabilidad")
        void findByIdThrowsWhenMissing() {
            // given
            when(persistencePort.findById(ORDER_ID)).thenReturn(Optional.empty());

            // when / then
            assertThatThrownBy(() -> useCase.findById(ORDER_ID))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage(ApplicationConstants.ORDER_NOT_FOUND);
        }

        @Test
        @DisplayName("findAllByClientId y findAllByEmployeeId delegan en el puerto de persistencia")
        void listQueriesDelegate() {
            // given
            List<OrderTraceability> records = List.of(record(null, "PENDING", START, null));
            when(persistencePort.findAllByClientId(CLIENT_ID)).thenReturn(records);
            when(persistencePort.findAllByEmployeeId(EMPLOYEE_ID)).thenReturn(records);

            // when / then
            assertThat(useCase.findAllByClientId(CLIENT_ID)).isSameAs(records);
            assertThat(useCase.findAllByEmployeeId(EMPLOYEE_ID)).isSameAs(records);
        }
    }
}
