package com.example.dmitro.weatherapp.data.repository;

import com.example.dmitro.weatherapp.data.model.social.User;
import com.example.dmitro.weatherapp.utils.callback.Action0;
import com.example.dmitro.weatherapp.utils.callback.Action1;

/**
 * Created by dmitro on 03.10.17.
 */

public interface SocialDataSources {
    public void cacheUserData(User userFacebook);
    public void getLocalUser(Action1<User> success, Action1<Throwable> failure, Action0 complete);
    public void getRemoteUser(Action1<User> success, Action1<Throwable> failure, Action0 complete);

}
