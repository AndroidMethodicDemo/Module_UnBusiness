package base.widget.listview.view;

import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import base.util.ui.XSResourceUtil;

import com.example.demoxswidget.R;

/**
 * 
 * 可上拉刷新的listview<br>
 * 需要上拉刷新功能时，设置OnRefreshListener监听处理刷新的操作，刷新结束时，在UI线程中调用onRefreshComplete结束刷新<br>
 * 不需要刷新功能时，不设置OnRefreshListener即可，已经设置过刷新且需要取消刷新功能时，调用removeOnRefreshListener方法<br>
 * 
 * @author zWX202297
 *
 */
public class XSWRefreshListView extends ListView implements OnScrollListener {
//	private static final String TAG = XSWRefreshListView.class.getSimpleName();
    
    // 用户自己注册监听的滚动监听器
    private OnScrollListener userScrollListener; 
    
	private final static int RELEASE_To_REFRESH = 0;
	private final static int PULL_To_REFRESH = 1;
	private final static int REFRESHING = 2;
	private final static int DONE = 3;
	private final static int LOADING = 4;

	private final static int RATIO = 3;

	private LayoutInflater inflater;

	private LinearLayout headerView;
	private XSEmptyCircleView headCircleView;
	private TextView headTipsTextview;

//	private int headContentWidth;
	private int headContentHeight;

	private int startY;
	private int state;

	private boolean isRecored;
	private boolean isBack;
	private boolean isRefreshable;
	
	private OnRefreshListener refreshListener;
	private OnLoadMoreListener onLoadMoreListener;
	
	private Animation animation;
	
	private LinearLayout footerView;
	private XSEmptyCircleView footerCircleView;
	private TextView footerTipsTextview;
	
//	private int footContentWidth;
	private int footContentHeight;
	
	private int loadMoreStartY;
	private int loadMoreState;
	
	private boolean loadMoreIsRecored;
	private boolean loadMoreIsBack;
	private boolean isLoadMoreAble;
	//true群组专用
	private boolean isGroupList;
	
	private Context context;
	
	public XSWRefreshListView(Context context) {
		super(context);
		
		this.context = context;
		
		init();
		
	}
	

	public XSWRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		this.context = context;
		
