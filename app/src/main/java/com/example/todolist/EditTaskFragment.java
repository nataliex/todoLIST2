package com.example.todolist;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class EditTaskFragment extends Fragment {
    ToDoViewModel toDoViewModel;
    public static final String EXTRA_VALUE = "EXTRA_VALUE";
    public static final int FRAGMENT_TASK = 0;
    public static final int EDIT_TASK_FRAGMENT = 1;
    static int curId;
    int taskId;
    Changer changer;
    public ArrayList<Task> mArrayTasks;
    //Убрал переменные listrepository,todolistdatabase

    TextView edit_tvName;
    TextView edit_tvDiscrription;
    TextView edit_tvDeadline;
    TextView edit_tvSetStarmark;

    EditText edit_etName;
    EditText edit_etDiscrription;
    EditText edit_etDeadline;

    CheckBox edit_cbStarmark;
    ImageView edit_ivCreateStarmark;

    CalendarView calendar;

    @Override
    public void onAttach(Context context) {
        try {
            changer = (Changer) context;
        } catch(Exception exception){
            Log.d("ImplmenetException","Context has not implemented Changer");
        }
        super.onAttach(context);
        toDoViewModel = ViewModelProviders.of(this,
                new ToDoListFactory(getActivity().getApplication(),taskId)).get(ToDoViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View tempView;
        final Task mainTask = toDoViewModel.getCurrentTask();

        tempView = inflater.inflate(R.layout.edit_task_fragment, container, false);

        calendar = tempView.findViewById(R.id.edit_calendar);
        float scalingFactor = 0.7f; // scale down to half the size
        calendar.setScaleX(scalingFactor);
        calendar.setScaleY(scalingFactor);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                month++;
                String tempDate = dayOfMonth + "/" + month + "/" + year;
                edit_etDeadline.setText(tempDate);
            }
        });

        edit_tvName = tempView.findViewById(R.id.edit_name_tv);
        edit_tvDiscrription = tempView.findViewById(R.id.edit_discription_tv);
        edit_tvDeadline = tempView.findViewById(R.id.edit_deadline_tv);

        edit_etName = tempView.findViewById(R.id.edit_name_et);
        edit_etDiscrription = tempView.findViewById(R.id.edit_discription_et);
        edit_etDeadline = tempView.findViewById(R.id.edit_deadline_et);

        edit_etName.setText(mainTask.getName());
        edit_etDiscrription.setText(mainTask.getDescription());
        edit_etDeadline.setText(mainTask.getDeadline().toString());

        edit_tvSetStarmark = tempView.findViewById(R.id.edit_set_starmark);

        //новые элементы на панеле добавления новой задачи,
        //все отвечают за важность/неважность задачи
        edit_cbStarmark = tempView.findViewById(R.id.edit_cb_starmark);
        edit_ivCreateStarmark = tempView.findViewById(R.id.edit_create_starmark);
        if (mainTask.getStarMark()){
            edit_ivCreateStarmark.setVisibility(View.VISIBLE);
            edit_cbStarmark.setChecked(true);
        }else {
            edit_ivCreateStarmark.setVisibility(View.INVISIBLE);
            edit_cbStarmark.setChecked(false);
        }

        //отображение важности задачи
        edit_cbStarmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_cbStarmark.isChecked()) edit_ivCreateStarmark.setVisibility(View.VISIBLE);
                else edit_ivCreateStarmark.setVisibility(View.INVISIBLE);
            }
        });

        Button btnEditTask = tempView.findViewById(R.id.edit_btn_add);
        btnEditTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainTask.setName(edit_etName.getText().toString());
                mainTask.setDescription(edit_etDiscrription.getText().toString());
                mainTask.setStarMark(edit_cbStarmark.isChecked());
                mainTask.setDeadline(edit_etDeadline.getText().toString());
                toDoViewModel.updateTask(mainTask);
            }
        });
        return tempView;
    }

    void setTask(int i){
        taskId = i;
    }
}
