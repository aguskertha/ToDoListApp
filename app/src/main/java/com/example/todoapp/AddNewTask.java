package com.example.todoapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.Utils.DataBaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddNewTask extends BottomSheetDialogFragment {
    public static final String TAG = "AddNewTask";
    private EditText edtTask;
    private Button btnAddTask;
    private DataBaseHelper db;
    private int userID;

    public AddNewTask(int userID){
        this.userID = userID;
    }

    public AddNewTask(){
        this.userID = 0;
    }

    public static AddNewTask newInstance(int userID){
        return new AddNewTask(userID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_new_task_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtTask = view.findViewById(R.id.edt_task);
        btnAddTask = view.findViewById(R.id.btn_save);

        db = new DataBaseHelper(getActivity());

        boolean isUpdate = false;

        Bundle bundle = getArguments();
        if(bundle != null){
            Toast.makeText(getContext(), "ADA BUNDLE", Toast.LENGTH_SHORT).show();
            isUpdate = true;
            String task = bundle.getString("task");
            edtTask.setText(task);
            if(task.length() > 0){
                btnAddTask.setEnabled(false);
            }
        }

        edtTask.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if(charSequence.toString().equals("")){
                    btnAddTask.setEnabled(false);
                    btnAddTask.setBackgroundColor(Color.GRAY);
                }
                else{
                    btnAddTask.setEnabled(true);
                    btnAddTask.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        boolean finalIsUpdate = isUpdate;
        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = edtTask.getText().toString().trim();

                if(finalIsUpdate){
                    db.updateTask(bundle.getInt("id"), text);
                }
                else{
//                Toast.makeText(getContext(), userID, Toast.LENGTH_SHORT).show();
                    ToDoModel task = new ToDoModel();
                    task.setUserID(userID);
                    task.setTask(text);
                    task.setStatus(0);
                    db.insertTask(task);
                }
                dismiss();
            }
        });

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if(activity instanceof OnDialogCloseListener){
            ((OnDialogCloseListener) activity).onDialogClose(dialog);
        }
    }
}
