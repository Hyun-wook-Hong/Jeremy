package com.example.alarmexample;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * date: 2020.03.03
 * module name: AlarmReceiver
 * author: Jeremy Hong
 * description: Executing alarm service
 * **/

public class RingtonePlayingService extends Service {

    MediaPlayer mediaPlayer;
    int startID;
    boolean IS_RUNNING;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //onCreate
    public void onCreate(){
        super.onCreate();
        //onCreate 시점에서 version check
        if(Build.VERSION.SDK_INT >= 26){
            String Channel_ID = "default";
            NotificationChannel channel
                    = new NotificationChannel(Channel_ID,
                    "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                    .createNotificationChannel(channel);

            Notification notification = new Notification.Builder(this, Channel_ID)
            .setContentTitle("알람 시작")
            .setContentText("알람음이 재생됩니다.")
            .setSmallIcon(R.mipmap.ic_launcher)

            .build();

            startForeground(1, notification);
        }
    }

    //onStartCommand
    public int onStartCommand(Intent intent, int flags, int startID){
        String getState = intent.getExtras().getString("state");

        assert getState != null;
        //상태에 따라 알람 flag를 결정함
        //1: Ringing alarm 0: default, no ringing
        switch(getState){
            case "alarm on":
                startID = 1;
                break;
            case "alarm off":
                startID = 0;
                break;
            default:
                startID = 0;
                break;
        }

        // 알람은 개짖는 소리로 다운해와서 테스트 함

        // 알람음 재생중은 X, 알람음 시작 클릭했을 경우
        if(!this.IS_RUNNING && startID == 1){
            mediaPlayer = MediaPlayer.create(this, R.raw.barking);
            mediaPlayer.start();

            this.IS_RUNNING = true;
            this.startID = 0;
        }

        // 알람은 재생중 O, 알람음 버튼 종료 클릭했을 경우
        else if(this.IS_RUNNING && startID == 0){
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();

            this.IS_RUNNING = false;
            this.startID = 0;
        }

        // 알람은 재생중 X, 알람음 버튼 종료 클릭했을 경우
        else if(!this.IS_RUNNING && startID == 0){
            this.IS_RUNNING = false;
            this.startID = 0;
        }

        // 알람은 재생중 O, 알람음 시작 클릭했을 경우
        else if(!this.IS_RUNNING && startID == 0){
            this.IS_RUNNING = true;
            this.startID = 1;
        }
        else{

        }
        return START_NOT_STICKY;
    }


    //onDestroy, 이 메서드가 실행되면 액티비티가 죽음
    public void onDestroy(){
        super.onDestroy();
        Log.d("onDestory() 실행", "서비스 파괴");
    }
}
