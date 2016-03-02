package com.hulian.evenhandle;



import com.hulian.evenhandle.Fragment.ContentFragment;
import com.hulian.evenhandle.Fragment.LeftFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.Window;

public class PagerActicity extends SlidingFragmentActivity{
	
	private static final String MAINPAGER_LAYOUT= "mainpager_layout";
	private static final String LEFT_ACTIVITY= "left_activity";
       @Override
	public void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	setContentView(R.layout.main_activity);
    	setBehindContentView(R.layout.left_activity);//获取侧边栏布局
    	SlidingMenu slidingMenu=getSlidingMenu();// 获取侧边栏对象
  	    slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);// 设置全屏触摸
  	    int width=getWindowManager().getDefaultDisplay().getWidth();
  	    slidingMenu.setBehindOffset(width*200/320);
  	     initFragment();
    }
       /**
   	 * 初始化fragment, 将fragment数据填充给布局文件
   	 */
   	private void initFragment() {
   		FragmentManager  fm= getFragmentManager();
   		FragmentTransaction transaction=fm.beginTransaction();//开启事务
   	    transaction.replace(R.id.main_content, new ContentFragment(),MAINPAGER_LAYOUT);
   	    transaction.replace(R.id.left_content, new LeftFragment(),LEFT_ACTIVITY);
   	    transaction.commit();//提交事务
	   	} 
	   	//获取侧边栏对象
	   	 public LeftFragment getLeftFragment(){
	   		FragmentManager  fm= getFragmentManager();
	   		LeftFragment leftMenu= (LeftFragment) fm.findFragmentByTag(LEFT_ACTIVITY);
	   		return leftMenu;
	   	}
	  	//获取侧边栏对象
	   	 public ContentFragment getContentFragment(){
	   		FragmentManager  fm= getFragmentManager();
	   		ContentFragment leftMenu= (ContentFragment) fm.findFragmentByTag(MAINPAGER_LAYOUT);
	   		return leftMenu;   	 
	}
	   	 }
