package com.elishanto.schoolconnect.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.elishanto.schoolconnect.DividerItemDecoration;
import com.elishanto.schoolconnect.R;
import com.elishanto.schoolconnect.activity.MainActivity;
import com.elishanto.schoolconnect.adapter.Homework;
import com.elishanto.schoolconnect.adapter.HomeworkAdapter;
import com.elishanto.schoolconnect.adapter.MarksAdapter;
import com.elishanto.schoolconnect.adapter.Subject;
import com.elishanto.schoolconnect.task.HomeworkTask;
import com.elishanto.schoolconnect.task.MarksTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by elishanto on 04/02/16.
 */
public class HomeworkFragment extends Fragment {
    static RecyclerView rv;
    static ProgressBar cpv;
    LinearLayoutManager llm;
    static HomeworkAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static Fragment newInstance() {
        return new HomeworkFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_homework, null);
        ((AppCompatActivity) this.getActivity()).getSupportActionBar().setTitle(this.getContext().getString(this.getContext().getApplicationInfo().labelRes));
        if(MainActivity.getInterstitialAd().isLoaded() && MainActivity.getCounter() % 5 == 0 && !MainActivity.isFirst()) {
            MainActivity.getInterstitialAd().show();
        }
        rv = (RecyclerView) root.findViewById(R.id.rv_homework);
        rv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        cpv = (ProgressBar) root.findViewById(R.id.progress_view_homework);
        if(MainActivity.getHomeworks() != null && !MainActivity.getHomeworks().isEmpty()) {
            cpv.setEnabled(false);
            cpv.setVisibility(View.INVISIBLE);
        }
        rv.setHasFixedSize(true);
        llm = new LinearLayoutManager(root.getContext());
        rv.setLayoutManager(llm);
        if(MainActivity.getHomeworks().isEmpty())
            initializeData();
        adapter = new HomeworkAdapter(homeworks, getContext());
        rv.setAdapter(adapter);
        MainActivity.incCounter();
        System.out.println(MainActivity.getCounter());
        return root;
    }

    private static List<Homework> homeworks;

    public static List<Homework> getHomeworks() {
        return homeworks;
    }

    public static HomeworkAdapter getAdapter() {
        return adapter;
    }

    public static ProgressBar getCpv() {
        return cpv;
    }

    public static RecyclerView getRv() {
        return rv;
    }

    private void initializeData(){
        homeworks = new ArrayList<>();
        if(MainActivity.getHomeworks() != null && !MainActivity.getHomeworks().isEmpty())
            homeworks = MainActivity.getHomeworks();
        else
            new HomeworkTask().execute(MainActivity.getLogin(), MainActivity.getPassword(), null, HomeworkFragment.this);
    }
}
