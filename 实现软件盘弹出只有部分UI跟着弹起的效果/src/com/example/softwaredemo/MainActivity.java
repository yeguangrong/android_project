
package com.example.softwaredemo;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;

public class MainActivity extends Activity {

    private View mRootView;
    
    boolean mSoftWareIsShowing = false;

    /**
     * 弹出软键盘时，键盘高度
     */
    private int mSoftWareHeight;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initListener();
    }
    
    private void initUI(){
        mRootView = findViewById(R.id.content_view); 
    }
    
    private void initListener(){
        //监听软键盘的弹出和收起，动态移动UI
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                // 需要先在Activity中设置键盘模式为adjustPan,然后在键盘弹出时把输入框移动上去，这样系统就不会改变屏幕大小
                Rect rect = new Rect();
                mRootView.getWindowVisibleDisplayFrame(rect);
                if(mRootView.getRootView().getHeight() - rect.bottom > 150){
                    mSoftWareHeight = mRootView.getRootView().getHeight() - rect.bottom;
//                    mContentView.scrollTo(0, mSoftWareHeight);
                    mRootView.setTranslationY(-mSoftWareHeight);
                    mSoftWareIsShowing = true;
                }else if(mSoftWareIsShowing){
                    mRootView.setTranslationY(0);
                    mSoftWareIsShowing = false;
                }
            }
        });
    }
}
