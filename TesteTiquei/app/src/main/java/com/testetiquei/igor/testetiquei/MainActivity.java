package com.testetiquei.igor.testetiquei;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {
    private TextView txtStatus;
    private LoginButton loginBtn;
    private CallbackManager callbackManager;
    private String email;
    private String name;
    private String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        }
        callbackManager = CallbackManager.Factory.create();
        Init();
        LoginFaceBook();
    }

    private void Init() {

        txtStatus = (TextView) findViewById( R.id.textView);
        loginBtn = (LoginButton) findViewById(R.id.login_button);
        loginBtn.setReadPermissions(Arrays.asList("public_profile", "email"));
        email = CarregarDadosFacebook("email");
        name = CarregarDadosFacebook("name");
        gender = CarregarDadosFacebook("gender");
        txtStatus.setText(CarregarDadosFacebook("email") + " " + CarregarDadosFacebook("name") + " " + CarregarDadosFacebook("gender"));
    }


    private void LoginFaceBook() {
        loginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                txtStatus.setText("SUCESSO\n" + loginResult.getAccessToken());
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(final JSONObject object,GraphResponse response) {
                                try {
                                    email = object.getString("email");
                                    name = object.getString("name");
                                    gender = object.getString("gender");
                                    SalvarConfiguracoesFacebook(email, name, gender);
                                    txtStatus.setText(email + " " + name + " " + gender);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,gender,email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                txtStatus.setText("CANCELADO");
            }

            @Override
            public void onError(FacebookException error) {
                txtStatus.setText("Login Error: " + error.getMessage());
            }
        });
   }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void SalvarConfiguracoesFacebook(String email, String nome, String sexo)
    {
        SharedPreferences sharedpreferences = getSharedPreferences("TesteTiqueiPref" , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("email", email);
        editor.putString("name", nome);
        editor.putString("gender", sexo);
        editor.commit();
    }

    private String CarregarDadosFacebook(String nomeChave)
    {
        SharedPreferences sharedpreferences = getSharedPreferences("TesteTiqueiPref" , Context.MODE_PRIVATE);
        return sharedpreferences.getString(nomeChave, "");
    }

    public void AcessarCamera(final View view)
    {
        Intent myIntent = new Intent(this, CameraActivity.class);
        myIntent.putExtra("email", email);
        myIntent.putExtra("name", name);
        myIntent.putExtra("gender", gender);
        startActivity(myIntent);
    }
}