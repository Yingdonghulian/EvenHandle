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
 * ��������ҳ
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
		settings.setJavaScriptEnabled(true);//֧��JS
		//settings.setBuiltInZoomControls(true);// ��ʾ�Ŵ���С��ť
		settings.setUseWideViewPort(true);// ֧��˫������
		
		mWebView.setWebViewClient(new WebViewClient(){
			//��ҳ��ʼ����
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				
				super.onPageStarted(view, url, favicon);
				System.out.println("��ҳ��ʼ����");
				pbProgress.setVisibility(View.VISIBLE);
			}
			//��ҳ���ؽ���
			@Override
			public void onPageFinished(WebView view, String url) {
			
				super.onPageFinished(view, url);
				System.out.println("��ҳ��������");
				pbProgress.setVisibility(View.GONE);
			}
			//������ת������ ���ڴ˷����лص�
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				System.out.println("��תURL"+url);
				view.loadUrl(url);
				return true;
			
			}
		});
		
		   mWebView.setWebChromeClient(new WebChromeClient(){
			   //��ҳ����
			   @Override
			public void onProgressChanged(WebView view, int newProgress) {
				System.out.println("��ҳ����"+newProgress);
				super.onProgressChanged(view, newProgress);
			}
			   //��ȡ��ҳ����
			   @Override
			public void onReceivedTitle(WebView view, String title) {
				   System.out.println("��ҳ����"+title);
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
	 * ����, ע����sdcard��Ŀ¼��test.jpg
	 */
	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		//oks.setTheme(OnekeyShareTheme.SKYBLUE);//��������ɫ������	
		// �ر�sso��Ȩ
		oks.disableSSOWhenAuthorize();
		// ����ʱNotification��ͼ�������
		oks.setNotification(R.drawable.ic_launcher,
				getString(R.string.app_name));
		// title���⣬ӡ��ʼǡ����䡢��Ϣ��΢�š���������QQ�ռ�ʹ��
		oks.setTitle(getString(R.string.share));
		// titleUrl�Ǳ�����������ӣ�������������QQ�ռ�ʹ��
		oks.setTitleUrl("http://sharesdk.cn");
		// text�Ƿ����ı�������ƽ̨����Ҫ����ֶ�
		oks.setText("���Ƿ����ı�");
		// imagePath��ͼƬ�ı���·����Linked-In�����ƽ̨��֧�ִ˲���
		oks.setImagePath("/sdcard/test.jpg");// ȷ��SDcard������ڴ���ͼƬ
		// url����΢�ţ��������Ѻ�����Ȧ����ʹ��
		oks.setUrl("http://sharesdk.cn");
		// comment���Ҷ�������������ۣ�������������QQ�ռ�ʹ��
		oks.setComment("���ǲ��������ı�");
		// site�Ƿ�������ݵ���վ���ƣ�����QQ�ռ�ʹ��
		oks.setSite(getString(R.string.app_name));
		// siteUrl�Ƿ�������ݵ���վ��ַ������QQ�ռ�ʹ��
		oks.setSiteUrl("http://sharesdk.cn");

		// ��������GUI
		oks.show(this);
	}
}
