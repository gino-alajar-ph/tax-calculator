package com.example.taxcalculator.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Emergency implements Vehicle {

    private String vin;

    public Emergency(String vin) {
        this.vin = vin;
    }

    @Override
    public String getVehicleType() {
        return "Emergency";
    }

    @Override
    public String getVin() {
        return vin;
    }
}
