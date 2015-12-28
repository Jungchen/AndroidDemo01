package com.tsticn.club.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;
/**
 * 使用不同的view 就把 不同的 view  实现   Pullable  
 * 继承要使用的layout
 * @author chen
 *
 */
public class PullableLayoutView extends ScrollView  implements Pullable {

	public PullableLayoutView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public PullableLayoutView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public PullableLayoutView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("NewApi")
	public PullableLayoutView(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		 
	}

	@Override
	public boolean canPullDown() {
		 if (getScrollY() == 0)  
	            return true;  
	        else  
	            return false;  
	}

	@Override
	public boolean canPullUp() {

		 if (getScrollY() >= (getChildAt(0).getHeight() - getMeasuredHeight()))  
	            return true;  
	        else  
	            return false;   

	}
}
