package com.example.john.rocketanimatino;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int ALERT_WINDOW_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button startFloatWindow = (Button) findViewById(R.id.start_float_window);
        startFloatWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
//                Intent intent = new Intent(MainActivity.this, FloatWindowService.class);
//                startService(intent);
//                finish();

                if(Build.VERSION.SDK_INT>=23)
                {
                    if(Settings.canDrawOverlays(MainActivity.this))
                    {
                        //有悬浮窗权限开启服务绑定 绑定权限
                        Intent intent = new Intent(MainActivity.this, FloatWindowService.class);
                        startService(intent);
                        finish();

                    }else{
                        //没有悬浮窗权限m,去开启悬浮窗权限
                        try{
                            Intent  intent=new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                            startActivityForResult(intent, ALERT_WINDOW_PERMISSION_CODE);
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                } else{
                    //默认有悬浮窗权限  但是 华为, 小米,oppo等手机会有自己的一套Android6.0以下  会有自己的一套悬浮窗权限管理 也需要做适配
                    Intent intent = new Intent(MainActivity.this, FloatWindowService.class);
                    startService(intent);
                    finish();

                }

            }
        });
    }

    /**
     * 用户返回
     */
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ALERT_WINDOW_PERMISSION_CODE) {
            if (!Settings.canDrawOverlays(MainActivity.this)) {
                Toast.makeText(MainActivity.this, "权限授予失败，无法开启悬浮窗", Toast.LENGTH_SHORT).show();
            } else {
                startService(new Intent(MainActivity.this, FloatWindowService.class));
                finish();
            }

        }
    }
}
