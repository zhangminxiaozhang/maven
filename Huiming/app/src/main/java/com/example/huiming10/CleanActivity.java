package com.example.huiming10;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;


import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
//冲洗功能
public class CleanActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "CleanActivity";

    private Button t_clean, female_clean, dry, adjust, clean_power, clean_stop;
    private Toolbar toolbar;
    private static final byte[] T_VALUE = {0x0c,0x02,0x04,0x00,0x12};
    private static final byte[] FEMALE_VALUE = {0x0c,0x02,0x06,0x00,0x14};
    private static final byte[] DRY_VALUE = {0x0c,0x02,0x05,0x00,0x13};
    private static final byte[] ADJUST_VALUE = {0x0c,0x02,0x03,0x00,0x11};
    private static final byte[] POWER_VALUE = {0x0c,0x02,0x01,0x00,0x0F};
    private static final byte[] STOP_VALUE= {0x0c,0x02,0x02,0x00,0x10};
    //    声明一个soundpool，音乐
    private SoundPool sp;
    private MediaPlayer mediaPlayer=null;
    private int music;
    private UDPClient udpClient;
    private String IP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean);
        initView();
        udpClient = UDPClient.getUdpClient();
        IP = staticValue.getIp();


    }

    private void initView(){
        toolbar = findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setTitle("冲洗");
        setSupportActionBar(toolbar);

        t_clean = findViewById(R.id.t_clean);
        female_clean = findViewById(R.id.female_clean);
        dry = findViewById(R.id.dry);
        adjust = findViewById(R.id.adjust);
        clean_power = findViewById(R.id.clean_power);
        clean_stop = findViewById(R.id.clean_stop);

        t_clean.setOnClickListener(CleanActivity.this);
        female_clean.setOnClickListener(CleanActivity.this);
        dry.setOnClickListener(CleanActivity.this);
        adjust.setOnClickListener(CleanActivity.this);
        clean_power.setOnClickListener(CleanActivity.this);
        clean_stop.setOnClickListener(CleanActivity.this);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            sp=new SoundPool.Builder().setMaxStreams(10).build();
        }else{
            sp=new SoundPool(10,AudioManager.STREAM_MUSIC,1);
        }
        music=sp.load(this,R.raw.music,1);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.t_clean:
                udpClient.send_data(T_VALUE);
                Log.d(TAG,"t_clean");
                sp.play(music,1,1,0,0,1);
                break;
            case R.id.female_clean:
                udpClient.send_data(FEMALE_VALUE);
                Log.d(TAG,"female_clean");
                sp.play(music,1,1,0,0,1);
                break;
            case R.id.dry:
                udpClient.send_data(DRY_VALUE);
                Log.d(TAG,"DRY_VALUE");
                sp.play(music,1,1,0,0,1);
                break;
            case R.id.adjust:
                udpClient.send_data(ADJUST_VALUE);
                Log.d(TAG,"ADJUST_VALUE");
                sp.play(music,1,1,0,0,1);
                break;
            case R.id.clean_power:
                udpClient.send_data(POWER_VALUE);
                Log.d(TAG,"POWER_VALUE");
                sp.play(music,1,1,0,0,1);
                break;
            case R.id.clean_stop:
                udpClient.send_data(STOP_VALUE);
                Log.d(TAG,"STOP_VALUE");
                sp.play(music,1,1,0,0,1);
                break;
            default:break;
        }
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ");
        AlertDialog.Builder dialog = new AlertDialog.Builder(CleanActivity.this);
        dialog.setTitle("家居床");
        dialog.setMessage("确认要离开么");
        dialog.setCancelable(false);
        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IP = staticValue.getIp();
    }
}
