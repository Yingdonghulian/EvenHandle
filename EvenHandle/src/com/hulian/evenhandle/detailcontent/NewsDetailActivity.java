package com.hulian.evenhandle.detailcontent;

import com.hulian.evenhandle.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;
/**
 * 新闻详情页
 * @author Liu-
 *
 */
public class NewsDetailActivity extends Activity implements 	OnClickListener{
	private WebView mWebView;
	private ImageButton btnBack;
	private ImageButton btnSize;
	private ImageButton btnShare;
    private ProgressBar pbProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_detail);
		
		mWebView = (WebView) findViewById(R.id.wv_web);
		btnBack = (ImageButton) findViewById(R.id.btn_back);
		btnSize = (ImageButton) findViewById(R.id.btn_size);
		btnShare = (ImageButton) findViewById(R.id.btn_share);
		pbProgress = (ProgressBar) findViewById(R.id.pb_progress);
		
		btnBack.setOnClickListener(this);
		btnSize.setOnClickListener(this);
		btnShare.setOnClickListener(this);
		
		String url = getIntent().getStringExtra("url");
		WebSettings settings = mWebView.getSettings();
		settings.setJavaScriptEnabled(true);//支持JS
		//settings.setBuiltInZoomControls(true);// 显示放大缩小按钮
		settings.setUseWideViewPort(true);// 支持双击缩放
		
		mWebView.setWebViewClient(new WebViewClient(){
			//网页开始加载
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				
				super.onPageStarted(view, url, favicon);
				System.out.println("网页开始加载");
				pbProgress.setVisibility(View.VISIBLE);
			}
			//网页加载结束
			@Override
			public void onPageFinished(WebView view, String url) {
			
				super.onPageFinished(view, url);
				System.out.println("网页结束加载");
				pbProgress.setVisibility(View.GONE);
			}
			//所有跳转的连接 都在此方法中回调
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				System.out.println("跳转URL"+url);
				view.loadUrl(url);
				return true;
			
			}
		});
		
		   mWebView.setWebChromeClient(new WebChromeClient(){
			   //网页进度
			   @Override
			public void onProgressChanged(WebView view, int newProgress) {
				System.out.println("网页进度"+newProgress);
				super.onProgressChanged(view, newProgress);
			}
			   //获取网页标题
			   @Override
			public void onReceivedTitle(WebView view, String title) {
				   System.out.println("网页标题"+title);
				super.onReceivedTitle(view, title);
			}
		   });
			mWebView.loadUrl(url);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
            finish();
			break;
		case R.id.btn_size:

			break;
		case R.id.btn_share:
             showShare();
			break;
		default:
			break;
		}	
	}
	/**
	 * 分享, 注意在sdcard根目录放test.jpg
	 */
	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		//oks.setTheme(OnekeyShareTheme.SKYBLUE);//设置天蓝色的主题	
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		// 分享时Notification的图标和文字
		oks.setNotification(R.drawable.ic_launcher,
				getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(getString(R.string.share));
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("http://sharesdk.cn");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("我是分享文本");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		oks.setImagePath("/sdcard/test.jpg");// 确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://sharesdk.cn");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://sharesdk.cn");

		// 启动分享GUI
		oks.show(this);
	}
}
