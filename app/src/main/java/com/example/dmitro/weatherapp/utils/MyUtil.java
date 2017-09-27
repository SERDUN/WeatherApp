package com.example.dmitro.weatherapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.example.dmitro.weatherapp.data.model.weather.WeatherResponse;
import com.example.dmitro.weatherapp.data.model.weather.many_day.ResponseManyDayWeather;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    public static final ArrayList<HashMap<String, ArrayList<WeatherResponse>>> groupByDays(ResponseManyDayWeather responseManyDayWeather) {
        String currenDay = "";
        ArrayList<WeatherResponse> weatherForDay = null;
        HashMap<String, ArrayList<WeatherResponse>> currentHasMap = null;
        ArrayList<HashMap<String, ArrayList<WeatherResponse>>> groupWeather = new ArrayList<>();


        for (WeatherResponse weather : responseManyDayWeather.getList()) {
            if (currenDay.isEmpty() || !currenDay.equals(getNameDayForDate(weather.getDate()))) {
                currenDay = getNameDayForDate(weather.getDate());
                weatherForDay = new ArrayList<>();
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

    public static void applyBlur(ImageView backgroundWeather, Context context, float radius) {
        backgroundWeather.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                backgroundWeather.getViewTreeObserver().removeOnPreDrawListener(this);
                backgroundWeather.buildDrawingCache();
                Bitmap outputBitmap = backgroundWeather.getDrawingCache();
                RenderScript renderScript = RenderScript.create(context);
                Allocation tmpIn = Allocation.createFromBitmap(renderScript, backgroundWeather.getDrawingCache());
                Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);

                ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
                theIntrinsic.setRadius(radius);
                theIntrinsic.setInput(tmpIn);
                theIntrinsic.forEach(tmpOut);
                tmpOut.copyTo(outputBitmap);
                backgroundWeather.setImageBitmap(outputBitmap);

                return true;
            }
        });
    }
}
