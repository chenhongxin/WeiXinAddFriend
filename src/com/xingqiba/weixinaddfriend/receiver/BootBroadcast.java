package com.xingqiba.weixinaddfriend.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class BootBroadcast extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent mintent) {
		Log.i("json", "onReceive");
		// 启动完成
		Intent intent = new Intent(context, AlarmReceiver.class);
		intent.setAction("arui.alarm.action");
		PendingIntent sender = PendingIntent
				.getBroadcast(context, 0, intent, 0);
		long firstime = SystemClock.elapsedRealtime();
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		// 10秒一个周期，不停的发送广播
		am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstime, 1000,
				sender);
	}

}