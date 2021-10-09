package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.todoapp.Model.User;
import com.example.todoapp.Utils.DataBaseHelper;
import com.example.todoapp.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private DataBaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvRegLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        db = new DataBaseHelper(getApplicationContext());
        binding.btnLogRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean empty = false;
                String username = binding.edtRegUsername.getText().toString().trim();
                String fullname = binding.edtRegName.getText().toString().trim();
                String password = binding.edtRegPassword.getText().toString().trim();
                String confirmPassword = binding.edtRegConfirmPass.getText().toString().trim();

                if(fullname.isEmpty()){
                    binding.edtRegName.setError("Fill fullname");
                    empty = true;
                }
                if(username.isEmpty()){
                    binding.edtRegUsername.setError("Fill username");
                    empty = true;
                }
                if(password.isEmpty()){
                    binding.edtRegPassword.setError("Fill password");
                    empty = true;
                }
                if(confirmPassword.isEmpty()){
                    binding.edtRegConfirmPass.setError("Fill confirm password");
                    empty = true;
                }
                if(!password.equals(confirmPassword)){
                    binding.edtRegConfirmPass.setError("Password not match");
                    binding.edtRegPassword.setError("Password not match");
                    empty = true;
                }
                if(empty){
                    return;
                }
                User user = new User();
                user.setUsername(username);
                user.setFullname(fullname);
                user.setPassword(password);
                if(db.insertUser(user)){
                    Toast.makeText(getApplicationContext(), "Successful created account", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Wrong input!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}