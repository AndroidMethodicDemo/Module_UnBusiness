package base.widget.listview.view;

import android.widget.BaseAdapter;

/**
 * @author andong
 * RefreshListViewˢ���¼��ļ���
 */
public interface LoadDataCallback {

	/**
	 * �����������
	 */
	public BaseAdapter loadNewData();
	
	/**
	 * ������ǰ�����
	 */
	public BaseAdapter loadOldData();
}
