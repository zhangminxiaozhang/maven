package com.example.huiming10;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
//设置活动
public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
//    mSwipeLayout可以对页面进行往下滑动
    private SwipeRefreshLayout mSwipeLayout;
    private  static  final String TAG="SettingActivity";
    private EditText user_,password_;
    private CheckBox remember_user;
    private Button re_,scan;
    private ProgressBar progress_bar;
    private Toolbar toolbar;
    private ListView mlistview;
    private WifiAdmin wifiAdmin;
    private LocationManager locationManager;
    private WifiManager wifiManager;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private List<ScanResult> mwifilist;

    private String IP;
    private String ssid;
    private Boolean RE_STATE = false;
    private boolean result = false;

    private  int reLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initview();
        initBroadReceiver();
//        重新写下拉刷新的方法
        mSwipeLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimary,R.color.colorPrimaryDark);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                final Random random=new Random();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        wifiAdmin.startScan(SettingActivity.this);
                        Toast.makeText(SettingActivity.this,"刷新完成", Toast.LENGTH_SHORT).show();
                        mSwipeLayout.setRefreshing(false);
                    }
                },2000);

            }
        });
    }

    private void initview(){
        wifiAdmin = new WifiAdmin(SettingActivity.this);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        progress_bar = findViewById(R.id.progress_bar);
        re_ = findViewById(R.id.re_);
        scan = findViewById(R.id.scan);
        //使用实现接口的方式来进行注册
        re_.setOnClickListener(SettingActivity.this);
        scan.setOnClickListener(SettingActivity.this);
//        扫描出来的wifi列表放在mlistview中
        mlistview = findViewById(R.id.mlistview);
        mSwipeLayout=findViewById(R.id.srl);
        pref = getSharedPreferences("user", MODE_PRIVATE);
        remember_user = findViewById(R.id.remember_user);
        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("设置");
        user_ = findViewById(R.id.user_);
        password_ = findViewById(R.id.password_);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用SharedPreferences对象的edit方法来获取一个SharedPreferences.Editor对象
                editor = pref.edit();
                if(remember_user.isChecked()){
                    editor.putBoolean("remember_user", true);
                    editor.putBoolean("RE_STATE", RE_STATE);
                    editor.putString("user", user_.getText().toString());
                }else {
                    editor.clear();
                }
                editor.apply();
                Intent intent = new Intent();
                intent.putExtra("result",result);
                setResult(1,intent);
                SettingActivity.this.finish();
            }
        });
        //取出 存在pref里面的remember_user的值，默认是false
        boolean isRemember = pref.getBoolean("remember_user" ,false);
        if(isRemember){
            String user = pref.getString("user", "");
            user_.setText(user);
            remember_user.setChecked(true);
        }

        RE_STATE = pref.getBoolean("RE_STATE", false);
        if(!RE_STATE){
            re_.setText("连接");
        }else {
            re_.setText("连接");
        }

        //ListView的点击事件
        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ssid = mwifilist.get(position).SSID;
                user_.setText(ssid);
            }
        });
    }
    private void initBroadReceiver(){
        IntentFilter intentFilter = new IntentFilter();
        //当wifi状态发生变化时会发出一条值为WifiManager.SCAN_RESULTS_AVAILABLE_ACTION
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.re_:
                progress_bar.setVisibility(View.VISIBLE);
                reLink=1;
                if(user_.length()==0||password_.length()==0){
                    progress_bar.setVisibility(View.GONE);
                    Toast.makeText(this,"请输入正确的用户名或密码后重新连接",Toast.LENGTH_SHORT).show();
                }
                else{
                    re_.setText("连接中...");
                    send();
                }
                break;
            case R.id.scan:
                if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)&&wifiManager.isWifiEnabled()){
                    progress_bar.setVisibility(View.VISIBLE);
                    wifiAdmin.startScan(SettingActivity.this);
                    scan.setText("扫描中");
                }else if(wifiManager.isWifiEnabled()){
                    Toast.makeText(this,"请打开GPS",Toast.LENGTH_SHORT).show();
                }else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    Toast.makeText(this,"请打开wifi",Toast.LENGTH_SHORT).show();
                }
                break;
                default:break;
        }
    }

