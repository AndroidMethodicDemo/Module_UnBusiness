package base.util.common;

import java.io.File;
import java.io.IOException;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class YJHttpUtil {
	public static String sendPost(RequestParams requestParams,String contextUrl){
		String result="";
		
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configResponseTextCharset("UTF-8");
		try {
			
			ResponseStream response = httpUtils.sendSync(HttpMethod.POST,"http://www.baidu.com"+
					contextUrl, requestParams);
			
			String responseStr = response.readString();
			return responseStr;
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	//"/AppApi/getinfo.json"
	public static String sendGet(RequestParams requestParams,String url,String contextUrl) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configResponseTextCharset("UTF-8");
		try {
			
			ResponseStream response = httpUtils.sendSync(HttpMethod.GET,url+
					contextUrl, requestParams);
			
			String responseStr = response.readString();
			return responseStr;
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void download(String url,String target, RequestCallBack<File> callback){
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.download(url, target, callback);
	}
}
