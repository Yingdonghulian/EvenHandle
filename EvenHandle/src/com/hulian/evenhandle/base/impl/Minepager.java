package com.hulian.evenhandle.base.impl;


import java.util.ArrayList;
import com.hulian.evenhandle.base.BaseMenuDetailPager;
import com.hulian.evenhandle.base.Basepager;
import com.hulian.evenhandle.pagerdetail.MineDetail;
import com.hulian.evenhandle.pagerdetail.ServiceDetail;

import android.app.Activity;
import android.view.View;
public class Minepager extends Basepager {
	private ArrayList<BaseMenuDetailPager> mpagers;
	public Minepager(Activity activity) {
		super(activity);
	}
	@Override
	public void initData() {		
		titletext.setText("我的");// 修改标题
        setslidingMenuEnabel(false);//关闭侧拉
		btnmenu.setVisibility(View.GONE);//隐藏图标
         initView();
	}
	private void initView() {
		 mpagers =new ArrayList<BaseMenuDetailPager>();
		 mpagers.add(new MineDetail(mActivity));
		 SetCurrentMenuDetailPager(0);

	}
	/**
	 * 设置当前菜单详情页
	 */
	     public  void SetCurrentMenuDetailPager(int position){
	    	BaseMenuDetailPager pager = mpagers.get(position);//获取当前要显示的页面
	    	flconnet.removeAllViews();//清除所有
	    	flconnet.addView(pager.mRootView);	//将菜单详情页布局给帧布局
	    	pager.initData();//初始化当前页面
	     }
}
