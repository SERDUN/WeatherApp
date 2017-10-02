package com.example.dmitro.weatherapp.screen.select_city;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.data.model.geo.places.Places;
import com.example.dmitro.weatherapp.data.model.geo.places.Prediction;
import com.example.dmitro.weatherapp.screen.RecyclerViewEmptySupport;
import com.example.dmitro.weatherapp.screen.select_city.list.CityChoiceRecyclerAdapter;
import com.example.dmitro.weatherapp.utils.callback.Action0;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CityChoiceFragment extends Fragment implements CityChoiceContract.View {

    public static final String CITY_CHOICE_FRAGMENT_KEY = "CityChoiceFragment";

    @BindView(R.id.places_et)
    public EditText placeEditText;

    @BindView(R.id.places_rv)
    public RecyclerViewEmptySupport recyclerView;

    @BindView(R.id.back_city_choice_btn)
    public ImageButton backButton;


    private CityChoiceRecyclerAdapter cityChoiceRecyclerAdapter;

    private CityChoiceContract.Presenter presenter;

    private Action0 listener;

    public CityChoiceFragment() {
    }


    public static CityChoiceFragment newInstance() {
        CityChoiceFragment fragment = new CityChoiceFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_city_choice, container, false);
        ButterKnife.bind(this, view);
        new CityChoicePresenter(this);
        initView(view);
        return view;
    }

    private void initView(View view) {
        cityChoiceRecyclerAdapter = new CityChoiceRecyclerAdapter(p -> {
            presenter.getDetailsPlace(((Prediction) p).getPlaceId());
        }, new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setEmptyView(view);
        recyclerView.setAdapter(cityChoiceRecyclerAdapter);

        placeEditText.addTextChangedListener(new TextWatcher() {
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
        backButton.setOnClickListener(view1 -> {
            listener.call();
        });
    }


    @Override
    public void setPresenter(CityChoiceContract.Presenter presenter) {
        this.presenter = presenter;

    }

    @Override
    public void showNewPlaces(Places places) {
        cityChoiceRecyclerAdapter.updateData((ArrayList<Prediction>) places.getPredictions());
    }

    @Override
    public void showWeatherSelectedCity(double lat, double lon) {
//        Intent intent = new Intent(this, WeatherDetailsActivity.class);
//        intent.putExtra("lat", lat);
//        intent.putExtra("lon", lon);

//        startActivity(intent);
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

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }


}
