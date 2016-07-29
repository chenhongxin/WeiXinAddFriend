package com.xingqiba.weixinaddfriend.constant;

import de.greenrobot.event.EventBus;

/**
 * @ClassName: BroadCastKeySets
 * @Description:(所有广播的发送key)
 * @author chengbo
 * @date 2015-12-1 下午3:16:28
 */
public class BroadCastKeySets {

	public static final String ADDUSERINFO = "addUserInfo";
	public static final String FINISH = "finish";

	public static void postBroadCast(Object object) {
		EventBus.getDefault().post(object);
	}
}
