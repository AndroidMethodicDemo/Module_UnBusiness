package com.example.crash;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

	private String s;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getScreenManager().pushActivity(this);
        System.out.println(s.equals("any string"));
    }
}
