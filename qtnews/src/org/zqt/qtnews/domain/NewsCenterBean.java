package org.zqt.qtnews.domain;

import java.util.List;

public class NewsCenterBean {

	public int retcode;
	public List<NewsCenterData> data;
	public List<String> extend;
	
	public class NewsCenterData {
		
		public List<ChildRen> children;
		public int id;
		public String title;
		public int type;
		public String url;
		public String url1;
		public String dayurl;
		public String excurl;
		public String weekurl;
		@Override
		public String toString() {
			return "NewsCenterData [children=" + children + ", id=" + id
					+ ", title=" + title + ", type=" + type + ", url=" + url
					+ ", url1=" + url1 + ", dayurl=" + dayurl + ", excurl="
					+ excurl + ", weekurl=" + weekurl + "]";
		}
	}
	
	public class ChildRen {
		
		public int id;
		public int type;
		public String title;
		public String url;
	}
}
