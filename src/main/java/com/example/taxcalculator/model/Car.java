package com.example.taxcalculator.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Car implements Vehicle {

    private String vin;

    public Car(String vin) {
        this.vin = vin;
    }

    @Override
    public String getVehicleType() {
        return "Car";
    }

    @Override
    public String getVin() {
        return vin;
    }
}
