package com.fashare.timerview.customize;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;

import com.fashare.timer_view.BaseTimerView;
import com.fashare.timer_view.TimeInfo;
import com.yus.clockview.ClockView;

/**
 * Created by jinliangshan on 17/1/11.
 *
 * 机械表 UI.
 * 接入 ClockView
 *
 */
public class MechanicalTimerView extends BaseTimerView {

    ClockView mClockView;

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
        this.setGravity(Gravity.CENTER);

        this.addView(mClockView = new ClockView(getContext()));
    }

    public void updateShow(long curTime){
        TimeInfo timeInfo = TimeInfo.make(curTime);

        int hour = (int)timeInfo.getHour(),
                minute = (int)timeInfo.getMinute(),
                second = (int)timeInfo.getSecond();

        mClockView.updateShow(hour, minute, second);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.start(300*1000);
    }
}
