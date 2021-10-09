package com.example.todoapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todoapp.AddNewTask;
import com.example.todoapp.MainActivity;
import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.R;
import com.example.todoapp.Utils.DataBaseHelper;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder> {

    private List<ToDoModel> modelList;
    private MainActivity activity;
    private DataBaseHelper db;

    public ToDoAdapter(DataBaseHelper db, MainActivity mainActivity){
        this.activity = mainActivity;
        this.db = db;
    }

    public Context getContext(){
        return activity;
    }

    public void setTask(List<ToDoModel> modelList){
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    public void deleteTask(int position){
        ToDoModel task = modelList.get(position);
        db.deleteTask(task.getId());
        modelList.remove(position);
        notifyItemRemoved(position);

    }

    public void editTask(int position){
        ToDoModel task = modelList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", task.getId());
        bundle.putString("task", task.getTask());

        AddNewTask newTask = new AddNewTask();
        newTask.setArguments(bundle);
        newTask.show(activity.getSupportFragmentManager(), newTask.TAG);
    }

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new ToDoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final ToDoModel task = modelList.get(position);
        holder.checkBoxTask.setText(task.getTask());
        holder.checkBoxTask.setChecked(toBoolean(task.getStatus()));
        holder.checkBoxTask.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    db.updateStatus(task.getId(), 1);
                }
                else {
                    db.updateStatus(task.getId(), 0);
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
    }



    public boolean toBoolean(int num){
        return num!=0;
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ToDoViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBoxTask;
        ImageButton imageButtonDelete;
        ImageButton imageButtonEdit;
        public ToDoViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBoxTask = itemView.findViewById(R.id.checkBox_task);
            imageButtonDelete = itemView.findViewById(R.id.img_btn_delete);
            imageButtonEdit = itemView.findViewById(R.id.img_btn_edit);
        }
    }
}
