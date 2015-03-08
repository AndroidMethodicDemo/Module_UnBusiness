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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoxswidget.R;

@SuppressLint("ViewConstructor")
public class SharePopupWindow extends PopupWindow {
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

	public SharePopupWindow(Context context,
			View.OnClickListener[] itemsListener) {
		super(context);
		this.mContext = context;

		initUI(itemsListener);
	}

	/**
	 * 初始化界面
	 */
	private void initUI(View.OnClickListener[] itemsListener) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.base_share_popupwindow,
				null);
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
		// 设置监听
		ImageView btnWechat=(ImageView) mMenuView.findViewById(R.id.iv_1);
		ImageView btnWechatMember=(ImageView) mMenuView.findViewById(R.id.iv_2);
		ImageView btnWeibo=(ImageView) mMenuView.findViewById(R.id.iv_3);
		Button btnCancel=(Button) mMenuView.findViewById(R.id.btnCancel);
		
		btnWechat.setOnClickListener(itemsListener[0]);
		btnWechatMember.setOnClickListener(itemsListener[1]);
		btnWeibo.setOnClickListener(itemsListener[2]);
		btnCancel.setOnClickListener(itemsListener[3]);

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
		if (durationMills < 0)
			return;

		showAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
				0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
				0.0f);
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
