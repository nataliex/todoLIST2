package com.example.todolist;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

public class AdapterFragment extends Fragment {

    ToDoViewModel toDoViewModel;
    ArrayList<Task> mArrayTasks;

    @Override
    public void onAttach(Context context) {
        toDoViewModel = ViewModelProviders.of(this.getActivity()).get(ToDoViewModel.class);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("Start","OnCreateView");
        View tempView;

        tempView = inflater.inflate(R.layout.adapter_fragment, container, false);

        TextView tvNoneTasks;
        tvNoneTasks = tempView.findViewById(R.id.tv_none_tasks);

        final AdapterTask adapterTask = new AdapterTask(tvNoneTasks);
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


        Button btnLoadTaskWeb = tempView.findViewById(R.id.btn_load_task);
        btnLoadTaskWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tempIntent = new Intent(getActivity(), LoadTaskFromWebActivity.class);
                startActivity(tempIntent);
            }
        });

        return tempView;
    }

    class AdapterTask extends RecyclerView.Adapter<AdapterTask.ViewHolderTask> {

        public AdapterTask(final TextView mTvNoneTasks){
            mArrayTasks = new ArrayList<Task>();
            mArrayTasks.addAll(toDoViewModel.getNotobservableCurrentTasks());
            toDoViewModel.getObservableCurrentTasks().
                    observe(AdapterFragment.this, new Observer<List<Task>>() {
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
        public ViewHolderTask onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View v = inflater.inflate(R.layout.task, viewGroup, false);
            return new ViewHolderTask(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolderTask viewHolderTask,final int i) {

            viewHolderTask.textViewName.setText(mArrayTasks.get(i).getName());
            viewHolderTask.checkIsDone.setChecked(mArrayTasks.get(i).getIsDone());
            viewHolderTask.checkIsDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toDoViewModel.changeIsDone(mArrayTasks.get(i));
                }
            });

            //отвечает за отображение, важная ли задача или нет
            if (mArrayTasks.get(i).getStarMark())
                viewHolderTask.imageStarMark.setVisibility(View.VISIBLE);
            else viewHolderTask.imageStarMark.setVisibility(View.INVISIBLE);

            //Изменяет выполненность залачи

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

        public ArrayList<Task> getArrayTasks() {
            return mArrayTasks;
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