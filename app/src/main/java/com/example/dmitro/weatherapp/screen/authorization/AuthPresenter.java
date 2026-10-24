package com.example.dmitro.weatherapp.screen.authorization;

import android.content.Context;

import com.example.dmitro.weatherapp.data.repository.local.UserSessionStore;
import com.example.dmitro.weatherapp.service.auth.GoogleAuthService;

public class AuthPresenter implements AuthContract.Presenter {
    private final AuthContract.View view;
    private final UserSessionStore sessionStore;
    private final GoogleAuthService authService;

    public AuthPresenter(AuthContract.View view, UserSessionStore sessionStore, GoogleAuthService authService) {
        this.view = view;
        this.sessionStore = sessionStore;
        this.authService = authService;
        view.setPresenter(this);
    }

    @Override
    public void trySignIn() {
        if (sessionStore.isSignedIn()) {
            view.openBaseScreen();
        } else if (!GoogleAuthService.isConfigured()) {
            view.showSignInUnavailable();
        }
    }

    @Override
    public void signInGoogle(Context activity) {
        view.showProgressBar(true);
        authService.signIn(activity, profile -> {
            sessionStore.save(profile);
            view.showProgressBar(false);
            view.openBaseScreen();
        }, error -> {
            view.showProgressBar(false);
            view.showSignInError();
        });
    }
}
