package org.zqt.qtnews.base.newscenterimpl;

import org.zqt.qtnews.base.MenuDetailBasePager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;


/**
 * @author zqt
 * 专题菜单对应的页面
 */
public class TopicMenuDetailPager extends MenuDetailBasePager {

	public TopicMenuDetailPager(Context context) {
		super(context);
	}

	@Override
	public View initView() {
		TextView tv = new TextView(mContext);
		tv.setText("专题菜单页面");
		tv.setTextSize(23);
		tv.setTextColor(Color.RED);
		tv.setGravity(Gravity.CENTER);
		return tv;
	}

}
