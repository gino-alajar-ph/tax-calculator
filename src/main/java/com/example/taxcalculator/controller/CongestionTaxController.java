package com.example.taxcalculator.controller;

import com.example.taxcalculator.model.TaxCalculationRequest;
import com.example.taxcalculator.service.CongestionTaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Validated
public class CongestionTaxController {

    @Autowired
    private CongestionTaxService congestionTaxService;

    @PostMapping("/calculate")
    public ResponseEntity<List<Map<String, Object>>> calculate(@Valid @RequestBody List<TaxCalculationRequest> requests) throws ParseException {
        List<Map<String, Object>> response = congestionTaxService.calculateTaxes(requests);
        return ResponseEntity.ok(response);
    }
}
