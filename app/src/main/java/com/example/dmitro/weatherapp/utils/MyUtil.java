package com.example.dmitro.weatherapp.utils;

import android.util.Log;

import com.example.dmitro.weatherapp.data.model.weather.WeatherResponse;
import com.example.dmitro.weatherapp.data.model.weather.many_day.ResponseManyDayWeather;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by dmitro on 26.09.17.
 */

public class MyUtil {
    private static final String FORMAT = "yyyy-MM-dd";
    public static final String POSTFIX_BY_TEMP_CELSIUS = "°";


    public static String getNameDayForDate(String date) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(FORMAT);
            Date parsedDate = dateFormat.parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(parsedDate);
            return cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static final LinkedList<HashMap<String, List<WeatherResponse>>> groupByDays(ResponseManyDayWeather responseManyDayWeather) {
        String currenDay = "";
        LinkedList<WeatherResponse> weatherForDay = null;
        HashMap<String, List<WeatherResponse>> currentHasMap = null;
        LinkedList<HashMap<String, List<WeatherResponse>>> groupWeather = new LinkedList<>();


        for (WeatherResponse weather : responseManyDayWeather.getList()) {
            if (currenDay.isEmpty() || !currenDay.equals(getNameDayForDate(weather.getDate()))) {
                currenDay = getNameDayForDate(weather.getDate());
                weatherForDay = new LinkedList<>();
                currentHasMap = new HashMap<>();
                currentHasMap.put(currenDay, weatherForDay);
                groupWeather.add(currentHasMap);
            }
            weatherForDay.add(weather);


        }

        return groupWeather;

    }

    public static String createTemperatureDiapasonString(double T1, double T2) {
        return T1 + POSTFIX_BY_TEMP_CELSIUS + " - " + T2 + POSTFIX_BY_TEMP_CELSIUS;

    }
}
