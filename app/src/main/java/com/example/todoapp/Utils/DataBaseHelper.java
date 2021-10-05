package com.example.todoapp.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.Model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;
    private static final String DATABASE_NAME = "TODO";

    private static final String TABLE_TODO = "TODO_TABLE";
    private static final String TABLE_USER = "USER_TABLE";

    private static final String KEY_ID = "ID";
    private static final String KEY_CREATED_AT = "CREATED_AT";

    private static final String KEY_TODO_TASK = "TASK";
    private static final String KEY_TODO_STATUS = "STATUS";
    private static final String KEY_TODO_USER_ID = "USER_ID";

    private static final String KEY_USER_USERNAME = "USERNAME";
    private static final String KEY_USER_PASSWORD = "PASSWORD";
    private static final String KEY_USER_FULLNAME = "FULLNAME";

    private static final String CREATE_TABLE_TODO = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TODO + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_TODO_TASK+" TEXT,"
            + KEY_TODO_STATUS+" INTEGER,"
            + KEY_CREATED_AT+" DATETIME,"
            + KEY_TODO_USER_ID + " INTEGER,"
            + "FOREIGN KEY ("+KEY_TODO_USER_ID+") REFERENCES "+TABLE_USER+"("+KEY_ID+"));";

    private static final String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS "
            + TABLE_USER + "(" +KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_USER_USERNAME+" TEXT,"
            + KEY_USER_PASSWORD+" TEXT,"
            + KEY_USER_FULLNAME+" TEXT,"
            + KEY_CREATED_AT+" DATETIME)";

    private static final String DROP_TABLE_USER = "DROP TABLE IF EXISTS " + TABLE_USER;
    private static final String DROP_TABLE_TODO = "DROP TABLE IF EXISTS " + TABLE_TODO;

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TODO);
        db.execSQL(CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(DROP_TABLE_USER);
        db.execSQL(DROP_TABLE_TODO);
        onCreate(db);
    }

    public void insertTask(ToDoModel model){
        db = this.getWritableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        ContentValues values = new ContentValues();
        values.put(KEY_TODO_TASK, model.getTask());
        values.put(KEY_TODO_STATUS, 0);
        values.put(KEY_CREATED_AT, dateFormat.format(date));
        values.put(KEY_TODO_USER_ID, model.getUserID() );
        db.insert(TABLE_TODO, null, values);
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

    public void updateTask(int id, String task){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TODO_TASK, task);
        db.update(TABLE_TODO, values, "ID=?", new String[]{String.valueOf(id)});
    }
//
    public void updateStatus(int id, int status){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TODO_STATUS, status);
        db.update(TABLE_TODO, values, "ID=?", new String[]{String.valueOf(id)});
    }
//
    public void deleteTask(int id){
        db = this.getWritableDatabase();
        db.delete(TABLE_TODO, "ID=?", new String[]{String.valueOf(id)});
        db.close();
    }
//
    public User Authenticate(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER,// Selecting Table
                new String[]{KEY_ID, KEY_USER_USERNAME, KEY_USER_PASSWORD, KEY_USER_FULLNAME},//Selecting columns want to query
                KEY_USER_USERNAME + "=?",
                new String[]{user.getUsername()},//Where clause
                null, null, null);

        if (cursor != null && cursor.moveToFirst()&& cursor.getCount()>0) {
            //if cursor has value then in user database there is user associated with this given email
            User newUser = new User();
            newUser.setId(cursor.getInt(0));
            newUser.setUsername(cursor.getString(1));
            newUser.setPassword(cursor.getString(2));
            newUser.setFullname(cursor.getString(3));
            //Match both passwords check they are same or not
            if (user.getPassword().equalsIgnoreCase(newUser.getPassword())) {
                return newUser;
            }
        }

        //if user password does not matches or there is no record with that email then return @false
        return null;
    }
    @SuppressLint("Range")
    public List<ToDoModel> getTaskByUserID(int userID){
        db = this.getWritableDatabase();
        Cursor cursor = null;
        List<ToDoModel> modelList = new ArrayList<>();

        db.beginTransaction();
        try{

            cursor = db.query(TABLE_TODO,
                    null,
                    KEY_TODO_USER_ID + "=?",
                    new String[]{String.valueOf(userID)},
                    null,
                    null,
                    null);
            if (cursor != null){
                if(cursor.moveToFirst()){
                    do {
                        ToDoModel task = new ToDoModel();
                        task.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                        task.setTask(cursor.getString(cursor.getColumnIndex(KEY_TODO_TASK)));
                        task.setStatus(cursor.getInt(cursor.getColumnIndex(KEY_TODO_STATUS)));
                        modelList.add(task);

                    }
                    while (cursor.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            cursor.close();
        }
        int i=0;
        for (ToDoModel toDoModel : modelList){
            System.out.println("I: "+i);
            i++;
        }
        return modelList;
    }
}
