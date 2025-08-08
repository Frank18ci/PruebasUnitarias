package com.edteam.reservations.repository;

import com.edteam.reservations.model.Reservation;
import org.junit.*;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


import static com.edteam.reservations.util.ReservationUtils.getReservation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class OldReservationRepositoryTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(OldReservationRepositoryTest.class);
    @Before
    public void initialize_each_test() {
        LOGGER.info(() -> "Metodo initialize_each_test");
    }
    @After
    public void destroy_each_test() {
        LOGGER.info(() -> "Metodo destroy_each_test");
    }
    @BeforeClass
    public static void initialize_class() {
        LOGGER.info(() -> "Metodo initialize_class");
    }
    @AfterClass
    public static void destroy_class() {
        LOGGER.info(() -> "Metodo destroy_class");
    }
    @Test
    public void getReservation_should_return_the_information() {
        //Given
        ReservationRepository repository = new ReservationRepository();

        //When
        Optional<Reservation> result = repository.getReservationById(1L);

        //Then
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(getReservation(1L, "EZE", "MIA"), result.get());
    }

}
