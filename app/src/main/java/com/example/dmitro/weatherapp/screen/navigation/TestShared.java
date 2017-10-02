package com.example.dmitro.weatherapp.screen.navigation;

import com.facebook.FacebookCallback;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareContent;

/**
 * Created by dmitro on 01.10.17.
 */

public class TestShared {
    public static void share(final ShareContent shareContent, final FacebookCallback<Sharer.Result> callback) {
        new ShareApi(shareContent).share(callback);
    }
}
