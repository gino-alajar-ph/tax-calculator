package com.example.taxcalculator.controller;

import com.example.taxcalculator.model.Car;
import com.example.taxcalculator.model.TaxCalculationRequest;
import com.example.taxcalculator.model.Vehicle;
import com.example.taxcalculator.service.CongestionTaxService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CongestionTaxControllerTest {

    @Mock
    private CongestionTaxService congestionTaxService;

    @InjectMocks
    private CongestionTaxController congestionTaxController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(congestionTaxController).build();
    }

    @Test
    void calculateTax_validRequest_returnsTax() throws Exception {
        TaxCalculationRequest request = new TaxCalculationRequest();
        request.setCity("Gothenburg");
        request.setVehicle(new Car());
        request.setTimestamps(List.of("2013-02-08 06:27:00", "2013-02-08 07:27:00"));

        when(congestionTaxService.calculateTax(anyString(), any(Vehicle.class), anyList()))
                .thenReturn(Map.of("2013-02-08", 26));

        mockMvc.perform(post("/api/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"city\": \"Gothenburg\", \"vehicle\": {\"type\": \"Car\"}, \"timestamps\": [\"2013-02-08 06:27:00\", \"2013-02-08 07:27:00\"]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['2013-02-08']").value(26));

        verify(congestionTaxService, times(1)).calculateTax(anyString(), any(Vehicle.class), anyList());
    }
}
