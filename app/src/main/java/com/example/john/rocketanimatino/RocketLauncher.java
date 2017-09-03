package com.example.john.rocketanimatino;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by John on 2017/8/13.
 */

public class RocketLauncher extends LinearLayout {

    /**
     * 记录火箭发射台的宽度
     */
    public static int width;

    /**
     * 记录火箭发射台的高度
     */
    public static int height;

    /**
     * 火箭发射台的背景图片
     */
    private ImageView launcherImg;
    public RocketLauncher(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.launcher, this);
        launcherImg = (ImageView) findViewById(R.id.launcher_img);
        width = launcherImg.getLayoutParams().width;
        height = launcherImg.getLayoutParams().height;
    }

    public void updateLauncherStatus(boolean isReadyToLaunch) {
        if (isReadyToLaunch) {
            launcherImg.setImageResource(R.drawable.launcher_bg_fire);
        } else {
            launcherImg.setImageResource(R.drawable.launcher_bg_hold);
        }
    }
}
