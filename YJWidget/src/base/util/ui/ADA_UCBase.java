package base.util.ui;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class ADA_UCBase<T> extends BaseAdapter {
	private boolean flag;
	private List<T> dataList;
	private Context context;
	
	public ADA_UCBase(Context context,List<T> dataList){
		this.context=context;
		this.dataList=dataList;
	}
	/**
	 * 设置数据集合
	 */
	public void refreshDataList(List<T> dataList){
		this.dataList=dataList;
	}
	@Override
	public int getCount() {
		return dataList==null?0:dataList.size();
	}

	@Override
	public T getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderViewBase holder = null;
		if(null == convertView){
			convertView = LayoutInflater.from(this.context).inflate(getLayoutId(), null);
			holder = initHolder(convertView);
			if(flag){
				holder=initHolder(position, convertView);
			}
			convertView.setTag(holder);
			
		}else{
			holder = (HolderViewBase) convertView.getTag();
		}
		
		initItemViewDatas(position, convertView, parent, holder);
		
		return convertView;
	}
	protected void setFlag(boolean flag) {
		this.flag=flag;
	}
	protected abstract HolderViewBase initHolder(View view);
	protected HolderViewBase initHolder(int position,View view){return null;}
	
	protected abstract void initItemViewDatas(int position, View convertView, ViewGroup parent, HolderViewBase holder);
	
	protected abstract int getLayoutId();
	
//	protected abstract Context getContext();
	
	public class HolderViewBase{}
}
