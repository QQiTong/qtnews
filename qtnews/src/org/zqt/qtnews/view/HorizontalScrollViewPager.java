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
			// ��ACTION_DOWN �� ������ ����down�¼�
			getParent().requestDisallowInterceptTouchEvent(true);
			downX = (int) ev.getX();
			downY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX = (int) ev.getX();
			int moveY = (int) ev.getY();

			int diffX = downX - moveX;
			int diffY = downY - moveY;
			
			if(Math.abs(diffX) > Math.abs(diffY)) { // ��ǰ�Ǻ��򻬶�, ����Ҫ����
				if(getCurrentItem() == 0 && diffX < 0) {
					// ��ǰҳ����ڵ�һ��ҳ��, �����Ǵ������һ���, ��������
					getParent().requestDisallowInterceptTouchEvent(false);
				} else if(getCurrentItem() == (getAdapter().getCount() -1)
						&& diffX > 0) {
					// ��ǰҳ��������һ��, �����Ǵ������󻬶�, ��������
					getParent().requestDisallowInterceptTouchEvent(false);
				} else {
					getParent().requestDisallowInterceptTouchEvent(true);
				}
			} else { // ���Ż���, ��������
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			break;
		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
}