//    退出方法
    @Override
    public void onBackPressed() {
        reLink = 5;
        editor = pref.edit();
        if(remember_user.isChecked()){
            editor.putBoolean("remember_user", true);
            editor.putString("user", user_.getText().toString());
            editor.putBoolean("RE_STATE", RE_STATE);
        }else {
            editor.clear();
        }
        editor.apply();
        Intent intent = new Intent();
        intent.putExtra("result",result);
        setResult(1,intent);
        SettingActivity.this.finish();
        finish();
    }

    private void send(){
        String SSID = user_.getText().toString();
        String PASSWORD = password_.getText().toString();
        MediaType JSON = MediaType.parse("application/json;charset=UTF-8");
        OkHttpClient client = new OkHttpClient.Builder()
                .writeTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .connectTimeout(3, TimeUnit.SECONDS)
                .build();
        final String jsonString = "{\n"+"\"sta\":{\"ssid\":\""+ SSID + "\",\"password\":\"" + PASSWORD + "\"},\"mode\":\"apsta\"\n"+"}";
        RequestBody requestBody = RequestBody.create(JSON, jsonString);
        Request request = new Request.Builder()
                .url("http://192.168.4.1/config?cmd=wifi")
                .header("Content-Type", "text/plain;charset=UTF-8")
                .put(requestBody)
                .post(requestBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Message message = Message.obtain();
                message.what = 1;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message message = Message.obtain();
                message.what = 2;
                handler.sendMessage(message);
            }
        });
    }

    private void Get_IP(){
        OkHttpClient client = new OkHttpClient.Builder()
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url("http://192.168.4.1/config?cmd=wifi")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Message message = Message.obtain();
                message.what = 3;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                IP = parseJSONWithJSONObjevt(responseBody);
                staticValue.setIp(IP);
                Message message = Message.obtain();
                message.what = 4;
                handler.sendMessage(message);
            }
        });
    }

    private final BroadcastReceiver mReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            progress_bar.setVisibility(View.GONE);
            mwifilist=wifiAdmin.getWifiList();
            if(mwifilist!=null){
                scan.setText("扫描");
                MyAdapter adapter=new MyAdapter(SettingActivity.this,mwifilist);
                mlistview.setAdapter(adapter);
            }
        }
    };

    private String parseJSONWithJSONObjevt(String jsonData){
        String IP_;
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONObject stationObject = jsonObject.getJSONObject("sta");
            IP_ = stationObject.getString("ip");
        }catch (Exception e){
            e.printStackTrace();
            IP_ = "0.0.0.0";
        }
        return IP_;
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    progress_bar.setVisibility(View.GONE);
                    Toast.makeText(SettingActivity.this, "设置超时，请确认连接安信可网络", Toast.LENGTH_SHORT).show();
                    re_.setText("连接");
                    break;
                case 2:
                    Toast.makeText(SettingActivity.this, "设置成功, 开始连接", Toast.LENGTH_SHORT).show();
                    RE_STATE = true;
                    Get_IP();
                    re_.setText("连接中");
                    break;
                case 3:
                    if (reLink < 5){
                        Toast.makeText(SettingActivity.this, "连接超时，正在重连第"+ reLink +"次", Toast.LENGTH_SHORT).show();
                        Get_IP();
                        reLink++;
                    }else {
                        Toast.makeText(SettingActivity.this, "连接失败，请检查网络", Toast.LENGTH_SHORT).show();
                        re_.setText("连接");
                        progress_bar.setVisibility(View.GONE);
                    }
                    break;
                case 4:
                    if(IP.equals("0.0.0.0")){
                        Get_IP();
                    }else {
                        editor = getSharedPreferences("data_", MODE_PRIVATE).edit();
                        editor.putString("IP", IP);
                        editor.apply();
                        progress_bar.setVisibility(View.GONE);
                        Toast.makeText(SettingActivity.this, "已成功连接" , Toast.LENGTH_LONG).show();
                        result = true;
                        re_.setText("连接");
                        RE_STATE = false;
                    }
                    break;
                default:break;
            }
        }
    };

    //动态注册的广播接收器一定要取消注册才行
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

}
