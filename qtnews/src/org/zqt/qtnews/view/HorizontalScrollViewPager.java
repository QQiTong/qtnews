package org.zqt.qtnews.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class HorizontalScrollViewPager extends ViewPager {

	private int downX;
	private int downY;

	public HorizontalScrollViewPager(Context context) {
		super(context);
	}

	public HorizontalScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 在ACTION_DOWN 中 向父容器 请求down事件
			getParent().requestDisallowInterceptTouchEvent(true);
			downX = (int) ev.getX();
			downY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX = (int) ev.getX();
			int moveY = (int) ev.getY();

			int diffX = downX - moveX;
			int diffY = downY - moveY;
			
			if(Math.abs(diffX) > Math.abs(diffY)) { // 当前是横向滑动, 不需要拦截
				if(getCurrentItem() == 0 && diffX < 0) {
					// 当前页面等于第一个页面, 并且是从左向右滑动, 可以拦截
					getParent().requestDisallowInterceptTouchEvent(false);
				} else if(getCurrentItem() == (getAdapter().getCount() -1)
						&& diffX > 0) {
					// 当前页面等于最后一个, 并且是从右向左滑动, 可以拦截
					getParent().requestDisallowInterceptTouchEvent(false);
				} else {
					getParent().requestDisallowInterceptTouchEvent(true);
				}
			} else { // 竖着滑动, 可以拦截
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			break;
		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
}
