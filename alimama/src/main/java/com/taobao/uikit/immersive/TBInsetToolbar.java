package com.taobao.uikit.immersive;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import com.taobao.tao.util.SystemBarDecorator;

public class TBInsetToolbar extends Toolbar {
    private boolean mRequirePaddingTop;

    public TBInsetToolbar(Context context) {
        this(context, (AttributeSet) null);
    }

    public TBInsetToolbar(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, -1);
    }

    /* JADX INFO: finally extract failed */
    public TBInsetToolbar(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mRequirePaddingTop = true;
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.TBInsetToolbar, 0, 0);
        try {
            this.mRequirePaddingTop = obtainStyledAttributes.getBoolean(R.styleable.TBInsetToolbar_requirePaddingTop, true);
            obtainStyledAttributes.recycle();
            if (this.mRequirePaddingTop) {
                setPaddingTop();
            }
            int minimumHeight = ViewCompat.getMinimumHeight(this);
            if (minimumHeight > 0 && Build.VERSION.SDK_INT >= 19) {
                setMinimumHeight(minimumHeight + SystemBarDecorator.getStatusBarHeight(getContext()));
            }
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }

    /* access modifiers changed from: protected */
    public void setPaddingTop() {
        if (Build.VERSION.SDK_INT >= 19) {
            setPadding(getPaddingLeft(), getPaddingTop() + SystemBarDecorator.getStatusBarHeight(getContext()), getPaddingRight(), getPaddingBottom());
        }
    }

    /* access modifiers changed from: protected */
    public final void onMeasure(int i, int i2) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams != null) {
            int i3 = layoutParams.height > 0 ? layoutParams.height : 0;
            if (Build.VERSION.SDK_INT >= 19) {
                if (i3 > 0) {
                    i3 += SystemBarDecorator.getStatusBarHeight(getContext());
                }
                setFitsSystemWindows(true);
            }
            if (i3 > 0) {
                super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(i3, 1073741824));
            } else {
                super.onMeasure(i, i2);
            }
        } else {
            super.onMeasure(i, i2);
        }
    }

    public void setRequirePaddingTop(boolean z) {
        if (this.mRequirePaddingTop != z) {
            if (!this.mRequirePaddingTop) {
                setPaddingTop();
            } else if (Build.VERSION.SDK_INT >= 19) {
                setPadding(getPaddingLeft(), getPaddingTop() - SystemBarDecorator.getStatusBarHeight(getContext()), getPaddingRight(), getPaddingBottom());
            }
        }
        this.mRequirePaddingTop = z;
    }
}
