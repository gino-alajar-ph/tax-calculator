package com.example.taxcalculator.service;

import com.example.taxcalculator.exception.UnsupportedCityException;
import com.example.taxcalculator.model.TaxCalculationRequest;
import com.example.taxcalculator.model.Vehicle;
import com.example.taxcalculator.service.calculator.CongestionTaxCalculator;
import com.example.taxcalculator.service.calculator.GothenburgCongestionTaxCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

@Service
public class CongestionTaxService {

    private final JsonDataLoader jsonDataLoader;
    private final Map<String, CongestionTaxCalculator> calculators = new HashMap<>();

    @Autowired
    public CongestionTaxService(GothenburgCongestionTaxCalculator gothenburgCongestionTaxCalculator, JsonDataLoader jsonDataLoader) {
        this.jsonDataLoader = jsonDataLoader;
        calculators.put("Gothenburg", gothenburgCongestionTaxCalculator);
    }

    public List<Map<String, Object>> calculateTaxes(List<TaxCalculationRequest> requests) throws ParseException {
        List<Map<String, Object>> response = new ArrayList<>();

        for (TaxCalculationRequest request : requests) {
            Vehicle vehicle = request.getVehicle();
            CongestionTaxCalculator calculator = calculators.get(request.getCity());
            if (calculator == null) {
                throw new UnsupportedCityException(request.getCity());
            }

            Map<String, Object> result = new HashMap<>();
            result.put("vin", vehicle.getVin());

            List<String> exemptedVehicles = jsonDataLoader.getExemptedVehiclesForCity(request.getCity());
            if (exemptedVehicles.contains(vehicle.getVehicleType().toUpperCase())) {
                result.put("exempted", true);
                result.put("taxResults", Collections.emptyMap());
            } else {
                result.put("exempted", false);
                Map<String, Integer> taxResults = calculator.calculateDailyTax(
                        request.getTimestamps(),
                        vehicle
                );
                result.put("taxResults", taxResults);
            }

            response.add(result);
        }

        return response;
    }
}
