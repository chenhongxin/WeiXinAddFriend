package com.xingqiba.weixinaddfriend.service;

import java.util.Iterator;
import java.util.List;

import com.xingqiba.weixinaddfriend.constant.BroadCastKeySets;
import com.xingqiba.weixinaddfriend.service.util.AccessibilityHelper;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;

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
			switch (className) {
			case "com.tencent.mm.plugin.subapp.ui.friend.FMessageConversationUI": {
				final int eventType = event.getEventType();
				if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
					findNodesByText(event, "" + WeiXinNotificationService.title);
					
				}
			}
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void search(AccessibilityNodeInfo child) {
		if (child.getChildCount() != 0) {
			for (int i = 0; i < child.getChildCount(); i++) {
				AccessibilityNodeInfo noteInfo = child.getChild(i);
				if (noteInfo != null) {
					if("android.widget.TextView".equals(noteInfo.getClassName())){
						String text = noteInfo.getText().toString();
						if(text.equals(WeiXinNotificationService.title)){
							int count = child.getChildCount();
							if(i + 1 < count){
								isContinute = false;
								noteInfo = child.getChild(i + 1);
								text = noteInfo.getText().toString();
								AccessibilityHelper.performBack(service);
								AccessibilityHelper.performBack(service);
								BroadCastKeySets.postBroadCast(BroadCastKeySets.ADDUSERINFO + "," + WeiXinNotificationService.title + "," + text);
							}
						}
					}
				}
				search(noteInfo);
			}
		}
	}
	
	boolean isContinute = true;
	/**
	 * 根据文字寻找节点
	 * 
	 * @param event
	 * @param text
	 *            文字
	 */
	private void findNodesByText(AccessibilityEvent event, String text) {
		List<AccessibilityNodeInfo> nodes = event.getSource()
				.findAccessibilityNodeInfosByText(text);
		if (nodes != null && !nodes.isEmpty()) {
			for (AccessibilityNodeInfo info : nodes) {
				AccessibilityNodeInfo note = info.getParent();
				while(note != null && isContinute){
					int count = note.getChildCount();
					for (int i = 0; i < count; i++) {
						AccessibilityNodeInfo child = note.getChild(i);
						search(child);
					}
					note = note.getParent();
				}
				isContinute = true;
			}
		}
	}
	@Override
	public void onInterrupt() {

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

}
