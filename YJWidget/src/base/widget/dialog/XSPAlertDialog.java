package base.widget.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import base.util.ui.XSSoftKeyboard;

import com.example.demoxswidget.R;

public class XSPAlertDialog {

	private Context ctx;

	public XSPAlertDialog(Context context) {
		ctx = context;
	}

	private AlertDialog alertDialog;
	private Window window;

	private int getWindowWidth() {

		DisplayMetrics dm = new DisplayMetrics();
		window.getWindowManager().getDefaultDisplay().getMetrics(dm);

		return dm.widthPixels;
	}

	private void setWindowWidth(int width) {

		WindowManager.LayoutParams lp = window.getAttributes();
		lp.width = width;
		window.setAttributes(lp);
	}

	private void setAlertTitleText(String txtTitle) {

		TextView txtDialogTitle = (TextView) window
				.findViewById(R.id.layout_dialog_title);
		txtDialogTitle.setText(txtTitle);
	}

	private void setMessgeText(String txtMessage) {

		TextView txtDialogMessage = (TextView) window
				.findViewById(R.id.alert_dialog_message);
		txtDialogMessage.setText(txtMessage);
	}

	private void setButtonInfo(int resID, String txtButton,
			OnClickListener clickListener) {

		Button btnDialog = (Button) window.findViewById(resID);

		btnDialog.setText(txtButton);
		btnDialog.setOnClickListener(clickListener);
	}

	public boolean isAlertDialogShowing() {
		return (null != alertDialog && alertDialog.isShowing());
	}

	public void dismissAlertDialog() {

		if (null == alertDialog) {
			return;
		}

		if (alertDialog.isShowing()) {
			alertDialog.hide();
		}

		alertDialog.dismiss();
		alertDialog = null;
		window = null;
	}

	/** 1
	 * 不懂，为什么要这样写呢？
	 */
	public void showAlertDialog0(String txtTitle, String txtMessage,
			boolean cancelable) {

		dismissAlertDialog();

		alertDialog = new AlertDialog.Builder(ctx).create();
		setCanceledOnTouchOutside(cancelable);
		alertDialog.show();
		window = alertDialog.getWindow();

		setWindowWidth((int) (getWindowWidth() * 0.9));

		window.setContentView(R.layout.base_xsp_dialog_001_alert_base);

		setAlertTitleText(txtTitle);

		setMessgeText(txtMessage);

		alertDialog.setCancelable(cancelable);
	}

	/**
	 * 
	 */
	public void showAlertDialog1(String txtTitle, String txtMessage,
			String txtButton1, OnClickListener clickListener1,
			boolean cancelable) {

		dismissAlertDialog();

		alertDialog = new AlertDialog.Builder(ctx).create();
		setCanceledOnTouchOutside(cancelable);
		alertDialog.show();
		window = alertDialog.getWindow();

		setWindowWidth((int) (getWindowWidth() * 0.9));

		window.setContentView(R.layout.base_xsp_dialog_002_alert_1_button);

		if (null == txtTitle || TextUtils.isEmpty(txtTitle)) {
			hideItem(R.id.layout_dialog_title);
		} else {
			setAlertTitleText(txtTitle);
		}

		setMessgeText(txtMessage);

		setButtonInfo(R.id.alert_dialog_button_1, txtButton1, clickListener1);

		alertDialog.setCancelable(cancelable);
	}

