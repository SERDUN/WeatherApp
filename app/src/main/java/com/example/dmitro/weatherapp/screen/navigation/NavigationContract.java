package com.example.dmitro.weatherapp.screen.navigation;

import com.example.dmitro.weatherapp.data.model.social.User;
import com.example.dmitro.weatherapp.screen.BaseView;

/**
 * Created by dmitro on 29.09.17.
 */

public class NavigationContract {
    interface View extends BaseView<NavigationContract.Presenter> {

        void showProgressBar(boolean show);

        void showContent(boolean show);

        void showUserInformation(User user);


        void showFailure();

    }

    interface Presenter {
        void init();

        void getCurrentUser();

    }
}
