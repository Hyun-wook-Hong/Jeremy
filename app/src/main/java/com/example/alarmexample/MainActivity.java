package com.example.alarmexample;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    //Alarm 관련 Class Implements
    AlarmManager ALM_Manager;
    TimePicker ALM_TimePicker;
    Context context;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //알람 시작
        this.context = this;

        //알람매니저, TimePicker 설정
        ALM_Manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        ALM_TimePicker = findViewById(R.id.ALM_TimePicker);

        //Calendar 객체 생성
        final Calendar calendar  = Calendar.getInstance();
        //알람 Receiver Intent 생성
        final Intent myIntent = new Intent(this.context, AlarmReceiver.class);

        //시작 Button Click Event
        Button AlarmON = findViewById(R.id.btn_start);

        AlarmON.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                // 선언한 calendar에 시간 세팅
                calendar.set(Calendar.HOUR_OF_DAY, ALM_TimePicker.getHour());
                calendar.set(Calendar.MINUTE, ALM_TimePicker.getMinute());

                // 시간을 아래 방식으로 가져온다
                int hour = ALM_TimePicker.getHour();
                int minute = ALM_TimePicker.getMinute();

                //토스트 메세지 출력
                Toast.makeText(MainActivity.this, hour + "시 " + minute + "분에 알람이 울립니다.", Toast.LENGTH_SHORT).show();

                //Receiver에 이벤트 발생을 위한 String 값을 넘긴다.
                myIntent.putExtra("state", "alarm on");

                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);


                //알람 세팅
                ALM_Manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);


            }
        });

        //종료 Button Click Event
        Button AlarmOFF = findViewById(R.id.btn_finish);

        AlarmOFF.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                //토스트 메세지 출력
                Toast.makeText(MainActivity.this, "알람이 종료되었습니다.", Toast.LENGTH_SHORT).show();

                //활성화중인 알람 매니저 취소
                ALM_Manager.cancel(pendingIntent);
                // Receiver에 이벤트 발생을 위한 String 값을 넘긴다.
                myIntent.putExtra("state", "alarm off");

                //알람 취소 이벤트 Broadcasting
                sendBroadcast(myIntent);
            }
        });

    }

}
