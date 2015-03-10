package org.zqt.qtnews.base;

import org.zqt.qtnews.R;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author zqt
 * 所有fragment的基类
 */
public abstract class BaseFragment extends Fragment {
	
	public Activity mActivity; // 把fragment绑定到哪个Activity上, 上下文对象就是那个Activity.

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = initView(inflater);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
	}
	
	/**
	 * 子类必须实现此方法, 返回一个View对象, 作为当前Fragment的布局来展示.
	 * @return
	 */
	public abstract View initView(LayoutInflater inflater);
	
	/**
	 * 如果子类需要初始化自己的数据, 把此方法给覆盖.
	 */
	public void initData() {
		
	}
}
