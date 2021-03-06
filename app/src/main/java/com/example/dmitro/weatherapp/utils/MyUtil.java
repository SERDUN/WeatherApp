package com.example.dmitro.weatherapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;
import android.widget.ImageView;

import com.example.dmitro.weatherapp.data.model.weather.current.WeatherResponse;
import com.example.dmitro.weatherapp.data.model.weather.many_day.ResponseManyDayWeather;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by dmitro on 26.09.17.
 */

public class MyUtil {
    private static final String FORMAT = "yyyy-MM-dd";
    public static final String POSTFIX_BY_TEMP_CELSIUS = "°";
    private static final float BLUR_RADIUS = 25;


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

    public static void blur(ImageView backgroundWeather, Context context) {
        backgroundWeather.buildDrawingCache();
        Bitmap outputBitmap = backgroundWeather.getDrawingCache();
        RenderScript renderScript = RenderScript.create(context);
        Allocation tmpIn = Allocation.createFromBitmap(renderScript, backgroundWeather.getDrawingCache());
        Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);

        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        backgroundWeather.setImageBitmap(outputBitmap);
    }


    public static void blur(ImageView background, Context context, View view) {
        Bitmap bkg = background.getDrawingCache();

        int width = (int) (view.getMeasuredWidth());
        int height = (int) (view.getMeasuredHeight());

        Bitmap overlay = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(overlay);

        canvas.translate(-view.getLeft(), -view.getTop());
        canvas.drawBitmap(bkg, 0, 0, null);

        RenderScript rs = RenderScript.create(context);

        Allocation overlayAlloc = Allocation.createFromBitmap(
                rs, overlay);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(
                rs, overlayAlloc.getElement());

        blur.setInput(overlayAlloc);

        blur.setRadius(BLUR_RADIUS);

        blur.forEach(overlayAlloc);

        overlayAlloc.copyTo(overlay);

        view.setBackground(new BitmapDrawable(context.getResources(), overlay));

        rs.destroy();
    }


    public static Bitmap takeScreenShot(View view) {
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        view.buildDrawingCache();

        if(view.getDrawingCache() == null) return null;

        Bitmap snapshot = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();

        return snapshot;
    }

}
