package com.hulian.evenhandle.Fragment;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.zip.Inflater;

import com.hulian.evenhandle.PagerActicity;
import com.hulian.evenhandle.R;
import com.hulian.evenhandle.base.Basepager;
import com.hulian.evenhandle.base.impl.Homepager;
import com.hulian.evenhandle.base.impl.Newspager;
import com.hulian.evenhandle.bean.NewsData;
import com.hulian.evenhandle.bean.NewsData.NewsMenuData;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnChildClick;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import junit.runner.BaseTestRunner;

public class LeftFragment extends BaseFragment {

	@ViewInject(R.id.lv_list)
	private ListView lvlist;
	private ArrayList<NewsMenuData> mMenuList;
	private int mcurrentPos;
	private MenuAdapter mAdapter;
	@Override
	public View initViews() {
		// TODO Auto-generated method stub
		View view = View.inflate(mActivity, R.layout.left_menu, null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void initData() {
		lvlist.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		     mcurrentPos=position;
		     mAdapter.notifyDataSetChanged();//刷新 完了调用GETView
	        SetCurrentMenuDetailPager(position);
	        toggleSlidingMenu();//关闭
			}
      /**
       * 切换slidingMenu
       * @param show
       */
			private void toggleSlidingMenu() {
				 PagerActicity mainUi=(PagerActicity) mActivity;
				 SlidingMenu slidingMenu = mainUi.getSlidingMenu();
				 slidingMenu.toggle();//切换状态
				
			}

			private void SetCurrentMenuDetailPager(int position) {
				 PagerActicity mainUi=(PagerActicity) mActivity;
				 ContentFragment fragment = mainUi.getContentFragment();//获取主页面fragment
				Homepager pager = fragment.getHomepager();//获取主页面
				 pager.SetCurrentMenuDetailPager(position);
				
			}
	});
	}

			// 设置网络数据
			public void setMenuData(NewsData data) {
				mMenuList = data.data;
				mAdapter = new MenuAdapter();
				lvlist.setAdapter(mAdapter);
			}
				   /**
				    * 侧边栏适配器
				    * @author Liu-
				    *
				    */
			class MenuAdapter extends BaseAdapter {
				@Override
				public int getCount() {
					return mMenuList.size();
				}
		
				@Override
				public NewsMenuData getItem(int position) {
					return mMenuList.get(position);
				}
		
				@Override
				public long getItemId(int position) {
					return position;
				}
		
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					View view = View.inflate(mActivity, R.layout.left_list_menu, null);
					TextView tvMenu = (TextView) view.findViewById(R.id.tv_list);
					NewsMenuData newsMenuData = getItem(position);
					tvMenu.setText(newsMenuData.title);
					
					if(mcurrentPos==position){//被选中显示红色
						tvMenu.setEnabled(true);
					}else{//不被选中
						tvMenu.setEnabled(false);
					}
					return view;
				}
			}

}
