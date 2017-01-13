package com.fashare.timerview.customize;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.Gravity;

import com.fashare.timer_view.BaseTimerView;
import com.fashare.timer_view.TimeInfo;
import com.lypeer.googleioclock.GoogleClock;
import com.lypeer.googleioclock.event.TimeUpdateEvent;
import com.lypeer.googleioclock.service.LyClockService;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by jinliangshan on 17/1/11.
 *
 * 标准的横向时间展示, "00:00:00".
 *
 * 展示的 UI 布局由外界注入.
 *
 */
public class GoogleTimerView extends BaseTimerView {

    public GoogleTimerView(Context context) {
        super(context);
    }

    public GoogleTimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GoogleTimerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init() {
        super.init();
        this.setGravity(Gravity.CENTER);

        this.addView(new MyGoogleClock(getContext()));
    }

    public void updateShow(long curTime){
        TimeInfo timeInfo = TimeInfo.make(curTime);

        int hour = (int)timeInfo.getHour(),
                minute = (int)timeInfo.getMinute(),
                second = (int)timeInfo.getSecond();

        EventBus.getDefault().post(new TimeUpdateEvent(
                hour/10, hour%10,
                minute/10, minute%10,
                second/10, second%10
        ));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.start(300*1000);
    }

    class MyGoogleClock extends GoogleClock{

        public MyGoogleClock(Context context) {
            super(context);
        }

        @Override
        protected void onWindowVisibilityChanged(int visibility) {
            // do nothing
//            super.onWindowVisibilityChanged(visibility);
            Intent intent = new Intent(getContext(), LyClockService.class);
            getContext().stopService(intent);
        }
    }

}
