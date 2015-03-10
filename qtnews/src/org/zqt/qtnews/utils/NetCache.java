package org.zqt.qtnews.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

/**
 * @author zqt
 * ���绺����
 */
public class NetCache {

	public static final int SUCCESS = 0;
	public static final int FAILED = 1;
	private Handler mHandler; 
	private MemoryCache mMemoryCache; // �ڴ滺�����
	private ExecutorService mExecutorService; // �̳߳ض���

	public NetCache(Handler handler, MemoryCache memoryCache) {
		this.mHandler = handler;
		this.mMemoryCache = memoryCache;
		
		// ����һ���ڲ���5���̵߳��̳߳�
		mExecutorService = Executors.newFixedThreadPool(5);
	}
	
	/**
	 * ��ȡͼƬ��������
	 * @param url
	 */
	public void getBitmapFromNet(String url, int tag) {
//		new Thread(new InternalRunnable(url, tag)).start();
		mExecutorService.execute(new InternalRunnable(url, tag));
	}
	
	class InternalRunnable implements Runnable {
		
		private String url; // ��ǰ������Ҫ����������ַ
		private int tag; // ��ǰ��������ͼƬ�ı�ʶ
		
		public InternalRunnable(String url, int tag) {
			this.url = url;
			this.tag = tag;
		}

		@Override
		public void run() {
			// ��������, ץȡͼƬ
			HttpURLConnection conn = null;
			try {
				conn = (HttpURLConnection) new URL(url).openConnection();
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(5000);
				conn.setReadTimeout(5000);
				conn.connect();
				int responseCode = conn.getResponseCode();
				if(responseCode == 200) {
					InputStream is = conn.getInputStream();
					// ����ת����ͼƬ
					Bitmap bm = BitmapFactory.decodeStream(is);
					
					Message msg = mHandler.obtainMessage();
					msg.obj = bm;
					msg.arg1 = tag;
					msg.what = SUCCESS;
					msg.sendToTarget();
					
					// �򱾵ش�һ��
					LocalCache.putBitmap(url, bm);
					
					// ���ڴ��һ��
					mMemoryCache.putBitmap(url, bm);
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(conn != null) {
					conn.disconnect(); // �Ͽ�����
				}
			}
			mHandler.obtainMessage(FAILED).sendToTarget();
		}
	}
}