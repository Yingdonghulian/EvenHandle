package com.hulian.evenhandle.base.impl;


import java.util.ArrayList;
import com.hulian.evenhandle.base.BaseMenuDetailPager;
import com.hulian.evenhandle.base.Basepager;
import com.hulian.evenhandle.pagerdetail.ServiceDetail;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;


public class Servicepager extends Basepager {
	private ArrayList<BaseMenuDetailPager> mpagers;
	public Servicepager(Activity activity) {
		super(activity);
	}
	@Override
	public void initData() {
		titletext.setText("��������");// �޸ı���
		setslidingMenuEnabel(false);	  
		btnmenu.setVisibility(View.GONE);//����ͼ��  
		initView();
		 
	   }
	private void initView() {
		 mpagers =new ArrayList<BaseMenuDetailPager>();
		 mpagers.add(new ServiceDetail(mActivity, 0));
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
