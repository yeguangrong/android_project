package com.example.yeguangrong.drawanimation.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.yeguangrong.drawanimation.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 通过绘制的方式实现气泡动画  PS:对于每个新生成的气泡都会以贝塞尔曲线的轨迹进行运动
 * @author yeguangrong
 *
 */
public class BubbleView extends View{

    public static final int MSG_HIDE = 0;
    public static final int MSG_SHOW = 1;
    public static final int MSG_BACK = 2;
    
    private DrawTask mDrawTask;
    private Bitmap bitmap;
    private Matrix matrix;
    private Random mRandom;
    private Random mRandom2;
    private boolean mIsStart = false;
    private float dx = 500;
    private float dy = 0;
    private boolean isToLeft;//
    private boolean isToRight;//
    private boolean isStartSwitch = false;

    //气泡区域
    private int mWitdth = 500;
    private int mHeight = 1200;

    private int [] pointsX = {100,400,700,1000};
    private int [] pointsY = {200,600,1000,1400};

    private PointF mStartPoint;
    private PointF mMiddlePoint;
    private PointF mMiddlePoint2;
    private PointF mEndPoint;
    private Paint mPaint;
    private Paint mPaint2;

    private Path mPath;
    private boolean mIsTouchUp = false;
    private boolean mIsInit = true;

    private List<Bubble> mBubbles = new ArrayList<Bubble>();
    
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
        mRandom = new Random(2);
        matrix = new Matrix();

        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Paint.Style.STROKE);

        mPaint2 = new Paint();
        mPaint2.setColor(Color.BLACK);
        mPaint2.setStrokeWidth(10);
        mPaint2.setStyle(Paint.Style.FILL);

        mStartPoint = new PointF(500,100);
        mMiddlePoint = new PointF(800,300);
        mMiddlePoint2 = new PointF(300,600);
        mEndPoint = new PointF(500,1200);
        mPath = new Path();

    }

    float r = 1;
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

        if(mIsStart){

            for (int i = 0; i< mBubbles.size();i++){
                Bubble bubble = mBubbles.get(i);
                canvas.save();
                canvas.drawBitmap(bitmap, bubble.dx, bubble.dy, null);
                bubble.update(bubble);
                canvas.restore();
                if(bubble.dy > mHeight){
                    mHandler.sendEmptyMessage(MSG_HIDE);
                    Log.e("remove", "remove = " + i);
//                    mBubbles.remove(bubble);
                }else{
                    mHandler.sendEmptyMessageDelayed(MSG_SHOW, 0);

                }
            }

        }
    }

    /**
     * 随着y坐标的变化，x坐标按照贝塞尔曲线的轨迹移动
     * @param y
     * @param bubble
     * @return
     */
    private float getBezierX(float y, Bubble bubble) {
        return bubble.startPoint.x*(1-y)*(1-y) + 3*bubble.secondPoint.x*y*(1-y)*(1-y) + 3*bubble.thirdPoint.x*y*y*(1-y) + bubble.endPoints.x*y*y*y;
    }

    /**
     * 每个气泡类对象
     */
    private class Bubble{

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

            startPoint = new PointF(500,0);
            secondPoint = getRandomPoint(1);

            thirdPoint = getRandomPoint(2);
            endPoints = getRandomPoint(3);

            dx = startPoint.x;
            dy = startPoint.y;
        }

        public void update(Bubble bubble){

            dy = dy + 4;
            offsetY = dy / 1200f;
            dx = getBezierX(offsetY,bubble);

        }

    }

    /**
     * 获取随机的点
     * @param index 区分第几个点
     * @return
     */
    private PointF getRandomPoint(int index){

        PointF pointF = new PointF();
        float random = mRandom.nextFloat() * 2 - 1;
        pointF.x = random*mWitdth + mWitdth;
        pointF.y = index * 400;

        Log.e("second","secondX = "+ pointF.x+" secondY = "+pointF.y);

        return pointF;

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
        Bubble bubble = new Bubble();
        mBubbles.add(bubble);
        mHandler.sendEmptyMessage(MSG_SHOW);
    }

}
