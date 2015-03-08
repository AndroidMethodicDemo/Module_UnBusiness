package base.util.ui;

import android.view.View;

public class ViewUtils {

	
	/**
	 * 设置View的背景，解决动态改变Background后Padding无效的问题
	 * @param view
	 * @param resourceId
	 */
	public static void setViewBackground(View view,int resourceId){
		int paddingLeft = view.getPaddingLeft();
		int paddingTop = view.getPaddingTop();
		int paddingRight = view.getPaddingRight();
		int paddingBottom = view.getPaddingBottom();
		view.setBackgroundResource(resourceId);
		view.setPadding(paddingLeft,paddingTop, paddingRight, paddingBottom);
	}
}
