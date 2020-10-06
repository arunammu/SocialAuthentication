package com.example.socialauthentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomePage extends AppCompatActivity {

    @BindView(R.id.google_sign_in_btn)
    SignInButton googleSignInButtonView;

    @BindView(R.id.facebook_login_button)
    LoginButton faceBookSignInBtnView;



    private boolean isGoogleSignInClicked = false;

    // Google Auth Keys

    GoogleSignInClient googleSignInClient;

    int RC_SIGN_IN = 6;

    // Fb Auth Keys

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        ButterKnife.bind(this);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        googleSignInButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isGoogleSignInClicked = true;
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, RC_SIGN_IN);
            }
        });

        faceBookSignInBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isGoogleSignInClicked = false;
            }
        });


        // Facebook related

        callbackManager = CallbackManager.Factory.create();

      //  faceBookSignInBtnView.setPermissions(Arrays.asList("email", "public_profile"));

        faceBookSignInBtnView.setLoginBehavior(LoginBehavior.WEB_ONLY);

        faceBookSignInBtnView.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Intent faceBookIntent = new Intent(getApplicationContext(), GoogleSignedInActivity.class);
                faceBookIntent.putExtra("isFromFacebookLogin", true);
                faceBookIntent.putExtra("faceBookAccessToken",loginResult.getAccessToken());
                startActivity(faceBookIntent);
                Toast.makeText(HomePage.this, "hai", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(HomePage.this, "error", Toast.LENGTH_SHORT).show();
            }
        });


    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (isGoogleSignInClicked) {
            if (requestCode == RC_SIGN_IN) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
            }

        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

        super.onActivityResult(requestCode, resultCode, data);

    }




    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            startActivity(new Intent(getApplicationContext(), GoogleSignedInActivity.class));
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Google Auth Error", "Result : " + e.getStatusCode());
            Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show();
        }
    }



    @Override
    protected void onStart() {

        // the GoogleSignInAccount will be non-null.
        // Check if a  google signin is made against the app
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            startActivity(new Intent(getApplicationContext(), GoogleSignedInActivity.class));
        }


        // Checks facebook logged in
        if(AccessToken.getCurrentAccessToken() != null){
        Intent faceBookIntent = new Intent(getApplicationContext(), GoogleSignedInActivity.class);
        faceBookIntent.putExtra("isFromFacebookLogin", true);
        faceBookIntent.putExtra("faceBookAccessToken",AccessToken.getCurrentAccessToken());
        startActivity(faceBookIntent);
        }

        super.onStart();
    }

}
