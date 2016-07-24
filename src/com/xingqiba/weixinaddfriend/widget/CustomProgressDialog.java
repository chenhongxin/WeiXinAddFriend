package com.xingqiba.weixinaddfriend.widget;

import java.util.UUID;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.xingqiba.weixinaddfriend.R;

public class CustomProgressDialog extends Dialog {

    private Context context = null;
    private CustomProgressDialog customProgressDialog = null;

    private AnimationDrawable animationDrawable;

    public CustomProgressDialog(Context context) {
        super(context);
        this.context = context;
    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    public CustomProgressDialog createDialog(Context context) {
        if (customProgressDialog == null) {
            customProgressDialog = new CustomProgressDialog(context, R.style.CustomProgressDialog);
            customProgressDialog.setContentView(R.layout.progress_dialog);
            customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
            customProgressDialog.setCanceledOnTouchOutside(false);
        }
        return customProgressDialog;
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        if (customProgressDialog == null) {
            return;
        }
        ImageView imageView = (ImageView) customProgressDialog.findViewById(R.id.loading_imageview);
        animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.start();
    }

    public CustomProgressDialog getProgressDialog(Context context) {
        if (this.context == context) {
            return customProgressDialog;
        } else {
            return null;
        }
    }

    /**
     *
     * [Summary] setTitile 标题
     *
     * @param strTitle
     * @return
     *
     */
    public CustomProgressDialog setTitile(String strTitle) {
        return customProgressDialog;
    }

    /**
     *
     * [Summary] setMessage 提示内容
     *
     * @param strMessage
     * @return
     *
     */
    public CustomProgressDialog setMessage(String strMessage) {
        TextView tvMsg = (TextView) customProgressDialog.findViewById(R.id.id_tv_loadingmsg);
        if (tvMsg != null) {
            tvMsg.setText(strMessage);
        }
        return customProgressDialog;
    }

    public void startProgressDialog() {
        try {
            if (customProgressDialog == null) {
                customProgressDialog = createDialog(context);
                if (animationDrawable == null) {
                    ImageView imageView = (ImageView) customProgressDialog.findViewById(R.id.loading_imageview);
                    animationDrawable = (AnimationDrawable) imageView.getBackground();
                }
            }
            animationDrawable.start();
            customProgressDialog.show();
        } catch (Exception e) {

        }
    }

    @Override
    public void show() {
        super.show();
    }

    public void stopProgressDialog() {
        try {
            if (context != null && customProgressDialog != null) {
                if (animationDrawable != null) {
                    animationDrawable.stop();
                }
                customProgressDialog.dismiss();
            }
        } catch (Exception e) {

        }

    }

    @Override
    public void dismiss() {
        super.dismiss();
        customProgressDialog = null;
    }

    public String createUUId() {
        UUID uuid = UUID.randomUUID(); // 实际项目中只有这句有用
        return uuid.toString();
    }
}
