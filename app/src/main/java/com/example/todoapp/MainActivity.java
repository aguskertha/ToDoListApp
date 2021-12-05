package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.amitshekhar.DebugDB;
import com.example.todoapp.Adapter.TopicAdapter;
import com.example.todoapp.Model.Topic;
import com.example.todoapp.Utils.DataBaseHelper;
import com.example.todoapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDialogCloseListener{

    private DataBaseHelper db;
    private List<Topic> topicList;
    private TopicAdapter topicAdapter;
    private ActivityMainBinding binding;
    private SharedPreferences getSession;
    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.setTitle("TwoDo");
        setSupportActionBar(binding.toolbar);

        DebugDB.getAddressLog();
        db = new DataBaseHelper(MainActivity.this);

        getSession = getSharedPreferences(LoginActivity.SESSION_LOGIN_USER, MODE_PRIVATE);

        if(getSession.contains(LoginActivity.SESSION_USER_ID)) {

            this.userID = getSession.getInt(LoginActivity.SESSION_USER_ID, 0);
            binding.tvName.setText(getSession.getString(LoginActivity.SESSION_USER_FULLNAME, null)+" - Topic");
            topicList = new ArrayList<>();
            topicList = db.getTopicByUserID(userID);
            Collections.reverse(topicList);

            topicAdapter = new TopicAdapter(topicList, db);
            binding.rvTopic.setLayoutManager(new LinearLayoutManager(this));
            binding.rvTopic.setAdapter(topicAdapter);

            binding.flAddTopic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddNewTopic.newInstance(userID).show(getSupportFragmentManager(), AddNewTopic.TAG);
                }
            });
        }
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        topicList = db.getTopicByUserID(userID);
        Collections.reverse(topicList);
        topicAdapter.setTopicList(topicList);
        topicAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.top_menu_logout:
                Toast.makeText(getApplicationContext(), "LOGOUT", Toast.LENGTH_SHORT).show();
                getSession.edit().clear().commit();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}