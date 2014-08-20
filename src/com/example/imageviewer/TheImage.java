
package com.example.imageviewer;

import java.io.File;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class TheImage extends ImageView {

    public int initWidth = 0;
    public int initHeight = 0;

    public float initScale = 1f;

    public TheImage(Context context) {
        super(context);
    }

    public TheImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TheImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        try {
            Bitmap bitmap = ((BitmapDrawable) getDrawable()).getBitmap();
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
        } catch (Exception e) {

        }
    }

    public void load(File file, int w, int h) {
        Bitmap bitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        options.inJustDecodeBounds = false;
        float hei = options.outHeight;
        float wid = options.outWidth;

        float r = 1;
        if (wid > w || hei > h) {
            float beWidth = wid / w;
            float beHeight = hei / h;
            if (beWidth < beHeight) {
                r = beHeight;
            } else {
                r = beWidth;
            }
        }
        initWidth = (int) (wid / r);
        initHeight = (int) (hei / r);
        initScale = 1 / r;
        if (r < 1) {
            r = 1;
        }
        options.inSampleSize = (int) r;
        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        setImageBitmap(bitmap);
    }

}
