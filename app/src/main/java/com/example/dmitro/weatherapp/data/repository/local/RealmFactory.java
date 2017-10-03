package com.example.dmitro.weatherapp.data.repository.local;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import io.realm.Realm;

/**
 * Created by dmitro on 03.10.17.
 */

public class RealmFactory {
    private static RealmFactory instance;
    private static Realm realm;
//
//    public RealmFactory(Application application) {
//        realm = Realm.getDefaultInstance();
//    }
//
//    public static RealmFactory with(Application application) {
//
//        if (instance == null) {
//            instance = new RealmFactory(application);
//        }
//        return instance;
//    }

    public static RealmFactory getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    //Refresh the realm istance
    public void refresh() {

        realm.refresh();
    }
}
