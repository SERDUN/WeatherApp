package com.example.dmitro.weatherapp.service.auth;

import android.content.Context;
import android.os.CancellationSignal;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.credentials.ClearCredentialStateRequest;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.ClearCredentialException;
import androidx.credentials.exceptions.GetCredentialException;

import com.example.dmitro.weatherapp.BuildConfig;
import com.example.dmitro.weatherapp.data.model.user.UserProfile;
import com.example.dmitro.weatherapp.utils.callback.Action0;
import com.example.dmitro.weatherapp.utils.callback.Action1;
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;

/** Sign in with Google through Credential Manager; needs the web (server) OAuth client id. */
public class GoogleAuthService {
    private static final String TAG = "GoogleAuthService";
    private final CredentialManager credentialManager;
    private final Context context;

    public GoogleAuthService(Context context) {
        this.context = context.getApplicationContext();
        this.credentialManager = CredentialManager.create(this.context);
    }

    public static boolean isConfigured() {
        return !BuildConfig.GOOGLE_SERVER_CLIENT_ID.isEmpty();
    }

    /** The activity context is required: Credential Manager shows its UI on top of it. */
    public void signIn(@NonNull Context activity, Action1<UserProfile> onSuccess, Action1<Throwable> onFailure) {
        GetSignInWithGoogleOption option = new GetSignInWithGoogleOption.Builder(BuildConfig.GOOGLE_SERVER_CLIENT_ID).build();
        GetCredentialRequest request = new GetCredentialRequest.Builder().addCredentialOption(option).build();

        credentialManager.getCredentialAsync(activity, request, new CancellationSignal(),
                ContextCompat.getMainExecutor(activity),
                new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                    @Override
                    public void onResult(GetCredentialResponse response) {
                        Credential credential = response.getCredential();
                        if (credential instanceof CustomCredential
                                && GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL.equals(credential.getType())) {
                            GoogleIdTokenCredential google = GoogleIdTokenCredential.createFrom(credential.getData());
                            String name = google.getDisplayName() == null ? google.getId() : google.getDisplayName();
                            String photo = google.getProfilePictureUri() == null ? null : google.getProfilePictureUri().toString();
                            onSuccess.call(new UserProfile(name, google.getId(), photo));
                        } else {
                            onFailure.call(new IllegalStateException("Unexpected credential type " + credential.getType()));
                        }
                    }

                    @Override
                    public void onError(@NonNull GetCredentialException e) {
                        Log.w(TAG, "Sign in with Google failed: " + e.getClass().getSimpleName(), e);
                        onFailure.call(e);
                    }
                });
    }

    public void signOut(Action0 onComplete) {
        credentialManager.clearCredentialStateAsync(new ClearCredentialStateRequest(), new CancellationSignal(),
                ContextCompat.getMainExecutor(context),
                new CredentialManagerCallback<Void, ClearCredentialException>() {
                    @Override
                    public void onResult(Void unused) {
                        onComplete.call();
                    }

                    @Override
                    public void onError(@NonNull ClearCredentialException e) {
                        onComplete.call();
                    }
                });
    }
}
