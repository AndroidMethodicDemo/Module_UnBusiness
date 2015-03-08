package base.manager;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import base.ACT_Base;

import com.example.demoxswidget.R;

public class ACT_Manager extends ACT_Base {

	private ListView listView;
	private List<String> dataList;
	@Override
	protected void initViews() {
		setContentView(R.layout.act_manager);
		listView=(ListView) findViewById(R.id.manager_listview);
		
	}

	@Override
	protected void bindEvents() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent=null;
				switch (position) {
				case 0:
					intent=new Intent(mContext,ACT_ExecutorManager.class);
					break;

				case 1:
					break;
				}
				startActivity(intent);
			}
		});
	}

	@Override
	protected void initDatas() {
		dataList=new ArrayList<String>();
		dataList.add("ExecutorManager");
		ADA_Manager ada_Manager = new ADA_Manager(mContext, dataList);
		listView.setAdapter(ada_Manager);
	}

}
