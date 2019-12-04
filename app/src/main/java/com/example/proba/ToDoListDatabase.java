package com.example.proba;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities={Task.class},version = 1)
public abstract class ToDoListDatabase extends RoomDatabase {
    private static volatile ToDoListDatabase toDoListDatabase;

    public abstract ListDAO listDAO();

    static ToDoListDatabase getDatabase(final Context context){
        if(toDoListDatabase==null){
            synchronized (ToDoListDatabase.class){
                if(toDoListDatabase==null){
                    toDoListDatabase = Room.databaseBuilder(context.getApplicationContext(),
                            ToDoListDatabase.class, "todolist_database")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }return toDoListDatabase;
    }
}
