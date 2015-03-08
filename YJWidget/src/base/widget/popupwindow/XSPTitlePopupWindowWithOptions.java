package base.widget.popupwindow;

import com.example.demoxswidget.R;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class XSPTitlePopupWindowWithOptions extends PopupWindow {
	private static String TAG = XSPTitlePopupWindowWithOptions.class.getSimpleName();
	
	private Context mContext;
	private View mMenuView;
	private LinearLayout mContentLayout;

	public XSPTitlePopupWindowWithOptions(Context context) {
		super(context);
	}
	
	public XSPTitlePopupWindowWithOptions(Context context, int[] txtResID, int[] LeftIconResID,
			View.OnClickListener[] itemsListener) {
		super(context);
		this.mContext = context;
		
		initUI(txtResID, LeftIconResID, itemsListener);
	}

	/**
	 * 初始化界面
	 */
	private void initUI(int[] txtResID, int[] LeftIconResID, View.OnClickListener[] itemsListener) {
		if (null == txtResID || null == LeftIconResID || null == itemsListener ||
				LeftIconResID.length != txtResID.length || LeftIconResID.length != itemsListener.length) {
//			LogApi.e(TAG, "resID, resID and itemsListener == null or size not same");
			
			return;
		}
		
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.base_xsp_popwindow_002_title_with_options, null);
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
		
		// 动态设置文字、图片和绑定点击事件
		for (int i = 0; i < txtResID.length && i < mContentLayout.getChildCount(); i++) {
			TextView txt_i = (TextView) mContentLayout.getChildAt(i);
			txt_i.setText(txtResID[i]);
			
			Drawable drawable = mContext.getResources().getDrawable(LeftIconResID[i]);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			txt_i.setCompoundDrawables(drawable, null, null, null);
			
			txt_i.setOnClickListener(itemsListener[i]);
			txt_i.setVisibility(View.VISIBLE);
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
	
}
