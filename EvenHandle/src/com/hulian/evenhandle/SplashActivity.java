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
	 * 开启动画
	 */
	private void startAnim() {

		// 动画集合
		AnimationSet set = new AnimationSet(false);

		// 旋转动画
		RotateAnimation rotate = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotate.setDuration(1000);// 动画时间
		rotate.setFillAfter(true);// 保持动画状态

		// 缩放动画
		ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scale.setDuration(1000);// 动画时间
		scale.setFillAfter(true);// 保持动画状态

		// 渐变动画
		AlphaAnimation alpha = new AlphaAnimation(0, 1);
		alpha.setDuration(2000);// 动画时间
		alpha.setFillAfter(true);// 保持动画状态

		set.addAnimation(rotate);
		set.addAnimation(scale);
		set.addAnimation(alpha);

		// 设置动画监听
		set.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			// 动画执行结束
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
