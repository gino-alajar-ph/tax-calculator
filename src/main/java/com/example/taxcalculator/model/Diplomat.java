package com.example.taxcalculator.model;

public class Diplomat implements Vehicle {

    private String vin;

    public Diplomat(String vin) {
        this.vin = vin;
    }

    @Override
    public String getVehicleType() {
        return "Diplomat";
    }

    @Override
    public String getVin() {
        return vin;
    }
}
