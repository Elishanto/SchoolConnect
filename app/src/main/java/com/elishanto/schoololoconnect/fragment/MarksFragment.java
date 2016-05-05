package com.elishanto.schoololoconnect.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.elishanto.schoololoconnect.activity.MainActivity;
import com.elishanto.schoololoconnect.R;
import com.elishanto.schoololoconnect.adapter.MarksAdapter;
import com.elishanto.schoololoconnect.adapter.Subject;
import com.elishanto.schoololoconnect.task.MarksTask;

import java.util.ArrayList;
import java.util.List;

public class MarksFragment extends Fragment {

    static RecyclerView rv;
    static ProgressBar cpv;
    LinearLayoutManager llm;
    static MarksAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static Fragment newInstance() {
        return new MarksFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_marks, null);
        ((AppCompatActivity) this.getActivity()).getSupportActionBar().setTitle(this.getContext().getString(this.getContext().getApplicationInfo().labelRes));
        rv = (RecyclerView) root.findViewById(R.id.rv_marks);
        cpv = (ProgressBar) root.findViewById(R.id.progress_view_marks);
        if(MainActivity.getSubjects() != null && !MainActivity.getSubjects().isEmpty()) {
            cpv.setEnabled(false);
            cpv.setVisibility(View.INVISIBLE);
        }
        rv.setHasFixedSize(true);
        llm = new LinearLayoutManager(root.getContext());
        rv.setLayoutManager(llm);
        if(MainActivity.getSubjects().isEmpty())
            initializeData();
        adapter = new MarksAdapter(subjects);
        rv.setAdapter(adapter);
        return root;
    }

    private static List<Subject> subjects;

    public static List<Subject> getSubjects() {
        return subjects;
    }

    public static MarksAdapter getAdapter() {
        return adapter;
    }

    public static ProgressBar getCpv() {
        return cpv;
    }

    public static RecyclerView getRv() {
        return rv;
    }

    private void initializeData(){
        subjects = new ArrayList<>();
        if(MainActivity.getSubjects() != null && !MainActivity.getSubjects().isEmpty())
            subjects = MainActivity.getSubjects();
        else
            new MarksTask().execute(MainActivity.getLogin(), MainActivity.getPassword(), MarksFragment.this);
    }
}
