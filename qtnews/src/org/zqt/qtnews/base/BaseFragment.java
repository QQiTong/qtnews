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
 * ����fragment�Ļ���
 */
public abstract class BaseFragment extends Fragment {
	
	public Activity mActivity; // ��fragment�󶨵��ĸ�Activity��, �����Ķ�������Ǹ�Activity.

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
	 * �������ʵ�ִ˷���, ����һ��View����, ��Ϊ��ǰFragment�Ĳ�����չʾ.
	 * @return
	 */
	public abstract View initView(LayoutInflater inflater);
	
	/**
	 * ���������Ҫ��ʼ���Լ�������, �Ѵ˷���������.
	 */
	public void initData() {
		
	}
}
