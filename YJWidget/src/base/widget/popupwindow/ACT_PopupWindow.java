package base.widget.popupwindow;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import base.ACT_Base;

import com.example.demoxswidget.R;

public class ACT_PopupWindow extends ACT_Base{
	
	private Button btnTitlePopup;
	private Button btnAboutPopup;
	private Button btnDownupPopup;
	private Button btnSharePopup;
	private Button btnMoreMenuPopup;
	private XSPTitlePopupWindowWithOptions titlePopupWindowWithOptions;
	private AboutPopupWindow aboutPopupWindow;
	private XSPDownUpPopupWindow popWindow;
	private SharePopupWindow shareWindow;
	private MoreMenuPopupWindow moreMenuPopup;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
	}

	@Override
	protected void initViews() {
		setContentView(R.layout.act_popupwindow);
		btnTitlePopup=(Button) findViewById(R.id.btnTitlePopup);
		btnAboutPopup=(Button) findViewById(R.id.btnAboutPopup);
		btnDownupPopup=(Button) findViewById(R.id.btnDownUpPopup);
		btnSharePopup=(Button) findViewById(R.id.btnSharePopup);
		btnMoreMenuPopup=(Button) findViewById(R.id.btnMoreMenuPopup);
		
		titlePopupWindowWithOptions = new XSPTitlePopupWindowWithOptions(mContext,
				new int[]{R.string.faqihuihua,R.string.delete},
				new int[]{R.drawable.main_003_contact,R.drawable.main_003_group},
				new View.OnClickListener[]{clickItem,clickItem});
		
		aboutPopupWindow = new AboutPopupWindow(mContext);
		
		popWindow = new XSPDownUpPopupWindow(mContext, 0, new int[] {
				R.string.str_messaging_action_take_video_006_013,
				R.string.str_messaging_action_pick_video_006_014 },
				new OnClickListener[] { clickItem, clickItem },
				null);
		popWindow.initAnimation(200);
		
		shareWindow=new SharePopupWindow(mContext, new OnClickListener[]{clickItem,clickItem,clickItem,clickCancel});
		shareWindow.initAnimation(200);
		
		moreMenuPopup=new MoreMenuPopupWindow(mContext, null);
	}

	@Override
	protected void bindEvents() {
		OnClickPopup onClickPopup = new OnClickPopup();
		btnTitlePopup.setOnClickListener(onClickPopup);
		btnAboutPopup.setOnClickListener(onClickPopup);
		btnDownupPopup.setOnClickListener(onClickPopup);
		btnSharePopup.setOnClickListener(onClickPopup);
		btnMoreMenuPopup.setOnClickListener(onClickPopup);
	}

	@Override
	protected void initDatas() {
		
	}
	class OnClickPopup implements OnClickListener{


		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.btnAboutPopup:
				aboutPopupWindow.showAsDropDown(btnAboutPopup, 100, 100);
				break;

			case R.id.btnTitlePopup:
				titlePopupWindowWithOptions.showAsDropDown(btnTitlePopup);
				break;
			case R.id.btnDownUpPopup:
				
				popWindow.showAtLocation(mContext.findViewById(R.id.layout), Gravity.BOTTOM
						| Gravity.CENTER_HORIZONTAL, 0, 0);
				break;
			case R.id.btnSharePopup:
				shareWindow.showAtLocation(mContext.findViewById(R.id.layout), Gravity.BOTTOM
						| Gravity.CENTER_HORIZONTAL, 0, 100);
				break;
			case R.id.btnMoreMenuPopup:
				moreMenuPopup.showAtLocation(mContext.findViewById(R.id.btnMoreMenuPopup), Gravity.BOTTOM
						| Gravity.CENTER_HORIZONTAL, 0, 100);

				break;
			}
		}
		
	}
	
	private OnClickListener clickItem = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Toast.makeText(mContext, "点击了"+v.getId(), 1).show();

		}
	};
	private OnClickListener clickCancel = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			shareWindow.dismiss();
		}
	};

	

}
