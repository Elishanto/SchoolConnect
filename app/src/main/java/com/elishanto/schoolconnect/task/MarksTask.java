package com.elishanto.schoolconnect.task;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.view.View;

import com.elishanto.schoolconnect.R;
import com.elishanto.schoolconnect.activity.MainActivity;
import com.elishanto.schoolconnect.adapter.Subject;
import com.elishanto.schoolconnect.fragment.MarksFragment;
import com.elishanto.schoolconnect.fragment.SubjectFragment;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

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
import java.util.Iterator;
import java.util.List;

public class MarksTask extends AsyncTask<Object, Void, List<Subject>> {
    String login;
    String password;
    Fragment fragment;

    @Override
    protected List<Subject> doInBackground(Object... params) {
        login = (String) params[0];
        password = (String) params[1];
        fragment = (Fragment) params[2];
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(String.format("http://185.117.154.149:8081/marks?login=%s&password=%s", login, password)).openConnection();
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

            List<Subject> subjects = new ArrayList<>();

            JSONObject object = new JSONObject(result);
            Iterator<String> keys = object.keys();
            while (keys.hasNext()) {
                List<Pair<String, String>> list = new ArrayList<>();
                String next = keys.next();
                JSONArray arr = object.getJSONArray(next);
                for (int k = 1; k < arr.length(); k++) {
                    String mark = arr.getJSONObject(k).getString("mark");
                    String desc = arr.getJSONObject(k).getString("desc");
                    list.add(new Pair<>(mark, desc));
                }
                subjects.add(new Subject(next, list, arr.getJSONObject(0).getString("mark")));
            }
            return subjects;
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Subject> subjects) {
        if (subjects != null) {
            MarksFragment.getSubjects().addAll(subjects);
            MainActivity.getSubjects().addAll(subjects);
            MarksFragment.getAdapter().notifyDataSetChanged();
            MarksFragment.getCpv().setVisibility(View.INVISIBLE);
            MarksFragment.getCpv().setEnabled(false);
            int useCard = Integer.parseInt(fragment.getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE).getString("useCard", "0"));
            MainActivity.getDrawer().addItem(
                    new PrimaryDrawerItem()
                            .withName("Домашние задания")
                            .withIcon(ContextCompat.getDrawable(fragment.getContext(), R.drawable.ic_book_grey_500_24dp))
                            .withSelectedIcon(ContextCompat.getDrawable(fragment.getContext(), R.drawable.ic_book_blue_500_24dp))
            );
            if (useCard == 0 || useCard == 2) {
                MainActivity.getDrawer().addItem(new DividerDrawerItem());
                for (final Subject subject : subjects) {
                    PrimaryDrawerItem item = new PrimaryDrawerItem().withName(subject.name).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                        @Override
                        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                            Bundle bundle = new Bundle();
                            bundle.putString("subject", subject.name);
                            MainActivity.replace(SubjectFragment.newInstance(bundle));
                            return true;
                        }
                    });
                    MainActivity.getDrawer().addItem(item);
                }
            }
        }
    }
}
