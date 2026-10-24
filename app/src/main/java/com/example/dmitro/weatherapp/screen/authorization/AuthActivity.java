package com.example.dmitro.weatherapp.screen.authorization;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.databinding.ActivityAuthBinding;
import com.example.dmitro.weatherapp.databinding.ViewFacebookLoginBinding;
import com.example.dmitro.weatherapp.screen.navigation.NavigationActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;

import java.util.Arrays;

public class AuthActivity extends AppCompatActivity implements AuthContract.View {

    private ActivityAuthBinding binding;
    private CallbackManager callbackManager;
    private AuthContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new AuthPresenter(this, getSharedPreferences(getString(R.string.key_for_token_in_sp_facebook), Context.MODE_PRIVATE));

        // The SDK auto-initialises from the manifest; without an app id in local.properties it stays
        // uninitialised and every Facebook widget throws, so the screen degrades to a guest entry.
        if (FacebookSdk.isInitialized()) {
            initSignInFacebook();
            presenter.trySignIn();
        } else {
            binding.facebookUnavailableTv.setVisibility(View.VISIBLE);
            binding.continueWithoutLoginBtn.setVisibility(View.VISIBLE);
            binding.continueWithoutLoginBtn.setOnClickListener(v -> openBaseScreen());
        }
    }

    private void initSignInFacebook() {
        callbackManager = CallbackManager.Factory.create();
        ViewFacebookLoginBinding login = ViewFacebookLoginBinding.inflate(getLayoutInflater(), binding.loginContainer, true);
        login.loginButtonFb.setPermissions(Arrays.asList("public_profile", "email"));
        login.loginButtonFb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(@NonNull LoginResult loginResult) {
                presenter.saveFacebookToken(loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(@NonNull FacebookException exception) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
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
        startActivity(new Intent(this, NavigationActivity.class));
        finish();
    }
}
