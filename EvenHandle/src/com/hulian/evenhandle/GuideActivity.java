package com.hulian.evenhandle;

import java.util.ArrayList;

import com.hulian.evenhandle.util.DensityUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class GuideActivity extends Activity{
	
	private static  final int[ ] mImageIds =new int[ ]{R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};
	private ViewPager guidepager;
	private ArrayList<ImageView> mImageViewList;
	private LinearLayout pointgroup;  //����ҳ��ԭ�㸸�ؼ�
	private int mpointwidth;//����ľ���
	private View guideredpoint;//����ҳ���
	private Button butstart;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide_layout);
		guidepager=(ViewPager) findViewById(R.id.guide_pic);
		pointgroup=(LinearLayout) findViewById(R.id.guide_point_group);
		guideredpoint=findViewById(R.id.guide_red_point);
		butstart=(Button) findViewById(R.id.but_start);
		 
		butstart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SharedPreferences sp  = getSharedPreferences("config", MODE_PRIVATE);
				sp.edit().putBoolean("is_user_guide_showed", true).commit();
				
				startActivity(new Intent(GuideActivity.this,PagerActicity.class));
				finish();
			}
		});
		
		initViews();	
        guidepager.setAdapter(new GuidepAdapter());
        guidepager.setOnPageChangeListener(new GuidePageListener());
        
	}
	/**
	 * ��ʼ������
	 */
	   private void  initViews(){
		   mImageViewList = new ArrayList<ImageView>();
		   //��ʼ������
		   for(int i=0; i<mImageIds.length; i++){
			 ImageView image=new ImageView(this);
			 image.setBackgroundResource(mImageIds[i]); 
			 mImageViewList.add(image);
		 }  	
		   //��ʼ��ҳ��ԭ��
		   for(int i=0; i<mImageIds.length; i++){
				 View point=new View(this);
				 point.setBackgroundResource(R.drawable.guide_shap_point); 
				 LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(DensityUtils.dp2px(this, 10), DensityUtils.dp2px(this, 10));
				 if(i>0){
					 params.leftMargin=DensityUtils.dp2px(this, 10);	
				 }
				 point.setLayoutParams(params);//����ԭ���С
				 pointgroup.addView(point);//��ԭ����ӵ�Linerlayout������
		 }	   
		   //��ȡ��ͼ�� ��Layout����
		         
		         pointgroup.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {					
					@Override
					public void onGlobalLayout() {
						// TODO Auto-generated method stub
						pointgroup.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				     mpointwidth = pointgroup.getChildAt(1).getLeft()-pointgroup.getChildAt(0).getLeft();
					}
				});		         
	   }
	   
	     class GuidepAdapter extends PagerAdapter{
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mImageIds .length;
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				// TODO Auto-generated method stub
				return arg0==arg1;
			}
		   @Override
		    public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			container.addView(mImageViewList.get(position) );   
			return mImageViewList.get(position);
		  }
	     @Override
	         public void destroyItem(ViewGroup container, int position, Object object) {
	    	// TODO Auto-generated method stub
	    	   container.removeView((View) object);    	
	     } 
	 }	
	     /**
	      * �����deҳ�����
	      * @author Liu-
	      *
	      */
	     class GuidePageListener implements OnPageChangeListener{

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageScrolled(int position,float positionoffset,int positionoffsetPixels) {
				// TODO Auto-generated method stub
				int len=(int) (mpointwidth*positionoffset)+ position*mpointwidth;
				RelativeLayout.LayoutParams Params =(RelativeLayout.LayoutParams) guideredpoint.getLayoutParams();//��ȡ���Ĳ��ֲ���
			   Params.leftMargin=len;//������ߵľ���
			   guideredpoint.setLayoutParams(Params);//���¸�С��㲼�ֲ���
			}

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				if(position==mImageIds.length-1)//���һ��ҳ��
				{
					butstart.setVisibility(View.VISIBLE);
				}else
				{
					butstart.setVisibility(View.INVISIBLE);
				}
			}
	     }}
	     
