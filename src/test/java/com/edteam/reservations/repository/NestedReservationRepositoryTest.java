package com.edteam.reservations.repository;

import com.edteam.reservations.model.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.edteam.reservations.util.ReservationUtils.getReservation;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

//@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Tags(@Tag("repository"))
@DisplayName("Check the funtionality about reservation repository")
public class NestedReservationRepositoryTest {

    // ReservationRepository repository;

    // @BeforeEach
    // void inicialize_each_test() {
    // System.out.println("Inicio");
    // repository = new ReservationRepository();
    // if (repository.getReservations().size() != 1) {
    // fail();
    // }
    // }

    // @AfterEach
    // void destroy_each_test() {
    // System.out.println("Fin");
    // }
    @Nested
    class GetReservation {

        @Tag("success-case")
        @DisplayName("Verificar valores de entidades")
        @Test
        void my_first_test() {
            ReservationRepository repository = new ReservationRepository();
            Optional<Reservation> result = repository.getReservationById(1L);
            assertAll(() -> assertNotNull(result), () -> assertTrue(result.isPresent()),
                    () -> assertEquals(getReservation(1L, "EZE", "MIA"), result.get()));
        }

        @Tag("error-case")
        @DisplayName("Verificar informacion no vacia")
        @Test
        void getReservation_should_not_return_the_information() {
            ReservationRepository repository = new ReservationRepository();
            Optional<Reservation> result = repository.getReservationById(20L);
            assertAll(() -> assertNotNull(result), () -> assertTrue(result.isEmpty()));
        }
    }

    @Nested
    class SaveReservation {

        @Tag("success-case")
        @DisplayName("Verificar valores de todas las reservaciones guardades")
        @ParameterizedTest
        @CsvSource({ "MIA,AEP", "BUE,SLC", "BUE,IA" })
        void save_should_return_the_information(String origin, String destination) {
            // Given
            ReservationRepository repository = new ReservationRepository();

            // When
            Reservation result = repository.save(getReservation(null, origin, destination));

            // Then
            assertAll(() -> assertNotNull(result),
                    () -> assertEquals(origin, result.getItinerary().getSegment().get(0).getOrigin()),
                    () -> assertEquals(destination, result.getItinerary().getSegment().get(0).getDestination()));

        }

        @Tag("success-case")
        @DisplayName("Verificar valores de todas las reservaciones guardades file resource")
        @ParameterizedTest
        @Timeout(value = 100L, unit = TimeUnit.MILLISECONDS)
        @CsvFileSource(resources = "/save-repository.csv")
        void save_should_return_the_information_file_resource(String origin, String destination) {
            // Given
            ReservationRepository repository = new ReservationRepository();

            // When
            Reservation result = repository.save(getReservation(null, origin, destination));

            // Then
            assertAll(() -> assertNotNull(result),
                    () -> assertEquals(origin, result.getItinerary().getSegment().get(0).getOrigin()),
                    () -> assertEquals(destination, result.getItinerary().getSegment().get(0).getDestination()));

        }
    }

}
