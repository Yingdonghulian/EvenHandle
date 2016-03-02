package com.hulian.evenhandle.menudetail;

import java.util.ArrayList;


import com.hulian.evenhandle.R;
import com.hulian.evenhandle.base.BaseMenuDetailPager;
import com.hulian.evenhandle.bean.NewsData.NewsTabData;
import com.hulian.evenhandle.detailcontent.HomeContentDetail;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
/**
 * 菜单详情页
 * @author Liu-
 *
 */
public class HomeMenuDetail extends BaseMenuDetailPager {
    private ViewPager mViewPager;
    private ArrayList<HomeContentDetail> mPagerList;
    private ArrayList<NewsTabData> HomeTabData;//页签网络数据
	private TabPageIndicator mindicator;
	public HomeMenuDetail(Activity activity, ArrayList<NewsTabData> children) {
		super(activity);
		HomeTabData=children;
	}
	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.home_news_layout, null);
		mViewPager=(ViewPager) view.findViewById(R.id.home_news);
		ViewUtils.inject(this,view);
		mindicator = (TabPageIndicator) view.findViewById(R.id.indicator);
		return view;	
	}
     @Override
    public void initData() {
    	 mPagerList=new ArrayList<HomeContentDetail>();
    	 for(int i=1;i<HomeTabData.size();i++){
    		 HomeContentDetail pager=new HomeContentDetail(mActivity,HomeTabData.get(i));
	         mPagerList.add(pager);
    	 }  	
    	 mViewPager.setAdapter(new HomePagerMenudetail());
    	 mindicator.setViewPager(mViewPager);//将viewpager和indicator结合，必须在设置完Adater之后才能调用
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
    		return HomeTabData.get(position).title;
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
			HomeContentDetail pager = mPagerList.get(position);
			container.addView(pager.mRootView);
			pager.initData();
			return pager.mRootView;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View)object);
		}
}
}
