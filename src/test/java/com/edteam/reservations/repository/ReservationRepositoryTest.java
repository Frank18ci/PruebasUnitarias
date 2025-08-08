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

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
// @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Tags(@Tag("repository"))
@DisplayName("Check the funtionality about reservation repository")
public class ReservationRepositoryTest {

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
    @Order(2)
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

    @Order(2)
    @Tag("success-case")
    @DisplayName("Verificar valores de todas las reservaciones guardades file resource")
    @ParameterizedTest
    @Timeout(value = 10L, unit = TimeUnit.MILLISECONDS)
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

    @Order(1)
    @Tag("success-case")
    @DisplayName("Verificar valores de todas las reservaciones")
    @Test
    void my_first_test_return_list() {
        ReservationRepository repository = new ReservationRepository();

        repository.save(getReservation(null, "AEP", "MIA"));
        List<Reservation> result = repository.getReservations();
        assertAll(() -> assertNotNull(result), () -> assertThat(result, hasSize(8)),
                () -> assertThat(getReservation(1L, "EZE", "MIA"), in(result)),
                () -> assertThat(result.get(0), hasProperty("id")),
                () -> assertThat(result.get(0).getPassengers().get(0).getFirstName(), stringContainsInOrder("A", "s")),
                () -> assertThat(result.get(0).getPassengers().get(0).getFirstName(), matchesRegex("[a-zA-Z]+")));
    }

    @Tag("success-case")
    @DisplayName("Verificar valores de entidades")
    @Test
    void my_first_test() {
        ReservationRepository repository = new ReservationRepository();
        Optional<Reservation> result = repository.getReservationById(1L);
        assertAll(() -> assertNotNull(result), () -> assertTrue(result.isPresent()),
                () -> assertEquals(getReservation(1L, "EZE", "MIA"), result.get()));
    }

    @Order(1)
    @Tag("error-case")
    @DisplayName("Verificar informacion no vacia")
    @Test
    void getReservation_should_not_return_the_information() {
        ReservationRepository repository = new ReservationRepository();
        Optional<Reservation> result = repository.getReservationById(2L);
        assertAll(() -> assertNotNull(result));
    }

}
