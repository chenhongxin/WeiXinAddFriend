package com.xingqiba.weixinaddfriend.base;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;


/**
 * Created by wangtao on 15/11/17.
 */
public abstract class BaseActivity extends Activity {

    protected Map<String, String> params = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getContentViewID());
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    public void onPause() {
        super.onPause();
    }

    public abstract void initView();

    public abstract int getContentViewID();
    
    public void showToast(String notice) {
        Toast.makeText(this, notice + "", Toast.LENGTH_SHORT).show();
    }
}

