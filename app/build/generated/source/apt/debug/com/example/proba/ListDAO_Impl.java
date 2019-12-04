package com.example.proba;

import android.arch.lifecycle.ComputableLiveData;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.InvalidationTracker.Observer;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.arch.persistence.room.SharedSQLiteStatement;
import android.database.Cursor;
import android.support.annotation.NonNull;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unchecked")
public class ListDAO_Impl implements ListDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfTask;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfTask;

  private final EntityDeletionOrUpdateAdapter __updateAdapterOfTask;

  private final SharedSQLiteStatement __preparedStmtOfDelete;

  public ListDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTask = new EntityInsertionAdapter<Task>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `TaskTable`(`Id`,`starMark`,`Name`,`Description`,`ParentId`,`isDone`,`Path`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Task value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindLong(1, value.getId());
        }
        final Integer _tmp;
        _tmp = value.getStarMark() == null ? null : (value.getStarMark() ? 1 : 0);
        if (_tmp == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindLong(2, _tmp);
        }
        if (value.getName() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getName());
        }
        if (value.getDescription() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getDescription());
        }
        if (value.getParentId() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindLong(5, value.getParentId());
        }
        final Integer _tmp_1;
        _tmp_1 = value.getIsDone() == null ? null : (value.getIsDone() ? 1 : 0);
        if (_tmp_1 == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindLong(6, _tmp_1);
        }
        final String _tmp_2;
        _tmp_2 = Task.ArrayListConverter.fromArrayListToString(value.getPath());
        if (_tmp_2 == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, _tmp_2);
        }
      }
    };
    this.__deletionAdapterOfTask = new EntityDeletionOrUpdateAdapter<Task>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `TaskTable` WHERE `Id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Task value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindLong(1, value.getId());
        }
      }
    };
    this.__updateAdapterOfTask = new EntityDeletionOrUpdateAdapter<Task>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `TaskTable` SET `Id` = ?,`starMark` = ?,`Name` = ?,`Description` = ?,`ParentId` = ?,`isDone` = ?,`Path` = ? WHERE `Id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Task value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindLong(1, value.getId());
        }
        final Integer _tmp;
        _tmp = value.getStarMark() == null ? null : (value.getStarMark() ? 1 : 0);
        if (_tmp == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindLong(2, _tmp);
        }
        if (value.getName() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getName());
        }
        if (value.getDescription() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getDescription());
        }
        if (value.getParentId() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindLong(5, value.getParentId());
        }
        final Integer _tmp_1;
        _tmp_1 = value.getIsDone() == null ? null : (value.getIsDone() ? 1 : 0);
        if (_tmp_1 == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindLong(6, _tmp_1);
        }
        final String _tmp_2;
        _tmp_2 = Task.ArrayListConverter.fromArrayListToString(value.getPath());
        if (_tmp_2 == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, _tmp_2);
        }
        if (value.getId() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindLong(8, value.getId());
        }
      }
    };
    this.__preparedStmtOfDelete = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM TaskTable WHERE path LIKE ?";
        return _query;
      }
    };
  }

  @Override
  public void insertTask(Task task) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfTask.insert(task);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertTasks(Task... tasks) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfTask.insert(tasks);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertTasks(ArrayList<Task> tasks) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfTask.insert(tasks);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(Task task) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfTask.handle(task);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(Task task) {
    __db.beginTransaction();
    try {
      __updateAdapterOfTask.handle(task);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(String partialQuerry) {
    final SupportSQLiteStatement _stmt = __preparedStmtOfDelete.acquire();
    __db.beginTransaction();
    try {
      int _argIndex = 1;
      if (partialQuerry == null) {
        _stmt.bindNull(_argIndex);
      } else {
        _stmt.bindString(_argIndex, partialQuerry);
      }
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDelete.release(_stmt);
    }
  }

  @Override
  public List<Task> getTask(Integer Id) {
    final String _sql = "SELECT * from TaskTable WHERE Id LIKE ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (Id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, Id);
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfMId = _cursor.getColumnIndexOrThrow("Id");
      final int _cursorIndexOfMStarMark = _cursor.getColumnIndexOrThrow("starMark");
      final int _cursorIndexOfMName = _cursor.getColumnIndexOrThrow("Name");
      final int _cursorIndexOfMDescription = _cursor.getColumnIndexOrThrow("Description");
      final int _cursorIndexOfMParentId = _cursor.getColumnIndexOrThrow("ParentId");
      final int _cursorIndexOfMIsDone = _cursor.getColumnIndexOrThrow("isDone");
      final int _cursorIndexOfMPath = _cursor.getColumnIndexOrThrow("Path");
      final List<Task> _result = new ArrayList<Task>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Task _item;
        final Integer _tmpMId;
        if (_cursor.isNull(_cursorIndexOfMId)) {
          _tmpMId = null;
        } else {
          _tmpMId = _cursor.getInt(_cursorIndexOfMId);
        }
        final Boolean _tmpMStarMark;
        final Integer _tmp;
        if (_cursor.isNull(_cursorIndexOfMStarMark)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getInt(_cursorIndexOfMStarMark);
        }
        _tmpMStarMark = _tmp == null ? null : _tmp != 0;
        final String _tmpMName;
        _tmpMName = _cursor.getString(_cursorIndexOfMName);
        final String _tmpMDescription;
        _tmpMDescription = _cursor.getString(_cursorIndexOfMDescription);
        final Integer _tmpMParentId;
        if (_cursor.isNull(_cursorIndexOfMParentId)) {
          _tmpMParentId = null;
        } else {
          _tmpMParentId = _cursor.getInt(_cursorIndexOfMParentId);
        }
        final Boolean _tmpMIsDone;
        final Integer _tmp_1;
        if (_cursor.isNull(_cursorIndexOfMIsDone)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getInt(_cursorIndexOfMIsDone);
        }
        _tmpMIsDone = _tmp_1 == null ? null : _tmp_1 != 0;
        final ArrayList<Integer> _tmpMPath;
        final String _tmp_2;
        _tmp_2 = _cursor.getString(_cursorIndexOfMPath);
        _tmpMPath = Task.ArrayListConverter.fromStringToArrayList(_tmp_2);
        _item = new Task(_tmpMName,_tmpMParentId,_tmpMIsDone,_tmpMDescription,_tmpMPath,_tmpMId,_tmpMStarMark);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Task> getTasks() {
    final String _sql = "SELECT * from TaskTable ORDER BY Name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfMId = _cursor.getColumnIndexOrThrow("Id");
      final int _cursorIndexOfMStarMark = _cursor.getColumnIndexOrThrow("starMark");
      final int _cursorIndexOfMName = _cursor.getColumnIndexOrThrow("Name");
      final int _cursorIndexOfMDescription = _cursor.getColumnIndexOrThrow("Description");
      final int _cursorIndexOfMParentId = _cursor.getColumnIndexOrThrow("ParentId");
      final int _cursorIndexOfMIsDone = _cursor.getColumnIndexOrThrow("isDone");
      final int _cursorIndexOfMPath = _cursor.getColumnIndexOrThrow("Path");
      final List<Task> _result = new ArrayList<Task>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Task _item;
        final Integer _tmpMId;
        if (_cursor.isNull(_cursorIndexOfMId)) {
          _tmpMId = null;
        } else {
          _tmpMId = _cursor.getInt(_cursorIndexOfMId);
        }
        final Boolean _tmpMStarMark;
        final Integer _tmp;
        if (_cursor.isNull(_cursorIndexOfMStarMark)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getInt(_cursorIndexOfMStarMark);
        }
        _tmpMStarMark = _tmp == null ? null : _tmp != 0;
        final String _tmpMName;
        _tmpMName = _cursor.getString(_cursorIndexOfMName);
        final String _tmpMDescription;
        _tmpMDescription = _cursor.getString(_cursorIndexOfMDescription);
        final Integer _tmpMParentId;
        if (_cursor.isNull(_cursorIndexOfMParentId)) {
          _tmpMParentId = null;
        } else {
          _tmpMParentId = _cursor.getInt(_cursorIndexOfMParentId);
        }
        final Boolean _tmpMIsDone;
        final Integer _tmp_1;
        if (_cursor.isNull(_cursorIndexOfMIsDone)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getInt(_cursorIndexOfMIsDone);
        }
        _tmpMIsDone = _tmp_1 == null ? null : _tmp_1 != 0;
        final ArrayList<Integer> _tmpMPath;
        final String _tmp_2;
        _tmp_2 = _cursor.getString(_cursorIndexOfMPath);
        _tmpMPath = Task.ArrayListConverter.fromStringToArrayList(_tmp_2);
        _item = new Task(_tmpMName,_tmpMParentId,_tmpMIsDone,_tmpMDescription,_tmpMPath,_tmpMId,_tmpMStarMark);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Task> getChildTasks(Integer mParentId) {
    final String _sql = "SELECT * from TaskTable WHERE ParentId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (mParentId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, mParentId);
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfMId = _cursor.getColumnIndexOrThrow("Id");
      final int _cursorIndexOfMStarMark = _cursor.getColumnIndexOrThrow("starMark");
      final int _cursorIndexOfMName = _cursor.getColumnIndexOrThrow("Name");
      final int _cursorIndexOfMDescription = _cursor.getColumnIndexOrThrow("Description");
      final int _cursorIndexOfMParentId = _cursor.getColumnIndexOrThrow("ParentId");
      final int _cursorIndexOfMIsDone = _cursor.getColumnIndexOrThrow("isDone");
      final int _cursorIndexOfMPath = _cursor.getColumnIndexOrThrow("Path");
      final List<Task> _result = new ArrayList<Task>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Task _item;
        final Integer _tmpMId;
        if (_cursor.isNull(_cursorIndexOfMId)) {
          _tmpMId = null;
        } else {
          _tmpMId = _cursor.getInt(_cursorIndexOfMId);
        }
        final Boolean _tmpMStarMark;
        final Integer _tmp;
        if (_cursor.isNull(_cursorIndexOfMStarMark)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getInt(_cursorIndexOfMStarMark);
        }
        _tmpMStarMark = _tmp == null ? null : _tmp != 0;
        final String _tmpMName;
        _tmpMName = _cursor.getString(_cursorIndexOfMName);
        final String _tmpMDescription;
        _tmpMDescription = _cursor.getString(_cursorIndexOfMDescription);
        final Integer _tmpMParentId;
        if (_cursor.isNull(_cursorIndexOfMParentId)) {
          _tmpMParentId = null;
        } else {
          _tmpMParentId = _cursor.getInt(_cursorIndexOfMParentId);
        }
        final Boolean _tmpMIsDone;
        final Integer _tmp_1;
        if (_cursor.isNull(_cursorIndexOfMIsDone)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getInt(_cursorIndexOfMIsDone);
        }
        _tmpMIsDone = _tmp_1 == null ? null : _tmp_1 != 0;
        final ArrayList<Integer> _tmpMPath;
        final String _tmp_2;
        _tmp_2 = _cursor.getString(_cursorIndexOfMPath);
        _tmpMPath = Task.ArrayListConverter.fromStringToArrayList(_tmp_2);
        _item = new Task(_tmpMName,_tmpMParentId,_tmpMIsDone,_tmpMDescription,_tmpMPath,_tmpMId,_tmpMStarMark);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<List<Task>> getObservableTask(Integer Id) {
    final String _sql = "SELECT * from TaskTable WHERE Id LIKE ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (Id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, Id);
    }
    return new ComputableLiveData<List<Task>>() {
      private Observer _observer;

      @Override
      protected List<Task> compute() {
        if (_observer == null) {
          _observer = new Observer("TaskTable") {
            @Override
            public void onInvalidated(@NonNull Set<String> tables) {
              invalidate();
            }
          };
          __db.getInvalidationTracker().addWeakObserver(_observer);
        }
        final Cursor _cursor = __db.query(_statement);
        try {
          final int _cursorIndexOfMId = _cursor.getColumnIndexOrThrow("Id");
          final int _cursorIndexOfMStarMark = _cursor.getColumnIndexOrThrow("starMark");
          final int _cursorIndexOfMName = _cursor.getColumnIndexOrThrow("Name");
          final int _cursorIndexOfMDescription = _cursor.getColumnIndexOrThrow("Description");
          final int _cursorIndexOfMParentId = _cursor.getColumnIndexOrThrow("ParentId");
          final int _cursorIndexOfMIsDone = _cursor.getColumnIndexOrThrow("isDone");
          final int _cursorIndexOfMPath = _cursor.getColumnIndexOrThrow("Path");
          final List<Task> _result = new ArrayList<Task>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Task _item;
            final Integer _tmpMId;
            if (_cursor.isNull(_cursorIndexOfMId)) {
              _tmpMId = null;
            } else {
              _tmpMId = _cursor.getInt(_cursorIndexOfMId);
            }
            final Boolean _tmpMStarMark;
            final Integer _tmp;
            if (_cursor.isNull(_cursorIndexOfMStarMark)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(_cursorIndexOfMStarMark);
            }
            _tmpMStarMark = _tmp == null ? null : _tmp != 0;
            final String _tmpMName;
            _tmpMName = _cursor.getString(_cursorIndexOfMName);
            final String _tmpMDescription;
            _tmpMDescription = _cursor.getString(_cursorIndexOfMDescription);
            final Integer _tmpMParentId;
            if (_cursor.isNull(_cursorIndexOfMParentId)) {
              _tmpMParentId = null;
            } else {
              _tmpMParentId = _cursor.getInt(_cursorIndexOfMParentId);
            }
            final Boolean _tmpMIsDone;
            final Integer _tmp_1;
            if (_cursor.isNull(_cursorIndexOfMIsDone)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getInt(_cursorIndexOfMIsDone);
            }
            _tmpMIsDone = _tmp_1 == null ? null : _tmp_1 != 0;
            final ArrayList<Integer> _tmpMPath;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfMPath);
            _tmpMPath = Task.ArrayListConverter.fromStringToArrayList(_tmp_2);
            _item = new Task(_tmpMName,_tmpMParentId,_tmpMIsDone,_tmpMDescription,_tmpMPath,_tmpMId,_tmpMStarMark);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    }.getLiveData();
  }

  @Override
  public LiveData<List<Task>> getObservableTasks() {
    final String _sql = "SELECT * from TaskTable ORDER BY Name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return new ComputableLiveData<List<Task>>() {
      private Observer _observer;

      @Override
      protected List<Task> compute() {
        if (_observer == null) {
          _observer = new Observer("TaskTable") {
            @Override
            public void onInvalidated(@NonNull Set<String> tables) {
              invalidate();
            }
          };
          __db.getInvalidationTracker().addWeakObserver(_observer);
        }
        final Cursor _cursor = __db.query(_statement);
        try {
          final int _cursorIndexOfMId = _cursor.getColumnIndexOrThrow("Id");
          final int _cursorIndexOfMStarMark = _cursor.getColumnIndexOrThrow("starMark");
          final int _cursorIndexOfMName = _cursor.getColumnIndexOrThrow("Name");
          final int _cursorIndexOfMDescription = _cursor.getColumnIndexOrThrow("Description");
          final int _cursorIndexOfMParentId = _cursor.getColumnIndexOrThrow("ParentId");
          final int _cursorIndexOfMIsDone = _cursor.getColumnIndexOrThrow("isDone");
          final int _cursorIndexOfMPath = _cursor.getColumnIndexOrThrow("Path");
          final List<Task> _result = new ArrayList<Task>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Task _item;
            final Integer _tmpMId;
            if (_cursor.isNull(_cursorIndexOfMId)) {
              _tmpMId = null;
            } else {
              _tmpMId = _cursor.getInt(_cursorIndexOfMId);
            }
            final Boolean _tmpMStarMark;
            final Integer _tmp;
            if (_cursor.isNull(_cursorIndexOfMStarMark)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(_cursorIndexOfMStarMark);
            }
            _tmpMStarMark = _tmp == null ? null : _tmp != 0;
            final String _tmpMName;
            _tmpMName = _cursor.getString(_cursorIndexOfMName);
            final String _tmpMDescription;
            _tmpMDescription = _cursor.getString(_cursorIndexOfMDescription);
            final Integer _tmpMParentId;
            if (_cursor.isNull(_cursorIndexOfMParentId)) {
              _tmpMParentId = null;
            } else {
              _tmpMParentId = _cursor.getInt(_cursorIndexOfMParentId);
            }
            final Boolean _tmpMIsDone;
            final Integer _tmp_1;
            if (_cursor.isNull(_cursorIndexOfMIsDone)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getInt(_cursorIndexOfMIsDone);
            }
            _tmpMIsDone = _tmp_1 == null ? null : _tmp_1 != 0;
            final ArrayList<Integer> _tmpMPath;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfMPath);
            _tmpMPath = Task.ArrayListConverter.fromStringToArrayList(_tmp_2);
            _item = new Task(_tmpMName,_tmpMParentId,_tmpMIsDone,_tmpMDescription,_tmpMPath,_tmpMId,_tmpMStarMark);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    }.getLiveData();
  }

  @Override
  public LiveData<List<Task>> getObservableChildTasks(Integer mParentId) {
    final String _sql = "SELECT * from TaskTable WHERE ParentId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (mParentId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, mParentId);
    }
    return new ComputableLiveData<List<Task>>() {
      private Observer _observer;

      @Override
      protected List<Task> compute() {
        if (_observer == null) {
          _observer = new Observer("TaskTable") {
            @Override
            public void onInvalidated(@NonNull Set<String> tables) {
              invalidate();
            }
          };
          __db.getInvalidationTracker().addWeakObserver(_observer);
        }
        final Cursor _cursor = __db.query(_statement);
        try {
          final int _cursorIndexOfMId = _cursor.getColumnIndexOrThrow("Id");
          final int _cursorIndexOfMStarMark = _cursor.getColumnIndexOrThrow("starMark");
          final int _cursorIndexOfMName = _cursor.getColumnIndexOrThrow("Name");
          final int _cursorIndexOfMDescription = _cursor.getColumnIndexOrThrow("Description");
          final int _cursorIndexOfMParentId = _cursor.getColumnIndexOrThrow("ParentId");
          final int _cursorIndexOfMIsDone = _cursor.getColumnIndexOrThrow("isDone");
          final int _cursorIndexOfMPath = _cursor.getColumnIndexOrThrow("Path");
          final List<Task> _result = new ArrayList<Task>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Task _item;
            final Integer _tmpMId;
            if (_cursor.isNull(_cursorIndexOfMId)) {
              _tmpMId = null;
            } else {
              _tmpMId = _cursor.getInt(_cursorIndexOfMId);
            }
            final Boolean _tmpMStarMark;
            final Integer _tmp;
            if (_cursor.isNull(_cursorIndexOfMStarMark)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(_cursorIndexOfMStarMark);
            }
            _tmpMStarMark = _tmp == null ? null : _tmp != 0;
            final String _tmpMName;
            _tmpMName = _cursor.getString(_cursorIndexOfMName);
            final String _tmpMDescription;
            _tmpMDescription = _cursor.getString(_cursorIndexOfMDescription);
            final Integer _tmpMParentId;
            if (_cursor.isNull(_cursorIndexOfMParentId)) {
              _tmpMParentId = null;
            } else {
              _tmpMParentId = _cursor.getInt(_cursorIndexOfMParentId);
            }
            final Boolean _tmpMIsDone;
            final Integer _tmp_1;
            if (_cursor.isNull(_cursorIndexOfMIsDone)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getInt(_cursorIndexOfMIsDone);
            }
            _tmpMIsDone = _tmp_1 == null ? null : _tmp_1 != 0;
            final ArrayList<Integer> _tmpMPath;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfMPath);
            _tmpMPath = Task.ArrayListConverter.fromStringToArrayList(_tmp_2);
            _item = new Task(_tmpMName,_tmpMParentId,_tmpMIsDone,_tmpMDescription,_tmpMPath,_tmpMId,_tmpMStarMark);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    }.getLiveData();
  }

  @Override
  public List<Task> getStatusTasks(Boolean status, Integer mParentId) {
    final String _sql = "SELECT * from TaskTable WHERE IsDone LIKE ? AND ParentId LIKE ? ORDER BY Name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    final Integer _tmp;
    _tmp = status == null ? null : (status ? 1 : 0);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, _tmp);
    }
    _argIndex = 2;
    if (mParentId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, mParentId);
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfMId = _cursor.getColumnIndexOrThrow("Id");
      final int _cursorIndexOfMStarMark = _cursor.getColumnIndexOrThrow("starMark");
      final int _cursorIndexOfMName = _cursor.getColumnIndexOrThrow("Name");
      final int _cursorIndexOfMDescription = _cursor.getColumnIndexOrThrow("Description");
      final int _cursorIndexOfMParentId = _cursor.getColumnIndexOrThrow("ParentId");
      final int _cursorIndexOfMIsDone = _cursor.getColumnIndexOrThrow("isDone");
      final int _cursorIndexOfMPath = _cursor.getColumnIndexOrThrow("Path");
      final List<Task> _result = new ArrayList<Task>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Task _item;
        final Integer _tmpMId;
        if (_cursor.isNull(_cursorIndexOfMId)) {
          _tmpMId = null;
        } else {
          _tmpMId = _cursor.getInt(_cursorIndexOfMId);
        }
        final Boolean _tmpMStarMark;
        final Integer _tmp_1;
        if (_cursor.isNull(_cursorIndexOfMStarMark)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getInt(_cursorIndexOfMStarMark);
        }
        _tmpMStarMark = _tmp_1 == null ? null : _tmp_1 != 0;
        final String _tmpMName;
        _tmpMName = _cursor.getString(_cursorIndexOfMName);
        final String _tmpMDescription;
        _tmpMDescription = _cursor.getString(_cursorIndexOfMDescription);
        final Integer _tmpMParentId;
        if (_cursor.isNull(_cursorIndexOfMParentId)) {
          _tmpMParentId = null;
        } else {
          _tmpMParentId = _cursor.getInt(_cursorIndexOfMParentId);
        }
        final Boolean _tmpMIsDone;
        final Integer _tmp_2;
        if (_cursor.isNull(_cursorIndexOfMIsDone)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getInt(_cursorIndexOfMIsDone);
        }
        _tmpMIsDone = _tmp_2 == null ? null : _tmp_2 != 0;
        final ArrayList<Integer> _tmpMPath;
        final String _tmp_3;
        _tmp_3 = _cursor.getString(_cursorIndexOfMPath);
        _tmpMPath = Task.ArrayListConverter.fromStringToArrayList(_tmp_3);
        _item = new Task(_tmpMName,_tmpMParentId,_tmpMIsDone,_tmpMDescription,_tmpMPath,_tmpMId,_tmpMStarMark);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<List<Task>> getObservableStatusTasks(Boolean status, Integer mParentId) {
    final String _sql = "SELECT * from TaskTable WHERE isDone LIKE ? AND ParentId LIKE ? ORDER BY Name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    final Integer _tmp;
    _tmp = status == null ? null : (status ? 1 : 0);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, _tmp);
    }
    _argIndex = 2;
    if (mParentId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, mParentId);
    }
    return new ComputableLiveData<List<Task>>() {
      private Observer _observer;

      @Override
      protected List<Task> compute() {
        if (_observer == null) {
          _observer = new Observer("TaskTable") {
            @Override
            public void onInvalidated(@NonNull Set<String> tables) {
              invalidate();
            }
          };
          __db.getInvalidationTracker().addWeakObserver(_observer);
        }
        final Cursor _cursor = __db.query(_statement);
        try {
          final int _cursorIndexOfMId = _cursor.getColumnIndexOrThrow("Id");
          final int _cursorIndexOfMStarMark = _cursor.getColumnIndexOrThrow("starMark");
          final int _cursorIndexOfMName = _cursor.getColumnIndexOrThrow("Name");
          final int _cursorIndexOfMDescription = _cursor.getColumnIndexOrThrow("Description");
          final int _cursorIndexOfMParentId = _cursor.getColumnIndexOrThrow("ParentId");
          final int _cursorIndexOfMIsDone = _cursor.getColumnIndexOrThrow("isDone");
          final int _cursorIndexOfMPath = _cursor.getColumnIndexOrThrow("Path");
          final List<Task> _result = new ArrayList<Task>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Task _item;
            final Integer _tmpMId;
            if (_cursor.isNull(_cursorIndexOfMId)) {
              _tmpMId = null;
            } else {
              _tmpMId = _cursor.getInt(_cursorIndexOfMId);
            }
            final Boolean _tmpMStarMark;
            final Integer _tmp_1;
            if (_cursor.isNull(_cursorIndexOfMStarMark)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getInt(_cursorIndexOfMStarMark);
            }
            _tmpMStarMark = _tmp_1 == null ? null : _tmp_1 != 0;
            final String _tmpMName;
            _tmpMName = _cursor.getString(_cursorIndexOfMName);
            final String _tmpMDescription;
            _tmpMDescription = _cursor.getString(_cursorIndexOfMDescription);
            final Integer _tmpMParentId;
            if (_cursor.isNull(_cursorIndexOfMParentId)) {
              _tmpMParentId = null;
            } else {
              _tmpMParentId = _cursor.getInt(_cursorIndexOfMParentId);
            }
            final Boolean _tmpMIsDone;
            final Integer _tmp_2;
            if (_cursor.isNull(_cursorIndexOfMIsDone)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getInt(_cursorIndexOfMIsDone);
            }
            _tmpMIsDone = _tmp_2 == null ? null : _tmp_2 != 0;
            final ArrayList<Integer> _tmpMPath;
            final String _tmp_3;
            _tmp_3 = _cursor.getString(_cursorIndexOfMPath);
            _tmpMPath = Task.ArrayListConverter.fromStringToArrayList(_tmp_3);
            _item = new Task(_tmpMName,_tmpMParentId,_tmpMIsDone,_tmpMDescription,_tmpMPath,_tmpMId,_tmpMStarMark);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    }.getLiveData();
  }

  @Override
  public List<Task> getMarkTasks(Boolean mStarMark, Integer mParentId) {
    final String _sql = "SELECT * from TaskTable WHERE starMark LIKE ? AND ParentId LIKE ? ORDER BY Name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    final Integer _tmp;
    _tmp = mStarMark == null ? null : (mStarMark ? 1 : 0);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, _tmp);
    }
    _argIndex = 2;
    if (mParentId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, mParentId);
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfMId = _cursor.getColumnIndexOrThrow("Id");
      final int _cursorIndexOfMStarMark = _cursor.getColumnIndexOrThrow("starMark");
      final int _cursorIndexOfMName = _cursor.getColumnIndexOrThrow("Name");
      final int _cursorIndexOfMDescription = _cursor.getColumnIndexOrThrow("Description");
      final int _cursorIndexOfMParentId = _cursor.getColumnIndexOrThrow("ParentId");
      final int _cursorIndexOfMIsDone = _cursor.getColumnIndexOrThrow("isDone");
      final int _cursorIndexOfMPath = _cursor.getColumnIndexOrThrow("Path");
      final List<Task> _result = new ArrayList<Task>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Task _item;
        final Integer _tmpMId;
        if (_cursor.isNull(_cursorIndexOfMId)) {
          _tmpMId = null;
        } else {
          _tmpMId = _cursor.getInt(_cursorIndexOfMId);
        }
        final Boolean _tmpMStarMark;
        final Integer _tmp_1;
        if (_cursor.isNull(_cursorIndexOfMStarMark)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getInt(_cursorIndexOfMStarMark);
        }
        _tmpMStarMark = _tmp_1 == null ? null : _tmp_1 != 0;
        final String _tmpMName;
        _tmpMName = _cursor.getString(_cursorIndexOfMName);
        final String _tmpMDescription;
        _tmpMDescription = _cursor.getString(_cursorIndexOfMDescription);
        final Integer _tmpMParentId;
        if (_cursor.isNull(_cursorIndexOfMParentId)) {
          _tmpMParentId = null;
        } else {
          _tmpMParentId = _cursor.getInt(_cursorIndexOfMParentId);
        }
        final Boolean _tmpMIsDone;
        final Integer _tmp_2;
        if (_cursor.isNull(_cursorIndexOfMIsDone)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getInt(_cursorIndexOfMIsDone);
        }
        _tmpMIsDone = _tmp_2 == null ? null : _tmp_2 != 0;
        final ArrayList<Integer> _tmpMPath;
        final String _tmp_3;
        _tmp_3 = _cursor.getString(_cursorIndexOfMPath);
        _tmpMPath = Task.ArrayListConverter.fromStringToArrayList(_tmp_3);
        _item = new Task(_tmpMName,_tmpMParentId,_tmpMIsDone,_tmpMDescription,_tmpMPath,_tmpMId,_tmpMStarMark);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<List<Task>> getObservableMarkTasks(Boolean mStarMark, Integer mParentId) {
    final String _sql = "SELECT * from TaskTable WHERE starMark LIKE ? AND ParentId LIKE ? ORDER BY Name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    final Integer _tmp;
    _tmp = mStarMark == null ? null : (mStarMark ? 1 : 0);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, _tmp);
    }
    _argIndex = 2;
    if (mParentId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, mParentId);
    }
    return new ComputableLiveData<List<Task>>() {
      private Observer _observer;

      @Override
      protected List<Task> compute() {
        if (_observer == null) {
          _observer = new Observer("TaskTable") {
            @Override
            public void onInvalidated(@NonNull Set<String> tables) {
              invalidate();
            }
          };
          __db.getInvalidationTracker().addWeakObserver(_observer);
        }
        final Cursor _cursor = __db.query(_statement);
        try {
          final int _cursorIndexOfMId = _cursor.getColumnIndexOrThrow("Id");
          final int _cursorIndexOfMStarMark = _cursor.getColumnIndexOrThrow("starMark");
          final int _cursorIndexOfMName = _cursor.getColumnIndexOrThrow("Name");
          final int _cursorIndexOfMDescription = _cursor.getColumnIndexOrThrow("Description");
          final int _cursorIndexOfMParentId = _cursor.getColumnIndexOrThrow("ParentId");
          final int _cursorIndexOfMIsDone = _cursor.getColumnIndexOrThrow("isDone");
          final int _cursorIndexOfMPath = _cursor.getColumnIndexOrThrow("Path");
          final List<Task> _result = new ArrayList<Task>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Task _item;
            final Integer _tmpMId;
            if (_cursor.isNull(_cursorIndexOfMId)) {
              _tmpMId = null;
            } else {
              _tmpMId = _cursor.getInt(_cursorIndexOfMId);
            }
            final Boolean _tmpMStarMark;
            final Integer _tmp_1;
            if (_cursor.isNull(_cursorIndexOfMStarMark)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getInt(_cursorIndexOfMStarMark);
            }
            _tmpMStarMark = _tmp_1 == null ? null : _tmp_1 != 0;
            final String _tmpMName;
            _tmpMName = _cursor.getString(_cursorIndexOfMName);
            final String _tmpMDescription;
            _tmpMDescription = _cursor.getString(_cursorIndexOfMDescription);
            final Integer _tmpMParentId;
            if (_cursor.isNull(_cursorIndexOfMParentId)) {
              _tmpMParentId = null;
            } else {
              _tmpMParentId = _cursor.getInt(_cursorIndexOfMParentId);
            }
            final Boolean _tmpMIsDone;
            final Integer _tmp_2;
            if (_cursor.isNull(_cursorIndexOfMIsDone)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getInt(_cursorIndexOfMIsDone);
            }
            _tmpMIsDone = _tmp_2 == null ? null : _tmp_2 != 0;
            final ArrayList<Integer> _tmpMPath;
            final String _tmp_3;
            _tmp_3 = _cursor.getString(_cursorIndexOfMPath);
            _tmpMPath = Task.ArrayListConverter.fromStringToArrayList(_tmp_3);
            _item = new Task(_tmpMName,_tmpMParentId,_tmpMIsDone,_tmpMDescription,_tmpMPath,_tmpMId,_tmpMStarMark);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    }.getLiveData();
  }

  @Override
  public List<Task> getRootTasks() {
    final String _sql = "SELECT * from TaskTable WHERE ParentId = -1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfMId = _cursor.getColumnIndexOrThrow("Id");
      final int _cursorIndexOfMStarMark = _cursor.getColumnIndexOrThrow("starMark");
      final int _cursorIndexOfMName = _cursor.getColumnIndexOrThrow("Name");
      final int _cursorIndexOfMDescription = _cursor.getColumnIndexOrThrow("Description");
      final int _cursorIndexOfMParentId = _cursor.getColumnIndexOrThrow("ParentId");
      final int _cursorIndexOfMIsDone = _cursor.getColumnIndexOrThrow("isDone");
      final int _cursorIndexOfMPath = _cursor.getColumnIndexOrThrow("Path");
      final List<Task> _result = new ArrayList<Task>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Task _item;
        final Integer _tmpMId;
        if (_cursor.isNull(_cursorIndexOfMId)) {
          _tmpMId = null;
        } else {
          _tmpMId = _cursor.getInt(_cursorIndexOfMId);
        }
        final Boolean _tmpMStarMark;
        final Integer _tmp;
        if (_cursor.isNull(_cursorIndexOfMStarMark)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getInt(_cursorIndexOfMStarMark);
        }
        _tmpMStarMark = _tmp == null ? null : _tmp != 0;
        final String _tmpMName;
        _tmpMName = _cursor.getString(_cursorIndexOfMName);
        final String _tmpMDescription;
        _tmpMDescription = _cursor.getString(_cursorIndexOfMDescription);
        final Integer _tmpMParentId;
        if (_cursor.isNull(_cursorIndexOfMParentId)) {
          _tmpMParentId = null;
        } else {
          _tmpMParentId = _cursor.getInt(_cursorIndexOfMParentId);
        }
        final Boolean _tmpMIsDone;
        final Integer _tmp_1;
        if (_cursor.isNull(_cursorIndexOfMIsDone)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getInt(_cursorIndexOfMIsDone);
        }
        _tmpMIsDone = _tmp_1 == null ? null : _tmp_1 != 0;
        final ArrayList<Integer> _tmpMPath;
        final String _tmp_2;
        _tmp_2 = _cursor.getString(_cursorIndexOfMPath);
        _tmpMPath = Task.ArrayListConverter.fromStringToArrayList(_tmp_2);
        _item = new Task(_tmpMName,_tmpMParentId,_tmpMIsDone,_tmpMDescription,_tmpMPath,_tmpMId,_tmpMStarMark);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<List<Task>> getObservableRootTasks() {
    final String _sql = "SELECT * from TaskTable WHERE ParentId = -1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return new ComputableLiveData<List<Task>>() {
      private Observer _observer;

      @Override
      protected List<Task> compute() {
        if (_observer == null) {
          _observer = new Observer("TaskTable") {
            @Override
            public void onInvalidated(@NonNull Set<String> tables) {
              invalidate();
            }
          };
          __db.getInvalidationTracker().addWeakObserver(_observer);
        }
        final Cursor _cursor = __db.query(_statement);
        try {
          final int _cursorIndexOfMId = _cursor.getColumnIndexOrThrow("Id");
          final int _cursorIndexOfMStarMark = _cursor.getColumnIndexOrThrow("starMark");
          final int _cursorIndexOfMName = _cursor.getColumnIndexOrThrow("Name");
          final int _cursorIndexOfMDescription = _cursor.getColumnIndexOrThrow("Description");
          final int _cursorIndexOfMParentId = _cursor.getColumnIndexOrThrow("ParentId");
          final int _cursorIndexOfMIsDone = _cursor.getColumnIndexOrThrow("isDone");
          final int _cursorIndexOfMPath = _cursor.getColumnIndexOrThrow("Path");
          final List<Task> _result = new ArrayList<Task>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Task _item;
            final Integer _tmpMId;
            if (_cursor.isNull(_cursorIndexOfMId)) {
              _tmpMId = null;
            } else {
              _tmpMId = _cursor.getInt(_cursorIndexOfMId);
            }
            final Boolean _tmpMStarMark;
            final Integer _tmp;
            if (_cursor.isNull(_cursorIndexOfMStarMark)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(_cursorIndexOfMStarMark);
            }
            _tmpMStarMark = _tmp == null ? null : _tmp != 0;
            final String _tmpMName;
            _tmpMName = _cursor.getString(_cursorIndexOfMName);
            final String _tmpMDescription;
            _tmpMDescription = _cursor.getString(_cursorIndexOfMDescription);
            final Integer _tmpMParentId;
            if (_cursor.isNull(_cursorIndexOfMParentId)) {
              _tmpMParentId = null;
            } else {
              _tmpMParentId = _cursor.getInt(_cursorIndexOfMParentId);
            }
            final Boolean _tmpMIsDone;
            final Integer _tmp_1;
            if (_cursor.isNull(_cursorIndexOfMIsDone)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getInt(_cursorIndexOfMIsDone);
            }
            _tmpMIsDone = _tmp_1 == null ? null : _tmp_1 != 0;
            final ArrayList<Integer> _tmpMPath;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfMPath);
            _tmpMPath = Task.ArrayListConverter.fromStringToArrayList(_tmp_2);
            _item = new Task(_tmpMName,_tmpMParentId,_tmpMIsDone,_tmpMDescription,_tmpMPath,_tmpMId,_tmpMStarMark);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    }.getLiveData();
  }
}
