package com.example.yeguangrong.drawanimation.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.yeguangrong.drawanimation.R;
import com.example.yeguangrong.drawanimation.utils.DeviceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 通过绘制的方式实现气泡动画  PS:对于每个新生成的气泡都会以贝塞尔曲线的轨迹进行运动，该实现方式只能从主线程中显示动画
 * @author yeguangrong
 *
 */
public class BubbleView extends View{

    public static final int MSG_HIDE = 0;
    public static final int MSG_SHOW = 1;

    private Bitmap bitmap;
    private Random mRandom;
    private boolean mIsStart = false;

    private int mWitdth = DeviceUtil.getScreenWidth(getContext());
    private int mHeight = 1200;//DeviceUtil.getScreenHeight(getContext());

    private Paint mPaint;

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
        mRandom = new Random();

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
                    mBubbles.remove(bubble);
                    mHandler.sendEmptyMessage(MSG_HIDE);
//                    invalidate();
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

        //3阶贝塞尔公式
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

            startPoint = new PointF(mWitdth/4,0);
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

        Log.e("point","secondX = "+ pointF.x+" secondY = "+pointF.y);

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
                    invalidate();
                    break;
                default:
                    break;
            }
        }  
    };

    public void showBubble(){
        Bubble bubble = new Bubble();
        mBubbles.add(bubble);
        mHandler.sendEmptyMessage(MSG_SHOW);
    }

    /**
     * 清空气泡
     */
    public void clearBubble(){
        if (mBubbles.size() > 0){
            mBubbles.clear();
        }
        mHandler.sendEmptyMessage(MSG_HIDE);
    }

}
