package com.example.pc.readnewsapp;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class FbActivity extends AppCompatActivity {

    ProfilePictureView profilePictureView;
    LoginButton loginButton;
    Button btndangxuat;
    TextView txtname, txtemail, txtBack;


    CallbackManager callbackManager;
    String email,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_fb);
        AnhXa();

        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(
                    "com.example.pc.readnewsapp",
                    PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        for (Signature signature : info.signatures) {
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("SHA");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            md.update(signature.toByteArray());
            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }



        btndangxuat.setVisibility(View.INVISIBLE);
        txtemail.setVisibility(View.INVISIBLE);
        txtname.setVisibility(View.INVISIBLE);
        loginButton.setReadPermissions(Arrays.asList("public_profile","email"));



        setLogin_Button();
        setLogout_Button();
        Back();


    }

    private void setLogout_Button() {
        btndangxuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                btndangxuat.setVisibility(View.INVISIBLE);
                txtemail.setVisibility(View.INVISIBLE);
                txtname.setVisibility(View.INVISIBLE);
                txtemail.setText("");
                txtname.setText("");
                profilePictureView.setProfileId(null);
                loginButton.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setLogin_Button() {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            //dang nhap thanh cong xu ly hàm onsuccess
            public void onSuccess(LoginResult loginResult) {

                loginButton.setVisibility(View.VISIBLE);
                btndangxuat.setVisibility(View.VISIBLE);
                txtemail.setVisibility(View.VISIBLE);
                txtname.setVisibility(View.VISIBLE);
                result();

            }

            //sau khi dang nhap thanh cong xu ly hàm này
            @Override
            public void onCancel() {

            }
            //sau khi dang nhap thatbai  xu ly hàm này
            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private void Back() {
        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FbActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void result() {
        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.d("JSON",response.getJSONObject().toString());
                try {
                    email = object.getString("email");
                    name = object.getString("name");
                    profilePictureView.setProfileId(Profile.getCurrentProfile().getId());
                    txtemail.setText(email);
                    txtname.setText(name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,email");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();

    }

    public void AnhXa(){
        profilePictureView = (ProfilePictureView) findViewById(R.id.image);
        loginButton =(LoginButton) findViewById(R.id.login_button);
        btndangxuat =(Button) findViewById(R.id.buttondangxuat);
        txtemail = (TextView) findViewById(R.id.textviewemail);
        txtname = (TextView) findViewById(R.id.textviewname);
        txtBack = (TextView) findViewById(R.id.textviewBack);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onStart() {
        LoginManager.getInstance().logOut();
        super.onStart();
    }
}
