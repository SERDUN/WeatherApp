package com.example.dmitro.weatherapp.screen.authorization;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.data.repository.local.UserSessionStore;
import com.example.dmitro.weatherapp.databinding.ActivityAuthBinding;
import com.example.dmitro.weatherapp.screen.navigation.NavigationActivity;
import com.example.dmitro.weatherapp.service.auth.GoogleAuthService;

public class AuthActivity extends AppCompatActivity implements AuthContract.View {

    private ActivityAuthBinding binding;
    private AuthContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new AuthPresenter(this, new UserSessionStore(this), new GoogleAuthService(this));
        binding.signInGoogleBtn.setOnClickListener(v -> presenter.signInGoogle(this));
        binding.continueWithoutLoginBtn.setOnClickListener(v -> openBaseScreen());
        presenter.trySignIn();
    }

    @Override
    public void setPresenter(AuthContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showProgressBar(boolean show) {
        binding.authPb.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.signInGoogleBtn.setEnabled(!show);
    }

    /** No server client id in local.properties: hide the Google button and offer a guest entry. */
    @Override
    public void showSignInUnavailable() {
        binding.signInGoogleBtn.setVisibility(View.GONE);
        binding.signInUnavailableTv.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSignInError() {
        Toast.makeText(this, R.string.google_sign_in_failed, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void openBaseScreen() {
        startActivity(new Intent(this, NavigationActivity.class));
        finish();
    }
}
