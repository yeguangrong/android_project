package com.example.drawanimation.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import com.example.drawanimation.R;

/**
 * 看直播领取金币倒计时冷却动画
 * @author yeguangrong
 *
 */
public class CoolDownView extends View{

    private Paint mPaint;
    
    private RectF mRectF;
    
    private int mStartAngle;
    
    private int mSweepAngle;
    
    private int COOL_GAP = 1000;
    
    
    private final int MSG_START_COUNT_DOWN = 0;
    private final int MSG_STOP_COUNT_DOWN = 1;
    
    private boolean mIsStarting = false;
    
    
    public CoolDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
        init();
    }

    public CoolDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init();
    }

    public CoolDownView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init();
    }
    
    private void init(){
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.half_transparency_black));
        mPaint.setStyle(Style.FILL);
        mRectF = new RectF(0, 0, 240, 240);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        
        if(mIsStarting){
            canvas.drawArc(mRectF, mStartAngle, mSweepAngle, true, mPaint);
        }
        
    }
    
    public void startCountDown(int startAngle, int sweepAngle){
        mStartAngle = startAngle;
        mSweepAngle = sweepAngle;
        mIsStarting = true;
        mHandler.sendEmptyMessage(MSG_START_COUNT_DOWN);
        
        
    }
    
    
    private Handler mHandler = new Handler(){
        
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_START_COUNT_DOWN:
                    mHandler.removeMessages(MSG_START_COUNT_DOWN);
                    invalidate();
                    mStartAngle++;
                    mSweepAngle--;
                    if(mSweepAngle>0){
                        mHandler.sendEmptyMessageDelayed(MSG_START_COUNT_DOWN, COOL_GAP);
                    }else{
                        mIsStarting = false;
                    }
                    break;
                case MSG_STOP_COUNT_DOWN:
                    
                    break;
                default:
                    break;
            }
        };
        
    };
    
    
    
    

}
