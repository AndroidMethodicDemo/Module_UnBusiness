package com.lidroid.xutils.sample.ui.base;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.lidroid.xutils.sample.R;

public abstract class ACT_Base extends FragmentActivity{
	protected Activity mContext;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		
		// 初始化crash handler
		//CrashHandler.getInstance().init(this);
		
		mContext = ACT_Base.this;
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		initViews();
		
		bindEvents();
		
		initDatas();
	}

	/**
	 * 初始化view
	 */
	protected abstract void initViews();
	
	/**
	 * 绑定事件
	 */
	protected abstract void bindEvents();
	
	/**
	 * 加载数据，显示到界面
	 */
	protected abstract void initDatas();
	
	public void exitFromLeftToRight(){
		mContext.finish();
		mContext.overridePendingTransition(0, R.anim.exit_left_right);
	}
	
	@Override
	public void onBackPressed() {
		exitFromLeftToRight();
	}
}
