package com.example.drawanimation.widget;

import java.util.Random;

import com.example.drawanimation.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 通过绘制的方式实现气泡动画
 * @author yeguangrong
 *
 */
public class BubbleView extends View{

    public static final int MSG_HIDE = 0;
    public static final int MSG_SHOW = 1;
    
    private DrawTask mDrawTask;
    private Bitmap bitmap;
    private Matrix matrix;
    private Random mRandom;
    private boolean mIsStart = false;
    private float dx = 0;
    private float dy = 0;
    
    public BubbleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
        init();
    }

    public BubbleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init();
    }

    public BubbleView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init();
    }
    
    private void init(){
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.praise_eight);
        mRandom = new Random(10);
        matrix = new Matrix();
        mDrawTask = new DrawTask();
        
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        if(mIsStart){
            float xx = (float) (Math.random() * 5);//mRandom.nextFloat()*10-5;
            Log.e("random", "x = "+xx);
            if(dy<300){
                dx = dx + 300;
            }
            if(dy>=300 && dy<600){
                dx = dx - 300;
            }
            if(dy>=600 && dy<=900){
                dx = dx + 300;
            }
            if(dy>=900 && dy<=1020){
                dx = dx - 300;
            }
            dy = dy + 300;
            
            matrix.setTranslate(dx, dy);
            
            canvas.drawBitmap(bitmap, matrix, null);
            if(dx > 600 || dy > 1020){
                mHandler.sendEmptyMessage(MSG_HIDE);
            }else{
                mHandler.sendEmptyMessageDelayed(MSG_SHOW, 400);
            }
        }
    }
    
    Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case MSG_SHOW:
                    mHandler.removeMessages(MSG_SHOW);
                    mIsStart = true;
                    invalidate();
                    break;
                case MSG_HIDE:
                    mHandler.removeMessages(MSG_HIDE);
                    mIsStart = false;
                    break;
                default:
                    break;
            }
        }  
    };
    
    class DrawTask implements Runnable{

        @Override
        public void run() {
            // TODO Auto-generated method stub
            
        }
        
    }
    
    public void showBubble(){
        mHandler.sendEmptyMessageDelayed(MSG_SHOW, 500);
    }

}
