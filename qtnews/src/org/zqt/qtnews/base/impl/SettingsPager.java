package org.zqt.qtnews.base.impl;

import org.zqt.qtnews.base.TabBasePager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;


/**
 * @author zqt
 * 设置的页面
 */
public class SettingsPager extends TabBasePager {

	public SettingsPager(Context context) {
		super(context);
	}

	@Override
	public void initData() {
		System.out.println("设置数据初始化了.");
		tvTitle.setText("设置");
		ibMenu.setVisibility(View.GONE);
		
		TextView tv = new TextView(mContext);
		tv.setText("设置");
		tv.setTextSize(25);
		tv.setTextColor(Color.RED);
		tv.setGravity(Gravity.CENTER);
		flContent.addView(tv);
	}
}
