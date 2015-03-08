package com.itheima28.sqlitedemo;

import com.itheima28.sqlitedemo.db.PersonSQLiteOpenHelper;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void onClick(View view) {
		// 数据库什么时候创建
		PersonSQLiteOpenHelper openHelper = new PersonSQLiteOpenHelper(this);
		// 第一次连接数据库时创建数据库文件. onCreate会被调用
		openHelper.getReadableDatabase();

	}

}
