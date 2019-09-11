package com.example.huiming10;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//主活动的设置
public class MainActivity extends AppCompatActivity implements View.OnTouchListener{
    private  static final String TAG="MainActivity";
//   定义8个按钮
    private Button back_up,back_down,left_re,right_re,leg_up,leg_down,both_up,both_down,gai_up,gai_down,bian_up,bian_down;
//    定义一个toolbar
    private Toolbar toolbar;
//    定义一个udpClient对象去发送信号
//    声明一个soundpool，音乐
    private SoundPool sp;
    private MediaPlayer mediaPlayer=null;
    private int music;
    private  UDPClient udpClient;
    private staticValue staticValue;
    private String IP="0.0.0.0";
//    8种信号的字节码
    private static final byte[] BACK_UP = {0x0c,0x01,0x01,0x01,0x0E};
    private static final byte[] BACK_DOWN = {0x0c,0x01,0x02,0x01,0x0F};
    private static final byte[] LEG_UP = {0x0c,0x01,0x03,0x01,0x10};
    private static final byte[] LEG_DOWN = {0x0c,0x01,0x04,0x01,0x11};
    private static final byte[] BOTH_UP = {0x0c,0x01,0x05,0x01,0x12};
    private static final byte[] BOTH_DOWN = {0x0c,0x01,0x06,0x01,0x13};
    private static final byte[] LEFT_RE={0x0c,0x01,0x07,0x01,0x14};
    private static final byte[] RIGHT_RE={0x0c,0x01,0x08,0x01,0x15};
    private static final byte[] GAI_UP={0x0c,0x01,0x09,0x01,0x16};
    private static final byte[] GAI_DOWN={0x0c,0x01,0x0A,0x01,0x17};
    private static final byte[] BIAN_UP={0x0c,0x01,0x0B,0x01,0x18};
    private static final byte[] BIAN_DOWN={0x0c,0x01,0x0C,0x01,0x19};
    private static final byte[] STOP_VALUE = {0x0c,0x03,0x01,0x01,0x10};
    private static final byte[] RST_VALUE = {0x52,0x53,0x54};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences pref=getSharedPreferences("data_",MODE_PRIVATE);
        IP=pref.getString("IP","0.0.0.0");
        initView();
        udpClient=UDPClient.getUdpClient();
//        这里加上了版本判断，否则如果直接写sp的话就会出错
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            sp=new SoundPool.Builder().setMaxStreams(10).build();
        }else{
            sp=new SoundPool(10,AudioManager.STREAM_MUSIC,1);
        }
        music=sp.load(this,R.raw.music,1);
    }
    private void initView(){
        back_up=findViewById(R.id.back_up);
        back_down=findViewById(R.id.back_down);
        leg_up=findViewById(R.id.leg_up);
        leg_down=findViewById(R.id.leg_down);
        both_up=findViewById(R.id.both_up);
        both_down=findViewById(R.id.both_down);
        left_re=findViewById(R.id.left_re);
        right_re=findViewById(R.id.right_re);
        gai_up=findViewById(R.id.gai_up);
        gai_down=findViewById(R.id.gai_down);
        bian_up=findViewById(R.id.bian_up);
        bian_down=findViewById(R.id.bian_down);

        toolbar=findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setTitle("慧明智能床");
        setSupportActionBar(toolbar);

        back_up.setOnTouchListener(MainActivity.this);
        back_down.setOnTouchListener(MainActivity.this);
        leg_up.setOnTouchListener(MainActivity.this);
        leg_down.setOnTouchListener(MainActivity.this);
        both_up.setOnTouchListener(MainActivity.this);
        both_down.setOnTouchListener(MainActivity.this);
        left_re.setOnTouchListener(MainActivity.this);
        right_re.setOnTouchListener(MainActivity.this);
        gai_up.setOnTouchListener(MainActivity.this);
        gai_down.setOnTouchListener(MainActivity.this);
        bian_up.setOnTouchListener(MainActivity.this);
        bian_down.setOnTouchListener(MainActivity.this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event){
        switch (v.getId()){
            case R.id.back_up:
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        udpClient.send_data(BACK_UP);
                        Log.d(TAG,"back_up");
                        sp.play(music,1,1,0,0,1);
                        break;
                    case MotionEvent.ACTION_UP:
                        udpClient.send_data(STOP_VALUE);
                        break;
                }
                break;
            case R.id.back_down:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(TAG, "back_down ");
                        udpClient.send_data(BACK_DOWN);
                        sp.play(music,1,1,0,0,1);
                        break;
                    case MotionEvent.ACTION_UP:
                        udpClient.send_data(STOP_VALUE);
                        break;
                }
                break;
            case R.id.leg_up:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        udpClient.send_data(LEG_UP);
                        sp.play(music,1,1,0,0,1);
                        Log.d(TAG,"leg_up");
                        break;
                    case MotionEvent.ACTION_UP:
                        udpClient.send_data(STOP_VALUE);
                        break;
                }
                break;
            case R.id.leg_down:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        udpClient.send_data(LEG_DOWN);
                        sp.play(music,1,1,0,0,1);
                       Log.d(TAG,"leg_down");
                        break;
                    case MotionEvent.ACTION_UP:
                        udpClient.send_data(STOP_VALUE);
                        break;
                }
                break;
            case R.id.both_up:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        udpClient.send_data(BOTH_UP);
                        sp.play(music,1,1,0,0,1);
                        Log.d(TAG,"both_up");
                        break;
                    case MotionEvent.ACTION_UP:
                        udpClient.send_data(STOP_VALUE);
                        break;
                }
                break;
            case R.id.both_down:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        udpClient.send_data(BOTH_DOWN);
                        sp.play(music,1,1,0,0,1);
                        Log.d(TAG,"both_down");
                        break;
                    case MotionEvent.ACTION_UP:
                        udpClient.send_data(STOP_VALUE);
                        break;
                }
                break;
            case R.id.left_re:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        udpClient.send_data(LEFT_RE);
                        sp.play(music,1,1,0,0,1);
                        Log.d(TAG,"LEFT_RE");
                        break;
                    case MotionEvent.ACTION_UP:
                        udpClient.send_data(STOP_VALUE);
                        break;
                }
                break;
            case R.id.right_re:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        udpClient.send_data(RIGHT_RE);
                        sp.play(music,1,1,0,0,1);
                        Log.d(TAG,"RIGHT_RE");
                        break;
                    case MotionEvent.ACTION_UP:
                        udpClient.send_data(STOP_VALUE);
                        break;
                }
                break;
            case R.id.gai_up:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        udpClient.send_data(GAI_UP);
                        sp.play(music,1,1,0,0,1);
                        Log.d(TAG,"GAI_UP");
                        break;
                    case MotionEvent.ACTION_UP:
                        udpClient.send_data(STOP_VALUE);
                        break;
                }
                break;

            case R.id.gai_down:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        udpClient.send_data(GAI_DOWN);
                        sp.play(music,1,1,0,0,1);
                        Log.d(TAG,"GAI_DOWN");
                        break;
                    case MotionEvent.ACTION_UP:
                        udpClient.send_data(STOP_VALUE);
                        break;
                }
                break;

            case R.id.bian_up:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        udpClient.send_data(BIAN_UP);
                        sp.play(music,1,1,0,0,1);
                        Log.d(TAG,"BIAN_UP");
                        break;
                    case MotionEvent.ACTION_UP:
                        udpClient.send_data(STOP_VALUE);
                        break;
                }
                break;

            case R.id.bian_down:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        udpClient.send_data(BIAN_DOWN);
                        sp.play(music,1,1,0,0,1);
                        Log.d(TAG,"BIAN_DOWN");
                        break;
                    case MotionEvent.ACTION_UP:
                        udpClient.send_data(STOP_VALUE);
                        break;
                }
                break;
        }
        return false;
    }


