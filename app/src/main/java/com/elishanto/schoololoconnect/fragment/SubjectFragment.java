package com.elishanto.schoololoconnect.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elishanto.schoololoconnect.activity.MainActivity;
import com.elishanto.schoololoconnect.R;
import com.elishanto.schoololoconnect.adapter.Subject;
import com.elishanto.schoololoconnect.adapter.SubjectAdapter;

public class SubjectFragment extends Fragment {
    RecyclerView rv;
    LinearLayoutManager llm;
    static SubjectAdapter adapter;

    public static Fragment newInstance(Bundle bundle) {
        final SubjectFragment fragment = new SubjectFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public SubjectFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_subject, null);
        ((AppCompatActivity) this.getActivity()).getSupportActionBar().setTitle((String) getArguments().get("subject"));
        rv = (RecyclerView) root.findViewById(R.id.rv_subject);
        rv.setHasFixedSize(true);
        llm = new LinearLayoutManager(root.getContext());
        rv.setLayoutManager(llm);
        initializeData();
        adapter = new SubjectAdapter(subject);
        rv.setAdapter(adapter);
        return root;
    }

    private static Subject subject;

    private void initializeData(){
        if(MainActivity.getSubjects() != null && !MainActivity.getSubjects().isEmpty()) {
            for(Subject subjecte : MainActivity.getSubjects()) {
                if(subjecte.name.equals(getArguments().get("subject")))
                    subject = subjecte;
            }
        }
    }
}
