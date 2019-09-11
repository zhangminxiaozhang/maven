package com.example.huiming10;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class helpActivity extends AppCompatActivity {
    private TextView help,pron;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Toolbar toolbar=findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("帮助与声明");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        help = findViewById(R.id.help);
        pron=findViewById(R.id.pron);
        help.setText(Html.fromHtml("&emsp;1.确定给予所有权限后，连接安信可网络。<br/>" +
                "&emsp;2.扫描获取当前wifi名称后填入密码进行连接，待出现连接成功提示后即可使用。<br/>" +
                "&emsp;3.过程中请保证连接安信可网络，如果超时或断连提示请点击重试，最终出现连接成功提示后即可使用<br/>" +
                "&emsp;4.若确认护理床已经成功连接wifi,则如果其他远程设备需要连接，可以不输入密码，在安信可网络中直接点击连接即可完成连接"));
        pron.setText(Html.fromHtml("&emsp;1.<br/>" +
                "&emsp;2.<br/>" +
                "&emsp;3.<br/>"));
    }
}
