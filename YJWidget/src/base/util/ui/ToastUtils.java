package base.util.ui;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoxswidget.R;

public class ToastUtils {
	
	private static Context mContext;
	private static View mView;
	private static Toast mToast;
	private static Toast mCenterToast;
//  private final static String TAG = "ToastUtil";
  private static Toast uitoast = null;
  public static void toastOnUiThread(Activity activity, final String message) {

      final Application application = activity.getApplication();
      activity.runOnUiThread(new Runnable() {
          public void run() {
          	if (null == uitoast) {
          		uitoast = Toast.makeText(application, message, Toast.LENGTH_SHORT);
				}
          	uitoast.setText(message);
          	uitoast.show();
          }
      });
      
  }

  public static void toastOnUiThread(Activity activity, final int resId) {
      toastOnUiThread(activity, activity.getString(resId));
  }
	public static void showShortToast(Context ctx, String str) {
		Toast toast = Toast.makeText(ctx, str, Toast.LENGTH_SHORT);
		toast.show();
	}

	public static void showShortToast(Context ctx, int id) {
		Toast toast = Toast.makeText(ctx, id, Toast.LENGTH_SHORT);
		toast.show();
	}

	public static void showLongToast(Context ctx, String str) {
		Toast toast = Toast.makeText(ctx, str, Toast.LENGTH_LONG);
		toast.show();
	}

	public static void showLongToast(Context ctx, int id) {
		Toast toast = Toast.makeText(ctx, id, Toast.LENGTH_LONG);
		toast.show();
	}
	
	private static Toast toast;
	public static void toastShowNoRepeated(Context context, int stringId) {
		toastShowNoRepeated(context, context.getString(stringId));
	}
	
	public static void toastShowNoRepeated(Context context, String string) {
		if (toast == null) {
			toast = Toast.makeText(context, string, Toast.LENGTH_SHORT);
		} else {
			toast.setText(string);
			toast.setDuration(Toast.LENGTH_SHORT);
		}
		toast.show();
	}
	
	public static void toastCenter(Context ctx, int stringId, int drawableId)
	{
	    View view = LayoutInflater.from(ctx).inflate(R.layout.messaging_view_004_ptt_tip, null);
        TextView tv = (TextView) view.findViewById(R.id.tv_msg);
        tv.setText(stringId);
        ImageView IV = (ImageView) view.findViewById(R.id.iv_image);
        IV.setImageResource(drawableId);
        Toast t = Toast.makeText(ctx, stringId, Toast.LENGTH_LONG);
        t.setView(view);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();
	}
	
	public static void toastCenterNoDirection(Context context, int stringId, int drawableId, int duration) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.main_toast_one, null);
        TextView tv = (TextView) view.findViewById(R.id.tv_msg);
        tv.setText(stringId);
        ImageView IV = (ImageView) view.findViewById(R.id.iv_image);
        IV.setImageResource(drawableId);
		Toast t = Toast.makeText(context, stringId, duration);
		t.setView(view);
		t.setGravity(Gravity.CENTER, 0, 0);
		t.show();
	}
	
	public static void toastCenterSendFailedWithDirection(Context context, int duration) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.main_toast_two, null);
		Toast t = Toast.makeText(context, null, duration);
		t.setView(view);
		t.setGravity(Gravity.CENTER, 0, 0);
		t.show();
	}
	
	public static void toastCenterWithTextDirection(Context context, int stringId, int duration) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.main_toast_three, null);
        TextView tv = (TextView) view.findViewById(R.id.tv_msg);
        tv.setText(stringId);
		Toast t = Toast.makeText(context, stringId, duration);
		t.setView(view);
		t.setGravity(Gravity.CENTER, 0, 0);
		t.show();
	}
	
	public static void toastCenterWithTextDirectionNoRepeated(Context context, int stringId, int duration) {
		toastCenterWithTextDirectionNoRepeated(context,context.getString(stringId),duration);
	}
	
	public static void toastCenterWithTextDirectionNoRepeated(Context context, String string, int duration) {
		if (null == mCenterToast ) {
        	mCenterToast = Toast.makeText(context, string, duration);
        	mCenterToast.setGravity(Gravity.CENTER, 0, 0);
        	View view = LayoutInflater.from(context).inflate(
    				R.layout.main_toast_three, null);
        	mCenterToast.setView(view);
		} 
		
        TextView tv = (TextView) mCenterToast.getView().findViewById(R.id.tv_msg);
        tv.setText(string);
        mCenterToast.setDuration(duration);
        mCenterToast.show();
	}
	
	public static void toastCenterWhileSending(Context context, int stringId, int duration) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.main_toast_one, null);
		
		int bck = R.drawable.toast_sending_06;
		ImageView infoOperatingIV = (ImageView) view.findViewById(R.id.iv_image);
		infoOperatingIV.setBackgroundResource(bck);
        TextView tv = (TextView) view.findViewById(R.id.tv_msg);
        tv.setText(stringId);
        
		Animation operatingAnim = AnimationUtils.loadAnimation(
				context, R.anim.base_anim_toast_while_sending);
		LinearInterpolator lin = new LinearInterpolator();
		
		if (operatingAnim != null) {
			operatingAnim.setInterpolator(lin);
			infoOperatingIV.startAnimation(operatingAnim);
			Toast t = Toast.makeText(context, null, duration);
			t.setView(view);
			t.setGravity(Gravity.CENTER, 0, 0);
			t.show();
		}
	}
	/**
	 * 
	 * @param context
	 * @param stringId
	 * @param duration
	 */
	public static void toastWhileLoading(Context context, int stringId, int duration) {

		// TODO Auto-generated method stub
		mContext = context;
		int bck = R.layout.main_selector_toast_dialog_circle;
		mView = LayoutInflater.from(context).inflate(
				R.layout.main_toast_one, null);
		ImageView IV = (ImageView) mView.findViewById(R.id.iv_image);
		IV.setBackgroundResource(bck);
        TextView tv = (TextView) mView.findViewById(R.id.tv_msg);
        tv.setText(stringId);
		AnimationDrawable animation = (AnimationDrawable) IV.getBackground();
		animation.stop();
		animation.start();
		mToast = Toast.makeText(context, null, Toast.LENGTH_LONG);
		mToast.setView(mView);
		mToast.setGravity(Gravity.CENTER, 0, 0);
		mToast.show();
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				int bck = R.drawable.toast_sending_06;
				ImageView infoOperatingIV = (ImageView) mView.findViewById(R.id.iv_image);
				infoOperatingIV.setBackgroundResource(bck);
				Animation operatingAnim = AnimationUtils.loadAnimation(
						mContext, R.anim.base_anim_toast_while_sending);
				LinearInterpolator lin = new LinearInterpolator();
				if (operatingAnim != null) {
					operatingAnim.setInterpolator(lin);
					infoOperatingIV.startAnimation(operatingAnim);
					mToast.setView(mView);
				}
			}
		}, 1000);
	
	}
}
