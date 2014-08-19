
package com.example.imageviewer;

import java.io.File;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ImageViewer extends RelativeLayout {

    TheImage leftImage;
    TheImage middleImage;
    TheImage rightImage;
    int scrollDis = 30;
    boolean scrolling = false;
    boolean resizing = false;

    PointF startPoint = new PointF(0f, 0f);
    PointF lastPoint = new PointF(0f, 0f);

    ArrayList<File> files = new ArrayList<File>();
    int imageIndex = 0;

    public ImageViewer(Context context) {
        super(context);
    }

    public ImageViewer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageViewer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * test only
     */
    public void initImage() {
        File[] filess = new File("/storage/emulated/0/androiddesk/wallpapers/").listFiles();
        for (int i = 0; i < filess.length; i++) {
            File file = filess[i];
            files.add(file);
        }
    }

    public void setDataSource(String path) {
        File FirstImage = new File(path);
        if (isImageFile(FirstImage)) {
            File parentFile = FirstImage.getParentFile();
            File[] listFiles = parentFile.listFiles();
            for (int i = 0; i < parentFile.listFiles().length; i++) {
                File file = listFiles[i];
                if (isImageFile(file)) {
                    files.add(file);
                    if (file.equals(FirstImage)) {
                        imageIndex = files.size() - 1;
                    }
                }
            }
            setupImages();
        }
    }

    public void setupImages() {
        if (files.size() == 1) {
            leftImage = null;
            middleImage = new TheImage(getContext());
            rightImage = null;
        } else {
            if (imageIndex == 0) {
                leftImage = null;
                middleImage = new TheImage(getContext());
                rightImage = new TheImage(getContext());
            } else if (imageIndex == files.size() - 1) {
                rightImage = null;
                middleImage = new TheImage(getContext());
                leftImage = new TheImage(getContext());
            } else {
                leftImage = new TheImage(getContext());
                middleImage = new TheImage(getContext());
                rightImage = new TheImage(getContext());
            }
        }

        if (leftImage != null) {
            leftImage.load(files.get(imageIndex - 1), getWidth(), getHeight());
        }

        middleImage.load(files.get(imageIndex), getWidth(), getHeight());

        if (rightImage != null) {
            rightImage.load(files.get(imageIndex + 1), getWidth(), getHeight());
        }
    }

    public boolean isImageFile(File file) {
        return true;
    }

    public float distance(PointF pointF, PointF pointF2) {
        return PointF.length(pointF.x - pointF2.x, pointF.y - pointF2.y);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        System.out.println("onInterceptTouchEvent");
        boolean flag = false;
        PointF pointF;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                scrolling = false;
                flag = false;
                break;
            case MotionEvent.ACTION_MOVE:
                pointF = new PointF(ev.getX(0), ev.getY(0));
                if (scrolling) {
                    flag = true;
                } else {
                    if (distance(pointF, startPoint) > scrollDis) {
                        flag = true;
                        scrolling = true;
                    } else {
                        flag = false;
                    }
                }
                lastPoint = pointF;
                break;
            case MotionEvent.ACTION_CANCEL:
                // TODO
                break;
            case MotionEvent.ACTION_UP:
                flag = scrolling;
                scrolling = false;
                break;

            default:
                break;
        }
        System.out.println(flag);
        return flag;
    }

    public void scrollImageBy(float x, float y) {
        if (resizing) {

        } else {
            scrollThreeBodyBy(x, y);
        }
    }

    public void scrollThreeBodyBy(float x, float y) {
        System.out.println(x + " , " + y);
        if (middleImage != null) {

        }
        if (leftImage != null) {

        }
        if (rightImage != null) {

        }
    }

    public void scroll() {

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // System.out.println("onTouchEvent");
        PointF pointF;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                scrolling = false;
                startPoint = new PointF(ev.getX(0), ev.getY(0));
                lastPoint = new PointF(ev.getX(0), ev.getY(0));
                break;
            case MotionEvent.ACTION_MOVE:
                pointF = new PointF(ev.getX(0), ev.getY(0));
                if (scrolling) {
                    scrollImageBy(pointF.x - lastPoint.x, pointF.y - lastPoint.y);
                } else {
                    if (distance(pointF, startPoint) > scrollDis) {
                        scrolling = true;
                        scrollImageBy(pointF.x - lastPoint.x, pointF.y - lastPoint.y);
                    }
                }
                lastPoint = pointF;
                break;
            case MotionEvent.ACTION_CANCEL:
                // TODO
                break;
            case MotionEvent.ACTION_UP:
                scrolling = false;
                break;

            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void onScrollDone() {
        // left push
        // middle-->left,left-->right,right-->middle

        // right push
        // middlg-->right,right-->left,left-->middle
    }

}
