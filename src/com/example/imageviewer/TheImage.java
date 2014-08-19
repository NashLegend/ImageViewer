
package com.example.imageviewer;

import java.io.File;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class TheImage extends ImageView {
    
    public TheImage(Context context) {
        super(context);
    }

    public TheImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TheImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void load(File file, int w, int h) {
        ImageView imageView = new ImageView(getContext());
        Bitmap bitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        options.inJustDecodeBounds = false;
        int hei = options.outHeight;
        int wid = options.outWidth;

        float r = 1;
        if (wid > w || hei > h) {
            float beWidth = wid / w;
            float beHeight = hei / h;
            if (beWidth < beHeight) {
                r = beWidth;
            } else {
                r = beHeight;
            }
        }
        wid /= r;
        hei /= r;
        imageView.setLayoutParams(new LayoutParams(wid, hei));
        options.inSampleSize = (int) r;
        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        imageView.setImageBitmap(bitmap);
    }

}
