package org.zqt.qtnews.base.impl;

import java.util.ArrayList;
import java.util.List;

import org.zqt.qtnews.MainUI;
import org.zqt.qtnews.base.MenuDetailBasePager;
import org.zqt.qtnews.base.TabBasePager;
import org.zqt.qtnews.base.newscenterimpl.InteractMenuDetailPager;
import org.zqt.qtnews.base.newscenterimpl.NewsMenuDetailPager;
import org.zqt.qtnews.base.newscenterimpl.PhotosMenuDetailPager;
import org.zqt.qtnews.base.newscenterimpl.TopicMenuDetailPager;
import org.zqt.qtnews.domain.NewsCenterBean;
import org.zqt.qtnews.domain.NewsCenterBean.NewsCenterData;
import org.zqt.qtnews.fragment.LeftMenuFragment;
import org.zqt.qtnews.utils.CacheUtils;
import org.zqt.qtnews.utils.Constants;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * @author zqt
 * 新闻中心的页面
 */
public class NewsCenterPager extends TabBasePager {
	
	private List<NewsCenterData> leftMenuDataList; // 左侧菜单的数据
	private List<MenuDetailBasePager> pagerList; // 左侧菜单对应的页面

	public NewsCenterPager(Context context) {
		super(context);
	}

	@Override
	public void initData() {
		System.out.println("新闻中心数据初始化了.");
		tvTitle.setText("新闻");
		ibMenu.setVisibility(View.VISIBLE);
		
		// 把本地缓存的数据取出来.
		String json = CacheUtils.getString(mContext, Constants.NEWSCENTER_URL, null);
		if(!TextUtils.isEmpty(json)) {
			processData(json); // 先把旧的新闻显示出来, 下面继续使用网络请求最新数据.
		}
		getDataFromNet();
	}

	/**
	 * 抓取数据
	 */
	private void getDataFromNet() {
		// 去服务端抓取数据.
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, Constants.NEWSCENTER_URL, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				System.out.println("新闻中心数据请求成功: " + responseInfo.result);
				
				// 把数据存储起来.
				CacheUtils.putString(mContext, Constants.NEWSCENTER_URL, responseInfo.result);
				
				processData(responseInfo.result);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				System.out.println("新闻中心数据请求失败: " + msg);
			}
		});
	}

	/**
	 * 解析和处理数据
	 * @param result
	 */
	protected void processData(String result) {
		Gson gson = new Gson();
		NewsCenterBean bean = gson.fromJson(result, NewsCenterBean.class);
		
		// 初始化左侧菜单对应的页面
		pagerList = new ArrayList<MenuDetailBasePager>();
		pagerList.add(new NewsMenuDetailPager(mContext, bean.data.get(0)));
		pagerList.add(new TopicMenuDetailPager(mContext));
		pagerList.add(new PhotosMenuDetailPager(mContext, bean.data.get(2)));
		pagerList.add(new InteractMenuDetailPager(mContext));
		
		// 初始化左侧菜单的数据
		leftMenuDataList = bean.data;
		LeftMenuFragment leftMenuFragment = ((MainUI) mContext).getLeftMenuFragment();
		leftMenuFragment.setMenuListData(leftMenuDataList); // 把菜单数据传递左侧菜单对象
	}
	
	/**
	 * 根据位置来切换当前菜单对应的页面
	 * @param position
	 */
	public void switchCurrentPager(int position) {
		final MenuDetailBasePager pager = pagerList.get(position);
		// 把pager中的view对象添加到FrameLayout中.
		View view = pager.getRootView();
		flContent.removeAllViews();
		flContent.addView(view);
		
		// 标题要跟着去改变.
		tvTitle.setText(leftMenuDataList.get(position).title);
		
		// 初始化当前pager的数据
		pager.initData();
		
		if(position == 2) {
			// 当前是组图页面.
			ibListOrGrid.setVisibility(View.VISIBLE);
			ibListOrGrid.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					PhotosMenuDetailPager photosPager = (PhotosMenuDetailPager) pager;
					photosPager.switchCurrentPager(ibListOrGrid);
				}
			});
		} else {
			ibListOrGrid.setVisibility(View.GONE);
		}
	}
}
