package com.example.dmitro.weatherapp.data.repository.local;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.example.dmitro.weatherapp.data.model.user.UserProfile;

/** Persists the signed-in profile between launches; the id token itself is not stored, there is no backend to send it to. */
public class UserSessionStore {
    private static final String PREFS = "user_session";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHOTO = "photo";

    private final SharedPreferences prefs;

    public UserSessionStore(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public void save(UserProfile profile) {
        prefs.edit()
                .putString(KEY_NAME, profile.getName())
                .putString(KEY_EMAIL, profile.getEmail())
                .putString(KEY_PHOTO, profile.getPhotoUrl())
                .apply();
    }

    @Nullable
    public UserProfile load() {
        String email = prefs.getString(KEY_EMAIL, null);
        if (email == null) {
            return null;
        }
        return new UserProfile(prefs.getString(KEY_NAME, ""), email, prefs.getString(KEY_PHOTO, null));
    }

    public boolean isSignedIn() {
        return prefs.contains(KEY_EMAIL);
    }

    public void clear() {
        prefs.edit().clear().apply();
    }
}
