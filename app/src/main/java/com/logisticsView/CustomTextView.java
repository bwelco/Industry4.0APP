package com.logisticsView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class CustomTextView extends TextView {
	/**
	 * TextView的文本高度
	 */
	private float txtHeight;
	
	public CustomTextView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public CustomTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("FloatMath")
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		txtHeight = getMeasuredHeight();

	}

	/**
	 * 获取TextView的文本高度
	 * @return
	 */
	public float getTxtHeight() {
		return txtHeight;
	}
	
	/**
	 * 获取父控件的顶部内边距
	 * @return
	 */
	public int getParentPaddingTop(){
		return ((View)this.getParent()).getPaddingTop();
	}
	
	/**
	 * 获取父控件的底部内边距
	 * @return
	 */
	public int getParentPaddingBotton(){
		return ((View)this.getParent()).getPaddingBottom();
	}
}
