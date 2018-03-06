package com.world.atom.todoeasy;


import android.graphics.Paint;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.world.atom.todoeasy.models.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final TaskInteractionCallback mTaskInteractionCallback;
    private ArrayList<Task> mTasks = new ArrayList<>();

    public TaskRecyclerAdapter(TaskInteractionCallback callback) {
        mTaskInteractionCallback = callback;
    }

    public void addTask(Task task) {
        mTasks.add(task);
        notifyDataSetChanged();
    }

    public void removeTask(Task task) {
        mTasks.remove(task);
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TaskViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.row_task_item,
                        parent,
                        false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Task task = mTasks.get(position);
        TaskViewHolder taskViewHolder = (TaskViewHolder) holder;
        taskViewHolder.bind(task);
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    public void addTasks(ArrayList<Task> doneTasks) {
        mTasks = doneTasks;
        notifyDataSetChanged();
    }


    interface TaskInteractionCallback {
        void taskStatusUpdated(Task task);
    }

    private class TaskViewHolder extends RecyclerView.ViewHolder implements
            CompoundButton.OnCheckedChangeListener {
        private boolean onBind;
        AppCompatCheckBox taskText;
        TextView taskType;

        public TaskViewHolder(View view) {
            super(view);
            taskText = view.findViewById(R.id.taskText);
            taskType = view.findViewById(R.id.taskType);
            taskText.setOnCheckedChangeListener(this);

        }

        public void bind(final Task task) {
            onBind = true;
            this.taskText.setText(task.getTaskText());
            if (task.isDone()) {
                this.taskText.setPaintFlags(
                        this.taskText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                this.taskText.setChecked(true);
            } else {
                this.taskText.setPaintFlags(
                        this.taskText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                this.taskText.setChecked(false);
            }
            this.taskType.setText(task.getTaskType());
            onBind = false;
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (!onBind) {
                mTaskInteractionCallback.taskStatusUpdated(mTasks.get(getLayoutPosition()));
                notifyDataSetChanged();
            }
        }
    }
}
