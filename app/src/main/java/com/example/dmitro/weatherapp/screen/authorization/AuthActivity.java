package com.example.dmitro.weatherapp.screen.authorization;


import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;

import com.dd.morphingbutton.MorphingAnimation;
import com.dd.morphingbutton.MorphingButton;
import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.data.repository.WeatherRepositoryManager;
import com.example.dmitro.weatherapp.screen.navigation.NavigationActivity;
import com.example.dmitro.weatherapp.utils.Injection;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuthActivity extends AppCompatActivity implements AuthContract.View {
    @BindView(R.id.sign_in_facebook)
    MorphingButton morphingButton;

    @BindView(R.id.container)
    View view;

    private int BUTTON_WIDTH_AFTER_ANIMATION = 56;
    private int BUTTON_WIDTH_HEIGHT_ANIMATION = BUTTON_WIDTH_AFTER_ANIMATION;
    private int DURATION_ANIMATION = 500;
    private int CORNER_RADIUS_ANIMATION = 500;


    private CallbackManager callbackManager;

    private AuthContract.Presenter presenter;

    MorphingButton.Params circle;

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


        circle = MorphingButton.Params.create()
                .duration(DURATION_ANIMATION)
                .cornerRadius(CORNER_RADIUS_ANIMATION)
                .width(BUTTON_WIDTH_AFTER_ANIMATION)
                .height(BUTTON_WIDTH_HEIGHT_ANIMATION)
                .color(R.color.com_facebook_button_background_color)
                .colorPressed(R.color.com_facebook_button_login_silver_background_color_pressed)
                .icon(R.drawable.ic_facebook_white_24dp);

        morphingButton.setOnClickListener(c -> {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
        });
        morphingButton.setIconLeft(R.drawable.ic_facebook_white_24dp);
//        morphingButton.setBackgroundColor(getResources().getColor(R.color.facebookButton));


    }

    private Animator showAnimation(final View view, final View buttonView) {
        int cx = (int) (buttonView.getX() + buttonView.getWidth() / 2) + 28;
        int cy = (int) (buttonView.getY() + buttonView.getHeight() / 2) + 28;

        int finalRadius = Math.max(view.getWidth(), view.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy,
                0, finalRadius);
        anim.setDuration(DURATION_ANIMATION);

        view.setVisibility(View.VISIBLE);
        anim.start();
        return anim;
    }


    private void initSignInFacebook() {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                circle.animationListener(new MorphingAnimation.Listener() {
                    @Override
                    public void onAnimationEnd() {

                        showAnimation(view, morphingButton).addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                presenter.trySignIn();
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });

                    }
                });
                morphingButton.morph(circle);


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

    public void onClick(View view){
        WeatherRepositoryManager manager= Injection.provideManager();

    }

}
