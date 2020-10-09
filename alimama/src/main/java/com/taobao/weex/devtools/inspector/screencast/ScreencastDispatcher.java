package com.taobao.weex.devtools.inspector.screencast;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Base64OutputStream;
import android.view.View;
import com.taobao.weex.devtools.common.LogUtil;
import com.taobao.weex.devtools.inspector.elements.android.ActivityTracker;
import com.taobao.weex.devtools.inspector.jsonrpc.JsonRpcPeer;
import com.taobao.weex.devtools.inspector.jsonrpc.PendingRequestCallback;
import com.taobao.weex.devtools.inspector.protocol.module.Page;
import java.io.ByteArrayOutputStream;

public final class ScreencastDispatcher {
    private static final long FRAME_DELAY = 200;
    /* access modifiers changed from: private */
    public static float sBitmapScale = 1.0f;
    /* access modifiers changed from: private */
    public final ActivityTracker mActivityTracker = ActivityTracker.get();
    /* access modifiers changed from: private */
    public Handler mBackgroundHandler;
    /* access modifiers changed from: private */
    public Bitmap mBitmap;
    /* access modifiers changed from: private */
    public final BitmapFetchRunnable mBitmapFetchRunnable = new BitmapFetchRunnable();
    /* access modifiers changed from: private */
    public Canvas mCanvas;
    /* access modifiers changed from: private */
    public Page.ScreencastFrameEvent mEvent = new Page.ScreencastFrameEvent();
    /* access modifiers changed from: private */
    public final EventDispatchRunnable mEventDispatchRunnable = new EventDispatchRunnable();
    /* access modifiers changed from: private */
    public HandlerThread mHandlerThread;
    /* access modifiers changed from: private */
    public boolean mIsRunning;
    /* access modifiers changed from: private */
    public final Handler mMainHandler = new Handler(Looper.getMainLooper());
    /* access modifiers changed from: private */
    public Page.ScreencastFrameEventMetadata mMetadata = new Page.ScreencastFrameEventMetadata();
    /* access modifiers changed from: private */
    public JsonRpcPeer mPeer;
    /* access modifiers changed from: private */
    public Page.StartScreencastRequest mRequest;
    /* access modifiers changed from: private */
    public ByteArrayOutputStream mStream;
    /* access modifiers changed from: private */
    public final RectF mTempDst = new RectF();
    /* access modifiers changed from: private */
    public final RectF mTempSrc = new RectF();

    public static float getsBitmapScale() {
        return sBitmapScale;
    }

    public void startScreencast(JsonRpcPeer jsonRpcPeer, Page.StartScreencastRequest startScreencastRequest) {
        this.mRequest = startScreencastRequest;
        this.mHandlerThread = new HandlerThread("Screencast Thread");
        this.mHandlerThread.start();
        this.mPeer = jsonRpcPeer;
        this.mIsRunning = true;
        this.mStream = new ByteArrayOutputStream();
        this.mBackgroundHandler = new Handler(this.mHandlerThread.getLooper());
        this.mMainHandler.postDelayed(this.mBitmapFetchRunnable, FRAME_DELAY);
    }

    public void stopScreencast() {
        this.mBackgroundHandler.post(new CancellationRunnable());
    }

    private class BitmapFetchRunnable implements Runnable {
        private BitmapFetchRunnable() {
        }

        public void run() {
            updateScreenBitmap();
            ScreencastDispatcher.this.mBackgroundHandler.post(ScreencastDispatcher.this.mEventDispatchRunnable.withEndAction(this));
        }

