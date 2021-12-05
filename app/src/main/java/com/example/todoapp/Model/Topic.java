package com.example.todoapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Topic implements Parcelable {

    public static final String EXTRA_TOPIC = "EXTRA_TOPIC";

    private int id;
    private String title;
    private String description;
    private int userID;

    public Topic(){

    }

    protected Topic(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        userID = in.readInt();
    }

    public static final Creator<Topic> CREATOR = new Creator<Topic>() {
        @Override
        public Topic createFromParcel(Parcel in) {
            return new Topic(in);
        }

        @Override
        public Topic[] newArray(int size) {
            return new Topic[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeInt(userID);
    }
}
