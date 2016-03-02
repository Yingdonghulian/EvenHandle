package com.hulian.evenhandle.menudetail;

import com.hulian.evenhandle.base.BaseMenuDetailPager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
/**
 * �˵�����ҳ
 * @author Liu-
 *
 */
public class ActivityMenuDetail extends BaseMenuDetailPager {

	public ActivityMenuDetail(Activity activity) {
		super(activity);
	}
	@Override
	public View initView() {
		TextView text=new TextView(mActivity);
		text.setText("��ҳ_����ҳ");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);
		return text;
	}

}
