package com.taobao.weex.ui.component.list;

import android.view.View;
import android.view.ViewGroup;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.common.Constants;
import com.taobao.weex.common.WXThread;
import com.taobao.weex.utils.WXLogUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class StickyHeaderHelper {
    private String mCurrentStickyRef = null;
    private Map<String, WXCell> mHeaderComps = new HashMap();
    private Map<String, View> mHeaderViews = new HashMap();
    /* access modifiers changed from: private */
    public final ViewGroup mParent;

    public StickyHeaderHelper(ViewGroup viewGroup) {
        this.mParent = viewGroup;
    }

    public void notifyStickyShow(WXCell wXCell) {
        if (wXCell != null) {
            this.mHeaderComps.put(wXCell.getRef(), wXCell);
            if (this.mCurrentStickyRef != null) {
                WXCell wXCell2 = this.mHeaderComps.get(this.mCurrentStickyRef);
                if (wXCell2 == null || wXCell.getScrollPositon() > wXCell2.getScrollPositon()) {
                    this.mCurrentStickyRef = wXCell.getRef();
                }
            } else {
                this.mCurrentStickyRef = wXCell.getRef();
            }
            if (this.mCurrentStickyRef == null) {
                WXLogUtils.e("Current Sticky ref is null.");
                return;
            }
            WXCell wXCell3 = this.mHeaderComps.get(this.mCurrentStickyRef);
            ViewGroup realView = wXCell3.getRealView();
            if (realView == null) {
                WXLogUtils.e("Sticky header's real view is null.");
                return;
            }
            View view = this.mHeaderViews.get(wXCell3.getRef());
            if (view != null) {
                view.bringToFront();
            } else {
                this.mHeaderViews.put(wXCell3.getRef(), realView);
                float translationX = realView.getTranslationX();
                float translationY = realView.getTranslationY();
                wXCell3.removeSticky();
                ViewGroup viewGroup = (ViewGroup) realView.getParent();
                if (viewGroup != null) {
                    viewGroup.removeView(realView);
                }
                realView.setTag(wXCell3.getRef());
                this.mParent.addView(realView, new ViewGroup.MarginLayoutParams(-2, -2));
                realView.setTag(this);
                if (wXCell3.getStickyOffset() > 0) {
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) realView.getLayoutParams();
                    if (wXCell3.getStickyOffset() != marginLayoutParams.topMargin) {
                        marginLayoutParams.topMargin = wXCell3.getStickyOffset();
                    }
                }
                realView.setTranslationX(translationX);
                realView.setTranslationY(translationY);
            }
            changeFrontStickyVisible();
            if (wXCell3.getEvents().contains("sticky")) {
                wXCell3.fireEvent("sticky");
            }
        }
    }

    public void notifyStickyRemove(WXCell wXCell) {
        if (wXCell != null) {
            final WXCell remove = this.mHeaderComps.containsValue(wXCell) ? this.mHeaderComps.remove(wXCell.getRef()) : wXCell;
            final View remove2 = this.mHeaderViews.remove(wXCell.getRef());
            if (remove == null || remove2 == null) {
                WXEnvironment.isApkDebugable();
                return;
            }
            if (this.mCurrentStickyRef != null && this.mCurrentStickyRef.equals(wXCell.getRef())) {
                this.mCurrentStickyRef = null;
            }
            this.mParent.post(WXThread.secure((Runnable) new Runnable() {
                public void run() {
                    StickyHeaderHelper.this.mParent.removeView(remove2);
                    if (remove2.getVisibility() != 0) {
                        remove2.setVisibility(0);
                    }
                    remove.recoverySticky();
                    StickyHeaderHelper.this.changeFrontStickyVisible();
                }
            }));
            if (remove.getEvents().contains(Constants.Event.UNSTICKY)) {
                remove.fireEvent(Constants.Event.UNSTICKY);
            }
        }
    }

    public void updateStickyView(int i) {
        ArrayList<WXCell> arrayList = new ArrayList<>();
        for (Map.Entry<String, WXCell> value : this.mHeaderComps.entrySet()) {
            WXCell wXCell = (WXCell) value.getValue();
            int scrollPositon = wXCell.getScrollPositon();
            if (scrollPositon > i) {
                arrayList.add(wXCell);
            } else if (scrollPositon == i) {
                this.mCurrentStickyRef = wXCell.getRef();
                View view = this.mHeaderViews.get(wXCell.getRef());
                if (view != null) {
                    view.bringToFront();
                    changeFrontStickyVisible();
                }
            }
        }
        for (WXCell notifyStickyRemove : arrayList) {
            notifyStickyRemove(notifyStickyRemove);
        }
    }

    public void clearStickyHeaders() {
        if (this.mHeaderViews.size() > 0) {
            Iterator<Map.Entry<String, WXCell>> it = this.mHeaderComps.entrySet().iterator();
            while (it.hasNext()) {
                it.remove();
                notifyStickyRemove((WXCell) it.next().getValue());
            }
        }
    }

    /* access modifiers changed from: private */
    public void changeFrontStickyVisible() {
        if (this.mHeaderViews.size() > 0) {
            boolean z = false;
            for (int childCount = this.mParent.getChildCount() - 1; childCount >= 0; childCount--) {
                View childAt = this.mParent.getChildAt(childCount);
                if (!z || !(childAt.getTag() instanceof StickyHeaderHelper)) {
                    if (childAt.getTag() instanceof StickyHeaderHelper) {
                        if (!(childAt == null || childAt.getVisibility() == 0)) {
                            childAt.setVisibility(0);
                        }
                        z = true;
                    }
                } else if (childAt.getVisibility() != 8) {
                    childAt.setVisibility(8);
                }
            }
        }
    }
}
