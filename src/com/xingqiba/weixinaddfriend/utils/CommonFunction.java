package com.xingqiba.weixinaddfriend.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * @ClassName: CommonFunction
 * @Description:(公共方法调用)
 * @author chengbo
 * @date 2015-4-9 下午1:14:30
 */
public class CommonFunction {

	/**
	 * 从JSONObject中根据key获取值
	 * 
	 * @param result
	 * @param value
	 * @return
	 */
	public static Object getValueByKey(JSONObject result, String key) {
		if (result.has(key)) {
			try {
				return result.get(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	/**
	 * 从JSONObject中根据key获取值
	 * 
	 * @param result
	 * @param value
	 * @return
	 */
	public static Object getValueByKey(com.alibaba.fastjson.JSONObject result,
			String key) {
		if (result.containsKey(key)) {
			return isEmpty(result.get(key)) ? "" : result.get(key);
		}
		return "";
	}

	/**
	 * 判断String类型是否为空
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(Object value) {
		return value == null || "".equals(value.toString())
				|| "".equals((value.toString()).trim()) || value == "null"
				|| "[]".equals(value) || "\"\"".equals(value);
	}

	/**
	 * 获取Message对象
	 * 
	 * @param code
	 * @param result
	 * @param bundleValues
	 * @return
	 */
	public static Message getMessage(int code, Object result,
			String... bundleValues) {
		Message message = new Message();
		message.what = code;
		message.obj = result;
		if (bundleValues != null) {
			int size = bundleValues.length;
			if (size > 0) {
				Bundle bundle = new Bundle();
				for (int i = 0; i < size; i++) {
					bundle.putString("arg" + i, bundleValues[i]);
				}
				message.setData(bundle);
			}
		}
		return message;
	}

	/**
	 * 判断有效的手机号码
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(147)|(15[^4,\\D])|(17[0-9])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 内容校验，只限汉字和字母
	 * 
	 * @param str
	 * @return
	 */
	public static boolean ChckText(String str, Context context) {
		// 只能输入中文和字母
		String reg = "^[\u4e00-\u9fa5a-zA-Z]+$";
		Pattern pattern = Pattern.compile(reg);
		Matcher remarkMatcher = null;
		if (!CommonFunction.isEmpty(str)) {
			remarkMatcher = pattern.matcher(str.toString());
		}
		if (remarkMatcher != null && !remarkMatcher.matches()) {
			ToastUtil.show(context, "只能输入汉字和字母");
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 正则表达式验证内容是否包含字符串
	 * 
	 * @param content
	 * @return
	 */
	public static boolean ChekIsEmpty(String content) {
		Pattern p = Pattern.compile("^[^\\s]+$");
		Matcher m = p.matcher(content);
		return m.matches();
	}

	/** 转码，UTF-8daoshi使用 **/
	public static String unescape(String s) {
		StringBuffer sbuf = new StringBuffer();
		int l = s.length();
		int ch = -1;
		int b, sumb = 0;
		for (int i = 0, more = -1; i < l; i++) {
			/* Get next byte b from URL segment s */
			switch (ch = s.charAt(i)) {
			case '%':
				ch = s.charAt(++i);
				int hb = (Character.isDigit((char) ch) ? ch - '0'
						: 10 + Character.toLowerCase((char) ch) - 'a') & 0xF;
				ch = s.charAt(++i);
				int lb = (Character.isDigit((char) ch) ? ch - '0'
						: 10 + Character.toLowerCase((char) ch) - 'a') & 0xF;
				b = (hb << 4) | lb;
				break;
			case '+':
				b = ' ';
				break;
			default:
				b = ch;
			}
			/* Decode byte b as UTF-8, sumb collects incomplete chars */
			if ((b & 0xc0) == 0x80) { // 10xxxxxx (continuation byte)
				sumb = (sumb << 6) | (b & 0x3f); // Add 6 bits to sumb
				if (--more == 0)
					sbuf.append((char) sumb); // Add char to sbuf
			} else if ((b & 0x80) == 0x00) { // 0xxxxxxx (yields 7 bits)
				sbuf.append((char) b); // Store in sbuf
			} else if ((b & 0xe0) == 0xc0) { // 110xxxxx (yields 5 bits)
				sumb = b & 0x1f;
				more = 1; // Expect 1 more byte
			} else if ((b & 0xf0) == 0xe0) { // 1110xxxx (yields 4 bits)
				sumb = b & 0x0f;
				more = 2; // Expect 2 more bytes
			} else if ((b & 0xf8) == 0xf0) { // 11110xxx (yields 3 bits)
				sumb = b & 0x07;
				more = 3; // Expect 3 more bytes
			} else if ((b & 0xfc) == 0xf8) { // 111110xx (yields 2 bits)
				sumb = b & 0x03;
				more = 4; // Expect 4 more bytes
			} else /* if ((b & 0xfe) == 0xfc) */{ // 1111110x (yields 1 bit)
				sumb = b & 0x01;
				more = 5; // Expect 5 more bytes
			}
			/* We don't test if the UTF-8 encoding is well-formed */
		}
		return sbuf.toString();
	}

	public static String getChannelName(Context activity) {
		if (activity == null) {
			return "android";
		}
		String channelName = null;
		try {
			PackageManager packageManager = activity.getPackageManager();
			if (packageManager != null) {
				// 注意此处为ApplicationInfo 而不是
				// ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
				ApplicationInfo applicationInfo = packageManager
						.getApplicationInfo(activity.getPackageName(),
								PackageManager.GET_META_DATA);
				if (applicationInfo != null) {
					if (applicationInfo.metaData != null) {
						channelName = applicationInfo.metaData
								.getString("UMENG_CHANNEL");
					}
				}
			}
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return channelName;
	}

	/**
	 * 避免重复点击
	 */
	public static long lastTimeMillis;
	public static final long MIN_CLICK_INTERVAL = 2000;// 设置重复点击间隔为2秒

	public static boolean isTimeEnabled() {
		long currentTimeMillis = System.currentTimeMillis();
		if ((currentTimeMillis - lastTimeMillis) > MIN_CLICK_INTERVAL) {
			lastTimeMillis = currentTimeMillis;
			return true;
		}
		return false;
	}

	public static void isSoftInputManager(EditText view) {
		isSoftInputManager(view, 0);
	}

	/**
	 * EditText 获得焦点，并键盘弹出
	 */
	@TargetApi(Build.VERSION_CODES.CUPCAKE)
	public static void isSoftInputManager(EditText view, int flag) {
		view.setFocusable(true);
		view.setFocusableInTouchMode(true);
		view.requestFocus();
		InputMethodManager inputManager = (InputMethodManager) view
				.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

		if (1 == flag)
			inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		else
			inputManager.showSoftInput(view, 0);
	}

	/**
	 * 点击其它地方时，隐藏键盘
	 */
	public static boolean isShouldHideInput(View v, MotionEvent event) {
		if (v != null && (v instanceof EditText)) {
			int[] leftTop = { 0, 0 };
			// 获取输入框当前的location位置
			v.getLocationInWindow(leftTop);
			int left = leftTop[0];
			int top = leftTop[1];
			int bottom = top + v.getHeight();
			int right = left + v.getWidth();
			if (event.getX() > left && event.getX() < right
					&& event.getY() > top && event.getY() < bottom) {
				// 点击的是输入框区域，保留点击EditText的事件
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断某个界面是否在前台
	 * 
	 * @param context
	 * @param className
	 *            某个界面名称
	 */
	public static boolean isForeground(Context context, String className) {
		if (context == null || isEmpty(className)) {
			return false;
		}
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(1);
		if (list != null && list.size() > 0) {
			ComponentName cpn = list.get(0).topActivity;
			if (className.equals(cpn.getClassName())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 判断当前应用程序处于前台还是后台
	 */
	public static boolean isApplicationBroughtToBackground(final Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}
		return false;
	}

	public static InputFilter getDecimalTextFilter(final int decimalDigits) {
		InputFilter lengthfilter = new InputFilter() {
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				// 删除等特殊字符，直接返回
				if ("".equals(source.toString())) {
					return null;
				}
				String dValue = dest.toString();
				String[] splitArray = dValue.split("\\.");
				if (splitArray.length > 1) {
					String dotValue = splitArray[1];
					int diff = dotValue.length() + 1 - decimalDigits;
					if (diff > 0) {
						return source.subSequence(start, end - diff);
					}
				}
				return null;
			}
		};
		return lengthfilter;
	}

}
