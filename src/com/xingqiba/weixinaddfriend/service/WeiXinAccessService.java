package com.xingqiba.weixinaddfriend.service;

import java.util.Iterator;
import java.util.List;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

import com.xingqiba.weixinaddfriend.constant.BroadCastKeySets;

public class WeiXinAccessService extends AccessibilityService {

	private static WeiXinAccessService service;
	public static boolean isRunning = false;

	@Override
	protected void onServiceConnected() {
		super.onServiceConnected();
		service = this;
		isRunning = true;
	}

	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		try {
			String pkn = String.valueOf(event.getPackageName());
			if (pkn.indexOf("com.tencent") == -1) {
				return;
			}
			String className = event.getClassName() + "";
			if("android.app.Notification".equals(className)){
				String text = event.getText().get(0) + "";
				if(text.endsWith("请求添加你为朋友")){
					text = text.substring(0, text.indexOf("请求添加你为朋友"));
					BroadCastKeySets.postBroadCast(BroadCastKeySets.ADDUSERINFO + "," + text);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断当前服务是否正在运行
	 * */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public static boolean isRunning() {
		if (service == null) {
			return false;
		}
		AccessibilityManager accessibilityManager = (AccessibilityManager) service
				.getSystemService(Context.ACCESSIBILITY_SERVICE);
		AccessibilityServiceInfo info = service.getServiceInfo();
		if (info == null) {
			return false;
		}
		List<AccessibilityServiceInfo> list = accessibilityManager
				.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
		Iterator<AccessibilityServiceInfo> iterator = list.iterator();

		boolean isConnect = false;
		while (iterator.hasNext()) {
			AccessibilityServiceInfo i = iterator.next();
			if (i.getId().equals(info.getId())) {
				isConnect = true;
				break;
			}
		}
		if (!isConnect) {
			return false;
		}
		return true;
	}

	@Override
	public void onInterrupt() {
		
	}

}
