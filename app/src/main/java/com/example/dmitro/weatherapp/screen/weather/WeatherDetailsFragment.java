package com.example.dmitro.weatherapp.screen.weather;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.data.model.weather.current.WeatherResponse;
import com.example.dmitro.weatherapp.databinding.ActivityWeatherBinding;
import com.example.dmitro.weatherapp.screen.failure.FailureDialog;
import com.example.dmitro.weatherapp.screen.weather.fragment.WeatherCurrentDetailsFragment;
import com.example.dmitro.weatherapp.screen.weather.fragment.WeatherListFragment;
import com.example.dmitro.weatherapp.utils.MyUtil;
import com.example.dmitro.weatherapp.utils.callback.Action0;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceTypes;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.widget.PlaceAutocomplete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class WeatherDetailsFragment extends Fragment implements WeatherDetailsContract.View {

    public final static String WEATHER_DETAILS_FRAGMENT_KEY = "WeatherDetailsFragment";
    public final static String WEATHER_DETAILS_FRAGMENT_NAME_CITY_KEY = "WeatherDetailsFragmentNameCity";

    private static final String[] LOCATION_PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static final List<Place.Field> PLACE_FIELDS =
            Arrays.asList(Place.Field.ID, Place.Field.DISPLAY_NAME, Place.Field.LOCATION);

    private WeatherDetailsContract.Presenter presenter;
    private ActivityWeatherBinding binding;
    private FailureDialog failureDialog;
    private Action0 listener;

    private final ActivityResultLauncher<String[]> locationPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                boolean granted = false;
                for (Boolean value : result.values()) {
                    granted |= Boolean.TRUE.equals(value);
                }
                if (granted) {
                    loadWeatherForCurrentLocation();
                } else {
                    showProgressBar(false);
                    showFailure();
                }
            });

    private final ActivityResultLauncher<Intent> placeAutocompleteLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (binding != null) {
                    binding.toolbar.locationButton.setEnabled(true);
                }
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    AutocompletePrediction prediction = PlaceAutocomplete.getPredictionFromIntent(result.getData());
                    AutocompleteSessionToken token = PlaceAutocomplete.getSessionTokenFromIntent(result.getData());
                    if (prediction != null) {
                        fetchPlaceAndLoad(prediction.getPlaceId(), token);
                    }
                }
            });

    public WeatherDetailsFragment() {
    }

    public static WeatherDetailsFragment newInstance() {
        return new WeatherDetailsFragment();
    }

    @Override
    public android.view.View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {
        binding = ActivityWeatherBinding.inflate(inflater, container, false);
        new WeatherDetailsPresenter(this);
        init();
        initToolbar();
        applyBlur();

        if (savedInstanceState == null) {
            requestLocationAndLoad();
        }
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Action0) {
            listener = (Action0) context;
        } else {
            throw new RuntimeException(context + " must implement Action0");
        }
    }

    private void initToolbar() {
        binding.toolbar.backBtn.setImageResource(R.drawable.ic_menu_black_24dp);
        binding.toolbar.backBtn.setOnClickListener(view -> listener.call());
    }

    private void applyBlur() {
        binding.backgWeatherDetailsActivityIv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (binding == null) {
                    return true;
                }
                binding.backgWeatherDetailsActivityIv.getViewTreeObserver().removeOnPreDrawListener(this);
                MyUtil.blur(binding.backgWeatherDetailsActivityIv, requireContext());
                return true;
            }
        });
    }

    private void init() {
        failureDialog = new FailureDialog();
        failureDialog.setCallback(() -> {
            failureDialog.dismiss();
            requestLocationAndLoad();
        });

        presenter.init();
        binding.toolbar.locationButton.setOnClickListener(onClick -> openPlaceAutocomplete());
    }

    private void openPlaceAutocomplete() {
        if (!Places.isInitialized()) {
            showFailure();
            return;
        }
        binding.toolbar.locationButton.setEnabled(false);
        Intent intent = new PlaceAutocomplete.IntentBuilder()
                .setTypesFilter(Collections.singletonList(PlaceTypes.CITIES))
                .build(requireContext());
        placeAutocompleteLauncher.launch(intent);
    }

    /** The autocomplete widget returns only a prediction; the coordinates come from a Place Details call. */
    private void fetchPlaceAndLoad(String placeId, @Nullable AutocompleteSessionToken token) {
        showProgressBar(true);
        FetchPlaceRequest.Builder request = FetchPlaceRequest.builder(placeId, PLACE_FIELDS);
        if (token != null) {
            request.setSessionToken(token);
        }
        Places.createClient(requireContext()).fetchPlace(request.build())
                .addOnSuccessListener(response -> {
                    LatLng location = response.getPlace().getLocation();
                    if (location == null) {
                        showProgressBar(false);
                        showFailure();
                        return;
                    }
                    presenter.getWeatherForLanLon(location.latitude, location.longitude);
                    presenter.getWeatherByFiveDayForLatLon(location.latitude, location.longitude);
                })
                .addOnFailureListener(e -> {
                    showProgressBar(false);
                    showFailure();
                });
    }

    private void requestLocationAndLoad() {
        if (hasLocationPermission()) {
            loadWeatherForCurrentLocation();
        } else {
            locationPermissionLauncher.launch(LOCATION_PERMISSIONS);
        }
    }

    private boolean hasLocationPermission() {
        for (String permission : LOCATION_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

    private void loadWeatherForCurrentLocation() {
        showProgressBar(true);
        presenter.getCurrentWeather();
        presenter.getCurrentWeatherByFiveDays();
    }

    @Override
    public void showCityNameInToolbar(String name) {
        binding.toolbar.toolbarTitle.setText(name);
    }

    /** Both requests may fail in the same frame; showNow commits synchronously so isAdded() is reliable for the second call. */
    @Override
    public void showFailure() {
        if (isAdded() && !getChildFragmentManager().isStateSaved() && !failureDialog.isAdded()) {
            failureDialog.showNow(getChildFragmentManager(), WEATHER_DETAILS_FRAGMENT_KEY);
        }
    }

    @Override
    public void showWeatherList(ArrayList<HashMap<String, ArrayList<WeatherResponse>>> responseManyDayWeather) {
        notifyChildLoader(R.id.fragment2, WeatherListFragment.FRAGMENT_KEY);
    }

    @Override
    public void showWeatherDetails(WeatherResponse weatherResponse) {
        notifyChildLoader(R.id.fragment1, WeatherCurrentDetailsFragment.FRAGMENT_KEY);
    }

    private void notifyChildLoader(int fragmentId, String loaderKey) {
        Fragment child = getChildFragmentManager().findFragmentById(fragmentId);
        if (child == null) {
            return;
        }
        androidx.loader.content.Loader<?> loader = androidx.loader.app.LoaderManager.getInstance(child).getLoader(loaderKey.hashCode());
        if (loader != null) {
            loader.onContentChanged();
        }
    }

    @Override
    public void setPresenter(WeatherDetailsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showProgressBar(boolean show) {
        if (binding != null) {
            binding.weatherDetailsPb.setVisibility(show ? android.view.View.VISIBLE : android.view.View.GONE);
        }
    }

    @Override
    public void showContent(boolean show) {
        if (binding != null) {
            binding.contentWeatherDetailsLv.setVisibility(show ? android.view.View.VISIBLE : android.view.View.GONE);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (binding != null) {
            savedInstanceState.putString(WEATHER_DETAILS_FRAGMENT_NAME_CITY_KEY, binding.toolbar.toolbarTitle.getText().toString());
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            binding.toolbar.toolbarTitle.setText(savedInstanceState.getString(WEATHER_DETAILS_FRAGMENT_NAME_CITY_KEY));
        }
    }
}
