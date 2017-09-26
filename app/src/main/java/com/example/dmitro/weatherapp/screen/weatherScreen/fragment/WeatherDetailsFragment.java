package com.example.dmitro.weatherapp.screen.weatherScreen.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dmitro.weatherapp.BuildConfig;
import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.data.model.weather.WeatherResponse;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherDetailsFragment extends Fragment {

    public static final String FRAGMENT_KEY = "WeatherDetailsFragment";
    public static final String POSTFIX_BY_ICON_URL = ".png";
    public static final String POSTFIX_BY_TEMP_CELSIUS = "°";
    public static final String POSTFIX_BY_TEMP_PERCENT = " %";
    public static final String POSTFIX_BY_TEMP_MS = " m/s";


    @BindView(R.id.temperature_tv)
    public TextView temperatureTextView;

    @BindView(R.id.wind_tv)
    public TextView windTextView;

    @BindView(R.id.diapason_temp_tv)
    public TextView diapasonTempTextView;

    @BindView(R.id.humidity_tv)
    public TextView humidityTextView;

    public TextView cityNameByToolbar;

    @BindView(R.id.weather_details_tb)
    public Toolbar toolbar;

    @BindView(R.id.backg_weather_details_activity_iv)
    public ImageView backgroundWeather;

    @BindView(R.id.weather_icon_iv)
    public ImageView weatherIconImageView;


    public WeatherDetailsFragment() {
    }

    public static WeatherDetailsFragment newInstance(String param1, String param2) {
        WeatherDetailsFragment fragment = new WeatherDetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_details, container, false);
        ButterKnife.bind(this, view);
        if (getArguments() != null) {
            WeatherResponse wr = (WeatherResponse) getArguments().getSerializable(FRAGMENT_KEY);
            initToolbar(view);
            initFields(wr);
            applyBlur();

        }
        return view;
    }

    private static final float BLUR_RADIUS = 25f;

    private void applyBlur() {
        backgroundWeather.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                backgroundWeather.getViewTreeObserver().removeOnPreDrawListener(this);
                backgroundWeather.buildDrawingCache();
                Bitmap outputBitmap = backgroundWeather.getDrawingCache();
                RenderScript renderScript = RenderScript.create(getContext());
                Allocation tmpIn = Allocation.createFromBitmap(renderScript, backgroundWeather.getDrawingCache());
                Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);

                ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
                theIntrinsic.setRadius(BLUR_RADIUS);
                theIntrinsic.setInput(tmpIn);
                theIntrinsic.forEach(tmpOut);
                tmpOut.copyTo(outputBitmap);
                backgroundWeather.setImageBitmap(outputBitmap);

                return true;
            }
        });


    }

    //// TODO: 26.09.17 what be
    private void initFields(WeatherResponse wr) {
        if (wr != null) {
            cityNameByToolbar.setText(wr.getName());
            temperatureTextView.setText(wr.getMain().getTemp().toString() + POSTFIX_BY_TEMP_CELSIUS);
            humidityTextView.setText(wr.getMain().getHumidity().toString() + POSTFIX_BY_TEMP_PERCENT);
            windTextView.setText(wr.getWind().getSpeed().toString() + POSTFIX_BY_TEMP_MS);
            diapasonTempTextView.setText(wr.getMain().getTempMax() + POSTFIX_BY_TEMP_CELSIUS + " - " + wr.getMain().getTempMin() + POSTFIX_BY_TEMP_CELSIUS);
            String url = BuildConfig.ICON_WEATHER_URL + wr.getWeather().get(0).getIcon() + POSTFIX_BY_ICON_URL;
            Picasso.with(getContext()).load(url).into(weatherIconImageView);

        }
    }

    private void initToolbar(View view) {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        cityNameByToolbar = toolbar.findViewById(R.id.toolbar_title);
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            toolbar.getBackground().setAlpha(17);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });
        }
    }


}
