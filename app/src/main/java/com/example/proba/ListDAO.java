package com.example.proba;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
interface ListDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(Task task);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTasks(Task... tasks);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTasks(ArrayList<Task> tasks);

    @Delete
    void delete(Task task);

    @Query("DELETE FROM TaskTable WHERE path LIKE :partialQuerry")
    void delete(String partialQuerry);

    @Query("SELECT * from TaskTable WHERE Id LIKE :Id")
    List<Task> getTask(Integer Id);

    @Query("SELECT * from TaskTable ORDER BY Name ASC")
    List<Task> getTasks();

    @Query("SELECT * from TaskTable WHERE ParentId = :mParentId")
    List<Task> getChildTasks(Integer mParentId);

    @Query("SELECT * from TaskTable WHERE Id LIKE :Id")
    LiveData<List<Task>> getObservableTask(Integer Id);

    @Query("SELECT * from TaskTable ORDER BY Name ASC")
    LiveData<List<Task>> getObservableTasks();

    @Query("SELECT * from TaskTable WHERE ParentId = :mParentId")
    LiveData<List<Task>> getObservableChildTasks(Integer mParentId);

    @Query("SELECT * from TaskTable WHERE IsDone LIKE :status " +
            "AND ParentId LIKE :mParentId ORDER BY Name ASC")
    List<Task> getStatusTasks(Boolean status, Integer mParentId);

    @Query("SELECT * from TaskTable WHERE isDone LIKE :status " +
            "AND ParentId LIKE :mParentId ORDER BY Name ASC")
    LiveData<List<Task>> getObservableStatusTasks(Boolean status, Integer mParentId);

    @Query("SELECT * from TaskTable WHERE starMark LIKE :mStarMark " +
            "AND ParentId LIKE :mParentId ORDER BY Name ASC")
    List<Task> getMarkTasks(Boolean mStarMark, Integer mParentId);

    @Query("SELECT * from TaskTable WHERE starMark LIKE :mStarMark " +
            "AND ParentId LIKE :mParentId ORDER BY Name ASC")
    LiveData<List<Task>> getObservableMarkTasks(Boolean mStarMark, Integer mParentId);

    @Query("SELECT * from TaskTable WHERE ParentId = -1")
    List<Task> getRootTasks();

    @Query("SELECT * from TaskTable WHERE ParentId = -1")
    LiveData<List<Task>> getObservableRootTasks();

    @Update
    void update(Task task);
}
