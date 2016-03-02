package com.hulian.evenhandle.Fragment;

import java.util.ArrayList;
import com.hulian.evenhandle.R;
import com.hulian.evenhandle.base.Basepager;
import com.hulian.evenhandle.base.impl.Homepager;
import com.hulian.evenhandle.base.impl.Minepager;
import com.hulian.evenhandle.base.impl.Newspager;
import com.hulian.evenhandle.base.impl.Servicepager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ContentFragment extends BaseFragment {
	@ViewInject(R.id.maintab_but)
	private RadioGroup rgGroup;
	@ViewInject(R.id.vp_main)
	private ViewPager mViewPager;
	private ArrayList<Basepager> mPagerList;

	public View initViews() {
		View view = View.inflate(mActivity, R.layout.mainpager_layout, null);
		ViewUtils.inject(this, view); // 注入VIEW和事件
		return view;
	}

	@SuppressWarnings("deprecation")
	public void initData() {
		rgGroup.check(R.id.but_home);// 默认勾选首页
		// 初始化4个子页面添加
		mPagerList = new ArrayList<Basepager>();
		mPagerList.add(new Homepager(mActivity));
		mPagerList.add(new Servicepager(mActivity));
		mPagerList.add(new Newspager(mActivity));
		mPagerList.add(new Minepager(mActivity));
		mViewPager.setAdapter(new ContentAdapter());
		// 监听BUT事件
		rgGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checedId) {
				switch (checedId) {
				case R.id.but_home:
					mViewPager.setCurrentItem(0, false);
					break;
				case R.id.but_service:
					mViewPager.setCurrentItem(1, false);
					break;
				case R.id.but_news:
					mViewPager.setCurrentItem(2, false);
					break;
				case R.id.but_mine:
					mViewPager.setCurrentItem(3, false);
					break;
				}
			}
		});

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				mPagerList.get(arg0).initData();// 获取当前被选中的页面, 初始化该页面数据
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		// mPagerList.get(0).initData();// 初始化首页数据
	}

	class ContentAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mPagerList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			Basepager pager = mPagerList.get(position);
			container.addView(pager.mRootView);
			pager.initData();// 初始化数据.... 不要放在此处初始化数据, 否则会预加载下一个页面
			return pager.mRootView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	// 获取主页面
	public Homepager getHomepager() {
		return (Homepager) mPagerList.get(0);
	}
}
