package com.example.ex4demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public final static String TAG="MainActivity";
    Button btnBindService,btnUnbindService,btnGetStatus;
    TextView tvServiceStatus;
    MyService.MyServiceBinder serviceBinder;
    boolean isServiceBind=false;
    ServiceConnection conn=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
//返回 Service 的 Binder 对象
            serviceBinder  = (MyService.MyServiceBinder) service;
           Log.i(TAG, "onServiceConnected");

            isServiceBind=true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            Log.e(TAG, "onServiceDisconnected");
            isServiceBind = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnBindService=findViewById(R.id.btn_main_activity_bind_service);
        btnUnbindService=findViewById(R.id.btn_main_activity_unbind_service);
        btnGetStatus=findViewById(R.id.btn_main_activity_get_status);
        tvServiceStatus=findViewById(R.id.tv_main_activity_service_status);
        btnBindService.setOnClickListener(this);
        btnUnbindService.setOnClickListener(this);
        btnGetStatus.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent();
        intent.setClass(MainActivity.this,MyService.class);
        switch (v.getId()){
            case R.id.btn_main_activity_bind_service:
//如果 service 尚未绑定就绑定，如果已经绑定则忽略
                if(!isServiceBind) {
                    bindService(intent, conn, BIND_AUTO_CREATE);
                }
                break;
            case R.id.btn_main_activity_get_status:
//如果 service 已经绑定，获取 service
                if(isServiceBind) {
                    tvServiceStatus.setText("当前服务状态："+serviceBinder.getCount());
                }
                break;
            case R.id.btn_main_activity_unbind_service:
//如果 service 已经绑定，则可以解绑，否则忽略
                if(isServiceBind) {
                    unbindService(conn);
                }
                break;
        }
    }

}