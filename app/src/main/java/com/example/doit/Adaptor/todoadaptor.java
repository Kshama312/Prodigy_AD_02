package com.example.doit.Adaptor;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doit.MainActivity;
import com.example.doit.Model.todomodel;
import com.example.doit.R;
import com.example.doit.addnewtask;
import com.example.doit.utils.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;
public class todoadaptor extends RecyclerView.Adapter<todoadaptor.ViewHolder> {
    private  List<todomodel> todoList;
    private MainActivity activity;
    private DatabaseHandler db;
    public todoadaptor(DatabaseHandler db, MainActivity activity){
        this.db=db;
        this.activity=activity;
        this.todoList=new ArrayList<>();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tasklayout, parent, false);
        return new ViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        db.openDatabase();

        final todomodel item= todoList.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    db.updateStatus(item.getId(),1);
                }
                else {
                    db.updateStatus(item.getId(),0);
                }

            }
        });
    }
   private boolean toBoolean(int n){
    return  n!=0;
    }

    @Override
    public int getItemCount(){
        return  todoList.size();}
    public Context getContext(){
        return activity;
    }
    public void setTask(List<todomodel> todoList){
        this.todoList.clear(); // Clear existing data
        this.todoList.addAll(todoList);
        this.todoList= todoList;
        notifyDataSetChanged();

    }
    public void deleteItem(int position){
        todomodel item= todoList.get(position);
        db.deleteTask((item.getId()));
        todoList.remove(position);
        notifyItemRemoved(position);
    }


    public void  editItem(int position){
        todomodel item = todoList.get(position);
        Bundle bundle= new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        addnewtask fragment = new addnewtask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), addnewtask.TAG);

    }
        public  static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task;
        ViewHolder(View view){
            super(view);
            task= view.findViewById(R.id.todocheckbox);
        }
    }

}
