package com.example.dmitro.weatherapp.screen.navigation;

import com.example.dmitro.weatherapp.data.model.user.UserProfile;
import com.example.dmitro.weatherapp.screen.BaseView;

public class NavigationContract {
    interface View extends BaseView<Presenter> {
        void showUserInformation(UserProfile profile);

        void showGuest();

        void openAuthScreen();
    }

    interface Presenter {
        void loadCurrentUser();

        void signOut();
    }
}
