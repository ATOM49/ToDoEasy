package com.world.atom.todoeasy;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class TaskTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String[] taskTypes;
    private TaskTypeInteractionCallback mTaskTypeInteractionCallback;

    public TaskTypeAdapter(String[] taskTypes,
            TaskTypeInteractionCallback taskTypeInteractionCallback) {
        this.taskTypes = taskTypes;
        mTaskTypeInteractionCallback = taskTypeInteractionCallback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TaskTypeViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.row_task_type_item,
                        parent,
                        false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String taskType = taskTypes[position];
        TaskTypeViewHolder taskViewHolder = (TaskTypeViewHolder) holder;
        taskViewHolder.bind(taskType);
    }

    @Override
    public int getItemCount() {
        return taskTypes.length;
    }


    interface TaskTypeInteractionCallback {
        void taskTypeSelected(String taskType);
    }

    private class TaskTypeViewHolder extends RecyclerView.ViewHolder {
        TextView taskTypeType;

        public TaskTypeViewHolder(View view) {
            super(view);
            taskTypeType = view.findViewById(R.id.taskTypeText);
            taskTypeType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mTaskTypeInteractionCallback.taskTypeSelected(taskTypes[getLayoutPosition()]);
                }
            });
        }

        public void bind(final String taskType) {
            this.taskTypeType.setText(taskType);
        }

    }
}
