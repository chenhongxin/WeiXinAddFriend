package com.xingqiba.weixinaddfriend.receiver;

import com.xingqiba.weixinaddfriend.service.LocalService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("arui.alarm.action")) {  
            Intent i = new Intent();  
            i.setClass(context, LocalService.class);
            context.startService(i);
			Log.i("json", "AlarmReceiver:onReceive");
        }
	}

}
