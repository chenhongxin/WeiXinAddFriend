package com.xingqiba.weixinaddfriend.module.user;

import android.view.View;

import com.xingqiba.weixinaddfriend.R;
import com.xingqiba.weixinaddfriend.base.BaseTitleActivity;
import com.xingqiba.weixinaddfriend.widget.SpecialLLWithEditText;

public class ChxLoginActivity extends BaseTitleActivity {

	private SpecialLLWithEditText chx_login_et_username;
	private SpecialLLWithEditText chx_login_et_password;
	
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
	}

	@Override
	public void widgetClick() {
		
	}

	@Override
	public void widgetClick(View view) {
		
	}

}