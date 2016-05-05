package com.elishanto.schoololoconnect.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elishanto.schoololoconnect.R;
import com.elishanto.schoololoconnect.fragment.HomeworkFragment;
import com.elishanto.schoololoconnect.activity.TaskActivity;

import java.util.List;

public class HomeworkAdapter extends RecyclerView.Adapter<HomeworkAdapter.HomeworkViewHolder>{

    public static class HomeworkViewHolder extends RecyclerView.ViewHolder {
        TextView created_delivery;
        TextView subject;

        HomeworkViewHolder(View itemView) {
            super(itemView);
            created_delivery = (TextView) itemView.findViewById(R.id.homework_dates);
            subject = (TextView) itemView.findViewById(R.id.homework_subject);
        }
    }

    List<Homework> homeworks;
    Context context;

    public HomeworkAdapter(List<Homework> homeworks, Context context){
        this.homeworks = homeworks;
        this.context = context;
    }

    @Override
    public HomeworkViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.homework, parent, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TaskActivity.class);
                intent.putExtra("task", HomeworkFragment.getRv().indexOfChild(v));
                context.startActivity(intent);
            }
        });
        return new HomeworkViewHolder(v);
    }

    @Override
    public void onBindViewHolder(HomeworkViewHolder holder, int position) {
        holder.created_delivery.setText("с " + homeworks.get(position).getCreated() + " по " + homeworks.get(position).getDelivery());
        holder.subject.setText(homeworks.get(position).getSubject());
    }

    @Override
    public int getItemCount() {
        return homeworks.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}