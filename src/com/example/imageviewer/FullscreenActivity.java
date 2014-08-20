
package com.example.imageviewer;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

public class FullscreenActivity extends Activity {
    ImageViewer imageViewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        imageViewer = (ImageViewer) findViewById(R.id.fullscreen_content);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                imageViewer.test();
            }
        }, 2000);
    }
}
