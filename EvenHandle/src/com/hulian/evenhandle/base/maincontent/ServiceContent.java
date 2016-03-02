package com.hulian.evenhandle.base.maincontent;

import com.hulian.evenhandle.Fragment.BaseFragment;
import com.hulian.evenhandle.base.BaseMenuDetailPager;
import com.hulian.evenhandle.base.Basepager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class ServiceContent extends BaseFragment {

	public View initViews() {
		TextView text=new TextView(mActivity);
		text.setText("Ê×Ò³");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);
		return text;
	}
}
