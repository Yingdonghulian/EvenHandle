package com.hulian.evenhandle.util;

import android.content.Context;

public class DensityUtils {

	/**
	 * dp转px
	 */
	public static int dp2px(Context ctx, float dp) {
		float density = ctx.getResources().getDisplayMetrics().density;
		int px = (int) (dp * density + 0.5f);// 四舍五入

		return px;
	}

	public static float px2dp(Context ctx, int px) {
		float density = ctx.getResources().getDisplayMetrics().density;
		float dp = px / density;

		return dp;
	}
}
