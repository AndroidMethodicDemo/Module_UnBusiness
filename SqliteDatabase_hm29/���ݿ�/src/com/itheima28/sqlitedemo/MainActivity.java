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
		// ���ݿ�ʲôʱ�򴴽�
		PersonSQLiteOpenHelper openHelper = new PersonSQLiteOpenHelper(this);
		// ��һ���������ݿ�ʱ�������ݿ��ļ�. onCreate�ᱻ����
		openHelper.getReadableDatabase();

	}

}
