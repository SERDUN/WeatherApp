package com.example.dmitro.weatherapp.screen.select_city.list;

import com.example.dmitro.weatherapp.data.model.geo.places.Prediction;

/**
 * Created by dmitro on 26.09.17.
 */

public interface RecyclerListener {
    public void onClick(Prediction prediction);

}
