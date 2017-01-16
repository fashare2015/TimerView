package com.fashare.timer_view;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by jinliangshan on 17/1/11.
 * 计时器
 */
public interface ITimer {
    void setLifeCycleListener(LifeCycleListener lifeCycleListener);

    void setOnCountTimeListener(OnCountTimeListener onCountTimeListener);

    void start(long initTime);

    void stop();

    /**
     * 生命周期, 用户手动控制
     */
    interface LifeCycleListener {
        void onStart();

        void onStop();
    }

    /**
     * 计数回调, 计数器自行控制
     */
    interface OnCountTimeListener{
        void countTime(long curTime);

        void onEnd();
    }

    /**
     * 倒计时的实现
     */
    class CountDown implements ITimer{
        private static final int PERIOD = 1000;     // 计数间隔 1s (1000ms)
        private ScheduledExecutorService mTimerExecutor; // 定时器
        private Handler sHandler = new Handler(Looper.getMainLooper());

        private long mLeftTime;     // 剩余时间(ms)

        private LifeCycleListener mLifeCycleListener;
        private OnCountTimeListener mOnCountTimeListener;

        @Override
        public void setLifeCycleListener(LifeCycleListener lifeCycleListener) {
            mLifeCycleListener = lifeCycleListener;
        }

        @Override
        public void setOnCountTimeListener(OnCountTimeListener onCountTimeListener) {
            mOnCountTimeListener = onCountTimeListener;
        }

        public CountDown() {
            this(null);
        }

        public CountDown(OnCountTimeListener onCountTimeListener) {
            mOnCountTimeListener = onCountTimeListener;
            mTimerExecutor = Executors.newSingleThreadScheduledExecutor();
        }

        @Override
        public void start(long initTime) {
            if(initTime <= 0)
                return ;

            showdown();
            mTimerExecutor = Executors.newSingleThreadScheduledExecutor();

            mLeftTime = initTime;

            mTimerExecutor.scheduleAtFixedRate(new Runnable() {
                Runnable mCountTimeTask = new CountTimeTask();
                @Override
                public void run() {
                    sHandler.post(mCountTimeTask);
                }
            }, 0, PERIOD, TimeUnit.MILLISECONDS);

            if(mLifeCycleListener != null)
                mLifeCycleListener.onStart();
        }

        @Override
        public void stop() {
            if(!mTimerExecutor.isShutdown()){
                showdown();

                if(mLifeCycleListener != null)
                    mLifeCycleListener.onStop();
            }
        }

        private void showdown(){
            if(!mTimerExecutor.isShutdown())
                mTimerExecutor.shutdown();
        }

        private class CountTimeTask implements Runnable {
            @Override
            public void run() {
                mLeftTime -= PERIOD;
                if(mLeftTime >= 0){
                    countTime(mLeftTime);
                }else{
                    onEnd();
                }
            }

            private void countTime(long curTime) {
                if (mOnCountTimeListener != null)
                    mOnCountTimeListener.countTime(curTime);
            }

            private void onEnd() {
                if(mOnCountTimeListener != null)
                    mOnCountTimeListener.onEnd();
            }
        }
    }
}
