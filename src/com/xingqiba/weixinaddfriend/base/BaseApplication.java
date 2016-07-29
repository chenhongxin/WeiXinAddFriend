package com.xingqiba.weixinaddfriend.base;

import com.xingqiba.weixinaddfriend.model.response.UserResponse;

import android.app.Application;

public class BaseApplication extends Application {

	static BaseApplication application;
	UserResponse userResponse;

	@Override
	public void onCreate() {
		super.onCreate();
		application = this;
	}

	public static BaseApplication getInstance() {
		return application;
	}

	public void setUserResponse(UserResponse userResponse) {
		this.userResponse = userResponse;
	}

	public UserResponse getUserResponse() {
		return userResponse;
	}

}