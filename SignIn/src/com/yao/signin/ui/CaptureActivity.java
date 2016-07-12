package com.yao.signin.ui;


import android.os.Bundle;
import com.yao.signin.R;
import com.yao.signin.ui.base.BackActivity;

/**
 * Created by yao on 2016/7/2.
 */
public class CaptureActivity extends BackActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scan);
		 setCenterTitle("二维码扫描");
		
	}
}
