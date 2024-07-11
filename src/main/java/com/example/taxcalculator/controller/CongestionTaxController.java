package com.example.taxcalculator.controller;

import com.example.taxcalculator.model.TaxCalculationRequest;
import com.example.taxcalculator.model.Vehicle;
import com.example.taxcalculator.service.CongestionTaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Validated
public class CongestionTaxController {

    @Autowired
    private CongestionTaxService congestionTaxService;

    @PostMapping("/calculate")
    public ResponseEntity<Map<String, Integer>> calculate(@Valid @RequestBody TaxCalculationRequest request) throws ParseException {
        Vehicle vehicle = request.getVehicle();
        Map<String, Integer> dailyTaxes = congestionTaxService.calculateTax(
                request.getCity(),
                vehicle,
                request.getTimestamps()
        );
        return ResponseEntity.ok(dailyTaxes);
    }
}
