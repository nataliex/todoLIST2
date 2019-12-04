package com.example.proba;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoadTaskFromWebActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_task_from_web_activity);

        final EditText et_load_task_name = findViewById(R.id.et_name_load_task);
        FloatingActionButton btn_load = findViewById(R.id.btn_load);
        btn_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NetworkViewModel networkViewModel = new NetworkViewModel(getApplication());
                networkViewModel.getIfExistsTree(et_load_task_name.getText().toString());
                LoadTaskFromWebActivity.super.finish();
            }
        });
    }
}
