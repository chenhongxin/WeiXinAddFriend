package com.xingqiba.weixinaddfriend.module.main;

import java.util.UUID;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;

import com.xingqiba.weixinaddfriend.R;
import com.xingqiba.weixinaddfriend.base.BaseTitleActivity;

public class MonitorAppActivity extends BaseTitleActivity {

	private TextView chx_monitor_tv_device_id;
	
	@Override
	public void setTitle() {
		setTitle(R.string.monitor_app);
	}

	@Override
	public void initControl() {
		chx_monitor_tv_device_id = (TextView) findViewById(R.id.chx_monitor_tv_device_id);
		chx_monitor_tv_device_id.setText(getMyUUID() + "");
	}

	@Override
	public void widgetClick() {

	}

	@Override
	public void widgetClick(View view) {

	}

	@Override
	public int getContentViewID() {
		return R.layout.chx_monitor_app_layout;
	}

	private String getMyUUID() {
		final TelephonyManager tm = (TelephonyManager) getBaseContext()
				.getSystemService(Context.TELEPHONY_SERVICE);
		final String tmDevice, tmSerial, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = ""
				+ android.provider.Settings.Secure.getString(
						getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
		UUID deviceUuid = new UUID(androidId.hashCode(),
				((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		String uniqueId = deviceUuid.toString();
		return uniqueId;
	}

}
