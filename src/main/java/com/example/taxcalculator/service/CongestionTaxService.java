package com.example.taxcalculator.service;

import com.example.taxcalculator.exception.UnsupportedCityException;
import com.example.taxcalculator.model.Vehicle;
import com.example.taxcalculator.service.calculator.CongestionTaxCalculator;
import com.example.taxcalculator.service.calculator.GothenburgCongestionTaxCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CongestionTaxService {

    private final Map<String, CongestionTaxCalculator> calculators = new HashMap<>();

    @Autowired
    public CongestionTaxService(GothenburgCongestionTaxCalculator gothenburgCongestionTaxCalculator) {
        calculators.put("Gothenburg", gothenburgCongestionTaxCalculator);
    }

    public Map<String, Integer> calculateTax(String city, Vehicle vehicle, List<String> timestamps) throws ParseException {
        CongestionTaxCalculator calculator = calculators.get(city.trim());

        if (calculator == null) {
            throw new UnsupportedCityException(city);
        }

        return calculator.calculateDailyTax(timestamps, vehicle);
    }
}
