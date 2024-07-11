package com.example.taxcalculator.service;

import com.example.taxcalculator.model.TaxPeriod;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class JsonDataLoader {

    private final Map<String, List<TaxPeriod>> cityTaxPeriods = new HashMap<>();
    private final Map<String, List<Date>> cityHolidays = new HashMap<>();
    private final Map<String, List<String>> cityExemptedVehicles = new HashMap<>();

    @PostConstruct
    public void loadJsonData() throws IOException, ParseException {
        ObjectMapper mapper = new ObjectMapper();

        try (InputStream taxRatesStream = getClass().getResourceAsStream("/tax-rates.json")) {
            Map<String, List<TaxPeriod>> taxRates = mapper.readValue(taxRatesStream, new TypeReference<Map<String, List<TaxPeriod>>>() {});
            cityTaxPeriods.putAll(taxRates);
        }

        try (InputStream holidaysStream = getClass().getResourceAsStream("/holidays.json")) {
            Map<String, List<String>> holidays = mapper.readValue(holidaysStream, new TypeReference<Map<String, List<String>>>() {});
            for (Map.Entry<String, List<String>> entry : holidays.entrySet()) {
                List<Date> holidayDates = new ArrayList<>();
                for (String dateString : entry.getValue()) {
                    holidayDates.add(new SimpleDateFormat("yyyy-MM-dd").parse(dateString));
                }
                cityHolidays.put(entry.getKey(), holidayDates);
            }
        }

        try (InputStream exemptedVehiclesStream = getClass().getResourceAsStream("/exempted-vehicles.json")) {
            Map<String, List<String>> exemptedVehicles = mapper.readValue(exemptedVehiclesStream, new TypeReference<Map<String, List<String>>>() {});
            cityExemptedVehicles.putAll(exemptedVehicles);
        }
    }

    public List<TaxPeriod> getTaxPeriodsForCity(String city) {
        return cityTaxPeriods.getOrDefault(city, Collections.emptyList());
    }

    public List<Date> getHolidaysForCity(String city) {
        return cityHolidays.getOrDefault(city, Collections.emptyList());
    }

    public List<String> getExemptedVehiclesForCity(String city) {
        return cityExemptedVehicles.getOrDefault(city, Collections.emptyList());
    }
}
