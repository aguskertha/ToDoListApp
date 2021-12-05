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

import com.example.todoapp.Model.Task;
import com.example.todoapp.Model.Topic;
import com.example.todoapp.Utils.DataBaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddNewTopic extends BottomSheetDialogFragment {
    public static final String TAG = "AddNewTopic";
    private EditText edtTopicTitle;
    private EditText edtTopicDescription;
    private Button btnAddTopic;
    private DataBaseHelper db;
    private boolean isUpdate = false;
    private int userID;

    public AddNewTopic(){

    }

    public AddNewTopic(int userID){
        this.userID = userID;
    }

    public static AddNewTopic newInstance(int userID){
        return new AddNewTopic(userID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_new_topic_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtTopicTitle = view.findViewById(R.id.edt_topic_title);
        edtTopicDescription = view.findViewById(R.id.edt_topic_description);
        btnAddTopic = view.findViewById(R.id.btn_save_topic);
        btnAddTopic.setEnabled(false);
        db = new DataBaseHelper(getActivity());

        Bundle bundle = getArguments();
        if(bundle != null){
            isUpdate = true;

            Topic topic = bundle.getParcelable(Topic.EXTRA_TOPIC);

            edtTopicTitle.setText(topic.getTitle());
            edtTopicDescription.setText(topic.getDescription());
            isButtonAddEnabled();
        }

        edtTopicDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isButtonAddEnabled();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtTopicTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isButtonAddEnabled();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnAddTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String topicTitle = edtTopicTitle.getText().toString().trim();
                String topicDescription = edtTopicDescription.getText().toString().trim();

                if(isUpdate){
                    if(bundle != null) {
                        Topic topic = bundle.getParcelable(Topic.EXTRA_TOPIC);
                        topic.setTitle(topicTitle);
                        topic.setDescription(topicDescription);
                        db.updateTopic(topic);
                    }
                }
                else{
                    Topic topic = new Topic();
                    topic.setTitle(topicTitle);
                    topic.setDescription(topicDescription);
                    topic.setUserID(userID);
                    db.insertTopic(topic);
                }
                dismiss();
            }
        });
    }

    public void isButtonAddEnabled(){
        if(edtTopicDescription.getText().toString().isEmpty() ){
            btnAddTopic.setEnabled(false);
        }
        else if(edtTopicTitle.getText().toString().isEmpty()){
            btnAddTopic.setEnabled(false);
        }
        else {
            btnAddTopic.setEnabled(true);
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
}
