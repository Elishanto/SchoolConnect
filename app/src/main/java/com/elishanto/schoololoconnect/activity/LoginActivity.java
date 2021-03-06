package com.elishanto.schoololoconnect.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.elishanto.schoololoconnect.R;
import com.elishanto.schoololoconnect.task.AuthTask;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    public static SharedPreferences preferences;

    EditText etLogin;
    EditText etPassword;
    ImageView ivShow_pass;
    static Button btEnter;
    ProgressDialog dialog;
    static InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("com.parse.Data")) {
                    JSONObject pushData = new JSONObject(getIntent().getExtras().getString("com.parse.Data"));
                    String intent = pushData.getString("intent");
                    String url = pushData.getString("url");
                    String gp = pushData.getString("gp");
                    if (url != null)
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    else if (gp != null)
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + gp)));
                    else {
                        Intent finalIntent = new Intent();
                        finalIntent.setClassName(LoginActivity.this, intent);
                        startActivity(finalIntent);
                    }
                    finish();
                    System.exit(0);
                }else{
                    preferences = getSharedPreferences("user_data", MODE_PRIVATE);
                    setContentView(R.layout.activity_login);
                    if (preferences.getBoolean("logged_in", false)) {
                        dialog = ProgressDialog.show(new ContextThemeWrapper(LoginActivity.this, R.style.MaterialBaseTheme_Light_AlertDialog), "", "Авторизация", true, false);
                        new AuthTask().execute(getApplicationContext(), LoginActivity.this, preferences.getString("login", ""), preferences.getString("password", ""), dialog);
                    } else {
                        AdView mAdView = (AdView) findViewById(R.id.adViewLogin);
                        AdRequest adRequest = new AdRequest.Builder().build();
                        mAdView.loadAd(adRequest);
                        etLogin = (EditText) findViewById(R.id.etLogin);
                        etPassword = (EditText) findViewById(R.id.etPassword);
                        ivShow_pass = (ImageView) findViewById(R.id.ivShow_pass);
                        btEnter = (Button) findViewById(R.id.btEnter);

                        ivShow_pass.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                    etPassword.setTransformationMethod(null);
                                }
                                if (event.getAction() == MotionEvent.ACTION_UP) {
                                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                }
                                etPassword.setSelection(etPassword.getText().length());
                                return true;
                            }
                        });

                        btEnter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (interstitialAd.isLoaded())
                                    interstitialAd.show();
                                else
                                    auth();
                            }
                        });

                        interstitialAd = new InterstitialAd(this);
                        interstitialAd.setAdUnitId("ca-app-pub-8050884723094020/2084358009");

                        interstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdClosed() {
                                requestNewInterstitial();
                                auth();
                            }
                        });
                        requestNewInterstitial();
                    }
                }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest);
    }

    private void auth() {
        btEnter.setEnabled(false);
        dialog = ProgressDialog.show(new ContextThemeWrapper(LoginActivity.this, R.style.MaterialBaseTheme_Light_AlertDialog), "", "Авторизация", true, false);
        new AuthTask().execute(getApplicationContext(), LoginActivity.this, etLogin.getText().toString(), etPassword.getText().toString(), dialog);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public static Button getBtEnter() {
        return btEnter;
    }
}
