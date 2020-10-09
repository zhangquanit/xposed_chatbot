package com.aurelhubert.ahbottomnavigation;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorUpdateListener;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

public class AHBottomNavigationBehavior<V extends View> extends VerticalScrollingBehavior<V> {
    private static final int ANIM_DURATION = 300;
    private static final Interpolator INTERPOLATOR = new LinearOutSlowInInterpolator();
    private boolean behaviorTranslationEnabled = true;
    private boolean fabBottomMarginInitialized = false;
    private float fabDefaultBottomMargin = 0.0f;
    private float fabTargetOffset = 0.0f;
    private FloatingActionButton floatingActionButton;
    private boolean hidden = false;
    private int mSnackbarHeight = -1;
    private TabLayout mTabLayout;
    private int mTabLayoutId;
    private int navigationBarHeight = 0;
    /* access modifiers changed from: private */
    public AHBottomNavigation.OnNavigationPositionListener navigationPositionListener;
    /* access modifiers changed from: private */
    public float snackBarY = 0.0f;
    /* access modifiers changed from: private */
    public Snackbar.SnackbarLayout snackbarLayout;
    /* access modifiers changed from: private */
    public float targetOffset = 0.0f;
    private ViewPropertyAnimatorCompat translationAnimator;
    private ObjectAnimator translationObjectAnimator;

    public void onDirectionNestedPreScroll(CoordinatorLayout coordinatorLayout, V v, View view, int i, int i2, int[] iArr, int i3) {
    }

    /* access modifiers changed from: protected */
    public boolean onNestedDirectionFling(CoordinatorLayout coordinatorLayout, V v, View view, float f, float f2, int i) {
        return false;
    }

    public void onNestedVerticalOverScroll(CoordinatorLayout coordinatorLayout, V v, int i, int i2, int i3) {
    }

    public AHBottomNavigationBehavior() {
    }

    public AHBottomNavigationBehavior(boolean z, int i) {
        this.behaviorTranslationEnabled = z;
        this.navigationBarHeight = i;
    }

