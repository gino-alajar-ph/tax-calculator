package com.example.taxcalculator.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Bus implements Vehicle {

    private String vin;

    public Bus(String vin) {
        this.vin = vin;
    }

    @Override
    public String getVehicleType() {
        return "Bus";
    }

    @Override
    public String getVin() {
        return vin;
    }
}
