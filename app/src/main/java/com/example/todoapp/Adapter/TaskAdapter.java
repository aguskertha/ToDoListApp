package com.example.todoapp.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.AddNewTask;
import com.example.todoapp.AddNewTopic;
import com.example.todoapp.MainActivity;
import com.example.todoapp.Model.Task;
import com.example.todoapp.Model.Topic;
import com.example.todoapp.R;
import com.example.todoapp.Utils.DataBaseHelper;
import com.google.android.material.card.MaterialCardView;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private DataBaseHelper db;
    private View view;

    public TaskAdapter(List<Task> taskList, DataBaseHelper db){
        this.db = db;
        this.taskList = taskList;
    }

    public void setTaskList(List<Task> taskList){
        this.taskList = taskList;
        notifyDataSetChanged();
    }

    public void deleteTask(int position){
        db.deleteTask(taskList.get(position).getId());
        taskList.remove(position);
        notifyItemRemoved(position);
    }

    public void editTask(int position){
        Bundle bundle = new Bundle();
        bundle.putParcelable(Task.EXTRA_TASK, taskList.get(position));

        AddNewTask newTask = new AddNewTask();
        newTask.setArguments(bundle);
        newTask.show( ((AppCompatActivity)view.getContext()).getSupportFragmentManager(), newTask.TAG);
    }

    public void updateTaskStatus(int position, int status){
        Task task = taskList.get(position);
        db.updateStatus(task.getId(), status);
//        notifyItemChanged(position);
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Task task = taskList.get(position);
        holder.checkBoxTask.setText(task.getTitle());
        holder.checkBoxTask.setChecked(toBoolean(task.getStatus()));

        holder.checkBoxTask.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    updateTaskStatus(holder.getAdapterPosition(), 1);
                    holder.imageButtonEdit.setVisibility(View.GONE);
                    holder.btnTaskDate.setColorFilter(((Activity)view.getContext()).getResources().getColor(R.color.green));
                    holder.cvTask.setStrokeColor(((Activity)view.getContext()).getResources().getColor(R.color.white) );
                }
                else {
                    updateTaskStatus(holder.getAdapterPosition(), 0);
                    holder.imageButtonEdit.setVisibility(View.VISIBLE);
                    if(compareDate(task.getDeadline())){
                        holder.cvTask.setStrokeColor(((Activity)view.getContext()).getResources().getColor(R.color.red) );
                        holder.btnTaskDate.setColorFilter(((Activity)view.getContext()).getResources().getColor(R.color.red));
                    }
                    else {
                        holder.btnTaskDate.setColorFilter(((Activity)view.getContext()).getResources().getColor(R.color.green));
                        holder.cvTask.setStrokeColor(((Activity)view.getContext()).getResources().getColor(R.color.white) );
                    }
                }
            }
        });

        holder.imageButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteTask(holder.getAdapterPosition());
            }
        });

        holder.imageButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTask(position);
            }
        });

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        holder.tvTaskDate.setText(dateFormat.format(task.getDeadline()));

        if(compareDate(task.getDeadline())){
            if(holder.checkBoxTask.isChecked()){
                holder.imageButtonEdit.setVisibility(View.GONE);
                holder.btnTaskDate.setColorFilter(((Activity)view.getContext()).getResources().getColor(R.color.green));
                holder.cvTask.setStrokeColor(((Activity)view.getContext()).getResources().getColor(R.color.white) );
            }
            else {
                holder.imageButtonEdit.setVisibility(View.VISIBLE);
                holder.cvTask.setStrokeColor(((Activity)view.getContext()).getResources().getColor(R.color.red) );
                holder.btnTaskDate.setColorFilter(((Activity)view.getContext()).getResources().getColor(R.color.red));
            }
        }
        else {
            if(holder.checkBoxTask.isChecked()){
                holder.imageButtonEdit.setVisibility(View.GONE);
            }
            else {
                holder.imageButtonEdit.setVisibility(View.VISIBLE);
            }
            holder.btnTaskDate.setColorFilter(((Activity)view.getContext()).getResources().getColor(R.color.green));
            holder.cvTask.setStrokeColor(((Activity)view.getContext()).getResources().getColor(R.color.white) );
        }

    }

    public boolean compareDate(Date dateDeadline){
        boolean isOverdue = false;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        String strDate = dateFormat.format(date);
        try {
            date = dateFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date.after(dateDeadline)) {
            isOverdue = true;
        }

        return isOverdue;
    }

    public static boolean toBoolean(int val) {
        if (val <= 0) return false;
        return true;
    }


    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBoxTask;
        ImageButton imageButtonDelete;
        ImageButton imageButtonEdit;
        ImageButton btnTaskDate;
        TextView tvTaskDate;
        MaterialCardView cvTask;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBoxTask = itemView.findViewById(R.id.checkBox_task);
            imageButtonDelete = itemView.findViewById(R.id.img_btn_delete);
            imageButtonEdit = itemView.findViewById(R.id.img_btn_edit);
            btnTaskDate = itemView.findViewById(R.id.btn_task_date);
            tvTaskDate = itemView.findViewById(R.id.tv_task_date);
            cvTask = itemView.findViewById(R.id.cv_task);
        }
    }
}
