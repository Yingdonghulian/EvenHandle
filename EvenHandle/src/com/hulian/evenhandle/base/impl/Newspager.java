package com.hulian.evenhandle.base.impl;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.hulian.evenhandle.PagerActicity;
import com.hulian.evenhandle.Fragment.LeftFragment;
import com.hulian.evenhandle.base.BaseMenuDetailPager;
import com.hulian.evenhandle.base.Basepager;
import com.hulian.evenhandle.bean.NewsData;
import com.hulian.evenhandle.global.GlobalConnect;
import com.hulian.evenhandle.menudetail.ActivityMenuDetail;
import com.hulian.evenhandle.menudetail.HomeMenuDetail;
import com.hulian.evenhandle.menudetail.PlayMenuDetail;
import com.hulian.evenhandle.menudetail.TopicMenuDetail;
import com.hulian.evenhandle.pagerdetail.NewsDetail;
import com.hulian.evenhandle.pagerdetail.ServiceDetail;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Newspager extends Basepager {
	private ArrayList<BaseMenuDetailPager> mpagers;
	public Newspager(Activity activity) {
		super(activity);
	}
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		titletext.setText("��Ϣ");// �޸ı���
		setslidingMenuEnabel(false);	  
		btnmenu.setVisibility(View.GONE);//����ͼ��
	   initView();
	}
	private void initView() {
		 mpagers =new ArrayList<BaseMenuDetailPager>();
		 mpagers.add(new NewsDetail(mActivity, 0));
		 SetCurrentMenuDetailPager(0);

	}
	/**
	 * ���õ�ǰ�˵�����ҳ
	 */
	     public  void SetCurrentMenuDetailPager(int position){
	    	BaseMenuDetailPager pager = mpagers.get(position);//��ȡ��ǰҪ��ʾ��ҳ��
	    	flconnet.removeAllViews();//�������
	    	flconnet.addView(pager.mRootView);	//���˵�����ҳ���ָ�֡����
	    	pager.initData();//��ʼ����ǰҳ��
	     }
}
