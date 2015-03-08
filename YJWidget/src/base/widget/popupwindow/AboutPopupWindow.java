package base.widget.popupwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.demoxswidget.R;

public class AboutPopupWindow extends PopupWindow {
	private static String TAG = AboutPopupWindow.class.getSimpleName();
	
	private Context mContext;
	private View mMenuView;
	private LinearLayout mContentLayout;

	public AboutPopupWindow(Context context) {
		super(context);
		this.mContext = context;
		initUI();
	}

	/**
	 * 初始化界面
	 */
	private void initUI() {
		
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.base_about_popwindow, null);
		mContentLayout = (LinearLayout) mMenuView.findViewById(R.id.title_pop_layout);

		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int height = mMenuView.findViewById(R.id.title_pop_layout).getBottom();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y > height) {
						dismiss();
					}
				}
				return true;
			}
		});

		// 设置XSPDownUpPopupWindow的View
		this.setContentView(mMenuView);
		// 设置XSPDownUpPopupWindow弹出窗体的宽
		this.setWidth(100);
		// 设置XSPDownUpPopupWindow弹出窗体的高
		this.setHeight(100);
		// 设置XSPDownUpPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置XSPDownUpPopupWindow背景动画
		this.setAnimationStyle(R.style.AnimFade);
		// 实例化一个ColorDrawable颜色为透明
		ColorDrawable dw = new ColorDrawable(0x50000000);
		// 设置XSPDownUpPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
	}
	
}
