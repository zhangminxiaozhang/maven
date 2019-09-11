package com.example.huiming10;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class LoginActivity  extends AppCompatActivity {
//    定义打印日志用到的标签
    private static final String TAG = "LoginActivity";
//    定义需要请求的列表
    private List<String> needPermission;
//    定义一个请求码 唯一即可
    private final int REQUEST_CODE_PERMISSION = 0;
//    定义需要请求权限的数组
    private String[] permissionArray = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        设置全屏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        permission_ask();
    }

    //        权限请求函数
    private void permission_ask(){
        needPermission=new ArrayList<>();
        for(String permissionName:permissionArray){
            if(!checkIsAskPermission(this,permissionName)){
                needPermission.add(permissionName);
            }
        }
        if(needPermission.size()>0){
//            如果要求权限的数组大于0，那么系统就开始索要权限
            ActivityCompat.requestPermissions(this,needPermission.toArray(new String[needPermission.size()]),REQUEST_CODE_PERMISSION);
        }else{
            begin();
        }
    }

//    用户选择完权限申请后，回调此函数
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case REQUEST_CODE_PERMISSION:
                Map<String,Integer> permissionMap=new HashMap<>();
                for(String name:needPermission){
                    permissionMap.put(name,PackageManager.PERMISSION_GRANTED);

                }
                for(int i=0;i<permissions.length;i++){
                    permissionMap.put(permissions[i],grantResults[i]);
                }
                if(checkIsAskPermissionState(permissionMap,permissions)){
                    begin();
                }else{
                    Toast.makeText(this,"请开启全部权限后使用",Toast.LENGTH_SHORT).show();
                    begin();
                }
        }
    }

    //定义一个检查权限是否被系统授权了的函数
    private boolean checkIsAskPermission(Context context, String permission){
        if(ContextCompat.checkSelfPermission(this,permission)!=PackageManager.PERMISSION_GRANTED){
            return false;
        }else{
            return true;
        }
    }
//    定义一个检查权限是否被用户授权了的函数
    private boolean checkIsAskPermissionState(Map<String,Integer> maps,String []list){
        for(int i=0;i<list.length;i++){
            if(maps.get(list[i])!= PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }
//开始进入Tab活动
    private void begin(){
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                Intent intent=new Intent(LoginActivity.this, TabActivity.class);
                startActivity(intent);
                finish();
            }
        };
        Timer timer=new Timer();
        timer.schedule(timerTask,2000);

    }

}
