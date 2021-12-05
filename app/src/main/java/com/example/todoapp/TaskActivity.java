package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.todoapp.Adapter.TaskAdapter;
import com.example.todoapp.Model.Task;
import com.example.todoapp.Model.Topic;
import com.example.todoapp.Utils.DataBaseHelper;
import com.example.todoapp.databinding.ActivityTaskBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskActivity extends AppCompatActivity implements OnDialogCloseListener{

    private ActivityTaskBinding binding;
    private View view;
    private DataBaseHelper db;
    private Topic topic;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;

    public TaskActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTaskBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);

        db = new DataBaseHelper(view.getContext());
        taskList = new ArrayList<>();
        setToolbar();
        setReceiveTopic();
        setContentTopic();
        setRecycleViewTask();
        setFloatingAddTask();
    }

    public void setRecycleViewTask(){
        taskList = db.getTaskByTopicID(topic.getId());
        Collections.reverse(taskList);
        for(Task task : taskList){
        }
        if(taskList.isEmpty()){
            System.out.println("HAHAH: ");
        }
        binding.rvTask.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(this.taskList, db);
        binding.rvTask.setAdapter(taskAdapter);
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        taskList = db.getTaskByTopicID(topic.getId());
        for(Task task : taskList){
            System.out.println("HAHAH: "+task.getTitle());
        }
        Collections.reverse(taskList);
        taskAdapter.setTaskList(taskList);
        taskAdapter.notifyDataSetChanged();
    }

    public void setReceiveTopic(){
        if(getIntent() != null){
            topic = getIntent().getParcelableExtra(Topic.EXTRA_TOPIC);
        }
    }

    public void setContentTopic(){
        binding.tvTaskTopicTitle.setText(topic.getTitle());
        binding.tvTaskTopicDescription.setText(topic.getDescription());
    }

    private void setToolbar(){
        setSupportActionBar(binding.taskToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Task");
    }

    public void setFloatingAddTask(){
        binding.flAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewTask.newInstance(topic.getId()).show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}