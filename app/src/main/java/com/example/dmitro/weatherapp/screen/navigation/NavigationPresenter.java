package com.example.dmitro.weatherapp.screen.navigation;

import android.os.Bundle;

import com.example.dmitro.weatherapp.data.model.social.UserFacebook;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.gson.Gson;


/**
 * Created by dmitro on 29.09.17.
 */

public class NavigationPresenter implements NavigationContract.Presenter {
    public NavigationContract.View view;

    public NavigationPresenter(NavigationContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void init() {


    }

    @Override
    public void getCurrentUser() {
        if (!FacebookSdk.isInitialized() || AccessToken.getCurrentAccessToken() == null) {
            return;
        }
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), (object, response) -> {
            if (response != null && response.getRawResponse() != null) {
                view.showUserInformation(new Gson().fromJson(response.getRawResponse(), UserFacebook.class));
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,link,picture.type(large),email");
        request.setParameters(parameters);
        request.executeAsync();
    }
}
