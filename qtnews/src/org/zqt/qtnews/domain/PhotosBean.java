package org.zqt.qtnews.domain;

import java.util.List;

public class PhotosBean {

	public int retcode;
	public PhotosData data;
	
	public class PhotosData {

		public String countcommenturl;
		public String more;
		public String title;
		public List<PhotosItem> news;
		public List<String> topic;
	}
	
	public class PhotosItem {

		public String comment;
		public String commentlist;
		public String commenturl;
		public String id;
		public String largeimage;
		public String listimage;
		public String pubdate;
		public String smallimage;
		public String title;
		public String type;
		public String url;
		
	}
}
