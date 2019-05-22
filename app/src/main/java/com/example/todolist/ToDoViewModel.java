package com.example.todolist;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ToDoViewModel extends AndroidViewModel{

    private ListRepository lr;
    private Context mContext;
    private int curId;
    private MutableLiveData<Integer> mCode;
    private LiveData<List<Task>> mObservableCurrentTasks;
    private Task mTask;
    private List<Task> mNotobservableCurrentTasks;

    public static final int CODE_ALLTASKS=-2;
    public static final int CODE_COMPLETEDTASKS=-3;
    public static final int CODE_NOTCOMPLETEDTASKS=-4;
    public static final int CODE_GREATTASKS=-5;
    public static final int CODE_NOTGREATTASKS=-6;

    public ToDoViewModel(@NonNull final Application application) {
        super(application);
        Log.d("StartInit","ViewModel");
        new Thread(new Runnable() {
            @Override
            public void run() {
                ToDoListDatabase tdld = ToDoListDatabase.getDatabase(application);
                lr = new ListRepository(tdld);
                mContext = application.getApplicationContext();
                mObservableCurrentTasks = lr.receiveObservableRootTasks();
                mNotobservableCurrentTasks = lr.receiveRootTasks();
                mCode = new MutableLiveData<>();
            }
        }).run();
        mCode.postValue(-1);//Сначала подгрузятся все данные,а уже потом вызовется observer в main'е
        Log.d("EndInit","ViewModel");
    }

    void changeTask(final Task task){
        Log.d("StartChange","ViewModel");
        new Thread(new Runnable() {
            @Override
            public void run() {
                mTask = task;
                mObservableCurrentTasks = lr.receiveObservableChildTasks(task);
                mNotobservableCurrentTasks = lr.receiveChildTasks(task);
            }
        }).run();
        mCode.setValue(0);
    }

    void getFilteredTasks(final int MODE){
        Log.d("StartChange","ViewModel");
        final int id = (mTask==null) ? -1 : mTask.getId();
        new Thread(new Runnable() {
            @Override
            public void run() {
                switch(MODE) {
                    case CODE_ALLTASKS:
                        if (mTask == null) {
                            mObservableCurrentTasks = lr.receiveObservableRootTasks();
                            mNotobservableCurrentTasks = lr.receiveRootTasks();
                        }
                        else {
                            mObservableCurrentTasks = lr.receiveObservableChildTasks(mTask);
                            mNotobservableCurrentTasks = lr.receiveChildTasks(mTask);
                        }
                        break;
                    case CODE_COMPLETEDTASKS:
                        mObservableCurrentTasks = lr.receiveObservableStatusTasks(true, id);
                        mNotobservableCurrentTasks = lr.receiveStatusTasks(true,id);
                        break;
                    case CODE_NOTCOMPLETEDTASKS:
                        mObservableCurrentTasks = lr.receiveObservableStatusTasks(false, id);
                        mNotobservableCurrentTasks = lr.receiveStatusTasks(false,id);
                        break;
                    case CODE_GREATTASKS:
                        mObservableCurrentTasks = lr.receiveObservableMarkTasks(true, id);
                        mNotobservableCurrentTasks = lr.receiveMarkTasks(true,id);
                        break;
                    case CODE_NOTGREATTASKS:
                        mObservableCurrentTasks = lr.receiveObservableMarkTasks(false, id);
                        mNotobservableCurrentTasks = lr.receiveMarkTasks(false,id);
                        break;
                }
            }
        }).run();
        Log.d("CurrentMode",Integer.toString(MODE));
        if(id==-1)
            mCode.setValue(id);
        else
            mCode.setValue(0);
    }

    void goBack(final Task task){
        Log.d("StartChange","ViewModel");
        Log.d("ReceivedId",Integer.toString(task.getId()));
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(task.getParentId()==-1) {
                    Log.d("ViewModel","0 case scenario");
                    mTask = null;
                    mObservableCurrentTasks = lr.receiveObservableRootTasks();
                    mNotobservableCurrentTasks = lr.receiveRootTasks();
                    mCode.setValue(-1);
                }else if(task.getParentId()>-1){
                    Log.d("ViewModel","-1 case scenario");
                    mTask = lr.receiveTask(task.getParentId());
                    Log.d("Task:",mTask.getName());
                    mObservableCurrentTasks = lr.receiveObservableChildTasks(mTask);
                    mNotobservableCurrentTasks = lr.receiveChildTasks(mTask);
                    mCode.setValue(0);
                }
            }
        }).run();
    }


    LiveData<Integer> getLiveCode(){
        return mCode;
    }

    Task getCurrentTask(){
        return mTask;
    }

    LiveData<List<Task>> getObservableCurrentTasks(){
        return mObservableCurrentTasks;
    }

    List<Task> getNotobservableCurrentTasks(){
        return mNotobservableCurrentTasks;
    }

    public void insertTask(Task parentTask,Task task){
        int curId = getCurId();
        ArrayList<Integer> subTaskPath = new ArrayList<>();
        subTaskPath.addAll(parentTask.getPath());
        subTaskPath.add(curId);
        Log.d("CurrentRootPath:",subTaskPath.toString());
        task.setPath(subTaskPath);
        task.setParentId(parentTask.getId());
        task.setId(curId);
        Log.d("TaskPathBeforeAdding:",task.getPath().toString());
        lr.insertTask(task);
    }


    public void insertRootTask(Task task){
        int curId = getCurId();
        ArrayList<Integer> TaskPath = new ArrayList<>();
        TaskPath.add(curId);
        task.setPath(TaskPath);
        task.setId(curId);
        Log.d("CurrentRootID:",task.getId().toString());
        task.setParentId(-1);
        Log.d("CurrentRootPath:",task.getPath().toString());
        lr.insertTask(task);
        Log.d("CurrentTaskPath",lr.receiveTask(task.getId()).getPath().toString());
    }

    public void deleteTask(Task task){
        lr.deleteTask(task);
    }

    public void changeIsDone(final Task task){
        task.setIsDone(!task.getIsDone());
        new Thread(new Runnable() {
            @Override
            public void run() {
                lr.updateTask(task);
            }
        }).run();
    }

    public void updateTask(Task task){
        lr.updateTask(task);
    }

    private int getCurId(){
        SharedPreferences sharedPreferencesCurId = sharedPreferencesCurId = PreferenceManager.getDefaultSharedPreferences(mContext);
        if(sharedPreferencesCurId.getInt("CUR_ID", -1)==-1){
            final SharedPreferences.Editor editor = sharedPreferencesCurId.edit();
            editor.putInt("CUR_ID",0);
            editor.apply();
        }
        curId = sharedPreferencesCurId.getInt("CUR_ID", -1);
        incrementCurId();//IMPORTANT ызов этого метода автоматически увеличивает счетчик тасков
        return curId;
    }

    private void incrementCurId(){
        SharedPreferences sharedPreferencesCurId = sharedPreferencesCurId = PreferenceManager.getDefaultSharedPreferences(mContext);
        final SharedPreferences.Editor editor = sharedPreferencesCurId.edit();
        editor.putInt("CUR_ID",sharedPreferencesCurId.getInt("CUR_ID",-2)+1);
        editor.apply();
    }

    @Override
    protected void onCleared(){
        Log.d("ViewModel","Destroyed");
    }


    Task getTask(int id){
        return lr.receiveTask(id);
    }
}