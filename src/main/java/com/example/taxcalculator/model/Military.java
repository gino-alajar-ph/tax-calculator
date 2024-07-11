package com.example.taxcalculator.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Military implements Vehicle {

    private String vin;

    public Military(String vin) {
        this.vin = vin;
    }

    @Override
    public String getVehicleType() {
        return "Military";
    }

    @Override
    public String getVin() {
        return vin;
    }
}
