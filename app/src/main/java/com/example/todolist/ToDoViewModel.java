package com.example.todolist;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ToDoViewModel extends AndroidViewModel {

    ListRepository lr;
    SharedPreferences sp;
    int curId;


    public ToDoViewModel(@NonNull Application application) {
        super(application);
        ToDoListDatabase tdld = ToDoListDatabase.getDatabase(application);
        lr = new ListRepository(tdld);
    }

    LiveData<List<Task>> getObservableChildTasks(Task task){
        return lr.receiveObservableChildTasks(task);
    }

    Task getTask(int id){
        return lr.receiveTask(id);
    }
    List<Task> getChildTasks(Task task){
        return lr.receiveChildTasks(task);
    }

    public void insertTask(Task parentTask,Task task){
        ArrayList<Integer> subTaskPath = new ArrayList<Integer>(parentTask.getPath());
        Log.d("CurrentRootPath:",subTaskPath.toString());
        subTaskPath.add(task.getId());
        task.setPath(subTaskPath);
        task.setParentId(parentTask.getId());
        Log.d("CurrentTaskPathWithId:",task.getPath().toString()+"   "+task.getId().toString());
        lr.insertTask(task);
    }

    public void insertRootTask(Task task){
        ArrayList<Integer> TaskPath = new ArrayList<Integer>();
        TaskPath.add(task.getId());
        Log.d("CurrentRootID:",task.getId().toString());
        task.setPath(TaskPath);
        task.setParentId(-1);
        Log.d("CurrentRootPath:",task.getPath().toString());
        lr.insertTask(task);
    }

    public void deleteRootTask(int Id){
        lr.deleteTask(getTask(Id));
    }

    public List<Task> getRootTasks(){
        return lr.receiveRootTasks();
    }

    public LiveData<List<Task>> getObservableRootTasks(){
        return lr.receiveObservableRootTasks();
    }

    public void changeIsDone(Task task){
        task.setIsDone(!task.getIsDone());
        lr.updateTask(task);
    }
}