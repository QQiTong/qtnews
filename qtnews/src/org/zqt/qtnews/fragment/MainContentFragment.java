package org.zqt.qtnews.fragment;

import java.util.ArrayList;
import java.util.List;

import org.zqt.qtnews.MainUI;
import org.zqt.qtnews.R;
import org.zqt.qtnews.base.BaseFragment;
import org.zqt.qtnews.base.TabBasePager;
import org.zqt.qtnews.base.impl.GovaffirsPager;
import org.zqt.qtnews.base.impl.HomePager;
import org.zqt.qtnews.base.impl.NewsCenterPager;
import org.zqt.qtnews.base.impl.SettingsPager;
import org.zqt.qtnews.base.impl.SmartServicePager;
import org.zqt.qtnews.view.NoScrollViewPager;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @author zqt
 * 主界面的Fragment
 */
public class MainContentFragment extends BaseFragment implements OnCheckedChangeListener {
	
	@ViewInject(R.id.vp_content_fragment)
	private NoScrollViewPager mViewPager;
	
	@ViewInject(R.id.rg_content_fragment)
	private RadioGroup mRadioGroup;

	private List<TabBasePager> pagerList;
	@Override
	public View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.content_fragment, null);
		ViewUtils.inject(this, view); // 把当前view对象注入到xUtils框架中.
		return view;
	}

	@Override
	public void initData() {
		pagerList = new ArrayList<TabBasePager>(); 
		pagerList.add(new HomePager(mActivity));
		pagerList.add(new NewsCenterPager(mActivity));
		pagerList.add(new SmartServicePager(mActivity));
		pagerList.add(new GovaffirsPager(mActivity));
		pagerList.add(new SettingsPager(mActivity));
		
		ContentAdapter mAdapter = new ContentAdapter();
		mViewPager.setAdapter(mAdapter);
		
		mRadioGroup.check(R.id.rb_content_fragment_home); // 设置默认选中的是home页签
		pagerList.get(0).initData(); // 初始化首页的数据
		((MainUI) mActivity).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		mRadioGroup.setOnCheckedChangeListener(this);
	}
	
	class ContentAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return pagerList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			System.out.println("预加载: " + position);
			TabBasePager pager = pagerList.get(position);
			View rootView = pager.getRootView();
			container.addView(rootView);
//			pager.initData();
			return rootView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		int index = -1;
		switch (checkedId) {
		case R.id.rb_content_fragment_home:
			index = 0;
			break;
		case R.id.rb_content_fragment_newscenter:
			index = 1;
			break;
		case R.id.rb_content_fragment_smartservice:
			index = 2;
			break;
		case R.id.rb_content_fragment_govaffairs:
			index = 3;
			break;
		case R.id.rb_content_fragment_settings:
			index = 4;
			break;
		default:
			break;
		}
		
		if(index != -1) {
			mViewPager.setCurrentItem(index);
			pagerList.get(index).initData(); // 初始化数据
			
			if(index == 0 || index == 4) {
				// 菜单不可用
				((MainUI) mActivity).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
			} else {
				((MainUI) mActivity).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			}
		}
	}
	
	/**
	 * 获取新闻中心页面
	 * @return
	 */
	public NewsCenterPager getNewsCenterPager() {
		return (NewsCenterPager) pagerList.get(1);
	}
}
