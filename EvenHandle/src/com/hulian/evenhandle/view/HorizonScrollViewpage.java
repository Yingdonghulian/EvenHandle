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
	//������д���¼��ַ� �Ƿ�����
	 public boolean dispatchTouchEvent(MotionEvent ev){
		 if(getCurrentItem()!=0){
			 getParent().requestDisallowInterceptTouchEvent(true);//������
		 }else{
			 getParent().requestDisallowInterceptTouchEvent(false);//����
		 }	        
			return super.dispatchTouchEvent(ev);
	}

}
