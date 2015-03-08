package base.util.ui;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class XSDisplayUtil {
	
	private static final String STATUSBAR_CLASS_NAME   = "com.android.internal.R$dimen";
	private static final String STATUSBAR_FIELD_HEIGHT = "status_bar_height";

    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }
    
    /**
     * get the display metrics,the width and height of the screen
     * @param activity
     * @param metrics the display metrics:
     * metrics[0]: the width of the screen
     * metrics[1]: the height of the screen
     */
    @SuppressWarnings("deprecation")
	public static void getDisplayMetrics(Context context, int metrics[]) {
    	if (null == metrics) {
    		metrics = new int[2];
    	}
    	
    	WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
		int screenWidth = windowManager.getDefaultDisplay().getWidth();						
		if (screenHeight > screenWidth) {
			int temp = screenWidth;
			screenWidth = screenHeight;
			screenHeight = temp;
		}		
		metrics[0] = screenWidth;
		metrics[1] = screenHeight;
    }
    
   /**
    * get the height of status bar
    * @param context the context 
    * @return the height of status bar
    */
    public static int getStatusBarHeight(Context context) {
 		Class<?> c = null;
 		Object obj = null;
 		Field field = null;
 		int x = 0;
 		int sbar = 0;
 		try {
 			c = Class.forName(STATUSBAR_CLASS_NAME);
 			obj = c.newInstance();
 			field = c.getField(STATUSBAR_FIELD_HEIGHT);
 			x = Integer.parseInt(field.get(obj).toString());
 			sbar = context.getResources().getDimensionPixelSize(x);
 		} catch (Exception e1) {
 			e1.printStackTrace();
 		}
 		return sbar;
 	}
    
    private static DisplayMetrics getDisplayMetrics(Context context){
		DisplayMetrics dm = new DisplayMetrics();
		// 获取屏幕信息
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm;
	}
	
    /**
     * 获取屏幕宽度
     * @param context
     * @return
     */
	public static int getScreenWidth(Context context){
		return getDisplayMetrics(context).widthPixels;
	}
	
	/**
     * 获取屏幕高度
     * @param context
     * @return
     */
	public static int getScreenHeight(Context context){
		return getDisplayMetrics(context).heightPixels;
	}
}
