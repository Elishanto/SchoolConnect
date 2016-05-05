package com.elishanto.schoololoconnect.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.elishanto.schoololoconnect.Helper;
import com.elishanto.schoololoconnect.activity.LoginActivity;
import com.elishanto.schoololoconnect.activity.MainActivity;
import com.parse.ParseInstallation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class AuthTask extends AsyncTask<Object, Void, Boolean> {
    Context context;
    Activity activity;
    String login;
    String password;
    String email;
    ProgressDialog dialog;
    @Override
    protected Boolean doInBackground(Object... params) {
        context = (Context) params[0];
        activity = (Activity) params[1];
        login = (String) params[2];
        password = (String) params[3];
        dialog = (ProgressDialog) params[4];
        System.out.println(Arrays.toString(params));
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(String.format("http://%s:8081/auth?login=%s&password=%s", Helper.getConfigValue(context, "server_ip"), login, password)).openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            String result = sb.toString();
            JSONObject object = new JSONObject(result);
            if(object.has("email"))
                email = object.getString("email");
            System.out.println(object);
            return object.getBoolean("success");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if(aBoolean) {
            SharedPreferences.Editor editor = activity.getSharedPreferences("user_data", Context.MODE_PRIVATE).edit();
            editor.putString("login", login);
            editor.putString("password", password);
            MainActivity.setEmail(email);
            editor.putBoolean("logged_in", true);
            editor.apply();
            if(dialog != null)
                dialog.hide();
            context.startActivity(new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            activity.finish();
            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
            if(!installation.has("login")) {
                installation.remove("login");
                installation.remove("password");
                installation.add("login", login);
                installation.add("password", password);
            }
            installation.saveInBackground();
        } else {
            if(dialog != null)
                dialog.hide();
            try {
                LoginActivity.getBtEnter().setEnabled(true);
                Toast.makeText(context, "Неправильный логин или пароль", Toast.LENGTH_LONG).show();
                System.out.println("Failed");
            } catch (NullPointerException e) {
                new AuthTask().execute(activity.getApplicationContext(), LoginActivity.class, activity.getSharedPreferences("user_data", Context.MODE_PRIVATE).getString("login", ""), activity.getSharedPreferences("user_data", Context.MODE_PRIVATE).getString("password", ""), dialog);
            }
        }
    }
}
