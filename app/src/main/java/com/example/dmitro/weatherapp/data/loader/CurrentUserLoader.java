package com.example.dmitro.weatherapp.data.loader;

import android.content.Context;

import com.example.dmitro.weatherapp.data.loader.base.BaseLoader;
import com.example.dmitro.weatherapp.data.loader.base.Response;

import java.io.IOException;

/**
 * Created by dmitro on 03.10.17.
 */

public class CurrentUserLoader extends BaseLoader {
    public CurrentUserLoader(Context context) {
        super(context);
    }

    @Override
    protected Response apiCall() throws IOException {
        return null;
    }
}
