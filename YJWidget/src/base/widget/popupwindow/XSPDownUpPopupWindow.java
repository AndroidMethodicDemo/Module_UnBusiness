package base.widget.popupwindow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoxswidget.R;

@SuppressLint("ViewConstructor")
public class XSPDownUpPopupWindow extends PopupWindow {
	private Context mContext;
	private View mMenuView;
	private LinearLayout mContentLayout;

	// 弹出框动画、背景
	private Animation showAnimation;
	private Animation hideAnimation;
	@SuppressWarnings("unused")
	private Drawable background;
	private long duration;

	private Handler mHandle = new Handler();

	public XSPDownUpPopupWindow(Context context, int txtResID, int[] btnResID,
			View.OnClickListener[] itemsListener, OnKeyListener keyListener) {
		super(context);
		this.mContext = context;

		initUI(txtResID, btnResID, itemsListener, keyListener);
	}

	/**
	 * 初始化界面
	 */
	private void initUI(int txtResID, int[] btnResID,
			View.OnClickListener[] itemsListener, OnKeyListener keyListener) {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.base_xsp_popwindow_001_down_up,null);
		mContentLayout = (LinearLayout) mMenuView.findViewById(R.id.pop_layout);

		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});
		
		if (null != keyListener) {
			mMenuView.setFocusableInTouchMode(true);
			mMenuView.setOnKeyListener(keyListener);
		}

		int childViewCount = mContentLayout.getChildCount();
		// 第一个子View，固定为TextView
		if (0 != txtResID) {
			TextView titleText = (TextView) mContentLayout.getChildAt(0);
			titleText.setText(txtResID);
			titleText.setVisibility(View.VISIBLE);
		}
		// btn和item不为null
		if (btnResID != null && itemsListener != null && btnResID.length != 0
				&& btnResID.length != itemsListener.length) {
			Toast.makeText(mContext, "resID and resID != null or length!=length!=0 ", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (btnResID == null) {
			Log.e("XSPDownUpPopupWindow---", "btnResID is null!");
			return;
		}
		
		for (int i = 0; i < btnResID.length && i < childViewCount - 1; i++) {
			// 第一个子View，固定为TextView,button子view需要+1
			Button btn_i = (Button) mContentLayout.getChildAt(i + 1);
			btn_i.setText(btnResID[i]);
			btn_i.setOnClickListener(itemsListener[i]);
			btn_i.setVisibility(View.VISIBLE);
		}

		// 设置XSPDownUpPopupWindow的View
		this.setContentView(mMenuView);
		// 设置XSPDownUpPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置XSPDownUpPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.MATCH_PARENT);
		// 设置XSPDownUpPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置XSPDownUpPopupWindow背景动画
		this.setAnimationStyle(R.style.AnimFade);
		// 实例化一个ColorDrawable颜色为透明
		ColorDrawable dw = new ColorDrawable(0x50000000);
		// 设置XSPDownUpPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
	}

	/**
	 * 初始化动画效果
	 */
	public void initAnimation(long durationMills) {
	    if (durationMills < 0) return;
	    
	    showAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
	    showAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
	    showAnimation.setDuration(durationMills);
	    
	    hideAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f);
	    hideAnimation.setDuration(durationMills);
	    duration = hideAnimation.getDuration();
	}

	/**
	 * 修改动画效果
	 * 
	 * @param showAnimation
	 * @param hideAnimation
	 */
	public void modifyAnimation(Animation showAnimation, Animation hideAnimation) {
		if (null != showAnimation) {
			this.showAnimation = showAnimation;
		}

		if (null != hideAnimation) {
			this.hideAnimation = hideAnimation;
			duration = hideAnimation.getDuration();
		}

	}
	
	/**
	 * 取消动画效果
	 */
	public void cancelAnimation() {
		showAnimation = null;
		hideAnimation = null;
	}

	/**
	 * 修改背景
	 * 
	 * @param background
	 */

	public void modifyBackground(Drawable background) {
		if (null != background) {
			this.background = background;
			this.setBackgroundDrawable(background);
		}
	}

	@Override
	public void dismiss() {
		if (null != hideAnimation) {
			mMenuView.startAnimation(hideAnimation);

			mHandle.postDelayed(new Runnable() {
				@Override
				public void run() {
					dismissPopupWindow();
				}
			}, duration);
		} else {
			dismissPopupWindow();
		}
	}

	/**
	 * 调用PopupWindow的dismiss方法
	 */
	private void dismissPopupWindow() {
		try {
			super.dismiss();
		} catch (Exception e) {
		}
	}

	@Override
	public void showAsDropDown(View anchor, int xoff, int yoff) {
		if (null != showAnimation) {
			// 设置弹出窗体动画效果
			mContentLayout.startAnimation(showAnimation);
		}
		super.showAsDropDown(anchor, xoff, yoff);
	}

	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		if (null != showAnimation) {
			// 设置弹出窗体动画效果
			mContentLayout.startAnimation(showAnimation);
		}
		super.showAtLocation(parent, gravity, x, y);
	}

	@Override
	public void showAsDropDown(View anchor) {
		if (null != showAnimation) {
			// 设置弹出窗体动画效果
			mContentLayout.startAnimation(showAnimation);
		}
		super.showAsDropDown(anchor);
	}
	
}
