package com.example.dmitro.weatherapp.screen.authorization;

import com.example.dmitro.weatherapp.screen.BaseView;

public class AuthContract {
    interface View extends BaseView<Presenter> {
        void showProgressBar(boolean show);

        void showSignInUnavailable();

        void showSignInError();

        void openBaseScreen();
    }

    interface Presenter {
        void trySignIn();

        void signInGoogle(android.content.Context activity);
    }
}
