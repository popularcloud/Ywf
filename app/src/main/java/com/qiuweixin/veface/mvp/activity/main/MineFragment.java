package com.qiuweixin.veface.mvp.activity.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qiuweixin.veface.R;

import butterknife.Bind;

/**
 * Created by Allen Lake on 2016/1/6 0006.
 */
public class MineFragment extends Fragment{




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine,container,false);
    }
}
