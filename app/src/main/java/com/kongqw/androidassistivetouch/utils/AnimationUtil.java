package com.kongqw.androidassistivetouch.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

/**
 * Created by kongqw on 2015/12/29.
 */
public class AnimationUtil {

    private static int widthPixels;
    private static int heightPixels;

    private AnimationUtil() {
    }

    /**
     * AssistiveTouch动画
     */
    public static void moveAssistiveTouch(Context context, View view) {
        // 获取手机屏幕尺寸
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        widthPixels = outMetrics.widthPixels;
        heightPixels = outMetrics.heightPixels;


        WindowManager.LayoutParams params = (WindowManager.LayoutParams) view.getLayoutParams();
        int left = params.x;
        int top = params.y;
        TranslateAnimation ta = new TranslateAnimation(left,top,0,0);
// 设置动画播放的时间
        ta.setDuration(3000);
// 设置动画重复播放的次数
//        ta.setRepeatCount(1);
// 设置动画重复播放的模式
//        ta.setRepeatMode(TranslateAnimation.REVERSE);
// 开始播放动画
        view.startAnimation(ta);
//        int right = widthPixels - left - view.getWidth();
//        int bottom = heightPixels - top - view.getHeight();
//        if (left < top && left < right && left < bottom) {
//            // 左移
//            move(windowManager, view, 2);
//            Toast.makeText(context, "左移", Toast.LENGTH_SHORT).show();
//        } else if (right < left && right < top && right < bottom) {
//            // 右移
//            move(windowManager, view, 3);
//            Toast.makeText(context, "右移", Toast.LENGTH_SHORT).show();
//        } else if (top < left && top < right && top < bottom) {
//            // 上移
//            move(windowManager, view, 0);
//            Toast.makeText(context, "上移", Toast.LENGTH_SHORT).show();
//        } else if (bottom < top && bottom < left && bottom < right) {
//            // 下移
//            move(windowManager, view, 1);
//            Toast.makeText(context, "下移", Toast.LENGTH_SHORT).show();
//        }
    }

    private static void move(final WindowManager windowManager, final View view, final int direction) {
        // 重新设置控件的显示位置
        final WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) view.getLayoutParams();

        CountDownTimer countDownTimer = new CountDownTimer(1000, 1) {
            @Override
            public void onTick(long millisUntilFinished) {
                switch (direction) {
                    case 0://上
                        if (layoutParams.y > 0) {
                            layoutParams.y = (int) millisUntilFinished;
                        }
                        break;
                    case 1:// 下
                        if (layoutParams.y < heightPixels) {
                            layoutParams.y = (int) millisUntilFinished;
                        }
                        break;
                    case 2:// 左
                        if (layoutParams.x > 0) {
                            layoutParams.x = (int) millisUntilFinished;
                        }
                        break;
                    case 3:// 右
                        if (layoutParams.x < widthPixels) {
                            layoutParams.x = (int) millisUntilFinished;
                        }
                        break;
                }
//                Log.i("updateViewLayout","layoutParams.x = " + layoutParams.x);
//                Log.i("updateViewLayout","layoutParams.y = " + layoutParams.y);
                Log.i("updateViewLayout","millisUntilFinished = " + millisUntilFinished);
                windowManager.updateViewLayout(view, layoutParams);
            }

            @Override
            public void onFinish() {

            }
        };

        countDownTimer.start();

    }
}