        private void updateScreenBitmap() {
            Activity tryGetTopActivity;
            if (ScreencastDispatcher.this.mIsRunning && (tryGetTopActivity = ScreencastDispatcher.this.mActivityTracker.tryGetTopActivity()) != null) {
                View decorView = tryGetTopActivity.getWindow().getDecorView();
                try {
                    if (ScreencastDispatcher.this.mBitmap == null) {
                        int width = decorView.getWidth();
                        int height = decorView.getHeight();
                        if (width <= 0) {
                            return;
                        }
                        if (height > 0) {
                            float f = (float) width;
                            float f2 = (float) height;
                            float min = Math.min(((float) ScreencastDispatcher.this.mRequest.maxWidth) / f, ((float) ScreencastDispatcher.this.mRequest.maxHeight) / f2);
                            float unused = ScreencastDispatcher.sBitmapScale = min;
                            int i = (int) (f * min);
                            int i2 = (int) (min * f2);
                            Bitmap unused2 = ScreencastDispatcher.this.mBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.RGB_565);
                            Canvas unused3 = ScreencastDispatcher.this.mCanvas = new Canvas(ScreencastDispatcher.this.mBitmap);
                            Matrix matrix = new Matrix();
                            ScreencastDispatcher.this.mTempSrc.set(0.0f, 0.0f, f, f2);
                            ScreencastDispatcher.this.mTempDst.set(0.0f, 0.0f, (float) i, (float) i2);
                            matrix.setRectToRect(ScreencastDispatcher.this.mTempSrc, ScreencastDispatcher.this.mTempDst, Matrix.ScaleToFit.CENTER);
                            ScreencastDispatcher.this.mCanvas.setMatrix(matrix);
                        } else {
                            return;
                        }
                    }
                    decorView.draw(ScreencastDispatcher.this.mCanvas);
                } catch (OutOfMemoryError unused4) {
                    LogUtil.w("Out of memory trying to allocate screencast Bitmap.");
                }
            }
        }
    }

    private class EventDispatchRunnable implements Runnable {
        private Runnable mEndAction;

        private EventDispatchRunnable() {
        }

        /* access modifiers changed from: private */
        public EventDispatchRunnable withEndAction(Runnable runnable) {
            this.mEndAction = runnable;
            return this;
        }

        public void run() {
            if (ScreencastDispatcher.this.mIsRunning && ScreencastDispatcher.this.mBitmap != null) {
                int width = ScreencastDispatcher.this.mBitmap.getWidth();
                int height = ScreencastDispatcher.this.mBitmap.getHeight();
                ScreencastDispatcher.this.mStream.reset();
                Base64OutputStream base64OutputStream = new Base64OutputStream(ScreencastDispatcher.this.mStream, 0);
                ScreencastDispatcher.this.mBitmap.compress(Bitmap.CompressFormat.valueOf(ScreencastDispatcher.this.mRequest.format.toUpperCase()), ScreencastDispatcher.this.mRequest.quality, base64OutputStream);
                ScreencastDispatcher.this.mEvent.data = ScreencastDispatcher.this.mStream.toString();
                ScreencastDispatcher.this.mMetadata.pageScaleFactor = ScreencastDispatcher.sBitmapScale;
                ScreencastDispatcher.this.mMetadata.deviceWidth = width;
                ScreencastDispatcher.this.mMetadata.deviceHeight = height;
                ScreencastDispatcher.this.mEvent.metadata = ScreencastDispatcher.this.mMetadata;
                if (ScreencastDispatcher.this.mPeer.getWebSocket() != null && ScreencastDispatcher.this.mPeer.getWebSocket().isOpen()) {
                    ScreencastDispatcher.this.mPeer.invokeMethod("Page.screencastFrame", ScreencastDispatcher.this.mEvent, (PendingRequestCallback) null);
                    ScreencastDispatcher.this.mMainHandler.postDelayed(this.mEndAction, ScreencastDispatcher.FRAME_DELAY);
                }
            }
        }
    }

    private class CancellationRunnable implements Runnable {
        private CancellationRunnable() {
        }

        public void run() {
            ScreencastDispatcher.this.mHandlerThread.interrupt();
            ScreencastDispatcher.this.mMainHandler.removeCallbacks(ScreencastDispatcher.this.mBitmapFetchRunnable);
            ScreencastDispatcher.this.mBackgroundHandler.removeCallbacks(ScreencastDispatcher.this.mEventDispatchRunnable);
            boolean unused = ScreencastDispatcher.this.mIsRunning = false;
            HandlerThread unused2 = ScreencastDispatcher.this.mHandlerThread = null;
            Bitmap unused3 = ScreencastDispatcher.this.mBitmap = null;
            Canvas unused4 = ScreencastDispatcher.this.mCanvas = null;
            ByteArrayOutputStream unused5 = ScreencastDispatcher.this.mStream = null;
        }
    }
}
