package base.widget.listview;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.TextView;
import base.ACT_Base;
import base.widget.listview.view.IPinnedHeaderExpLvCallback;
import base.widget.listview.view.XSWPinnedHeaderExpListView;

import com.example.demoxswidget.R;

public class ACT_PinnedHeaderExpListView extends ACT_Base {

	View mPinnedHeaderView, mPinnedHeaderTouchView;
	XSWPinnedHeaderExpListView mListView;
	ADA_Pinned mAdapter;
	private LinkedHashMap<String, ArrayList<String>> mMaps;

	@Override
	protected void initViews() {
		setContentView(R.layout.act_pinned);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		// listView
		mListView = (XSWPinnedHeaderExpListView) findViewById(R.id.exListView);
		mPinnedHeaderTouchView = findViewById(R.id.rl_pinned_header_touch_view);
		mPinnedHeaderTouchView.setClickable(true);

		mPinnedHeaderView = inflater.inflate(R.layout.contact_item_007_group,
				mListView, false);

		mAdapter = new ADA_Pinned(mContext, mMaps);
		mListView.setAdapter(mAdapter);

		mListView.setPinnedHeaderView(mPinnedHeaderView);
		mListView.setPinnedHeaderTouchView(mPinnedHeaderTouchView);
		mListView.setIPinnedHeaderExpListView(callback);
	}

	/**
	 * 这个是什么东西呢？
	 */
	IPinnedHeaderExpLvCallback callback = new IPinnedHeaderExpLvCallback() {

		@Override
		public void configurePinnedHeader(View header, int position) {

			TextView tv = (TextView) header.findViewById(R.id.text);

			tv.setText((String) mAdapter.getGroup(position));

			boolean isExpanded = mListView.isGroupExpanded(position);
			Drawable icon = isExpanded ? mContext.getResources().getDrawable(
					R.drawable.contact_002_arrowhead_02) : mContext
					.getResources().getDrawable(
							R.drawable.contact_002_arrowhead_01);
			icon.setBounds(0, 0, icon.getMinimumWidth(),
					icon.getMinimumHeight());
			tv.setCompoundDrawables(icon, null, null, null);

		}
	};

	@Override
	protected void bindEvents() {

		mListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				mListView.configureHeaderView(firstVisibleItem);
			}
		});

		mListView
				.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

					@Override
					public boolean onGroupClick(ExpandableListView parent,
							View v, int groupPosition, long id) {
						return false;
					}
				});

		mListView
				.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

					@Override
					public boolean onChildClick(ExpandableListView parent,
							View v, int groupPosition, int childPosition,
							long id) {

						String childContact = (String) mAdapter.getChild(
								groupPosition, childPosition);
						if (null != childContact) {
							// UTIL_UCPageJump.startMessagingChatActivity(parent.getContext(),
							// childContact.uri);
						}

						return false;
					}
				});

		mListView
				.setOnItemLongClickListener(new ExpandableListView.OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View view, int position, long id) {
						if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
							int groupPosition = ExpandableListView
									.getPackedPositionGroup(id);
							int childPosition = ExpandableListView
									.getPackedPositionChild(id);

							String ucContact = (String) mAdapter.getChild(
									groupPosition, childPosition);
							// mAdapter.showAlerdialog(getActivity(), new
							// CallEntity(ucContact.uri, true,
							// CallSession.TYPE_AUDIO));
							return true;
						}

						return false;
					}
				});
	}

	@Override
	protected void initDatas() {
		// 模拟数据
		mMaps = new LinkedHashMap<String, ArrayList<String>>();
		for (int i = 0; i < 5; i++) {
			ArrayList<String> list = new ArrayList<String>();
			for (int j = 0; j < 5; j++) {
				list.add("孩子：" + j + "");
			}
			mMaps.put("组：" + i + "", list);
		}
		mAdapter.setDatas(mMaps);
		mAdapter.notifyDataSetChanged();
		// 此处并非实际重设Adapter
		mListView.resetAdapter(mAdapter);
	}

	public void setDatas(LinkedHashMap<String, ArrayList<String>> maps) {
		if (null == mAdapter)
			return;

		mAdapter.setDatas(maps);
		mAdapter.notifyDataSetChanged();
		// 此处并非实际重设Adapter
		mListView.resetAdapter(mAdapter);
	}

}
