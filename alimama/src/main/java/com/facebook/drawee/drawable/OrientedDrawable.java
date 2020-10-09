package com.facebook.drawee.drawable;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.internal.VisibleForTesting;
import com.facebook.imagepipeline.common.RotationOptions;

public class OrientedDrawable extends ForwardingDrawable {
    private int mExifOrientation;
    private int mRotationAngle;
    @VisibleForTesting
    final Matrix mRotationMatrix;
    private final Matrix mTempMatrix;
    private final RectF mTempRectF;

    public OrientedDrawable(Drawable drawable, int i) {
        this(drawable, i, 0);
    }

    public OrientedDrawable(Drawable drawable, int i, int i2) {
        super(drawable);
        this.mTempMatrix = new Matrix();
        this.mTempRectF = new RectF();
        boolean z = false;
        Preconditions.checkArgument(i % 90 == 0);
        if (i2 >= 0 && i2 <= 8) {
            z = true;
        }
        Preconditions.checkArgument(z);
        this.mRotationMatrix = new Matrix();
        this.mRotationAngle = i;
        this.mExifOrientation = i2;
    }

    public void draw(Canvas canvas) {
        if (this.mRotationAngle > 0 || !(this.mExifOrientation == 0 || this.mExifOrientation == 1)) {
            int save = canvas.save();
            canvas.concat(this.mRotationMatrix);
            super.draw(canvas);
            canvas.restoreToCount(save);
            return;
        }
        super.draw(canvas);
    }

    public int getIntrinsicWidth() {
        if (this.mExifOrientation == 5 || this.mExifOrientation == 7 || this.mRotationAngle % RotationOptions.ROTATE_180 != 0) {
            return super.getIntrinsicHeight();
        }
        return super.getIntrinsicWidth();
    }

    public int getIntrinsicHeight() {
        if (this.mExifOrientation == 5 || this.mExifOrientation == 7 || this.mRotationAngle % RotationOptions.ROTATE_180 != 0) {
            return super.getIntrinsicWidth();
        }
        return super.getIntrinsicHeight();
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(Rect rect) {
        Drawable current = getCurrent();
        if (this.mRotationAngle > 0 || !(this.mExifOrientation == 0 || this.mExifOrientation == 1)) {
            int i = this.mExifOrientation;
            if (i == 2) {
                this.mRotationMatrix.setScale(-1.0f, 1.0f);
            } else if (i != 7) {
                switch (i) {
                    case 4:
                        this.mRotationMatrix.setScale(1.0f, -1.0f);
                        break;
                    case 5:
                        this.mRotationMatrix.setRotate(270.0f, (float) rect.centerX(), (float) rect.centerY());
                        this.mRotationMatrix.postScale(1.0f, -1.0f);
                        break;
                    default:
                        this.mRotationMatrix.setRotate((float) this.mRotationAngle, (float) rect.centerX(), (float) rect.centerY());
                        break;
                }
            } else {
                this.mRotationMatrix.setRotate(270.0f, (float) rect.centerX(), (float) rect.centerY());
                this.mRotationMatrix.postScale(-1.0f, 1.0f);
            }
            this.mTempMatrix.reset();
            this.mRotationMatrix.invert(this.mTempMatrix);
            this.mTempRectF.set(rect);
            this.mTempMatrix.mapRect(this.mTempRectF);
            current.setBounds((int) this.mTempRectF.left, (int) this.mTempRectF.top, (int) this.mTempRectF.right, (int) this.mTempRectF.bottom);
            return;
        }
        current.setBounds(rect);
    }

    public void getTransform(Matrix matrix) {
        getParentTransform(matrix);
        if (!this.mRotationMatrix.isIdentity()) {
            matrix.preConcat(this.mRotationMatrix);
        }
    }
}
