package org.zqt.qtnews.base.impl;

import org.zqt.qtnews.base.TabBasePager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;


/**
 * @author zqt
 * 智慧服务的页面
 */
public class SmartServicePager extends TabBasePager {

	public SmartServicePager(Context context) {
		super(context);
	}

	@Override
	public void initData() {
		System.out.println("智慧服务数据初始化了.");
		tvTitle.setText("生活");
		ibMenu.setVisibility(View.VISIBLE);
		
		TextView tv = new TextView(mContext);
		tv.setText("智慧服务");
		tv.setTextSize(25);
		tv.setTextColor(Color.RED);
		tv.setGravity(Gravity.CENTER);
		flContent.addView(tv);
	}
}
