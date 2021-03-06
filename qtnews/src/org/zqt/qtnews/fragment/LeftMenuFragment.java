package org.zqt.qtnews.fragment;

import java.util.List;

import org.zqt.qtnews.MainUI;
import org.zqt.qtnews.R;
import org.zqt.qtnews.base.BaseFragment;
import org.zqt.qtnews.base.impl.NewsCenterPager;
import org.zqt.qtnews.domain.NewsCenterBean.NewsCenterData;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * @author zqt
 * 左侧菜单的Fragment
 */
public class LeftMenuFragment extends BaseFragment implements OnItemClickListener {
	
	private List<NewsCenterData> mMenuListData;
	private ListView mListView;
	private int selectPosition; // 默认选中的菜单选项
	private MenuAdapter mAdapter;

	@Override
	public View initView(LayoutInflater inflater) {
		mListView = new ListView(mActivity);
		mListView.setCacheColorHint(Color.TRANSPARENT);
		mListView.setDividerHeight(0);
		mListView.setBackgroundColor(Color.BLACK);
		mListView.setPadding(0, 50, 0, 0);
		mListView.setSelector(android.R.color.transparent);
		mListView.setOnItemClickListener(this);
		return mListView;
	}

	public void setMenuListData(List<NewsCenterData> menuListData) {
		this.mMenuListData = menuListData;
		
		selectPosition = 0;
		
		mAdapter = new MenuAdapter();
		mListView.setAdapter(mAdapter);
		
		switchNewsCenterContentPager();
	}

	class MenuAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mMenuListData.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView view = null;
			if(convertView == null) {
				view = (TextView) View.inflate(mActivity, R.layout.menu_item, null);
			} else {
				view = (TextView) convertView;
			}
			
			view.setText(mMenuListData.get(position).title);
			
			// 设置当前的item是否被选中
			view.setEnabled(position == selectPosition);
			return view;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
		selectPosition = position;
		mAdapter.notifyDataSetChanged();
		
		// 把左侧菜单收回去
		SlidingMenu slidingMenu = ((MainUI) mActivity).getSlidingMenu();
		slidingMenu.toggle();
		
		// 主界面中间部分要显示对应position的页面
		switchNewsCenterContentPager();
	}
	/**
	 * 根据selectPosition来切换当前新闻中心的内容页面
	 */
	private void switchNewsCenterContentPager() {
		// 默认选中第0个菜单, 菜单对应的页面需要切换为第0个页面
		MainUI mainUI = (MainUI) mActivity;
		MainContentFragment fragment = mainUI.getMainContentFragment();
		NewsCenterPager newsCenterPager = fragment.getNewsCenterPager();
		newsCenterPager.switchCurrentPager(selectPosition);
	}
}
