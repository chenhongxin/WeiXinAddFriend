package com.xingqiba.weixinaddfriend.utils;

import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.xingqiba.weixinaddfriend.R;

/**
 * @ClassName: MyURLSpan
 * @Description:(TextView带有链接的点击事件的截获)
 * @author chengbo
 * @date 2015-4-15 下午2:08:53
 */
public class MyURLSpan extends ClickableSpan {
	private MyClickListener listener;
	private Context context;

	public MyURLSpan(Context context, MyClickListener listener) {
		this.listener = listener;
		this.context = context;
	}

	public MyURLSpan() {
		super();
	}

	@Override
	public void onClick(View view) {
		if (listener != null) {
			listener.onMyClick(view);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void updateDrawState(TextPaint ds) {
		ds.setUnderlineText(false);// 去除下划线
		ds.setColor(context.getResources().getColor(R.color.topViewColor));
	}

	public interface MyClickListener {
		void onMyClick(View view);
	}
}
