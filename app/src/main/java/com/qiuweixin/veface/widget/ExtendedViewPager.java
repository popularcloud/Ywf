package com.qiuweixin.veface.widget;


import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class ExtendedViewPager extends ViewPager {

    private static final int DEFAULT_AUTO_SCROLL_DELAY_TIME_MILLS = 5000;

    int mAutoScrollDelayTimeMills = DEFAULT_AUTO_SCROLL_DELAY_TIME_MILLS;

    boolean mScrollable = true;
    boolean mAutoScrollable = false;

    Handler mUIHandler;
    Runnable mAutoScrollCallBack;

    OnPageChangeListener mOnPageChangeListener;

    public ExtendedViewPager(Context context) {
        super(context);

        init();
    }

    public ExtendedViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        mUIHandler = new Handler();

        mOnPageChangeListener = new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                resetAutoScrollState();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

        this.addOnPageChangeListener(mOnPageChangeListener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (mScrollable)
            return super.onTouchEvent(arg0);
        else
            return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (mScrollable)
            return super.onInterceptTouchEvent(arg0);
        else
            return false;
    }

    /**
     *
     * @param scrollable 是否允许滚动
     */
    public void setScrollable(boolean scrollable) {
        mScrollable = scrollable;
    }

    public void setAutoScrollDelay(int timeMills) {
        mAutoScrollDelayTimeMills = timeMills;
    }

    public void startAutoScroll() {
        if (mAutoScrollable) {
            return;
        }

        mAutoScrollable = true;
        mAutoScrollCallBack = new Runnable() {
            @Override
            public void run() {
                PagerAdapter adapter = getAdapter();
                if (adapter == null) {
                    return;
                }

                int current = getCurrentItem();
                int count = adapter.getCount();
                int next = current < count - 1 ? current + 1 : 0;

                setCurrentItem(next);
                startNextAutoScroll();
            }
        };

        resumeAutoScroll();
    }

    public void resumeAutoScroll() {
        if (mAutoScrollable) {
            startNextAutoScroll();
        }
    }

    public void pauseAutoScroll() {
        if (mAutoScrollable) {
            mUIHandler.removeCallbacks(mAutoScrollCallBack);
        }
    }

    public void stopAutoScroll() {
        if (!mAutoScrollable) {
            return;
        }

        pauseAutoScroll();

        mAutoScrollable = false;
        mAutoScrollCallBack = null;
    }

    private void resetAutoScrollState() {
        if (mAutoScrollable) {
            pauseAutoScroll();
            resumeAutoScroll();
        }
    }

    private void startNextAutoScroll() {
        if (mAutoScrollable) {
            mUIHandler.postDelayed(mAutoScrollCallBack, mAutoScrollDelayTimeMills);
        }
    }
}
