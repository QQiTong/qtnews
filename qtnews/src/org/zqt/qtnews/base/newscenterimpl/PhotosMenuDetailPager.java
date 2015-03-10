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
 * ��ͼ�˵���Ӧ��ҳ��
 */
public class PhotosMenuDetailPager extends MenuDetailBasePager {

	private ListView mListView;
	private GridView mGridView;
	private List<PhotosItem> photosList; // ��ͼ����
	private boolean isDisplayList = true; // �Ƿ���ʾ�����б�ҳ��, Ĭ��Ϊ: �б�ҳ
	private ImageUtils mImageUtils; // ͼƬ������������
	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case NetCache.SUCCESS:
				Bitmap bm = (Bitmap) msg.obj; // ͼƬ
				int tag = msg.arg1; // ��ǰץȡͼƬ���Ǹ�ImageView��tag
				
				ImageView iv = (ImageView) mListView.findViewWithTag(tag);
				iv.setImageBitmap(bm);
				break;
			case NetCache.FAILED:
				Toast.makeText(mContext, "ץȡͼƬʧ��", 0).show();
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
				System.out.println("��ͼ��������ɹ�: " + responseInfo.result);
				
				CacheUtils.putString(mContext, Constants.PHOTOS_URL, responseInfo.result);
				
				processData(responseInfo.result);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				System.out.println("��ͼ��������ʧ��: " + msg);
			}
		});
	}

	/**
	 * ��������������
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
			
			// Ϊ�˷���ͼƬ����, ��ivImage����һ��Ĭ�ϵ�ͼƬ
			mHolder.ivImage.setImageResource(R.drawable.pic_item_list_default);
			
			// ��imageView��һ��tag(��ʶ)
			mHolder.ivImage.setTag(position);
			
			// ȡͼƬ.
			Bitmap bitmap = mImageUtils.getImageFromUrl(photosItem.listimage, position);
			if(bitmap != null) {
				// ��ǰ�Ǵ��ڴ���߱���ȡ��ͼƬ
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
	 * �л���ǰҳ�����ʾ, �����listҳ���л���grid. ͬʱ��Ҫ�Ѱ�ť�ı����޸���
	 * @param ibListOrGrid
	 */
	public void switchCurrentPager(ImageButton ibListOrGrid) {
		if(isDisplayList) {
			// �л���gridҳ��
			mListView.setVisibility(View.GONE);
			mGridView.setVisibility(View.VISIBLE);
			mGridView.setAdapter(new PhotosAdapter());
			isDisplayList = false;
			ibListOrGrid.setImageResource(R.drawable.icon_pic_list_type);
		} else {
			// �л���listҳ��
			mListView.setVisibility(View.VISIBLE);
			mGridView.setVisibility(View.GONE);
			mListView.setAdapter(new PhotosAdapter());
			isDisplayList = true;
			ibListOrGrid.setImageResource(R.drawable.icon_pic_grid_type);
		}
		
	}
}