package com.kongqw.androidassistivetouch.utils;

import android.view.View;
import android.view.animation.AlphaAnimation;

/**
 * Created by kongqw on 2015/12/29.
 */
public class AnimationUtil {

    private AnimationUtil() {
    }

    /**
     * 透明动画
     *
     * @param view view
     * @param flag true 显示 false 隐藏
     */
    public static void alphaAnimation(View view, boolean flag) {
        // 创建透明度的动画对象
        AlphaAnimation aa;
        if (flag) {
            aa = new AlphaAnimation(0.3f, 1f);
        } else {
            aa = new AlphaAnimation(1f, 0.3f);
        }
        // 设置动画播放的时间
        aa.setDuration(1000);

        // 移动后保持移动后的状态
        aa.setFillAfter(true);
        // 开始播放动画
        view.startAnimation(aa);
    }

}
