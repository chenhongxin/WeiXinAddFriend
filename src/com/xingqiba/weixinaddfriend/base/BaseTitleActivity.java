package com.xingqiba.weixinaddfriend.base;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xingqiba.weixinaddfriend.R;

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

    public void showBack(){
    	backBtn.setVisibility(View.VISIBLE);
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