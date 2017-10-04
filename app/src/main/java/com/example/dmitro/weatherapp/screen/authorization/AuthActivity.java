package com.example.dmitro.weatherapp.screen.authorization;


import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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

    private int DURATION_ANIMATION = 500;

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

//        signInFacebookButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));


    }


    private void initMorphingAnimation() {
        morphingAnimationFacebook = MorphingButton.Params.create()
                .duration(DURATION_ANIMATION)
                .cornerRadius(CORNER_RADIUS_ANIMATION)
                .width(BUTTON_WIDTH_AFTER_ANIMATION)
                .height(BUTTON_WIDTH_HEIGHT_ANIMATION)
                .color(R.color.com_facebook_button_background_color)
                .colorPressed(R.color.com_facebook_button_login_silver_background_color_pressed)
                .icon(R.drawable.ic_facebook_white_24dp);

        morphingAnimationGoogle = MorphingButton.Params.create()
                .duration(DURATION_ANIMATION)
                .cornerRadius(CORNER_RADIUS_ANIMATION)
                .width(BUTTON_WIDTH_AFTER_ANIMATION)
                .height(BUTTON_WIDTH_HEIGHT_ANIMATION)
                .color(getResources().getColor(R.color.colorGoogleCircleSplash))
                .colorPressed(getResources().getColor(R.color.colorGoogleCircleSplash));
//                .icon(R.drawable.ic_google_plus_white_24dp);


    }


    private void initSignInFacebook() {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                morphingAnimationFacebook.animationListener(() -> showAnimation(view, signInFacebookButton, R.color.colorFacebookCircleSplash).addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
//                        presenter.saveTokenForFacebook(loginResult.getAccessToken().getToken());
//                        presenter.trySignIn();
                        moveCenter(signInFacebookButton);
                        useTransparency(signInGoogleButton);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                }));
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
            useTransparency(signInFacebookButton);
//        moveReplaceView(signInGoogleButton, signInFacebookButton);


            morphingAnimationGoogle.animationListener(() -> {
//                AlphaAnimation animation1 = new AlphaAnimation(1f, .0f);
//                animation1.setDuration(100);
//                signInGoogleButton.setAnimation(animation1);
////

                moveCenter(signInGoogleButton).setAnimationListener(new AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        showAnimation(view, signInGoogleButton, R.color.colorGoogleCircleSplash).addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {
                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                view.setAlpha(1f);
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

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
//
//                /////////////////////
//                TranslateAnimation anim = new TranslateAnimation(0, 0, 0, -450);
//                anim.setDuration(1000);
//
//                anim.setAnimationListener(new TranslateAnimation.AnimationListener() {
//
//                    @Override
//                    public void onAnimationStart(Animation animation) {
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation) {
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animation animation) {
//                        LinearLayout li = (LinearLayout) findViewById(R.id.containerLL);
//
//                        li.layout(li.getLeft(), 450, li.getRight(), li.getBottom());
//
//                        signInGoogleButton.startAnimation(animation1);
//                        animation1.setAnimationListener(new AnimationListener() {
//                            @Override
//                            public void onAnimationStart(Animation animation) {
//
//                            }
//
//                            @Override
//                            public void onAnimationEnd(Animation animation) {
//                                signInGoogleButton.setAlpha(.0f);
//
//                                showAnimation(view, signInGoogleButton, R.color.colorGoogleCircleSplash).addListener(new Animator.AnimatorListener() {
//                                    @Override
//                                    public void onAnimationStart(Animator animator) {
//                                    }
//
//                                    @Override
//                                    public void onAnimationEnd(Animator animator) {
//                                        view.setAlpha(1f);
//                                        presenter.saveTokenForGoogle(acct.getId());
//                                        presenter.trySignIn();
//                                    }
//
//                                    @Override
//                                    public void onAnimationCancel(Animator animator) {
//
//                                    }
//
//                                    @Override
//                                    public void onAnimationRepeat(Animator animator) {
//
//                                    }
//                                });
//                            }
//
//                            @Override
//                            public void onAnimationRepeat(Animation animation) {
//
//                            }
//                        });
//                    }
//                });
//
//                signInGoogleButton.startAnimation(anim);
//
//
//                /////////////////
//
            });
            signInGoogleButton.morph(morphingAnimationGoogle);
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

    private Animator showAnimation(final View view, final View buttonView, int color) {
        view.setBackgroundColor(getResources().getColor(color));
        int cx = (int) (buttonView.getX() + buttonView.getWidth() / 2) + 28;
        int cy = (int) (buttonView.getY() + buttonView.getHeight() / 2) + 28;

        int finalRadius = Math.max(view.getWidth(), view.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 28, finalRadius);
        anim.setDuration(DURATION_ANIMATION);

        view.setVisibility(View.VISIBLE);

        AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
        animation1.setDuration(DURATION_ANIMATION);
        view.startAnimation(animation1);
        anim.start();
        return anim;
    }

    String LOG = "TEST_PSRAM";
    int xDest;
    int yDest;

    public Animation moveCenter(View view) {
        LinearLayout root = (LinearLayout) findViewById(R.id.containerLL);
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int statusBarOffset = root.getHeight() - root.getMeasuredHeight();

        int originalPos[] = new int[2];
        view.getLocationOnScreen(originalPos);

        xDest = dm.widthPixels / 2;
        xDest -= (view.getMeasuredWidth() / 2);
        yDest = dm.heightPixels / 2 - (view.getMeasuredHeight() / 2) - statusBarOffset;

        TranslateAnimation anim = new TranslateAnimation(0, xDest - originalPos[0], 0, yDest - originalPos[1]);
        anim.setDuration(1000);
        anim.setFillAfter(true);
        anim.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(anim);
        return anim;

    }


    public void useTransparency(View view) {
        AlphaAnimation animation1 = new AlphaAnimation(1f, .0f);
        animation1.setDuration(1000);
        animation1.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                view.setAlpha(.0f);
                view.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation1);
    }


}
