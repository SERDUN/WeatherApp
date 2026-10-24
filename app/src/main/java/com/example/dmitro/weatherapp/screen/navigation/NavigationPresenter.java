package com.example.dmitro.weatherapp.screen.navigation;

import com.example.dmitro.weatherapp.data.model.user.UserProfile;
import com.example.dmitro.weatherapp.data.repository.local.UserSessionStore;
import com.example.dmitro.weatherapp.service.auth.GoogleAuthService;

public class NavigationPresenter implements NavigationContract.Presenter {
    private final NavigationContract.View view;
    private final UserSessionStore sessionStore;
    private final GoogleAuthService authService;

    public NavigationPresenter(NavigationContract.View view, UserSessionStore sessionStore, GoogleAuthService authService) {
        this.view = view;
        this.sessionStore = sessionStore;
        this.authService = authService;
        view.setPresenter(this);
    }

    @Override
    public void loadCurrentUser() {
        UserProfile profile = sessionStore.load();
        if (profile == null) {
            view.showGuest();
        } else {
            view.showUserInformation(profile);
        }
    }

    @Override
    public void signOut() {
        sessionStore.clear();
        authService.signOut(view::openAuthScreen);
    }
}
