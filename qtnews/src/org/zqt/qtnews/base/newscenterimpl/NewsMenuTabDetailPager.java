package org.zqt.qtnews.base.newscenterimpl;

import java.util.List;

import org.zqt.qtnews.NewsDetailUI;
import org.zqt.qtnews.R;
import org.zqt.qtnews.base.MenuDetailBasePager;
import org.zqt.qtnews.domain.TabDetailBean;
import org.zqt.qtnews.domain.NewsCenterBean.ChildRen;
import org.zqt.qtnews.domain.TabDetailBean.News;
import org.zqt.qtnews.domain.TabDetailBean.TopNew;
import org.zqt.qtnews.utils.CacheUtils;
import org.zqt.qtnews.utils.Constants;
import org.zqt.qtnews.view.HorizontalScrollViewPager;
import org.zqt.qtnews.view.RefreshListView;
import org.zqt.qtnews.view.RefreshListView.OnRefreshListener;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Bitmap.Config;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @author zqt
 * ���Ų˵�-ҳǩ����ҳ
 */
public class NewsMenuTabDetailPager extends MenuDetailBasePager implements OnPageChangeListener, 
	OnRefreshListener, OnItemClickListener {

	@ViewInject(R.id.hsvp_news_menu_tab_detail_top_news)
	private HorizontalScrollViewPager mViewPager;
	
	@ViewInject(R.id.tv_news_menu_tab_detail_description)
	private TextView tvDescription;
	
	@ViewInject(R.id.ll_news_menu_tab_detail_point_group)
	private LinearLayout llPointGroup;
	
	@ViewInject(R.id.rlv_news_menu_tab_detail_list_news)
	private RefreshListView mListView;

	private ChildRen mChildRen; // ��ǰҳǩ����ҳ�������.
 
	private String url; // ��ǰҳ���url
	
	private List<TopNew> topNewList; // �����ֲ�ͼ���ŵ�����

	private BitmapUtils bitmapUtils; // ͼƬ���ʿ��
	
	private int previousEnabledPosition; // ǰһ��ѡ�е������
	
	private InternalHandler mHandler;

	private List<News> newsList; // �б����ŵ�����

	private HttpUtils httpUtils;

	private TopNewAdapter topNewAdapter; // �����ֲ�ͼ��������

	private NewsAdapter newsAdapter; // �б����ŵ�������

	private String moreUrl; // �������ݵ�url
	
	// ����������id����
	private final String READ_NEWS_ID_ARRAY_KEY = "read_news_id_array";
	
	public NewsMenuTabDetailPager(Context context) {
		super(context);
	}

	public NewsMenuTabDetailPager(Context context, ChildRen childRen) {
		super(context);
		
		this.mChildRen = childRen;
		
		bitmapUtils = new BitmapUtils(mContext);
		bitmapUtils.configDefaultBitmapConfig(Config.ARGB_4444);
	}

	@Override
	public View initView() {
		View view = View.inflate(mContext, R.layout.news_menu_tab_detail, null);
		ViewUtils.inject(this, view); // ��viewע�뵽xutils�����
		
		View topNewsView = View.inflate(mContext, R.layout.news_menu_tab_detail_topnews, null);
		ViewUtils.inject(this, topNewsView); // ��viewע�뵽xutils�����
		
		// ��ListView�ĵ�0����Ŀ����������һ������.
		// mListView.addHeaderView(topNewsView);
		mListView.addCustomHeaderView(topNewsView);
		mListView.isEnabledPullDownRefresh(true); // ��������ˢ��
		mListView.isEnabledLoadingMore(true); // ��������ˢ��
		mListView.setOnRefreshListener(this);
		mListView.setOnItemClickListener(this);
		return view;
	}

	@Override
	public void initData() {
		url = Constants.SERVICE_URL + mChildRen.url;
//		System.out.println(mChildRen.title + "���������ӵ�ַ: " + url);
		
		String json = CacheUtils.getString(mContext, url, null);
		if(!TextUtils.isEmpty(json)) {
			processData(json);
		}
		
		getDataFromNet();
	}

	/**
	 * ����·��ȡ����
	 */
	private void getDataFromNet() {
		httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				System.out.println(mChildRen.title + "��������ɹ���: " + responseInfo.result);
				
				// �����ݴ洢����
				CacheUtils.putString(mContext, url, responseInfo.result);
				
				processData(responseInfo.result);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				System.out.println(mChildRen.title + "��������ʧ����: " + msg);
			}
		});
	}
	
	/**
	 * ����json������, ��װ��һ��bean, ����.
	 * @param json
	 * @return
	 */
	private TabDetailBean parserJSON(String json) {
		Gson gson = new Gson();
		TabDetailBean bean = gson.fromJson(json, TabDetailBean.class);
		moreUrl = bean.data.more;
		if(!TextUtils.isEmpty(moreUrl)) {
			moreUrl = Constants.SERVICE_URL + moreUrl;
		}
		return bean;
	}

	/**
	 * ��������������
	 * @param result
	 */
	protected void processData(String result) {
		TabDetailBean bean = parserJSON(result);
		
		// ��ʼ���������ŵ�����
		topNewList = bean.data.topnews;
		if(topNewAdapter == null) {
			topNewAdapter = new TopNewAdapter();
			mViewPager.setAdapter(topNewAdapter);
			mViewPager.setOnPageChangeListener(this);
		} else {
			topNewAdapter.notifyDataSetChanged();
		}
		
		// ��ʼ��ͼƬ�������͵�
		llPointGroup.removeAllViews();
		for (int i = 0; i < topNewList.size(); i++) {
			View view = new View(mContext);
			view.setBackgroundResource(R.drawable.tab_detail_top_news_point_bg);
			LayoutParams params = new LayoutParams(5, 5);
			if(i != 0) {
				params.leftMargin = 10;
			}
			view.setLayoutParams(params);
			view.setEnabled(false);
			llPointGroup.addView(view);
		}
		previousEnabledPosition = 0;
		tvDescription.setText(topNewList.get(previousEnabledPosition).title);
		llPointGroup.getChildAt(previousEnabledPosition).setEnabled(true);
		
		// ��̬�����ֲ�ͼ�л�����.
		/**
		 * -> 1.ʹ��handlerִ��һ����ʱ����: postDelayed
		 * -> 2.������runnable��run�����ᱻִ��
		 * -> 3.ʹ��handler����һ����Ϣ 
		 * -> 4.Handler���handleMessage�������յ���Ϣ.
		 * -> 5.��handleMessage������, ��ViewPager��ҳ���л�����һ��, ͬʱ:1.ʹ��handlerִ��һ����ʱ���� 
		 */
		if(mHandler == null) {
			mHandler = new InternalHandler();
		}
		
		mHandler.removeCallbacksAndMessages(null); // �����е���Ϣ���������
		mHandler.postDelayed(new AutoSwitchPagerRunnable(), 4000);
		
		// ��ʼ���б����ŵ�����
		newsList = bean.data.news;
		if(newsAdapter == null) {
			newsAdapter = new NewsAdapter();
			mListView.setAdapter(newsAdapter);
		} else {
			newsAdapter.notifyDataSetChanged();
		}
	}
	
	class NewsAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return newsList.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			NewsViewHolder mHolder = null;
			if(convertView == null) {
				convertView = View.inflate(mContext, R.layout.news_menu_tab_detail_news_item, null);
				mHolder = new NewsViewHolder();
				mHolder.ivImage = (ImageView) convertView.findViewById(R.id.iv_news_menu_tab_detail_news_item_image);
				mHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_news_menu_tab_detail_news_item_title);
				mHolder.tvDate = (TextView) convertView.findViewById(R.id.tv_news_menu_tab_detail_news_item_date);
				convertView.setTag(mHolder);
			} else {
				mHolder = (NewsViewHolder) convertView.getTag();
			}
			
			// ���ؼ���ֵ.
			News news = newsList.get(position);
			mHolder.tvTitle.setText(news.title);
			mHolder.tvDate.setText(news.pubdate);
			bitmapUtils.display(mHolder.ivImage, news.listimage);
			
			// �жϵ�ǰ���������Ƿ��Ѿ�����
			String readIDArray = CacheUtils.getString(mContext, READ_NEWS_ID_ARRAY_KEY, null);
			if(!TextUtils.isEmpty(readIDArray)
					&& readIDArray.contains(news.id)) {
				mHolder.tvTitle.setTextColor(Color.GRAY);
			} else {
				mHolder.tvTitle.setTextColor(Color.BLACK);
			}
			
			return convertView;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
	}
	
	class NewsViewHolder {
		
		public ImageView ivImage;
		public TextView tvTitle;
		public TextView tvDate;
	}
	
	class TopNewAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return topNewList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView iv = new ImageView(mContext);
			iv.setScaleType(ScaleType.FIT_XY);
			iv.setImageResource(R.drawable.home_scroll_default);
			iv.setOnTouchListener(new TopNewItemTouchListener());
			
			String topImageUrl = topNewList.get(position).topimage;
			/**
			 * ��һ�������� ��ͼƬ��Ҫ��ʾ����һ���ؼ���: iv.setImageBitmap
			 * �ڶ��������� ͼƬ��url��ַ
			 */
			bitmapUtils.display(iv, topImageUrl);
			
			container.addView(iv);
			return iv;
		}
	}
	
	class TopNewItemTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				System.out.println("ֹͣ����");
				mHandler.removeCallbacksAndMessages(null);
				break;
			case MotionEvent.ACTION_UP:
				System.out.println("��ʼ����");
				mHandler.postDelayed(new AutoSwitchPagerRunnable(), 4000);
				break;
			default:
				break;
			}
			return true;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int position) {
		llPointGroup.getChildAt(previousEnabledPosition).setEnabled(false);
		llPointGroup.getChildAt(position).setEnabled(true);
		tvDescription.setText(topNewList.get(position).title);
		previousEnabledPosition = position;
	}
	
	/**
	 * @author zqt
	 * �ڲ���handler
	 */
	class InternalHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
