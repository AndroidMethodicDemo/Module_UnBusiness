package base.widget.listview;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.example.demoxswidget.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import base.util.ui.XSDisplayUtil;

public class ADA_Pinned extends BaseExpandableListAdapter{
	
	private LinkedHashMap<String, ArrayList<String>> mMap;
	private ArrayList<String> group;
	private Activity mContext;
	
	public ADA_Pinned(Activity mContext,
			LinkedHashMap<String, ArrayList<String>> mMaps) {
		this.mMap=mMaps;
		this.mContext=mContext;
	}

	public void setDatas(LinkedHashMap<String, ArrayList<String>> map){
		this.mMap=map;
		if(this.mMap!=null){
			group=new ArrayList<String>();
			for(String key:mMap.keySet()){
				group.add(key);
			}
		}
	}

	@Override
	public int getGroupCount() {
		return mMap!=null? mMap.keySet().size():0;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mMap!=null?mMap.get(getGroup(groupPosition)).size():0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return group.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return mMap.get(getGroup(groupPosition)).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	// 渲染父View
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if(convertView==null){
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView=inflater.inflate(R.layout.contact_item_007_group, null);
		}
		TextView text=(TextView) convertView.findViewById(R.id.text);
		text.setText((CharSequence) getGroup(groupPosition));
		return convertView;
	}

	// 渲染子View
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if(convertView==null){
			TextView textView = new TextView(mContext);
			int dip14 = XSDisplayUtil.dip2px(mContext, 14);
			textView.setPadding(dip14, dip14, dip14, dip14);
			convertView=textView;
		}
		((TextView)convertView).setText((CharSequence) getChild(groupPosition, childPosition));
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
