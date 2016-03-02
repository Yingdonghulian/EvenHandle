package com.hulian.evenhandle.base;

import com.hulian.evenhandle.PagerActicity;
import com.hulian.evenhandle.R;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.view.annotation.event.OnChildClick;
import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

public class Basepager {

	protected Activity mActivity;
	public View mRootView;//布局对象
	public TextView titletext;//标题	
	public FrameLayout flconnet;//内容
    public ImageButton	btnmenu;//按钮
    public  ViewPager Vpagers;
	public Basepager( Activity activity){
		mActivity=activity;
		initViews();
		
	}
	/*
	 * 初始化页面
	 */
    public void initViews(){
    	mRootView = View.inflate(mActivity, R.layout.basepager_layout, null);
    	titletext=(TextView) mRootView.findViewById(R.id.titlebar_text);
    	flconnet=(FrameLayout) mRootView.findViewById(R.id.fl_content);
    	btnmenu=(ImageButton) mRootView.findViewById(R.id.btn_menu);      	
    	btnmenu.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View arg0) {
				toggleSlidingMenu();//关闭				
			}
			private void toggleSlidingMenu() {
			 PagerActicity mainUi=(PagerActicity) mActivity;
			 SlidingMenu slidingMenu = mainUi.getSlidingMenu();
			 slidingMenu.toggle();//切换状态				
			}
		});

    }
			//初始化数据
			public void initData(){	
			}
			
			 //设置Slindingmenu开关	
			    public void  setslidingMenuEnabel(boolean enable ){
			    PagerActicity MainUI =(PagerActicity) mActivity;	
			    SlidingMenu slingMenu=MainUI.getSlidingMenu();
			    
			    if(enable){
			        slingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			    }	else{
			    	 slingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
			    }
			}
}
