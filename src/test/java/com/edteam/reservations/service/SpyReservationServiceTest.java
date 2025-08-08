package com.edteam.reservations.service;

import com.edteam.reservations.connector.CatalogConnector;
import com.edteam.reservations.dto.ReservationDTO;
import com.edteam.reservations.enums.APIError;
import com.edteam.reservations.exception.EdteamException;
import com.edteam.reservations.model.Reservation;
import com.edteam.reservations.repository.ReservationRepository;
import org.junit.jupiter.api.*;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.core.convert.ConversionService;

import java.util.Optional;

import static com.edteam.reservations.util.ReservationUtils.getReservationDTO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tags(@Tag("service"))
@DisplayName("Check the functionality of ReservationService using Spy")
public class SpyReservationServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpyReservationServiceTest.class);

    @Spy
    ReservationRepository repository = new ReservationRepository(); // usando implementación real

    @Mock
    ConversionService conversionService;

    @Mock
    CatalogConnector catalogConnector;

    @BeforeEach
    void initializeEachTest() {
        LOGGER.info(() -> "Metodo initializeEachTest");
        MockitoAnnotations.openMocks(this);
    }

    @Tag("error-case")
    @Test
    @DisplayName("Should throw exception if reservation not found using Spy")
    void getReservation_shouldThrow_whenNotFound() {
        // Given
        ReservationService service = new ReservationService(repository, conversionService, catalogConnector);

        // Cuando buscamos un ID inexistente, no necesitas stub porque el repo real ya devuelve Optional.empty()
        // When
        EdteamException exception = assertThrows(EdteamException.class, () -> {
            service.getReservationById(99L); // ID que no existe
        });

        // Then
        verify(repository, atMostOnce()).getReservationById(99L);
        verify(conversionService, never()).convert(any(), any());

        assertAll(() -> assertNotNull(exception),
                () -> assertEquals(APIError.RESERVATION_NOT_FOUND.getMessage(), exception.getDescription()),
                () -> assertEquals(APIError.RESERVATION_NOT_FOUND.getHttpStatus(), exception.getStatus()));
    }

    @Tag("success-case")
    @Test
    @DisplayName("Should return reservationDTO using real repository and spy")
    void getReservation_shouldReturnDTO_whenFoundWithSpy() {
        // Given
        ReservationService service = new ReservationService(repository, conversionService, catalogConnector);

        // Obtén la reserva directamente de la clase util
        Reservation expectedReservation = com.edteam.reservations.util.ReservationUtils.getReservation(1L, "BUE",
                "MAD");
        ReservationDTO expectedDTO = getReservationDTO(1L, "BUE", "MAD");

        // No invocas el repo aquí, solo haces stub de lo que devolverá el conversionService
        when(conversionService.convert(any(Reservation.class), eq(ReservationDTO.class))).thenReturn(expectedDTO);

        // When
        ReservationDTO result = service.getReservationById(1L);

        // Then
        verify(repository, atMostOnce()).getReservationById(1L);
        verify(conversionService, atMostOnce()).convert(any(Reservation.class), eq(ReservationDTO.class));
        verify(catalogConnector, never()).getCity(any());

        assertAll(() -> assertNotNull(result), () -> assertEquals(expectedDTO, result));
    }

}
