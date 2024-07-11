package com.example.taxcalculator.service;

import com.example.taxcalculator.exception.UnsupportedCityException;
import com.example.taxcalculator.model.Car;
import com.example.taxcalculator.model.TaxCalculationRequest;
import com.example.taxcalculator.model.Vehicle;
import com.example.taxcalculator.service.calculator.GothenburgCongestionTaxCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CongestionTaxServiceTest {

    @Mock
    private GothenburgCongestionTaxCalculator gothenburgCongestionTaxCalculator;

    @Mock
    private JsonDataLoader jsonDataLoader;

    @InjectMocks
    private CongestionTaxService congestionTaxService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        congestionTaxService = new CongestionTaxService(gothenburgCongestionTaxCalculator, jsonDataLoader);
    }

    @Test
    void calculateTaxes_validCityNonExemptedVehicle_returnsTax() throws ParseException {
        TaxCalculationRequest request = new TaxCalculationRequest();
        request.setCity("Gothenburg");
        Vehicle vehicle = new Car("1HGCM82633A654321");
        request.setVehicle(vehicle);
        request.setTimestamps(List.of("2013-02-08 06:27:00", "2013-02-08 06:20:27"));

        List<String> exemptedVehicles = new ArrayList<>();
        when(jsonDataLoader.getExemptedVehiclesForCity("Gothenburg")).thenReturn(exemptedVehicles);
        Map<String, Integer> taxResults = Map.of("2013-02-08", 26);
        when(gothenburgCongestionTaxCalculator.calculateDailyTax(anyList(), any(Vehicle.class))).thenReturn(taxResults);

        List<Map<String, Object>> result = congestionTaxService.calculateTaxes(List.of(request));

        assertNotNull(result);
        assertEquals(1, result.size());
        Map<String, Object> response = result.get(0);
        assertEquals("1HGCM82633A654321", response.get("vin"));
        assertEquals(false, response.get("exempted"));
        assertEquals(taxResults, response.get("taxResults"));

        verify(jsonDataLoader, times(1)).getExemptedVehiclesForCity("Gothenburg");
        verify(gothenburgCongestionTaxCalculator, times(1)).calculateDailyTax(anyList(), any(Vehicle.class));
    }

    @Test
    void calculateTaxes_validCityExemptedVehicle_returnsExempted() throws ParseException {
        TaxCalculationRequest request = new TaxCalculationRequest();
        request.setCity("Gothenburg");
        Vehicle vehicle = new Car("1HGCM82633A654321");
        request.setVehicle(vehicle);
        request.setTimestamps(List.of("2013-02-08 06:27:00", "2013-02-08 06:20:27"));

        List<String> exemptedVehicles = List.of("CAR");
        when(jsonDataLoader.getExemptedVehiclesForCity("Gothenburg")).thenReturn(exemptedVehicles);

        List<Map<String, Object>> result = congestionTaxService.calculateTaxes(List.of(request));

        assertNotNull(result);
        assertEquals(1, result.size());
        Map<String, Object> response = result.get(0);
        assertEquals("1HGCM82633A654321", response.get("vin"));
        assertEquals(true, response.get("exempted"));
        assertTrue(((Map<?, ?>) response.get("taxResults")).isEmpty());

        verify(jsonDataLoader, times(1)).getExemptedVehiclesForCity("Gothenburg");
        verify(gothenburgCongestionTaxCalculator, times(0)).calculateDailyTax(anyList(), any(Vehicle.class));
    }

    @Test
    void calculateTaxes_invalidCity_throwsException() throws ParseException {
        TaxCalculationRequest request = new TaxCalculationRequest();
        request.setCity("InvalidCity");
        Vehicle vehicle = new Car("1HGCM82633A654321");
        request.setVehicle(vehicle);
        request.setTimestamps(List.of("2013-02-08 06:27:00", "2013-02-08 06:20:27"));

        UnsupportedCityException exception = assertThrows(UnsupportedCityException.class, () -> {
            congestionTaxService.calculateTaxes(List.of(request));
        });

        assertEquals("No tax calculator found for city: InvalidCity", exception.getMessage());

        verify(jsonDataLoader, times(0)).getExemptedVehiclesForCity(anyString());
        verify(gothenburgCongestionTaxCalculator, times(0)).calculateDailyTax(anyList(), any(Vehicle.class));
    }
}
