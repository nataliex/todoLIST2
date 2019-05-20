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
    LiveData<List<Task>> mCurrentTasks;
    Task mTask;


    public ToDoViewModel(@NonNull Application application) {
        super(application);
        ToDoListDatabase tdld = ToDoListDatabase.getDatabase(application);
        lr = new ListRepository(tdld);
        context = application.getApplicationContext();
    }

    public ToDoViewModel(@NonNull Application application,Integer curentId) {//TODO ММЕНЯЮ!!!
        super(application);
        Log.d("StartInit","ViewModel");
        ToDoListDatabase tdld = ToDoListDatabase.getDatabase(application);
        lr = new ListRepository(tdld);
        context = application.getApplicationContext();
        //todo стереть switch
        switch (curentId){
            case -1:
                mCurrentTasks = lr.receiveObservableRootTasks();
                break;
            case -2:
                mCurrentTasks = lr.receiveObservableStatusTasks(true, -1);
                break;
            case -3:
                mCurrentTasks = lr.receiveObservableStatusTasks(false, -1);
                break;
            case -4:
                mCurrentTasks = lr.receiveObservableMarkTasks(true, -1);
                break;
            case -5:
                mCurrentTasks = lr.receiveObservableMarkTasks(false, -1);
                break;
            default:
                mCurrentTasks = lr.receiveObservableChildTasks(curentId);
                mTask = lr.receiveTask(curentId);
                break;
        }
       /* if(curentId==-1) {
            mCurrentTasks = lr.receiveObservableRootTasks();//todo раскоментить
        }else {
            mCurrentTasks = lr.receiveObservableChildTasks(curentId);
            mTask = lr.receiveTask(curentId);
        }*/
        Log.d("EndInit","ViewModel");
    }

    Task getCurrentTask(){
        return mTask;
    }

    Task getTask(int id){
        return lr.receiveTask(id);
    }

    List<Task> getChildTasks(Task task){
        return lr.receiveChildTasks(task);
    }

    public void insertTask(Task parentTask,Task task){
        int curId = getCurId();
        ArrayList<Integer> subTaskPath = new ArrayList<Integer>(parentTask.getPath());
        Log.d("CurrentRootPath:",subTaskPath.toString());
        subTaskPath.add(curId);
        task.setPath(subTaskPath);
        task.setParentId(parentTask.getId());
        task.setId(curId);
        Log.d("CurrentTaskPathWithId:",task.getPath().toString()+"   "+task.getId().toString());
        lr.insertTask(task);
    }

    public void insertRootTask(Task task){
        int curId = getCurId();
        ArrayList<Integer> TaskPath = new ArrayList<Integer>();
        TaskPath.add(curId);
        task.setId(curId);
        Log.d("CurrentRootID:",task.getId().toString());
        task.setPath(TaskPath);
        task.setParentId(-1);
        Log.d("CurrentRootPath:",task.getPath().toString());
        lr.insertTask(task);
    }

    public LiveData<List<Task>> getCurrentTasks(){
        return mCurrentTasks;
    }

    public void deleteTask(Task task){
        lr.deleteTask(task);
    }

    public void updateTask(Task task){
        lr.updateTask(task);
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