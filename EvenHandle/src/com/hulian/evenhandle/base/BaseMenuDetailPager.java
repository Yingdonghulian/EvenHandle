package com.hulian.evenhandle.base;
/**
 * ҳ������ҳ
 */
import android.app.Activity;
import android.view.View;

public abstract  class BaseMenuDetailPager {	
	public Activity mActivity;
	public View mRootView;//�����ֶ���
	public BaseMenuDetailPager (Activity activity){
		mActivity=activity;	
		mRootView=initView();
	}

	public abstract View initView();
	//��ʼ������
	public void initData(){
	}	
}
