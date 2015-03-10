package org.zqt.qtnews.utils;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.util.Xml;

public class MemoryCache {
	public LruCache<String, Bitmap> mMemoryCache;
	public MemoryCache() {

		int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 8);
		// 定义的缓存大小为 运行时内存的八分之一
		mMemoryCache = new LruCache<String, Bitmap>(maxMemory) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				
				return value.getRowBytes() * value.getHeight();
			}
		};
	}
	
	public void putBitmap(String url, Bitmap bm) {
		mMemoryCache.put(url, bm);
	}
	
	public Bitmap getBitmap(String url) {
		return mMemoryCache.get(url);
	}
}
