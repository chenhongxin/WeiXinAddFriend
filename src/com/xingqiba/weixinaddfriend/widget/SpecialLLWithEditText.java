package com.xingqiba.weixinaddfriend.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xingqiba.weixinaddfriend.R;
import com.xingqiba.weixinaddfriend.utils.DisplayUtil;
import com.xingqiba.weixinaddfriend.utils.MyInputFilter;

/**
 * @ClassName: SpecialLLWithEditText
 * @Description:(特殊的带有EditText框的控件)
 * @author chengbo
 * @date 2015-4-13 下午5:53:39
 */
public class SpecialLLWithEditText extends LinearLayout implements
		OnClickListener {
	private Context context;
	private LayoutInflater inflater;
	private View topLineView;
	private ImageView img_LeftImage;
	private ImageView img_del;
	private TextView tv_leftTitle;
	private EditText et_value;
	private SpecialButton btn_comm;
	private ImageView img_rightBtn;
	private View bottomLineView;
	private MyBtnClickListener btnListener;
	private MyImgClickListener imgListener;
	private MyChangeTextListener changeTextListener;
	private boolean isSelected = false;
	private boolean editable = true;
	private boolean isNeedShowDel;
	private boolean isChecked;

	private Drawable rightImageSelected;
	private Drawable rightImage;
	private String digits = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#￥*";
	private String phoneDigits = "0123456789";

	public SpecialLLWithEditText(Context context) {
		super(context);
		this.context = context;
		init(null);
	}

	public SpecialLLWithEditText(Context context, AttributeSet set) {
		super(context, set);
		this.context = context;
		init(set);
	}

	/**
	 * 初始化属性
	 * 
	 * @param set
	 */
	private void init(AttributeSet set) {
		if (set != null) {
			TypedArray array = context.obtainStyledAttributes(set,
					R.styleable.special_ll_with_eidttext);
			if (array != null) {
				int background = array.getColor(
						R.styleable.special_ll_with_eidttext_background,
						Color.WHITE);
				int editInputType = array.getInt(
						R.styleable.special_ll_with_eidttext_editInputType, -1);
				int gravity = array.getInt(
						R.styleable.special_ll_with_eidttext_gravity, -1);
				int editLengths = array.getInt(
						R.styleable.special_ll_with_eidttext_editLength, -1);
				Drawable leftImage = array
						.getDrawable(R.styleable.special_ll_with_eidttext_leftImage);
				String leftTitle = array
						.getString(R.styleable.special_ll_with_eidttext_leftTitle);
				@SuppressWarnings("deprecation")
				int leftTitleColor = array.getColor(
						R.styleable.special_ll_with_eidttext_leftTitleColor,
						getResources().getColor(R.color.specialTitleColor));
				float leftTitleSize = array
						.getDimension(
								R.styleable.special_ll_with_eidttext_leftTitleSize,
								36f);
				String editText = array
						.getString(R.styleable.special_ll_with_eidttext_editText);
				String editHint = array
						.getString(R.styleable.special_ll_with_eidttext_editHint);
				int editTextColor = array.getColor(
						R.styleable.special_ll_with_eidttext_editTextColor,
						R.color.letter_grey_deep_11);
				float editTextSize = array.getDimension(
						R.styleable.special_ll_with_eidttext_editTextSize, 32f);

				rightImage = array
						.getDrawable(R.styleable.special_ll_with_eidttext_rightImage);
				rightImageSelected = array
						.getDrawable(R.styleable.special_ll_with_eidttext_rightImageSelected);
				String rightButtonText = array
						.getString(R.styleable.special_ll_with_eidttext_rightButtonText);
				float rightButtonTextSize = array
						.getDimension(
								R.styleable.special_ll_with_eidttext_rightButtonTextSize,
								16);
				int rightButtonTextColor = array
						.getColor(
								R.styleable.special_ll_with_eidttext_rightButtonTextColor,
								-1);
				boolean hasTopLine = array.getBoolean(
						R.styleable.special_ll_with_eidttext_topLine, true);
				isNeedShowDel = array.getBoolean(
						R.styleable.special_ll_with_eidttext_isNeedShowDel,
						false);
				boolean hasBottomLine = array.getBoolean(
						R.styleable.special_ll_with_eidttext_bottomLine, true);
				editable = array.getBoolean(
						R.styleable.special_ll_with_eidttext_editable, true);
				inflater = LayoutInflater.from(context);
				View parent = inflater.inflate(
						R.layout.layout_spcial_with_edittext, this);
				topLineView = parent.findViewById(R.id.topLine);
				img_LeftImage = (ImageView) parent
						.findViewById(R.id.img_leftImage);
				img_del = (ImageView) parent.findViewById(R.id.img_del);
				tv_leftTitle = (TextView) parent
						.findViewById(R.id.tv_leftTitle);
				et_value = (EditText) parent.findViewById(R.id.et_value);
				btn_comm = (SpecialButton) parent.findViewById(R.id.btn_common);
				img_rightBtn = (ImageView) parent
						.findViewById(R.id.img_nextIcon);
				bottomLineView = parent.findViewById(R.id.bottomLine);

				parent.setBackgroundColor(background);
				setVisibleOrHide(img_LeftImage, getFlag(leftImage));
				setVisibleOrHide(tv_leftTitle, getFlag(leftTitle));
				setVisibleOrHide(btn_comm, getFlag(rightButtonText));
				setVisibleOrHide(img_rightBtn, getFlag(rightImage));
				setVisibleOrHide(topLineView, getFlag(hasTopLine));
				setVisibleOrHide(bottomLineView, getFlag(hasBottomLine));

				img_LeftImage.setImageDrawable(leftImage);

				tv_leftTitle.setText(leftTitle);
				leftTitleSize = DisplayUtil.px2dip(context, leftTitleSize);
				tv_leftTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,
						leftTitleSize);
				tv_leftTitle.setTextColor(leftTitleColor);
				InputFilter[] filters = new InputFilter[1];
				if (editInputType == 4 || editInputType == 5) {
					filters[0] = new MyInputFilter(digits);
				} else if (editInputType == 2) {
					filters[0] = new MyInputFilter(phoneDigits);
				}
				if (editInputType != -1) {// 设置输入类型
					if (editInputType == 4) {
						et_value.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
						et_value.setFilters(filters);
					} else if (editInputType == 5) {
						et_value.setInputType(InputType.TYPE_CLASS_TEXT
								| InputType.TYPE_TEXT_VARIATION_PASSWORD);
						et_value.setFilters(filters);
					} else if (editInputType == 2 || editInputType == 6) {
						et_value.setInputType(InputType.TYPE_CLASS_NUMBER);
						et_value.setFilters(filters);
					} else {
						et_value.setInputType(editInputType);
					}
				}
				if (editLengths != -1) {// 设置文本长度
					et_value.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
							editLengths) });
				}
				if (editInputType == 6) {// 给
					et_value.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
							12) });
				}
				et_value.setText(editText);
				et_value.setHint(editHint);
				if (gravity == 2) {// 2:为右对齐
					et_value.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
				}
				et_value.setOnFocusChangeListener(new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View arg0, boolean arg1) {
						isChecked = arg1;
						if (isNeedShowDel) {
							if (TextUtils.isEmpty(et_value.getText())
									&& arg1) {
								img_del.setVisibility(View.GONE);
							} else if (!TextUtils.isEmpty(et_value.getText()) && arg1) {
								img_del.setVisibility(View.VISIBLE);
							} else if (!TextUtils.isEmpty(et_value.getText()) && !arg1) {
								img_del.setVisibility(View.GONE);
							}
						} else {
							img_del.setVisibility(View.GONE);
						}
					}
				});
				editTextSize = DisplayUtil.px2sp(context, editTextSize);
				et_value.setTextSize(TypedValue.COMPLEX_UNIT_SP, editTextSize);
				et_value.setTextColor(editTextColor);
				et_value.addTextChangedListener(new MyTextWatcher());

				if (editable) {
					et_value.setFocusable(true);
					et_value.setFocusableInTouchMode(true);
				} else {
					et_value.setFocusable(false);
					et_value.setFocusableInTouchMode(false);
				}
				et_value.setEnabled(editable);

				// 设置光标位置
				CharSequence text = et_value.getText();
				if (text instanceof Spannable) {
					Spannable spanText = (Spannable) text;
					Selection.setSelection(spanText, text.length());
				}

				btn_comm.setText(rightButtonText);
				// btn_comm.setBackgroundColor(rightButtonBg);
				btn_comm.setTextColor(rightButtonTextColor);
				rightButtonTextSize = DisplayUtil.px2sp(context,
						rightButtonTextSize);
				btn_comm.setTextSize(TypedValue.COMPLEX_UNIT_SP,
						rightButtonTextSize);

				img_rightBtn.setImageDrawable(rightImage);

				btn_comm.setOnClickListener(this);
				img_rightBtn.setOnClickListener(this);
				img_del.setOnClickListener(this);

				array.recycle();
			}
		}
	}

	public class MyTextWatcher implements TextWatcher {

		@Override
		public void afterTextChanged(Editable arg0) {
			String result = arg0.toString();
			if (changeTextListener != null) {
				boolean isNull = false;
				if (TextUtils.isEmpty(result)) {
					isNull = true;
					if (isNeedShowDel) {
						img_del.setVisibility(View.GONE);
					}
				} else {
					if (isNeedShowDel && isChecked) {
						img_del.setVisibility(View.VISIBLE);
					} else {
						img_del.setVisibility(View.GONE);
					}
				}
				changeTextListener.afterTextChange(result, isNull);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {

		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {

		}

	}

	private boolean getFlag(Object object) {
		if (object == null) {
			return false;
		} else {
			if (object instanceof String) {
				if (TextUtils.isEmpty((String) object)) {
					return false;
				}
			}
			if (object instanceof Boolean) {
				return (Boolean) object;
			}
			return true;
		}

	}

	private void setVisibleOrHide(View view, boolean flag) {
		if (flag) {
			view.setVisibility(View.VISIBLE);
		} else {
			view.setVisibility(View.GONE);
		}
	}

	/**
	 * 获取EditText的值
	 * 
	 * @return
	 */
	public String getText() {
		if (et_value != null)
			return et_value.getText().toString();
		else
			return "";
	}

	public void setText(String text) {
		if (et_value != null) {
			et_value.setText(text);
			et_value.setSelection(et_value.getText().length());
		}
	}
	public EditText getEditText(){
		return et_value;
	}

	public void setBtnText(String text) {
		btn_comm.setText(text);
	}

	public void setRightImage(Drawable drawable) {
		img_rightBtn.setVisibility(View.VISIBLE);
		img_rightBtn.setImageDrawable(drawable);
	}

	public void setOnBtnClickListener(MyBtnClickListener listener) {
		this.btnListener = listener;
	}

	public void setOnImgClickListener(MyImgClickListener listener) {
		this.imgListener = listener;
	}

	public void setMyChangeTextListener(MyChangeTextListener listener) {
		this.changeTextListener = listener;
	}

	public interface MyBtnClickListener {
		void onBtnClick(View view);
	}

	public interface MyImgClickListener {
		void onImgClick(View view, boolean isSelected);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_common:
			if (btnListener != null) {
				btnListener.onBtnClick(arg0);
			}
			break;
		case R.id.img_nextIcon:
			isSelected = !isSelected;
			if (imgListener != null) {
				if (isSelected) {
					if (rightImageSelected != null)
						img_rightBtn.setImageDrawable(rightImageSelected);
				} else {
					if (rightImage != null)
						img_rightBtn.setImageDrawable(rightImage);
				}
				imgListener.onImgClick(arg0, isSelected);
			}
			break;
		case R.id.img_del:
			et_value.setText("");
			break;
		}
	}

	/**
	 * 设置密码的状态
	 */
	public void changePwdVisible() {
		if (isSelected) {
			et_value.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		} else {
			et_value.setInputType(InputType.TYPE_CLASS_TEXT
					| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		}
		et_value.setSelection(et_value.getText().length());
	}

	public interface MyChangeTextListener {
		void afterTextChange(String text, boolean isNull);
	}
}
