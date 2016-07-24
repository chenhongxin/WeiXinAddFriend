package com.chx.http;

import java.util.Map;

import okhttp3.Call;
import android.app.Activity;
import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.xingqiba.weixinaddfriend.widget.CustomProgressDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

public class HttpHandler {

	private static HttpHandler handler;
	private String path = "http://yushengjun.tunnel.qydev.com/";
	private boolean isShow;
	private static CustomProgressDialog dialog;

	public static HttpHandler getInstance(Context context) {
		if (handler == null) {
			synchronized (HttpHandler.class) {
				handler = new HttpHandler();
				dialog = new CustomProgressDialog(context);
			}
		}
		return handler;
	}

	public void post(Activity activity, String url, Map<String, String> params,
			HttpHandlerResponse httpResponse) {
		if(isShow && dialog != null){
			dialog.startProgressDialog();
		}
		url = path + url;
		OkHttpUtils.post().url(url).params(params).build()
				.execute(new HttpCallback(activity, httpResponse));
	}

	class HttpCallback extends StringCallback {

		HttpHandlerResponse httpResponse;
		Activity activity;

		public HttpCallback(Activity activity, HttpHandlerResponse httpResponse) {
			this.activity = activity;
			this.httpResponse = httpResponse;
		}

		@Override
		public void onError(Call call, Exception e, int id) {
			httpResponse.onFaile(e.getMessage() + "");
			dialog.stopProgressDialog();
		}

		@Override
		public void onResponse(final String response, int id) {
			dialog.stopProgressDialog();
			if (activity != null) {
				activity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						try {
							JSONObject object = JSONObject.parseObject(response
									+ "");
							String data = object.getString("data");
							httpResponse.onSuccess(data);
						} catch (Exception e) {
							e.printStackTrace();
							httpResponse.onFaile(e.getMessage() + "");
						}
					}
				});
			}
		}

	}

	public interface HttpHandlerResponse {
		public void onSuccess(String data);

		public void onFaile(String data);
	}

	public static class Builder{
		private boolean isShow;
		
		public Builder(){
			
		}
		
		public Builder setShow(boolean isShow) {
			this.isShow = isShow;
			return this;
		}
		
		public HttpHandler build(Context context){
			HttpHandler handler = HttpHandler.getInstance(context);
			handler.isShow = isShow;
			return handler;
		}
	}
	
}