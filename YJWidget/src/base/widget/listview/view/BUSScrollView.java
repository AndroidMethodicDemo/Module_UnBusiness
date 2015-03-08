package base.widget.listview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

public class BUSScrollView extends ViewGroup {

	private static final String TAG = null;
	private Scroller mScroller;
	private int mTouchSlop;
	private int mMinimumVelocity;
	private int mMaximumVelocity;
	private int maxScrollEdge;
	private VelocityTracker mVelocityTracker;
	private float mLastMotionY;
	private int childTotalHeight;
	private int height;
	private boolean mIsInEdge;

	public BUSScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub

	}

	void init(Context context) {
		mScroller = new Scroller(getContext());
		setFocusable(true);
		setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
		setWillNotDraw(false);
		final ViewConfiguration configuration = ViewConfiguration.get(context);
		mTouchSlop = configuration.getScaledTouchSlop();
		mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
		mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();

	}

	public void fling(int velocityY) {
		if (getChildCount() > 0) {
			mScroller.fling(getScrollX(), getScrollY(), 0, velocityY, 0, 0, 0,
					maxScrollEdge);
			final boolean movingDown = velocityY > 0;
			awakenScrollBars(mScroller.getDuration());
			invalidate();
		}
	}

	private void obtainVelocityTracker(MotionEvent event) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
	}

	private void releaseVelocityTracker() {
		if (mVelocityTracker != null) {
			mVelocityTracker.recycle();
			mVelocityTracker = null;
		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN
                        && event.getEdgeFlags() != 0) {
                return false;
        }

        obtainVelocityTracker(event);

        final int action = event.getAction();
        final float x = event.getX();
        final float y = event.getY();

        switch (action) {
        case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "ACTION_DOWN#currentScrollY:" + getScrollY()
                                + ", mLastMotionY:" + mLastMotionY
                                );
                if (!mScroller.isFinished()) {
                        mScroller.abortAnimation();
                }
                mLastMotionY = y;
                break;

        case MotionEvent.ACTION_MOVE:
                final int deltaY = (int) (mLastMotionY - y);
                mLastMotionY = y;
                if (deltaY < 0) {
                        if (getScrollY() > 0) {
                                scrollBy(0, deltaY);
                        } 
                } else if (deltaY > 0) {
                        mIsInEdge = getScrollY() <= childTotalHeight - height;
                        if (mIsInEdge) {
                                scrollBy(0, deltaY);
                        }
                }
                break;

        case MotionEvent.ACTION_UP:
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int initialVelocity = (int) velocityTracker.getYVelocity();

                if ((Math.abs(initialVelocity) > mMinimumVelocity)
                                && getChildCount() > 0) {
                        fling(-initialVelocity);
                }

                releaseVelocityTracker();
                break;
        }

        return true;
}

	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			int scrollX = getScrollX();
			int scrollY = getScrollY();
			int oldX = scrollX;
			int oldY = scrollY;
			int x = mScroller.getCurrX();
			int y = mScroller.getCurrY();
			scrollX = x;
			scrollY = y;
			scrollY = scrollY + 10;
			scrollTo(scrollX, scrollY);
			postInvalidate();
		}
	}

}
