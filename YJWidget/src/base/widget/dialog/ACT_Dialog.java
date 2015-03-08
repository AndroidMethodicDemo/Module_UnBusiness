package base.widget.dialog;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import base.ACT_Base;
import base.widget.dialog.XSPTimePickerDialog.OnTimeChangedCallback;
import base.widget.itemview.XSPInfoItemView;

import com.example.demoxswidget.R;

public class ACT_Dialog extends ACT_Base{

	private XSPTimePickerDialog timePickerDialog;
	private XSPAlertDialog xspAlertDialog;
	
	private int yearOfStartTime, monthOfStartTime, dayOfStartTime,
	hourOfStartTime, minuteOfStartTime;
//	private long hourOfTimeLength, minuteOfTimeLength;
	private XSPInfoItemView subjectItem, meetingTypeItem, meetingDateItem,
			meetingStartTimeItem, meetingTimeLengthItem, maximumAttendeesItem,
			attendeesItem, schedulerItem;
	/*
	 * 展示item的View及数据
	 */
	private ListView lvActDialog;
	private String[] dialogs={"showAlertDialog0","showAlertDialog1","showAlertDialog1OnWindow",
			"showAlertDialog2","showAlertDialog2OnWindow","showAlertMeetingDialog",
			"XSPAlertDialog1","TimePickerDialog","XSAlertDialogUtil"};

	@Override
	protected void initViews() {
		setContentView(R.layout.act_dialog);
		lvActDialog=(ListView) findViewById(R.id.lvActDialog);
		// TimePicker
		initMeetingStartTimeDialog();
		xspAlertDialog = new XSPAlertDialog(mContext);
	}
	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return dialogs.length;
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
			textView.setText(dialogs[position]);
			return content;
		}
		
	}
	@Override
	protected void bindEvents() {
		lvActDialog.setOnItemClickListener(new MyOnItem());
	}

	class MyOnItem implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			android.view.View.OnClickListener onClick=new MyOnClick();
			switch (position) {
			case 0:
				xspAlertDialog.showAlertDialog0("title", "message", true);
				break;
			case 1:
				xspAlertDialog.showAlertDialog1("title","message","button", onClick, true);
				break;
			case 2:
				xspAlertDialog.showAlertDialog1OnWindow("title", "message", "button", onClick, true);
				break;
			case 3:
				xspAlertDialog.showAlertDialog2("title", "message", "button", onClick, "button", onClick, true);
				break;
			case 4:
				xspAlertDialog.showAlertDialog0("title", "message", true);
				break;
			case 5:
				xspAlertDialog.showAlertDialog0("title", "message", true);
				break;
			case 6:
				xspAlertDialog.showAlertDialog0("title", "message", true);
				break;
			case 7:
				timePickerDialog.show();
				break;
			case 8:
				MyDialogOnClick myOnClick = new MyDialogOnClick();
				XSAlertDialogUtil.show(mContext, R.string.title, R.string.message, R.string.button1, myOnClick, R.string.button2, myOnClick, true);
				break;
			}
		}
		
	}//end of onItem
	
	class MyDialogOnClick implements OnClickListener{

		@Override
		public void onClick(DialogInterface dialog, int which) {
			Toast.makeText(mContext, "hehe", 1).show();
		}
	}
	class MyOnClick implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			
		}
		
	}

	@Override
	protected void initDatas() {
		MyAdapter myAdapter = new MyAdapter();
		lvActDialog.setAdapter(myAdapter);
	}
	
	private void initMeetingStartTimeDialog() {
		timePickerDialog = new XSPTimePickerDialog(mContext,
				new OnTimeChangedCallback() {

					@Override
					public void onTimeChanged(int hourOfDay, int minute) {
						hourOfStartTime = hourOfDay;
						minuteOfStartTime = minute;
						showFormattedStartTime();
					}
				});

		timePickerDialog.initData(hourOfStartTime, minuteOfStartTime);
	}
	
	private void showFormattedStartTime() {
		if (10 > minuteOfStartTime) { // 分钟数小于10，格式化显示
			meetingStartTimeItem.setContentTxt(hourOfStartTime + " : 0"
					+ minuteOfStartTime);
		} else {
			meetingStartTimeItem.setContentTxt(hourOfStartTime + " : "
					+ minuteOfStartTime);
		}
	}
}
