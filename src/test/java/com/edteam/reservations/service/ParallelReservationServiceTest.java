package com.edteam.reservations.service;

import com.edteam.reservations.connector.CatalogConnector;
import com.edteam.reservations.dto.ReservationDTO;
import com.edteam.reservations.enums.APIError;
import com.edteam.reservations.exception.EdteamException;
import com.edteam.reservations.model.Reservation;
import com.edteam.reservations.repository.ReservationRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;

import java.util.Optional;

import static com.edteam.reservations.util.ReservationUtils.getReservation;
import static com.edteam.reservations.util.ReservationUtils.getReservationDTO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tags(@Tag("service"))
@DisplayName("Check the functionality of ReservationService")
public class ParallelReservationServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParallelReservationServiceTest.class);

    @Mock
    ReservationRepository repository;
    @Mock
    ConversionService conversionService;
    @Mock
    CatalogConnector catalogConnector;

    @BeforeEach
    void initializeEachTest() {
        LOGGER.info(() -> "Metodo initializeEachTest");
        MockitoAnnotations.openMocks(this);
    }
    @ResourceLock(value = "reservation")
    @Tag("error-case")
    @Test
    @DisplayName("Check error when reservation not found")
    void getReservation_shouldThrowException_whenNotFound() throws InterruptedException {
        ReservationService service = new ReservationService(repository, conversionService, catalogConnector);

        when(repository.getReservationById(6L)).thenReturn(Optional.empty());
        Thread.sleep(500);
        EdteamException exception = assertThrows(EdteamException.class, () -> {
            service.getReservationById(6L);
        });

        verify(repository, atMostOnce()).getReservationById(6L);
        verify(conversionService, never()).convert(any(), any());

        assertAll(() -> assertNotNull(exception),
                () -> assertEquals(APIError.RESERVATION_NOT_FOUND.getMessage(), exception.getDescription()),
                () -> assertEquals(APIError.RESERVATION_NOT_FOUND.getHttpStatus(), exception.getStatus()));
    }
    @ResourceLock(value = "reservation")
    @Tag("success-case")
    @Test
    @DisplayName("Check success with existing reservation")
    void getReservation_shouldReturnReservationDTO_whenFound() throws InterruptedException {
        ReservationService service = new ReservationService(repository, conversionService, catalogConnector);

        Reservation reservationModel = getReservation(1L, "BUE", "MAD");
        when(repository.getReservationById(1L)).thenReturn(Optional.of(reservationModel));

        ReservationDTO reservationDTO = getReservationDTO(1L, "BUE", "MAD");
        when(conversionService.convert(reservationModel, ReservationDTO.class)).thenReturn(reservationDTO);
        Thread.sleep(500);
        ReservationDTO result = service.getReservationById(1L);

        verify(repository, atMostOnce()).getReservationById(1L);
        verify(catalogConnector, never()).getCity(any());
        verify(conversionService, atMostOnce()).convert(reservationModel, ReservationDTO.class);

        assertAll(() -> assertNotNull(result), () -> assertEquals(getReservationDTO(1L, "BUE", "MAD"), result));
    }

    @Tag("success-case")
    @Test
    @DisplayName("Check success when deleting reservation")
    void deleteReservation_shouldSucceed_whenReservationExists() throws InterruptedException {
        ReservationService service = new ReservationService(repository, conversionService, catalogConnector);

        Reservation reservationModel = getReservation(1L, "BUE", "MAD");
        when(repository.getReservationById(1L)).thenReturn(Optional.of(reservationModel));
        when(conversionService.convert(reservationModel, ReservationDTO.class))
                .thenReturn(getReservationDTO(1L, "BUE", "MAD"));
        doNothing().when(repository).delete(1L);

        Thread.sleep(500);
        service.delete(1L); // <- llamada correcta al método del service

        verify(repository, atMostOnce()).getReservationById(1L);
        verify(conversionService, atMostOnce()).convert(reservationModel, ReservationDTO.class);
        verify(repository, atMostOnce()).delete(1L);
        verify(catalogConnector, never()).getCity(any());
    }
}
