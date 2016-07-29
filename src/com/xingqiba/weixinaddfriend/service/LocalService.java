package com.xingqiba.weixinaddfriend.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.xingqiba.weixinaddfriend.aidl.ProcessConnection;

public class LocalService extends Service {

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
		this.bindService(new Intent(this, RemoteService.class), connection, Context.BIND_IMPORTANT);
		return START_STICKY;
	}

	class MyBinder extends ProcessConnection.Stub{

		@Override
		public String getProcessName() throws RemoteException {
			return "LocalService";
		}
		
	}
	
	class MyServiceConnection implements ServiceConnection{

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// RemoteService被干掉了
			LocalService.this.startService(new Intent(LocalService.this, RemoteService.class));
			// 乘机建立连接
			LocalService.this.bindService(new Intent(LocalService.this, RemoteService.class), connection, Context.BIND_IMPORTANT);
		}
		
	}
	
}
