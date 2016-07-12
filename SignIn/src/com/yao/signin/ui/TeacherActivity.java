package com.yao.signin.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.yao.signin.PullDownList;
import com.yao.signin.PullDownList.OnPDListListener;
import com.yao.signin.R;
import com.yao.signin.ui.base.BackActivity;

/**
 * 教师端Activity
 * Created by yao on 2016/7/2.
 */
public class TeacherActivity extends BackActivity {

    private EditText et_key;
    private RadioGroup radioGroup;
    private RadioButton time;
    private RadioButton number;
    private ImageButton refresh;
    private ListView listView;
    
    private PullDownList pullDownList = null;
    

	//学生数据
	private static ArrayList<String> charList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher);
        setCenterTitle("教师端");
        ImageView iv_scan = (ImageView)setRightDrawable(getResources().getDrawable(R.drawable.sao));
        iv_scan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
                Intent openCameraIntent = new Intent(TeacherActivity.this, CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
			}
		});
        
        et_key = (EditText)findViewById(R.id.et_t_key);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        time = (RadioButton)findViewById(R.id.radioButton);
        number = (RadioButton)findViewById(R.id.radioButton2);
		
		initPullView();

    }
    
    private void initPullView() {
    	pullDownList = (PullDownList) findViewById(R.id.pullDownList1);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line);
		for (int i = 1; i <= 20; i++) {
			adapter.add("" + i);
		}
		pullDownList.setAdapter(adapter);

		pullDownList.setOnPDListen(onPDListListener);
	}
    
    private OnPDListListener onPDListListener = new OnPDListListener() {

		@Override
		public boolean onloadMore() {
			return false;
		}

		@Override
		public void onRefresh() {
			Toast.makeText(TeacherActivity.this, "刷新成功", Toast.LENGTH_LONG).show();
			Handler handler = new Handler();
			handler.postDelayed(new MyThread(), 3000);
		}
	};

	private class MyThread implements Runnable {
		@Override
		public void run() {
			pullDownList.stopRefresh(true);
		}
	}

	private void pullDown() {
		pullDownList.startRefresh();
	}


}