package base.util.sys;

import java.util.List;

import base.util.ui.ToastUtils;

import com.example.demoxswidget.R;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;

public class XSSysUtil {
	
	private static final String TAG = XSSysUtil.class.getSimpleName();
	private static WakeLock mWakeLock;

	
	/*
	 * ----------------------------------------------------------------------------------------------
	 * 权限获取
		要进行电源的操作需要在AndroidManifest.xml中声明该应用有设置电源管理的权限。
		<uses-permission android:name="android.permission.WAKE_LOCK" />
		你可能还需要
		<uses-permission android:name="android.permission.DEVICE_POWER" />
		另外WakeLock的设置是 Activiy 级别的，不是针对整个Application应用的。
		可以在activity的onResume方法里面操作WakeLock,  在onPause方法里面释放。
	 */
	/**
	 * 唤醒点亮屏幕
	 * @param context
	 */
	public static void acquierWakeLock(Context context) {
		PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		mWakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK
				| PowerManager.ACQUIRE_CAUSES_WAKEUP, "acquierWakeLock");
		mWakeLock.acquire();
	}
	/**
	 * 恢复屏幕到黑暗
	 */
	public static void releaseWakeLock() {
		if (null != mWakeLock && mWakeLock.isHeld()) {
			mWakeLock.release();
			mWakeLock = null;
		}
	}
	
	public static boolean isCurrentActivity(Context context, Class<?> cls) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> list = am.getRunningTasks(10);    
        String currentActivityName = "";
        if (list.size() >= 1) {
            currentActivityName = list.get(0).topActivity.getClassName();
        }

        return currentActivityName.equals(cls.getName());
    }
	

	/**
	 * 
	 * @param context
	 * @return
	 */
	public static boolean existSDCard(Context context) {
		
		String status = Environment.getExternalStorageState();
		
		if (!status.equals(Environment.MEDIA_MOUNTED)) {
        	return false;
        }
		else {
	       	return true;
        }
	}
	
	/**
	 * 检查是否有SDCard,如果没有则会提示：没有SD卡
	 * @param context
	 * @return
	 */
	public static boolean checkSDCard(Context context){
		String status=Environment.getExternalStorageState();  
		 if(!status.equals(Environment.MEDIA_MOUNTED)){
			 ToastUtils.toastShowNoRepeated(context, R.string.str_base_show_no_sd_card);
        	Log.d(TAG, "checkSDCard() have not sd card");
        	return false;
        }else {
	       	return true;
        }
	}
	
	
	/*
	 * ----------------------------------------------------------------------------------------------------------------------
	 * ScreenBrightness
	 */
	private static final int SCREENBRIGHTNESS_MAX = 255;
	private static final int SCREENBRIGHTNESS_MIN = 80;
	/**
	 * 
	 * @param context
	 * @return
	 */
	public static  int getScreenBrightness(Context context) {
		int screenBrightness = SCREENBRIGHTNESS_MAX;
		
		try {
			screenBrightness = Settings.System.getInt(context.getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS);
		} catch (Exception localException) {
		}
		
		return screenBrightness;
	}
	/**
	 * 
	 * @param context
	 * @param brightness
	 */
	public static void setBrightness(Context context, float brightness) {
		if (context  instanceof Activity) {
			Activity contextActivity= (Activity)context;
			
			WindowManager.LayoutParams lp = contextActivity.getWindow().getAttributes();
			lp.screenBrightness = brightness;
			contextActivity.getWindow().setAttributes(lp);
		}
	}
	/**
	 * @param context
	 * @param currentBrightnessInt
	 */
	public static void changeBrightness(Activity context, int currentBrightnessInt) {
		
		if (currentBrightnessInt < SCREENBRIGHTNESS_MIN) {
			currentBrightnessInt = SCREENBRIGHTNESS_MIN;
		}
		
		Settings.System.putInt(context.getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS, currentBrightnessInt);
		
		currentBrightnessInt = Settings.System.getInt(context.getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS, -1);
		
		WindowManager.LayoutParams wl = context.getWindow().getAttributes();

		float tmpFloat = (float)(currentBrightnessInt / SCREENBRIGHTNESS_MAX);
		if (tmpFloat > 0 && tmpFloat <= 1) {
			wl.screenBrightness = tmpFloat;
		}
		
		context.getWindow().setAttributes(wl);
	}
	
	public static boolean isScreenLocked(Context context) {
		KeyguardManager mKeyguardManager = (KeyguardManager) context
				.getSystemService(Context.KEYGUARD_SERVICE);
		return mKeyguardManager != null
				&& mKeyguardManager.inKeyguardRestrictedInputMode();
	}
	/**
	 * get image file path from an image uri
	 * @param uri the uri
	 * @return the file path
	 */
	public static String getImageFilePathFromUri(Context context, Uri uri) {
		String strUtfPath = null;
		String[] proj = { MediaStore.Images.Media.DATA,
				MediaStore.Images.Media._ID,
				MediaStore.Images.ImageColumns.DATA };

		Cursor cursor = null;
		try {
			cursor = context.getContentResolver().query(uri, proj, null, null, null);

			if (cursor == null || !cursor.moveToFirst()) {
				String temp = uri.toString().replaceFirst("file:///", "/");
				strUtfPath = Uri.decode(temp);
			} else {
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA);
				strUtfPath = cursor.getString(column_index);
			}
		} catch (Exception e) {
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		return strUtfPath;
	}
	
}
