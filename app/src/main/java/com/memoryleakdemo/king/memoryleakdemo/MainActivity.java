package com.memoryleakdemo.king.memoryleakdemo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv1).setOnClickListener(this);
        findViewById(R.id.tv2).setOnClickListener(this);
        findViewById(R.id.tv3).setOnClickListener(this);
        findViewById(R.id.tv4).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv1:
                startActivity(new Intent(this,ThreadErr.class));
                break;
            case R.id.tv2:
                startActivity(new Intent(this,HandlerAndTimerErr.class));
                break;
            case R.id.tv3:
                startActivity(new Intent(this,HandlerErr.class));
                break;
            case R.id.tv4:
                startActivity(new Intent(this,ContextErr.class));
                break;
        }
    }
}
