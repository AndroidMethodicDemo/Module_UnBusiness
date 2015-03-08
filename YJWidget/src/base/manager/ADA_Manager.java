package base.manager;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import base.util.ui.ADA_UCBase;

import com.example.demoxswidget.R;

public class ADA_Manager extends ADA_UCBase<String> {

	public ADA_Manager(Context context, List<String> dataList) {
		super(context, dataList);
	}

	@Override
	protected HolderViewBase initHolder(View view) {
		ManagerHolder managerHolder = new ManagerHolder();
		managerHolder.tvItem=(TextView) view.findViewById(R.id.tvItem);
		return managerHolder;
	}

	@Override
	protected void initItemViewDatas(int position, View convertView,
			ViewGroup parent, HolderViewBase holder) {
		ManagerHolder managerHolder=(ManagerHolder) holder;
		managerHolder.tvItem.setText(getItem(position));
	}

	@Override
	protected int getLayoutId() {
		return R.layout.item_base;
	}
	
	class ManagerHolder extends HolderViewBase{
		TextView tvItem;
	}

}
