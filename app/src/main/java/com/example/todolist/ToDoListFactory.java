package com.example.todolist;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;


public class ToDoListFactory extends ViewModelProvider.NewInstanceFactory {
    int mTaskId;
    Application mApplication;

    ToDoListFactory(Application application,int taskId){
        mTaskId = taskId;
        mApplication = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == ToDoViewModel.class) {
            return (T) new ToDoViewModel(mApplication, mTaskId);
        }
        return null;
    }

}
