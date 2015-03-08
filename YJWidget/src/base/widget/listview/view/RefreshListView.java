package base.widget.listview.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.demoxswidget.R;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class RefreshListView extends ListView {
	
	private int firstVisibleItem;
	private int downY;
	private int headerViewHeight;
	private View headerView;
	private ImageView ivArrow;
	private ProgressBar pbProgress;
	private TextView tvState;
	private TextView tvLastUpdateTime;
	
	private final int DOWN_PULL = 0;
	private final int RELEASE_REFRESH = 1;
	private final int REFRESHING = 2;
	
	private int currentState = DOWN_PULL;
	private RotateAnimation upAnimation;
	private RotateAnimation downAnimation;
	private LoadDataCallback mOnRefreshListener;
	private boolean isScroll2Bottom;
	private View footerView;
	private int footerViewHeight;
	private boolean isLoadingMore = false;
	private int scroll=0;

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initHeader();
		initFooter();
		
		setOnScrollListener(new MyListener());
	}

	private void initFooter() {
		footerView = View.inflate(getContext(), R.layout.listview_footer, null);
		footerView.measure(0, 0);
		
		footerViewHeight = footerView.getMeasuredHeight();
		
		footerView.setPadding(0, -footerViewHeight, 0, 0);
		
		addFooterView(footerView);
	}

	public void initHeader() {
		View placeHolderView = View.inflate(this.getContext(),R.layout.view_header_placeholder, null);
		addHeaderView(placeHolderView);
		
		// ListView下拉刷新头
		headerView = View.inflate(getContext(), R.layout.listview_header, null);
		ivArrow = (ImageView) headerView.findViewById(R.id.iv_listview_header_arrow);
		pbProgress = (ProgressBar) headerView.findViewById(R.id.pb_progress);
		tvState = (TextView) headerView.findViewById(R.id.tv_refresh_state);
		tvLastUpdateTime = (TextView) headerView.findViewById(R.id.tv_last_update_time);
		
		headerView.measure(0, 0);
		
		headerViewHeight = headerView.getMeasuredHeight();
		
		headerView.setPadding(0, -headerViewHeight, 0, 0);
		
		addHeaderView(headerView);
		
		initAnimation();
	}
	public void setScroll(int scroll){
		this.scroll=scroll;
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			int moveY = (int) ev.getY();
			
			int diff = (moveY - downY) / 2;
			
//			if(firstVisibleItem == 0 && diff > 0 && currentState != REFRESHING) {
			if(scroll == 0 && diff > 0 && currentState != REFRESHING) {
				int paddingTop = -headerViewHeight + diff;
				
				if(paddingTop > 0 && currentState == DOWN_PULL) {
					currentState = RELEASE_REFRESH;
					refreshHeaderViewState();
				} else if(paddingTop < 0 && currentState == RELEASE_REFRESH) {
					currentState = DOWN_PULL;
					refreshHeaderViewState();
				}
				
				headerView.setPadding(0, paddingTop, 0, 0);
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			if(currentState == RELEASE_REFRESH) {
				currentState = REFRESHING;
				refreshHeaderViewState();
				headerView.setPadding(0, 0, 0, 0);
				//------------------��ȡ�������------------------------
				if(mOnRefreshListener != null) {
//					mOnRefreshListener.onRefresh();
					loadNewData();
				}
			} else if(currentState == DOWN_PULL) {
				headerView.setPadding(0, -headerViewHeight, 0, 0);
			}
			break;
		default:
			break;
		}
		return super.onTouchEvent(ev);
	}
	
	class MyListener implements OnScrollListener{
		
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if(isScroll2Bottom && 
					(scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING)) {
//			System.out.println("�������ײ�");
				
				if(!isLoadingMore) {
					isLoadingMore = true;
					footerView.setPadding(0, 0, 0, 0);
					setSelection(getCount() -1);
					
					if(mOnRefreshListener != null) {
//						mOnRefreshListener.loadOldData();
						/*-------------------------------------------------
						 * ������ǰ�����
						 * ��Ҫ���̳߳���ִ�У����������߳��и��½���
						 */
						loadOldData();
					}
				}
			}
		}
		
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			RefreshListView.this.firstVisibleItem = firstVisibleItem;
			
			if(getLastVisiblePosition() == (getCount() -1)) {
				isScroll2Bottom = true;
			} else {
				isScroll2Bottom = false;
			}
		}
	}// End of OnRefreshListener

	public void setOnRefreshListener(LoadDataCallback listener) {
		this.mOnRefreshListener = listener;
	}
	
	private void loadNewData(){
		new AsyncTask<Void, Void, BaseAdapter>(){

			@Override
			protected BaseAdapter doInBackground(Void... params) {
				return mOnRefreshListener.loadNewData();
				
			}
			
			@Override
			protected void onPostExecute(BaseAdapter result) {
				onRefreshFinish();
				result.notifyDataSetChanged();
			}
			
		}.execute();
	}
	private void loadOldData(){
		new AsyncTask<Void, Void, BaseAdapter>(){
			@Override
			protected BaseAdapter doInBackground(Void... params) {
				return mOnRefreshListener.loadOldData();
			}
			@Override
			protected void onPostExecute(BaseAdapter result) {
				onRefreshFinish();
				result.notifyDataSetChanged();
			}
		}.execute();
	}
	/**
	 * ������ݳɹ�֮��Ľ������
	 */
	public void onRefreshFinish() {
		if(isLoadingMore) {
			isLoadingMore = false;
			footerView.setPadding(0, -footerViewHeight, 0, 0);
		} else {
			headerView.setPadding(0, -headerViewHeight, 0, 0);
			currentState = DOWN_PULL;
			ivArrow.setVisibility(View.VISIBLE);
			pbProgress.setVisibility(View.GONE);
			tvLastUpdateTime.setText("���ˢ��ʱ��: " + getLastUpdateTime());
		}
	}

	/**
	 * ��Ҫ�ķ���
	 */
	private void refreshHeaderViewState() {
		if(currentState == DOWN_PULL) {	// ����ˢ��״̬
			ivArrow.startAnimation(downAnimation);
			tvState.setText("����ˢ��");
		} else if(currentState == RELEASE_REFRESH) {	// �ͷ�ˢ��
			ivArrow.startAnimation(upAnimation);
			tvState.setText("�ɿ�ˢ��");
		} else if(currentState == REFRESHING) {	// ����ˢ��
			ivArrow.clearAnimation();
			ivArrow.setVisibility(View.GONE);
			pbProgress.setVisibility(View.VISIBLE);
			tvState.setText("����ˢ��");
		}
	}
	
	/**
	 * ������������
	 */
	private void initAnimation() {
		upAnimation = new RotateAnimation(
				0f, -180f, 
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF, 0.5f);
		upAnimation.setDuration(500);
		upAnimation.setFillAfter(true);
		
		downAnimation = new RotateAnimation(
				-180f, -360f, 
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF, 0.5f);
		downAnimation.setDuration(500);
		downAnimation.setFillAfter(true);
	}
	
	/**
	 * ������µ�ʱ��
	 * @return
	 */
	private String getLastUpdateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}
}
