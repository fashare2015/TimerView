package com.fashare.timerview;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.fashare.timer_view.DigitalTimerView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((DigitalTimerView)findViewById(R.id.dtv_simple)).start(300*1000);

        DigitalTimerView mDtv = (DigitalTimerView)findViewById(R.id.dtv);

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                dp2px(this, 40),
                dp2px(this, 40)
        );

        mDtv.setSubTimeView(R.layout.item_clock, R.id.tv_time, lp)
                .setSuffixView(R.layout.item_suffix, R.id.tv_time, null)
                .start(300*1000);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
