package base.manager;

import java.util.concurrent.ThreadPoolExecutor;

import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import base.ACT_Base;

import com.example.demoxswidget.R;

public class ACT_ExecutorManager extends ACT_Base{

	private TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv11,tv12,tv13,tv14,tv15,tv16,tv9,tv10,button1,button2,button3;
	final String target=Environment.getExternalStorageDirectory() +"/huatongdianhua_1703.apk";
	final String apkUrl="http://gdown.baidu.com/data/wisegame/1d5afcf91ecec5e3/huatongdianhua_1703.apk";
	ThreadPoolExecutor downloadPool = ExecutorManager.getDownloadPool();
	ThreadPoolExecutor shortPool = ExecutorManager.getShortPool();
	
	@Override
	protected void initViews() {
		setContentView(R.layout.act_threadmanager);
		button1=(TextView) findViewById(R.id.button1);
		button2=(TextView) findViewById(R.id.button2);
		button3=(TextView) findViewById(R.id.button3);
		tv1=(TextView) findViewById(R.id.tv1);
		tv2=(TextView) findViewById(R.id.tv2);
		tv3=(TextView) findViewById(R.id.tv3);
		tv4=(TextView) findViewById(R.id.tv4);
		tv5=(TextView) findViewById(R.id.tv5);
		tv6=(TextView) findViewById(R.id.tv6);
		tv7=(TextView) findViewById(R.id.tv7);
		tv8=(TextView) findViewById(R.id.tv8);
		tv9=(TextView) findViewById(R.id.tv9);
		tv10=(TextView) findViewById(R.id.tv10);
		tv11=(TextView) findViewById(R.id.tv11);
		tv12=(TextView) findViewById(R.id.tv12);
		tv13=(TextView) findViewById(R.id.tv13);
		tv14=(TextView) findViewById(R.id.tv14);
		tv15=(TextView) findViewById(R.id.tv15);
		tv16=(TextView) findViewById(R.id.tv16);
//		tv17=(TextView) findViewById(R.id.tv17);
//		tv18=(TextView) findViewById(R.id.tv18);
	}

	@Override
	protected void bindEvents() {
		MyOnClick myOnClick = new MyOnClick();
		button1.setOnClickListener(myOnClick);
		button2.setOnClickListener(myOnClick);
		button3.setOnClickListener(myOnClick);
	}
	class MyOnClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.button1:
				loadData1();
				break;

			case R.id.button2:
				
				loadData2();
				break;
			case R.id.button3:
				tv1.setText("");
				tv2.setText("");
				tv3.setText("");
				tv4.setText("");
				tv5.setText("");
				tv6.setText("");
				tv7.setText("");
				tv8.setText("");
				tv9.setText("");
				tv10.setText("");
				tv11.setText("");
				tv12.setText("");
				tv13.setText("");
				tv14.setText("");
				tv15.setText("");
				tv16.setText("");
//				tv17.setText("");
//				tv18.setText("");
				break;
			}
		}
		
	}
	
	@Override
	protected void initDatas() {
		
	}
	private void loadData1(){

//		BIZ_Executor.downloadApk(apkUrl,target,callback,downloadPool);
		BIZ_Executor.loadWebPage("http://www.baidu.com",tv1,shortPool);
		BIZ_Executor.loadWebPage("http://www.sina.com.cn/",tv2,shortPool);
		BIZ_Executor.loadWebPage("http://finance.sina.com.cn/",tv3,shortPool);
		BIZ_Executor.loadWebPage("http://sports.sina.com.cn/",tv4,shortPool);
		BIZ_Executor.loadWebPage("http://news.sina.com.cn/",tv5,shortPool);
		BIZ_Executor.loadWebPage("http://mail.sina.com.cn/",tv6,shortPool);
		BIZ_Executor.loadWebPage("http://auto.sina.com.cn/",tv7,shortPool);
		BIZ_Executor.loadWebPage("http://ent.sina.com.cn/",tv8,shortPool);
		BIZ_Executor.loadWebPage("http://blog.sina.com.cn/",tv9,shortPool);
		BIZ_Executor.loadWebPage("http://games.sina.com.cn/",tv10,shortPool);
		
		BIZ_Executor.loadWebPage("http://www.baidu.com",tv11,shortPool);
		BIZ_Executor.loadWebPage("http://www.sohu.com/",tv12,shortPool);
		BIZ_Executor.loadWebPage("http://business.sohu.com/",tv13,shortPool);
		BIZ_Executor.loadWebPage("http://sports.sohu.com/",tv14,shortPool);
		BIZ_Executor.loadWebPage("http://news.sohu.com/",tv15,shortPool);
		BIZ_Executor.loadWebPage("http://mail.sohu.com/",tv16,shortPool);
//		BIZ_Executor.loadWebPage("http://auto.sohu.com/",tv17,shortPool);
//		BIZ_Executor.loadWebPage("http://ent.sohu.com/",tv18,shortPool);
	}
	private void loadData2(){
		
//		BIZ_Executor.downloadApk(apkUrl,target,callback,downloadPool);
		BIZ_Executor.loadWebPage2("http://www.baidu.com",tv1);
		BIZ_Executor.loadWebPage2("http://www.sina.com.cn/",tv2);
		BIZ_Executor.loadWebPage2("http://finance.sina.com.cn/",tv3);
		BIZ_Executor.loadWebPage2("http://sports.sina.com.cn/",tv4);
		BIZ_Executor.loadWebPage2("http://news.sina.com.cn/",tv5);
		BIZ_Executor.loadWebPage2("http://mail.sina.com.cn/",tv6);
		BIZ_Executor.loadWebPage2("http://auto.sina.com.cn/",tv7);
		BIZ_Executor.loadWebPage2("http://ent.sina.com.cn/",tv8);
		BIZ_Executor.loadWebPage2("http://blog.sina.com.cn/",tv9);
		BIZ_Executor.loadWebPage2("http://games.sina.com.cn/",tv10);
		
		BIZ_Executor.loadWebPage2("http://www.baidu.com",tv11);
		BIZ_Executor.loadWebPage2("http://www.sohu.com/",tv12);
		BIZ_Executor.loadWebPage2("http://business.sohu.com/",tv13);
		BIZ_Executor.loadWebPage2("http://sports.sohu.com/",tv14);
		BIZ_Executor.loadWebPage2("http://news.sohu.com/",tv15);
		BIZ_Executor.loadWebPage2("http://mail.sohu.com/",tv16);
//		BIZ_Executor.loadWebPage2("http://auto.sohu.com/",tv17);
//		BIZ_Executor.loadWebPage2("http://ent.sohu.com/",tv18);
	}
	


}
