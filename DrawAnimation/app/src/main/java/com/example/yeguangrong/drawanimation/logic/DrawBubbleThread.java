package com.example.yeguangrong.drawanimation.logic;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.SurfaceHolder;

import com.example.yeguangrong.drawanimation.widget.BubbleSurfaceView;

import java.util.List;

/**
 * * 绘制气泡的线程
 * Created by yeguangrong on 2016/7/16.
 */
public class DrawBubbleThread extends Thread{

    public static final int MSG_HIDE = 0;
    public static final int MSG_SHOW = 1;

    Handler mHandler = null;
    SurfaceHolder mSurfaceHolder;
    private BubbleSurfaceView mBubbleSurfaceView;

    private List<BubbleSurfaceView.Bubble> mBubbles;

    private Bitmap mBitmap;

    Canvas canvas = null;

    public DrawBubbleThread(SurfaceHolder surfaceHolder, BubbleSurfaceView bubbleSurfaceView, List<BubbleSurfaceView.Bubble> bubbles,Bitmap bitmap){

        mSurfaceHolder = surfaceHolder;
        mBubbleSurfaceView = bubbleSurfaceView;
        mBubbles = bubbles;
        mBitmap = bitmap;

    }

    @Override
    public void run() {

        Looper.prepare();//为子线程创建消息队列

        mHandler =  new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_SHOW:
                        drawBubble();
                        break;
                    case MSG_HIDE:
                        mHandler.removeMessages(MSG_HIDE);
                        break;
                    default:
                        break;
                }
            }
        };

        Looper.loop();//启动子线程的消息队列

        showBubble();
    }

    //在子线程内发送绘制气泡的消息
    public void showBubble(){
        mHandler.sendEmptyMessageDelayed(MSG_SHOW,30);
    }

    /**
     * 真正在画布上执行绘制
     */
    private void drawBubble(){

        synchronized (mSurfaceHolder){
            canvas = mSurfaceHolder.lockCanvas();
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//清空气泡轨迹

            for (int i = 0; i< mBubbles.size();i++){

                BubbleSurfaceView.Bubble bubble = mBubbles.get(i);

                canvas.save();
                canvas.drawBitmap(mBitmap, bubble.dx, bubble.dy, null);
                bubble.update(bubble);
                canvas.restore();

                if (bubble.dy >= 1200){
                    mBubbles.remove(bubble);
                }
                mHandler.sendEmptyMessageDelayed(MSG_SHOW,30);
            }

            mSurfaceHolder.unlockCanvasAndPost(canvas);
        }

    }
}
