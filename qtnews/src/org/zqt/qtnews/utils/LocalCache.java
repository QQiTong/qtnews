package org.zqt.qtnews.utils;

import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;

public class LocalCache {
	
	private static final File CACHEDIR = new File("/mnt/sdcard/itheima42");

	/**
	 * 根据url, 向本地存储一张图片
	 * @param url
	 * @param bm
	 */
	public static void putBitmap(String url, Bitmap bm) {
		try {
			String fileName = MD5Encoder.encode(url).substring(0, 10);
			if(!CACHEDIR.exists()) {
				CACHEDIR.mkdir();
			}
			File cacheFile = new File(CACHEDIR, fileName);
			FileOutputStream fos = new FileOutputStream(cacheFile);
			bm.compress(CompressFormat.JPEG, 100, fos);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据url从本地缓存中取出图片
	 * @param url
	 * @return
	 */
	public static Bitmap getBitmap(String url) {
		try {
			String fileName = MD5Encoder.encode(url).substring(0, 10);
			File cacheFile = new File(CACHEDIR, fileName);
			if(cacheFile.exists()) {
				return BitmapFactory.decodeFile(cacheFile.getPath());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
