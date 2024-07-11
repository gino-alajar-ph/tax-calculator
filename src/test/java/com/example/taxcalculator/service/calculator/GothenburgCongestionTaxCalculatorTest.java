package com.example.taxcalculator.service.calculator;

import com.example.taxcalculator.model.TaxPeriod;
import com.example.taxcalculator.model.Vehicle;
import com.example.taxcalculator.model.Car;
import com.example.taxcalculator.service.JsonDataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GothenburgCongestionTaxCalculatorTest {

    @Mock
    private JsonDataLoader jsonDataLoader;

    @InjectMocks
    private GothenburgCongestionTaxCalculator calculator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        calculator = new GothenburgCongestionTaxCalculator(jsonDataLoader);
    }

    @Test
    void calculateDailyTax_exemptedVehicle_returnsZero() throws ParseException {
        Vehicle vehicle = new Car();
        List<String> timestamps = Arrays.asList("2013-02-08 06:27:00");

        when(jsonDataLoader.getExemptedVehiclesForCity("Gothenburg")).thenReturn(List.of("CAR"));

        Map<String, Integer> result = calculator.calculateDailyTax(timestamps, vehicle);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(jsonDataLoader, times(1)).getExemptedVehiclesForCity("Gothenburg");
    }

    @Test
    void calculateDailyTax_nonExemptedVehicle_returnsTax() throws ParseException {
        Vehicle vehicle = new Car();
        List<String> timestamps = Arrays.asList("2013-02-08 06:27:00", "2013-02-08 07:27:00");

        when(jsonDataLoader.getExemptedVehiclesForCity("Gothenburg")).thenReturn(Collections.emptyList());
        when(jsonDataLoader.getTaxPeriodsForCity("Gothenburg")).thenReturn(
                Arrays.asList(
                        new TaxPeriod("06:00:00", "06:29:59", 8),
                        new TaxPeriod("07:00:00", "07:59:59", 18)
                )
        );

        Map<String, Integer> result = calculator.calculateDailyTax(timestamps, vehicle);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(18, result.get("2013-02-08"));

        verify(jsonDataLoader, times(1)).getExemptedVehiclesForCity("Gothenburg");
        verify(jsonDataLoader, atLeastOnce()).getTaxPeriodsForCity("Gothenburg");
    }
}
