
package com.example.imageviewer;

import android.app.Activity;
import android.os.Bundle;

public class FullscreenActivity extends Activity {
    ImageViewer ImageViewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        ImageViewer = (ImageViewer) findViewById(R.id.fullscreen_content);
    }
}
