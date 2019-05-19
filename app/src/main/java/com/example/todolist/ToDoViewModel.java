package com.example.todolist;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ToDoViewModel extends AndroidViewModel {

    ListRepository lr;
    SharedPreferences sp;
    Context context;
    int curId;
    LiveData<List<Task>> curentTasks;


    public ToDoViewModel(@NonNull Application application) {
        super(application);
        ToDoListDatabase tdld = ToDoListDatabase.getDatabase(application);
        lr = new ListRepository(tdld);
        context = application.getApplicationContext();
        curentTasks = lr.receiveObservableRootTasks();
    }

    public ToDoViewModel(@NonNull Application application,Integer curentId) {
        super(application);
        ToDoListDatabase tdld = ToDoListDatabase.getDatabase(application);
        lr = new ListRepository(tdld);
        context = application.getApplicationContext();
        if(curentId==-1) {
            curentTasks = lr.receiveObservableChildTasks(curentId);
        }else curentTasks = lr.receiveObservableRootTasks();
    }

    //Добавлен метод для работы с Id,чтобы лишний раз не обращаться к БД
    LiveData<List<Task>> getObservableTasks(Integer taskId){
        return curentTasks;
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
        subTaskPath.add(getCurId());
        task.setPath(subTaskPath);
        task.setParentId(parentTask.getId());
        task.setId(getCurId());
        Log.d("CurrentTaskPathWithId:",task.getPath().toString()+"   "+task.getId().toString());
        lr.insertTask(task);
    }

    public void insertRootTask(Task task){
        ArrayList<Integer> TaskPath = new ArrayList<Integer>();
        TaskPath.add(getCurId());
        task.setId(getCurId());
        Log.d("CurrentRootID:",task.getId().toString());
        task.setPath(TaskPath);
        task.setParentId(-1);
        Log.d("CurrentRootPath:",task.getPath().toString());
        lr.insertTask(task);
    }

    public List<Task> getRootTasks(){
        return lr.receiveRootTasks();
    }

    public LiveData<List<Task>> getCurrentTasks(){
        return lr.receiveObservableRootTasks();
    }

    public void changeIsDone(Task task){
        task.setIsDone(!task.getIsDone());
        lr.updateTask(task);
    }

    //Добавлено:getCurId
    private int getCurId(){
        SharedPreferences sharedPreferencesCurId = sharedPreferencesCurId = PreferenceManager.getDefaultSharedPreferences(context);
        if(sharedPreferencesCurId.getInt("CUR_ID", -1)==-1){
            final SharedPreferences.Editor editor = sharedPreferencesCurId.edit();
            editor.putInt("CUR_ID",0);
            editor.apply();
        }
        curId = sharedPreferencesCurId.getInt("CUR_ID", -1);
        incrementCurId();//TODO вызов этого метода автоматически увеличивает счетчик тасков
        return curId;
    }

    private void incrementCurId(){
        SharedPreferences sharedPreferencesCurId = sharedPreferencesCurId = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = sharedPreferencesCurId.edit();
        editor.putInt("CUR_ID",sharedPreferencesCurId.getInt("CUR_ID",-2)+1);
        editor.apply();
    }
}