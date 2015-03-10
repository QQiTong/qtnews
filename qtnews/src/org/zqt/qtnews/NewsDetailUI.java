package org.zqt.qtnews;

import org.zqt.qtnews.utils.ShareUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class NewsDetailUI extends Activity implements OnClickListener {

	private String url;
	private int tempSelectTextSizePosition; // 在对话框中临时选择的字体
	private int currentSelectTextSizePosition = 2; // 当前选中的字体, 默认为: 正常大小
	private WebSettings settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.news_detail);
		
		Intent intent = getIntent();
		url = intent.getStringExtra("url");
//		url = "http://10.0.2.2:8080";
		initView();
	}

	private void initView() {
		findViewById(R.id.ib_title_bar_menu).setVisibility(View.GONE);
		findViewById(R.id.tv_title_bar_title).setVisibility(View.GONE);

		findViewById(R.id.ib_title_bar_back).setVisibility(View.VISIBLE);
		findViewById(R.id.ib_title_bar_textsize).setVisibility(View.VISIBLE);
		findViewById(R.id.ib_title_bar_share).setVisibility(View.VISIBLE);

		findViewById(R.id.ib_title_bar_back).setOnClickListener(this);
		findViewById(R.id.ib_title_bar_textsize).setOnClickListener(this);
		findViewById(R.id.ib_title_bar_share).setOnClickListener(this);
		
		WebView mWebView = (WebView) findViewById(R.id.wv_news_detail);
		final ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.pb_news_detail);
		
		// 配置WebView相关信息
		settings = mWebView.getSettings();
		settings.setJavaScriptEnabled(true); // 启用javascript脚本
		// 界面上放大和缩小按钮控制器
		settings.setBuiltInZoomControls(true);
		// 启用双击放大或者缩小
		settings.setUseWideViewPort(true);
		
		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageFinished(WebView view, String url) {
				// 当前页面加载完成了, 需要把进度圈隐藏了
				mProgressBar.setVisibility(View.GONE);
//				System.out.println("页面加载完成了");
			}
		});
		
		mWebView.loadUrl(url); // 加载指定连接地址的数据
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_title_bar_back:
			finish();
			break;
		case R.id.ib_title_bar_textsize:
			showTextSizeDialog();
			break;
		case R.id.ib_title_bar_share:
			ShareUtils utils = new ShareUtils();
			utils.showShare(this, "天王盖地虎!!" + url, "/mnt/sdcard/xinlang.jpg");
			break;
		default:
			break;
		}
	}

	/**
	 * 弹出改变字体的对话框
	 */
	private void showTextSizeDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("选择字体大小");
		String[] items = {"超大号字体", "大号字体", "正常字体", "小号字体", "超小号字体"};
		
		tempSelectTextSizePosition = currentSelectTextSizePosition;
		builder.setSingleChoiceItems(items, currentSelectTextSizePosition, 
				new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				tempSelectTextSizePosition = which;
			}
		});
		
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				currentSelectTextSizePosition = tempSelectTextSizePosition;
				changeWebViewTextSize();
			}
		});
		builder.setNegativeButton("取消", null);
		
		builder.show();
	}

	/**
	 * 根据currentSelectTextSizePosition变量来改变字体
	 */
	protected void changeWebViewTextSize() {
		switch (currentSelectTextSizePosition) {
		case 0:
			settings.setTextSize(TextSize.LARGEST);
			break;
		case 1:
			settings.setTextSize(TextSize.LARGER);
			break;
		case 2:
			settings.setTextSize(TextSize.NORMAL);
			break;
		case 3:
			settings.setTextSize(TextSize.SMALLER);
			break;
		case 4:
			settings.setTextSize(TextSize.SMALLEST);
			break;
		default:
			break;
		}
	}
}
