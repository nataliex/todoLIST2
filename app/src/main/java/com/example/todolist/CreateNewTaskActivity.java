package com.example.todolist;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class CreateNewTaskActivity extends Activity {

    ToDoViewModel toDoViewModel = new ToDoViewModel(getApplication());
    TextView tvName;
    TextView tvDiscrription;
    TextView tvDeadline;
    EditText etName;
    EditText etDiscrription;
    EditText etDeadline;
    int curId;

    ToDoListDatabase tdld;
    ListRepository lr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_task);

         tdld = ToDoListDatabase.getDatabase(this.getApplicationContext());
         lr = new ListRepository(tdld);

        tvName = findViewById(R.id.name_tv);
        tvDiscrription = findViewById(R.id.discription_tv);
        tvDeadline = findViewById(R.id.deadline_tv);

        etName = findViewById(R.id.name_et);
        etDiscrription = findViewById(R.id.discription_et);
        etDeadline = findViewById(R.id.deadline_et);

        final Bundle temp = getIntent().getExtras();
        curId = temp.getInt("EXTRA_VALUE");

        Button btnAddTask = findViewById(R.id.btn_add);
        if(temp.getInt("Root")==-1) {
            btnAddTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//Хранить cur в sharedpreferences
                    toDoViewModel.insertRootTask(new Task(etName.getText().toString(), etDiscrription.getText().toString()
                            , new Date(1000), false, curId));
                }
            });
        }else{
            final int taskId = temp.getInt("Root");
            btnAddTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<Integer> tempArrayList = new ArrayList<>();
                    tempArrayList.add(taskId);
                    toDoViewModel.insertTask(lr.receiveTask(taskId),new Task(etName.getText().toString(), taskId, false, new Date(1000),
                            etDiscrription.getText().toString(), tempArrayList, curId, false));
                }
            });
        }
    }
}
