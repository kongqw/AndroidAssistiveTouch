package com.kongqw.androidassistivetouch.view;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;

import com.kongqw.androidassistivetouch.R;
import com.kongqw.androidassistivetouch.event.CenterStateEvent;
import com.kongqw.androidassistivetouch.utils.AnimationUtil;

import de.greenrobot.event.EventBus;

public class AssistiveTouch extends Service implements OnTouchListener {

    // 窗口的管理者
    private static WindowManager windowManager;
    // 界面布局对象
    private static View view;

    private static int widthPixels;
    private static int heightPixels;

    private static SharedPreferences mSharedPreferences;

    @Override
    public IBinder onBind(Intent arg0) {
        return new MyBinder();
    }


    /**
     * 中间人，是IBinder类型的
     *
     * @author Administrator
     */
    public class MyBinder extends Binder {

        /**
         * 显示AssistiveTouch
         */
        public void showMiniMode() {
            showAssistiveTouch();
        }

        /**
         * 关闭AssistiveTouch
         */
        public void hideMiniMode() {
            hideAssistiveTouch();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        // 初始化SharedPreferences
        mSharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 显示迷你模式
     */
    public void showAssistiveTouch() {
        try {
            // 关闭之前打开的Toast
            hideAssistiveTouch();
            // 初始化窗口的管理者
            windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            // 获取手机屏幕尺寸
            DisplayMetrics outMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(outMetrics);
            widthPixels = outMetrics.widthPixels;
            heightPixels = outMetrics.heightPixels;
            // 加载自定义布局
            view = View.inflate(this, R.layout.assistive_touch, null);

            // 迷你模式按钮的触摸事件
            view.setOnTouchListener(this);

            // 创建布局的参数
            final WindowManager.LayoutParams params = new WindowManager.LayoutParams();

			/*
             * 设置Toast的位置 以距离顶部距离和左侧距离作为定位标准
			 */
            params.gravity = Gravity.LEFT + Gravity.TOP;

            // 设置Toast显示位置
            params.x = mSharedPreferences.getInt("Left", 0);
            params.y = mSharedPreferences.getInt("Top", 0);

            // 高度包裹内容
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            // 宽度包裹内容
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            /*
             * 不可获得焦点 不可触摸 屏幕常亮
			 */
            params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;// |
            // WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE|
            // WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;

            // 半透明
            params.format = PixelFormat.TRANSLUCENT;

			/*
             * 设置Toast类型 小米使用TYPE_TOAST类型就可以触摸 模拟器使用TYPE_PRIORITY_PHONE类型就可以触摸
			 */
            params.type = WindowManager.LayoutParams.TYPE_TOAST;

            // 显示自定义Toast
            windowManager.addView(view, params);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 关闭自定义Toast
     */
    public void hideAssistiveTouch() {
        try {
            if (null != windowManager && view != null) {
                windowManager.removeView(view);
                windowManager = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 手指按下屏幕的坐标
    private float rawX;
    private float rawY;
    private float startX;
    private float startY;
    private float endX;
    private float endY;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        try {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:// 按下
                    // 获取按下时的左边
                    rawX = event.getRawX();
                    rawY = event.getRawY();
                    startX = event.getRawX();
                    startY = event.getRawY();
                    break;
                case MotionEvent.ACTION_UP:// 抬起
                    endX = event.getRawX();
                    endY = event.getRawY();
                    if (startX == endX && startY == endY) {
                        AssistiveCenter assistiveCenter = new AssistiveCenter(AssistiveTouch.this);
                        assistiveCenter.showAssistiveCenter();
                    }

                    WindowManager.LayoutParams params = (LayoutParams) view.getLayoutParams();
                    int left = params.x;
                    int top = params.y;
                    // 将移动后的坐标坐标保存到sp
                    mSharedPreferences.edit().putInt("Left", left).putInt("Top", top).commit();
                    break;
                case MotionEvent.ACTION_MOVE:// 移动
                    // 获取手指新的位置
                    float newRawX = event.getRawX();
                    float newRawY = event.getRawY();
                    // 计算移动的差值
                    float moveX = newRawX - rawX;
                    float moveY = newRawY - rawY;

                    // 重新设置控件的显示位置
                    WindowManager.LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
                    layoutParams.x += moveX;
                    layoutParams.y += moveY;

                    // 处理手机屏幕移动出界问题
                    if (layoutParams.x < 0) {
                        layoutParams.x = 0;
                    }
                    if (layoutParams.y < 0) {
                        layoutParams.y = 0;
                    }
                    if (layoutParams.x + view.getWidth() > widthPixels) {
                        layoutParams.x = widthPixels - view.getWidth();
                    }
                    if (layoutParams.y + view.getHeight() > heightPixels - 30) {
                        layoutParams.y = heightPixels - 30 - view.getHeight();
                    }

                    windowManager.updateViewLayout(view, layoutParams);

                    // 将手指的位置更新为新的位置
                    rawX = newRawX;
                    rawY = newRawY;
                    break;
                default:
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * EventBus
     *
     * @param event
     */
    public void onEvent(CenterStateEvent event) {
        if (event.isOpen()) {
            AnimationUtil.alphaAnimation(view.findViewById(R.id.iv), false);
        } else {
            AnimationUtil.alphaAnimation(view.findViewById(R.id.iv), true);
        }
    }

}
