package org.zqt.qtnews.base;

import android.content.Context;
import android.view.View;

/**
 * @author zqt
 * 菜单对应的页面的基类
 */
public abstract class MenuDetailBasePager {
	
	public Context mContext;
	private View rootView;

	public MenuDetailBasePager(Context context) {
		this.mContext = context;
		
		rootView = initView();
	}
	
	public abstract View initView();
	
	public View getRootView() {
		return rootView;
	}
	
	/**
	 * 子类需要覆盖此方法, 实现自己的数据初始化.
	 */
	public void initData() {
		
	}
}
