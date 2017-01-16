package com.yus.clockview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by ${余胜} on 2016/8/22.
 */
public class ClockView extends View {

    private Paint bigCiclePaint;
    /**
     * 小圆心画笔
     */
    private Paint smallCiclePaint;
    /**
     * 画矩形刻度画笔
     */
    private Paint calibrationPaint;
    /**
     * 画刻度数字的画笔
     */
    private Paint numPaint;
    /**画时针的画笔*/
    private Paint hourPaint;
    private Paint minutePaint;
    private Paint secondPaint;


    public ClockView(Context context) {
        this(context,null);
    }

    public ClockView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        bigCiclePaint = new Paint();
        bigCiclePaint.setColor(Color.BLUE);
        bigCiclePaint.setStyle(Paint.Style.STROKE);
        bigCiclePaint.setStrokeWidth(34);

        smallCiclePaint = new Paint();
        smallCiclePaint.setColor(Color.RED);
        smallCiclePaint.setStyle(Paint.Style.FILL);

        calibrationPaint = new Paint();
        calibrationPaint.setColor(Color.BLUE);
        calibrationPaint.setStyle(Paint.Style.FILL);
        calibrationPaint.setStrokeWidth(20);

        numPaint = new Paint();
        numPaint.setColor(Color.RED);
        numPaint.setAntiAlias(true);
        numPaint.setTextAlign(Paint.Align.CENTER);
        numPaint.setTextSize(40);

        hourPaint = new Paint();
        hourPaint.setColor(Color.parseColor("#3F51B5"));
        hourPaint.setStrokeWidth(25);

        minutePaint = new Paint();
        minutePaint.setColor(Color.DKGRAY);
        minutePaint.setStrokeWidth(18);

        secondPaint = new Paint();
        secondPaint.setColor(Color.parseColor("#FF4081"));
        secondPaint.setStrokeWidth(11);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        setMeasuredDimension();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int mHour, mMinute, mSecond;

    public void updateShow(int hour, int minute, int second){
        mHour = hour;
        mMinute = minute;
        mSecond = second;
        invalidate();
    }

    private int cX;
    private int cY;
    private float bigRadius;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int clockWidth = getWidth();
        cX=clockWidth/2;
        cY=clockWidth/2;
        bigRadius=(float) clockWidth/2-75;

        canvas.drawCircle(cX,cY,bigRadius,bigCiclePaint);//画大表盘
        canvas.drawCircle(cX,cY,30,smallCiclePaint);//画小圆心

//        canvas.save()方法和canvas.restore()方法都是成对出现的，这一点要注意。
        for (int i = 1; i <13 ; i++) {
            //在旋转之前保存画布状态
            canvas.save();
            canvas.rotate(i*30,cX,cY);//先画1点，从1到12
            canvas.drawLine(cX,cY-bigRadius+34/2,cX,cY-bigRadius+34/2+18,calibrationPaint);//画刻度
            //画刻度上数字,第三个参数表示文字的基准线
            canvas.drawText(i+"",cX,cY-bigRadius+34/2+18+50,numPaint);
            //恢复画布状态
            canvas.restore();
        }
        //获得当前小时
        int hour = mHour;
        int minute  = mMinute;
        int second  = mSecond;

//        Log.d("alan","hour----->"+hour);
//        Log.d("alan","minute--->"+minute);
        Log.d("alan","second--->"+second);
        float hourDegree=((float)minute/60+hour)*30;//画时针时画布应该旋转的度数
//        Log.d("alan","(float)minute/60---->"+(float)minute/60);
//        Log.d("alan","hourDegree--->"+hourDegree);
        canvas.save();
        canvas.rotate(hourDegree,cX,cY);//旋转画布准备画时针
        canvas.drawLine(cX,cY-200,cX,cY,hourPaint);//画时针
        canvas.restore();//重置画布

        canvas.save();
        float minuteDegree=((float)minute)/60*360;//画分针时画布应该旋转的度数
//        Log.d("alan","minuteDegree--->"+minuteDegree);
        canvas.rotate(minuteDegree,cX,cY);
        canvas.drawLine(cX,cY-250,cX,cY,minutePaint);
        canvas.restore();

        canvas.save();
        float secondDegree=((float)second)/60*360;//画秒针时画布应该旋转的度数
//        Log.d("alan","secondDegree--->"+secondDegree);
        canvas.rotate(secondDegree,cX,cY);//这里是顺时针旋转
        canvas.drawLine(cX,cY-300,cX,cY,secondPaint);
        canvas.restore();

//        postInvalidateDelayed(1000);//每1秒钟重绘一次
    }
}
