package com.xingqiba.weixinaddfriend.base;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.xingqiba.weixinaddfriend.utils.MyURLSpan;


/**
 * Created by wangtao on 15/11/17.
 */
public abstract class BaseActivity extends Activity {

    protected Map<String, String> params = new HashMap<>();
    protected Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getContentViewID());
        context = this;
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

    @SuppressWarnings("deprecation")
	public void handleTextViewURl(String text, TextView view, MyURLSpan myURLSpan) {
		int start = 0;
		int end = text.length();
		SpannableString spannableInfo = new SpannableString(text);
		spannableInfo.setSpan(myURLSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		view.append(spannableInfo);
		view.setHighlightColor(getResources().getColor(android.R.color.transparent));
		view.setMovementMethod(LinkMovementMethod.getInstance());
	}
    
    public abstract void initView();

    public abstract int getContentViewID();
    
    public void showToast(String notice) {
        Toast.makeText(this, notice + "", Toast.LENGTH_SHORT).show();
    }
    
    public Intent getIntent(Class<?> clazz){
    	Intent intent = new Intent(this, clazz);
    	return intent;
    }
    
}