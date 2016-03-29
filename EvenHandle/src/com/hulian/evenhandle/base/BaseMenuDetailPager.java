package com.hulian.evenhandle.base;
/**
 * 页面详情页
 */
import android.app.Activity;
import android.view.View;

public abstract  class BaseMenuDetailPager {	
	public Activity mActivity;
	public View mRootView;//根部局对象
	public BaseMenuDetailPager (Activity activity){
		mActivity=activity;	
		mRootView=initView();
	}

	public abstract View initView();
	//初始化数据
	public void initData(){
	}	
}
