package com.fashare.timer_view;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jinliangshan on 17/1/11.
 *
 * 标准的横向时间展示, "00:00:00".
 *
 * 展示的 UI 布局由外界注入.
 *
 * @see #setSubTimeView(int, ViewGroup.LayoutParams)
 * @see #setSuffixView(int, ViewGroup.LayoutParams)
 */
public class DigitalTimerView extends BaseTimerView {
    public static final int UNIT_COUNT = 3;

    private List<ViewUnit> mViewUnitList;
    boolean mIsShowLastSuffix = false;      // 是否显示 "秒" 后面的后缀

    ViewUnit mViewUnitStyle;    // 显示风格

    ViewUpdater mViewUpdater;

    public DigitalTimerView setViewUpdater(ViewUpdater viewUpdater) {
        mViewUpdater = viewUpdater;
        return this;
    }

    /**
     * 对外接口: 视图更新方式
     */
    public interface ViewUpdater{
        /**
         * 初始化 "后缀View", 如设成 "冒号" 或者 "时, 分, 秒" 啥的
         * @param suffixView
         * @param pos
         */
        void initSuffix(View suffixView, int pos);

        /**
         * subTime 指的是 "时, 分, 秒" 这三个小单元
         * @param subTimeView
         * @param subTime
         * @param formattedSubTime
         */
        void updateShow(View subTimeView, int subTime, String formattedSubTime);
    }

    /**
     * 设置 "时、分、秒" 的显示风格
     * @param rootLayoutRes 布局 xml 文件
     * @param rootLayoutParams  rootLayout 的 LayoutParams, 输入 null 则取默认 WRAP_CONTENT
     * @return
     */
    public DigitalTimerView setSubTimeView(@LayoutRes int rootLayoutRes, ViewGroup.LayoutParams rootLayoutParams){
        mViewUnitStyle.mSubTimeItem.mRootLayoutRes = rootLayoutRes;
        mViewUnitStyle.mSubTimeItem.mRootLayoutParams = rootLayoutParams;
        return this;
    }

    /**
     * 设置 "后缀" 的显示风格, 默认为 冒号":"
     * @param rootLayoutRes 布局 xml 文件
     * @param rootLayoutParams  rootLayout 的 LayoutParams, 输入 null 则取默认 WRAP_CONTENT
     * @return
     */
    public DigitalTimerView setSuffixView(@LayoutRes int rootLayoutRes, ViewGroup.LayoutParams rootLayoutParams){
        mViewUnitStyle.mSuffixItem.mRootLayoutRes = rootLayoutRes;
        mViewUnitStyle.mSuffixItem.mRootLayoutParams = rootLayoutParams;
        return this;
    }

    public DigitalTimerView(Context context) {
        super(context);
    }

    public DigitalTimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DigitalTimerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init() {
        super.init();
        this.setOrientation(LinearLayout.HORIZONTAL);
        this.setGravity(Gravity.CENTER);

        mViewUnitStyle = new ViewUnit(
                new SubTimeItem(getContext()),
                new SuffixItem(getContext())
        );
    }

    private void initViewUnitList() {
        this.removeAllViews();
        mViewUnitList = new ArrayList<>();

        for(int i=0; i<UNIT_COUNT; i++){
            ViewUnit viewUnit = new ViewUnit(
                    new SubTimeItem(getContext(), mViewUnitStyle.mSubTimeItem),
                    new SuffixItem(getContext(), mViewUnitStyle.mSuffixItem)
            );
            mViewUnitList.add(viewUnit);

            this.addView(viewUnit.mSubTimeItem.mRootView);
            if(i != UNIT_COUNT-1 || mIsShowLastSuffix){
                View suffixView = viewUnit.mSuffixItem.mRootView;
                this.addView(suffixView);

                if(mViewUpdater != null)
                    mViewUpdater.initSuffix(suffixView, i);
            }
        }
    }

    boolean mHasInit = false;
    public void updateShow(long curTime){
        if(!mHasInit){
            mHasInit = true;
            initViewUnitList();
        }
        TimeInfo timeInfo = TimeInfo.make(curTime);

        List<Integer> timeList = Arrays.asList(
                (int)timeInfo.getHour(),
                (int)timeInfo.getMinute(),
                (int)timeInfo.getSecond()
        );

        List<String> formattedTimeList = Arrays.asList(
                timeInfo.getFormatedHour(),
                timeInfo.getFormatedMinute(),
                timeInfo.getFormatedSecond()
        );

        for(int i=0; i<mViewUnitList.size() && i<formattedTimeList.size(); i++) {
            View subTimeView = mViewUnitList.get(i).mSubTimeItem.mRootView;
            if(mViewUpdater != null)
                mViewUpdater.updateShow(subTimeView, timeList.get(i), formattedTimeList.get(i));
        }
    }

    private static class ViewUnit{
        Item mSubTimeItem,      // 子时间单元: 如 时、分、秒
                mSuffixItem;    // 后缀: 如 冒号":"

        public ViewUnit(Item subTimeItem, Item suffixItem) {
            mSubTimeItem = subTimeItem;
            mSuffixItem = suffixItem;
        }

        private static class Item{
            @LayoutRes
            int mRootLayoutRes;  // 根布局

            View mRootView;

            ViewGroup.LayoutParams mRootLayoutParams;

            public Item(Context context) {
                this(context, android.R.layout.simple_list_item_1, null);
            }

            public Item(Context context, @LayoutRes int rootLayoutRes, ViewGroup.LayoutParams lp) {
                mRootLayoutRes = rootLayoutRes;
                mRootLayoutParams = lp;
                if(mRootLayoutParams == null){
                    mRootLayoutParams = new LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT
                    );
                }

                mRootView = View.inflate(context, mRootLayoutRes, null);
                mRootView.setLayoutParams(mRootLayoutParams);
            }

            public Item(Context context, Item item){
                this(context, item.mRootLayoutRes, item.mRootLayoutParams);
            }
        }
    }

    private static class SubTimeItem extends ViewUnit.Item{
        public SubTimeItem(Context context) {
            super(context);
        }

        public SubTimeItem(Context context, ViewUnit.Item item) {
            super(context, item);
        }

        public SubTimeItem(Context context, @LayoutRes int rootLayoutRes, ViewGroup.LayoutParams lp) {
            super(context, rootLayoutRes, lp);
        }
    }

    private static class SuffixItem extends ViewUnit.Item{
        public SuffixItem(Context context) {
            super(context);
        }

        public SuffixItem(Context context, ViewUnit.Item item) {
            super(context, item);
        }

        public SuffixItem(Context context, @LayoutRes int rootLayoutRes, ViewGroup.LayoutParams lp) {
            super(context, rootLayoutRes, lp);
        }
    }
}
