package com.memoryleakdemo.king.memoryleakdemo;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

public class ThreadErr extends AppCompatActivity {

    private TextView tv;
    private boolean running;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_err);
        running = true;
        tv = (TextView) findViewById(R.id.tv);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (running) {
                    SystemClock.sleep(1000);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd    hh:mm:ss");
                            String date = sDateFormat.format(new Date());
                            tv.setText(date);
                        }
                    });

                }

            }
        }).start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        running = false;
    }
}
