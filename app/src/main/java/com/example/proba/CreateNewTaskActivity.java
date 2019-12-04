package com.example.proba;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateNewTaskActivity extends Activity {
    ToDoViewModel toDoViewModel;

    TextView tvSetStarmark;

    EditText etName;
    EditText etDiscrription;

    CheckBox cbStarmark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_task);
        toDoViewModel = new ToDoViewModel(getApplication());

        etName = findViewById(R.id.name_et);
        etDiscrription = findViewById(R.id.discription_et);

        tvSetStarmark = findViewById(R.id.set_starmark);

        //новые элементы на панеле добавления новой задачи,
        //все отвечают за важность/неважность задачи
        cbStarmark = findViewById(R.id.cb_starmark);
        FloatingActionButton btnAddTask = findViewById(R.id.btn_add);

        final Bundle temp = getIntent().getExtras();

        if (temp.getInt("Root") == -2) {
            etName.setText(temp.getString("TaskName"));
            etDiscrription.setText(temp.getString("TaskDescription"));
            cbStarmark.setChecked(temp.getBoolean("TaskStarMark"));
            final Task currentTask = toDoViewModel.getTask(temp.getInt("ID"));
            btnAddTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (etName.getText().toString().length() != 0) {
                        currentTask.setName(etName.getText().toString());
                        currentTask.setDescription(etDiscrription.getText().toString());
                        currentTask.setStarMark(cbStarmark.isChecked());
                        toDoViewModel.updateTask(currentTask);
                        CreateNewTaskActivity.super.finish();
                    } else
                        Toast.makeText(getApplicationContext(), "Введите имя задачи!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            if (temp.getInt("Root") == -1) {
                btnAddTask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etName.getText().toString().length() != 0) {
                            toDoViewModel.insertRootTask(new Task(etName.getText().toString(), etDiscrription.getText().toString(), false, cbStarmark.isChecked()));
                            CreateNewTaskActivity.super.finish();
                        } else
                            Toast.makeText(getApplicationContext(), "Введите имя задачи!", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                final int taskId = temp.getInt("Root");
                btnAddTask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etName.getText().toString().length() != 0) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    toDoViewModel.insertTask(toDoViewModel.getTask(taskId), new Task(etName.getText().toString(),
                                            etDiscrription.getText().toString(), false, cbStarmark.isChecked()));
                                }
                            }).run();
                            CreateNewTaskActivity.super.finish();
                        } else
                            Toast.makeText(getApplicationContext(), "Введите имя задачи!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}