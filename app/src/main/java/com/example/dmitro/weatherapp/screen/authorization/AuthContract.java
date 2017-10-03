package com.example.dmitro.weatherapp.screen.authorization;

import com.example.dmitro.weatherapp.screen.BaseView;

/**
 * Created by dmitro on 28.09.17.
 */

public class AuthContract {
    interface View extends BaseView<AuthContract.Presenter> {
        public void showProgressBar(boolean show);


        public void signInGoogle();

        public void openBaseScreen();

    }

    interface Presenter {

        public void trySignIn();

        public void signInFacebook();

        public void signInGoogle();


    }
}
