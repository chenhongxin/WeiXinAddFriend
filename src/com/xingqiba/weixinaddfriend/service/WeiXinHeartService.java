package com.xingqiba.weixinaddfriend.service;

import java.util.HashMap;
import java.util.Map;

import android.app.IntentService;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.chx.http.HttpHandler;
import com.chx.http.HttpHandler.HttpHandlerResponse;
import com.xingqiba.weixinaddfriend.base.BaseApplication;
import com.xingqiba.weixinaddfriend.constant.ConstantHelperUtil;
import com.xingqiba.weixinaddfriend.model.response.UserResponse;

public class WeiXinHeartService extends IntentService {

	public WeiXinHeartService() {
		super("WeiXinHeartService");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		timer.start();
		Toast.makeText(getApplicationContext(),
				"onCreate", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Toast.makeText(getApplicationContext(),
				"onHandleIntent", Toast.LENGTH_SHORT).show();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		flags = START_STICKY;
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(getApplicationContext(),
				"onDestroy", Toast.LENGTH_SHORT).show();
		timer.cancel();
	}

	int time = 10 * 1000;

	CountDownTimer timer = new CountDownTimer(time, 1000) {

		@Override
		public void onTick(long millisUntilFinished) {
			Log.i("json", millisUntilFinished + "");
		}

		@Override
		public void onFinish() {
			getMonitor();
			timer.start();
		}
	};

	protected void getMonitor() {
		Map<String, String> params = new HashMap<>();
		UserResponse userResponse = BaseApplication.getInstance()
				.getUserResponse();
		params.put("userName", "" + userResponse.userName);
		params.put("deviceNo", "" + userResponse.deviceNo);
		params.put("telphone", "" + userResponse.telephone);
		params.put("passWord", "" + userResponse.passWord);
		new HttpHandler.Builder()
				.setShow(false)
				.build(this)
				.post(null, ConstantHelperUtil.RequestURL.MONITOR, params,
						new HttpHandlerResponse() {

							@Override
							public void onSuccess(String msg, String data) {
								Toast.makeText(getApplicationContext(),
										"" + msg, Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onFaile(String msg) {

							}
						});
	}

}
