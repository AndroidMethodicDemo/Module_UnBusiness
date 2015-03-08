package base.util.sys;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class XSNetworkUtil {
	
	public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo info = cm.getActiveNetworkInfo();
            return (info != null && info.isConnected());
        }
        return false;
    }
	
	public static boolean isWiFiActive(Context context) {               
		WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo(); 
		int ipAddress = wifiInfo == null ? 0 : wifiInfo.getIpAddress();  
		return wifiManager.isWifiEnabled() && ipAddress != 0;
	}
	
	public static boolean isWifiNetWork(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean isWifi = false;
        if (cm != null) {
            NetworkInfo info = cm.getActiveNetworkInfo();
            if(null != info) {
            	isWifi = info.isConnected() 
            			&& info.getType()==ConnectivityManager.TYPE_WIFI;
            }
        }
        return isWifi;
	}
	
	/**
	 * WIFI是否使用网络
	 * @return 
	 */
	public static boolean isWifiUseNetwork(Context context){
//		return isNetworkAvailable(context) && isWiFiActive(context);
		return isWifiNetWork(context);
	}
}
