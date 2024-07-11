package com.example.taxcalculator.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Motorbike implements Vehicle {

    private String vin;

    public Motorbike(String vin) {
        this.vin = vin;
    }

    @Override
    public String getVehicleType() {
        return "Motorbike";
    }

    @Override
    public String getVin() {
        return vin;
    }
}
