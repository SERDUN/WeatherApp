package com.example.dmitro.weatherapp.screen.navigation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.WeatherApp;
import com.example.dmitro.weatherapp.data.model.social.User;
import com.example.dmitro.weatherapp.screen.authorization.AuthActivity;
import com.example.dmitro.weatherapp.screen.weather.WeatherDetailsFragment;
import com.example.dmitro.weatherapp.service.social_service.FacebookApi;
import com.example.dmitro.weatherapp.utils.PicassoCirleTransformation;
import com.example.dmitro.weatherapp.utils.callback.Action0;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Action0, NavigationContract.View {

    DrawerLayout drawer;
    private NavigationContract.Presenter presenter;

    public ImageView userImageView;

    public TextView fullNameTextView;

    public TextView emailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        ButterKnife.bind(this);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        fullNameTextView = (TextView) drawer.findViewById(R.id.full_name_text_view);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        initHeader(header);

        addFragment(WeatherDetailsFragment.newInstance(), WeatherDetailsFragment.WEATHER_DETAILS_FRAGMENT_KEY);
        new NavigationPresenter(this);
        presenter.getCurrentUser();


    }


    private void initHeader(View header) {
        fullNameTextView = (TextView) header.findViewById(R.id.full_name_text_view);
        emailTextView = (TextView) header.findViewById(R.id.email_text_view);
        userImageView = (ImageView) header.findViewById(R.id.user_avatar_image_view);

    }

    @OnClick(R.id.nav_footer_sign_out)
    public void sigOut() {
        if (getSharedPreferences("token",MODE_PRIVATE).getString(WeatherApp.getInstance().getString(R.string.google_token), "").isEmpty()) {
            LoginManager.getInstance().logOut();
            startActivity(new Intent(this, AuthActivity.class));
            finish();
        } else {
            SharedPreferences.Editor editor =getSharedPreferences("token",MODE_PRIVATE).edit();
            editor.putString(WeatherApp.getInstance().getString(R.string.google_token), "").apply();
            startActivity(new Intent(this, AuthActivity.class));
            finish();
        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.nav_current_weather:
                addFragment(WeatherDetailsFragment.newInstance(), WeatherDetailsFragment.WEATHER_DETAILS_FRAGMENT_KEY);
                break;
            case R.id.nav_share_weather:
                Toast.makeText(this, "Shared weather", Toast.LENGTH_SHORT).show();
                FacebookApi.shareWeather();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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
    public void showProgressBar(boolean show) {

    }

    @Override
    public void showContent(boolean show) {

    }

    @Override
    public void showUserInformation(User userFacebook) {
        fullNameTextView.setText(userFacebook.getName());
        emailTextView.setText(userFacebook.getEmail());
        Picasso.with(getApplicationContext())
                .load(userFacebook.getImageUrl()).transform(new PicassoCirleTransformation())
                .into(userImageView);
    }

    @Override
    public void showFailure() {

    }

    @Override
    public void call() {
        drawer.openDrawer(Gravity.START);
    }
}
