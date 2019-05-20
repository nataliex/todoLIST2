package com.example.todolist;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity implements Changer{

    //  Слушатель для элементов списка в выдвижной панели
    //todo убрать вообще весь класс
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            AdapterFragment adapterFragment = new AdapterFragment();
            Bundle bundle = new Bundle();
            switch (position){
                case 0:
                    bundle.putInt("key", -1);
                    break;
                case 1:
                    bundle.putInt("key", -2);
                    break;
                case 2:
                    bundle.putInt("key", -3);
                    break;
                case 3:
                    bundle.putInt("key", -4);
                    break;
                case 4:
                    bundle.putInt("key", -5);
                    break;
            }
            adapterFragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            Fragment f = fragmentManager.findFragmentById(R.id.conteiner_adapter);

            Log.d("TAG", "TRANSACTION");
            transaction.replace(R.id.conteiner_adapter, adapterFragment);
            transaction.commit();

            Toast.makeText(getApplicationContext(),
                    "Выбран пункт " + position, Toast.LENGTH_SHORT).show();
        }
    }

    public static final int FRAGMENT_TASK = 0;
    public static final int EDIT_TASK_FRAGMENT = 1;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView mDrawerListView = findViewById(R.id.left_drawer);
        ArrayList<String> Names = new ArrayList<>();
        Names.add("Корневые");
        Names.add("Выполненные");
        Names.add("Невыполненные");
        Names.add("Важные");
        Names.add("Неважные");
        mDrawerListView.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, Names));

        mDrawerListView.setOnItemClickListener(new DrawerItemClickListener());

        // подключим адаптер для списка
       // mDrawerListView.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, Names));
       // DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        //Toolbar toolbar = findViewById(R.id.toolbar);

       /* ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 1);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();*/
        AdapterFragment adapterFragment = new AdapterFragment(); //TODO убрать adapterFragment, bundle, putInt, setArguments
        Bundle bundle = new Bundle();
        bundle.putInt("key", -1);
        adapterFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment f = fragmentManager.findFragmentById(R.id.conteiner_adapter);

            Log.d("TAG", "TRANSACTION");
            transaction.replace(R.id.conteiner_adapter, adapterFragment); //todo сделать new AdapterFragment
            transaction.commit();
    }

    @Override
    public void changeFragment(int i, int typeFragment) {

        Fragment newFragment;

        switch (typeFragment){
            case FRAGMENT_TASK:
                newFragment = new FragmentTask();
                ((FragmentTask) newFragment).setTask(i);
                break;
            case EDIT_TASK_FRAGMENT:
                newFragment = new EditTaskFragment();
                ((EditTaskFragment) newFragment).setTask(i);
                break;
                default: newFragment = null;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.conteiner_adapter, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