		init();
		
	}

	/**
	 * 
	 */
	private void init() {
	    
		setCacheColorHint(context.getResources().getColor(XSResourceUtil.getColorId(context, "rlv_cache_color_hint")));
		inflater = LayoutInflater.from(context);
		// 初始化头布局
		initHeaderView();
		// 初始化脚布局
		initFooterView();
		
		animation = AnimationUtils.loadAnimation(getContext(), R.anim.base_anim_refresh_listview_pull_loading_animation);
		animation.setInterpolator(new LinearInterpolator());
		
		super.setOnScrollListener(this);
	}


	private void initFooterView() {
		footerView = (LinearLayout) inflater.inflate(XSResourceUtil.getLayoutId(context, "base_xsp_view_005_refresh_list_footer"), null);
		footerCircleView = (XSEmptyCircleView) footerView.findViewById(XSResourceUtil.getId(context, "base_xsp_view_006_refresh_list_header_emptyCircleView"));
		footerTipsTextview = (TextView) footerView.findViewById(XSResourceUtil.getId(context, "base_xsp_view_006_refresh_list_header_tipsTextView"));
		
		measureView(footerView);
		footContentHeight = footerView.getMeasuredHeight();
//		footContentWidth = footerView.getMeasuredWidth();
		
		footerView.setPadding(0, 0, 0, -1 * footContentHeight);
		footerCircleView.setActualAngle(footContentHeight);
		footerView.invalidate();
		
		addFooterView(footerView, null, false);
		
		loadMoreState = DONE;
		isLoadMoreAble = false;
		footerView.setVisibility(View.GONE);
	}


	private void initHeaderView() {
		headerView = (LinearLayout) inflater.inflate(R.layout.base_xsp_view_006_refresh_list_header, null);
		headCircleView = (XSEmptyCircleView) headerView.findViewById(R.id.base_xsp_view_006_refresh_list_header_emptyCircleView);
		headTipsTextview = (TextView) headerView.findViewById(R.id.base_xsp_view_006_refresh_list_header_tipsTextView);
		// 测量View
		measureView(headerView);
		headContentHeight = headerView.getMeasuredHeight();
//		headContentWidth = headView.getMeasuredWidth();
		
		headerView.setPadding(0, -1 * headContentHeight, 0, 0);
		headCircleView.setActualAngle(headContentHeight);
		headerView.invalidate();
		
		state = DONE;
		isRefreshable = false;
		headerView.setVisibility(View.GONE);
	}

	int mPullDownDistance;
	int mPullUpDistance;
	boolean isPullDown;
	boolean isPullUp;
	public boolean onTouchEvent(MotionEvent event) {
		if (isRefreshable && getFirstVisiblePosition() == 0) {
		    isPullDown = true;
		    isPullUp = false;
			pullDownRefresh(event);
		}
		
		if(isLoadMoreAble && getLastVisiblePosition() == getCount()-1){
		    if (getFirstVisiblePosition() != 0) {
		        isPullDown = false;
		    }
		    isPullUp = true;
            pullUpLoadMore(event);
        }
		
		return super.onTouchEvent(event);
	}

	private void pullUpLoadMore(MotionEvent event) {
		switch (event.getAction()) {
		    case MotionEvent.ACTION_DOWN:
		    	if(!loadMoreIsRecored){
		    		loadMoreIsRecored = true;
		            loadMoreStartY = (int) event.getY();
		        }
		        break;
		        
		    case MotionEvent.ACTION_UP:
		    	if (loadMoreState != REFRESHING && loadMoreState != LOADING) {
					
					if (loadMoreState == PULL_To_REFRESH) {
						loadMoreState = DONE;
						changeFooterViewByState();
					}
					
					if (loadMoreState == RELEASE_To_REFRESH) {
						loadMoreState = REFRESHING;
						changeFooterViewByState();
						onLoadMore();
					}
				}
		    	
		    	loadMoreIsRecored = false;
		    	loadMoreIsBack = false;
		    	break;
		        
		    case MotionEvent.ACTION_MOVE:
		        int tempY = (int) event.getY();
		        if(!loadMoreIsRecored){
		            loadMoreIsRecored = true;
		            loadMoreStartY = tempY;
		        }
		        
		        if(loadMoreState != REFRESHING && loadMoreIsRecored && loadMoreState != LOADING){
		        	if (loadMoreState == RELEASE_To_REFRESH) {

						setSelection(getCount()-1);

						if (((loadMoreStartY - tempY) / RATIO < footContentHeight)
								&& (loadMoreStartY - tempY) > 0) {
							loadMoreState = PULL_To_REFRESH;
							changeFooterViewByState();
						}
						else if (loadMoreStartY - tempY <= 0) {
							loadMoreState = DONE;
							changeFooterViewByState();
						}
						else {
						}
					}
		        }
		        
		        if (loadMoreState == PULL_To_REFRESH) {
					setSelection(getCount()-1);
					if ((loadMoreStartY - tempY) / RATIO >=  footContentHeight) {
						loadMoreState = RELEASE_To_REFRESH;
						loadMoreIsBack = true;
						changeFooterViewByState();
					}
					else if (loadMoreStartY - tempY <=  0) {
						loadMoreState = DONE;
						changeFooterViewByState();
					}
				}
		        
		        if (loadMoreState == DONE) {
					if (loadMoreStartY - tempY > 0) {
						loadMoreState = PULL_To_REFRESH;
						changeFooterViewByState();
					}
				}
		        
		        if (loadMoreState == PULL_To_REFRESH || loadMoreState == RELEASE_To_REFRESH) {
		        	mPullUpDistance = -1 * footContentHeight + (loadMoreStartY - tempY) / RATIO;
		        	footerView.setPadding(0, 0, 0, mPullUpDistance);
				}
		        break;
		}
	}


	private void pullDownRefresh(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (!isRecored) {
				isRecored = true;
				startY = (int) event.getY();
			}
			break;

		case MotionEvent.ACTION_UP:
			if (state != REFRESHING && state != LOADING) {
				if (state == DONE) {

				}
				if (state == PULL_To_REFRESH) {
					state = DONE;
					changeHeaderViewByState();
				}
				if (state == RELEASE_To_REFRESH) {
					state = REFRESHING;
					changeHeaderViewByState();
					onRefresh();
				}
			}

			isRecored = false;
			isBack = false;

			break;

		case MotionEvent.ACTION_MOVE:
			int tempY = (int) event.getY();

			if (!isRecored) {
				isRecored = true;
				startY = tempY;
			}
			
			if (state != REFRESHING && isRecored && state != LOADING) {
				if (state == RELEASE_To_REFRESH) {

					setSelection(0);

					if (((tempY - startY) / RATIO < headContentHeight)
							&& (tempY - startY) > 0) {
						state = PULL_To_REFRESH;
						changeHeaderViewByState();
					}
					else if (tempY - startY <= 0) {
						state = DONE;
						changeHeaderViewByState();
					}
					else {
					}
				}
				if (state == PULL_To_REFRESH) {
					setSelection(0);
					if ((tempY - startY) / RATIO >= headContentHeight) {
						state = RELEASE_To_REFRESH;
						isBack = true;
						changeHeaderViewByState();
					}
					else if (tempY - startY <= 0) {
						state = DONE;
						changeHeaderViewByState();
					}
				}

				if (state == DONE) {
					if (tempY - startY > 0) {
						state = PULL_To_REFRESH;
						changeHeaderViewByState();
					}
				}

				if (state == PULL_To_REFRESH || state == RELEASE_To_REFRESH) {
					mPullDownDistance =  -1 * headContentHeight + (tempY - startY) / RATIO;
					headerView.setPadding(0, mPullDownDistance, 0, 0);
					
				}
			}

			break;
		}
	}

	private void changeHeaderViewByState() {
		switch (state) {
			case RELEASE_To_REFRESH://松开刷新
				headTipsTextview.setText(R.string.ua_rlv_list_head_up_update_str);
				break;
				
			case PULL_To_REFRESH://下拉刷新
				if (isBack) {
					isBack = false;
				}
				headTipsTextview.setText(R.string.ua_rlv_list_head_down_update_str);
				break;
	
			case REFRESHING://正在刷新
				mPullDownDistance = 0;
				headCircleView.startAnimation(animation);
				headerView.setPadding(0, mPullDownDistance, 0, 0);
				headTipsTextview.setText(R.string.ua_rlv_list_head_waitting_update_str);
				break;
				
			case DONE://完成刷新
				headCircleView.clearAnimation();
				mPullDownDistance = -1 * headContentHeight;
				headerView.setPadding(0, mPullDownDistance, 0, 0);
				headCircleView.initSweepAngle();
				break;
		}
	}

	private boolean isAddHeaderView;
	public void setOnRefreshListener(OnRefreshListener refreshListener) {
		this.refreshListener = refreshListener;
		isRefreshable = true;
		headerView.setVisibility(View.VISIBLE);
		
		if(!isAddHeaderView){
			addHeaderView(headerView, null, false);
			isAddHeaderView = true;
		}
	}
	
	public void removeOnRefreshListener() {
	    this.refreshListener = null;
	    isRefreshable = false;
	    headerView.setVisibility(View.GONE);
	}

	public interface OnRefreshListener {
		public void onRefresh();
	}

	/**
	 * 更新结束调用的方法，默认更新结束时间为当前时间
	 * 
	 */
	@SuppressWarnings("deprecation")
	public void onRefreshComplete() {
	    String time = getResources().getString(XSResourceUtil.getStringId(context, "ua_rlv_list_head_least_update_str")) + new Date().toLocaleString();
	    onRefreshComplete(time);
	}
	
	/**
	 *  更新结束调用的方法，更新时间为传递的时间
	 * 
	 * @param time 更新结束的时间，如果为null，不再进行设置时间
	 */
	public void onRefreshComplete(String time) {
	    state = DONE;
        changeHeaderViewByState();
	}

	private void onRefresh() {
		if (refreshListener != null) {
			refreshListener.onRefresh();
		}
	}

	/**
	 * 不知道这是在干什么？？
	 */
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	/**
     * 设置适配器，顶部下拉刷新时间默认为当前时间
     * 
     * @param adapter
     */
    @SuppressWarnings("deprecation")
	@Override
    public void setAdapter(ListAdapter adapter) {
        String time = getResources().getString(XSResourceUtil.getStringId(context, "ua_rlv_list_head_least_update_str")) + new Date().toLocaleString();
        setAdapter(adapter, time);
    }
	
	/**
	 * 设置适配器，顶部时间为传入的时间
	 * 
	 * @param adapter
	 * @param time 顶部下拉刷新对应的时间，如果为null，不再进行设置时间
	 */
	public void setAdapter(ListAdapter adapter, String time) {
        super.setAdapter(adapter);
	}
	
	
	@Override
	public void setOnScrollListener(OnScrollListener l) {
	    userScrollListener = l;
	}

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(userScrollListener != null){
            userScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount,
            int totalItemCount) {
    	if(isPullDown){
    		
    		if(mPullDownDistance < 0){
    			headCircleView.updateDrawAngle(headContentHeight + mPullDownDistance);
    		}else{
    			headCircleView.updateDrawAngle(headContentHeight);
    		}
    	}
    	
    	if(isPullUp){
	    	if(mPullUpDistance < 0){
				footerCircleView.updateDrawAngle(footContentHeight + mPullUpDistance);
			}else{
				footerCircleView.updateDrawAngle(footContentHeight);
			}
    	}
    	
        if (userScrollListener != null) {
            userScrollListener.onScroll(absListView, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }
    
    public void setOnLoadMoreListener(OnLoadMoreListener listener){
        onLoadMoreListener = listener;
        isLoadMoreAble = true;
        footerView.setVisibility(View.VISIBLE);
    }
    

    private void onLoadMore() {
        if (onLoadMoreListener != null) {
            onLoadMoreListener.onLoadMore();
        }
    }
    
    public void removeOnLoadMoreListener() {
        onLoadMoreListener = null;
        isLoadMoreAble = false;
        footerView.setVisibility(View.GONE);
    }
    
    public void onLoadMoreComplete(){
        loadMoreState = DONE;
        changeFooterViewByState();
    }
    
    public void setGroupLoadMore(boolean isGroupList){
    	this.isGroupList = isGroupList;
    }
    
    private void changeFooterViewByState() {
        
        if(isLoadMoreAble){
            switch (loadMoreState) {
            
	            case RELEASE_To_REFRESH://松开刷新
					footerTipsTextview.setText(R.string.ua_rlv_list_head_up_update_str);
					break;
					
				case PULL_To_REFRESH://上拉刷新
					if (isGroupList) {
						footerView.setVisibility(View.VISIBLE);
					}
					
					if (loadMoreIsBack) {
						loadMoreIsBack = false;
					}
					footerTipsTextview.setText(R.string.ua_rlv_list_footer_up_update_str);
					break;
		
				case REFRESHING://正在刷新
					mPullUpDistance = 0;
					footerCircleView.startAnimation(animation);
					footerView.setPadding(0, 0, 0, mPullUpDistance);
					footerTipsTextview.setText(R.string.ua_rlv_list_head_waitting_update_str);
					break;
            
	            case DONE://完成刷新
	            	footerCircleView.clearAnimation();
	            	mPullUpDistance = -1 * footContentHeight;
					footerView.setPadding(0, 0, 0, mPullUpDistance);
					footerCircleView.initSweepAngle();
					if (isGroupList) {
						footerView.setVisibility(View.GONE);
					}
            }
        }
    }
    
    public void changeSate2Refreashing() {
        this.state = REFRESHING;
        isPullDown = true;
        mPullDownDistance = -1 * headContentHeight;
        headCircleView.updateDrawAngle(headContentHeight);
        changeHeaderViewByState();
    }

    public interface OnLoadMoreListener {
        public void onLoadMore();
    }
	
}
