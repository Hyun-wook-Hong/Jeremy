package com.example.alarmexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

/**
 * date: 2020.03.03
 * module name: AlarmReceiver
 * author: Jeremy Hong
 * description: Broadcasting alarm events,
 *             메인 Class에서 조건에따라 실행되면 RingtonePlayingService를 실행하는 역할을 한다.
 * **/

public class AlarmReceiver extends BroadcastReceiver {
    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        //intent로부터 전달 받은 String
        String getMyString = intent.getExtras().getString("state");

        //RingtonePlayingService 서비스 intent 생성
        //RingtonePlayingService RPS = new RingtonePlayingService();
        Intent serviceIntent = new Intent(context, RingtonePlayingService.class);

        serviceIntent.putExtra("state", getMyString);

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            this.context.startForegroundService(serviceIntent);
        }else{
            this.context.startService(serviceIntent);
        }

    }
}
