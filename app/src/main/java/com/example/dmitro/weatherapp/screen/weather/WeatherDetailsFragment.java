package com.example.dmitro.weatherapp.screen.weather;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.data.model.weather.current.WeatherResponse;
import com.example.dmitro.weatherapp.screen.failure.FailureDialog;
import com.example.dmitro.weatherapp.screen.weather.fragment.WeatherCurrentDetailsFragment;
import com.example.dmitro.weatherapp.screen.weather.fragment.WeatherListFragment;
import com.example.dmitro.weatherapp.utils.MyUtil;
import com.example.dmitro.weatherapp.utils.callback.Action0;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class WeatherDetailsFragment extends Fragment implements WeatherDetailsContract.View {

    public final static String WEATHER_DETAILS_FRAGMENT_KEY = "WeatherDetailsFragment";
    public final static String WEATHER_DETAILS_FRAGMENT_NAME_CITY_KEY = "WeatherDetailsFragmentNameCity";

    private WeatherDetailsContract.Presenter presenter;

    private int PLACE_PICKER_REQUEST = 1;

    private PlacePicker.IntentBuilder builder;

    @BindView(R.id.toolbar_title)
    public TextView titleTv;

    @BindView(R.id.backg_weather_details_activity_iv)
    public ImageView backgroundWeather;

    @BindView(R.id.weather_details_pb)
    public ProgressBar progressBar;

    @BindView(R.id.content_weather_details_lv)
    public android.view.View contentView;

    @BindView(R.id.back_btn)
    public ImageButton backButton;

    @BindView(R.id.location_button)
    public ImageButton locationButton;


    private FailureDialog failureDialog;

    private Action0 listener;


    public WeatherDetailsFragment() {
    }


    public static WeatherDetailsFragment newInstance() {
        WeatherDetailsFragment fragment = new WeatherDetailsFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {
        android.view.View view = inflater.inflate(R.layout.activity_weather, container, false);
        ButterKnife.bind(this, view);
        new WeatherDetailsPresenter(this);
        init();
        initToolbar();
        applyBlur();

        if (savedInstanceState == null) {
            presenter.getCurrentWeather();
            presenter.getCurrentWeatherByFiveDays();
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Action0) {
            listener = (Action0) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    private void initToolbar() {
        backButton.setImageResource(R.drawable.ic_menu_black_24dp);
        backButton.setOnClickListener(view -> {
            listener.call();

        });
    }


    private void applyBlur() {
        backgroundWeather.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                backgroundWeather.getViewTreeObserver().removeOnPreDrawListener(this);
                MyUtil.blur(backgroundWeather, getContext());
                return true;
            }
        });

    }


    private void init() {
        builder = new PlacePicker.IntentBuilder();

        failureDialog = new FailureDialog();
        failureDialog.setCallback(() -> {
            failureDialog.dismiss();
            presenter.getCurrentWeather();
            presenter.getCurrentWeatherByFiveDays();
        });

        presenter.init();
        builder = new PlacePicker.IntentBuilder();
        locationButton.setOnClickListener(onClick -> {
            try {
                locationButton.setEnabled(false);
                startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, getContext());
                presenter.getWeatherForLanLon(place.getLatLng().latitude, place.getLatLng().longitude);
                presenter.getWeatherByFiveDayForLatLon(place.getLatLng().latitude, place.getLatLng().longitude);

            }
            locationButton.setEnabled(true);

        }
    }

    @Override
    public void showCityNameInToolbar(String name) {
        titleTv.setText(name);
    }

    @Override
    public void showFailure() {
//        contentView.setVisibility(android.view.View.GONE);
//        failureDialog.show(getChildFragmentManager(), WEATHER_DETAILS_FRAGMENT_KEY);

    }

    @Override
    public void showWeatherList(ArrayList<HashMap<String, ArrayList<WeatherResponse>>> responseManyDayWeather) {
        getChildFragmentManager().findFragmentById(R.id.fragment2).getLoaderManager().getLoader(WeatherListFragment.FRAGMENT_KEY.hashCode()).onContentChanged();


    }

    @Override
    public void setPresenter(WeatherDetailsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showProgressBar(boolean show) {
        if (show) {
            progressBar.setVisibility(android.view.View.VISIBLE);
        } else {
            progressBar.setVisibility(android.view.View.GONE);

        }
    }


    @Override
    public void showContent(boolean show) {
        if (show) {
            contentView.setVisibility(android.view.View.VISIBLE);
        } else {
            contentView.setVisibility(android.view.View.GONE);

        }

    }

    @Override
    public void showWeatherDetails(WeatherResponse weatherResponse) {
        getChildFragmentManager().findFragmentById(R.id.fragment1).getLoaderManager().getLoader(WeatherCurrentDetailsFragment.FRAGMENT_KEY.hashCode()).onContentChanged();

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(WEATHER_DETAILS_FRAGMENT_NAME_CITY_KEY, titleTv.getText().toString());
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null)
            titleTv.setText(savedInstanceState.getString(WEATHER_DETAILS_FRAGMENT_NAME_CITY_KEY));
    }


}


