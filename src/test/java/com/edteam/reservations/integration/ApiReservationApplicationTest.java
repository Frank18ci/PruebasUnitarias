package com.edteam.reservations.integration;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.web.context.WebApplicationContext;

@Tags(@Tag("integration"))
@DisplayName("Integration tests for the Reservation API application")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiReservationApplicationTest {
    private MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    @Tag("success-case")
    @DisplayName("Should get a reservation by ID")
    @Test
    void should_get_a_reservation_by_id() throws Exception {
        mockMvc.perform(get("/reservation/1")
                .contentType("application/json"))
                .andExpect(status().is2xxSuccessful()
        );
    }
    @Tag("error-case")
    @DisplayName("Should not get a reservation by non-existing ID")
    @Test
    void should_not_get_a_reservation_by_id() throws Exception {
        mockMvc.perform(get("/reservation/1000")
                .contentType("application/json"))
                .andExpect(status().is4xxClientError()
        );
    }

}
