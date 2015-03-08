package base.widget.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;

/**
 * 警告对话框帮助类
 * 
 * @author zWX202297
 *
 */
public class XSAlertDialogUtil {
	
	public static void show(Context context, int titleId, int messageId, int leftBtnTextId, DialogInterface.OnClickListener leftClickListener, boolean cancelable){
		show(context, titleId, messageId, leftBtnTextId, leftClickListener, -1, null, cancelable);
	}
	
	public static void show(Context context, int titleId, int messageId, int leftBtnTextId, DialogInterface.OnClickListener leftClickListener, int rightBtnTextId, DialogInterface.OnClickListener rightClickListener, boolean cancelable){
		show(context, null, titleId, messageId, leftBtnTextId, leftClickListener, rightBtnTextId, rightClickListener, cancelable);
	}
	
	public static void show(Context context, Drawable icon, int titleId, int messageId, int leftBtnTextId, DialogInterface.OnClickListener leftClickListener, int rightBtnTextId, DialogInterface.OnClickListener rightClickListener, boolean cancelable){
		new AlertDialog.Builder(context)
			.setIcon(icon)
			.setTitle(titleId)
			.setMessage(messageId)
			.setPositiveButton(leftBtnTextId, leftClickListener)
			.setNegativeButton(rightBtnTextId, rightClickListener)
			.setCancelable(cancelable)
			.create()
			.show();
	}
}
