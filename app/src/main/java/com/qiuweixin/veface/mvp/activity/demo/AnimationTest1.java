package com.qiuweixin.veface.mvp.activity.demo;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Scroller;

import com.qiuweixin.veface.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Allen Lake on 2016/1/18 0018.
 */
public class AnimationTest1 extends Activity{

    @Bind(R.id.btn_test)
    Button btn_test;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation01);
        ButterKnife.bind(this);
       // initContent();
    }

    private void initContent() {
        ObjectAnimator.ofFloat(btn_test,"translationX",0,300).setDuration(100).start();
        ObjectAnimator.ofFloat(btn_test,"translationY",0,300).setDuration(100).start();
    }

    @OnClick(R.id.btn_test)
    public void btntestOonclick(View view){
       // initContent();
    }

}
