package com.example.doit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.doit.Adaptor.todoadaptor;
import com.example.doit.Model.todomodel;
import com.example.doit.utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {
    private RecyclerView TasksRecyclerView;
    private todoadaptor taskadaptor;
    public FloatingActionButton fab;

    private List<todomodel> taskList;
    public DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHandler(this);
        db.openDatabase();

        TasksRecyclerView = findViewById(R.id.TasksRecyclerView);
        TasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskadaptor = new todoadaptor(db, MainActivity.this);
        TasksRecyclerView.setAdapter(taskadaptor);


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(taskadaptor));
        itemTouchHelper.attachToRecyclerView(TasksRecyclerView);

        fab = findViewById(R.id.fab);

        taskList = db.getAllTasks();

        Collections.reverse(taskList);
        taskadaptor.setTask(taskList);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addnewtask.newInstance().show(getSupportFragmentManager(), addnewtask.TAG);
            }

        });
    }
    @Override
    public void handleDialogClose(DialogInterface dialog){
        taskList= db.getAllTasks();
        Collections.reverse(taskList);
        taskadaptor.setTask(taskList);
        taskadaptor.notifyDataSetChanged();
    }
}