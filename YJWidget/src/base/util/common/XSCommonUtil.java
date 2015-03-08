package base.util.common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import com.example.demoxswidget.R;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class XSCommonUtil {
	private static final String TAG = XSCommonUtil.class.getSimpleName();
	private static NetworkInfo networkinfo;
	
	private static final String HUAWEI_P6_MODEL = "HUAWEI P6-U06";
	
	/**
	 * 最大屏幕亮度.
	 */
	private static final int SCREENBRIGHTNESS_MAX = 255;
	
	/**
	 * 最小屏幕亮度.
	 */
	private static final int SCREENBRIGHTNESS_MIN =80;
	
    /**
     * 表示1M有1024KB
     */
    private static final long SIZE_LIMIT = 1024;
	
	public static final String VERSION = "apk_version";
	
	public static String mSecretaryNumber;

    public static void saveFileFunc(Context context, String mFilePath) throws IOException{
    	String sdcard = Environment.getExternalStorageState();
    	String targetFileName;
		boolean isHavingSD = Environment.MEDIA_MOUNTED.equals(sdcard);
		Log.d(TAG, "sdcard = " + sdcard + " isHavingSD = " + isHavingSD);
		if (isHavingSD) {
			File sdCardDir = Environment.getExternalStorageDirectory();
			String dir =  sdCardDir.getCanonicalPath() + context.getString(R.string.im_image_save_path);
			File oldFile = new File(mFilePath);
			if (mFilePath.contains(File.separator)) {
				int lastIndex = mFilePath.lastIndexOf(File.separator);
				targetFileName =  mFilePath.substring(lastIndex);
			} else {
				targetFileName =  mFilePath;
			}
			File fileDir = new File(dir);
			if (!fileDir.exists()) {
				fileDir.mkdir();
			}
			String targetName = FileUtils.existThenRenameFile(fileDir + targetFileName);
			File targetFile = new File(targetName);
			Log.d(TAG, "onClick_save dir = " + dir + 
					",onClick_save mFilePath = " + mFilePath);
			Toast.makeText(context, context.getString(R.string.str_base_action_save_to) + dir, Toast.LENGTH_SHORT).show();
			FileUtils.copyfile(oldFile, targetFile, true);
			//即时扫描媒体库更新
			Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE); 
			Uri uri = Uri.fromFile(targetFile); 
			intent.setData(uri); 
			context.sendBroadcast(intent); 
		} else {
			Toast.makeText(context, context.getResources().getString(R.string.str_base_show_no_sd_card), Toast.LENGTH_SHORT).show();
		}
    }
	
	/**
	 * 版本号的比较
	 * @param oldVersion
	 * @param newVersion
	 * @return true 表示 newVersion 高于 oldVersion 
	 */
	public static boolean compareVersion(String oldVersion, String newVersion) {
		Log.i(TAG, "oldVersion = " + oldVersion + ", newVersion = "
				+ newVersion);

		if (null == oldVersion || oldVersion.length() <= 0
				|| null == newVersion || newVersion.length() <= 0) {
			return false;
		}
		oldVersion = oldVersion.replace("V", "");
		newVersion = newVersion.replace("V", "");
		
		if (!checkVersionStyle(oldVersion) || !checkVersionStyle(newVersion)) {
			return false;
		}
		oldVersion = oldVersion.substring(0, oldVersion.indexOf('-'));
		newVersion = newVersion.substring(0, newVersion.indexOf('-'));

		String[] oldVersionArray = oldVersion.split("\\.");
		String[] newVersionArray = newVersion.split("\\.");

		int count = oldVersionArray.length;

		for (int i = 0; i < count; i++) {
			if (Integer.valueOf(newVersionArray[i]) > Integer
					.valueOf(oldVersionArray[i])) {
				return true;
			} else if (Integer.valueOf(newVersionArray[i]) < Integer
					.valueOf(oldVersionArray[i])) {
				return false;
			}
		}
		return false;
	}

	private static boolean checkVersionStyle(String version) {
		if (null == version) {
			return false;
		}
		boolean result = false;
		String TIPS_VERSION_REGULAR_EXPRESSION = "\\d{1}[.]\\d{1,2}[.]\\d{1,2}[.]\\d{1,5}[-]\\d{8}";
		try {
			result = version.matches(TIPS_VERSION_REGULAR_EXPRESSION);

		} catch (PatternSyntaxException e) {
			Log.e(TAG,
					"checkVersionStyle() failed.PatternSyntaxException error msg : "
							+ e.getMessage(), e);
		} catch (NullPointerException e) {
			Log.e(TAG,
					"checkVersionStyle() failed.NullPointerException error msg : "
							+ e.getMessage(), e);
		}
		return result;
	}
	
		/**
	 * 仅过滤出 数字, '*','+','#' 字符 其他的都摒弃掉.
	 * @param source
	 * @return 处理过后的字符串.
	 * added by yangjinbao 2013-07-16.
	 */
	public static CharSequence filterSpecialCharacters(CharSequence source) {
		//StringBuilder在单线程模式下性能比StringBuffer高, 因为它是非线程安全的.
		
		StringBuilder  result =new StringBuilder(); 
		if(source!=null){
			int  sourceLength = source.length();
			char  sourceItem;
			String sourceItemStr;
			for(int i =0; i<sourceLength; i++){
				sourceItem = source.charAt(i);
				sourceItemStr = String.valueOf(sourceItem);
				if(sourceItemStr.matches("[0-9]") ||
						sourceItemStr.equals("*") ||
						sourceItemStr.equals("+") ||
						sourceItemStr.equals("#") ){
					result = result.append(sourceItemStr);
				}
			}
			return result;
		}else{
		  return  "";
		}
		
	}
	
	public static boolean isEmail(String email) {
		if(null == email) {
			return false;
		}
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}
	
	public static boolean isHuaweiP6(){
		return HUAWEI_P6_MODEL.equals(Build.MODEL);
	}
	
   public static StringBuilder conversionFileSize(long originalFileSize) {
		StringBuilder finalFileSize = new StringBuilder();

        long videoSize = originalFileSize / 1024;
        
        if (videoSize > SIZE_LIMIT) {
            String integerPart = Long.toString(videoSize / 1024);
            String reminderPart = Long.toString(videoSize % 1024);
            int reminderPartLength = reminderPart.length();
            // 如果结果是小数，则只保留两位小数方便显示
            finalFileSize = finalFileSize
                    .append(integerPart)
                    .append(".")
                    .append(reminderPartLength > 1 ? reminderPart.subSequence(
                            0, 1) : reminderPart);
            return finalFileSize.append(" MB");
        }
        else if (originalFileSize < 1024)
        {
            return finalFileSize.append(originalFileSize).append(" B");
        }
        else
        {
            return finalFileSize.append(videoSize).append(" KB");
        }
    }
	
}
