package com.example.john.rocketanimatino;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by John on 2017/8/13.
 */

public class MyWindowManager {

    /**
     * 小悬浮窗View的实例
     */
    private static FloatWindowSmallView smallWindow;

    /**
     * 大悬浮窗View的实例
     */
    private static FloatWindowBigView bigWindow;

    /**
     * 用于控制在屏幕上添加或移除悬浮窗
     */
    private static WindowManager mWindowManager;

    /**
     * 小悬浮窗View的参数
     */
    private static WindowManager.LayoutParams smallWindowParams;

    /**
     * 用于获取手机可用内存
     */
    private static ActivityManager mActivityManager;

    /**
     * 火箭发射台的参数
     */
    private static WindowManager.LayoutParams launcherParams;

    /**
     * 火箭发射台的实例
     */
    private static RocketLauncher rocketLauncher;
    /**
     * 是否有悬浮窗(包括小悬浮窗和大悬浮窗)显示在屏幕上。
     *
     * @return 有悬浮窗显示在桌面上返回true，没有的话返回false。
     */
    public static boolean isWindowShowing() {
        return smallWindow != null || bigWindow != null;
    }

    public static void createSmallWindow(Context context) {
        WindowManager windowManager = getWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if (smallWindow == null) {
            smallWindow = new FloatWindowSmallView(context);
            if (smallWindowParams == null) {
                smallWindowParams = new WindowManager.LayoutParams();
                smallWindowParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
                smallWindowParams.format = PixelFormat.RGBA_8888;
                smallWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                smallWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
                smallWindowParams.width = FloatWindowSmallView.windowViewWidth;
                smallWindowParams.height = FloatWindowSmallView.windowViewHeight;
                smallWindowParams.x = screenWidth;
                smallWindowParams.y = screenHeight / 2;
            }
            smallWindow.setParams(smallWindowParams);
            windowManager.addView(smallWindow, smallWindowParams);
        }
    }

    /**
     * 如果WindowManager还未创建，则创建一个新的WindowManager返回。否则返回当前已创建的WindowManager。
     *
     * @param context
     *            必须为应用程序的Context.
     * @return WindowManager的实例，用于控制在屏幕上添加或移除悬浮窗。
     */
    private static WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }

    /**
     * 计算已使用内存的百分比，并返回。
     *
     * @param context
     *            可传入应用程序上下文。
     * @return 已使用内存的百分比，以字符串形式返回。
     */
    public static String getUsedPercentValue(Context context) {
        String dir = "/proc/meminfo";
        try {
            FileReader fr = new FileReader(dir);
            BufferedReader br = new BufferedReader(fr, 2048);
            String memoryLine = br.readLine();
            String subMemoryLine = memoryLine.substring(memoryLine
                    .indexOf("MemTotal:"));
            br.close();
            long totalMemorySize = Integer.parseInt(subMemoryLine.replaceAll(
                    "\\D+", ""));
            long availableSize = getAvailableMemory(context) / 1024;
            int percent = (int) ((totalMemorySize - availableSize)
                    / (float) totalMemorySize * 100);
            return percent + "%";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "悬浮窗";
    }

    private static long getAvailableMemory(Context context) {
        ActivityManager.MemoryInfo memoryin = new ActivityManager.MemoryInfo();
        getActivityManager(context).getMemoryInfo(memoryin);
        return memoryin.availMem;
    }

    /**
     * 如果ActivityManager还未创建，则创建一个新的ActivityManager返回。否则返回当前已创建的ActivityManager。
     *
     * @param context
     *            可传入应用程序上下文。
     * @return ActivityManager的实例，用于获取手机可用内存。
     */
    private static ActivityManager getActivityManager(Context context) {
        if (mActivityManager == null) {
            mActivityManager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
        }
        return mActivityManager;
    }

    public static void createLauncher(Context context) {
        WindowManager windowManager = getWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if (rocketLauncher == null) {
            rocketLauncher = new RocketLauncher(context);
            if (launcherParams == null) {
                launcherParams = new WindowManager.LayoutParams();
                launcherParams.x = screenWidth / 2 - RocketLauncher.width / 2;
                launcherParams.y = screenHeight - RocketLauncher.height;
                launcherParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                launcherParams.format = PixelFormat.RGBA_8888;
                launcherParams.gravity = Gravity.LEFT | Gravity.TOP;
                launcherParams.width = RocketLauncher.width;
                launcherParams.height = RocketLauncher.height;
            }
            windowManager.addView(rocketLauncher, launcherParams);
        }
    }

    public static void removeLauncher(Context context) {
        if (rocketLauncher != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(rocketLauncher);
            rocketLauncher = null;
        }
    }

    public static void updateLauncher() {
        if (rocketLauncher != null) {
            rocketLauncher.updateLauncherStatus(isReadyToLaunch());
        }
    }

    /**
     * 判断小火箭是否准备好发射了。
     *
     * @return 当火箭被发到发射台上返回true，否则返回false。
     */
    public static boolean isReadyToLaunch() {
        if ((smallWindowParams.x > launcherParams.x && smallWindowParams.x
                + smallWindowParams.width < launcherParams.x
                + launcherParams.width)
                && (smallWindowParams.y + smallWindowParams.height > launcherParams.y)) {
            return true;
        }
        return false;
    }
}
