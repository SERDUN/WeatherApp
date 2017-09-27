package com.example.dmitro.weatherapp.screen.select_city;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.data.model.geo.places.Places;
import com.example.dmitro.weatherapp.data.model.geo.places.Prediction;
import com.example.dmitro.weatherapp.screen.RecyclerViewEmptySupport;
import com.example.dmitro.weatherapp.screen.current_weather.WeatherDetailsActivity;
import com.example.dmitro.weatherapp.screen.select_city.list.CityChoiceRecyclerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CityChoiceActivity extends AppCompatActivity implements CityChoiceContract.View {
    @BindView(R.id.places_et)
    public EditText placeEditText;
    @BindView(R.id.places_rv)
    public RecyclerViewEmptySupport recyclerView;

    @BindView(R.id.empty_text_view)
    View view;

    private CityChoiceRecyclerAdapter cityChoiceRecyclerAdapter;

    private CityChoiceContract.Presenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_choice);
        ButterKnife.bind(this);
        new CityChoicePresenter(this);
        initView();


    }

    private void initView() {
        cityChoiceRecyclerAdapter = new CityChoiceRecyclerAdapter(p -> {
            presenter.getDetailsPlace(((Prediction)p).getPlaceId());
        }, new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
    }


    public void onClick(View view){
        switch (view.getId()){
            case R.id.search_btn:
                placeEditText.setVisibility(View.VISIBLE);
                break;
        }
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
        Intent intent = new Intent(this, WeatherDetailsActivity.class);
        intent.putExtra("lat", lat);
        intent.putExtra("lon", lon);

        startActivity(intent);
    }
}
