package com.xingqiba.weixinaddfriend.module.user;

import android.content.Context;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.chx.http.HttpHandler;
import com.chx.http.HttpHandler.HttpHandlerResponse;
import com.xingqiba.weixinaddfriend.R;
import com.xingqiba.weixinaddfriend.base.BaseTitleActivity;
import com.xingqiba.weixinaddfriend.utils.CommonFunction;
import com.xingqiba.weixinaddfriend.widget.SpecialButton;
import com.xingqiba.weixinaddfriend.widget.SpecialLLWithEditText;

public class ChxLoginActivity extends BaseTitleActivity {

	private SpecialLLWithEditText chx_login_et_username;
	private SpecialLLWithEditText chx_login_et_password;
	private SpecialButton chx_login_sb_login;
	private TextView chx_login_tv_register;

	@Override
	public int getContentViewID() {
		return R.layout.chx_login_activity;
	}

	@Override
	public void setTitle() {
		setTitle("登录");
	}

	@Override
	public void initControl() {
		chx_login_et_username = (SpecialLLWithEditText) findViewById(R.id.chx_login_et_username);
		chx_login_et_password = (SpecialLLWithEditText) findViewById(R.id.chx_login_et_password);
		chx_login_sb_login = (SpecialButton) findViewById(R.id.chx_login_sb_login);
		chx_login_tv_register = (TextView) findViewById(R.id.chx_login_tv_register);
	}

	@Override
	public void widgetClick() {
		handleTextViewURl("?请注册", chx_login_tv_register);
		chx_login_sb_login.setOnClickListener(this);
	}

	@Override
	public void widgetClick(View view) {
		switch (view.getId()) {
		case R.id.chx_login_tv_register:{
			startActivity(getIntent(ChxRegisterActivity.class));
		}
			break;
		case R.id.chx_login_sb_login:
		{
			String username = chx_login_et_username.getText().toString();
			String pwd = chx_login_et_password.getText().toString();
			if(TextUtils.isEmpty(username)){
				showToast("用戶名不能为空哦！");
				return;
			}
			if(TextUtils.isEmpty(pwd)){
				showToast("密码不能为空哦！");
				return;
			}
			if(pwd.length() < 6){
				showToast("请输入不少于6位的密码");
				return;
			}
			new HttpHandler.Builder().setShow(true).build(context).post(this, "", params, new HttpHandlerResponse() {
				
				@Override
				public void onSuccess(String data) {
					
				}
				
				@Override
				public void onFaile(String data) {
					
				}
			});
		}
			break;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			View v = getCurrentFocus();
			if (CommonFunction.isShouldHideInput(v, event)) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm != null) {
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				}
			}
		}
		return super.onTouchEvent(event);
	}
	

}