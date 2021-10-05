package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.todoapp.Model.User;
import com.example.todoapp.Utils.DataBaseHelper;
import com.example.todoapp.databinding.ActivityLoginBinding;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {

    public static final String SESSION_USER_ID = "SESSION_USER_ID";
    public static final String SESSION_USER_FULLNAME = "SESSION_USER_FULLNAME";
    public static final String SESSION_LOGIN_USER = "SESSION_LOGIN_USER";

    private ActivityLoginBinding binding;
    private DataBaseHelper db;
    private SharedPreferences.Editor setSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = new DataBaseHelper(getApplicationContext());
        setSession = getSharedPreferences(SESSION_LOGIN_USER, MODE_PRIVATE).edit();
        binding.btnLogLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = binding.edtLogEmail.getText().toString().trim();
                String password = binding.edtLogPassword.getText().toString().trim();

                User user = new User();
                user.setUsername(username);
                user.setPassword(password);

                User currentUser = db.Authenticate(user);
                if (currentUser != null) {

                    setSession.putInt(SESSION_USER_ID, currentUser.getId());
                    setSession.putString(SESSION_USER_FULLNAME, currentUser.getFullname());
                    setSession.apply();

                    Snackbar.make(binding.btnLogLogin, "Successfully Logged in!", Snackbar.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
//                    finish();

                } else {

                    //User Logged in Failed
                    Snackbar.make(binding.btnLogLogin, "Failed to log in , please try again", Snackbar.LENGTH_LONG).show();

                }

            }
        });

        binding.tvLogRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}