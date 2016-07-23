package com.chx.http;

import java.util.Map;

import okhttp3.Call;
import android.app.Activity;

import com.alibaba.fastjson.JSONObject;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

public class HttpHandler {

	private static HttpHandler handler;
	private String path = "http://yushengjun.tunnel.qydev.com/";

	public static HttpHandler getInstance() {
		if (handler == null) {
			synchronized (HttpHandler.class) {
				handler = new HttpHandler();
			}
		}
		return handler;
	}

	public void post(Activity activity, String url, Map<String, String> params,
			HttpHandlerResponse httpResponse) {
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
		}

		@Override
		public void onResponse(final String response, int id) {
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

}