package com.example.todolist;

import android.app.Activity;
<<<<<<< HEAD
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
=======
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
>>>>>>> FirstCommit by Ruslan, edited xml
import android.widget.TextView;

import java.util.Date;

public class CreateNewTaskActivity extends Activity {

    ToDoViewModel toDoViewModel = new ToDoViewModel(getApplication());
    TextView tvName;
    TextView tvDiscrription;
    TextView tvDeadline;
<<<<<<< HEAD
    EditText etName;
    EditText etDiscrription;
    EditText etDeadline;
=======
    TextView tvSetStarmark;

    EditText etName;
    EditText etDiscrription;
    EditText etDeadline;

    CheckBox cbStarmark;
    ImageView ivCreateStarmark;
>>>>>>> FirstCommit by Ruslan, edited xml
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
<<<<<<< HEAD

        etName = findViewById(R.id.name_et);
        etDiscrription = findViewById(R.id.discription_et);
        etDeadline = findViewById(R.id.deadline_et);

=======
        tvSetStarmark = findViewById(R.id.set_starmark);

        etName = findViewById(R.id.name_et);
        etDiscrription = findViewById(R.id.description_et);
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

>>>>>>> FirstCommit by Ruslan, edited xml
        Bundle temp = getIntent().getExtras();
        curId = temp.getInt("EXTRA_VALUE");

        Button btnAddTask = findViewById(R.id.btn_add);
        if(temp.getInt("Root")==-1) {
            btnAddTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//Хранить cur в sharedpreferences
                    toDoViewModel.insertRootTask(new Task(etName.getText().toString(), etDiscrription.getText().toString()
<<<<<<< HEAD
                            , new Date(1000), false, curId));
=======
                            , new Date(1000), false, curId, cbStarmark.isChecked()));
>>>>>>> FirstCommit by Ruslan, edited xml
                }
            });
        }else{
            final int taskId = temp.getInt("Root");
            btnAddTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toDoViewModel.insertTask(lr.receiveTask(taskId),new Task(etName.getText().toString(), etDiscrription.getText().toString()
<<<<<<< HEAD
                            , new Date(1000), false, curId));
=======
                            , new Date(1000), false, curId, cbStarmark.isChecked()));
>>>>>>> FirstCommit by Ruslan, edited xml
                }
            });
        }
    }
}
