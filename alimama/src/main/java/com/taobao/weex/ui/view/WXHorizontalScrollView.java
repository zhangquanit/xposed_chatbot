package com.taobao.weex.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;
import com.taobao.weex.ui.view.gesture.WXGesture;
import com.taobao.weex.ui.view.gesture.WXGestureObservable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class WXHorizontalScrollView extends HorizontalScrollView implements IWXScroller, WXGestureObservable {
    private ScrollViewListener mScrollViewListener;
    private List<ScrollViewListener> mScrollViewListeners;
    private boolean scrollable = true;
    private WXGesture wxGesture;

    public interface ScrollViewListener {
        void onScrollChanged(WXHorizontalScrollView wXHorizontalScrollView, int i, int i2, int i3, int i4);
    }

    public void destroy() {
    }

    public WXHorizontalScrollView(Context context) {
        super(context);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        setOverScrollMode(2);
    }

    public WXHorizontalScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    /* access modifiers changed from: protected */
    public void onScrollChanged(int i, int i2, int i3, int i4) {
        super.onScrollChanged(i, i2, i3, i4);
        if (this.mScrollViewListener != null) {
            this.mScrollViewListener.onScrollChanged(this, i, i2, i3, i4);
        }
        if (this.mScrollViewListeners != null) {
            for (ScrollViewListener onScrollChanged : this.mScrollViewListeners) {
                onScrollChanged.onScrollChanged(this, i, i2, i3, i4);
            }
        }
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.mScrollViewListener = scrollViewListener;
    }

    public void addScrollViewListener(ScrollViewListener scrollViewListener) {
        if (this.mScrollViewListeners == null) {
            this.mScrollViewListeners = new CopyOnWriteArrayList();
        }
        if (!this.mScrollViewListeners.contains(scrollViewListener)) {
            this.mScrollViewListeners.add(scrollViewListener);
        }
    }

    public void removeScrollViewListener(ScrollViewListener scrollViewListener) {
        this.mScrollViewListeners.remove(scrollViewListener);
    }

    public void registerGestureListener(WXGesture wXGesture) {
        this.wxGesture = wXGesture;
    }

    public WXGesture getGestureListener() {
        return this.wxGesture;
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        boolean dispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
        return this.wxGesture != null ? dispatchTouchEvent | this.wxGesture.onTouch(this, motionEvent) : dispatchTouchEvent;
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.scrollable) {
            return true;
        }
        return super.onTouchEvent(motionEvent);
    }

    public boolean isScrollable() {
        return this.scrollable;
    }

    public void setScrollable(boolean z) {
        this.scrollable = z;
    }

    public Rect getContentFrame() {
        return new Rect(0, 0, computeHorizontalScrollRange(), computeVerticalScrollRange());
    }
}
