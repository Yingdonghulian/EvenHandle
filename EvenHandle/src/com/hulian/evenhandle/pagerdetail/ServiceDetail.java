package com.hulian.evenhandle.pagerdetail;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import com.hulian.evenhandle.R;
import com.hulian.evenhandle.ServicePhoto.MainActivity;
import com.hulian.evenhandle.base.BaseMenuDetailPager;
import com.hulian.evenhandle.detailcontent.NewsDetailActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnChildClick;
import com.viewpagerindicator.TabPageIndicator;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class ServiceDetail extends BaseMenuDetailPager implements OnClickListener{
	    private ViewPager mViewPager;
	    private static final String[] CONTENT = new String[] { "社区管理", "社区服务" };
	    private ArrayList<ServiceDetail> mPagerList;
		private TabPageIndicator mindicator;
		private View but1;
		

		public ServiceDetail(Activity activity, int length) {
			super(activity);		
		}
		@Override
		public View initView() {
			View view = View.inflate(mActivity, R.layout.news_detail, null);
			mViewPager=(ViewPager) view.findViewById(R.id.id_viewpager);
			ViewUtils.inject(this,view);	
			mindicator = (TabPageIndicator) view.findViewById(R.id.id_indicator);	
			return view;	
		}
	     @Override
	    public void initData() {
	    	 mPagerList=new ArrayList<ServiceDetail>();	    	
	    	 for(int i=0;i<CONTENT.length;i++){
	    	 ServiceDetail pager=new ServiceDetail(mActivity,CONTENT.length);    	 
		     mPagerList.add(pager);		    
	    	 }  	    	
	    	 mViewPager.setAdapter(new HomePagerMenudetail());
	         mindicator.setViewPager(mViewPager);//将viewpager和indicator结合，必须在设置完Adater之后才能调用       
	    	 mindicator.setVisibility(View.VISIBLE);

	    }        
	         //适配器
	        class HomePagerMenudetail extends PagerAdapter{                
	     	 //重写此方法，返回页面标签
	     	 @Override
	     	public CharSequence getPageTitle(int position) {
	     		return CONTENT[position % CONTENT.length].toUpperCase();
	     	}	 		
	     	public HomePagerMenudetail() {
			}
	 		@Override
	 		public int getCount() {
	 			return mPagerList.size();
	 		}
	 		@Override
	 		public boolean isViewFromObject(View arg0, Object arg1) {
	 			return arg0==arg1;
	 		}
	 		@Override
	 		public Object instantiateItem(ViewGroup container, int position) {	
	 	        ServiceDetail pager = mPagerList.get(position);
	 			if(position==0){
	 				View ServiceManagement = View.inflate(mActivity, R.layout.servicemangent, null);	
	 				com.hulian.evenhandle.util.ImageButtonDefine but1=(com.hulian.evenhandle.util.ImageButtonDefine) ServiceManagement.findViewById(R.id.but_repair);
	 				but1.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							Intent intent=new Intent();
							intent.setClass(mActivity, MainActivity.class);
							mActivity.startActivity(intent);
						}
					});
	 				container.addView(ServiceManagement, 0);	
	 				
	 				return ServiceManagement;
	 			  }else{
		 			View Service=View.inflate(mActivity, R.layout.lay1,null);
		 			container.addView(Service, 1);
					return Service;
		 		}
	 			}
			@Override
	 		public void destroyItem(ViewGroup container, int position, Object object) {
	 			container.removeView((View)object);
	 		}
	 }
			@Override
			public void onClick(View v) {
			}
			
	}