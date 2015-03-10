package org.zqt.qtnews.view;

import com.viewpagerindicator.TabPageIndicator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.BaseAdapter;

/**
 * @����:org.zqt.qtnews.view
 * @����:TouchedTabPageIndicator
 * @������: zqt
 * @����ʱ��:2015-3-9 ����6:31:56
 *
 * @����:TODO �������϶���ʱ��,�����������˵���TabPageIndicator
 * 
 * @SVN�汾��:$Rev: 22 $
 * @����ʱ��:$Date: 2015-03-09 20:39:45 +0800 (Mon, 09 Mar 2015) $
 * @������:$Author: zqt $
 * @��������:TODO
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
		// true ��ϣ������������touch �¼�
		// false:ϣ������������touch �¼�
		getParent().requestDisallowInterceptTouchEvent(true);
		return super.dispatchTouchEvent(ev);
	}
}