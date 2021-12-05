package com.example.todoapp.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.todoapp.Model.Task;
import com.example.todoapp.Model.Topic;
import com.example.todoapp.Model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;
    private static final String DATABASE_NAME = "TWODO";

    private static final String TABLE_TASK = "TASK_TABLE";
    private static final String TABLE_TOPIC = "TOPIC_TABLE";
    private static final String TABLE_USER = "USER_TABLE";

    private static final String KEY_ID = "ID";
    private static final String KEY_CREATED_AT = "CREATED_AT";

    private static final String KEY_TASK_TITLE = "TITLE";
    private static final String KEY_TASK_STATUS = "STATUS";
    private static final String KEY_TASK_DEADLINE = "DEADLINE";
    private static final String KEY_TASK_TOPIC_ID = "TOPIC_ID";

    private static final String KEY_TOPIC_TITLE = "TITLE";
    private static final String KEY_TOPIC_DESCRIPTION = "DESCRIPTION";
    private static final String KEY_TOPIC_USER_ID = "USER_ID";

    private static final String KEY_USER_USERNAME = "USERNAME";
    private static final String KEY_USER_PASSWORD = "PASSWORD";
    private static final String KEY_USER_FULLNAME = "FULLNAME";

    private static final String CREATE_TABLE_TASK = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TASK + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_TASK_TITLE+" TEXT,"
            + KEY_TASK_STATUS+" INTEGER,"
            + KEY_TASK_DEADLINE+" DEADLINE,"
            + KEY_CREATED_AT+" DATETIME,"
            + KEY_TASK_TOPIC_ID + " INTEGER,"
            + "FOREIGN KEY ("+KEY_TASK_TOPIC_ID+") REFERENCES "+TABLE_TOPIC+"("+KEY_ID+"));";

    private static final String CREATE_TABLE_TOPIC = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TOPIC + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_TOPIC_TITLE+" TEXT,"
            + KEY_TOPIC_DESCRIPTION+" TEXT,"
            + KEY_CREATED_AT+" DATETIME,"
            + KEY_TOPIC_USER_ID + " INTEGER,"
            + "FOREIGN KEY ("+KEY_TOPIC_USER_ID+") REFERENCES "+TABLE_USER+"("+KEY_ID+"));";

    private static final String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS "
            + TABLE_USER + "(" +KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_USER_USERNAME+" TEXT,"
            + KEY_USER_PASSWORD+" TEXT,"
            + KEY_USER_FULLNAME+" TEXT,"
            + KEY_CREATED_AT+" DATETIME)";

    private static final String DROP_TABLE_USER = "DROP TABLE IF EXISTS " + TABLE_USER;
    private static final String DROP_TABLE_TASK = "DROP TABLE IF EXISTS " + TABLE_TASK;
    private static final String DROP_TABLE_TOPIC = "DROP TABLE IF EXISTS " + TABLE_TOPIC;

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_TOPIC);
        db.execSQL(CREATE_TABLE_TASK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(DROP_TABLE_USER);
        db.execSQL(DROP_TABLE_TOPIC);
        db.execSQL(DROP_TABLE_TASK);
        onCreate(db);
    }

    public void insertTask(Task task){
        db = this.getWritableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateDeadlineFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        ContentValues values = new ContentValues();
        values.put(KEY_TASK_TITLE, task.getTitle());
        values.put(KEY_TASK_STATUS, 0);
        values.put(KEY_TASK_DEADLINE, dateDeadlineFormat.format(task.getDeadline() ));
        values.put(KEY_CREATED_AT, dateFormat.format(date));
        values.put(KEY_TASK_TOPIC_ID, task.getTopicID() );
        db.insert(TABLE_TASK, null, values);
    }

    public void insertTopic(Topic topic){
        db = this.getWritableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        ContentValues values = new ContentValues();
        values.put(KEY_TOPIC_TITLE, topic.getTitle());
        values.put(KEY_TOPIC_DESCRIPTION, topic.getDescription());
        values.put(KEY_CREATED_AT, dateFormat.format(date));
        values.put(KEY_TOPIC_USER_ID, topic.getUserID());
        db.insert(TABLE_TOPIC, null, values);
    }

    public boolean insertUser(User model){
        boolean valid = false;
        db = this.getWritableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_USERNAME, model.getUsername());
        values.put(KEY_USER_PASSWORD, model.getPassword());
        values.put(KEY_USER_FULLNAME, model.getFullname());
        values.put(KEY_CREATED_AT, dateFormat.format(date));
        long rowInserted = db.insert(TABLE_USER, null, values);
        if (rowInserted != -1){
            valid = true;
        }
        else {
            valid = false;
        }
        return valid;
    }

    public void updateTask(Task task){
        SimpleDateFormat dateDeadlineFormat = new SimpleDateFormat("dd-MM-yyyy");
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TASK_TITLE, task.getTitle());
        values.put(KEY_TASK_DEADLINE, dateDeadlineFormat.format(task.getDeadline()) );
        db.update(TABLE_TASK, values, "ID=?", new String[]{String.valueOf(task.getId())});
    }

    public void updateStatus(int id, int status){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TASK_STATUS, status);
        db.update(TABLE_TASK, values, "ID=?", new String[]{String.valueOf(id)});
    }

    public void updateTopic(Topic topic){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TOPIC_TITLE, topic.getTitle());
        values.put(KEY_TOPIC_DESCRIPTION, topic.getDescription());
        db.update(TABLE_TOPIC, values, "ID=?", new String[]{String.valueOf(topic.getId())});
    }

    public void deleteTask(int id){
        db = this.getWritableDatabase();
        db.delete(TABLE_TASK, "ID=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteTopic(int id){
        db = this.getWritableDatabase();
        db.delete(TABLE_TOPIC, "ID=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public User Authenticate(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER,
                new String[]{KEY_ID, KEY_USER_USERNAME, KEY_USER_PASSWORD, KEY_USER_FULLNAME},//Selecting columns want to query
                KEY_USER_USERNAME + "=?",
                new String[]{user.getUsername()},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()&& cursor.getCount()>0) {
            User newUser = new User();
            newUser.setId(cursor.getInt(0));
            newUser.setUsername(cursor.getString(1));
            newUser.setPassword(cursor.getString(2));
            newUser.setFullname(cursor.getString(3));
            if (user.getPassword().equalsIgnoreCase(newUser.getPassword())) {
                return newUser;
            }
        }
        return null;
    }

    @SuppressLint("Range")
    public List<Topic> getTopicByUserID(int userID){
        db = this.getWritableDatabase();
        Cursor cursor = null;
        List<Topic> topicList = new ArrayList<>();

        db.beginTransaction();
        try{
            cursor = db.query(TABLE_TOPIC,
                    null,
                    KEY_TOPIC_USER_ID + "=?",
                    new String[]{String.valueOf(userID)},
                    null,
                    null,
                    null);
            if (cursor != null){
                if(cursor.moveToFirst()){
                    do {
                        Topic topic = new Topic();
                        topic.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                        topic.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TOPIC_TITLE)));
                        topic.setDescription(cursor.getString(cursor.getColumnIndex(KEY_TOPIC_DESCRIPTION)));
                        topicList.add(topic);

                    }
                    while (cursor.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            cursor.close();
        }
        return topicList;
    }

    @SuppressLint("Range")
    public List<Task> getTaskByTopicID(int topicID){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        db = this.getWritableDatabase();
        Cursor cursor = null;
        List<Task> taskList = new ArrayList<>();

        db.beginTransaction();
        try{
            cursor = db.query(TABLE_TASK,
                    null,
                    KEY_TASK_TOPIC_ID + "=?",
                    new String[]{String.valueOf(topicID)},
                    null,
                    null,
                    null);
            if (cursor != null){
                if(cursor.moveToFirst()){
                    do {
                        Task task = new Task();
                        task.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                        task.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TASK_TITLE)));
                        task.setStatus(cursor.getInt(cursor.getColumnIndex(KEY_TASK_STATUS)));
                        Date date = new Date();
                        task.setDeadline(dateFormat.parse(cursor.getString(cursor.getColumnIndex(KEY_TASK_DEADLINE))));
                        taskList.add(task);

                    }
                    while (cursor.moveToNext());
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            cursor.close();
        }
        return taskList;
    }
}
