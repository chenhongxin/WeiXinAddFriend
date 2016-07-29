package com.xingqiba.weixinaddfriend.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.xingqiba.weixinaddfriend.service.util.NotifyHelper;

public class WeiXinNotificationService extends NotificationListenerService {

	private static WeiXinNotificationService service;
	public static String title = "";

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
	}

	@SuppressLint("NewApi")
	@Override
	public void onNotificationPosted(StatusBarNotification sbn) {
		Bundle bundle = sbn.getNotification().extras;
		String text = bundle.getString("android.text");
		title = bundle.getString("android.title");
		if ("请求添加你为朋友".equals(text)) {
			Notification notification = sbn.getNotification();
			// 以下是精华，将微信的通知栏消息打开
			PendingIntent pendingIntent = notification.contentIntent;
			boolean lock = NotifyHelper.isLockScreen(getApplicationContext());
			if (!lock) {
				NotifyHelper.send(pendingIntent);
			}
		}
	}

	@Override
	public void onNotificationRemoved(StatusBarNotification sbn) {

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/** 是否启动通知栏监听 */
	public static boolean isRunning() {
		if (service == null) {
			return false;
		}
		return true;
	}

}
