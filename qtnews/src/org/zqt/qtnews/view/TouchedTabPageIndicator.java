package org.zqt.qtnews.view;

import com.viewpagerindicator.TabPageIndicator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.BaseAdapter;

/**
 * @包名:org.zqt.qtnews.view
 * @类名:TouchedTabPageIndicator
 * @创建人: zqt
 * @创建时间:2015-3-9 下午6:31:56
 *
 * @描述:TODO 当向右拖动的时候,不会拉出左侧菜单的TabPageIndicator
 * 
 * @SVN版本号:$Rev: 22 $
 * @更新时间:$Date: 2015-03-09 20:39:45 +0800 (Mon, 09 Mar 2015) $
 * @更新人:$Author: zqt $
 * @更新描述:TODO
 */
public class TouchedTabPageIndicator extends TabPageIndicator{
	
	public TouchedTabPageIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}

	public TouchedTabPageIndicator(Context context) {
		super(context);
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// true 不希望父容器拦截touch 事件
		// false:希望父容器拦截touch 事件
		getParent().requestDisallowInterceptTouchEvent(true);
		return super.dispatchTouchEvent(ev);
	}
}
