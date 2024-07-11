package com.example.taxcalculator.controller;

import com.example.taxcalculator.model.Car;
import com.example.taxcalculator.model.TaxCalculationRequest;

import com.example.taxcalculator.service.CongestionTaxService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyList;

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
    void calculate_validRequest_returnsTax() throws Exception {
        TaxCalculationRequest request = new TaxCalculationRequest();
        request.setCity("Gothenburg");
        request.setVehicle(new Car("1HGCM82633A654321"));
        request.setTimestamps(List.of(
                "2013-01-14 21:00:00",
                "2013-01-15 21:00:00",
                "2013-02-07 06:23:27",
                "2013-02-07 15:27:00",
                "2013-02-08 06:27:00",
                "2013-02-08 06:20:27",
                "2013-02-08 14:35:00",
                "2013-02-08 15:29:00",
                "2013-02-08 15:47:00",
                "2013-02-08 16:01:00",
                "2013-02-08 16:48:00",
                "2013-02-08 17:49:00",
                "2013-02-08 18:29:00",
                "2013-02-08 18:35:00",
                "2013-03-26 14:25:00",
                "2013-03-28 14:07:27"
        ));

        Map<String, Object> responseMap = Map.of(
                "vin", "1HGCM82633A654321",
                "taxResults", Map.of(
                        "2013-01-14", 0,
                        "2013-01-15", 0,
                        "2013-02-07", 13,
                        "2013-02-08", 26,
                        "2013-03-26", 8,
                        "2013-03-28", 8
                )
        );

        when(congestionTaxService.calculateTaxes(anyList())).thenReturn(Collections.singletonList(responseMap));

        mockMvc.perform(post("/api/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{" +
                                "\"city\": \"Gothenburg\"," +
                                "\"vehicle\": {\"type\": \"Car\", \"vin\": \"1HGCM82633A654321\"}," +
                                "\"timestamps\": [" +
                                "\"2013-01-14 21:00:00\"," +
                                "\"2013-01-15 21:00:00\"," +
                                "\"2013-02-07 06:23:27\"," +
                                "\"2013-02-07 15:27:00\"," +
                                "\"2013-02-08 06:27:00\"," +
                                "\"2013-02-08 06:20:27\"," +
                                "\"2013-02-08 14:35:00\"," +
                                "\"2013-02-08 15:29:00\"," +
                                "\"2013-02-08 15:47:00\"," +
                                "\"2013-02-08 16:01:00\"," +
                                "\"2013-02-08 16:48:00\"," +
                                "\"2013-02-08 17:49:00\"," +
                                "\"2013-02-08 18:29:00\"," +
                                "\"2013-02-08 18:35:00\"," +
                                "\"2013-03-26 14:25:00\"," +
                                "\"2013-03-28 14:07:27\"" +
                                "]" +
                                "}]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].vin").value("1HGCM82633A654321"))
                .andExpect(jsonPath("$[0].taxResults['2013-01-14']").value(0))
                .andExpect(jsonPath("$[0].taxResults['2013-01-15']").value(0))
                .andExpect(jsonPath("$[0].taxResults['2013-02-07']").value(13))
                .andExpect(jsonPath("$[0].taxResults['2013-02-08']").value(26))
                .andExpect(jsonPath("$[0].taxResults['2013-03-26']").value(8))
                .andExpect(jsonPath("$[0].taxResults['2013-03-28']").value(8));

        verify(congestionTaxService, times(1)).calculateTaxes(anyList());
    }
}
