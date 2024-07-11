package com.example.taxcalculator.exception;

public class UnsupportedCityException extends RuntimeException {
    public UnsupportedCityException(String city) {
        super("No tax calculator found for city: " + city);
    }
}
