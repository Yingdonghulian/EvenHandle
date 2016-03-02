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
		titletext.setText("��������");// �޸ı���
		setslidingMenuEnabel(true);			 
	    String cache = CacheUtils.getCache(GlobalConnect.CATEGORIES_URL, mActivity);
		if(!TextUtils.isEmpty(cache)){//����������ֱ�ӽ��� ���õ��ýӿ�
		     parseData(cache);
		}
		   getDataFromServer();//ÿ�ζ���ȡ����
	}
	/**
	 * �ӷ�������ȡ����
	 */
	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, GlobalConnect.CATEGORIES_URL, new RequestCallBack<String>() {

			// �ɹ�����
			@Override
			public void onSuccess(ResponseInfo responseInfo) {
				String result = (String) responseInfo.result;
				System.out.println("����ֵ" + result);
  		        parseData(result);
  		        //���û���
  		       CacheUtils.setCache(GlobalConnect.CATEGORIES_URL, result, mActivity);
			}

//			 ����ʧ��
			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(mActivity, msg, Toast.LENGTH_LONG).show();
				error.printStackTrace();
			}
		});
	}
		   //��������
			protected void parseData(String result) {
				Gson gson=new Gson();
				mMenudata = gson.fromJson(result, NewsData.class);
				System.out.println("������"+mMenudata); 
								
				//ˢ�²��������
			    PagerActicity mainUi=(PagerActicity) mActivity;
			    LeftFragment leftfragment= mainUi.getLeftFragment();
			    leftfragment.setMenuData(mMenudata);
			    //�������ҳ��
			    mpagers =new ArrayList<BaseMenuDetailPager>();
			    mpagers.add(new HomeMenuDetail(mActivity,mMenudata.data.get(0).children));
			    mpagers.add(new ActivityMenuDetail(mActivity));
			    mpagers.add(new PlayMenuDetail(mActivity));
			    mpagers.add(new TopicMenuDetail(mActivity));
			    SetCurrentMenuDetailPager(0);
			}
			/**
			 * ���õ�ǰ�˵�����ҳ
			 */
			    public  void SetCurrentMenuDetailPager(int position){
			    	BaseMenuDetailPager pager = mpagers.get(position);//��ȡ��ǰҪ��ʾ��ҳ��
			    	flconnet.removeAllViews();//�������
			    	flconnet.addView(pager.mRootView);	//���˵�����ҳ���ָ�֡����
			    	//���õ�ǰҳ�ı���
			    	NewsMenuData MenuData = mMenudata.data.get(position);
			    	titletext.setText(MenuData.title);
			    	pager.initData();//��ʼ����ǰҳ��
			    }
}
