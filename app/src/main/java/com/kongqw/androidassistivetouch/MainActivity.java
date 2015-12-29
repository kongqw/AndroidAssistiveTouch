package com.kongqw.androidassistivetouch;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kongqw.androidassistivetouch.view.AssistiveTouch;

public class MainActivity extends AppCompatActivity {

    private static AssistiveTouch.MyBinder mSpeechServiceBinder;
    private MyServiceConnection myServiceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // 开启迷你模式的服务
        Intent miniModeService = new Intent(this, AssistiveTouch.class);
        myServiceConnection = new MyServiceConnection();
        bindService(miniModeService, myServiceConnection, Context.BIND_AUTO_CREATE);
        startService(miniModeService);

    }

    @Override
    protected void onDestroy() {
        unbindService(myServiceConnection);
        // 关闭迷你模式
        if (null != mSpeechServiceBinder) {
            mSpeechServiceBinder.hideMiniMode();
        }
        super.onDestroy();
    }

    /**
     * 绑定服务的回调
     *
     * @author kongqw
     */
    public class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mSpeechServiceBinder = (AssistiveTouch.MyBinder) service;
            mSpeechServiceBinder.showMiniMode();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

    }
}
