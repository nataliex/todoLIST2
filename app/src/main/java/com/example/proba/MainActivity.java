package com.example.proba;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ToDoViewModel toDoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        toDoViewModel = ViewModelProviders.of(this).get(ToDoViewModel.class);
        toDoViewModel.getLiveCode().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                if(integer==-1){

                    transaction.replace(R.id.conteiner_adapter, new AdapterFragment());
                }if(integer==0){

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.all_tasks_menu) {
            setTitle("Список задач");
            toDoViewModel.getFilteredTasks(toDoViewModel.CODE_ALLTASKS);
        } else if (id == R.id.isDone_tasks_menu) {
            setTitle("Выполненные задачи");
            toDoViewModel.getFilteredTasks(toDoViewModel.CODE_COMPLETEDTASKS);
        } else if (id == R.id.notDone_tasks_menu) {
            setTitle("Невыполненные задачи");
            toDoViewModel.getFilteredTasks(toDoViewModel.CODE_NOTCOMPLETEDTASKS);
        } else if (id == R.id.great_tasks_menu) {
            setTitle("Важные задачи");
            toDoViewModel.getFilteredTasks(toDoViewModel.CODE_GREATTASKS);
        } else if (id == R.id.notGreat_tasks_menu) {
            setTitle("Неважные задачи");
            toDoViewModel.getFilteredTasks(toDoViewModel.CODE_NOTGREATTASKS);
        } else if (id == R.id.nav_share) {

            Intent tempIntent = new Intent(MainActivity.this, LoadTaskFromWebActivity.class);
            startActivity(tempIntent);

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
