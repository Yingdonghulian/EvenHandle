package com.hulian.evenhandle.pagerdetail;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.Gson;
import com.hulian.evenhandle.R;
import com.hulian.evenhandle.bean.ServieceData;
import com.hulian.evenhandle.global.GlobalConnect;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ServiceComment extends Activity implements OnClickListener {
	private Button selectImageBtn;
	private Button button;
	private static final int IMAGE_SELECT = 1;
	private TextView username;
	private TextView useraddress;
	private ImageView img;
	private TextView time;
	public static final int SETMAIN = 1; // 设置一个what标记
	public static final int SETCHILD = 2; // 设置what的标记]
	private Handler mainHandler, childHandler;
	private static final int SET = 1; // 操作的what状态
	String FILE_PATH = "/sdcard/mmym";
	private Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) { // 判断操作的消息类型
			case SET: // 更新组件
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");
				Date data = new Date(System.currentTimeMillis());
				String str = dateFormat.format(data);
				time.setText("" + str);
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.srervice_detail);
		initData();
		username = (TextView) findViewById(R.id.username);
		useraddress = (TextView) findViewById(R.id.useraddress);
		selectImageBtn = (Button) this.findViewById(R.id.select);
		img = (ImageView) findViewById(R.id.img);
		time = (TextView) findViewById(R.id.data);
		selectImageBtn.setOnClickListener(new Click());
		Timer timer = new Timer();
		timer.schedule(new MyTask(), 0, 1000);
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		Bitmap pic = toRoundBitmap(bitmap);
		img.setImageBitmap(pic);

	}

	class Click implements OnClickListener {

		public void onClick(View v) {
			Intent intent = null;
			intent = new Intent();
			intent.setAction("android.media.action.IMAGE_CAPTURE");
			intent.addCategory("android.intent.category.DEFAULT");
			File file = new File(FILE_PATH);
			if (file.exists()) {
				file.delete();
			}
			Uri uri = Uri.fromFile(file);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			startActivityForResult(intent, 0);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("****", "系统相机拍照完成，resultCode=" + resultCode);
		if (requestCode == 0) {
			File file = new File(FILE_PATH);
			Uri uri = Uri.fromFile(file);
			LayoutInflater inflater = LayoutInflater.from(ServiceComment.this);
			ImageView imageView = (ImageView) inflater.inflate(R.layout.repairimager, null);
			LinearLayout frameLayout = (LinearLayout) findViewById(R.id.line);
			frameLayout.addView(imageView);
			imageView.setImageURI(uri);
		} else if (requestCode == 1) {
			Log.i("***8", "默认content地址：" + data.getData());
			LayoutInflater inflater = LayoutInflater.from(ServiceComment.this);
			ImageView imageView = (ImageView) inflater.inflate(R.layout.repairimager, null);
			LinearLayout frameLayout = (LinearLayout) findViewById(R.id.line);
			frameLayout.addView(imageView);
			imageView.setImageURI(data.getData());
		}
	}

	public void onClick(View v) {
		// 如何提取手机的图片的，并且进行选择图片的功能
		Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);// 打开手机的图片库
		startActivityForResult(intent, IMAGE_SELECT);

	}

	private Intent getImageClipIntent() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		// 实现对图片的裁剪，必须要设置图片的属性和大小
		intent.setType("image/*");// 获取任意的图片类型
		intent.putExtra("crop", "true");// 滑动选中图片区域
		intent.putExtra("aspectX", 1);// 表示剪切框的比例1:1的效果
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 80);// 指定输出图片的大小
		intent.putExtra("outputY", 80);
		intent.putExtra("return-data", true);
		return intent;
	}

	public Bitmap toRoundBitmap(Bitmap bitmap) {
		// 圆形图片宽高
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		// 正方形的边长
		int r = 0;
		// 取最短边做边长
		if (width > height) {
			r = height;
		} else {
			r = width;
		}
		// 构建一个bitmap
		Bitmap backgroundBmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		// new一个Canvas，在backgroundBmp上画图
		Canvas canvas = new Canvas(backgroundBmp);
		Paint paint = new Paint();
		// 设置边缘光滑，去掉锯齿
		paint.setAntiAlias(true);
		// 宽高相等，即正方形
		RectF rect = new RectF(0, 0, r, r);
		// 通过制定的rect画一个圆角矩形，当圆角X轴方向的半径等于Y轴方向的半径时，
		// 且都等于r/2时，画出来的圆角矩形就是圆形
		canvas.drawRoundRect(rect, r / 2, r / 2, paint);
		// 设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去掉
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		// canvas将bitmap画在backgroundBmp上
		canvas.drawBitmap(bitmap, null, rect, paint);
		// 返回已经绘画好的backgroundBmp
		return backgroundBmp;
	}

	private class MyTask extends TimerTask {

		@Override
		public void run() {
			Message msg = new Message(); // 设置更新
			msg.what = SET; // 操作的标记
			myHandler.sendMessage(msg); // 发送消息
		}
	}
	public void initData() {
		
		getDataFromServer();
	}

	private void getDataFromServer() {
		// 请求网络数据
		HttpUtils Utlis = new HttpUtils();
		Utlis.send(HttpMethod.POST, GlobalConnect.SERVICE_URL, new RequestCallBack<String>() {
			private ServieceData serviceData;

			// 访问主线程失败
			@Override
			public void onFailure(HttpException error, String msg) {
				// Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
				error.printStackTrace();
			}
			@Override
			public void onSuccess(ResponseInfo<String> responselnfo) {
				// 访问数据成功
				String result = (String) responselnfo.result;
				System.out.println("服务页面数据" + result);
				parseData(result);
			}

			private void parseData(String result) {
				Gson gson = new Gson();
				serviceData = gson.fromJson(result, ServieceData.class);
				System.out.println("服务页面解析数据" + serviceData);
				username.setText(serviceData.username);
				useraddress.setText(serviceData.address);
			}
		});
	}
}