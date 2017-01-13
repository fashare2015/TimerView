package com.fashare.timerview.customize;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fashare.timer_view.BaseTimerView;
import com.fashare.timer_view.TimeInfo;

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
 * @see #setSubTimeView(int, int, ViewGroup.LayoutParams)
 * @see #setSuffixView(int, int, ViewGroup.LayoutParams)
 */
public class MechanicalTimerView extends BaseTimerView {
    public static final int UNIT_COUNT = 3;

    private List<ViewUnit> mViewUnitList;
    boolean mIsShowLastSuffix = false;      // 是否显示 "秒" 后面的后缀

    ViewUnit mViewUnitStyle;    // 显示风格

    /**
     * 设置 "时、分、秒" 的显示风格
     * @param rootLayoutRes 布局 xml 文件
     * @param textIdRes     rootLayout 中, 用于显示的 TextView
     * @param rootLayoutParams  rootLayout 的 LayoutParams, 输入 null 则取默认 WRAP_CONTENT
     * @return
     */
    public MechanicalTimerView setSubTimeView(@LayoutRes int rootLayoutRes, @IdRes int textIdRes, ViewGroup.LayoutParams rootLayoutParams){
        mViewUnitStyle.mSubTimeItem.mRootLayoutRes = rootLayoutRes;
        mViewUnitStyle.mSubTimeItem.mTextIdRes = textIdRes;
        mViewUnitStyle.mSubTimeItem.mRootLayoutParams = rootLayoutParams;
        initViewUnitList();
        return this;
    }

    /**
     * 设置 "后缀" 的显示风格, 默认为 冒号":"
     * @param rootLayoutRes 布局 xml 文件
     * @param textIdRes     rootLayout 中, 用于显示的 TextView
     * @param rootLayoutParams  rootLayout 的 LayoutParams, 输入 null 则取默认 WRAP_CONTENT
     * @return
     */
    public MechanicalTimerView setSuffixView(@LayoutRes int rootLayoutRes, @IdRes int textIdRes, ViewGroup.LayoutParams rootLayoutParams){
        mViewUnitStyle.mSuffixItem.mRootLayoutRes = rootLayoutRes;
        mViewUnitStyle.mSuffixItem.mTextIdRes = textIdRes;
        mViewUnitStyle.mSuffixItem.mRootLayoutParams = rootLayoutParams;
        initViewUnitList();
        return this;
    }

    public MechanicalTimerView(Context context) {
        super(context);
    }

    public MechanicalTimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MechanicalTimerView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        initViewUnitList();
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
            if(i != UNIT_COUNT-1 || mIsShowLastSuffix)
                this.addView(viewUnit.mSuffixItem.mRootView);
        }
    }

    public void updateShow(long curTime){
        TimeInfo timeInfo = TimeInfo.make(curTime);

        List<String> timeTextList = Arrays.asList(
                timeInfo.getFormatedHour(),
                timeInfo.getFormatedMinute(),
                timeInfo.getFormatedSecond()
        );

        for(int i=0; i<mViewUnitList.size() && i<timeTextList.size(); i++)
            mViewUnitList.get(i).mSubTimeItem.mTextView.setText(timeTextList.get(i));
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
            @IdRes
            int mTextIdRes;          // 其中的 TextView

            View mRootView;
            TextView mTextView;

            ViewGroup.LayoutParams mRootLayoutParams;

            public Item(Context context) {
                this(context, android.R.layout.simple_list_item_1, android.R.id.text1, null);
            }

            public Item(Context context, @LayoutRes int rootLayoutRes, @IdRes int textIdRes, ViewGroup.LayoutParams lp) {
                mRootLayoutRes = rootLayoutRes;
                mTextIdRes = textIdRes;
                mRootLayoutParams = lp;
                if(mRootLayoutParams == null){
                    mRootLayoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                }

                mRootView = View.inflate(context, mRootLayoutRes, null);
                mRootView.setLayoutParams(mRootLayoutParams);

                mTextView = (TextView)mRootView.findViewById(textIdRes);
                if(mTextView == null || !(mTextView instanceof TextView))
                    throw new IllegalArgumentException(String.format("Can not find target TextView with your textResId: %d", mTextIdRes));

                initView();
            }

            protected void initView() {}

            public Item(Context context, Item item){
                this(context, item.mRootLayoutRes, item.mTextIdRes, item.mRootLayoutParams);
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

        public SubTimeItem(Context context, @LayoutRes int rootLayoutRes, @IdRes int textIdRes, ViewGroup.LayoutParams lp) {
            super(context, rootLayoutRes, textIdRes, lp);
        }
    }

    private static class SuffixItem extends ViewUnit.Item{
        public SuffixItem(Context context) {
            super(context);
        }

        public SuffixItem(Context context, ViewUnit.Item item) {
            super(context, item);
        }

        public SuffixItem(Context context, @LayoutRes int rootLayoutRes, @IdRes int textIdRes, ViewGroup.LayoutParams lp) {
            super(context, rootLayoutRes, textIdRes, lp);
        }

        @Override
        protected void initView() {
            super.initView();
            mTextView.setText(":");
        }
    }
}
