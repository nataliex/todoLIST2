package com.example.todolist;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Ignore;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class AdapterFragment extends Fragment {
    ToDoViewModel toDoViewModel;

    public static final int FRAGMENT_TASK = 0;
    public static final int EDIT_TASK_FRAGMENT = 1;

    Changer changer;
    public ArrayList<Task> mArrayTasks;
    //Убрал переменные listrepository,todolistdatabase


    @Override
    public void onAttach(Context context) {
        Log.d("Start","OnAttach");
        try {
            changer = (Changer) context;
        } catch(Exception exception){
            Log.d("ImplmenetException","Context has not implemented Changer");
        }
        super.onAttach(context);
        Bundle bundle = this.getArguments();
        int curQuest = bundle.getInt("key", -1);//todo убрать bundle curquest
        Log.d("Fast","---------------");
        toDoViewModel = ViewModelProviders.of(this,
                new ToDoListFactory(getActivity().getApplication(),curQuest)).get(ToDoViewModel.class); //TODO Меняю передаваемое значение
                                                                                                         //TODO taskId в надежде сделать разные запросы
        Log.d("Fast","---------------");
        Log.d("End","OnAttach");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("Start","OnCreateView");
        View tempView;

        tempView = inflater.inflate(R.layout.adapter_fragment, container, false);


        final AdapterTask adapterTask = new AdapterTask();
        RecyclerView recyclerViewTask = tempView.findViewById(R.id.list_task);
        recyclerViewTask.setAdapter(adapterTask);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        recyclerViewTask.setLayoutManager(linearLayoutManager);

        Button addTaskButton = tempView.findViewById(R.id.add_task_button);
        addTaskButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent tempIntent = new Intent(getActivity(), CreateNewTaskActivity.class);
                tempIntent.putExtra("Root",-1);
                startActivity(tempIntent);

            }
        });
        return tempView;
    }

    class AdapterTask extends RecyclerView.Adapter<AdapterTask.ViewHolderTask>{



        public AdapterTask(){
            mArrayTasks = new ArrayList<>();
            Observer<List<Task>> observer = new Observer<List<Task>>() {
                @Override
                public void onChanged(@Nullable final List<Task> tasks) {
                    mArrayTasks.clear();
                    mArrayTasks.addAll(tasks);
                    AdapterFragment.AdapterTask.super.notifyDataSetChanged();
                    Log.d("DataObserved!","DataObserved!");
                }
            };
            toDoViewModel.getCurrentTasks().observe(AdapterFragment.this,observer);
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

                //Регистрируем контекстное меню на textViewName
                textViewName.setOnCreateContextMenuListener(this);
            }

            //Создаем контекстное меню при длительном нажатии
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(getAdapterPosition(), 1, 0, "Удалить");
                menu.add(getAdapterPosition(), 2, 0, "Редактировать");
            }
        }

        @NonNull
        @Override
        public ViewHolderTask onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View v = inflater.inflate(R.layout.task, viewGroup, false);
            return new ViewHolderTask(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolderTask viewHolderTask,final int i) {

            viewHolderTask.textViewName.setText(mArrayTasks.get(i).getName());
            viewHolderTask.textViewDeadline.setText(mArrayTasks.get(i).getDeadline().toString());
            viewHolderTask.checkIsDone.setChecked(mArrayTasks.get(i).getIsDone());

            //Отвечает за отображение, важная ли задача или нет
            if (mArrayTasks.get(i).getStarMark())
                viewHolderTask.imageStarMark.setVisibility(View.VISIBLE);
            else viewHolderTask.imageStarMark.setVisibility(View.INVISIBLE);

            //Изменяет выполненность залачи
            viewHolderTask.checkIsDone.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    toDoViewModel.changeIsDone(mArrayTasks.get(i));
                }
            });

            //Нажимаем на задачу и переходим к ее содержимому
            viewHolderTask.textViewName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toDoViewModel.getCurrentTasks().removeObservers(AdapterFragment.this);
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

            toDoViewModel.getCurrentTasks().removeObservers(AdapterFragment.this);
            changer.changeFragment(mArrayTasks.get(item.getGroupId()).getId(), EDIT_TASK_FRAGMENT);
        }

        return super.onContextItemSelected(item);
    }

}