	/**
	 * 
	 */
	public void showAlertDialog1OnWindow(String txtTitle, String txtMessage,
			String txtButton1, OnClickListener clickListener1,
			boolean cancelable) {

		dismissAlertDialog();

		alertDialog = new AlertDialog.Builder(ctx).create();

		alertDialog.getWindow().setType(
				WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG); // 系统中关机对话框就是这个属性
		alertDialog.getWindow().setType(
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

		setCanceledOnTouchOutside(cancelable);
		alertDialog.show();
		window = alertDialog.getWindow();

		setWindowWidth((int) (getWindowWidth() * 0.9));

		window.setContentView(R.layout.base_xsp_dialog_002_alert_1_button);

		if (null == txtTitle || TextUtils.isEmpty(txtTitle)) {
			hideItem(R.id.layout_dialog_title);
		} else {
			setAlertTitleText(txtTitle);
		}

		setMessgeText(txtMessage);

		setButtonInfo(R.id.alert_dialog_button_1, txtButton1, clickListener1);

		alertDialog.setCancelable(cancelable);
	}
	/**
	 * 
	 */
	public void showAlertDialog2(String txtTitle, String txtMessage,
			String txtButton1, OnClickListener clickListener1,
			String txtButton2, OnClickListener clickListener2,
			boolean cancelable) {

		dismissAlertDialog();

		alertDialog = new AlertDialog.Builder(ctx).create();
		setCanceledOnTouchOutside(cancelable);
		alertDialog.show();
		window = alertDialog.getWindow();

		setWindowWidth((int) (getWindowWidth() * 0.9));

		window.setContentView(R.layout.base_xsp_dialog_003_alert_2_button);

		// setAlertTitleText(txtTitle);
		if (null == txtTitle || TextUtils.isEmpty(txtTitle)) {
			hideItem(R.id.layout_dialog_title);
		} else {
			setAlertTitleText(txtTitle);
		}

		setMessgeText(txtMessage);

		setButtonInfo(R.id.alert_dialog_button_1, txtButton1, clickListener1);
		setButtonInfo(R.id.alert_dialog_button_2, txtButton2, clickListener2);

		alertDialog.setCancelable(cancelable);
	}
	/**
	 * 
	 */	
	public void showAlertDialog2OnWindow(String txtTitle, String txtMessage,
			String txtButton1, OnClickListener clickListener1,
			String txtButton2, OnClickListener clickListener2,
			boolean cancelable) {

		dismissAlertDialog();

		alertDialog = new AlertDialog.Builder(ctx).create();
		
		alertDialog.getWindow().setType(
				WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG); // 系统中关机对话框就是这个属性
		alertDialog.getWindow().setType(
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		
		setCanceledOnTouchOutside(cancelable);
		alertDialog.show();
		window = alertDialog.getWindow();

		setWindowWidth((int) (getWindowWidth() * 0.9));

		window.setContentView(R.layout.base_xsp_dialog_003_alert_2_button);

		// setAlertTitleText(txtTitle);
		if (null == txtTitle || TextUtils.isEmpty(txtTitle)) {
			hideItem(R.id.layout_dialog_title);
		} else {
			setAlertTitleText(txtTitle);
		}

		setMessgeText(txtMessage);

		setButtonInfo(R.id.alert_dialog_button_1, txtButton1, clickListener1);
		setButtonInfo(R.id.alert_dialog_button_2, txtButton2, clickListener2);

		alertDialog.setCancelable(cancelable);
	}
	/**
	 * 
	 */
	public void showAlertMeetingDialog(String title, String msg,
			String btn1Msg, String btn2Msg, OnClickListener listener,
			boolean isCancelable, int dialogType) {

		dismissAlertDialog();
		alertDialog = new AlertDialog.Builder(ctx).create();
		alertDialog.show();
		window = alertDialog.getWindow();
		window.setContentView(R.layout.meeting_xsp_dialog_001_base);
		Button btnOne = (Button) window.findViewById(R.id.btn_dialog_one);
		Button btnTwo = (Button) window.findViewById(R.id.btn_dialog_two);
		TextView tvTitle = (TextView) window.findViewById(R.id.tv_dialog_title);
		TextView tvMsg = (TextView) window.findViewById(R.id.tv_dialog_msg);
		if (dialogType == 0) {
			btnOne.setVisibility(View.GONE);
			btnTwo.setBackgroundResource(R.drawable.base_selector_007_alert_dialog_down_selector);
		} else {
			btnOne.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					dismissAlertDialog();
				}
			});
			btnOne.setText(btn1Msg);
		}
		btnTwo.setText(btn2Msg);
		if (TextUtils.isEmpty(title)) {
			tvTitle.setVisibility(View.GONE);
		} else {
			tvTitle.setText(title);
		}
		tvMsg.setText(msg);
		if (listener != null) {
			btnTwo.setOnClickListener(listener);
		}
		setWindowWidth((int) (getWindowWidth() * 0.6));
		alertDialog.setCancelable(isCancelable);
	}

	private void setSelectTitleText(String txtTitle) {

		View view = window.findViewById(R.id.layout_select_dialog_title);
		TextView txtDialogTitle = (TextView) window
				.findViewById(R.id.select_dialog_title);
		if (null == txtTitle) {
			view.setVisibility(View.GONE);
		} else {
			txtDialogTitle.setText(txtTitle);
		}
	}

	private void hideItem(int resID) {

		TextView txtDialogItem = (TextView) window.findViewById(resID);
		txtDialogItem.setVisibility(View.GONE);
	}

	private void showItem(int resID) {
		LinearLayout layoutDialogItem = (LinearLayout) window
				.findViewById(resID);
		layoutDialogItem.setVisibility(View.VISIBLE);
		// TextView txtDialogItem = (TextView) window.findViewById(resID);
		// txtDialogItem.setVisibility(View.VISIBLE);
	}

	private void setItemInfo(int parentID, int resID, Object tag,
			String txtItem, OnClickListener clickListener) {

		LinearLayout layoutDialogItem = (LinearLayout) window
				.findViewById(parentID);
		TextView txtDialogItem = (TextView) window.findViewById(resID);

		// if (tag != null) txtDialogItem.setTag(tag);
		if (tag != null && layoutDialogItem != null)
			layoutDialogItem.setTag(tag);
		if (txtItem != null)
			txtDialogItem.setText(txtItem);
		if (clickListener != null && layoutDialogItem != null)
			layoutDialogItem.setOnClickListener(clickListener);
	}

	public void showSelectDialog1(Object tag, String txtTitle, String txtItem1,
			OnClickListener clickListener1) {

		showSelectDialog10(tag, txtTitle, txtItem1, clickListener1, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null);
	}

	public void showSelectDialog2(Object tag, String txtTitle, String txtItem1,
			OnClickListener clickListener1, String txtItem2,
			OnClickListener clickListener2) {

		showSelectDialog10(tag, txtTitle, txtItem1, clickListener1, txtItem2,
				clickListener2, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null);
	}

	public void showSelectDialog3(Object tag, String txtTitle, String txtItem1,
			OnClickListener clickListener1, String txtItem2,
			OnClickListener clickListener2, String txtItem3,
			OnClickListener clickListener3) {

		showSelectDialog10(tag, txtTitle, txtItem1, clickListener1, txtItem2,
				clickListener2, txtItem3, clickListener3, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null);
	}

	public void showSelectDialog4(Object tag, String txtTitle, String txtItem1,
			OnClickListener clickListener1, String txtItem2,
			OnClickListener clickListener2, String txtItem3,
			OnClickListener clickListener3, String txtItem4,
			OnClickListener clickListener4) {

		showSelectDialog10(tag, txtTitle, txtItem1, clickListener1, txtItem2,
				clickListener2, txtItem3, clickListener3, txtItem4,
				clickListener4, null, null, null, null, null, null, null, null,
				null, null, null, null);
	}

	public void showSelectDialog5(Object tag, String txtTitle, String txtItem1,
			OnClickListener clickListener1, String txtItem2,
			OnClickListener clickListener2, String txtItem3,
			OnClickListener clickListener3, String txtItem4,
			OnClickListener clickListener4, String txtItem5,
			OnClickListener clickListener5) {

		showSelectDialog10(tag, txtTitle, txtItem1, clickListener1, txtItem2,
				clickListener2, txtItem3, clickListener3, txtItem4,
				clickListener4, txtItem5, clickListener5, null, null, null,
				null, null, null, null, null, null, null);
	}

	public void showSelectDialog6(Object tag, String txtTitle, String txtItem1,
			OnClickListener clickListener1, String txtItem2,
			OnClickListener clickListener2, String txtItem3,
			OnClickListener clickListener3, String txtItem4,
			OnClickListener clickListener4, String txtItem5,
			OnClickListener clickListener5, String txtItem6,
			OnClickListener clickListener6) {

		showSelectDialog10(tag, txtTitle, txtItem1, clickListener1, txtItem2,
				clickListener2, txtItem3, clickListener3, txtItem4,
				clickListener4, txtItem5, clickListener5, txtItem6,
				clickListener6, null, null, null, null, null, null, null, null);
	}

	public void showSelectDialog10(Object tag, String txtTitle,
			String txtItem1, OnClickListener clickListener1, String txtItem2,
			OnClickListener clickListener2, String txtItem3,
			OnClickListener clickListener3, String txtItem4,
			OnClickListener clickListener4, String txtItem5,
			OnClickListener clickListener5, String txtItem6,
			OnClickListener clickListener6, String txtItem7,
			OnClickListener clickListener7, String txtItem8,
			OnClickListener clickListener8, String txtItem9,
			OnClickListener clickListener9, String txtItem10,
			OnClickListener clickListener10) {

		dismissAlertDialog();

		alertDialog = new AlertDialog.Builder(ctx).create();

		alertDialog.show();
		setCanceledOnTouchOutside(true);
		window = alertDialog.getWindow();

		setWindowWidth((int) (getWindowWidth() * 0.9));

		window.setContentView(R.layout.base_xsp_dialog_004_select);
		// if (!TextUtils.isEmpty(txtItem1) && null != clickListener1)
		// showItem(R.id.dialog_select_item_1);
		// if (!TextUtils.isEmpty(txtItem2) && null != clickListener2)
		// showItem(R.id.dialog_select_item_2);
		// if (!TextUtils.isEmpty(txtItem3) && null != clickListener3)
		// showItem(R.id.dialog_select_item_3);
		// if (!TextUtils.isEmpty(txtItem4) && null != clickListener4)
		// showItem(R.id.dialog_select_item_4);
		// if (!TextUtils.isEmpty(txtItem5) && null != clickListener5)
		// showItem(R.id.dialog_select_item_5);
		// if (!TextUtils.isEmpty(txtItem6) && null != clickListener6)
		// showItem(R.id.dialog_select_item_6);
		// if (!TextUtils.isEmpty(txtItem7) && null != clickListener7)
		// showItem(R.id.dialog_select_item_7);
		// if (!TextUtils.isEmpty(txtItem8) && null != clickListener8)
		// showItem(R.id.dialog_select_item_8);
		// if (!TextUtils.isEmpty(txtItem9) && null != clickListener9)
		// showItem(R.id.dialog_select_item_9);
		// if (!TextUtils.isEmpty(txtItem10) && null != clickListener10)
		// showItem(R.id.dialog_select_item_10);
		if (!TextUtils.isEmpty(txtItem1) && null != clickListener1)
			showItem(R.id.select_layout_item_1);
		if (!TextUtils.isEmpty(txtItem2) && null != clickListener2)
			showItem(R.id.select_layout_item_2);
		if (!TextUtils.isEmpty(txtItem3) && null != clickListener3)
			showItem(R.id.select_layout_item_3);
		if (!TextUtils.isEmpty(txtItem4) && null != clickListener4)
			showItem(R.id.select_layout_item_4);
		if (!TextUtils.isEmpty(txtItem5) && null != clickListener5)
			showItem(R.id.select_layout_item_5);
		if (!TextUtils.isEmpty(txtItem6) && null != clickListener6)
			showItem(R.id.select_layout_item_6);
		if (!TextUtils.isEmpty(txtItem7) && null != clickListener7)
			showItem(R.id.select_layout_item_7);
		if (!TextUtils.isEmpty(txtItem8) && null != clickListener8)
			showItem(R.id.select_layout_item_8);
		if (!TextUtils.isEmpty(txtItem9) && null != clickListener9)
			showItem(R.id.select_layout_item_9);
		if (!TextUtils.isEmpty(txtItem10) && null != clickListener10)
			showItem(R.id.select_layout_item_10);

		setSelectTitleText(txtTitle);

		setItemInfo(R.id.select_layout_item_1, R.id.dialog_select_item_1, tag,
				txtItem1, clickListener1);
		setItemInfo(R.id.select_layout_item_2, R.id.dialog_select_item_2, tag,
				txtItem2, clickListener2);
		setItemInfo(R.id.select_layout_item_3, R.id.dialog_select_item_3, tag,
				txtItem3, clickListener3);
		setItemInfo(R.id.select_layout_item_4, R.id.dialog_select_item_4, tag,
				txtItem4, clickListener4);
		setItemInfo(R.id.select_layout_item_5, R.id.dialog_select_item_5, tag,
				txtItem5, clickListener5);
		setItemInfo(R.id.select_layout_item_6, R.id.dialog_select_item_6, tag,
				txtItem6, clickListener6);
		setItemInfo(R.id.select_layout_item_7, R.id.dialog_select_item_7, tag,
				txtItem7, clickListener7);
		setItemInfo(R.id.select_layout_item_8, R.id.dialog_select_item_8, tag,
				txtItem8, clickListener8);
		setItemInfo(R.id.select_layout_item_9, R.id.dialog_select_item_9, tag,
				txtItem9, clickListener9);
		setItemInfo(R.id.select_layout_item_10, R.id.dialog_select_item_10,
				tag, txtItem10, clickListener10);
	}

	/**
	 * @param title
	 *            标题
	 * @param itemNames
	 *            各选项名字id
	 * @param itemClickEvens
	 *            各选项点击事件，若itemClickEvens数量为1，则全部选项共用第一个元素
	 * @param tags
	 *            各选项tag,若tags数量为1，则全部选项共用第一个元素
	 */
	public void showSelectDialogNI(String title, Integer[] itemNames,
			OnClickListener[] itemClickEvens, Object[] tags) {
		String[] itemNameStrings = new String[itemNames.length];
		for (int i = 0; i < itemNameStrings.length; i++) {
			itemNameStrings[i] = ctx.getResources().getString(itemNames[i]);
		}
		showSelectDialogNS(title, itemNameStrings, itemClickEvens, tags);
	}

	/**
	 * @param title
	 *            标题
	 * @param itemNames
	 *            各选项名字
	 * @param itemClickEvens
	 *            各选项点击事件 若itemClickEvens数量为1，则全部选项共用第一个元素
	 * @param tags
	 *            各选项tag,若tags数量为1，则全部选项共用第一个元素
	 */
	public void showSelectDialogNS(String title, String[] itemNames,
			OnClickListener[] itemClickEvens, Object[] tags) {
		dismissAlertDialog();
		if (null == ctx) {
			return;
		}
		alertDialog = new AlertDialog.Builder(ctx).create();
		setCanceledOnTouchOutside(true);
		alertDialog.show();
		alertDialog.setCancelable(true);
		window = alertDialog.getWindow();

		setWindowWidth((int) (getWindowWidth() * 0.9));

		window.setContentView(R.layout.base_xsp_dialog_004_select);
		LinearLayout items_container = (LinearLayout) window
				.findViewById(R.id.items_container);

		// 设置标题
		// if (!TextUtils.isEmpty(title)) {
		// ((TextView)items_container.getChildAt(0)).setText(title);
		// }
		setSelectTitleText(title);

		int viewCount = items_container.getChildCount();
		for (int i = 1; i <= itemNames.length && i < viewCount; i++) {
			LinearLayout layoutItem = (LinearLayout) items_container
					.getChildAt(i);
			TextView item = (TextView) layoutItem.getChildAt(0);
			// 选项菜单不能为空
			if (null == itemNames[i - 1] || "".equals(itemNames[i - 1])) {
				continue;
			}

			item.setText(itemNames[i - 1]);
			if (tags != null) {
				if (tags.length == 1) {
					layoutItem.setTag(tags[0]);
					// item.setTag(tags[0]);
				} else {
					layoutItem.setTag(tags[i - 1]);
					// item.setTag(tags[i-1]);
				}
			}
			if (itemClickEvens != null) {
				if (itemClickEvens.length == 1) {
					layoutItem.setOnClickListener(itemClickEvens[0]);
					// item.setOnClickListener(itemClickEvens[0]);
				} else {
					layoutItem.setOnClickListener(itemClickEvens[i - 1]);
					// item.setOnClickListener(itemClickEvens[i-1]);
				}
			}
			if (i == itemNames.length) {
				// XSCommonUtil.setViewBackground(item,
				// R.drawable.base_selector_007_alert_dialog_down_selector);
			}
			layoutItem.setVisibility(View.VISIBLE);
		}
	}

	public void showEditTextDialog(String txtTitle, String editStr,
			final View.OnClickListener confirm) {
		View view = LayoutInflater.from(ctx).inflate(
				R.layout.base_xsp_dialog_006_edit_text, null);
		final Dialog mDialog = new Dialog(ctx);

		TextView groupNameEdtTitle = (TextView) view
				.findViewById(R.id.layout_dialog_title);
		groupNameEdtTitle.setText(txtTitle);

		final EditText groupNameEdt = (EditText) view
				.findViewById(R.id.edt_new_group_name);
		if (!TextUtils.isEmpty(editStr)) {
			groupNameEdt.setText(editStr);
		}

		Button confirmBtn = (Button) view
				.findViewById(R.id.dialog_button_confirm);
		Button cancelBtn = (Button) view
				.findViewById(R.id.dialog_button_cancel);

		confirmBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(groupNameEdt.getText().toString())) {
					return;
				}
				// 优化：先关软键盘，再关dialog，再调onClick事件
				XSSoftKeyboard.hideKeyboard(v);
				mDialog.dismiss();

				confirm.onClick(groupNameEdt);
			}
		});

		cancelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				XSSoftKeyboard.hideKeyboard(v);
				mDialog.dismiss();
			}
		});

		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(view);
		mDialog.show();
	}

	public void setCanceledOnTouchOutside(boolean outsideCancelable) {
		if (null != alertDialog) {
			alertDialog.setCanceledOnTouchOutside(outsideCancelable);
		}
	}
}
