package com.qiuweixin.veface.mvp.activity.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.qiuweixin.veface.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity implements View.OnClickListener{

    @Bind(R.id.contentPager)
    ViewPager contentViewPager;
    @Bind(R.id.tabDiscovery)
    View tabDiscovery;
    @Bind(R.id.tabSubscription)
    View tabSubscription;
    @Bind(R.id.tabPublished)
    View tabPublished;
    @Bind(R.id.tabMine)
    View tabMine;

    @Bind(R.id.iconDiscovery)
    View iconDiscovery;
    @Bind(R.id.iconSubscription)
    View iconSubscription;
    @Bind(R.id.iconPublished)
    View iconPublished;
    @Bind(R.id.iconMine)
    View iconMine;

    List<Fragment> mainFragmentLists;

    DiscoveryFragment discoveryFragment;
    SubscriptionFragment subscriptionFragment;
    PublishedFragment publishedFragment;
    MineFragment mineFragment;

    //当前选中的项
    int currenttab=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initContent();
    }

    //初始化
    private void initContent() {
        mainFragmentLists=new ArrayList<>();
        discoveryFragment=new DiscoveryFragment();
        subscriptionFragment=new SubscriptionFragment();
        publishedFragment=new PublishedFragment();
        mineFragment=new MineFragment();

        mainFragmentLists.add(discoveryFragment);
        mainFragmentLists.add(subscriptionFragment);
        mainFragmentLists.add(publishedFragment);
        mainFragmentLists.add(mineFragment);

        tabDiscovery.setOnClickListener(this);
        tabSubscription.setOnClickListener(this);
        tabPublished.setOnClickListener(this);
        tabMine.setOnClickListener(this);

        contentViewPager.setAdapter(new MyFrageStatePagerAdapter(getSupportFragmentManager()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tabDiscovery:
                changeView(0);
                break;
            case R.id.tabSubscription:
                changeView(1);
                break;
            case R.id.tabPublished:
                changeView(2);
                break;
            case R.id.tabMine:
                changeView(3);
                break;
            default:
                break;
        }
    }

    //手动设置ViewPager要显示的视图
    private void changeView(int desTab)
    {
        contentViewPager.setCurrentItem(desTab, true);
    }


    private class MyFrageStatePagerAdapter extends FragmentStatePagerAdapter {

        public MyFrageStatePagerAdapter(FragmentManager fm){
                super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            return mainFragmentLists.get(position);
        }

        @Override
        public int getCount() {
            return mainFragmentLists.size();
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            super.finishUpdate(container);
            //这句话要放在最前面，否则会报错
            //获取当前的视图是位于ViewGroup的第几个位置，用来更新对应的覆盖层所在的位置
            int currentItem=contentViewPager.getCurrentItem();
            if (currentItem==currenttab)
            {
                return ;
            }
            imageMove(contentViewPager.getCurrentItem());
            currenttab=contentViewPager.getCurrentItem();
        }
    }

    /**
     * 切换底部导航栏
     * @param moveToTab
     */
    private void imageMove(int moveToTab)
    {
        iconDiscovery.setSelected(false);
        iconSubscription.setSelected(false);
        iconPublished.setSelected(false);
        iconMine.setSelected(false);
        switch (moveToTab){
            case 0:
                iconDiscovery.setSelected(true);
                break;
            case 1:
                iconSubscription.setSelected(true);
                break;
            case 2:
                iconPublished.setSelected(true);
                break;
            case 3:
                iconMine.setSelected(true);
                break;
            default:
                break;
        }
    }
}
