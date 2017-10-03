package com.example.dmitro.weatherapp.screen.authorization;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.morphingbutton.MorphingAnimation;
import com.dd.morphingbutton.MorphingButton;
import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.data.model.social.User;
import com.example.dmitro.weatherapp.screen.navigation.NavigationActivity;
import com.example.dmitro.weatherapp.utils.Injection;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuthActivity extends AppCompatActivity implements AuthContract.View, GoogleApiClient.OnConnectionFailedListener {
    @BindView(R.id.sign_in_facebook)
    MorphingButton morphingButton;

    @BindView(R.id.sign_in_google)
    MorphingButton signInGoogleButton;

    @BindView(R.id.container)
    View view;



    private int BUTTON_WIDTH_AFTER_ANIMATION = 56;
    private int BUTTON_WIDTH_HEIGHT_ANIMATION = BUTTON_WIDTH_AFTER_ANIMATION;
    private int DURATION_ANIMATION = 500;
    private int CORNER_RADIUS_ANIMATION = 500;


    private CallbackManager callbackManager;

    private AuthContract.Presenter presenter;

    private MorphingButton.Params circle;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_auth);


        ButterKnife.bind(this);
        new AuthPresenter(this, getSharedPreferences("token", MODE_PRIVATE));
        presenter.trySignIn();
        callbackManager = CallbackManager.Factory.create();
        presenter.trySignIn();
        initSignInFacebook();
        init();
    }

    private void init() {
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

        signInGoogleButton.setOnClickListener(c -> {
            signIn();
        });
        morphingButton.setIconLeft(R.drawable.ic_facebook_white_24dp);
        signInGoogleButton.setIconLeft(R.drawable.ic_google_plus_white_24dp);
//        morphingButton.setBackgroundColor(getResources().getColor(R.color.facebookButton));

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

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
        if (requestCode == 1) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            int a = 4;
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

            Log.d("googleAcc", "handleSignInResult: " + acct.getDisplayName());
            Log.d("googleAcc", "handleSignInResult: " + acct.getFamilyName());
            Log.d("googleAcc", "handleSignInResult: " + acct.getGivenName());

            ///////////////////////////////
            circle.animationListener(new MorphingAnimation.Listener() {
                @Override
                public void onAnimationEnd() {

                    showAnimation(view, signInGoogleButton).addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            presenter.saveTokenForGoogle(acct.getId());
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
            signInGoogleButton.morph(circle);


            ///////////////

        } else {
            // Signed out, show unauthenticated UI.
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
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        int s = 4;
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, 1);
    }


}
