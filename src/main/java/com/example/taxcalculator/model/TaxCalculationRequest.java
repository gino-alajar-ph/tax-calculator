package com.example.taxcalculator.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class TaxCalculationRequest {
    @NotEmpty(message = "City is required")
    private String city;

    @NotNull(message = "Vehicle is required")
    private Vehicle vehicle;

    @NotEmpty(message = "Timestamps are required")
    private List<@NotEmpty String> timestamps;
}
