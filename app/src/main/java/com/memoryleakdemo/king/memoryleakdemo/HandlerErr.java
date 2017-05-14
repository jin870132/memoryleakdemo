package com.memoryleakdemo.king.memoryleakdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class HandlerErr extends AppCompatActivity {
    Handler mhandler = new Handler();
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_err);
        tv = (TextView) findViewById(R.id.tv);
       mhandler.postDelayed(new Runnable() {
           @Override
           public void run() {
               SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd    hh:mm:ss");
               String date = sDateFormat.format(new Date());
               tv.setText(date);
               mhandler.postDelayed(this,1000);
           }
       },1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除当前handler发送的请求
        mhandler.removeCallbacksAndMessages(null);
    }
}
