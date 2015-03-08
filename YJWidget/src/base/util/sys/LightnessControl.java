package base.util.sys;

import android.app.Activity;
import android.content.ContentResolver;
import android.provider.Settings;
import android.provider.Settings.System;
import android.view.WindowManager;
import android.widget.Toast;
/**
 * 这是一个单独可工作的类，主要用于调节屏幕亮度，有部分注释了，解释几个概念，不标准，供参考：
　　ContentResolver类，为访问其它应用程序的对外共享数据提供方法，如上面获取、设置亮度时使用的System.getInt()，System.setInt()。
　　Activity类，android程序的主要类，一个界面必有此类提供后台支持，需要继承此类。
　　Settings类，android程序系统相关的设置类，各种设置都可在此找。
　　LayoutParams类，android界面相关的参数，如高度、宽度、亮度等。
　　Toast类，一个可自动消失的提示框，轻型控件。
 */
public class LightnessControl {
    // 判断是否开启了自动亮度调节 
    public static boolean isAutoBrightness(Activity act) { 
        boolean automicBrightness = false; 
        ContentResolver aContentResolver = act.getContentResolver();
        try { 
            automicBrightness = Settings.System.getInt(aContentResolver, 
                   Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC; 
        } catch (Exception e) { 
            Toast.makeText(act,"无法获取亮度",Toast.LENGTH_SHORT).show();
        } 
        return automicBrightness; 
    }     
    // 改变亮度
    public static void SetLightness(Activity act,int value)
    {        
        try {
            System.putInt(act.getContentResolver(),System.SCREEN_BRIGHTNESS,value); 
            WindowManager.LayoutParams lp = act.getWindow().getAttributes(); 
            lp.screenBrightness = (value<=0?1:value) / 255f;
            act.getWindow().setAttributes(lp);
        } catch (Exception e) {
            Toast.makeText(act,"无法改变亮度",Toast.LENGTH_SHORT).show();
        }        
    }
    // 获取亮度
    public static int GetLightness(Activity act)
    {
        return System.getInt(act.getContentResolver(),System.SCREEN_BRIGHTNESS,-1);
    }
    // 停止自动亮度调节 
    public static void stopAutoBrightness(Activity activity) { 
        Settings.System.putInt(activity.getContentResolver(), 
                Settings.System.SCREEN_BRIGHTNESS_MODE, 
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL); 
    }
    // 开启亮度自动调节 
    public static void startAutoBrightness(Activity activity) { 
        Settings.System.putInt(activity.getContentResolver(), 
                Settings.System.SCREEN_BRIGHTNESS_MODE, 
                Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC); 
    } 
}
