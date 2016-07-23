package com.xingqiba.weixinaddfriend.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.widget.Button;

import com.xingqiba.weixinaddfriend.R;

/**
 * @ClassName: SpecialButton
 * @Description:(特殊按钮封装可以设置圆角以及按钮的状态变化的颜色)
 * @author chengbo
 * @date 2015-4-3 上午11:14:32
 */
public class SpecialButton extends Button {
	private Context context;
	private float ANGLE_TOP_LEFT = 0;// 左上角圆角弧度
	private float ANGLE_TOP_RIGHT = 0;// 右上角圆角弧度
	private float ANGLE_BOTTOM_LEFT = 0;// 左下角圆角弧度
	private float ANGLE_BOTTOM_RIGHT = 0;// 右上角圆角弧度
	private float ANGLE_ALL = 0;// 四个角的弧度
	private int BG_UNCLICKED = -1;// 按钮未按下的颜色
	private int BG_CLICKED = -1;// 按钮按下的颜色
	private boolean ISNEEDCHECK = false;

	public SpecialButton(Context context) {
		super(context);
		this.context = context;
		init(null);
	}

	public SpecialButton(Context context, AttributeSet set) {
		super(context, set);
		this.context = context;
		init(set);
	}

	@SuppressWarnings("deprecation")
	private void init(AttributeSet set) {
		if (set != null) {
			TypedArray array = context.obtainStyledAttributes(set,
					R.styleable.SPECIAL_BUTTON);
			ANGLE_ALL = array.getDimension(
					R.styleable.SPECIAL_BUTTON_angle_all, 0f);
			ANGLE_TOP_LEFT = array.getDimension(
					R.styleable.SPECIAL_BUTTON_angle_top_left, 0f);
			ANGLE_TOP_RIGHT = array.getDimension(
					R.styleable.SPECIAL_BUTTON_angle_top_right, 0f);
			ANGLE_BOTTOM_LEFT = array.getDimension(
					R.styleable.SPECIAL_BUTTON_angle_bottom_left, 0f);
			ANGLE_BOTTOM_RIGHT = array.getDimension(
					R.styleable.SPECIAL_BUTTON_angle_bottom_right, 0f);
			BG_UNCLICKED = array.getColor(R.styleable.SPECIAL_BUTTON_unclicked,
					getResources().getColor(R.color.btnStartUnClicked));
			BG_CLICKED = array.getColor(R.styleable.SPECIAL_BUTTON_clicked,
					getResources().getColor(R.color.btnStartClicked));
			ISNEEDCHECK = array.getBoolean(
					R.styleable.SPECIAL_BUTTON_isNeedCheck, false);
			// 设置背景形状
			setAngles(R.color.btnStartUnClicked, R.color.btnStartClicked);
			array.recycle();
		}
	}

	public void setIsNeedCheck(boolean isNeedCheck) {
		this.ISNEEDCHECK = isNeedCheck;
		setAngles(R.color.btnStartUnClicked, R.color.btnStartClicked);
	}

	/**
	 * 设置背景形状以及状态颜色
	 */
	@SuppressWarnings("deprecation")
	private void setAngles(int clickColor, int unClickColor) {
		float[] outerAngle = null;
		if (ANGLE_ALL != 0) {
			ANGLE_TOP_LEFT = ANGLE_ALL;
			ANGLE_TOP_RIGHT = ANGLE_ALL;
			ANGLE_BOTTOM_LEFT = ANGLE_ALL;
			ANGLE_BOTTOM_RIGHT = ANGLE_ALL;
		}
		outerAngle = new float[] { ANGLE_TOP_LEFT, ANGLE_TOP_LEFT,
				ANGLE_TOP_RIGHT, ANGLE_TOP_RIGHT, ANGLE_BOTTOM_LEFT,
				ANGLE_BOTTOM_LEFT, ANGLE_BOTTOM_RIGHT, ANGLE_BOTTOM_RIGHT };

		RoundRectShape roundRectShape = new RoundRectShape(outerAngle, null,
				null);
		ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
		ShapeDrawable shapeDrawabled = new ShapeDrawable(roundRectShape);
		if (!ISNEEDCHECK) {
			shapeDrawable.getPaint().setColor(BG_UNCLICKED);
			shapeDrawabled.getPaint().setColor(BG_CLICKED);
		} else {
			shapeDrawable.getPaint().setColor(
					getResources().getColor(clickColor));
			shapeDrawabled.getPaint().setColor(
					getResources().getColor(unClickColor));
		}
		StateListDrawable stateListDrawable = new StateListDrawable();
		int pressed = android.R.attr.state_pressed;
		stateListDrawable.addState(new int[] { pressed }, shapeDrawabled);
		stateListDrawable.addState(new int[] { -pressed }, shapeDrawable);
		stateListDrawable.addState(new int[] {}, shapeDrawable);
		Drawable drawable = stateListDrawable;
		drawable.setCallback(this);
		setBackgroundDrawable(drawable);
	}

	public void setBackGround(int clickColor, int unClickColor) {
		ISNEEDCHECK = true;
		setAngles(clickColor, unClickColor);
	}

}
