package base.widget.listview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

/**
 * A ListView that maintains a header pinned at the top of the list. The
 * pinned header can be pushed up and dissolved as needed.
 */
public class XSWPinnedHeaderExpListView extends ExpandableListView{

    private View mHeaderView, mHeaderTouchView;
    
    IPinnedHeaderExpLvCallback mCallback;
    private boolean mHeaderViewVisible;

    private int mHeaderViewWidth;
    private int mHeaderViewHeight;
    
    private ExpandableListAdapter mAdapter;

    public XSWPinnedHeaderExpListView(Context context) {
        super(context);
    }

    public XSWPinnedHeaderExpListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XSWPinnedHeaderExpListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    public int getPinnedHeaderHeight(){
        return mHeaderViewHeight;
    }

    public void setPinnedHeaderView(View headerView) {
        mHeaderView = headerView;

        // Disable vertical fading when the pinned header is present
        // TODO change ListView to allow separate measures for top and bottom fading edge;
        // in this particular case we would like to disable the top, but not the bottom edge.
        if (mHeaderView != null) {
            setFadingEdgeLength(0);
        }
        requestLayout();
    }
    
    
    public void setPinnedHeaderTouchView(View view){
    	mHeaderTouchView = view;
    }
    
    public void setIPinnedHeaderExpListView(IPinnedHeaderExpLvCallback callback){
    	this.mCallback = callback;
    }

    @Override
    public void setAdapter(ExpandableListAdapter adapter) {
        super.setAdapter(adapter);
        this.mAdapter = adapter;
    }
    
    public void resetAdapter(ExpandableListAdapter adapter){
    	this.mAdapter = adapter;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mHeaderView != null) {
            measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
            mHeaderViewWidth = mHeaderView.getMeasuredWidth();
            mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mHeaderView != null) {
            mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
            configureHeaderView(getFirstVisiblePosition());
        }
    }

    /** 
     * animating header pushing
     * @param position
     */
    public void configureHeaderView(int position) {
        final int group = getPackedPositionGroup(getExpandableListPosition(position));

        if (mHeaderView == null) {
            return;
        }

//        mHeaderView.setOnClickListener(new OnClickListener() {
//
//            public void onClick(View header) {
//                if(!expandGroup(group)) collapseGroup(group); 
//            }
//        }); 
        //由于mHeaderView无法设置点击事件
        mHeaderTouchView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!expandGroup(group)) collapseGroup(group); 
			}
		});

        int state,nextSectionPosition = getFlatListPosition(getPackedPositionForGroup(group+1));

        int headerCount = getHeaderViewsCount();
        if (mAdapter.getGroupCount()== 0) {
            state = IPinnedHeaderExpLvCallback.PINNED_HEADER_GONE;
        }else if (position < 0 + headerCount) {
            state = IPinnedHeaderExpLvCallback.PINNED_HEADER_GONE;
        }else if (nextSectionPosition != -1 && position == nextSectionPosition - 1) {
            state=IPinnedHeaderExpLvCallback.PINNED_HEADER_PUSHED_UP;
        }else  state=IPinnedHeaderExpLvCallback.PINNED_HEADER_VISIBLE;

        int y = 0;
        switch (state) {    
            case IPinnedHeaderExpLvCallback.PINNED_HEADER_GONE: {
                mHeaderViewVisible = false;
                break;
            }

            case IPinnedHeaderExpLvCallback.PINNED_HEADER_VISIBLE: {
            	if(null != mCallback){
            		mCallback.configurePinnedHeader(mHeaderView, group);
            	}
                if (mHeaderView.getTop() != 0) {
                    mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
                }
                mHeaderViewVisible = true;
                break;
            }

            case IPinnedHeaderExpLvCallback.PINNED_HEADER_PUSHED_UP: {
                View firstView = getChildAt(0);
                if(firstView==null){
                    if (mHeaderView.getTop() != 0) {
                        mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
                    }
                    mHeaderViewVisible = true;
                    break;
                }
                int bottom = firstView.getBottom();
                int headerHeight = mHeaderView.getHeight();
                if (bottom < headerHeight) {
                    y = (bottom - headerHeight);
                } else {
                    y = 0;
                }
                
                if(null != mCallback){
                	mCallback.configurePinnedHeader(mHeaderView, group);
                }

                if (mHeaderView.getTop() != y) {
                    mHeaderView.layout(0, y, mHeaderViewWidth, mHeaderViewHeight + y);
                }
                mHeaderViewVisible = true;
                break;
            }
        }
        
        MarginLayoutParams layoutParams = (MarginLayoutParams) mHeaderTouchView.getLayoutParams();
        layoutParams.topMargin = y;
        mHeaderTouchView.setLayoutParams(layoutParams);
        mHeaderTouchView.setVisibility(mHeaderViewVisible?View.VISIBLE:View.GONE);
        
        
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mHeaderViewVisible) {
            drawChild(canvas, mHeaderView, getDrawingTime());
        }
    }

}

