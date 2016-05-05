package com.elishanto.schoololoconnect.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elishanto.schoololoconnect.R;
import com.elishanto.schoololoconnect.activity.MainActivity;
import com.elishanto.schoololoconnect.fragment.MarksFragment;
import com.elishanto.schoololoconnect.fragment.SubjectFragment;

import java.util.List;

public class MarksAdapter extends RecyclerView.Adapter<MarksAdapter.SubjectViewHolder>{

    public static class SubjectViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView name;
        TextView marks;
        TextView avg;

        SubjectViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            name = (TextView) itemView.findViewById(R.id.homework_subject);
            marks = (TextView) itemView.findViewById(R.id.subject_marks);
            avg = (TextView) itemView.findViewById(R.id.subject_avg);
        }
    }

    List<Subject> subjects;

    public MarksAdapter(List<Subject> subjects){
        this.subjects = subjects;
    }

    @Override
    public SubjectViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        int useCard = Integer.parseInt(parent.getContext().getSharedPreferences("settings", Context.MODE_PRIVATE).getString("useCard", "0"));
        if (useCard == 1 || useCard == 2) {
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("subject", subjects.get(MarksFragment.getRv().indexOfChild(v)).name);
                    MainActivity.replace(SubjectFragment.newInstance(bundle));
                }
            });
        }
        return new SubjectViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SubjectViewHolder holder, int position) {
        holder.name.setText(subjects.get(position).name);
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        for(Pair<String, String> pair : subjects.get(position).marks) {
            SpannableString string = new SpannableString(pair.first.replace("B", ""));
            if(pair.first.contains("2"))
                string.setSpan(new BackgroundColorSpan(Color.parseColor("#810A00")), 0, string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            else if(pair.first.contains("3"))
                string.setSpan(new BackgroundColorSpan(Color.parseColor("#B93C00")), 0, string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            else if(pair.first.contains("4") || pair.first.contains("5"))
                string.setSpan(new BackgroundColorSpan(Color.parseColor("#51BE00")), 0, string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if(pair.first.startsWith("B"))
                string.setSpan(new StyleSpan(Typeface.BOLD), 0, string.length(), 0);
            ssb.append(string).append(" ");
        }
        holder.marks.setText(ssb);
        holder.avg.setText(subjects.get(position).avg);
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}