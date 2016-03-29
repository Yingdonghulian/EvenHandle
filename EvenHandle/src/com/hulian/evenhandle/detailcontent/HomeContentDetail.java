package com.hulian.evenhandle.detailcontent;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.hulian.evenhandle.R;
import com.hulian.evenhandle.base.BaseMenuDetailPager;
import com.hulian.evenhandle.bean.NewsData.NewsTabData;
import com.hulian.evenhandle.bean.TabData;
import com.hulian.evenhandle.bean.TabData.TabNewsData;
import com.hulian.evenhandle.bean.TabData.TopNewsData;
import com.hulian.evenhandle.global.GlobalConnect;
import com.hulian.evenhandle.util.CacheUtils;
import com.hulian.evenhandle.util.PrefUtils;
import com.hulian.evenhandle.util.RefreshListView;
import com.hulian.evenhandle.util.RefreshListView.OnRefreshListener;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 主页详情页
 * 
 * @author Liu-
 *
 */
public class HomeContentDetail extends BaseMenuDetailPager implements OnPageChangeListener {
	private TabData mTabDetailData;
	NewsTabData mTabData;
	private String mUrl;
	@ViewInject(R.id.tap_news)
	private ViewPager mViewPager;
	@ViewInject(R.id.top_title)
	private TextView tvTitle;// 新闻标题
	private ArrayList<TopNewsData> mTopnews;// 头条新闻数据集合
	@ViewInject(R.id.indicator)
	private CirclePageIndicator Indicator;// 头条新闻指示器
	@ViewInject(R.id.list_view)
	private RefreshListView mListView;// 新闻列表
	private ArrayList<TabNewsData> newsList;// 新闻页数据集合
	private String mMoreUrl;
	private NewsAdapter mNewsAdapter;
	private Handler mHandler;

