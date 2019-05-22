package com.example.todolist;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    int taskId;
    //Создавать toDoViewModel здесь,что инициализировать её раньше,также с помощью неё перейти на открытие новых фрагментов
    //основных проблемы две:при повороте экрана не сохраняются нужный список задач и при запуске toDoViewModel в асинхронном режиме
    //она не успевает проинициализироваться на всех устройствах
    ToDoViewModel toDoViewModel;
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MainActivity","onStart");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0FB2F7")));

        Log.d("MainActivity","onCreate");
        initDrawer();
        toDoViewModel = ViewModelProviders.of(this).get(ToDoViewModel.class);
        toDoViewModel.getLiveCode().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                Log.d("MainActivity","OnChangedCalled");
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                if(integer==-1){
                    Log.d("MainActivity","-1 is called");
                    transaction.replace(R.id.conteiner_adapter, new AdapterFragment());
                }if(integer==0){
                    Log.d("MainActivity","0 is called");
                    transaction.replace(R.id.conteiner_adapter, new FragmentTask());
                }
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
    @Override
    public void onBackPressed(){
        if(toDoViewModel.getCurrentTask()!=null) toDoViewModel.goBack(toDoViewModel.getCurrentTask());
        else MainActivity.super.finish();
    }

//e


    //-----------------------------------ItemClickListeners---------------------------------------

    private void initDrawer(){
        Log.d("Before","SetAdapterCalled");
        ListView mDrawerListView = findViewById(R.id.left_drawer);
        ArrayList<String> Names = new ArrayList<>();
        Names.add("Все задачи");
        Names.add("Выполненные");
        Names.add("Невыполненные");
        Names.add("Важные");
        Names.add("Неважные");
        mDrawerListView.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item, Names));
        Log.d("After","SetAdapterCalled");

        mDrawerListView.setOnItemClickListener(new DrawerItemClickListener());
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    setTitle("Список задач");
                    toDoViewModel.getFilteredTasks(toDoViewModel.CODE_ALLTASKS);
                    break;
                case 1:
                    setTitle("Выполненные задачи");
                    toDoViewModel.getFilteredTasks(toDoViewModel.CODE_COMPLETEDTASKS);
                    break;
                case 2:
                    setTitle("Невыполненные задачи");
                    toDoViewModel.getFilteredTasks(toDoViewModel.CODE_NOTCOMPLETEDTASKS);
                    break;
                case 3:
                    setTitle("Важные задачи");
                    toDoViewModel.getFilteredTasks(toDoViewModel.CODE_GREATTASKS);
                    break;
                case 4:
                    setTitle("Неважные задачи");
                    toDoViewModel.getFilteredTasks(toDoViewModel.CODE_NOTGREATTASKS);
                    break;
            }
        }
    }
}
