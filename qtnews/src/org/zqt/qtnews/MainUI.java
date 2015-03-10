package org.zqt.qtnews;

import org.zqt.qtnews.fragment.LeftMenuFragment;
import org.zqt.qtnews.fragment.MainContentFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * alt + shift + J
 * @author zqt
 *
 * ������
 */
public class MainUI extends SlidingFragmentActivity {

	// ���˵�fragment��tag
	private final String LEFT_MENU_FRAGMENT_TAG = "left_menu";
	// ������fragment��tag
	private final String MAIN_CONTENT_FRAGMENT_TAG = "main_content";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_content); // �����沼��
		setBehindContentView(R.layout.left_menu); // ���˵�����
		
		SlidingMenu slidingMenu = getSlidingMenu();
		slidingMenu.setMode(SlidingMenu.LEFT); // �������˵�����.
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN); // ������Ļ��������ק���˵�.
		slidingMenu.setBehindOffset(200); // ����������������Ļ�ϵĿ���
		
		initFragment();
	}

	/**
	 * ��ʼ���˵���������Fragment
	 */
	private void initFragment() {
		// ��ȡFragment����������
		FragmentManager fm = getSupportFragmentManager();
		
		// ��������
		FragmentTransaction ft = fm.beginTransaction(); // �õ������������

		// �滻���˵�����
		ft.replace(R.id.fl_left_menu, new LeftMenuFragment(), LEFT_MENU_FRAGMENT_TAG);
		// �滻�����沼��
		ft.replace(R.id.fl_main_content, new MainContentFragment(), MAIN_CONTENT_FRAGMENT_TAG);
		
		// �ύ
		ft.commit();
	}
	
	/**
	 * ��ȡ���˵���Fragment����
	 * @return
	 */
	public LeftMenuFragment getLeftMenuFragment() {
		FragmentManager fm = getSupportFragmentManager();
		LeftMenuFragment leftMenuFragment = (LeftMenuFragment) fm.findFragmentByTag(LEFT_MENU_FRAGMENT_TAG);
		return leftMenuFragment;
	}
	
	/**
	 * ��ȡ��ҳ���Fragment����
	 * @return
	 */
	public MainContentFragment getMainContentFragment() {
		FragmentManager fm = getSupportFragmentManager();
		MainContentFragment mainContentFragment = (MainContentFragment) 
				fm.findFragmentByTag(MAIN_CONTENT_FRAGMENT_TAG);
		return mainContentFragment;
	}
}