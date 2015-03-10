package org.zqt.qtnews.utils;

import android.graphics.Bitmap;
import android.os.Handler;

public class ImageUtils {
	
	private NetCache mNetCache;
	private MemoryCache mMemoryCache;

	public ImageUtils(Handler handler) {
		// �����ڴ滺�����
		mMemoryCache = new MemoryCache();
		
		// �������绺�����.
		mNetCache = new NetCache(handler, mMemoryCache);
	}

	/**
	 * ����url��ȡͼƬ
	 * @param url
	 * @return
	 */
	public Bitmap getImageFromUrl(String url, int tag) {
		// 1. ȥ�ڴ���ȡ, ȡ����֮��ֱ�ӷ���.
		Bitmap bm = mMemoryCache.getBitmap(url);
		if(bm != null) {
			System.out.println("���ڴ���ȡ");
			return bm;
		}
		
		// 2. ȥ������ȡ, ȡ����֮��ֱ�ӷ���.
		bm = LocalCache.getBitmap(url);
		if(bm != null) {
			System.out.println("�ӱ�����ȡ");
			return bm;
		}

		System.out.println("��������ȡ");
		// 3. ȥ������ȡ, �������߳��첽ץȡ, ����ֱ�ӷ���. ��ץȡ��Ϻ�, �õ�ͼƬ, ʹ��handler������Ϣ����������.
		mNetCache.getBitmapFromNet(url, tag);
		return null;
	}
	
}
