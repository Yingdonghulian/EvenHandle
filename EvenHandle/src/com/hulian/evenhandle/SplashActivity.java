package com.hulian.evenhandle;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.RelativeLayout;

public class SplashActivity extends Activity {
    
    RelativeLayout rlRoot;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);
		rlRoot=(RelativeLayout) findViewById(R.id.start_layout);
		startAnim();
	}

	/**
	 * ��������
	 */
	private void startAnim() {

		// ��������
		AnimationSet set = new AnimationSet(false);

		// ��ת����
		RotateAnimation rotate = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotate.setDuration(1000);// ����ʱ��
		rotate.setFillAfter(true);// ���ֶ���״̬

		// ���Ŷ���
		ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scale.setDuration(1000);// ����ʱ��
		scale.setFillAfter(true);// ���ֶ���״̬

		// ���䶯��
		AlphaAnimation alpha = new AlphaAnimation(0, 1);
		alpha.setDuration(2000);// ����ʱ��
		alpha.setFillAfter(true);// ���ֶ���״̬

		set.addAnimation(rotate);
		set.addAnimation(scale);
		set.addAnimation(alpha);

		// ���ö�������
		set.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			// ����ִ�н���
			@Override
			public void onAnimationEnd(Animation animation) {
				jumpNextpage();
				
			}
		});

		rlRoot.startAnimation(set);
	}
		  public void jumpNextpage(){
		  SharedPreferences sp =getSharedPreferences("config",MODE_PRIVATE);
		  Boolean userGuide=sp.getBoolean("is_user_guide_showed", false);
		  if(!userGuide){
		  	startActivity(new  Intent(SplashActivity.this,GuideActivity.class));
		  }else
		  {
			  startActivity(new  Intent(SplashActivity.this,PagerActicity.class));
		  }
		  finish();
}
}
