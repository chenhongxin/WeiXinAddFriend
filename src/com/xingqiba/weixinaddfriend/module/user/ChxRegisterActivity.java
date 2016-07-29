package com.xingqiba.weixinaddfriend.module.user;

import android.content.Context;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.chx.http.HttpHandler;
import com.chx.http.HttpHandler.HttpHandlerResponse;
import com.xingqiba.weixinaddfriend.R;
import com.xingqiba.weixinaddfriend.base.BaseApplication;
import com.xingqiba.weixinaddfriend.base.BaseTitleActivity;
import com.xingqiba.weixinaddfriend.constant.BroadCastKeySets;
import com.xingqiba.weixinaddfriend.constant.ConstantHelperUtil;
import com.xingqiba.weixinaddfriend.model.response.UserResponse;
import com.xingqiba.weixinaddfriend.utils.AndroidBug5497Workaround;
import com.xingqiba.weixinaddfriend.utils.CommonFunction;
import com.xingqiba.weixinaddfriend.utils.SharedPreferencesTool;
import com.xingqiba.weixinaddfriend.widget.SpecialButton;
import com.xingqiba.weixinaddfriend.widget.SpecialLLWithEditText;

public class ChxRegisterActivity extends BaseTitleActivity {

	private TextView chx_register_tv_android_id;
	private SpecialButton chx_register_sb_register;
	private SpecialLLWithEditText chx_register_et_username;
	private SpecialLLWithEditText chx_register_et_password;
	private SpecialLLWithEditText chx_register_et_newpassword;
	private SpecialLLWithEditText chx_register_et_phone;
	private SpecialLLWithEditText chx_register_et_realname;
	private String android_id = "";

	@Override
	public void setTitle() {
		setTitle(R.string.register);
	}

	@Override
	public void initControl() {
		chx_register_tv_android_id = (TextView) findViewById(R.id.chx_register_tv_android_id);
		chx_register_sb_register = (SpecialButton) findViewById(R.id.chx_register_sb_register);
		android_id = Secure.getString(getContentResolver(),
				Secure.ANDROID_ID);
		chx_register_et_realname = (SpecialLLWithEditText) findViewById(R.id.chx_register_et_realname);
		chx_register_et_username = (SpecialLLWithEditText) findViewById(R.id.chx_register_et_username);
		chx_register_et_password = (SpecialLLWithEditText) findViewById(R.id.chx_register_et_password);
		chx_register_et_newpassword = (SpecialLLWithEditText) findViewById(R.id.chx_register_et_newpassword);
		chx_register_et_phone = (SpecialLLWithEditText) findViewById(R.id.chx_register_et_phone);
		chx_register_tv_android_id.setText(android_id + "");
		showBack();
	}

	@Override
	public void widgetClick() {
		AndroidBug5497Workaround.assistActivity(this);
		chx_register_sb_register.setOnClickListener(this);
	}

	@Override
	public void widgetClick(View view) {
		switch (view.getId()) {
		case R.id.chx_register_sb_register: {
			final String realName = chx_register_et_realname.getText();
			final String username = chx_register_et_username.getText().toString();
			final String pwd = chx_register_et_password.getText().toString();
			String againPwd = chx_register_et_newpassword.getText().toString();
			final String phone = chx_register_et_phone.getText().toString();
			if (TextUtils.isEmpty(realName)) {
				showToast("姓名不能为空哦！");
				return;
			}
			if (TextUtils.isEmpty(username)) {
				showToast("微信号不能为空哦！");
				return;
			}
			if (TextUtils.isEmpty(pwd)) {
				showToast("密码不能为空哦！");
				return;
			}
			if (pwd.length() < 6) {
				showToast("密码长度不能少于6位，建议设置字母数字特殊字符组合密码");
				return;
			}
			if (TextUtils.isEmpty(againPwd)) {
				showToast("确认密码不能为空哦！");
				return;
			}
			if (pwd.equals(username)) {
				showToast("密码和用户名不能相同");
				return;
			}
			if (!pwd.equals(againPwd)) {
				showToast("密码和确认密码不一致哦！");
				return;
			}
			if (TextUtils.isEmpty(phone)) {
				showToast("手机号码不能为空哦！");
				return;
			}
			if (!CommonFunction.isMobileNO(phone)) {
				showToast("请输入您的真实手机号码，为11位数字");
				return;
			}
			params.clear();
			params.put("userName", username + "");
			params.put("passWord", pwd + "");
			params.put("telphone", phone + "");
			params.put("deviceNo", android_id + "");
			params.put("realName", realName + "");
			params.put("userType", "1");
			new HttpHandler.Builder()
					.setShow(true)
					.build(context)
					.post(this, ConstantHelperUtil.RequestURL.REGISTER, params,
							new HttpHandlerResponse() {

								@Override
								public void onSuccess(String msg, String data) {
									UserResponse userResponse = new UserResponse();
									userResponse.userName = username;
									userResponse.passWord = pwd;
									userResponse.deviceNo = android_id;
									userResponse.telephone = phone;
									userResponse.realName = realName;
									BaseApplication.getInstance()
											.setUserResponse(userResponse);
									SharedPreferencesTool.setParam(ChxRegisterActivity.this, "userName", userResponse.userName + "");
									SharedPreferencesTool.setParam(ChxRegisterActivity.this, "realName", userResponse.realName + "");
									SharedPreferencesTool.setParam(ChxRegisterActivity.this, "deviceNo", userResponse.deviceNo + "");
									SharedPreferencesTool.setParam(ChxRegisterActivity.this, "passWord", userResponse.passWord + "");
									SharedPreferencesTool.setParam(ChxRegisterActivity.this, "telephone", userResponse.telephone + "");
									SharedPreferencesTool.setParam(ChxRegisterActivity.this, "isLogin", true);
									BroadCastKeySets.postBroadCast(BroadCastKeySets.FINISH);
									finish();
								}

								@Override
								public void onFaile(String data) {
									showToast(data + "");
								}
							});
		}
			break;

		default:
			break;
		}
	}

	@Override
	public int getContentViewID() {
		return R.layout.chx_register_activity;
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
