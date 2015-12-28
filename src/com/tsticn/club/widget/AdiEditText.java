package com.tsticn.club.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

public class AdiEditText extends EditText {

	private final String TAG = "AdiEditText";
	private Drawable dRight;
	private Drawable dLeft;
	private Rect rBounds;

	// 构造器
	public AdiEditText(Context paramContext) {
		super(paramContext);
		initEditText();
	}

	public AdiEditText(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		initEditText();
	}

	public AdiEditText(Context paramContext, AttributeSet paramAttributeSet,
			int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
		initEditText();
	}

	// 初始化edittext 控件
	private void initEditText() {
//		setEditTextDrawable();
		//先让右边的删除图片置空
		setCompoundDrawables(this.dLeft, null, null, null);
		//在获取到焦点的输入框中判断
		setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				AdiEditText.this.setEditTextHasFocus();
				addTextChangedListener(new TextWatcher() { // 对文本内容改变进行监听
					public void afterTextChanged(Editable paramEditable) {
					}

					public void beforeTextChanged(CharSequence paramCharSequence,
							int paramInt1, int paramInt2, int paramInt3) {
					}

					public void onTextChanged(CharSequence paramCharSequence,
							int paramInt1, int paramInt2, int paramInt3) {
						AdiEditText.this.setEditTextDrawable();
					}
				});
			}
		});
		
	}

	// 控制图片的显示
	private void setEditTextDrawable() {
		if (getText().toString().length() == 0) {
			setCompoundDrawables(this.dLeft, null, null, null);
		} else {
			setCompoundDrawables(this.dLeft, null, this.dRight, null);
		}
	}
	// 控制图片的显示
	private void setEditTextHasFocus() {
		if(hasFocus()){
			setCompoundDrawables(this.dLeft, null, this.dRight, null);
		 }else{
			 setCompoundDrawables(this.dLeft, null, null, null);
		 }
		}
	protected void finalize() throws Throwable {
		super.finalize();
		this.dRight = null;
		this.rBounds = null;
	}

	// 添加触摸事件
	public boolean onTouchEvent(MotionEvent paramMotionEvent) {
		//取到输入框的图片信息
		Drawable[] 	drawables = AdiEditText.this.getCompoundDrawables();
		if ((drawables[2] != null) && (paramMotionEvent.getAction() == 1)) {
			this.rBounds = this.dRight.getBounds();
			int i = (int) paramMotionEvent.getX() + getLeft();
			if (i > getRight() - this.rBounds.width()) {

				setText("");
				paramMotionEvent.setAction(MotionEvent.ACTION_CANCEL);
			}
		}
		return super.onTouchEvent(paramMotionEvent);
	}

	// 设置显示的图片资源
	public void setCompoundDrawables(Drawable paramDrawable1,
			Drawable paramDrawable2, Drawable paramDrawable3,
			Drawable paramDrawable4) {
		if (paramDrawable3 != null)
			this.dRight = paramDrawable3;
		if (paramDrawable1 != null)
			this.dLeft = paramDrawable1;
		super.setCompoundDrawables(paramDrawable1, paramDrawable2,
				paramDrawable3, paramDrawable4);
	}
}