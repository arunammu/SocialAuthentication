package com.example.socialauthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.LogManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class GoogleSignedInActivity extends AppCompatActivity {

    @BindView(R.id.logged_in_user_name)
    TextView loggedInUserName;

    @BindView(R.id.logged_in_user_email)
    TextView loggedInUseEmail;

    @BindView(R.id.user_profile_image)
    CircleImageView userProfileImage;

    @BindView(R.id.sign_out_btn)
    Button signOutBtn;

    @BindView(R.id.logged_in_by)
    TextView loggedInBy;

    GoogleSignInClient googleSignInClient;

    private boolean isFromFacebookLogin = false;

    private AccessToken accessToken ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_signed_in_activity);
        ButterKnife.bind(this);
        isFromFacebookLogin = getIntent().getBooleanExtra("isFromFacebookLogin",false);
        accessToken = getIntent().getParcelableExtra("faceBookAccessToken");
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);

        // Will get the signed account details
        GoogleSignInAccount signedInAccount = GoogleSignIn.getLastSignedInAccount(this);

        if (signedInAccount != null) {

            Glide
                    .with(this)
                    .load(signedInAccount.getPhotoUrl())
                    .placeholder(R.mipmap.profile_pic_placeholder)
                    .into(userProfileImage);


            loggedInUserName.setText(""+signedInAccount.getDisplayName());
            loggedInUseEmail.setText(""+signedInAccount.getEmail());
        }

        if(isFromFacebookLogin)
            loggedInBy.setText("Logged in using FaceBook");
        else
            loggedInBy.setText("Logged in using Google");

        if(isFromFacebookLogin && accessToken!=null){

            renderLoggedUser(accessToken);

        }

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutAction();
            }
        });
    }

    private void renderLoggedUser(AccessToken accessToken){

        GraphRequest graphRequest = new GraphRequest().newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                try {
                        String firstName = object.getString("first_name");
                        String lastName = object.getString("last_name");
                        String email = object.getString("email");
                        String id = object.getString("id");

                        String imageUrl = "https://graph.facebook.com/" + id + "/picture?type=normal";
                        Glide
                                .with(getApplicationContext())
                                .load(imageUrl)
                                .placeholder(R.mipmap.profile_pic_placeholder)
                                .into(userProfileImage);
                        loggedInUserName.setText("" + firstName + " " + lastName);
                        loggedInUseEmail.setText("" + email);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        Bundle params = new Bundle();
        params.putString("fields","first_name,last_name,email,id");
        graphRequest.setParameters(params);
        graphRequest.executeAsync();

    }

    private void signOutAction() {

        if(isFromFacebookLogin){

            LoginManager.getInstance().logOut();

            Intent intent = new Intent(this,HomePage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            /*AccessToken.setCurrentAccessToken(null);
            Profile.setCurrentProfile((Profile)null);
            if (LoginManager.getInstance() != null) {
                LoginManager.getInstance().logOut();
                Toast.makeText(getApplicationContext(),"Successfully Signed out",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), HomePage.class));
                finish();
            }*/


        }else{
            googleSignInClient.signOut()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(),"Successfully Signed out",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), HomePage.class));
                            finish();
                        }
                    });
        }

    }


}