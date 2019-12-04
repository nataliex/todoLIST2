package com.example.proba;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FragmentTask extends Fragment {

    ToDoViewModel toDoViewModel;
    ArrayList<Task> mArrayTasks;
    //При возвращении в активити никто не изменяет значения taskid и оно инициализируется значением
    //по умолчание - нулём.Для решения этой проблемы необходимо во ViewModel хранить id таска,можно помимо этого хранить
    //и саму таску,и массив известных про неё данных

    @Override
    public void onAttach(Context context) {
        toDoViewModel = ViewModelProviders.of(this.getActivity()).get(ToDoViewModel.class);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View tempView;
        tempView = inflater.inflate(R.layout.fragment_fragment_task, container, false);

        TextView tvNoneTasks;
        tvNoneTasks = tempView.findViewById(R.id.tv_none_tasks_2);

        final FragmentTask.AdapterTask adapterTask = new FragmentTask.AdapterTask(tvNoneTasks);
        TextView mainTaskName =  tempView.findViewById(R.id.main_task_name);
        TextView mainTaskDescriptions =  tempView.findViewById(R.id.main_task_descriptions);
        CheckBox mainTaskCheckbox = tempView.findViewById(R.id.main_task_checkbox);
        ImageView mainTaskStarMark = tempView.findViewById(R.id.main_task_star_mark);

        final Task mainTask = toDoViewModel.getCurrentTask();

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

        FloatingActionButton addTaskButton = tempView.findViewById(R.id.add_task_button1);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tempIntent = new Intent(getActivity(), CreateNewTaskActivity.class);
                tempIntent.putExtra("Root",toDoViewModel.getCurrentTask().getId());

                startActivity(tempIntent);
            }
        });
        return tempView;
    }

    class AdapterTask extends RecyclerView.Adapter<AdapterTask.ViewHolderTask> {


        public AdapterTask(final TextView mTvNoneTasks) {

            mArrayTasks = new ArrayList<>();
            mArrayTasks.addAll(toDoViewModel.getNotobservableCurrentTasks());

            toDoViewModel.getObservableCurrentTasks().observe(FragmentTask.this,new Observer<List<Task>>() {
                @Override
                public void onChanged(@Nullable List<Task> tasks) {
                    mArrayTasks.clear();
                    mArrayTasks.addAll(tasks);

                    if(mArrayTasks.size() == 0)
                        mTvNoneTasks.setVisibility(View.VISIBLE);
                    else mTvNoneTasks.setVisibility(View.INVISIBLE);

                    AdapterTask.super.notifyDataSetChanged();
                }
            });
        }

        class ViewHolderTask extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

            TextView textViewName;
            CheckBox checkIsDone;
            ImageView imageStarMark;

            public ViewHolderTask(@NonNull View itemView) {
                super(itemView);
                textViewName = itemView.findViewById(R.id.task_name);
                checkIsDone = itemView.findViewById(R.id.check_is_done);
                imageStarMark = itemView.findViewById(R.id.star_mark);
                textViewName.setOnCreateContextMenuListener(this);
            }

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
            viewHolderTask.checkIsDone.setChecked(mArrayTasks.get(i).getIsDone());

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

            viewHolderTask.textViewName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toDoViewModel.changeTask(mArrayTasks.get(i));
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
            toDoViewModel.deleteTask(toDoViewModel.getTask(mArrayTasks.get(item.getGroupId()).getId()));//TODO удаление по Id
        }else{
            Intent tempIntent = new Intent(getActivity(), CreateNewTaskActivity.class);
            tempIntent.putExtra("Root",-2);
            tempIntent.putExtra("ID", mArrayTasks.get(item.getGroupId()).getId());
            tempIntent.putExtra("TaskName",mArrayTasks.get(item.getGroupId()).getName());
            tempIntent.putExtra("TaskDescription",mArrayTasks.get(item.getGroupId()).getDescription());
            tempIntent.putExtra("TaskStarMark",mArrayTasks.get(item.getGroupId()).getStarMark());
            startActivity(tempIntent);
        }

        return super.onContextItemSelected(item);
    }
}