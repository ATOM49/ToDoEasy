package com.world.atom.todoeasy;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.world.atom.todoeasy.models.Task;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements
        TaskRecyclerAdapter.TaskInteractionCallback, View.OnClickListener,
        TaskTypeAdapter.TaskTypeInteractionCallback {

    private TaskRecyclerAdapter mActiveTaskListAdapter, mDoneTaskListAdapter;
    private TextInputEditText mNewTaskEditText;
    private TextInputLayout mNewTaskTextLayout;
    private static final String[] taskTypes = {"Personal", "Work", "Top Secret"};
    private View taskTypeLayout;
    private TodoDatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initialize database
        db = new TodoDatabaseHandler(this);

        //Add Button
        final ImageButton addTaskButton = findViewById(R.id.addTaskButton);
        addTaskButton.setEnabled(false);
        addTaskButton.setOnClickListener(this);

        //Type Selector
        taskTypeLayout = findViewById(R.id.taskTypeLayout);
        RecyclerView taskTypeRecycler = findViewById(R.id.taskTypeRecycler);
        taskTypeRecycler.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        taskTypeRecycler.setAdapter(new TaskTypeAdapter(taskTypes, this));

        //Text entry
        mNewTaskTextLayout = findViewById(R.id.newTaskTextLayout);
        mNewTaskEditText = findViewById(R.id.newTaskEditText);
        mNewTaskEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    addTaskButton.setEnabled(true);
                } else {
                    addTaskButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //Recycler views
        RecyclerView activeTasksRecycler = findViewById(R.id.activeTasksRecycler);
        activeTasksRecycler.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView doneTasksRecycler = findViewById(R.id.doneTasksRecycler);
        doneTasksRecycler.setLayoutManager(new LinearLayoutManager(this));
        mActiveTaskListAdapter = new TaskRecyclerAdapter(this);
        activeTasksRecycler.setAdapter(mActiveTaskListAdapter);
        mDoneTaskListAdapter = new TaskRecyclerAdapter(this);
        doneTasksRecycler.setAdapter(mDoneTaskListAdapter);


        //get data from database
        ArrayList<Task> doneTasks = db.getTasksBasedOnStatus(true);
        if (doneTasks.size() > 0) {
            mDoneTaskListAdapter.addTasks(doneTasks);
        }
        ArrayList<Task> activeTasks = db.getTasksBasedOnStatus(false);
        if (activeTasks.size() > 0) {
            mActiveTaskListAdapter.addTasks(activeTasks);
        }
    }

    @Override
    public void taskTypeSelected(String taskType) {
        addTaskToAdapter(taskType);

    }

    private void addTaskToAdapter(String type) {
        Task newTask = new Task(new Random().nextInt(),
                mNewTaskEditText.getText().toString(), false, type);
        mActiveTaskListAdapter.addTask(newTask);
        mNewTaskEditText.setText("");
        taskTypeLayout.setVisibility(View.GONE);
        db.addTask(newTask);
    }

    @Override
    public void taskStatusUpdated(Task task) {
        if (task.isDone()) {
            mDoneTaskListAdapter.removeTask(task);
            mActiveTaskListAdapter.addTask(task.flip());
        } else {
            mActiveTaskListAdapter.removeTask(task);
            mDoneTaskListAdapter.addTask(task.flip());
        }
        db.updateTask(task.flip());
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.addTaskButton) {
            if (mNewTaskEditText.getText().toString().length() > 0) {
                taskTypeLayout.setVisibility(View.VISIBLE);
            } else {
                mNewTaskTextLayout.setError("God damn it, Morty!");
            }
        }
    }


}
