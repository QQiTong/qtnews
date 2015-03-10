package org.zqt.qtnews.base.impl;

import org.zqt.qtnews.base.TabBasePager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;


/**
 * @author zqt
 * ��ҳ��ҳ��
 */
public class HomePager extends TabBasePager {

	public HomePager(Context context) {
		super(context);
	}

	@Override
	public void initData() {
		System.out.println("��ҳ���ݳ�ʼ����.");
		tvTitle.setText("�ǻ۱���");
		ibMenu.setVisibility(View.GONE);
		
		TextView tv = new TextView(mContext);
		tv.setText("��ҳ");
		tv.setTextSize(25);
		tv.setTextColor(Color.RED);
		tv.setGravity(Gravity.CENTER);
		flContent.addView(tv);
	}
}