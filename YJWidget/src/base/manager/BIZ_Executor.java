package base.manager;

import java.io.File;
import java.util.concurrent.ThreadPoolExecutor;

import android.os.AsyncTask;
import android.widget.TextView;
import base.util.common.YJHttpUtil;

import com.lidroid.xutils.http.callback.RequestCallBack;

public class BIZ_Executor {

	public static void loadWebPage(final String url,final TextView tv,ThreadPoolExecutor pool){
		new AsyncTask<Void, Void, String>() {

			protected void onPreExecute() {
				
			}
			@Override
			protected String doInBackground(Void... params) {
				return YJHttpUtil.sendGet(null,url, "");
			}
			protected void onPostExecute(String result) {
				tv.setText(result);
			}
			
			
		}.executeOnExecutor(pool);
	}
	
	public static void downloadApk(final String url,final String target,final RequestCallBack<File> callback,ThreadPoolExecutor pool){
		new AsyncTask<Void, Void, Void>() {

			protected void onPreExecute() {
				
			}
			@Override
			protected Void doInBackground(Void... params) {
				
				
				YJHttpUtil.download(url, target, callback);
				return null;
			}
			protected void onPostExecute(Void result) {
			}
			
			
		}.executeOnExecutor(pool);
	}
	public static void loadWebPage2(final String url,final TextView tv){
		new AsyncTask<Void, Void, String>() {
			
			protected void onPreExecute() {
				
			}
			@Override
			protected String doInBackground(Void... params) {
				return YJHttpUtil.sendGet(null,url, "");
			}
			protected void onPostExecute(String result) {
				tv.setText(result);
			}
			
			
		}.execute();
	}
	
	public static void downloadApk2(final String url,final String target,final RequestCallBack<File> callback){
		new AsyncTask<Void, Void, Void>() {
			
			protected void onPreExecute() {
				
			}
			@Override
			protected Void doInBackground(Void... params) {
				
				
				YJHttpUtil.download(url, target, callback);
				return null;
			}
			protected void onPostExecute(Void result) {
			}
			
			
		}.execute();
	}
}
