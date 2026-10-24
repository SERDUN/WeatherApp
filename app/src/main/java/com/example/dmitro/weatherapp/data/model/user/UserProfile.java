package com.example.dmitro.weatherapp.data.model.user;

import androidx.annotation.Nullable;

/** The signed-in user as shown in the navigation drawer header. */
public final class UserProfile {
    private final String name;
    private final String email;
    @Nullable
    private final String photoUrl;

    public UserProfile(String name, String email, @Nullable String photoUrl) {
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Nullable
    public String getPhotoUrl() {
        return photoUrl;
    }
}
