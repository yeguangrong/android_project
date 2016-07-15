
package com.example.drawanimation;

import com.example.drawanimation.widget.BubbleView;
import com.example.drawanimation.widget.CoolDownView;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    private CoolDownView mCoolDownView;
    private BubbleView mBubbleView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBubbleView = (BubbleView) findViewById(R.id.bubble_view);
        mBubbleView.showBubble();
    }
}
