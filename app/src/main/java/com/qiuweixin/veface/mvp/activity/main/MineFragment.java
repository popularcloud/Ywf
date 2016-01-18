package com.qiuweixin.veface.mvp.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.qiuweixin.veface.R;
import com.qiuweixin.veface.mvp.activity.demo.AnimationTest1;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Allen Lake on 2016/1/6 0006.
 */
public class MineFragment extends Fragment{

    @Bind(R.id.btn_test)
    Button btn_test;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

   @OnClick(R.id.btn_test)
   public void btntestOonclick(View view){
       startActivity(new Intent(getActivity(),AnimationTest1.class));
   }
}
