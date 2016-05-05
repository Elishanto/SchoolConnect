package com.elishanto.schoololoconnect.adapter;

import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elishanto.schoololoconnect.R;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {

    public static class SubjectViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView desc;
        TextView mark;

        SubjectViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv_subject);
            desc = (TextView) itemView.findViewById(R.id.subject_desc);
            mark = (TextView) itemView.findViewById(R.id.subject_mark);
        }
    }

    Subject subject;

    public SubjectAdapter(Subject subject) {
        this.subject = subject;
    }

    @Override
    public SubjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_card, parent, false);
        return new SubjectViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SubjectViewHolder holder, int position) {
        SpannableString temp = new SpannableString(new SpannableStringBuilder(subject.marks.get(position).first));
        if(temp.toString().startsWith("B")) {
            temp = new SpannableString(temp.toString().replace("B", ""));
            temp.setSpan(new StyleSpan(Typeface.BOLD), 0, temp.length(), 0);
        }
        holder.desc.setText(subject.marks.get(position).second);
        holder.mark.setText(temp);
    }

    @Override
    public int getItemCount() {
        return subject.marks.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}