package com.example.mynote.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mynote.R;
import com.example.mynote.util.StatusBarUtil;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        //调用工具类，使得状态栏字体变黑
        StatusBarUtil.setStatusBarLightMode(getWindow());
    }
}
