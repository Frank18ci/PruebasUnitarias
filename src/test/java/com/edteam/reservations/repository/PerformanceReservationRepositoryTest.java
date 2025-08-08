package com.edteam.reservations.repository;

import com.edteam.reservations.model.Reservation;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.quickperf.annotation.ExpectMaxExecutionTime;
import org.quickperf.junit5.QuickPerfTest;
import org.quickperf.jvm.annotations.MeasureHeapAllocation;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.edteam.reservations.util.ReservationUtils.getReservation;
import static org.junit.jupiter.api.Assertions.*;

@QuickPerfTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tags(@Tag("performance"))
@DisplayName("Check the funtionality about reservation repository")
public class PerformanceReservationRepositoryTest {





    @MeasureHeapAllocation
    //@ExpectMaxExecutionTime(milliSeconds = 500)
    @Tag("success-case")
    @DisplayName("Verificar valores de value 1 ")
    @Test
    void getReservation_should_return_the_value_1() {
        ReservationRepository repository = new ReservationRepository();
        Optional<Reservation> result = repository.getReservationById(1L);
        assertAll(() -> assertNotNull(result), () -> assertTrue(result.isPresent()),
                () -> assertEquals(getReservation(1L, "EZE", "MIA"), result.get()));
    }


}
