package org.zqt.qtnews;

import org.zqt.qtnews.utils.CacheUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

public class WelcomeUI extends Activity implements AnimationListener {
	
	public static final String IS_OPEN_MAIN_PAGER = "IS_OPEN_MAIN_PAGER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        
        init();
    }

	private void init() {
		View rootView = findViewById(R.id.rl_welcome_root);
		
		RotateAnimation rotateAnima = new RotateAnimation(
				0, 360, 
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF, 0.5f);
		rotateAnima.setDuration(1000);
		rotateAnima.setFillAfter(true); // ���ö���ִ�����ʱ, ͣ������ϵ�״̬��.
		
		ScaleAnimation scaleAnima = new ScaleAnimation(
				0, 1, 
				0, 1, 
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF, 0.5f);
		scaleAnima.setDuration(1000);
		scaleAnima.setFillAfter(true);
		
		AlphaAnimation alphaAnima = new AlphaAnimation(0, 1);
		alphaAnima.setDuration(2000);
		alphaAnima.setFillAfter(true);
		
		
		// ��������������һ��, ���һ�����϶���
		AnimationSet setAnima = new AnimationSet(false);
		setAnima.addAnimation(rotateAnima);
		setAnima.addAnimation(scaleAnima);
		setAnima.addAnimation(alphaAnima);
		setAnima.setAnimationListener(this);
		
		rootView.startAnimation(setAnima);
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// ȥ�ļ���ȡ�Ƿ�򿪹������ֵ
		boolean isOpenMainPager = CacheUtils.getBoolean(WelcomeUI.this, IS_OPEN_MAIN_PAGER, false);
		Intent intent = new Intent();
		if(isOpenMainPager) {
			// �Ѿ��򿪹�һ��������, ֱ�ӽ���������.
			intent.setClass(WelcomeUI.this, MainUI.class);
		} else {
			// û�д򿪹�������, ��������ҳ��.
			intent.setClass(WelcomeUI.this, GuideUI.class);
		}
		startActivity(intent);
		
		// �رյ���ӭ����
		finish();
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		
	}
}
