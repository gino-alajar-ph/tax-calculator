package com.example.taxcalculator.service.calculator;

import com.example.taxcalculator.model.Vehicle;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface CongestionTaxCalculator {
    Map<String, Integer> calculateDailyTax(List<String> timestamps, Vehicle vehicle) throws ParseException;
}
