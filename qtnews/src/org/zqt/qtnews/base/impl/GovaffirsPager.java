package org.zqt.qtnews.base.impl;

import org.zqt.qtnews.base.TabBasePager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;


/**
 * @author zqt
 * �����ҳ��
 */
public class GovaffirsPager extends TabBasePager {

	public GovaffirsPager(Context context) {
		super(context);
	}

	@Override
	public void initData() {
		System.out.println("�������ݳ�ʼ����.");
		tvTitle.setText("�˿ڹ���");
		ibMenu.setVisibility(View.VISIBLE);
		
		TextView tv = new TextView(mContext);
		tv.setText("����");
		tv.setTextSize(25);
		tv.setTextColor(Color.RED);
		tv.setGravity(Gravity.CENTER);
		flContent.addView(tv);
	}
}