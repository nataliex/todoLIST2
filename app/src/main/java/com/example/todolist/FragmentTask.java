package com.example.todolist;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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
    //При возвращении в активити никто не изменяет значения taskid и оно инициализируется значением
    //по умолчание - нулём.Для решения этой проблемы необходимо во ViewModel хранить id таска,можно помимо этого хранить
    //и саму таску,и массив известных про неё данных

    void setTask(int i){
        taskId = i;
    }

    public static final String EXTRA_VALUE = "EXTRA_VALUE";
    static int curId;
    ToDoListDatabase tdld = null;
    ListRepository lr = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        tdld = ToDoListDatabase.getDatabase(context.getApplicationContext());//Общатсья через ViewModel
        lr = new ListRepository(tdld);
        toDoViewModel = new ToDoViewModel(getActivity().getApplication());
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
        Task mainTask = toDoViewModel.getTask(taskId);
        mainTaskNameDeadline.setText(mainTask.getDeadline().toString());
        mainTaskName.setText(mainTask.getName());
        mainTaskDescriptions.setText(mainTask.getDescription());
        mainTaskCheckbox.setChecked(mainTask.getIsDone());

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
                tempIntent.putExtra("isRoot",taskId);
                startActivity(tempIntent);
            }
        });
        return tempView;
    }

    class AdapterTask extends RecyclerView.Adapter<FragmentTask.AdapterTask.ViewHolderTask> {

        private ArrayList<Task> mArrayTasks;

        public AdapterTask() {
            Task task = lr.receiveTask(taskId);
            Log.d("Current task",task.getName());
            Log.d("Current task id",task.getId().toString());
            for (Task t:toDoViewModel.getChildTasks(task)) {//TODO Общение с бд в основном потоке
                Log.d("ChildTask:",t.getName());
            }
            mArrayTasks = (ArrayList<Task>) toDoViewModel.getChildTasks(task);
            Observer<List<Task>> observer = new Observer<List<Task>>() {
                @Override
                public void onChanged(@Nullable List<Task> tasks) {
                    for (Task t: tasks) {
                        Log.d("AllTasks",t.getName());
                        Log.d("ParentIdOfTasks",t.getParentId().toString());
                    }
                    mArrayTasks.clear();
                    mArrayTasks.addAll(tasks);
                    FragmentTask.AdapterTask.super.notifyDataSetChanged();
                    Log.d("DataObserved!","DataObserved!");
                }
            };
            toDoViewModel.getObservableChildTasks(task).observe(FragmentTask.this,observer);
        }

            class ViewHolderTask extends RecyclerView.ViewHolder {

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
        public void onBindViewHolder(@NonNull FragmentTask.AdapterTask.ViewHolderTask viewHolderTask, int i) {
            viewHolderTask.textViewName.setText(mArrayTasks.get(i).getName());
            viewHolderTask.textViewDeadline.setText(mArrayTasks.get(i).getDeadline().toString());
            viewHolderTask.checkIsDone.setChecked(mArrayTasks.get(i).getIsDone());
        }

        @Override
        public int getItemCount() {
            return mArrayTasks.size();
        }
    }


}