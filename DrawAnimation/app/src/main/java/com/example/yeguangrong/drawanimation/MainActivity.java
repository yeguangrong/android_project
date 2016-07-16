
package com.example.yeguangrong.drawanimation;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.yeguangrong.drawanimation.widget.BubbleSurfaceView;
import com.example.yeguangrong.drawanimation.widget.BubbleView;

public class MainActivity extends Activity {

    private BubbleSurfaceView mBubbleView;

    private ImageView image;
    private ViewGroup mRootView;

    Handler mThreadHandler;

    Handler mThreadHandler2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        long threadID = Thread.currentThread().getId();
        Log.e("thread", "threadId " + threadID);
        mBubbleView = (BubbleSurfaceView) findViewById(R.id.bubble_view);
        image = (ImageView) findViewById(R.id.image);
        mRootView = (RelativeLayout) findViewById(R.id.root_view);
//        mBubbleView.showBubble();
        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBubbleView.showBubble();
            }
        });

        new Thread("james"){

            int x = 0;
            @Override
            public void run() {
                Looper.prepare();

                mThreadHandler = new Handler(Looper.myLooper()){
                    @Override
                    public void handleMessage(Message msg) {

                        if (x<10){
                            mThreadHandler.sendEmptyMessageDelayed(0,1000);
                        }
                        x++;
                    }
                };

                mThreadHandler.sendEmptyMessageDelayed(0,2000);

                Looper.loop();
            }
        }.start();

        HandlerThread handlerThread = new HandlerThread("james2");
        handlerThread.start();

        mThreadHandler2 = new Handler(handlerThread.getLooper()){
            int x = 0;
            @Override
            public void handleMessage(Message msg) {
                if (x<10){
                    mThreadHandler2.sendEmptyMessageDelayed(0,1000);
                }
                x++;
            }
        };

        mThreadHandler2.sendEmptyMessageDelayed(0,2000);

    }



    @Override
    protected void onStop() {
        super.onStop();
        mBubbleView.clearBubble();
    }
}
