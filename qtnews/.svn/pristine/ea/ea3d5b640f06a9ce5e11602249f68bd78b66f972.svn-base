package org.zqt.qtnews.base;

import org.zqt.qtnews.R;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

public class TabBasePager {
	
	public Context mContext;
	public TextView tvTitle;
	public ImageButton ibMenu;
	public ImageButton ibListOrGrid;
	public FrameLayout flContent;
	private View rootView;

	public TabBasePager(Context context) {
		this.mContext = context;  
		
		rootView = initView();
	}

	private View initView() {
		View view = View.inflate(mContext, R.layout.tab_base_pager, null);
		tvTitle = (TextView) view.findViewById(R.id.tv_title_bar_title);
		ibMenu = (ImageButton) view.findViewById(R.id.ib_title_bar_menu);
		ibListOrGrid = (ImageButton) view.findViewById(R.id.ib_title_bar_list_or_grid);
		flContent = (FrameLayout) view.findViewById(R.id.fl_tab_base_pager_content);
		return view;
	}
	
	/**
	 * 获得当前页面布局对象
	 * @return
	 */
	public View getRootView() {
		return rootView;
	}
	
	public void initData() {
		
	}
}
