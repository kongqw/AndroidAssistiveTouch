package com.kongqw.androidassistivetouch.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kongqw.androidassistivetouch.R;
import com.kongqw.androidassistivetouch.event.CenterStateEvent;

import de.greenrobot.event.EventBus;

public class AssistiveCenter implements View.OnClickListener {

    // 窗口的管理者
    private static WindowManager windowManager;
    // 界面布局对象
    private static View view;

    private Context mContext;


    public AssistiveCenter(Context context) {
        mContext = context;
    }

    /**
     * 显示迷你模式
     */
    public void showAssistiveCenter() {
        try {
            // 关闭之前打开的Toast
            hideAssistiveCenter();
            EventBus.getDefault().post(new CenterStateEvent(true));
            // 初始化窗口的管理者
            windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            // 加载自定义布局
            view = View.inflate(mContext, R.layout.assistive_center, null);
            LinearLayout home = (LinearLayout) view.findViewById(R.id.home);
            RelativeLayout back = (RelativeLayout) view.findViewById(R.id.back);

            home.setOnClickListener(this);
            back.setOnClickListener(this);

            // 创建布局的参数
            final LayoutParams params = new LayoutParams();

            params.gravity = Gravity.CENTER;
            // 高度包裹内容
            params.height = LayoutParams.MATCH_PARENT;
            // 宽度包裹内容
            params.width = LayoutParams.MATCH_PARENT;
            /*
             * 不可获得焦点 不可触摸 屏幕常亮
			 */
            params.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
//             WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
            // WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;

            // 半透明
            params.format = PixelFormat.TRANSLUCENT;

			/*
             * 设置Toast类型 小米使用TYPE_TOAST类型就可以触摸 模拟器使用TYPE_PRIORITY_PHONE类型就可以触摸
			 */
            params.type = LayoutParams.TYPE_TOAST;

            // 显示自定义Toast
            windowManager.addView(view, params);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 关闭自定义Toast
     */
    public void hideAssistiveCenter() {
        try {
            if (null != windowManager && view != null) {
                windowManager.removeView(view);
                windowManager = null;
                EventBus.getDefault().post(new CenterStateEvent(false));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home:
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 注意
                intent.addCategory(Intent.CATEGORY_HOME);
                mContext.startActivity(intent);
                hideAssistiveCenter();
                break;
            case R.id.back:
                hideAssistiveCenter();
                break;
        }
    }
}
