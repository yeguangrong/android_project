
package com.example.yeguangrong.drawanimation;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.yeguangrong.drawanimation.widget.BubbleView;

public class MainActivity extends Activity {

    private BubbleView mBubbleView;

    private ImageView image;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBubbleView = (BubbleView) findViewById(R.id.bubble_view);
        image = (ImageView) findViewById(R.id.image);
//        mBubbleView.showBubble();
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBubbleView.showBubble();
            }
        });

    }
}
