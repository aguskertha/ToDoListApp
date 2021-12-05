package com.example.todoapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todoapp.Model.Task;
import com.example.todoapp.Model.Topic;
import com.example.todoapp.Utils.DataBaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddNewTask extends BottomSheetDialogFragment implements DatePickerDialog.OnDateSetListener{
    public static final String TAG = "AddNewTask";
    private EditText edtTaskTitle;
    private EditText edtTaskDate;
    private Button btnAddTask;
    private ImageButton btnDatePicker;
    private DataBaseHelper db;
    private View view;
    boolean isUpdate = false;
    private int topicID;
    private Date dateDeadline;
    public AddNewTask(int topicID){
        this.topicID = topicID;
    }

    public AddNewTask(){

    }

    public static AddNewTask newInstance(int topicID){
        return new AddNewTask(topicID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.add_new_task_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtTaskTitle = view.findViewById(R.id.edt_task_title);
        edtTaskDate = view.findViewById(R.id.edt_task_date);
        btnAddTask = view.findViewById(R.id.btn_save_task);
        btnDatePicker = view.findViewById(R.id.btn_date_picker);
        btnDatePicker = view.findViewById(R.id.btn_date_picker);

        db = new DataBaseHelper(getActivity());

        Bundle bundle = getArguments();
        if(bundle != null){
            isUpdate = true;

            Task task = bundle.getParcelable(Task.EXTRA_TASK);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            edtTaskTitle.setText(task.getTitle());
            edtTaskDate.setText(dateFormat.format(task.getDeadline()));
            isButtonAddEnabled();
        }

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        view.getContext(),
                        AddNewTask.this::onDateSet,
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.show();
            }
        });

        edtTaskTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                isButtonAddEnabled();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String taskTitle = edtTaskTitle.getText().toString().trim();
                String taskDeadline = edtTaskDate.getText().toString().trim();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

                if(isUpdate){
                    if(bundle != null) {
                        Task task = bundle.getParcelable(Task.EXTRA_TASK);
                        task.setTitle(taskTitle);
                        try {
                            task.setDeadline(dateFormat.parse(taskDeadline));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        db.updateTask(task);
                    }
                }
                else{
                    Task task = new Task();
                    task.setTopicID(topicID);
                    task.setTitle(taskTitle);
                    task.setStatus(0);
                    task.setDeadline(dateDeadline);
                    db.insertTask(task);
                }
                dismiss();
            }
        });

    }

    public void isButtonAddEnabled(){
        if(edtTaskTitle.getText().toString().isEmpty() ){
            btnAddTask.setEnabled(false);
        }
        else {
            btnAddTask.setEnabled(true);
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if(activity instanceof OnDialogCloseListener){
            ((OnDialogCloseListener) activity).onDialogClose(dialog);
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String textDate = dateFormat.format(calendar.getTime());
        try {
            dateDeadline = dateFormat.parse(textDate);
            edtTaskDate.setText(textDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
