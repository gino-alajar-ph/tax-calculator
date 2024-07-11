package com.example.taxcalculator.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class TaxPeriod {
    private String start;
    private String end;
    private int amount;

    @JsonCreator
    public TaxPeriod(@JsonProperty("start") String start, @JsonProperty("end") String end, @JsonProperty("amount") int amount) {
        this.start = start;
        this.end = end;
        this.amount = amount;
    }

    public boolean isWithinPeriod(Date date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String timeString = dateFormat.format(date);
        Date time = dateFormat.parse(timeString);
        Date startTime = dateFormat.parse(this.start);
        Date endTime = dateFormat.parse(this.end);

        return time.after(startTime) && time.before(endTime);
    }
}
