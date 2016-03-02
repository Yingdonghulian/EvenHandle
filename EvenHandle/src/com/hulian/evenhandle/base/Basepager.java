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
	public View mRootView;//���ֶ���
	public TextView titletext;//����	
	public FrameLayout flconnet;//����
    public ImageButton	btnmenu;//��ť
    public  ViewPager Vpagers;
	public Basepager( Activity activity){
		mActivity=activity;
		initViews();
		
	}
	/*
	 * ��ʼ��ҳ��
	 */
    public void initViews(){
    	mRootView = View.inflate(mActivity, R.layout.basepager_layout, null);
    	titletext=(TextView) mRootView.findViewById(R.id.titlebar_text);
    	flconnet=(FrameLayout) mRootView.findViewById(R.id.fl_content);
    	btnmenu=(ImageButton) mRootView.findViewById(R.id.btn_menu);      	
    	btnmenu.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View arg0) {
				toggleSlidingMenu();//�ر�				
			}
			private void toggleSlidingMenu() {
			 PagerActicity mainUi=(PagerActicity) mActivity;
			 SlidingMenu slidingMenu = mainUi.getSlidingMenu();
			 slidingMenu.toggle();//�л�״̬				
			}
		});

    }
			//��ʼ������
			public void initData(){	
			}
			
			 //����Slindingmenu����	
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
