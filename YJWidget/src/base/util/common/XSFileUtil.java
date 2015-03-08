package base.util.common;

import java.io.File;

import base.util.sys.XSSysUtil;

import android.content.Context;
import android.util.Log;

public class XSFileUtil {
	
	private static final String TAG = "XSFileUtil";
	
	/**
     * 根据给出的路径看图片是否存在
     * @param path
     * @return
     */
	public static boolean fileIsExists(String path) {
		File f = new File(path);
		if (!f.exists()) {
			return false;
		}
		return true;
	}
	
	/**
	 * Get file's directory that store data.
	 * <p>
	 * e.g. mnt/sdcard/com.huawei.rcs/Files or data/data/com.huawei.rcs/Files.
	 * 
	 * @param ctx
	 * @return
	 */
	public static String getFilesStoreDir(Context ctx) {
		String filedir = "";
		if (XSSysUtil.existSDCard(ctx)) {
			filedir = android.os.Environment.getExternalStorageDirectory()
					+ "/" + ctx.getPackageName();
		} else {
			filedir = "data/data/" + ctx.getPackageName();
		}

		File fFile = new File(filedir);
		if (!fFile.exists()) {
			boolean success = fFile.mkdirs();
			if(!success){
				Log.e(TAG, "getFilesStoreDir()-->first mkdirs fail!");
			}
		}

		filedir += "/Files";
		fFile = new File(filedir);
		if (!fFile.exists()) {
			boolean success = fFile.mkdirs();
			if(!success){
				Log.e(TAG, "getFilesStoreDir()-->second mkdirs fail!");
			}
		}
		return filedir;
	}
	
}
