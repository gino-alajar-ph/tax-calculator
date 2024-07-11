package com.example.taxcalculator.exception;

public class UnsupportedVehicleTypeException extends RuntimeException {
    public UnsupportedVehicleTypeException(String vehicleType) {
        super("Unsupported vehicle type: " + vehicleType);
    }
}
