
package com.example.imageviewer;

import java.io.File;
import java.util.ArrayList;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
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

    Point leftPoint;
    Point middlePoint;
    Point rightPoint;

    AnimatorSet animatorSet;

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
    public void test() {
        File[] filess = new File("/storage/emulated/0/androidesk/wallpapers/").listFiles();
        for (int i = 0; i < filess.length; i++) {
            File file = filess[i];
            files.add(file);
        }
        imageIndex = 0;
        setupImages();
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
            LayoutParams paramsl = new LayoutParams(leftImage.initWidth, leftImage.initHeight);
            leftImage.setLayoutParams(paramsl);
            addView(leftImage);
            leftImage.setX((getWidth() - leftImage.initWidth) / 2 - getWidth());
            leftImage.setY((getHeight() - leftImage.initHeight) / 2);
            leftPoint = new Point((getWidth() - leftImage.initWidth) / 2 - getWidth(),
                    (getHeight() - leftImage.initHeight) / 2);
        }

        middleImage.load(files.get(imageIndex), getWidth(), getHeight());
        LayoutParams paramsm = new LayoutParams(middleImage.initWidth, middleImage.initHeight);
        middleImage.setLayoutParams(paramsm);
        addView(middleImage);
        middleImage.setX((getWidth() - middleImage.initWidth) / 2);
        middleImage.setY((getHeight() - middleImage.initHeight) / 2);
        middlePoint = new Point((getWidth() - middleImage.initWidth) / 2,
                (getHeight() - middleImage.initHeight) / 2);

        if (rightImage != null) {
            rightImage.load(files.get(imageIndex + 1), getWidth(), getHeight());
            LayoutParams paramsr = new LayoutParams(rightImage.initWidth, rightImage.initHeight);
            rightImage.setLayoutParams(paramsr);
            addView(rightImage);
            rightImage.setX((getWidth() - rightImage.initWidth) / 2);
            rightImage.setY((getHeight() - rightImage.initHeight) / 2);
            rightImage.setImageAlpha(0);
            rightImage.setScaleX(0.001f);
            rightImage.setScaleY(0.001f);
            rightPoint = new Point((getWidth() - rightImage.initWidth) / 2,
                    (getHeight() - rightImage.initHeight) / 2);
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
        boolean flag = false;
        PointF pointF;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (animatorSet != null && animatorSet.isRunning()) {
                    animatorSet.cancel();
                }
                scrolling = false;
                flag = false;
                startPoint = new PointF(ev.getX(0), ev.getY(0));
                lastPoint = new PointF(ev.getX(0), ev.getY(0));
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
                break;

            default:
                break;
        }
        return flag;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        PointF pointF;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                // TODO 使用开始滚动时的点更好些
                pointF = new PointF(ev.getX(0), ev.getY(0));
                if (scrolling) {
                    scrollImageBy(pointF.x - startPoint.x, pointF.y - startPoint.y);
                } else {
                    if (distance(pointF, startPoint) > scrollDis) {
                        scrolling = true;
                        scrollImageBy(pointF.x - startPoint.x, pointF.y - startPoint.y);
                    }
                }
                lastPoint = pointF;
                break;
            case MotionEvent.ACTION_CANCEL:
                // TODO
                break;
            case MotionEvent.ACTION_UP:
                if (scrolling) {
                    scrolling = false;
                }
                onDragEnd(ev.getX(0), ev.getY(0));
                break;

            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void onDragEnd(float ex, float ey) {
        if (resizing) {

        } else {
            if (middleImage.getScaleX()<0.5) {
                scrollRight();
            } else if (middleImage.getX() < middlePoint.x - getWidth() / 2) {
                scrollLeft();
            } else {
                scrollBack();
            }
        }
    }

    public void scrollRight() {
        imageIndex--;
        if (rightImage != null) {
            removeView(rightImage);
        }

        rightImage = middleImage;
        rightPoint = new Point(middlePoint.x, middlePoint.y);

        middleImage = leftImage;
        middlePoint = new Point(leftPoint.x + getWidth(), leftPoint.y);

        if (imageIndex > 0) {
            leftImage = new TheImage(getContext());
            leftImage.load(files.get(imageIndex - 1), getWidth(), getHeight());
            LayoutParams paramsl = new LayoutParams(leftImage.initWidth, leftImage.initHeight);
            leftImage.setLayoutParams(paramsl);
            addView(leftImage);
            leftImage.setX((getWidth() - leftImage.initWidth) / 2 - getWidth());
            leftImage.setY((getHeight() - leftImage.initHeight) / 2);
            leftPoint = new Point((getWidth() - leftImage.initWidth) / 2 - getWidth(),
                    (getHeight() - leftImage.initHeight) / 2);
        } else {
            leftImage = null;
            leftPoint = null;
        }
        scrollBack();
    }

    public void scrollLeft() {
        imageIndex++;
        if (leftImage != null) {
            removeView(leftImage);
        }

        leftImage = middleImage;
        leftPoint = new Point(middlePoint.x - getWidth(), middlePoint.y);

        middleImage = rightImage;
        middlePoint = new Point(rightPoint.x, rightPoint.y);

        if (imageIndex < files.size() - 1) {
            rightImage = new TheImage(getContext());
            rightImage.load(files.get(imageIndex + 1), getWidth(), getHeight());
            LayoutParams paramsr = new LayoutParams(rightImage.initWidth, rightImage.initHeight);
            rightImage.setLayoutParams(paramsr);
            addView(rightImage);
            rightImage.setX((getWidth() - rightImage.initWidth) / 2);
            rightImage.setY((getHeight() - rightImage.initHeight) / 2);
            rightImage.setImageAlpha(0);
            rightImage.setScaleX(0.001f);
            rightImage.setScaleY(0.001f);
            rightPoint = new Point((getWidth() - rightImage.initWidth) / 2,
                    (getHeight() - rightImage.initHeight) / 2);

        } else {
            rightImage = null;
            rightPoint = null;
        }
        scrollBack();
    }

    public void scrollBack() {
        System.out.println("childcount " + getChildCount());
        animatorSet = new AnimatorSet();
        ArrayList<Animator> animators = new ArrayList<Animator>();
        if (leftImage != null) {
            ObjectAnimator animatorLeft = ObjectAnimator.ofFloat(leftImage, "x",
                    leftImage.getX(), leftPoint.x);
            animators.add(animatorLeft);
        }

        ObjectAnimator holderMiddleX = ObjectAnimator.ofFloat(middleImage, "x",
                middleImage.getX(), middlePoint.x);
        ObjectAnimator holderMiddleAlpha = ObjectAnimator.ofFloat(middleImage, "imageAlpha",
                middleImage.getImageAlpha(), 255);
        ObjectAnimator holderMiddleScaleX = ObjectAnimator.ofFloat(middleImage, "scaleX",
                middleImage.getScaleX(), 1f);
        ObjectAnimator holderMiddleScaleY = ObjectAnimator.ofFloat(middleImage, "scaleY",
                middleImage.getScaleY(), 1f);
        
        animators.add(holderMiddleX);
        animators.add(holderMiddleAlpha);
        animators.add(holderMiddleScaleX);
        animators.add(holderMiddleScaleY);

        if (rightImage != null) {
            ObjectAnimator holderRightX = ObjectAnimator.ofFloat(rightImage, "x",
                    rightImage.getX(), rightPoint.x);
            ObjectAnimator holderRightAlpha = ObjectAnimator.ofFloat(rightImage, "imageAlpha",
                    rightImage.getImageAlpha(), 255);
            ObjectAnimator holderRightScaleX = ObjectAnimator.ofFloat(rightImage, "scaleX",
                    rightImage.getScaleX(), 0.001f);
            ObjectAnimator holderRightScaleY = ObjectAnimator.ofFloat(rightImage, "scaleY",
                    rightImage.getScaleY(), 0.001f);
            
            animators.add(holderRightX);
            animators.add(holderRightAlpha);
            animators.add(holderRightScaleX);
            animators.add(holderRightScaleY);
        }
        animatorSet.playTogether(animators);
        animatorSet.setDuration(300);
        animatorSet.start();
    }

    public void scrollImageBy(float x, float y) {
        if (resizing) {

        } else {
            scrollThreeBodyBy(x, y);
        }
    }

    public void scrollThreeBodyBy(float x, float y) {
        // TODO 如果是动画过程中动画停止，按住滑动，这里得到的结果将是错的
        if (x > 0) {
            // 右侧，rightImage不动
            if (leftImage == null) {
                middleImage.setX(middlePoint.x);
            } else {
                leftImage.setX(leftPoint.x + x);

                // middle shrink
                float scale = (1 - x / getWidth());
                middleImage.setScaleX(scale);
                middleImage.setScaleY(scale);
                middleImage.setImageAlpha((int) (255 * (1 - x / getWidth())));
            }
            if (rightImage != null) {
                rightImage.setImageAlpha(0);
            }
        } else {
            // 左侧，leftImage不动
            if (rightImage == null) {
                middleImage.setX(middlePoint.x);
            } else {
                middleImage.setX(middlePoint.x + x);
                // expand right
                float scale = (-x / getWidth());
                rightImage.setScaleX(scale);
                rightImage.setScaleY(scale);
                rightImage.setImageAlpha((int) (255 * (-x / getWidth())));
            }
            if (leftImage != null) {
                leftImage.setImageAlpha(0);
            }
        }
    }

    public void onScrollDone() {
        // left push
        // middle-->left,left-->right,right-->middle

        // right push
        // middlg-->right,right-->left,left-->middle
    }

}
