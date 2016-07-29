package com.xingqiba.weixinaddfriend.module.user;

import android.content.Context;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.chx.http.HttpHandler;
import com.chx.http.HttpHandler.HttpHandlerResponse;
import com.xingqiba.weixinaddfriend.R;
import com.xingqiba.weixinaddfriend.base.BaseApplication;
import com.xingqiba.weixinaddfriend.base.BaseTitleActivity;
import com.xingqiba.weixinaddfriend.constant.BroadCastKeySets;
import com.xingqiba.weixinaddfriend.constant.ConstantHelperUtil;
import com.xingqiba.weixinaddfriend.model.response.UserResponse;
import com.xingqiba.weixinaddfriend.module.main.MonitorAppActivity;
import com.xingqiba.weixinaddfriend.utils.CommonFunction;
import com.xingqiba.weixinaddfriend.utils.SharedPreferencesTool;
import com.xingqiba.weixinaddfriend.widget.SpecialButton;
import com.xingqiba.weixinaddfriend.widget.SpecialLLWithEditText;

import de.greenrobot.event.EventBus;

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
		isLogin();
		chx_login_et_username = (SpecialLLWithEditText) findViewById(R.id.chx_login_et_username);
		chx_login_et_password = (SpecialLLWithEditText) findViewById(R.id.chx_login_et_password);
		chx_login_sb_login = (SpecialButton) findViewById(R.id.chx_login_sb_login);
		chx_login_tv_register = (TextView) findViewById(R.id.chx_login_tv_register);
	}
	
	private void isLogin(){
		boolean isLogin = (boolean) SharedPreferencesTool.getParam(this, "isLogin", false);
		if(isLogin){
			String realName = (String) SharedPreferencesTool.getParam(ChxLoginActivity.this, "realName", "");
			String userName = (String) SharedPreferencesTool.getParam(ChxLoginActivity.this, "userName", "");
			String deviceNo = (String) SharedPreferencesTool.getParam(ChxLoginActivity.this, "deviceNo", "");
			String passWord = (String) SharedPreferencesTool.getParam(ChxLoginActivity.this, "passWord", "");
			String telephone = (String) SharedPreferencesTool.getParam(ChxLoginActivity.this, "telephone", "");
			UserResponse userResponse = new UserResponse();
			userResponse.userName = userName;
			userResponse.deviceNo = deviceNo;
			userResponse.passWord = passWord;
			userResponse.telephone = telephone;
			userResponse.realName = realName;
			BaseApplication.getInstance().setUserResponse(userResponse);
			startActivity(getIntent(MonitorAppActivity.class));
			finish();
		}
	}

	@Override
	public void widgetClick() {
		handleTextViewURl("?请注册", chx_login_tv_register);
		chx_login_sb_login.setOnClickListener(this);
		EventBus.getDefault().register(this);
	}

	@Override
	public void widgetClick(View view) {
		switch (view.getId()) {
		case R.id.chx_login_tv_register: {
			startActivity(getIntent(ChxRegisterActivity.class));
		}
			break;
		case R.id.chx_login_sb_login: {
			final String username = chx_login_et_username.getText().toString();
			String pwd = chx_login_et_password.getText().toString();
			if (TextUtils.isEmpty(username)) {
				showToast("微信号不能为空哦！");
				return;
			}
			if (TextUtils.isEmpty(pwd)) {
				showToast("密码不能为空哦！");
				return;
			}
			if (pwd.length() < 6) {
				showToast("请输入不少于6位的密码");
				return;
			}
			params.clear();
			params.put("userName", username + "");
			params.put("passWord", pwd + "");
			new HttpHandler.Builder()
					.setShow(true)
					.build(context)
					.post(this, ConstantHelperUtil.RequestURL.LOGIN, params,
							new HttpHandlerResponse() {

								@Override
								public void onSuccess(String msg, String data) {
									UserResponse userResponse = JSONObject
											.parseObject(data,
													UserResponse.class);
									BaseApplication.getInstance()
											.setUserResponse(userResponse);
									SharedPreferencesTool.setParam(ChxLoginActivity.this, "isLogin", true);
									SharedPreferencesTool.setParam(ChxLoginActivity.this, "realName", userResponse.realName + "");
									SharedPreferencesTool.setParam(ChxLoginActivity.this, "userName", userResponse.userName + "");
									SharedPreferencesTool.setParam(ChxLoginActivity.this, "deviceNo", userResponse.deviceNo + "");
									SharedPreferencesTool.setParam(ChxLoginActivity.this, "passWord", userResponse.passWord + "");
									SharedPreferencesTool.setParam(ChxLoginActivity.this, "telephone", userResponse.telephone + "");
									startActivity(getIntent(MonitorAppActivity.class));
									finish();
								}

								@Override
								public void onFaile(String data) {
									showToast(data + "");
								}
							});
		}
			break;
		}
	}

	public void onEvent(String key) {
		if (BroadCastKeySets.FINISH.equals(key)) {// 找回密码成功，需要重新登陆
			startActivity(getIntent(MonitorAppActivity.class));
			finish();
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