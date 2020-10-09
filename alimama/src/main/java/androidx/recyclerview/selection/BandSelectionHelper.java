package androidx.recyclerview.selection;

import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.core.util.Preconditions;
import androidx.recyclerview.selection.GridModel;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Set;

class BandSelectionHelper<K> implements RecyclerView.OnItemTouchListener {
    static final boolean DEBUG = false;
    static final String TAG = "BandSelectionHelper";
    private final BandPredicate mBandPredicate;
    @Nullable
    private Point mCurrentPosition;
    private final FocusDelegate<K> mFocusDelegate;
    private final GridModel.SelectionObserver mGridObserver;
    private final BandHost mHost;
    private final ItemKeyProvider<K> mKeyProvider;
    private final OperationMonitor mLock;
    @Nullable
    private GridModel mModel;
    @Nullable
    private Point mOrigin;
    private final AutoScroller mScroller;
    final SelectionTracker<K> mSelectionTracker;

    public void onRequestDisallowInterceptTouchEvent(boolean z) {
    }

    BandSelectionHelper(@NonNull BandHost bandHost, @NonNull AutoScroller autoScroller, @NonNull ItemKeyProvider<K> itemKeyProvider, @NonNull SelectionTracker<K> selectionTracker, @NonNull BandPredicate bandPredicate, @NonNull FocusDelegate<K> focusDelegate, @NonNull OperationMonitor operationMonitor) {
        boolean z = false;
        Preconditions.checkArgument(bandHost != null);
        Preconditions.checkArgument(autoScroller != null);
        Preconditions.checkArgument(itemKeyProvider != null);
        Preconditions.checkArgument(selectionTracker != null);
        Preconditions.checkArgument(bandPredicate != null);
        Preconditions.checkArgument(focusDelegate != null);
        Preconditions.checkArgument(operationMonitor != null ? true : z);
        this.mHost = bandHost;
        this.mKeyProvider = itemKeyProvider;
        this.mSelectionTracker = selectionTracker;
        this.mBandPredicate = bandPredicate;
        this.mFocusDelegate = focusDelegate;
        this.mLock = operationMonitor;
        this.mHost.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                BandSelectionHelper.this.onScrolled(recyclerView, i, i2);
            }
        });
        this.mScroller = autoScroller;
        this.mGridObserver = new GridModel.SelectionObserver<K>() {
            public void onSelectionChanged(Set<K> set) {
                BandSelectionHelper.this.mSelectionTracker.setProvisionalSelection(set);
            }
        };
    }

    static <K> BandSelectionHelper create(@NonNull RecyclerView recyclerView, @NonNull AutoScroller autoScroller, @DrawableRes int i, @NonNull ItemKeyProvider<K> itemKeyProvider, @NonNull SelectionTracker<K> selectionTracker, @NonNull SelectionTracker.SelectionPredicate<K> selectionPredicate, @NonNull BandPredicate bandPredicate, @NonNull FocusDelegate<K> focusDelegate, @NonNull OperationMonitor operationMonitor) {
        RecyclerView recyclerView2 = recyclerView;
        int i2 = i;
        SelectionTracker.SelectionPredicate<K> selectionPredicate2 = selectionPredicate;
        return new BandSelectionHelper(new DefaultBandHost(recyclerView, i, itemKeyProvider, selectionPredicate), autoScroller, itemKeyProvider, selectionTracker, bandPredicate, focusDelegate, operationMonitor);
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public boolean isActive() {
        return this.mModel != null;
    }

    /* access modifiers changed from: package-private */
    public void reset() {
        if (isActive()) {
            this.mHost.hideBand();
            if (this.mModel != null) {
                this.mModel.stopCapturing();
                this.mModel.onDestroy();
            }
            this.mModel = null;
            this.mOrigin = null;
            this.mScroller.reset();
            this.mLock.stop();
        }
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public boolean shouldStart(@NonNull MotionEvent motionEvent) {
        return MotionEvents.isPrimaryMouseButtonPressed(motionEvent) && MotionEvents.isActionMove(motionEvent) && this.mBandPredicate.canInitiate(motionEvent) && !isActive();
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public boolean shouldStop(@NonNull MotionEvent motionEvent) {
        return isActive() && (MotionEvents.isActionUp(motionEvent) || MotionEvents.isActionPointerUp(motionEvent) || MotionEvents.isActionCancel(motionEvent));
    }

    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        if (shouldStart(motionEvent)) {
            startBandSelect(motionEvent);
        } else if (shouldStop(motionEvent)) {
            endBandSelect();
        }
        return isActive();
    }

    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        if (shouldStop(motionEvent)) {
            endBandSelect();
        } else if (isActive()) {
            this.mCurrentPosition = MotionEvents.getOrigin(motionEvent);
            this.mModel.resizeSelection(this.mCurrentPosition);
            resizeBand();
            this.mScroller.scroll(this.mCurrentPosition);
        }
    }

    private void startBandSelect(@NonNull MotionEvent motionEvent) {
        Preconditions.checkState(!isActive());
        if (!MotionEvents.isCtrlKeyPressed(motionEvent)) {
            this.mSelectionTracker.clearSelection();
        }
        Point origin = MotionEvents.getOrigin(motionEvent);
        this.mModel = this.mHost.createGridModel();
        this.mModel.addOnSelectionChangedListener(this.mGridObserver);
        this.mLock.start();
        this.mFocusDelegate.clearFocus();
        this.mOrigin = origin;
        this.mModel.startCapturing(this.mOrigin);
    }

    private void resizeBand() {
        this.mHost.showBand(new Rect(Math.min(this.mOrigin.x, this.mCurrentPosition.x), Math.min(this.mOrigin.y, this.mCurrentPosition.y), Math.max(this.mOrigin.x, this.mCurrentPosition.x), Math.max(this.mOrigin.y, this.mCurrentPosition.y)));
    }

    private void endBandSelect() {
        int positionNearestOrigin = this.mModel.getPositionNearestOrigin();
        if (positionNearestOrigin != -1 && this.mSelectionTracker.isSelected(this.mKeyProvider.getKey(positionNearestOrigin))) {
            this.mSelectionTracker.anchorRange(positionNearestOrigin);
        }
        this.mSelectionTracker.mergeProvisionalSelection();
        reset();
    }

    /* access modifiers changed from: package-private */
    public void onScrolled(@NonNull RecyclerView recyclerView, int i, int i2) {
        if (isActive()) {
            this.mOrigin.y -= i2;
            resizeBand();
        }
    }

    static abstract class BandHost<K> {
        /* access modifiers changed from: package-private */
        public abstract void addOnScrollListener(@NonNull RecyclerView.OnScrollListener onScrollListener);

        /* access modifiers changed from: package-private */
        public abstract GridModel<K> createGridModel();

        /* access modifiers changed from: package-private */
        public abstract void hideBand();

        /* access modifiers changed from: package-private */
        public abstract void showBand(@NonNull Rect rect);

        BandHost() {
        }
    }
}
