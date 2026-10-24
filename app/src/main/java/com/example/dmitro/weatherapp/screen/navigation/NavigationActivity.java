package com.example.dmitro.weatherapp.screen.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.data.model.user.UserProfile;
import com.example.dmitro.weatherapp.data.repository.local.UserSessionStore;
import com.example.dmitro.weatherapp.databinding.ActivityNavigationBinding;
import com.example.dmitro.weatherapp.screen.authorization.AuthActivity;
import com.example.dmitro.weatherapp.screen.weather.WeatherDetailsFragment;
import com.example.dmitro.weatherapp.service.auth.GoogleAuthService;
import com.example.dmitro.weatherapp.service.share.WeatherShare;
import com.example.dmitro.weatherapp.utils.ImageLoader;
import com.example.dmitro.weatherapp.utils.callback.Action0;
import com.google.android.material.navigation.NavigationView;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Action0, NavigationContract.View {

    private ActivityNavigationBinding binding;
    private NavigationContract.Presenter presenter;

    private ImageView userImageView;
    private TextView fullNameTextView;
    private TextView emailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.navView.setNavigationItemSelectedListener(this);
        binding.navFooterSignOut.setOnClickListener(v -> presenter.signOut());
        initHeader(binding.navView.getHeaderView(0));

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });

        addFragment(WeatherDetailsFragment.newInstance(), WeatherDetailsFragment.WEATHER_DETAILS_FRAGMENT_KEY);
        new NavigationPresenter(this, new UserSessionStore(this), new GoogleAuthService(this));
        presenter.loadCurrentUser();
    }

    private void initHeader(View header) {
        fullNameTextView = header.findViewById(R.id.full_name_text_view);
        emailTextView = header.findViewById(R.id.email_text_view);
        userImageView = header.findViewById(R.id.user_avatar_image_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_current_weather) {
            addFragment(WeatherDetailsFragment.newInstance(), WeatherDetailsFragment.WEATHER_DETAILS_FRAGMENT_KEY);
        } else if (id == R.id.nav_share_weather) {
            if (!WeatherShare.shareCurrentWeather(this)) {
                Toast.makeText(this, R.string.message_text, Toast.LENGTH_SHORT).show();
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void addFragment(Fragment fragment, String fragmentKey) {
        if (getSupportFragmentManager().findFragmentByTag(fragmentKey) == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment, fragmentKey)
                    .commit();
        }
    }

    @Override
    public void setPresenter(NavigationContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showUserInformation(UserProfile profile) {
        fullNameTextView.setText(profile.getName());
        emailTextView.setText(profile.getEmail());
        if (profile.getPhotoUrl() != null) {
            ImageLoader.loadAvatar(userImageView, profile.getPhotoUrl());
        }
    }

    @Override
    public void showGuest() {
        fullNameTextView.setText(R.string.guest);
        emailTextView.setText("");
    }

    @Override
    public void openAuthScreen() {
        startActivity(new Intent(this, AuthActivity.class));
        finish();
    }

    @Override
    public void call() {
        binding.drawerLayout.openDrawer(GravityCompat.START);
    }
}
