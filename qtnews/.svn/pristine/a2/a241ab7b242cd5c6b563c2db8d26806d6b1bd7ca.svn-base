package org.zqt.qtnews.base.newscenterimpl;

import java.util.List;

import org.zqt.qtnews.R;
import org.zqt.qtnews.base.MenuDetailBasePager;
import org.zqt.qtnews.domain.PhotosBean;
import org.zqt.qtnews.domain.NewsCenterBean.NewsCenterData;
import org.zqt.qtnews.domain.PhotosBean.PhotosItem;
import org.zqt.qtnews.utils.CacheUtils;
import org.zqt.qtnews.utils.Constants;
import org.zqt.qtnews.utils.ImageUtils;
import org.zqt.qtnews.utils.NetCache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * @author zqt
 * 组图菜单对应的页面
 */
public class PhotosMenuDetailPager extends MenuDetailBasePager {

	private ListView mListView;
	private GridView mGridView;
	private List<PhotosItem> photosList; // 组图数据
	private boolean isDisplayList = true; // 是否显示的是列表页面, 默认为: 列表页
	private ImageUtils mImageUtils; // 图片三级缓存框架类
	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case NetCache.SUCCESS:
				Bitmap bm = (Bitmap) msg.obj; // 图片
				int tag = msg.arg1; // 当前抓取图片的那个ImageView的tag
				
				ImageView iv = (ImageView) mListView.findViewWithTag(tag);
				iv.setImageBitmap(bm);
				break;
			case NetCache.FAILED:
				Toast.makeText(mContext, "抓取图片失败", 0).show();
				break;
			default:
				break;
			}
			
		}
	};

	public PhotosMenuDetailPager(Context context) {
		super(context);
	}

	public PhotosMenuDetailPager(Context context, NewsCenterData newsCenterData) {
		super(context);
	}

	@Override
	public View initView() {
		View view = View.inflate(mContext, R.layout.photos, null);
		mListView = (ListView) view.findViewById(R.id.lv_photos);
		mGridView = (GridView) view.findViewById(R.id.gv_photos);
		return view;
	}

	@Override
	public void initData() {
		mImageUtils = new ImageUtils(handler);
		
		String json = CacheUtils.getString(mContext, Constants.PHOTOS_URL, null);
		if(!TextUtils.isEmpty(json)) {
			processData(json);
		}
		
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, Constants.PHOTOS_URL, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				System.out.println("组图数据请求成功: " + responseInfo.result);
				
				CacheUtils.putString(mContext, Constants.PHOTOS_URL, responseInfo.result);
				
				processData(responseInfo.result);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				System.out.println("组图数据请求失败: " + msg);
			}
		});
	}

	/**
	 * 解析并处理数据
	 * @param result
	 */
	protected void processData(String result) {
		Gson gson = new Gson();
		PhotosBean bean = gson.fromJson(result, PhotosBean.class);
		
		photosList = bean.data.news;
		PhotosAdapter mAdapter = new PhotosAdapter();
		mListView.setAdapter(mAdapter);
	}
	
	class PhotosAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return photosList.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			PhotosViewHolder mHolder = null;
			
			if(convertView == null) {
				convertView = View.inflate(mContext, R.layout.photos_item, null);
				mHolder = new PhotosViewHolder();
				mHolder.ivImage = (ImageView) convertView.findViewById(R.id.iv_photos_item);
				mHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_photos_item);
				convertView.setTag(mHolder);
			} else {
				mHolder = (PhotosViewHolder) convertView.getTag();
			}
			
			PhotosItem photosItem = photosList.get(position);
			mHolder.tvTitle.setText(photosItem.title);
			
			// 为了放置图片错乱, 给ivImage设置一张默认的图片
			mHolder.ivImage.setImageResource(R.drawable.pic_item_list_default);
			
			// 给imageView打一个tag(标识)
			mHolder.ivImage.setTag(position);
			
			// 取图片.
			Bitmap bitmap = mImageUtils.getImageFromUrl(photosItem.listimage, position);
			if(bitmap != null) {
				// 当前是从内存或者本地取的图片
				mHolder.ivImage.setImageBitmap(bitmap);
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
	
	class PhotosViewHolder {
		
		public ImageView ivImage;
		public TextView tvTitle;
	}

	/**
	 * 切换当前页面的显示, 如果是list页面切换成grid. 同时需要把按钮的背景修改了
	 * @param ibListOrGrid
	 */
	public void switchCurrentPager(ImageButton ibListOrGrid) {
		if(isDisplayList) {
			// 切换到grid页面
			mListView.setVisibility(View.GONE);
			mGridView.setVisibility(View.VISIBLE);
			mGridView.setAdapter(new PhotosAdapter());
			isDisplayList = false;
			ibListOrGrid.setImageResource(R.drawable.icon_pic_list_type);
		} else {
			// 切换到list页面
			mListView.setVisibility(View.VISIBLE);
			mGridView.setVisibility(View.GONE);
			mListView.setAdapter(new PhotosAdapter());
			isDisplayList = true;
			ibListOrGrid.setImageResource(R.drawable.icon_pic_grid_type);
		}
		
	}
}
