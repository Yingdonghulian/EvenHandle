package com.hulian.evenhandle.pagerdetail;

import java.util.ArrayList;
import java.util.List;

import com.hulian.evenhandle.R;
import com.hulian.evenhandle.base.BaseMenuDetailPager;
import com.hulian.evenhandle.bean.NewsData.NewsTabData;
import com.hulian.evenhandle.detailcontent.HomeContentDetail;
import com.hulian.evenhandle.util.DensityUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsDetail extends BaseMenuDetailPager{
	    private ViewPager mViewPager;
	    private static final String[] CONTENT = new String[] { "社区通知", "@我" };
	    private ArrayList<NewsDetail> mPagerList;
		private TabPageIndicator mindicator;
		public NewsDetail(Activity activity, int length) {
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
	    	 mPagerList=new ArrayList<NewsDetail>();
	    	 for(int i=0;i<CONTENT.length;i++){
	    	 NewsDetail pager=new NewsDetail(mActivity,CONTENT.length);
		     mPagerList.add(pager);
	    	 }  	
	    	 mViewPager.setAdapter(new HomePagerMenudetail());
	    	 mindicator.setViewPager(mViewPager);//将viewpager和indicator结合，必须在设置完Adater之后才能调用
	    	 mindicator.setVisibility(View.VISIBLE);
	    }
	         @OnClick(R.id.news_cate)
	         public void NextPager(View view){
	    	  int CurrentItem= mViewPager.getCurrentItem();
	    	  mViewPager.setCurrentItem(++CurrentItem);
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
	 		//	 container.addView(mPagerList.get());//添加页卡  
	             return mPagerList.get(position);  
	 		}
	 		@Override
	 		public void destroyItem(ViewGroup container, int position, Object object) {
	 			container.removeView((View)object);
	 		}
	 }
		
	}