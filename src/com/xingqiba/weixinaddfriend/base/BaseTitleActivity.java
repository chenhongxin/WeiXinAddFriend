package com.xingqiba.weixinaddfriend.base;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xingqiba.weixinaddfriend.R;
import com.xingqiba.weixinaddfriend.utils.MyURLSpan;
import com.xingqiba.weixinaddfriend.utils.MyURLSpan.MyClickListener;

/**
 * Created by wangtao on 16/1/19.
 */
public abstract class BaseTitleActivity extends BaseActivity implements View.OnClickListener {

    protected TextView titleText;
    protected LinearLayout backBtn;
    protected TextView rightText;
    protected ImageView rightBtn;

    @Override
    public void initView() {
        titleText = (TextView) findViewById(R.id.activity_title_text);
        backBtn = (LinearLayout) findViewById(R.id.activity_back_btn);

        rightText = (TextView) findViewById(R.id.activity_right_text);
        rightBtn = (ImageView) findViewById(R.id.activity_right_btn);
        backBtn.setOnClickListener(this);
        setTitle();
        initControl();
        widgetClick();
    }

    public void setTitle(String title) {
        titleText.setText(title);
    }
    
    public void setTitle(int title) {
        titleText.setText(getResources().getString(title));
    }
    
    public void setRightText(String right){
    	rightText.setText(right);
    	rightText.setVisibility(View.VISIBLE);
    	rightBtn.setVisibility(View.VISIBLE);
    }

    public void showBack(){
    	backBtn.setVisibility(View.VISIBLE);
    }
    
    @SuppressWarnings("deprecation")
	public void handleTextViewURl(String text, TextView view) {
		int start = 0;
		int end = text.length();
		SpannableString spannableInfo = new SpannableString(text);
		spannableInfo.setSpan(new MyURLSpan(context, new MyClickListener() {
			
			@Override
			public void onMyClick(View view) {
				widgetClick(view);
			}
		}){}, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		view.append(spannableInfo);
		view.setHighlightColor(getResources().getColor(android.R.color.transparent));
		view.setMovementMethod(LinkMovementMethod.getInstance());
	}
    
    @Override
    public void onClick(View view) {
    	widgetClick(view);
        switch (view.getId()) {
            case R.id.activity_back_btn: {
                finish();
            }
            break;
        }
    }
    
    public abstract void setTitle();
    public abstract void initControl();
    public abstract void widgetClick();
    public abstract void widgetClick(View view);
    
}