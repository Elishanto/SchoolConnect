package com.elishanto.schoolconnect.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elishanto.schoolconnect.R;
import com.elishanto.schoolconnect.adapter.Homework;

/**
 * Created by elishanto on 04/02/16.
 */
public class TaskActivity extends AppCompatActivity {

    private static int taskId;
    TextView shrt;
    TextView dates;
    TextView desc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        taskId = getIntent().getIntExtra("task", -1);
        Homework task = MainActivity.getHomeworks().get(taskId);
        getSupportActionBar().setTitle(task.getSubject() + "  " + task.getCreated().substring(0, task.getCreated().lastIndexOf('.')) + "-" + task.getDelivery().substring(0, task.getDelivery().lastIndexOf('.')));
        shrt = (TextView) findViewById(R.id.task_short);
        desc = (TextView) findViewById(R.id.task_desc);
        if (task.getDesc().isEmpty())
            shrt.setEnabled(false);
        else
            shrt.setText(task.getDesc());
        desc.setText(task.getFull());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.incCounter();
        if (MainActivity.getInterstitialAd().isLoaded() && MainActivity.getCounter() % 5 == 0)
            MainActivity.getInterstitialAd().show();
    }
}