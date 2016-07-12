package com.yao.signin.ui;

import android.os.Bundle;
import android.view.MenuItem;

import com.yao.signin.R;
import com.yao.signin.ui.base.BackActivity;

/**
 * Created by yao on 2016/7/2.
 */
public class IntroductionActivity extends BackActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introduction);
        setCenterTitle("介绍");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}