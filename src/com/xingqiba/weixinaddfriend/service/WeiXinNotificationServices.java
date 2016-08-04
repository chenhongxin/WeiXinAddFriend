package com.xingqiba.weixinaddfriend.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import com.xingqiba.weixinaddfriend.R;
import com.xingqiba.weixinaddfriend.constant.BroadCastKeySets;
import com.xingqiba.weixinaddfriend.module.main.MonitorAppActivity;

public class WeiXinNotificationServices extends NotificationListenerService {

	private static WeiXinNotificationServices service;
	public static String title = "";
	private PowerManager pm;
	private PowerManager.WakeLock wakeLock;

	@Override
	public void onCreate() {
		super.onCreate();
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			onListenerConnected();
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onListenerConnected() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			super.onListenerConnected();
		}
		service = this;
		timer.start();
		// 创建PowerManager对象
		pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		// 保持cpu一直运行，不管屏幕是否黑屏
		wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
				"CPUKeepRunning");
		wakeLock.acquire();
	}

	// @SuppressLint("NewApi")
	// @Override
	// public void onNotificationPosted(StatusBarNotification sbn) {
	// Bundle bundle = sbn.getNotification().extras;
	// String text = bundle.getString("android.text");
	// title = bundle.getString("android.title");
	// if ("请求添加你为朋友".equals(text)) {
	// Notification notification = sbn.getNotification();
	// // 以下是精华，将微信的通知栏消息打开
	// PendingIntent pendingIntent = notification.contentIntent;
	// boolean lock = NotifyHelper.isLockScreen(getApplicationContext());
	// if (!lock) {
	// NotifyHelper.send(pendingIntent);
	// }
	// }
	// }

	@SuppressLint("NewApi")
	@Override
	public void onNotificationPosted(StatusBarNotification sbn) {
		String packName = sbn.getPackageName();
		if (!"com.tencent.mm".equals(packName)) {
			return;
		}
		Bundle bundle = sbn.getNotification().extras;
		String text = bundle.getString("android.text");
		title = bundle.getString("android.title");
		if ("请求添加你为朋友".equals(text)) {
			BroadCastKeySets.postBroadCast(BroadCastKeySets.ADDUSERINFO + ","
					+ WeiXinNotificationServices.title);
		}
	}

	@Override
	public void onNotificationRemoved(StatusBarNotification sbn) {

	}

	@Override
	public void onDestroy() {
		stopForeground(true);
		Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
		Log.i("json", "onDestroy");
		wakeLock.release();
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Notification notification = new Notification(R.drawable.ic_launcher,
				getString(R.string.app_name), System.currentTimeMillis());

		PendingIntent pendingintent = PendingIntent.getActivity(this, 0,
				new Intent(this, MonitorAppActivity.class), 0);
//		notification.setLatestEventInfo(this, "uploadservice", "请保持程序在后台运行",
//				pendingintent);
		notification.contentIntent = pendingintent;
		startForeground(0x111, notification);
		flags = START_STICKY;
		return super.onStartCommand(intent, flags, startId);
	}

	/** 是否启动通知栏监听 */
	public static boolean isRunning() {
		if (service == null) {
			return false;
		}
		return true;
	}

	CountDownTimer timer = new CountDownTimer(20000, 1000) {

		@Override
		public void onTick(long millisUntilFinished) {

		}

		@Override
		public void onFinish() {
			timer.start();
		}
	};

}
