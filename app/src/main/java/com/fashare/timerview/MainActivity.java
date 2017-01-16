package com.fashare.timerview;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fashare.timer_view.DigitalTimerView;
import com.fashare.timer_view.TextViewUpdater;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Integer> mFireDigitRes = Arrays.asList(
            R.drawable.digit_0,
            R.drawable.digit_1,
            R.drawable.digit_2,
            R.drawable.digit_3,
            R.drawable.digit_4,
            R.drawable.digit_5,
            R.drawable.digit_6,
            R.drawable.digit_7,
            R.drawable.digit_8,
            R.drawable.digit_9
    );

    DigitalTimerView mDtvSimple, mDtvComplex, mDtvFire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDtvSimple();
        initDtvComplex();
        initDtvFire();
    }

    private void initDtvSimple() {
        int rootLayoutRes = android.R.layout.simple_list_item_1,
                textViewIdRes = android.R.id.text1;
        (mDtvSimple = (DigitalTimerView)findViewById(R.id.dtv_simple))
                .setSubTimeView(rootLayoutRes, null)
                .setSuffixView(rootLayoutRes, null)
                .setViewUpdater(new TextViewUpdater(textViewIdRes, textViewIdRes));
    }

    private void initDtvComplex() {
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                dp2px(this, 40),
                dp2px(this, 40)
        );

        (mDtvComplex = (DigitalTimerView)findViewById(R.id.dtv_complex))
                .setSubTimeView(R.layout.item_clock, lp)
                .setSuffixView(R.layout.item_suffix, null)
                .setViewUpdater(new TextViewUpdater(R.id.tv_time, R.id.tv_suffix));
    }

    private void initDtvFire() {
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        (mDtvFire = (DigitalTimerView)findViewById(R.id.dtv_fire))
                .setSubTimeView(R.layout.item_clock_fire, lp)
                .setSuffixView(R.layout.item_suffix_fire, lp)
                .setViewUpdater(new DigitalTimerView.ViewUpdater() {
                    @Override
                    public void initSuffix(View suffixView, int pos) {}

                    @Override
                    public void updateShow(View subTimeView, int subTime, String formattedSubTime) {
                        ImageView ivHigh = (ImageView) subTimeView.findViewById(R.id.iv_high),
                                ivLow = (ImageView) subTimeView.findViewById(R.id.iv_low);
                        ivHigh.setImageResource(mFireDigitRes.get(subTime/10));
                        ivLow.setImageResource(mFireDigitRes.get(subTime%10));
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDtvSimple.start(300*1000);
        mDtvComplex.start(300*1000);
        mDtvFire.start(300*1000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDtvSimple.stop();
        mDtvComplex.stop();
        mDtvFire.stop();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
