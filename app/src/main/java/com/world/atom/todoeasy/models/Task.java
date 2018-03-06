package com.world.atom.todoeasy.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Task implements Parcelable {
    int taskId = 0;
    String taskText = "";
    boolean isDone = false;
    String taskType = "";

    protected Task(Parcel in) {
        taskText = in.readString();
        isDone = in.readByte() != 0;
        taskType = in.readString();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public Task(int taskId, String taskText, boolean b, String taskType) {
        setTaskId(taskId);
        setTaskText(taskText);
        setDone(b);
        setTaskType(taskType);
    }

    public Task() {

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(taskText);
        parcel.writeByte((byte) (isDone ? 1 : 0));
        parcel.writeString(taskType);
    }


    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskText() {
        return taskText;
    }

    public void setTaskText(String taskText) {
        this.taskText = taskText;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public Task flip() {
        return new Task(getTaskId(), getTaskText(), !isDone(), getTaskType());
    }
}
