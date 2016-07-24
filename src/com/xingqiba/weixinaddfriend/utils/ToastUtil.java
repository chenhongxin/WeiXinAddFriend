package com.xingqiba.weixinaddfriend.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Toast工具类
 * 
 * @author chengbo
 */
public class ToastUtil {
	private static Toast toast = null;
	
	/**
	 * Toast默认显示在底部
	 * @param context
	 * @param message
	 */
	public static void show(Context context, String message) {
		show(context, message, Gravity.CENTER);
	}

	/**
	 * 现实Toast
	 * 
	 * @param context
	 * @param message
	 * @param position
	 */
	public static void show(Context context, String message, int position) {
		if (toast != null) {
			toast.cancel();
			toast = null;
		}
		toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		toast.setGravity(position, 0, 0);
		toast.show();
	}

}
