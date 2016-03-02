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
	public static final int SETMAIN = 1; // ����һ��what���
	public static final int SETCHILD = 2; // ����what�ı��]
	private Handler mainHandler, childHandler;
	private static final int SET = 1; // ������what״̬
	String FILE_PATH = "/sdcard/mmym";
	private Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) { // �жϲ�������Ϣ����
			case SET: // �������
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy��MM��dd��  HH:mm:ss");
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
		Log.i("****", "ϵͳ���������ɣ�resultCode=" + resultCode);
		if (requestCode == 0) {
			File file = new File(FILE_PATH);
			Uri uri = Uri.fromFile(file);
			LayoutInflater inflater = LayoutInflater.from(ServiceComment.this);
			ImageView imageView = (ImageView) inflater.inflate(R.layout.repairimager, null);
			LinearLayout frameLayout = (LinearLayout) findViewById(R.id.line);
			frameLayout.addView(imageView);
			imageView.setImageURI(uri);
		} else if (requestCode == 1) {
			Log.i("***8", "Ĭ��content��ַ��" + data.getData());
			LayoutInflater inflater = LayoutInflater.from(ServiceComment.this);
			ImageView imageView = (ImageView) inflater.inflate(R.layout.repairimager, null);
			LinearLayout frameLayout = (LinearLayout) findViewById(R.id.line);
			frameLayout.addView(imageView);
			imageView.setImageURI(data.getData());
		}
	}

	public void onClick(View v) {
		// �����ȡ�ֻ���ͼƬ�ģ����ҽ���ѡ��ͼƬ�Ĺ���
		Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);// ���ֻ���ͼƬ��
		startActivityForResult(intent, IMAGE_SELECT);

	}

	private Intent getImageClipIntent() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		// ʵ�ֶ�ͼƬ�Ĳü�������Ҫ����ͼƬ�����Ժʹ�С
		intent.setType("image/*");// ��ȡ�����ͼƬ����
		intent.putExtra("crop", "true");// ����ѡ��ͼƬ����
		intent.putExtra("aspectX", 1);// ��ʾ���п�ı���1:1��Ч��
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 80);// ָ�����ͼƬ�Ĵ�С
		intent.putExtra("outputY", 80);
		intent.putExtra("return-data", true);
		return intent;
	}

	public Bitmap toRoundBitmap(Bitmap bitmap) {
		// Բ��ͼƬ���
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		// �����εı߳�
		int r = 0;
		// ȡ��̱����߳�
		if (width > height) {
			r = height;
		} else {
			r = width;
		}
		// ����һ��bitmap
		Bitmap backgroundBmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		// newһ��Canvas����backgroundBmp�ϻ�ͼ
		Canvas canvas = new Canvas(backgroundBmp);
		Paint paint = new Paint();
		// ���ñ�Ե�⻬��ȥ�����
		paint.setAntiAlias(true);
		// �����ȣ���������
		RectF rect = new RectF(0, 0, r, r);
		// ͨ���ƶ���rect��һ��Բ�Ǿ��Σ���Բ��X�᷽��İ뾶����Y�᷽��İ뾶ʱ��
		// �Ҷ�����r/2ʱ����������Բ�Ǿ��ξ���Բ��
		canvas.drawRoundRect(rect, r / 2, r / 2, paint);
		// ���õ�����ͼ���ཻʱ��ģʽ��SRC_INΪȡSRCͼ���ཻ�Ĳ��֣�����Ľ���ȥ��
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		// canvas��bitmap����backgroundBmp��
		canvas.drawBitmap(bitmap, null, rect, paint);
		// �����Ѿ��滭�õ�backgroundBmp
		return backgroundBmp;
	}

	private class MyTask extends TimerTask {

		@Override
		public void run() {
			Message msg = new Message(); // ���ø���
			msg.what = SET; // �����ı��
			myHandler.sendMessage(msg); // ������Ϣ
		}
	}
	public void initData() {
		
		getDataFromServer();
	}

	private void getDataFromServer() {
		// ������������
		HttpUtils Utlis = new HttpUtils();
		Utlis.send(HttpMethod.POST, GlobalConnect.SERVICE_URL, new RequestCallBack<String>() {
			private ServieceData serviceData;

			// �������߳�ʧ��
			@Override
			public void onFailure(HttpException error, String msg) {
				// Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
				error.printStackTrace();
			}
			@Override
			public void onSuccess(ResponseInfo<String> responselnfo) {
				// �������ݳɹ�
				String result = (String) responselnfo.result;
				System.out.println("����ҳ������" + result);
				parseData(result);
			}

			private void parseData(String result) {
				Gson gson = new Gson();
				serviceData = gson.fromJson(result, ServieceData.class);
				System.out.println("����ҳ���������" + serviceData);
				username.setText(serviceData.username);
				useraddress.setText(serviceData.address);
			}
		});
	}
}