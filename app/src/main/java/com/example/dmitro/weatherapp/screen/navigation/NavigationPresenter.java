package com.example.dmitro.weatherapp.screen.navigation;

import com.example.dmitro.weatherapp.data.repository.WeatherRepositoryManager;
import com.example.dmitro.weatherapp.utils.Injection;

/**
 * Created by dmitro on 29.09.17.
 */

public class NavigationPresenter implements NavigationContract.Presenter {
    public NavigationContract.View view;
    WeatherRepositoryManager weatherRepositoryManager = Injection.provideManager();

    public NavigationPresenter(NavigationContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void init() {


    }

    @Override
    public void getCurrentUser() {
        weatherRepositoryManager.getCurrentUser(s -> {
            view.showUserInformation(s);
        }, f -> {

        }, () -> {
        });
//        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), (object, response) -> {
//            view.showUserInformation(new Gson().fromJson(response.getRawResponse(), UserFacebook.class));
//        });
//        Bundle parameters = new Bundle();
//        parameters.putString("fields", "name,link,picture.type(large),email,id");
//        request.setParameters(parameters);
//        request.executeAsync();
    }
}
