package com.example.todolist;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class CreateNewTaskActivity extends Activity {

    ToDoViewModel toDoViewModel = new ToDoViewModel(getApplication());

    TextView tvName;
    TextView tvDiscrription;
    TextView tvDeadline;
    TextView tvSetStarmark;

    EditText etName;
    EditText etDiscrription;
    EditText etDeadline;

    CheckBox cbStarmark;
    ImageView ivCreateStarmark;

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

        tvSetStarmark = findViewById(R.id.set_starmark);

        etName = findViewById(R.id.name_et);
        etDiscrription = findViewById(R.id.discription_et);
        etDeadline = findViewById(R.id.deadline_et);

        //новые элементы на панеле добавления новой задачи,
        //все отвечают за важность/неважность задачи
        cbStarmark = findViewById(R.id.cb_starmark);
        ivCreateStarmark = findViewById(R.id.create_starmark);
        ivCreateStarmark.setVisibility(View.INVISIBLE);

        //отображение важности задачи
        cbStarmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbStarmark.isChecked()) ivCreateStarmark.setVisibility(View.VISIBLE);
                else ivCreateStarmark.setVisibility(View.INVISIBLE);
            }
        });

        final Bundle temp = getIntent().getExtras();
        curId = temp.getInt("EXTRA_VALUE");

        Button btnAddTask = findViewById(R.id.btn_add);
        if(temp.getInt("Root")==-1) {
            btnAddTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//Хранить cur в sharedpreferences
                    toDoViewModel.insertRootTask(new Task(etName.getText().toString(), etDiscrription.getText().toString()
                            , new Date(1000), false, curId, cbStarmark.isChecked()));
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
                            etDiscrription.getText().toString(), tempArrayList, curId, cbStarmark.isChecked()));
                }
            });
        }
    }
}
