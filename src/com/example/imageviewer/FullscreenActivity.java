
package com.example.imageviewer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

public class FullscreenActivity extends Activity {
    ImageViewer imageViewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        imageViewer = (ImageViewer) findViewById(R.id.fullscreen_content);
        imageViewer.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            
            @SuppressLint("NewApi")
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                if (imageViewer.getWidth()>0) {                    
                    imageViewer.test();
                    if (android.os.Build.VERSION.SDK_INT>=16) {
                        imageViewer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }else {                        
                        imageViewer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                }
            }
        });
    }
}
