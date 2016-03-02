package com.hulian.evenhandle.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class HorizonScrollViewpage extends ViewPager {

	public HorizonScrollViewpage(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HorizonScrollViewpage(Context context) {
		super(context);
	}
	//方法重写，事件分发 是否拦截
	 public boolean dispatchTouchEvent(MotionEvent ev){
		 if(getCurrentItem()!=0){
			 getParent().requestDisallowInterceptTouchEvent(true);//不拦截
		 }else{
			 getParent().requestDisallowInterceptTouchEvent(false);//拦截
		 }	        
			return super.dispatchTouchEvent(ev);
	}

}
