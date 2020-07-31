package com.firstapp.helpapp;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {

    Date date1;
    Date date2;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");


    public Long numDaysBetweenNowDate (String date_ddmmyyy) throws ParseException {
        Date currentTime = Calendar.getInstance().getTime();
        String finalDate = dateFormat.format(currentTime);
        date1 = dateFormat.parse(date_ddmmyyy);
        date2 = dateFormat.parse(finalDate);
        assert date1 != null && date2 != null;
        long difference = Math.abs(date1.getTime() - date2.getTime());
        return difference / (24 * 60 * 60 * 1000);
    }
}
