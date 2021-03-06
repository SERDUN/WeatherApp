package com.example.dmitro.weatherapp.screen.authorization;


import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.screen.navigation.NavigationActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;

import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.dmitro.weatherapp.R.id.view;

public class AuthActivity extends AppCompatActivity implements AuthContract.View {

    //
//    private final int REQUEST_CODE_SIGN_IN = 41;
//
//    @BindView(R.id.sign_in_button)
//    public SignInButton signInButton;
//
//    @BindView(R.id.sign_out_button)
//    public Button signOutButton;
//
//    @BindView(R.id.revoke_access_button)
//    public Button revokeButton;
//
//
//    private GoogleApiClient googleApiClient;
    @BindView(R.id.login_button_fb)
    public LoginButton loginButton;

    private CallbackManager callbackManager;

    private AuthContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);
        new AuthPresenter(this, getSharedPreferences(getString(R.string.key_for_token_in_sp_facebook), Context.MODE_PRIVATE));
        presenter.trySignIn();
        callbackManager = CallbackManager.Factory.create();
        presenter.trySignIn();
        initSignInFacebook();
    }

    private void initSignInFacebook() {
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                presenter.saveFacebookToken(loginResult.getAccessToken().getToken());

            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void setPresenter(AuthContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showProgressBar(boolean show) {

    }


    @Override
    public void signInGoogle() {

    }

    @Override
    public void openBaseScreen() {
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
        finish();
    }

}
