package com.xingqiba.weixinaddfriend.service;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;

import com.xingqiba.weixinaddfriend.aidl.ProcessConnection;
import com.xingqiba.weixinaddfriend.base.BaseApplication;
import com.xingqiba.weixinaddfriend.constant.ConstantHelperUtil;
import com.xingqiba.weixinaddfriend.model.response.UserResponse;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

public class LocalService extends Service {

	private MyBinder binder;
	private MyServiceConnection connection;
//	private PowerManager pm;
//	private PowerManager.WakeLock wakeLock;
	
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
		timer.start();
//		// 创建PowerManager对象
//		pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//		// 保持cpu一直运行，不管屏幕是否黑屏
//		wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
//				"CPUKeepRunning");
//		wakeLock.acquire();
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
//			wakeLock.release();
			// RemoteService被干掉了
			LocalService.this.startService(new Intent(LocalService.this, RemoteService.class));
			// 乘机建立连接
			LocalService.this.bindService(new Intent(LocalService.this, RemoteService.class), connection, Context.BIND_IMPORTANT);
		}
		
	}
	
	CountDownTimer timer = new CountDownTimer(10000, 1000) {
		
		@Override
		public void onTick(long millisUntilFinished) {
			
		}
		
		@Override
		public void onFinish() {
			timer.start();
			Map<String, String> params = new HashMap<>();
			UserResponse userResponse = BaseApplication.getInstance()
					.getUserResponse();
			params.put("userName", "" + userResponse.userName);
			params.put("deviceNo", "" + userResponse.deviceNo);
			params.put("telphone", "" + userResponse.telephone);
			params.put("passWord", "" + userResponse.passWord);
			OkHttpUtils.post().url("http://114.55.114.35:8080/" + ConstantHelperUtil.RequestURL.MONITOR).params(params).build()
			.execute(new StringCallback() {
				
				@Override
				public void onResponse(String response, int id) {
					Toast.makeText(getApplicationContext(), "回调完成", Toast.LENGTH_SHORT).show();
				}
				
				@Override
				public void onError(Call call, Exception e, int id) {
					
				}
			});
		}
	};
	
}
