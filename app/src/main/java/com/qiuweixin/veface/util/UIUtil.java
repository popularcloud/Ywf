package com.qiuweixin.veface.util;


import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiuweixin.veface.R;

public class UIUtil {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) (pxValue / scale + 0.5f);
    }
    public static Drawable getSelectedItemDrawable(Context context) {
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray ta = context.obtainStyledAttributes(attrs);
        Drawable selectedItemDrawable = ta.getDrawable(0);
        ta.recycle();

        return selectedItemDrawable;
    }



    public static View initItem(Activity activity,int dividingLineColor,float textSize,int textColor,ViewGroup parent,String text,int backgroundResource,boolean hasLine){

        /**
         * 创建popuwindow中的按钮
         */
        Button button = new Button(activity);
        button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        button.setPadding(12, 12, 12, 12);

        button.setBackgroundResource(backgroundResource);
        button.setTextSize(textSize);
        button.setTextColor(textColor);
        button.setText(text);

        parent.addView(button);


        if(hasLine){
            /**
             * 分割线布局
             */
            View dividingLine = new View(activity);
            dividingLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
            dividingLine.setBackgroundColor(dividingLineColor);
            parent.addView(dividingLine);

        }

        return button;
    }
}
