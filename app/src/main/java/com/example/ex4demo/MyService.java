package com.example.ex4demo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    private int count=0;
    private boolean quit=false;
    private Timer timer;
    private TimerTask timerTask;
    private Handler handler = new MessageHandler();

    private MyServiceBinder myServiceBinder=new MyServiceBinder();
    public MyService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return myServiceBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //创建新 Thread，如果 service 启动每秒钟 count++，如果 quit 为真则退出
        startTime();
    }

    class MyServiceBinder extends Binder {
        public int getCount(){
            return MyService.this.count;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        quit=true;
    }
    private final class MessageHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            // timeViewText.setText(currTime);
            // 显示当前时间
            count++;
            startTime();
        }
    }

    private void startTime() {

        timer = new Timer();
        timerTask = new TimerTask() {

            @Override
            public void run() {

                Log.v("info", "currCount:" + count);

                if (count >=100) {
                    timer.cancel();
                    return;
                }


                // 发送消息，传送当前时间
                Message message = handler.obtainMessage();
                handler.sendMessage(message);
            }

        };

        timer.schedule(timerTask, 1000);

    }
}

