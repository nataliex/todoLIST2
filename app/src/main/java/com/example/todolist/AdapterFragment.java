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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class AdapterFragment extends Fragment {
    ToDoViewModel toDoViewModel;
    public static final String EXTRA_VALUE = "EXTRA_VALUE";
    static int curId;
    ToDoListDatabase tdld= null;
    ListRepository lr = null;
    Changer changer;

    SharedPreferences sharedPreferencesCurId;

    @Override
    public void onAttach(Context context) {
        try {
            changer = (Changer) context;
        } catch(Exception exception){
            Log.d("ImplmenetException","Context has not implemented Changer");
        }
        super.onAttach(context);
        tdld = ToDoListDatabase.getDatabase(context.getApplicationContext());//Общатсья через ViewModel
        lr = new ListRepository(tdld);
        toDoViewModel = new ToDoViewModel(getActivity().getApplication());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       // super.onCreateView(inflater, container, savedInstanceState);

        View tempView;
        sharedPreferencesCurId = PreferenceManager.getDefaultSharedPreferences(getContext());
        final SharedPreferences.Editor editor = sharedPreferencesCurId.edit();
        curId = sharedPreferencesCurId.getInt("CUR_ID", 0);
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
                curId++;
                Intent tempIntent = new Intent(getActivity(), CreateNewTaskActivity.class);
                tempIntent.putExtra(EXTRA_VALUE, curId);
                tempIntent.putExtra("Root",-1);

                editor.putInt("CUR_ID", curId);
                editor.apply();

                startActivity(tempIntent);
            }
        });
        return tempView;
    }

    class AdapterTask extends RecyclerView.Adapter<AdapterTask.ViewHolderTask> {

        private ArrayList<Task> mArrayTasks;

        public AdapterTask(){
                mArrayTasks = new ArrayList<>();
                Observer<List<Task>> observer = new Observer<List<Task>>() {
                    @Override
                    public void onChanged(@Nullable final List<Task> tasks) {
                        mArrayTasks.clear();
                        mArrayTasks.addAll(tasks);
                        AdapterTask.super.notifyDataSetChanged();
                        Log.d("DataObserved!","DataObserved!");
                    }
                };
                toDoViewModel.getObservableRootTasks().observe(AdapterFragment.this,observer);//TODO Общение с бд в основном потоке и где то еще второе есть
        }

        class ViewHolderTask extends RecyclerView.ViewHolder{

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
                viewHolderTask.textViewName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toDoViewModel.getObservableRootTasks().removeObservers(AdapterFragment.this);
                        changer.changeFragment(mArrayTasks.get(i).getId());

                    }
                });
        }

        @Override
        public int getItemCount() {
            return mArrayTasks.size();
        }

    }
}
