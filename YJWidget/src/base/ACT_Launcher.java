package base;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import base.manager.ACT_Manager;
import base.widget.dialog.ACT_Dialog;
import base.widget.image.ACT_ImageView;
import base.widget.itemview.ACT_ItemView;
import base.widget.listview.ACT_ListView;
import base.widget.other.ACT_SearchView;
import base.widget.popupwindow.ACT_PopupWindow;
import base.widget.scrollview.ACT_HoverBar;
import base.widget.scrollview.ACT_HoverView;

import com.example.demoxswidget.R;

public class ACT_Launcher extends ACT_Base{
	// 核心控件
	private ListView listView;
	//
	private String[] activityNames;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		
	}

	@Override
	protected void initViews() {
		setContentView(R.layout.act_launcher);
		listView=(ListView) findViewById(R.id.listView);
	}

	@Override
	protected void bindEvents() {
		listView.setOnItemClickListener(new OnItemClick());
	}

	@Override
	protected void initDatas() {
		activityNames=new String[]{"PopupWindow","Dialog","SearchView","ImageView","ListView","ItemView","Manager","HoverBar","HoverView"};
		listView.setAdapter(new MyAdapter());
	}
	
	class OnItemClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = null;
			switch (position) {
			case 0:
				intent=new Intent(mContext,ACT_PopupWindow.class);
				break;
			case 1:
				intent=new Intent(mContext,ACT_Dialog.class);
				break;
			case 2:
				intent=new Intent(mContext,ACT_SearchView.class);
				break;
			case 3:
				intent=new Intent(mContext,ACT_ImageView.class);
				break;
			case 4:
				intent=new Intent(mContext,ACT_ListView.class);
				break;
			case 5:
				intent=new Intent(mContext,ACT_ItemView.class);
			case 6:
				intent=new Intent(mContext,ACT_Manager.class);
				break;
			case 7:
				intent=new Intent(mContext,ACT_HoverBar.class);
				break;
			case 8:
				intent=new Intent(mContext,ACT_HoverView.class);
				break;
			}
			startActivity(intent);
		}
		
	}
	
	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return activityNames.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View content = View.inflate(mContext, R.layout.item_launcher, null);
			TextView textView = (TextView) content.findViewById(R.id.tvTitle);
			textView.setText(activityNames[position]);
			return content;
		}
		
	}

}
