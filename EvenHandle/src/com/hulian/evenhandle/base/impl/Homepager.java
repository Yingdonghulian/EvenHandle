package com.hulian.evenhandle.base.impl;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.hulian.evenhandle.PagerActicity;
import com.hulian.evenhandle.Fragment.LeftFragment;
import com.hulian.evenhandle.base.BaseMenuDetailPager;
import com.hulian.evenhandle.base.Basepager;
import com.hulian.evenhandle.bean.NewsData;
import com.hulian.evenhandle.bean.NewsData.NewsMenuData;
import com.hulian.evenhandle.global.GlobalConnect;
import com.hulian.evenhandle.menudetail.ActivityMenuDetail;
import com.hulian.evenhandle.menudetail.HomeMenuDetail;
import com.hulian.evenhandle.menudetail.PlayMenuDetail;
import com.hulian.evenhandle.menudetail.TopicMenuDetail;
import com.hulian.evenhandle.util.CacheUtils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Homepager extends Basepager {
	private ArrayList<BaseMenuDetailPager> mpagers;
	private NewsData mMenudata;
	public Homepager(Activity activity) {
		super(activity);
	}
	@Override
	public void initData() {
		titletext.setText("新闻中心");// 修改标题
		setslidingMenuEnabel(true);			 
	    String cache = CacheUtils.getCache(GlobalConnect.CATEGORIES_URL, mActivity);
		if(!TextUtils.isEmpty(cache)){//如果缓存存在直接解析 不用调用接口
		     parseData(cache);
		}
		   getDataFromServer();//每次都获取数据
	}
	/**
	 * 从服务器获取数据
	 */
	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, GlobalConnect.CATEGORIES_URL, new RequestCallBack<String>() {

			// 成功访问
			@Override
			public void onSuccess(ResponseInfo responseInfo) {
				String result = (String) responseInfo.result;
				System.out.println("返回值" + result);
  		        parseData(result);
  		        //设置缓存
  		       CacheUtils.setCache(GlobalConnect.CATEGORIES_URL, result, mActivity);
			}

//			 访问失败
			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(mActivity, msg, Toast.LENGTH_LONG).show();
				error.printStackTrace();
			}
		});
	}
		   //解析数据
			protected void parseData(String result) {
				Gson gson=new Gson();
				mMenudata = gson.fromJson(result, NewsData.class);
				System.out.println("解析后"+mMenudata); 
								
				//刷新侧边栏数据
			    PagerActicity mainUi=(PagerActicity) mActivity;
			    LeftFragment leftfragment= mainUi.getLeftFragment();
			    leftfragment.setMenuData(mMenudata);
			    //添加详情页面
			    mpagers =new ArrayList<BaseMenuDetailPager>();
			    mpagers.add(new HomeMenuDetail(mActivity,mMenudata.data.get(0).children));
			    mpagers.add(new ActivityMenuDetail(mActivity));
			    mpagers.add(new PlayMenuDetail(mActivity));
			    mpagers.add(new TopicMenuDetail(mActivity));
			    SetCurrentMenuDetailPager(0);
			}
			/**
			 * 设置当前菜单详情页
			 */
			    public  void SetCurrentMenuDetailPager(int position){
			    	BaseMenuDetailPager pager = mpagers.get(position);//获取当前要显示的页面
			    	flconnet.removeAllViews();//清除所有
			    	flconnet.addView(pager.mRootView);	//将菜单详情页布局给帧布局
			    	//设置当前页的标题
			    	NewsMenuData MenuData = mMenudata.data.get(position);
			    	titletext.setText(MenuData.title);
			    	pager.initData();//初始化当前页面
			    }
}