//每一个活动都捆绑了一个Menu，要想定义和使用Menu，必须重写onCreateOptionsMenu，onOptionsItemSelected方法
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        inflate的作用就是讲一个用xml定义的布局文件查找并返回出来，返回的父view是menu
        getMenuInflater().inflate(R.menu.setting_menu,menu);
        return true;
}


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
//                为了返回设置活动的结果所以使用startActivityForResult
                startActivityForResult(intent, 1);
                break;
            case R.id.help:
                Intent intent1 = new Intent(MainActivity.this, helpActivity.class);
                startActivity(intent1);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case 1:
                boolean result=data.getExtras().getBoolean("result");
                if(result){
                    udpClient.send_data(RST_VALUE);
                }

        }
    }
//退出方法
    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("护理床");
        dialog.setMessage("确认要离开吗？");
        dialog.setCancelable(false);
        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }

 /*   private String getHostIP(){
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            //wifiManager.setWifiEnabled(true);
            Toast.makeText(MainActivity.this, "请开启wifi", Toast.LENGTH_SHORT).show();
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        return (ipAddress & 0xFF) + "." +
                ((ipAddress >> 8) & 0xFF) + "." +
                ((ipAddress >> 16) & 0xFF) + "." +
                (ipAddress >> 24 & 0xFF);
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IP = staticValue.getIp();
    }
}
