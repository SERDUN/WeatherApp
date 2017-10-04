package com.example.dmitro.weatherapp.screen.authorization;

import com.example.dmitro.weatherapp.screen.BaseView;

/**
 * Created by dmitro on 28.09.17.
 */

public class AuthContract {
    interface View extends BaseView<AuthContract.Presenter> {
        void showProgressBar(boolean show);

        void openBaseScreen();

    }

    interface Presenter {
        void saveTokenForGoogle(String token);

        void saveTokenForFacebook(String token);

        public void trySignIn();

    }
}
