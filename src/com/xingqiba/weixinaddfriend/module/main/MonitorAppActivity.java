package com.xingqiba.weixinaddfriend.module.main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chx.http.HttpHandler;
import com.chx.http.HttpHandler.HttpHandlerResponse;
import com.xingqiba.weixinaddfriend.R;
import com.xingqiba.weixinaddfriend.base.BaseApplication;
import com.xingqiba.weixinaddfriend.base.BaseTitleActivity;
import com.xingqiba.weixinaddfriend.constant.BroadCastKeySets;
import com.xingqiba.weixinaddfriend.constant.ConstantHelperUtil;
import com.xingqiba.weixinaddfriend.model.response.UserResponse;
import com.xingqiba.weixinaddfriend.module.user.ChxLoginActivity;
import com.xingqiba.weixinaddfriend.service.LocalService;
import com.xingqiba.weixinaddfriend.service.RemoteService;
import com.xingqiba.weixinaddfriend.service.WeiXinAccessService;
import com.xingqiba.weixinaddfriend.utils.SharedPreferencesTool;
import com.xingqiba.weixinaddfriend.widget.SpecialButton;

import de.greenrobot.event.EventBus;

public class MonitorAppActivity extends BaseTitleActivity {

	private TextView chx_monitor_tv_device_id;
	private SpecialButton chx_monitor_sb_test;
	private TextView chx_monitor_tv_time;
	private TextView chx_monitor_tv_message;
	private String android_id = "";

	@Override
	public void setTitle() {
		setTitle(R.string.monitor_app);
	}

	@Override
	public void initControl() {

		android_id = Secure.getString(getContentResolver(), Secure.ANDROID_ID);

		chx_monitor_tv_device_id = (TextView) findViewById(R.id.chx_monitor_tv_device_id);
		chx_monitor_tv_time = (TextView) findViewById(R.id.chx_monitor_tv_time);
		chx_monitor_tv_message = (TextView) findViewById(R.id.chx_monitor_tv_message);
		chx_monitor_sb_test = (SpecialButton) findViewById(R.id.chx_monitor_sb_test);
		setRightText("退出登录");
		chx_monitor_tv_device_id.setText(android_id + "");

	}

	@Override
	public void widgetClick() {
		chx_monitor_sb_test.setOnClickListener(this);
		EventBus.getDefault().register(this);
		rightText.setOnClickListener(this);
	}

	boolean start = false;
	Intent localService;
	Intent remoteService;

	@Override
	public void widgetClick(View view) {
		switch (view.getId()) {
		case R.id.chx_monitor_sb_test: {
			if (!openAccessibilityServiceSettings()) {
				return;
			}
			if (start) {
				showToast("已监控中...");
				return;
			}
			start = true;
			chx_monitor_sb_test.setText("监控中");
			// 兩個服務首先得启动起来
			if (localService == null) {
				localService = new Intent(this, LocalService.class);
				remoteService = new Intent(this, RemoteService.class);
				this.startService(localService);
				this.startService(remoteService);
				Intent intent = new Intent(
						"android.intent.action.BootBroadcast");
				sendBroadcast(intent);
			}
			getMonitor();
		}
			break;
		case R.id.activity_right_text: {
			startActivity(getIntent(ChxLoginActivity.class));
			SharedPreferencesTool.setParam(this, "isLogin", false);
			finish();
		}
			break;
		}
	}

	@Override
	public int getContentViewID() {
		return R.layout.chx_monitor_app_layout;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onLowMemory() {
		Log.i("json", "onLowMemory");
		showToast("onLowMemory");
		recreate();
		super.onLowMemory();
	}

	@Override
	public void onTrimMemory(int level) {
		Log.i("json", "level:" + level);
		// showToast("level:" + level);
		super.onTrimMemory(level);
	}

	public void onEvent(final Object data) {
		if (BroadCastKeySets.ADDUSERINFO.equals(data.toString().split(",")[0])) {
			chx_monitor_sb_test.post(new Runnable() {

				@Override
				public void run() {
					notificationMessage(data);
				}
			});
		}
	}

	String friend = "";

	private void notificationMessage(Object data) {
		final String friendName = data.toString().split(",")[1];
		if (friendName.equals(friend)) {
			chx_monitor_sb_test.postDelayed(new Runnable() {

				@Override
				public void run() {
					friend = "";
				}
			}, 10000);
			return;
		}
		friend = friendName;
		String noticeMessage = "没有通知信息";
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.CHINESE);
		chx_monitor_tv_time.setText(format.format(date));
		chx_monitor_tv_message.setText(friendName + ":" + noticeMessage);
		params.clear();
		UserResponse userResponse = BaseApplication.getInstance()
				.getUserResponse();
		params.put("userName", "" + userResponse.userName);
		params.put("deviceNo", "" + userResponse.deviceNo);
		params.put("passWord", "" + userResponse.passWord);
		params.put("friendWeixinName", "" + friendName);
		params.put("noticeMessage", "" + noticeMessage);
		params.put("appName", "微信");
		new HttpHandler.Builder().build(this).post(this,
				ConstantHelperUtil.RequestURL.ADDFRIENDINFO, params,
				new HttpHandlerResponse() {

					@Override
					public void onSuccess(String msg, String data) {
						showToast(msg);
					}

					@Override
					public void onFaile(String msg) {

					}
				});
	}

	private void getMonitor() {
		params.clear();
		UserResponse userResponse = BaseApplication.getInstance()
				.getUserResponse();
		params.put("userName", "" + userResponse.userName);
		params.put("deviceNo", "" + userResponse.deviceNo);
		params.put("telphone", "" + userResponse.telephone);
		params.put("passWord", "" + userResponse.passWord);
		new HttpHandler.Builder()
				.setShow(true)
				.build(this)
				.post(this, ConstantHelperUtil.RequestURL.MONITOR, params,
						new HttpHandlerResponse() {

							@Override
							public void onSuccess(String msg, String data) {
								showToast("" + msg);
							}

							@Override
							public void onFaile(String msg) {
								showToast(msg + "");
								start = false;
							}
						});
	}

	/** 打开辅助服务的设置 */
	private boolean openAccessibilityServiceSettings() {
		boolean isOpen = true;
		if (!WeiXinAccessService.isRunning()) {
			try {
				Intent intent = new Intent(
						Settings.ACTION_ACCESSIBILITY_SETTINGS);
				startActivity(intent);
				isOpen = false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return isOpen;
	}

	@Override
	public void onBackPressed() {
		// 按返回键返回桌面
		moveTaskToBack(true);
	}

	// @Override
	// public void onBackPressed() {
	// // 按返回键返回桌面
	// Intent intent = new Intent(Intent.ACTION_MAIN);
	// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	// intent.addCategory(Intent.CATEGORY_HOME);
	// startActivity(intent);
	// }

}