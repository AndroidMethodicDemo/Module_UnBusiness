package base.widget.listview;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import base.widget.listview.view.RefreshableView;
import base.widget.listview.view.RefreshableView.PullToRefreshListener;

import com.example.demoxswidget.R;
/**
 * 可以看到，我们通过调用RefreshableView的setOnRefreshListener方法注册了一个监听器，
 * 当ListView正在刷新时就会回调监听器的onRefresh方法，刷新的具体逻辑就在这里处理。
 * 而且这个方法已经自动开启了线程，可以直接在onRefresh方法中进行耗时操作，
 * 比如向服务器请求最新数据等，在这里我就简单让线程睡眠3秒钟。另外在onRefresh方法的最后，
 * 一定要调用RefreshableView中的finishRefreshing方法，这个方法是用来通知RefreshableView刷新结束了，
 * 不然我们的ListView将一直处于正在刷新的状态。
不知道大家有没有注意到，setOnRefreshListener这个方法其实是有两个参数的，
我们刚刚也是传入了一个不起眼的0。那这第二个参数是用来做什么的呢？由于RefreshableView比较智能，
它会自动帮我们记录上次刷新完成的时间，然后下拉的时候会在下拉头中显示距上次刷新已过了多久。
这是一个非常好用的功能，让我们不用再自己手动去记录和计算时间了，但是却存在一个问题。
如果当前我们的项目中有三个地方都使用到了下拉刷新的功能，现在在一处进行了刷新，
其它两处的时间也都会跟着改变！因为刷新完成的时间是记录在配置文件中的，
由于在一处刷新更改了配置文件，导致在其它两处读取到的配置文件时间已经是更改过的了。
那解决方案是什么？就是每个用到下拉刷新的地方，给setOnRefreshListener方法的第二个参数
中传入不同的id就行了。这样各处的上次刷新完成时间都是单独记录的，相互之间就不会再有影响。
效果看起来还是非常不错的。我们最后再来总结一下，在项目中引入ListView下拉刷新功能只需三步：

1. 在Activity的布局文件中加入自定义的RefreshableView，并让ListView包含在其中。
2. 在Activity中调用RefreshableView的setOnRefreshListener方法注册回调接口。
3. 在onRefresh方法的最后，记得调用RefreshableView的finishRefreshing方法，通知刷新结束。

从此以后，在项目的任何地方，一分钟引入下拉刷新功能妥妥的。
 */
public class ACT_RefreshableView extends Activity {

	RefreshableView refreshableView;
	ListView listView;
	ArrayAdapter<String> adapter;
	String[] items = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_refreshable_view);
		refreshableView = (RefreshableView) findViewById(R.id.refreshable_view);
		listView = (ListView) findViewById(R.id.list_view);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
		listView.setAdapter(adapter);
		refreshableView.setOnRefreshListener(new PullToRefreshListener() {
			@Override
			public void onRefresh() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				refreshableView.finishRefreshing();
			}
		}, 0);
	}

}
