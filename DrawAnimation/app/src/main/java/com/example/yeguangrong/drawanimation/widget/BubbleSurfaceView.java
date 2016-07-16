package com.example.yeguangrong.drawanimation.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.yeguangrong.drawanimation.R;
import com.example.yeguangrong.drawanimation.logic.DrawBubbleThread;
import com.example.yeguangrong.drawanimation.utils.DeviceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * Created by yeguangrong on 2016/7/15.
 */
public class BubbleSurfaceView extends SurfaceView implements SurfaceHolder.Callback{


    private boolean mIsSurfaceCreated;

    private SurfaceHolder mSurfaceHolder;//

    public static final int MSG_HIDE = 0;
    public static final int MSG_SHOW = 1;

    private Bitmap bitmap;
    private Random mRandom;
    private boolean mIsStart = false;

    private int mWitdth = DeviceUtil.getScreenWidth(getContext());
    private int mHeight = 1200;//DeviceUtil.getScreenHeight(getContext());

    private Paint mPaint;

    private List<Bubble> mBubbles = new ArrayList<Bubble>();

//    private Thread mDrawThread;
    private Looper mLooper;//
    private DrawBubbleThread mDrawBubbleTask;

    public BubbleSurfaceView(Context context) {
        super(context);
        init();
    }

    public BubbleSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){

        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.praise_eight);
        mRandom = new Random();

        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(this);
        mDrawBubbleTask = new DrawBubbleThread(mSurfaceHolder,this,mBubbles,bitmap);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        mIsStart = false;
    }

    /**
     * 用户点击产生气泡的入口
     */
    public void showBubble(){

        mIsStart = true;
        Bubble bubble = new Bubble();
        mBubbles.add(bubble);
        Log.e("thread","bubbleNum = "+mBubbles.size());

        if (!mDrawBubbleTask.isAlive()){
            Log.e("thread","thread start");
            mDrawBubbleTask.start();
        }else {
            mDrawBubbleTask.showBubble();
        }

    }

    /**
     * 清空气泡
     */
    public void clearBubble(){
        if (mBubbles.size() > 0){
            mBubbles.clear();
        }
    }

    /**
     * 每个气泡类对象
     */
    public class Bubble{

        private PointF startPoint;//起始点
        private PointF secondPoint;//第二个点
        private PointF thirdPoint;//第三个点
        private PointF endPoints;//最后的一个点

        public float dx;//x轴偏移量
        public float dy;//y轴偏移量

        public float offsetY;//y坐标移动的距离与

        public Bubble(){
            init();
        }

        public void init(){

            startPoint = new PointF(mWitdth/4,0);
            secondPoint = getRandomPoint(1);

            thirdPoint = getRandomPoint(2);
            endPoints = getRandomPoint(3);

            dx = startPoint.x;
            dy = startPoint.y;
        }

        public void update(Bubble bubble){

            dy = dy + 6;
            offsetY = dy / 1200f;
            dx = getBezierX(offsetY,bubble);

        }

        /**
         * 随着y坐标的变化，x坐标按照贝塞尔曲线的轨迹移动
         * @param y
         * @param bubble
         * @return
         */
        private float getBezierX(float y, Bubble bubble) {

            //3阶贝塞尔公式
            return bubble.startPoint.x*(1-y)*(1-y) + 3*bubble.secondPoint.x*y*(1-y)*(1-y) + 3*bubble.thirdPoint.x*y*y*(1-y) + bubble.endPoints.x*y*y*y;
        }

        /**
         * 获取随机的点
         * @param index 区分第几个点
         * @return
         */
        private PointF getRandomPoint(int index){

            PointF pointF = new PointF();
            float random = mRandom.nextFloat() * 2 - 1;//获取[-1,1]范围内的随机数
            pointF.x = random*mWitdth/4 + mWitdth/4;
            switch (index){
                case 1:
                    pointF.y = 200 + 400*mRandom.nextFloat();
                    break;
                case 2:
                    pointF.y = 600 + 400*mRandom.nextFloat();
                    break;
                default:
                    pointF.y = 1200;
                    break;
            }

            Log.e("point", "secondX = " + pointF.x + " secondY = " + pointF.y);

            return pointF;

        }

    }


}
