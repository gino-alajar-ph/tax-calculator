package com.example.taxcalculator.service.calculator;

import com.example.taxcalculator.model.Car;
import com.example.taxcalculator.model.TaxPeriod;
import com.example.taxcalculator.model.Vehicle;
import com.example.taxcalculator.service.JsonDataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    }

    @Test
    void calculateDailyTax_nonExemptedVehicle_returnsTax() throws ParseException {
        Vehicle vehicle = new Car("1HGCM82633A654321");
        List<String> timestamps = Arrays.asList(
                "2013-02-08 06:27:00",
                "2013-02-08 06:20:27",
                "2013-02-08 14:35:00",
                "2013-02-08 15:29:00",
                "2013-02-08 15:47:00",
                "2013-02-08 16:01:00",
                "2013-02-08 16:48:00",
                "2013-02-08 17:49:00",
                "2013-02-08 18:29:00",
                "2013-02-08 18:35:00"
        );

        List<Date> holidays = new ArrayList<>();
        when(jsonDataLoader.getHolidaysForCity("Gothenburg")).thenReturn(holidays);

        List<TaxPeriod> taxPeriods = Arrays.asList(
                new TaxPeriod("06:00:00", "06:29:59", 8),
                new TaxPeriod("06:30:00", "06:59:59", 13),
                new TaxPeriod("07:00:00", "07:59:59", 18),
                new TaxPeriod("08:00:00", "08:29:59", 13),
                new TaxPeriod("08:30:00", "14:59:59", 8),
                new TaxPeriod("15:00:00", "15:29:59", 13),
                new TaxPeriod("15:30:00", "16:59:59", 18),
                new TaxPeriod("17:00:00", "17:59:59", 13),
                new TaxPeriod("18:00:00", "18:29:59", 8),
                new TaxPeriod("18:30:00", "05:59:59", 0)
        );
        when(jsonDataLoader.getTaxPeriodsForCity("Gothenburg")).thenReturn(taxPeriods);

        List<String> exemptedVehicles = Arrays.asList("BUS", "EMERGENCY", "DIPLOMAT");
        when(jsonDataLoader.getExemptedVehiclesForCity("Gothenburg")).thenReturn(exemptedVehicles);

        Map<String, Integer> taxResults = calculator.calculateDailyTax(timestamps, vehicle);

        assertNotNull(taxResults);
        assertEquals(1, taxResults.size());
        assertEquals(39, taxResults.get("2013-02-08")); // Change this value according to the expected result

        verify(jsonDataLoader, atLeastOnce()).getHolidaysForCity("Gothenburg");
        verify(jsonDataLoader, atLeastOnce()).getTaxPeriodsForCity("Gothenburg");
    }

    @Test
    void calculateDailyTax_onHoliday_returnsNoTax() throws ParseException {
        Vehicle vehicle = new Car("1HGCM82633A654321");
        List<String> timestamps = Arrays.asList(
                "2013-02-08 06:27:00"
        );

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date holiday = format.parse("2013-02-08 00:00:00");
        List<Date> holidays = Arrays.asList(holiday);
        when(jsonDataLoader.getHolidaysForCity("Gothenburg")).thenReturn(holidays);

        Map<String, Integer> taxResults = calculator.calculateDailyTax(timestamps, vehicle);

        assertNotNull(taxResults);
        assertEquals(1, taxResults.size());
        assertEquals(0, taxResults.get("2013-02-08"));

        verify(jsonDataLoader, atLeastOnce()).getHolidaysForCity("Gothenburg");
    }

    @Test
    void calculateDailyTax_onWeekend_returnsZeroTax() throws ParseException {
        Vehicle vehicle = new Car("1HGCM82633A654321");
        List<String> timestamps = Arrays.asList(
                "2013-02-09 06:27:00" // 2013-02-09 is a Saturday
        );

        Map<String, Integer> taxResults = calculator.calculateDailyTax(timestamps, vehicle);

        assertNotNull(taxResults);
        assertEquals(1, taxResults.size());
        assertEquals(0, taxResults.get("2013-02-09"));
    }

}
