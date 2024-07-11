package com.example.taxcalculator.service;

import com.example.taxcalculator.exception.UnsupportedCityException;
import com.example.taxcalculator.model.Car;
import com.example.taxcalculator.model.Vehicle;
import com.example.taxcalculator.service.calculator.GothenburgCongestionTaxCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CongestionTaxServiceTest {

    @Mock
    private GothenburgCongestionTaxCalculator gothenburgCongestionTaxCalculator;

    @InjectMocks
    private CongestionTaxService congestionTaxService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        congestionTaxService = new CongestionTaxService(gothenburgCongestionTaxCalculator);
    }

    @Test
    void calculateTax_validCity_returnsTax() throws ParseException {
        String city = "Gothenburg";
        Vehicle vehicle = new Car();
        List<String> timestamps = Arrays.asList("2013-02-08 06:27:00", "2013-02-08 06:20:27");

        when(gothenburgCongestionTaxCalculator.calculateDailyTax(anyList(), any(Vehicle.class)))
                .thenReturn(Map.of("2013-02-08", 13));

        Map<String, Integer> result = congestionTaxService.calculateTax(city, vehicle, timestamps);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(13, result.get("2013-02-08"));

        verify(gothenburgCongestionTaxCalculator, times(1)).calculateDailyTax(anyList(), any(Vehicle.class));
    }

    @Test
    void calculateTax_invalidCity_throwsException() {
        String city = "InvalidCity";
        Vehicle vehicle = new Car();
        List<String> timestamps = Arrays.asList("2013-02-08 06:27:00", "2013-02-08 06:20:27");

        UnsupportedCityException exception = assertThrows(UnsupportedCityException.class, () -> {
            congestionTaxService.calculateTax(city, vehicle, timestamps);
        });

        assertEquals("No tax calculator found for city: InvalidCity", exception.getMessage());
    }
}
