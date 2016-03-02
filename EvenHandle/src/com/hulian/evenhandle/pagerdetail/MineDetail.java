package com.hulian.evenhandle.pagerdetail;

import com.hulian.evenhandle.R;
import com.hulian.evenhandle.base.BaseMenuDetailPager;
import com.lidroid.xutils.ViewUtils;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class MineDetail extends BaseMenuDetailPager implements OnClickListener {
	public MineDetail(Activity activity) {
		super(activity);
	}

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.mine_detail, null);
        ViewUtils.inject(view);
        View loginButton = view.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(mActivity, LoginActivity.class);
			    mActivity.startActivity(intent);				
			}
		});
		return view;
 
	}
	@Override
	public void initData() {
		super.initData();
	}

	@Override
	public void onClick(View v) {
         switch (v.getId()) {
		case R.id.login_button:
			
			break;

		default:
			break;
		}		
	}

}
