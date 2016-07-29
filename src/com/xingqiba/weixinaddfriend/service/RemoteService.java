package com.xingqiba.weixinaddfriend.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.xingqiba.weixinaddfriend.aidl.ProcessConnection;

public class RemoteService extends Service {

	private MyBinder binder;
	private MyServiceConnection connection;
	
	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		if(binder == null){
			binder = new MyBinder();
		}
		connection = new MyServiceConnection();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// 监听连接远程服务
		this.bindService(new Intent(this, LocalService.class), connection, Context.BIND_ABOVE_CLIENT);
		return START_STICKY;
	}

	class MyBinder extends ProcessConnection.Stub{

		@Override
		public String getProcessName() throws RemoteException {
			return "RemoteService";
		}
		
	}
	
	class MyServiceConnection implements ServiceConnection{

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// LocalService被干掉了
			RemoteService.this.startService(new Intent(RemoteService.this, LocalService.class));

			RemoteService.this.bindService(new Intent(RemoteService.this, LocalService.class), connection, Context.BIND_ABOVE_CLIENT);
		}
		
	}
	
}
