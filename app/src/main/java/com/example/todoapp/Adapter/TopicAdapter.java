package com.example.todoapp.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.AddNewTopic;
import com.example.todoapp.Model.Task;
import com.example.todoapp.Model.Topic;
import com.example.todoapp.R;
import com.example.todoapp.TaskActivity;
import com.example.todoapp.Utils.DataBaseHelper;

import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

    private List<Topic> topicList;
    private View view;
    private DataBaseHelper db;
    public TopicAdapter(List<Topic> topicList, DataBaseHelper db){
        this.topicList = topicList;
        this.db = db;
    }

    public void setTopicList(List<Topic> topicList){
        this.topicList = topicList;
        notifyDataSetChanged();
    }

    public void deleteTopic(int position){
        db.deleteTopic(topicList.get(position).getId());
        topicList.remove(position);
        notifyItemRemoved(position);
    }

    public void editTopic(int position){
        Bundle bundle = new Bundle();
        bundle.putParcelable(Topic.EXTRA_TOPIC, topicList.get(position));

        AddNewTopic newTopic = new AddNewTopic();
        newTopic.setArguments(bundle);
        newTopic.show( ((AppCompatActivity)view.getContext()).getSupportFragmentManager(), newTopic.TAG);
    }

    @NonNull
    @Override
    public TopicAdapter.TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_layout, parent, false);
        return new TopicAdapter.TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicAdapter.TopicViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Topic topic = topicList.get(position);
        final int[] toggle = {0};
        holder.tvTopicTitle.setText(topic.getTitle());
        holder.tvTopicDescription.setText(topic.getDescription());
        holder.clTopicDesc.setVisibility(View.GONE);
        holder.btnPopUpDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(toggle[0] % 2 == 0){
                    holder.clTopicDesc.setVisibility(View.VISIBLE);
                    holder.btnPopUpDescription.setImageDrawable(view.getContext().getResources().getDrawable(R.drawable.ic_baseline_arrow_drop_up_24));
                }
                else {
                    holder.clTopicDesc.setVisibility(View.GONE);
                    holder.btnPopUpDescription.setImageDrawable(view.getContext().getResources().getDrawable(R.drawable.ic_baseline_arrow_drop_down_24));
                }
                toggle[0]++;
            }
        });

        holder.cvTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(((Activity)view.getContext()), TaskActivity.class);
                intent.putExtra(Topic.EXTRA_TOPIC, topic);
                ((Activity)view.getContext()).startActivity(intent);
            }
        });

        holder.btnTopicEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTopic(position);
            }
        });

        holder.btnTopicDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteTopic(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.topicList.size();
    }

    public class TopicViewHolder extends RecyclerView.ViewHolder {

        TextView tvTopicTitle;
        TextView tvTopicDescription;
        CardView cvTopic;
        ImageButton btnPopUpDescription;
        ImageButton btnTopicEdit;
        ImageButton btnTopicDelete;
        ConstraintLayout clTopicDesc;

        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTopicTitle = itemView.findViewById(R.id.tv_topic_title);
            tvTopicDescription = itemView.findViewById(R.id.tv_topic_desc);
            cvTopic = itemView.findViewById(R.id.cv_topic);
            btnPopUpDescription = itemView.findViewById(R.id.btn_pop_up_description);
            btnTopicDelete = itemView.findViewById(R.id.btn_topic_delete);
            btnTopicEdit = itemView.findViewById(R.id.btn_topic_edit);
            clTopicDesc = itemView.findViewById(R.id.cl_topic_desc);
        }
    }
}
