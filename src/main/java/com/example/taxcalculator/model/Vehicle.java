package com.example.taxcalculator.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Bus.class, name = "Bus"),
        @JsonSubTypes.Type(value = Car.class, name = "Car"),
        @JsonSubTypes.Type(value = Diplomat.class, name = "Diplomat"),
        @JsonSubTypes.Type(value = Emergency.class, name = "Emergency"),
        @JsonSubTypes.Type(value = Military.class, name = "Military"),
        @JsonSubTypes.Type(value = Motorbike.class, name = "Motorbike")
})
public interface Vehicle {
    String getVehicleType();
    String getVin();
}