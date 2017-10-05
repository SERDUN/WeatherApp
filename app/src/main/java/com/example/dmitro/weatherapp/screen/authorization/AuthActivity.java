package com.example.dmitro.weatherapp.screen.authorization;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dd.morphingbutton.MorphingButton;
import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.data.model.social.User;
import com.example.dmitro.weatherapp.screen.navigation.NavigationActivity;
import com.example.dmitro.weatherapp.utils.Injection;
import com.example.dmitro.weatherapp.utils.animation.AnimationUtil;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuthActivity extends AppCompatActivity implements AuthContract.View, GoogleApiClient.OnConnectionFailedListener {

    private int BUTTON_WIDTH_AFTER_ANIMATION = 56;

    private int BUTTON_WIDTH_HEIGHT_ANIMATION = BUTTON_WIDTH_AFTER_ANIMATION;

    private int DURATION_ANIMATION = 1000;

    private int CORNER_RADIUS_ANIMATION = 500;

    private CallbackManager callbackManager;

    private AuthContract.Presenter presenter;

    private MorphingButton.Params morphingAnimationFacebook;

    private MorphingButton.Params morphingAnimationGoogle;

    private GoogleApiClient mGoogleApiClient;

    @BindView(R.id.sign_in_facebook)
    MorphingButton signInFacebookButton;

    @BindView(R.id.sign_in_google)
    MorphingButton signInGoogleButton;

    @BindView(R.id.container)
    View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);
        new AuthPresenter(this, getSharedPreferences("token", MODE_PRIVATE));
        presenter.trySignIn();
        init();
        initMorphingAnimation();
        initSignInFacebook();
        presenter.trySignIn();

    }


    private void init() {
        callbackManager = CallbackManager.Factory.create();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInFacebookButton.setIconLeft(R.drawable.ic_facebook_white_24dp);
        signInGoogleButton.setIconLeft(R.drawable.ic_google_plus_white_24dp);

        signInFacebookButton.setOnClickListener(c -> {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(getResources().getStringArray(R.array.my_facebook_permission)));


        });
        signInGoogleButton.setOnClickListener(c -> {
            signInGoogle();
        });
    }


    private void initMorphingAnimation() {
        morphingAnimationFacebook = MorphingButton.Params.create()
                .duration(DURATION_ANIMATION)
                .cornerRadius(CORNER_RADIUS_ANIMATION)
                .width(BUTTON_WIDTH_AFTER_ANIMATION)
                .height(BUTTON_WIDTH_HEIGHT_ANIMATION)
                .color(getResources().getColor(R.color.com_facebook_blue))
                .colorPressed(getResources().getColor(R.color.facebookButton));

        morphingAnimationGoogle = MorphingButton.Params.create()
                .duration(DURATION_ANIMATION)
                .cornerRadius(CORNER_RADIUS_ANIMATION)
                .width(BUTTON_WIDTH_AFTER_ANIMATION)
                .height(BUTTON_WIDTH_HEIGHT_ANIMATION)
                .color(getResources().getColor(R.color.colorGoogleCircleSplash))
                .colorPressed(getResources().getColor(R.color.colorGoogleCircleSplash));


    }


    private void initSignInFacebook() {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AnimationUtil.useTransparency(signInGoogleButton, 500, 1f, .0f, () -> signInFacebookButton.setEnabled(false));
                morphingAnimationFacebook.animationListener(() -> {
                    int coord[] = new int[2];
                    AnimationUtil.moveToCenterView(coord, (ViewGroup) findViewById(R.id.containerLL), signInFacebookButton, getBaseContext(), () -> {
                        AnimationUtil.useTransparency(signInFacebookButton, 100, 1f, 0f, null);
                        AnimationUtil.splashInCoordinate(view, coord[0], coord[1], getResources().getColor(R.color.colorFacebookCircleSplash), DURATION_ANIMATION, 0.2f, 1f, () -> {
                            presenter.saveTokenForFacebook(loginResult.getAccessToken().getToken());
                            presenter.trySignIn();
                        });
                    });


                });
                signInFacebookButton.morph(morphingAnimationFacebook);


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
        if (requestCode == 1) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            User user = new User();
            user.setImageUrl(acct.getPhotoUrl().toString());
            user.setName(acct.getDisplayName());
            user.setEmail(acct.getEmail());
            Injection.provideManager().cacheUserData(user);
            AnimationUtil.useTransparency(signInFacebookButton, 100, 1f, .0f, () -> signInFacebookButton.setEnabled(false));

            morphingAnimationGoogle.animationListener(() -> {
                int coord[] = new int[2];
                AnimationUtil.moveToCenterView(coord, (ViewGroup) findViewById(R.id.containerLL), signInGoogleButton, this, () -> {
                    AnimationUtil.useTransparency(signInGoogleButton, 100, 1f, 0f, null);
                    AnimationUtil.splashInCoordinate(view, coord[0], coord[1], getResources().getColor(R.color.colorGoogleCircleSplash), DURATION_ANIMATION, 0.2f, 1f, () -> {
                        presenter.saveTokenForGoogle(acct.getId());
                        presenter.trySignIn();
                    });
                });

            });
            signInGoogleButton.morph(morphingAnimationGoogle);
        } else {
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
    public void openBaseScreen() {
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, getString(R.string.authorization_error), Toast.LENGTH_SHORT).show();
    }

    private void signInGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, 1);
    }


}