//			System.out.println("�ֲ�ͼ�л�ҳ����");
			int currentItem = mViewPager.getCurrentItem() + 1;
			mViewPager.setCurrentItem(currentItem % topNewList.size());
			
			mHandler.postDelayed(new AutoSwitchPagerRunnable(), 4000);
		}
	}
	
	/**
	 * @author zqt
	 * �Զ��л�ҳ��������
	 */
	class AutoSwitchPagerRunnable implements Runnable {

		@Override
		public void run() {
			mHandler.obtainMessage().sendToTarget();
		}
	}

	@Override
	public void onPullDownRefresh() {
		httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				mListView.onRefreshFinish(); // ˢ����ɰ�ͷ������.
				System.out.println(mChildRen.title + "���ݼ��سɹ�: " + responseInfo.result);
				
				CacheUtils.putString(mContext, url, responseInfo.result);
				
				processData(responseInfo.result);
				
				Toast.makeText(mContext, "����ˢ�����", 0).show();
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				mListView.onRefreshFinish(); // ˢ����ɰ�ͷ������.
				System.out.println(mChildRen.title + "���ݼ���ʧ��: " + msg);
			}
		});
	}

	@Override
	public void onLoadingMore() {
		// �鿴�Ƿ��и�������
		if(TextUtils.isEmpty(moreUrl)) {
			mListView.onRefreshFinish(); // �ѽŲ�������
			Toast.makeText(mContext, "û�и���������", 0).show();
		} else {
			// ���ظ�������
			httpUtils.send(HttpMethod.GET, moreUrl, new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					mListView.onRefreshFinish(); // �ѽŲ�������
					Toast.makeText(mContext, "���ظ���ɹ�", 0).show();
					
					// �����¼��س���������, ���ӵ�newsList������. ˢ��ListView
					TabDetailBean bean = parserJSON(responseInfo.result);
					newsList.addAll(bean.data.news);
					newsAdapter.notifyDataSetChanged();
				}

				@Override
				public void onFailure(HttpException error, String msg) {
					mListView.onRefreshFinish(); // �ѽŲ�������
					Toast.makeText(mContext, "���ظ���ʧ��", 0).show();
				}
			});
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
//		System.out.println("position: " + position);
		
		News news = newsList.get(position -1);
		// �ѵ�ǰ��������������ŵ�id������ 1#2#3#4
		
		String readNewsIDArray = CacheUtils.getString(mContext, READ_NEWS_ID_ARRAY_KEY, null);
		
		String idArray = null;
		if(TextUtils.isEmpty(readNewsIDArray)) {
			idArray = news.id;
		} else {
			idArray = readNewsIDArray + "#" + news.id;
		}
		CacheUtils.putString(mContext, READ_NEWS_ID_ARRAY_KEY, idArray);
		
		// ֪ͨListViewˢ��
		newsAdapter.notifyDataSetChanged();
		
		Intent intent = new Intent(mContext, NewsDetailUI.class);
		intent.putExtra("url", news.url);
		mContext.startActivity(intent);
	}
}