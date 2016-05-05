package com.elishanto.schoololoconnect.task;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;

import com.elishanto.schoololoconnect.Helper;
import com.elishanto.schoololoconnect.activity.MainActivity;
import com.elishanto.schoololoconnect.adapter.Homework;
import com.elishanto.schoololoconnect.fragment.HomeworkFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by elishanto on 04/02/16.
 */
public class HomeworkTask extends AsyncTask<Object, Void, List<Homework>> {
    String login;
    String password;
    String date;
    Fragment fragment;

    @Override
    protected List<Homework> doInBackground(Object... params) {
        login = (String) params[0];
        password = (String) params[1];
        date = (String) params[2];
        fragment = (Fragment) params[3];
        try {
            String url = String.format("http://%s:8081/homework?login=%s&password=%s", Helper.getConfigValue(fragment.getContext(), "server_ip"), login, password);
            if(date != null)
                url += "&date=" + date;
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
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

            List<Homework> homeworks = new ArrayList<>();

            JSONArray arr = new JSONArray(result);
            for(int i = 0; i < arr.length(); i++) {
                JSONObject object = arr.getJSONObject(i);
                homeworks.add(new Homework(object.getString("created"), object.getString("delivery"), object.getString("subject"), object.getString("desc"), object.getString("full")));
            }
            return homeworks;
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Homework> homeworks) {
        if (homeworks != null) {
            HomeworkFragment.getHomeworks().addAll(homeworks);
            MainActivity.getHomeworks().addAll(homeworks);
            HomeworkFragment.getAdapter().notifyDataSetChanged();
            HomeworkFragment.getCpv().setVisibility(View.INVISIBLE);
            HomeworkFragment.getCpv().setEnabled(false);
            if(MainActivity.isFirst()) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.getInterstitialAd().show();
                    }
                }, 150);
                MainActivity.setFirst(false);
            }
        }
    }
}