    public AHBottomNavigationBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.AHBottomNavigationBehavior_Params);
        this.mTabLayoutId = obtainStyledAttributes.getResourceId(R.styleable.AHBottomNavigationBehavior_Params_tabLayoutId, -1);
        obtainStyledAttributes.recycle();
    }

    public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, V v, int i) {
        boolean onLayoutChild = super.onLayoutChild(coordinatorLayout, v, i);
        if (this.mTabLayout == null && this.mTabLayoutId != -1) {
            this.mTabLayout = findTabLayout(v);
        }
        return onLayoutChild;
    }

    private TabLayout findTabLayout(View view) {
        if (this.mTabLayoutId == 0) {
            return null;
        }
        return (TabLayout) view.findViewById(this.mTabLayoutId);
    }

    public boolean onDependentViewChanged(CoordinatorLayout coordinatorLayout, V v, View view) {
        return super.onDependentViewChanged(coordinatorLayout, v, view);
    }

    public void onDependentViewRemoved(CoordinatorLayout coordinatorLayout, V v, View view) {
        super.onDependentViewRemoved(coordinatorLayout, v, view);
    }

    public boolean layoutDependsOn(CoordinatorLayout coordinatorLayout, V v, View view) {
        if (view == null || !(view instanceof Snackbar.SnackbarLayout)) {
            return super.layoutDependsOn(coordinatorLayout, v, view);
        }
        updateSnackbar(v, view);
        return true;
    }

    public void onNestedScroll(CoordinatorLayout coordinatorLayout, V v, View view, int i, int i2, int i3, int i4) {
        super.onNestedScroll(coordinatorLayout, v, view, i, i2, i3, i4);
        if (i2 < 0) {
            handleDirection(v, -1);
        } else if (i2 > 0) {
            handleDirection(v, 1);
        }
    }

    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, V v, View view, View view2, int i) {
        return i == 2 || super.onStartNestedScroll(coordinatorLayout, v, view, view2, i);
    }

    private void handleDirection(V v, int i) {
        if (this.behaviorTranslationEnabled) {
            if (i == -1 && this.hidden) {
                this.hidden = false;
                animateOffset(v, 0, false, true);
            } else if (i == 1 && !this.hidden) {
                this.hidden = true;
                animateOffset(v, v.getHeight(), false, true);
            }
        }
    }

    private void animateOffset(V v, int i, boolean z, boolean z2) {
        if (!this.behaviorTranslationEnabled && !z) {
            return;
        }
        if (Build.VERSION.SDK_INT < 19) {
            ensureOrCancelObjectAnimation(v, i, z2);
            this.translationObjectAnimator.start();
            return;
        }
        ensureOrCancelAnimator(v, z2);
        this.translationAnimator.translationY((float) i).start();
    }

    private void ensureOrCancelAnimator(V v, boolean z) {
        long j = 0;
        if (this.translationAnimator == null) {
            this.translationAnimator = ViewCompat.animate(v);
            ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = this.translationAnimator;
            if (z) {
                j = 300;
            }
            viewPropertyAnimatorCompat.setDuration(j);
            this.translationAnimator.setUpdateListener(new ViewPropertyAnimatorUpdateListener() {
                public void onAnimationUpdate(View view) {
                    if (AHBottomNavigationBehavior.this.navigationPositionListener != null) {
                        AHBottomNavigationBehavior.this.navigationPositionListener.onPositionChange((int) ((((float) view.getMeasuredHeight()) - view.getTranslationY()) + AHBottomNavigationBehavior.this.snackBarY));
                    }
                }
            });
            this.translationAnimator.setInterpolator(INTERPOLATOR);
            return;
        }
        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat2 = this.translationAnimator;
        if (z) {
            j = 300;
        }
        viewPropertyAnimatorCompat2.setDuration(j);
        this.translationAnimator.cancel();
    }

    private void ensureOrCancelObjectAnimation(final V v, int i, boolean z) {
        if (this.translationObjectAnimator != null) {
            this.translationObjectAnimator.cancel();
        }
        this.translationObjectAnimator = ObjectAnimator.ofFloat(v, View.TRANSLATION_Y, new float[]{(float) i});
        this.translationObjectAnimator.setDuration(z ? 300 : 0);
        this.translationObjectAnimator.setInterpolator(INTERPOLATOR);
        this.translationObjectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (AHBottomNavigationBehavior.this.snackbarLayout != null && (AHBottomNavigationBehavior.this.snackbarLayout.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) {
                    float unused = AHBottomNavigationBehavior.this.targetOffset = ((float) v.getMeasuredHeight()) - v.getTranslationY();
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) AHBottomNavigationBehavior.this.snackbarLayout.getLayoutParams();
                    marginLayoutParams.setMargins(marginLayoutParams.leftMargin, marginLayoutParams.topMargin, marginLayoutParams.rightMargin, (int) AHBottomNavigationBehavior.this.targetOffset);
                    AHBottomNavigationBehavior.this.snackbarLayout.requestLayout();
                }
                if (AHBottomNavigationBehavior.this.navigationPositionListener != null) {
                    AHBottomNavigationBehavior.this.navigationPositionListener.onPositionChange((int) ((((float) v.getMeasuredHeight()) - v.getTranslationY()) + AHBottomNavigationBehavior.this.snackBarY));
                }
            }
        });
    }

    public static <V extends View> AHBottomNavigationBehavior<V> from(V v) {
        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        if (layoutParams instanceof CoordinatorLayout.LayoutParams) {
            CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) layoutParams).getBehavior();
            if (behavior instanceof AHBottomNavigationBehavior) {
                return (AHBottomNavigationBehavior) behavior;
            }
            throw new IllegalArgumentException("The view is not associated with AHBottomNavigationBehavior");
        }
        throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
    }

    public void setTabLayoutId(int i) {
        this.mTabLayoutId = i;
    }

    public void setBehaviorTranslationEnabled(boolean z, int i) {
        this.behaviorTranslationEnabled = z;
        this.navigationBarHeight = i;
    }

    public void setOnNavigationPositionListener(AHBottomNavigation.OnNavigationPositionListener onNavigationPositionListener) {
        this.navigationPositionListener = onNavigationPositionListener;
    }

    public void removeOnNavigationPositionListener() {
        this.navigationPositionListener = null;
    }

    public void hideView(V v, int i, boolean z) {
        if (!this.hidden) {
            this.hidden = true;
            animateOffset(v, i, true, z);
        }
    }

    public void resetOffset(V v, boolean z) {
        if (this.hidden) {
            this.hidden = false;
            animateOffset(v, 0, true, z);
        }
    }

    public void updateSnackbar(View view, View view2) {
        if (view2 != null && (view2 instanceof Snackbar.SnackbarLayout)) {
            this.snackbarLayout = (Snackbar.SnackbarLayout) view2;
            if (this.mSnackbarHeight == -1) {
                this.mSnackbarHeight = view2.getHeight();
            }
            int measuredHeight = (int) (((float) view.getMeasuredHeight()) - view.getTranslationY());
            if (Build.VERSION.SDK_INT < 21) {
                view.bringToFront();
            }
            if (view2.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view2.getLayoutParams();
                marginLayoutParams.setMargins(marginLayoutParams.leftMargin, marginLayoutParams.topMargin, marginLayoutParams.rightMargin, measuredHeight);
                view2.requestLayout();
            }
        }
    }
}
