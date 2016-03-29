package com.hulian.evenhandle.base.impl;


import java.util.ArrayList;
import com.google.gson.Gson;
import com.hulian.evenhandle.base.BaseMenuDetailPager;
import com.hulian.evenhandle.base.Basepager;
import com.hulian.evenhandle.pagerdetail.MineDetail;
import android.app.Activity;
import android.view.View;
public class Minepager extends Basepager {
	private ArrayList<BaseMenuDetailPager> mpagers;
	public Minepager(Activity activity) {
		super(activity);
	}
	@Override
	public void initData() {		
		titletext.setText("�ҵ�");// �޸ı���
        setslidingMenuEnabel(false);//�رղ���
		btnmenu.setVisibility(View.GONE);//����ͼ��
         initView();
	}
	private void initView() {
		 mpagers =new ArrayList<BaseMenuDetailPager>();
		 mpagers.add(new MineDetail(mActivity));
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
