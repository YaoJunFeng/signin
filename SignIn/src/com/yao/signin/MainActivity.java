package com.yao.signin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.yao.signin.ui.IntroductionActivity;
import com.yao.signin.ui.StudentActivity;
import com.yao.signin.ui.TeacherActivity;

public class MainActivity extends Activity {

	 private Button star_check;
	    private Button check;
	    private TextView intro;

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.activity_main);

	        star_check = (Button) findViewById(R.id.button);
	        check = (Button) findViewById(R.id.button2);
	        intro = (TextView) findViewById(R.id.textView2);

	        star_check.setOnClickListener(new Button.OnClickListener() {
	            public void onClick(View arg) {
	                Intent it = new Intent(MainActivity.this, TeacherActivity.class);
	                MainActivity.this.startActivity(it);
	            }

	        });

	        check.setOnClickListener(new Button.OnClickListener() {
	            public void onClick(View arg) {
	                Intent it = new Intent(MainActivity.this, StudentActivity.class);
	                MainActivity.this.startActivity(it);
	            }

	        });

	        intro.setOnClickListener(new TextView.OnClickListener() {
	            public void onClick(View arg) {
	                Intent it = new Intent(MainActivity.this, IntroductionActivity.class);
	                MainActivity.this.startActivity(it);
	            }
	        });

	    }

}
