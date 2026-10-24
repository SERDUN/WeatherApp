package com.example.dmitro.weatherapp.screen.select_city;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.dmitro.weatherapp.data.model.geo.places.Places;
import com.example.dmitro.weatherapp.data.model.geo.places.Prediction;
import com.example.dmitro.weatherapp.databinding.ActivityCityChoiceBinding;
import com.example.dmitro.weatherapp.screen.select_city.list.CityChoiceRecyclerAdapter;
import com.example.dmitro.weatherapp.utils.callback.Action0;

import java.util.ArrayList;

public class CityChoiceFragment extends Fragment implements CityChoiceContract.View {

    public static final String CITY_CHOICE_FRAGMENT_KEY = "CityChoiceFragment";

    private ActivityCityChoiceBinding binding;
    private CityChoiceRecyclerAdapter cityChoiceRecyclerAdapter;
    private CityChoiceContract.Presenter presenter;
    private Action0 listener;

    public CityChoiceFragment() {
    }

    public static CityChoiceFragment newInstance() {
        return new CityChoiceFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = ActivityCityChoiceBinding.inflate(inflater, container, false);
        new CityChoicePresenter(this);
        initView();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initView() {
        cityChoiceRecyclerAdapter = new CityChoiceRecyclerAdapter(p -> presenter.getDetailsPlace(((Prediction) p).getPlaceId()), new ArrayList<>());
        binding.placesRv.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.placesRv.setEmptyView(binding.emptyTextView);
        binding.placesRv.setAdapter(cityChoiceRecyclerAdapter);

        binding.placesEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.getPlacesByWord(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        binding.backCityChoiceBtn.setOnClickListener(v -> listener.call());
    }

    @Override
    public void setPresenter(CityChoiceContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showNewPlaces(Places places) {
        if (places != null && places.getPredictions() != null) {
            cityChoiceRecyclerAdapter.updateData(new ArrayList<>(places.getPredictions()));
        }
    }

    @Override
    public void showWeatherSelectedCity(double lat, double lon) {
        // The screen is not wired into navigation yet; see README follow-ups.
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

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
