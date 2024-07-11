package com.example.taxcalculator.service.calculator;

import com.example.taxcalculator.model.TaxPeriod;
import com.example.taxcalculator.model.Vehicle;
import com.example.taxcalculator.service.JsonDataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class GothenburgCongestionTaxCalculator implements CongestionTaxCalculator {

    private final JsonDataLoader jsonDataLoader;

    @Autowired
    public GothenburgCongestionTaxCalculator(JsonDataLoader jsonDataLoader) {
        this.jsonDataLoader = jsonDataLoader;
    }

    private boolean isWeekend(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY);
    }

    private boolean isHoliday(Date date) {
        List<Date> holidays = jsonDataLoader.getHolidaysForCity("Gothenburg");
        return holidays.contains(date);
    }

    private int getTax(Date date) throws ParseException {
        if (isWeekend(date) || isHoliday(date)) return 0;

        List<TaxPeriod> taxPeriods = jsonDataLoader.getTaxPeriodsForCity("Gothenburg");
        for (TaxPeriod period : taxPeriods) {
            if (period.isWithinPeriod(date)) {
                return period.getAmount();
            }
        }
        return 0;
    }

    @Override
    public Map<String, Integer> calculateDailyTax(List<String> timestamps, Vehicle vehicle) throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, List<Date>> groupedByDate = new HashMap<>();

        for (String timestamp : timestamps) {
            Date date = format.parse(timestamp);
            String day = dayFormat.format(date);
            groupedByDate.computeIfAbsent(day, k -> new ArrayList<>()).add(date);
        }

        Map<String, Integer> dailyTaxes = new HashMap<>();

        for (Map.Entry<String, List<Date>> entry : groupedByDate.entrySet()) {
            String day = entry.getKey();
            List<Date> times = entry.getValue();
            times.sort(Comparator.naturalOrder());
            int dailyTotal = 0;
            int maxTax = 0;
            Date prevTime = null;

            for (Date time : times) {
                int tax = getTax(time);
                if (prevTime == null || (time.getTime() - prevTime.getTime()) > 60 * 60 * 1000) {
                    dailyTotal += maxTax;
                    maxTax = tax;
                } else {
                    if (tax > maxTax) {
                        maxTax = tax;
                    }
                }
                prevTime = time;
            }
            dailyTotal += maxTax;
            dailyTotal = Math.min(dailyTotal, 60);
            dailyTaxes.put(day, dailyTotal);
        }

        return dailyTaxes;
    }

    @Override
    public String toString() {
        return "GothenburgCongestionTaxCalculator";
    }

}
