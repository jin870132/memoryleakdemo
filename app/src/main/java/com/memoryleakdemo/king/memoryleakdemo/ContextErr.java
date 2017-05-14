package com.memoryleakdemo.king.memoryleakdemo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.memoryleakdemo.king.memoryleakdemo.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ContextErr extends AppCompatActivity {
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_err);
        tv = (TextView) findViewById(R.id.tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd    hh:mm:ss");
                String date = sDateFormat.format(new Date());
                ToastUtils.ShowToast(ContextErr.this,date);
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除当前handler发送的请求
    }
}
