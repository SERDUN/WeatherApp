package com.example.dmitro.weatherapp.data.repository.local;

import com.example.dmitro.weatherapp.data.model.social.User;
import com.example.dmitro.weatherapp.data.repository.SocialDataSources;
import com.example.dmitro.weatherapp.utils.callback.Action0;
import com.example.dmitro.weatherapp.utils.callback.Action1;

import io.realm.Realm;

/**
 * Created by dmitro on 25.09.17.
 */

public class WeatherLocalRepository implements SocialDataSources {
    Realm realm = Realm.getDefaultInstance();


    @Override
    public void cacheUserData(User userFacebook) {
        realm.beginTransaction();
        realm.delete(User.class);
        realm.copyToRealm(userFacebook);
        realm.commitTransaction();
        realm.close();
    }

    @Override
    public void getCurrentUser(Action1<User> success, Action1<Throwable> failure, Action0 complete) {
        realm=Realm.getDefaultInstance();
        realm.beginTransaction();
        success.call(realm.where(User.class).findFirst());
        realm.commitTransaction();
        realm.close();
    }
}
