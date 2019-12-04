package com.example.proba;

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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ToDoViewModel extends AndroidViewModel {

    ListRepository lr;
    Context mContext;
    int curId;
    MutableLiveData<Integer> mCode;
    LiveData<List<Task>> mObservableCurrentTasks;
    Task mTask;
    List<Task> mNotobservableCurrentTasks;
    ExecutorService mExecutor;
    public static final int CODE_ALLTASKS = -2;
    public static final int CODE_COMPLETEDTASKS = -3;
    public static final int CODE_NOTCOMPLETEDTASKS = -4;
    public static final int CODE_GREATTASKS = -5;
    public static final int CODE_NOTGREATTASKS = -6;

    public ToDoViewModel(@NonNull final Application application) {
        super(application);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ToDoListDatabase tdld = ToDoListDatabase.getDatabase(application);
                lr = new ListRepository(tdld);
                mContext = application.getApplicationContext();
                mObservableCurrentTasks = lr.receiveObservableRootTasks();
                mNotobservableCurrentTasks = lr.receiveRootTasks();
                mCode = new MutableLiveData<Integer>();
                mExecutor = Executors.newSingleThreadExecutor();
            }
        }).run();
        mCode.postValue(-1);//Сначала подгрузятся все данные,а уже потом вызовется observer в main'е
    }

    void changeTask(final Task task) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mTask = task;
                mObservableCurrentTasks = lr.receiveObservableChildTasks(task);
                mNotobservableCurrentTasks = lr.receiveChildTasks(task);
            }
        });
        mCode.setValue(0);
    }

    void getFilteredTasks(final int MODE) {
        Log.d("StartChange", "ViewModel");
        final int id = (mTask == null) ? -1 : mTask.getId();
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                switch (MODE) {
                    case CODE_ALLTASKS:
                        if (mTask == null) {
                            mObservableCurrentTasks = lr.receiveObservableRootTasks();
                            mNotobservableCurrentTasks = lr.receiveRootTasks();
                        } else {
                            mObservableCurrentTasks = lr.receiveObservableChildTasks(mTask);
                            mNotobservableCurrentTasks = lr.receiveChildTasks(mTask);
                        }
                        break;
                    case CODE_COMPLETEDTASKS:
                        mObservableCurrentTasks = lr.receiveObservableStatusTasks(true, id);
                        mNotobservableCurrentTasks = lr.receiveStatusTasks(true, id);
                        break;
                    case CODE_NOTCOMPLETEDTASKS:
                        mObservableCurrentTasks = lr.receiveObservableStatusTasks(false, id);
                        mNotobservableCurrentTasks = lr.receiveStatusTasks(false, id);
                        break;
                    case CODE_GREATTASKS:
                        mObservableCurrentTasks = lr.receiveObservableMarkTasks(true, id);
                        mNotobservableCurrentTasks = lr.receiveMarkTasks(true, id);
                        break;
                    case CODE_NOTGREATTASKS:
                        mObservableCurrentTasks = lr.receiveObservableMarkTasks(false, id);
                        mNotobservableCurrentTasks = lr.receiveMarkTasks(false, id);
                        break;
                }
            }
        });
        Log.d("CurrentMode", Integer.toString(MODE));
        if (id == -1)
            mCode.setValue(id);
        else
            mCode.setValue(0);
    }

    void goBack(final Task task) {
        final int code = 0;
        if (task.getParentId() == -1) {
            mTask = null;
            mExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    mObservableCurrentTasks = lr.receiveObservableRootTasks();
                    mNotobservableCurrentTasks = lr.receiveRootTasks();
                }
            });
            mCode.setValue(-1);
        } else if (task.getParentId() > -1) {
            mTask = lr.receiveTask(task.getParentId());
            mExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    mObservableCurrentTasks = lr.receiveObservableChildTasks(mTask);
                    mNotobservableCurrentTasks = lr.receiveChildTasks(mTask);
                }
            });
            mCode.setValue(0);
        }
    }


    LiveData<Integer> getLiveCode() {
        return mCode;
    }

    Task getCurrentTask() {
        return mTask;
    }

    LiveData<List<Task>> getObservableCurrentTasks() {
        return mObservableCurrentTasks;
    }

    List<Task> getNotobservableCurrentTasks() {
        return mNotobservableCurrentTasks;
    }

    public void insertTask(Task parentTask, Task task) {
        int curId = getCurId();
        ArrayList<Integer> subTaskPath = new ArrayList<Integer>();
        subTaskPath.addAll(parentTask.getPath());
        subTaskPath.add(curId);
        task.setPath(subTaskPath);
        task.setParentId(parentTask.getId());
        task.setId(curId);
        lr.insertTask(task);
    }


    public void insertRootTask(Task task) {
        int curId = getCurId();
        ArrayList<Integer> TaskPath = new ArrayList<Integer>();
        TaskPath.add(curId);
        task.setPath(TaskPath);
        task.setId(curId);
        task.setParentId(-1);
        lr.insertTask(task);
    }

    public void deleteTask(Task task) {
        lr.deleteTask(task);
        for (Task task123 : lr.receiveAllTasks()) {
        }
    }

    public void changeIsDone(final Task task) {
        task.setIsDone(!task.getIsDone());
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                lr.updateTask(task);
            }
        });
    }

    public void updateTask(Task task) {
        lr.updateTask(task);
    }

    private int getCurId() {
        SharedPreferences sharedPreferencesCurId = sharedPreferencesCurId = PreferenceManager.getDefaultSharedPreferences(mContext);
        if (sharedPreferencesCurId.getInt("CUR_ID", -1) == -1) {
            final SharedPreferences.Editor editor = sharedPreferencesCurId.edit();
            editor.putInt("CUR_ID", 0);
            editor.apply();
        }
        curId = sharedPreferencesCurId.getInt("CUR_ID", -1);
        incrementCurId();//IMPORTANT ызов этого метода автоматически увеличивает счетчик тасков
        return curId;
    }

    private void incrementCurId() {
        SharedPreferences sharedPreferencesCurId = sharedPreferencesCurId = PreferenceManager.getDefaultSharedPreferences(mContext);
        final SharedPreferences.Editor editor = sharedPreferencesCurId.edit();
        editor.putInt("CUR_ID", sharedPreferencesCurId.getInt("CUR_ID", -2) + 1);
        editor.apply();
    }

    Task getTask(int id){
        return lr.receiveTask(id);
    }
}