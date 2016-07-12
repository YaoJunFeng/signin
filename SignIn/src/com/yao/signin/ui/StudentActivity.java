package com.yao.signin.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yao.signin.R;
import com.yao.signin.ui.base.BackActivity;

/**
 * 学生端Activity
 * Created by yao on 2016/7/2.
 *
 */
public class StudentActivity extends BackActivity {

    public EditText et_checkKey;
    public EditText et_number;
    public TextView check;
    public TextView tv_tip;
    
    String key = "";//签到码
    String number = "";//学号
    String name = "";//学生的蓝牙设备名字

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student);
        setCenterTitle("学生端");

        et_checkKey = (EditText)findViewById(R.id.editText);
        et_number = (EditText)findViewById(R.id.editText2);
        check = (TextView)findViewById(R.id.textView5);
        tv_tip = (TextView)findViewById(R.id.tv_tips);
        tv_tip.setText("请输入相关信息");

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	key = et_checkKey.getText().toString();
            	number = et_number.getText().toString();
            	if(key==null||key.equals("")){
            		Toast.makeText(StudentActivity.this, "签到码不能为空", Toast.LENGTH_LONG).show();
            		return;
            	}
            	if(number==null||number.equals("")){
            		Toast.makeText(StudentActivity.this, "学号不能为空", Toast.LENGTH_LONG).show();
            		return;
            	}
            	
            	tv_tip.setText("正在进行签到");
            }
        });
        

    }
}
