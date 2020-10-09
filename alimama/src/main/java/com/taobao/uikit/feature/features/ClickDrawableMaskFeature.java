package com.taobao.uikit.feature.features;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import com.taobao.uikit.R;
import com.taobao.uikit.feature.callback.CanvasCallback;
import com.taobao.uikit.feature.callback.TouchEventCallback;

public class ClickDrawableMaskFeature extends AbsFeature<ImageView> implements CanvasCallback, TouchEventCallback {
    private int mClickMaskColor;
    private boolean mClickMaskEnable;
    private boolean mIsPressed;

    public void afterDispatchDraw(Canvas canvas) {
    }

    public void afterDispatchTouchEvent(MotionEvent motionEvent) {
    }

    public void afterDraw(Canvas canvas) {
    }

    public void afterOnDraw(Canvas canvas) {
    }

    public void afterOnTouchEvent(MotionEvent motionEvent) {
    }

    public void beforeDispatchDraw(Canvas canvas) {
    }

    public void beforeDispatchTouchEvent(MotionEvent motionEvent) {
    }

    public void beforeOnDraw(Canvas canvas) {
    }

    public void constructor(Context context, AttributeSet attributeSet, int i) {
        TypedArray obtainStyledAttributes;
        this.mClickMaskEnable = true;
        this.mIsPressed = false;
        this.mClickMaskColor = 1996488704;
        if (attributeSet != null && (obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.ClickDrawableMaskFeature)) != null) {
            this.mClickMaskColor = obtainStyledAttributes.getColor(R.styleable.ClickDrawableMaskFeature_uik_clickMaskColor, this.mClickMaskColor);
            this.mClickMaskEnable = obtainStyledAttributes.getBoolean(R.styleable.ClickDrawableMaskFeature_uik_clickMaskEnable, true);
            obtainStyledAttributes.recycle();
        }
    }

    public void beforeDraw(Canvas canvas) {
        Drawable drawable;
        if (this.mClickMaskEnable && (drawable = ((ImageView) getHost()).getDrawable()) != null) {
            drawable.setCallback((Drawable.Callback) null);
            if (this.mIsPressed) {
                drawable.setColorFilter(this.mClickMaskColor, PorterDuff.Mode.SRC_ATOP);
            } else {
                drawable.clearColorFilter();
            }
        }
    }

    public void beforeOnTouchEvent(MotionEvent motionEvent) {
        if (this.mClickMaskEnable) {
            int action = motionEvent.getAction();
            if (action != 3) {
                switch (action) {
                    case 0:
                        this.mIsPressed = true;
                        requestLayoutHost();
                        invalidateHost();
                        return;
                    case 1:
                        break;
                    default:
                        return;
                }
            }
            this.mIsPressed = false;
            requestLayoutHost();
            invalidateHost();
        }
    }

    public void setClickMaskColor(int i) {
        this.mClickMaskColor = i;
        invalidateHost();
    }

    public void setClickMaskEnable(boolean z) {
        this.mClickMaskEnable = z;
        invalidateHost();
    }

    private void invalidateHost() {
        if (this.mHost != null) {
            ((ImageView) this.mHost).invalidate();
        }
    }

    private void requestLayoutHost() {
        if (this.mHost != null) {
            ((ImageView) this.mHost).requestLayout();
        }
    }
}
