package com.example.todolist;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FragmentTask extends Fragment {
    ToDoViewModel toDoViewModel;
    private int taskId;
    public ArrayList<Task> mArrayTasks;

    public static final int FRAGMENT_TASK = 0;
    public static final int EDIT_TASK_FRAGMENT = 1;

    //При возвращении в активити никто не изменяет значения taskid и оно инициализируется значением
    //по умолчание - нулём.Для решения этой проблемы необходимо во ViewModel хранить id таска,можно помимо этого хранить
    //и саму таску,и массив известных про неё данных

    void setTask(int i){
        taskId = i;
    }

    public static final String EXTRA_VALUE = "EXTRA_VALUE";
    static int curId;
    Changer changer;

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

        tempView = inflater.inflate(R.layout.fragment_fragment_task, container, false);

        final FragmentTask.AdapterTask adapterTask = new FragmentTask.AdapterTask();

        TextView mainTaskName =  tempView.findViewById(R.id.main_task_name);
        TextView mainTaskNameDeadline =  tempView.findViewById(R.id.main_task_name_deadline);
        TextView mainTaskDescriptions =  tempView.findViewById(R.id.main_task_descriptions);
        CheckBox mainTaskCheckbox = tempView.findViewById(R.id.main_task_checkbox);
        ImageView mainTaskStarMark = tempView.findViewById(R.id.main_task_star_mark);

        final Task mainTask = toDoViewModel.getCurrentTask();

        mainTaskNameDeadline.setText(mainTask.getDeadline().toString());
        mainTaskName.setText(mainTask.getName());
        mainTaskDescriptions.setText(mainTask.getDescription());
        mainTaskCheckbox.setChecked(mainTask.getIsDone());

        if (mainTask.getStarMark()) mainTaskStarMark.setVisibility(View.VISIBLE);
        else mainTaskStarMark.setVisibility(View.INVISIBLE);

        if (mainTask.getIsDone()) mainTaskCheckbox.setChecked(true);
        else mainTaskCheckbox.setChecked(false);

        mainTaskCheckbox.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                toDoViewModel.changeIsDone(mainTask);
            }
        });

        RecyclerView recyclerViewTask = tempView.findViewById(R.id.list_task1);
        recyclerViewTask.setAdapter(adapterTask);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        recyclerViewTask.setLayoutManager(linearLayoutManager);

        Button addTaskButton = tempView.findViewById(R.id.add_task_button1);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curId++;
                Intent tempIntent = new Intent(getActivity(), CreateNewTaskActivity.class);
                tempIntent.putExtra(EXTRA_VALUE, curId);
                tempIntent.putExtra("Root",taskId);

                startActivity(tempIntent);

            }
        });
        return tempView;
    }

    class AdapterTask extends RecyclerView.Adapter<FragmentTask.AdapterTask.ViewHolderTask> {



        public AdapterTask() {
            //убрал toDoViewModel.get(Task mTask) тк сидит в main thread'e
            mArrayTasks = new ArrayList<Task>();
            Observer<List<Task>> observer = new Observer<List<Task>>() {
                @Override
                public void onChanged(@Nullable List<Task> tasks) {
                    mArrayTasks.clear();
                    mArrayTasks.addAll(tasks);
                    FragmentTask.AdapterTask.super.notifyDataSetChanged();
                    Log.d("DataObserved!","DataObserved!");
                }
            };
            toDoViewModel.getCurrentTasks().observe(FragmentTask.this,observer);//Ошибка - надо передавать не только таску
        }

        class ViewHolderTask extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

            TextView textViewName;
            TextView textViewDeadline;
            CheckBox checkIsDone;
            ImageView imageStarMark;

            public ViewHolderTask(@NonNull View itemView) {
                super(itemView);
                textViewName = itemView.findViewById(R.id.task_name);
                textViewDeadline = itemView.findViewById(R.id.dead_line);
                checkIsDone = itemView.findViewById(R.id.check_is_done);
                imageStarMark = itemView.findViewById(R.id.star_mark);

                //Регистрируем
                textViewName.setOnCreateContextMenuListener(this);
            }

            //Реализовываем метод
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(getAdapterPosition(), 1, 0, "Удалить");
                menu.add(getAdapterPosition(), 2, 0, "Редактировать");
            }
        }

        @NonNull
        @Override
        public FragmentTask.AdapterTask.ViewHolderTask onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View v = inflater.inflate(R.layout.task, viewGroup, false);
            return new FragmentTask.AdapterTask.ViewHolderTask(v);
        }

        @Override
        public void onBindViewHolder(@NonNull FragmentTask.AdapterTask.ViewHolderTask viewHolderTask, final int i) {
            viewHolderTask.textViewName.setText(mArrayTasks.get(i).getName());
            viewHolderTask.textViewDeadline.setText(mArrayTasks.get(i).getDeadline().toString());
            viewHolderTask.checkIsDone.setChecked(mArrayTasks.get(i).getIsDone());

            //Отображение важности
            if (mArrayTasks.get(i).getStarMark())
                viewHolderTask.imageStarMark.setVisibility(View.VISIBLE);
            else viewHolderTask.imageStarMark.setVisibility(View.INVISIBLE);

            //Изменяет выполненность залачи
            viewHolderTask.checkIsDone.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    toDoViewModel.changeIsDone(mArrayTasks.get(i));//Обращение к БД в mainThread'e
                }
            });

            //Переход во внутренности задачи
            viewHolderTask.textViewName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toDoViewModel.getCurrentTasks().removeObservers(FragmentTask.this);
                    changer.changeFragment(mArrayTasks.get(i).getId(), FRAGMENT_TASK);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mArrayTasks.size();
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        //В зависимости оттого, что нажато - удаление или редактирование.
        if (item.getItemId() == 1) {

            //Прежде, чем удалять задачу, удалим все ее подзадачи
            for (Task t : toDoViewModel.getChildTasks(toDoViewModel.getTask(mArrayTasks.get(item.getGroupId()).getId()))) {
                toDoViewModel.deleteTask(t);
            }

            //Удалим саму задачу, найдя ее айдишник в mArrayTasks
            toDoViewModel.deleteTask(toDoViewModel.getTask(mArrayTasks.get(item.getGroupId()).getId()));
        }else{

            toDoViewModel.getCurrentTasks().removeObservers(FragmentTask.this);
            changer.changeFragment(mArrayTasks.get(item.getGroupId()).getId(), EDIT_TASK_FRAGMENT);
        }

        return super.onContextItemSelected(item);
    }
}