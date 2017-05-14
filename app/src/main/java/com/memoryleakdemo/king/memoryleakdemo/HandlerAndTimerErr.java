package com.memoryleakdemo.king.memoryleakdemo;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static android.R.attr.data;
import static android.R.attr.track;
import static com.memoryleakdemo.king.memoryleakdemo.R.id.tv;

public class HandlerAndTimerErr extends AppCompatActivity {
    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
                tv.setText(msg.obj.toString());
        }
    };
    private TextView tv;
    private boolean runnalbe;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_err);
        tv = (TextView) findViewById(R.id.tv);
        runnalbe = true;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(runnalbe) {
                    SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd    hh:mm:ss");
                    String date = sDateFormat.format(new Date());
                    Message message = new Message();
                    message.obj = date;
                    mhandler.sendMessage(message);
                }
            }
        }, 1, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
