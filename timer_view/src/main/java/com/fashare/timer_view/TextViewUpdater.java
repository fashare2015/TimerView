package com.fashare.timer_view;

import android.support.annotation.IdRes;
import android.view.View;
import android.widget.TextView;

/**
 * User: fashare(153614131@qq.com)
 * Date: 2017-01-17
 * Time: 00:18
 * <br/><br/>
 */
public class TextViewUpdater implements DigitalTimerView.ViewUpdater {

    @IdRes int mTimeTextViewId, mSuffixTextViewId;

    public TextViewUpdater(int timeTextViewId, int suffixTextViewId) {
        mTimeTextViewId = timeTextViewId;
        mSuffixTextViewId = suffixTextViewId;
    }

    @Override
    public void initSuffix(View suffixView, int pos) {
        View v = suffixView.findViewById(mSuffixTextViewId);
        if(v != null && v instanceof TextView)
            ((TextView)v).setText(":");
    }

    @Override
    public void updateShow(View subTimeView, int subTime, String formattedSubTime) {
        View v = subTimeView.findViewById(mTimeTextViewId);
        if(v != null && v instanceof TextView)
            ((TextView)v).setText(formattedSubTime);
    }
}