	public HomeContentDetail(Activity activity, NewsTabData newsTabData) {
		super(activity);
		mTabData = newsTabData;
		mUrl = GlobalConnect.SERVER_URL + mTabData.url;
	}

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.tap_detali_pager, null);
		View headnews = View.inflate(mActivity, R.layout.list_headnews, null);
		ViewUtils.inject(this, view);
		ViewUtils.inject(this, headnews);
		mListView.addHeaderView(headnews);// 将头条新闻以头布局加入
		// 设置下拉刷新
		mListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				getDataFromServer();
			}

			@Override
			public void onLoadMore() {
				if (mMoreUrl != null) {
					getMoreDataFromServer();
				} else {
					Toast.makeText(mActivity, "最后一页了", Toast.LENGTH_SHORT).show();
					mListView.onRefreshComplete(false);// 收起加载更多的布局
				}
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				System.out.println("被点击:" + position);
				// 在本地记录已读状态
				String ids = PrefUtils.getString(mActivity, "read_ids", "");
				String readId = newsList.get(position).id;
				if (!ids.contains(readId)) {
					ids = ids + readId + ",";
					PrefUtils.setString(mActivity, "read_ids", ids);
				}
				changeReadState(view);// 实现局部界面刷新, 这个view就是被点击的item布局对象
				// 跳转到新闻页面
				Intent intent = new Intent();
				intent.setClass(mActivity, NewsDetailActivity.class);
				intent.putExtra("url", newsList.get(position).url);
				mActivity.startActivity(intent);
			}
		});
		return view;
	}

	/**
	 * 改变已读新闻的颜色
	 */
	private void changeReadState(View view) {
		TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
		tvTitle.setTextColor(Color.GRAY);
	}

	@Override
	public void initData() {
	     // 设置缓存
		 String cache = CacheUtils.getCache(mUrl, mActivity);
		 if(!TextUtils.isEmpty(cache)){
		 parseData(cache, false);
		 }
		getDataFromServer();
	}

	private void getDataFromServer() {
		// 使用xutils请求访问
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, mUrl, new RequestCallBack<String>() {
			// 访问主线程失败
			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(mActivity, msg, Toast.LENGTH_LONG).show();
				error.printStackTrace();
				mListView.onRefreshComplete(false);
			}

			// 访问主线程成功
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = (String) responseInfo.result;
				System.out.println(" 详情页数据" + result);
				parseData(result, false);
				mListView.onRefreshComplete(true);
				// 设置缓存
				CacheUtils.setCache(mUrl, result, mActivity);
			}
		});
	}

	/**
	 * 加载下一页数据
	 */
	private void getMoreDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = (String) responseInfo.result;
				parseData(result, true);
				mListView.onRefreshComplete(true);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
				error.printStackTrace();
				mListView.onRefreshComplete(false);
			}
		});
	}

	// 解析数据
	protected void parseData(String result, boolean isMore) {
		Gson gson = new Gson();
		mTabDetailData = gson.fromJson(result, TabData.class);
		System.out.println("TapDetailData解析结果" + mTabDetailData);
		// 处理更多页面
		String more = mTabDetailData.data.more;
		if (!TextUtils.isEmpty(more)) {
			mMoreUrl = GlobalConnect.SERVER_URL + more;
		} else {
			mMoreUrl = null;
		}
		if (!isMore) {
			mTopnews = mTabDetailData.data.topnews;
			ArrayList<TabNewsData> news = mTabDetailData.data.news;
			newsList = news;
			if (mTopnews != null) {
				mViewPager.setAdapter(new NewsTapdetail());
				Indicator.setViewPager(mViewPager);
				Indicator.setSnap(true);// 快照显示
				Indicator.setOnPageChangeListener(this);
				Indicator.onPageSelected(0);// 让指示器指到第一张
				tvTitle.setText(mTopnews.get(0).title);
				if (newsList != null) {
					mNewsAdapter = new NewsAdapter();
					mListView.setAdapter(mNewsAdapter);
				}

				// 自动轮播条显示
				if (mHandler == null) {
					mHandler = new Handler() {
						public void handleMessage(android.os.Message msg) {
							int currentItem = mViewPager.getCurrentItem();

							if (currentItem < mTopnews.size() - 1) {
								currentItem++;
							} else {
								currentItem = 0;
							}
							mViewPager.setCurrentItem(currentItem);// 切换到下一个页面
							mHandler.sendEmptyMessageDelayed(0, 3000);// 继续延时3秒发消息,
																		// 形成循环
						};
					};
					mHandler.sendEmptyMessageDelayed(0, 3000);// 延时3秒后发消息
				}
			}
		} else {// 如果是加载下一页,必须将数据添加到原来的集合当中去
			ArrayList<TabNewsData> New = mTabDetailData.data.news;
			newsList.addAll(New);
			mNewsAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 新闻头条适配器
	 * 
	 * @author Liu-
	 *
	 */
	class NewsTapdetail extends PagerAdapter {
		private BitmapUtils utils;

		public NewsTapdetail() {
			utils = new BitmapUtils(mActivity);
		}

		@Override
		public int getCount() {
			return mTabDetailData.data.topnews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView imgView = new ImageView(mActivity);
			imgView.setImageResource(R.drawable.topnews_item_default);
			imgView.setScaleType(ScaleType.FIT_XY);// 基于控件的大小填充
			TopNewsData topNewsData = mTopnews.get(position);
			utils.display(imgView, topNewsData.topimage);// 传递imageView对象和地址
			container.addView(imgView);
			imgView.setOnTouchListener(new TopNewsTouchListener());// 设置触摸监听
			return imgView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	// 头条新闻的触摸监听

	class TopNewsTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				System.out.println("按下");
				mHandler.removeCallbacksAndMessages(null);// 删除Handler中的所有消息
				break;
			case MotionEvent.ACTION_CANCEL:
				System.out.println("事件取消");
				mHandler.sendEmptyMessageDelayed(0, 3000);
				break;
			case MotionEvent.ACTION_UP:
				System.out.println("抬起");
				mHandler.sendEmptyMessageDelayed(0, 3000);
				break;

			default:
				break;
			}

			return true;
		}
	}

	/**
	 * 新闻列表适配器
	 * 
	 * @author Liu-
	 *
	 */
	class NewsAdapter extends BaseAdapter {
		private BitmapUtils Utils;

		public NewsAdapter() {
			Utils = new BitmapUtils(mActivity);
			Utils.configDefaultLoadingImage(R.drawable.pic_item_list_default);// 设置默认图片
		}

		@Override
		public int getCount() {
			return newsList.size();
		}

		@Override
		public TabNewsData getItem(int postion) {
			return newsList.get(postion);
		}

		@Override
		public long getItemId(int postion) {
			return postion;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.list_news_item, null);
				holder = new ViewHolder();
				holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_pic);
				holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
				holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			TabNewsData item = getItem(position);

			holder.tvTitle.setText(item.title);
			holder.tvDate.setText(item.pubdate);

			Utils.display(holder.ivPic, item.listimage);

			String ids = PrefUtils.getString(mActivity, "read_ids", "");
			if (ids.contains(getItem(position).id)) {
				holder.tvTitle.setTextColor(Color.GRAY);
			} else {
				holder.tvTitle.setTextColor(Color.BLACK);
			}

			return convertView;
		}

	}

	static class ViewHolder {
		public TextView tvTitle;
		public TextView tvDate;
		public ImageView ivPic;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		TopNewsData topNewsData = mTopnews.get(arg0);
		tvTitle.setText(topNewsData.title);
	}

}
