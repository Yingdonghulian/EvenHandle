package com.hulian.evenhandle.pagerdetail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.hulian.evenhandle.R;
import com.hulian.evenhandle.base.BaseMenuDetailPager;
import com.hulian.evenhandle.bean.ServieceData;
import com.hulian.evenhandle.bean.UserData;
import com.hulian.evenhandle.global.GlobalConnect;
import com.hulian.evenhandle.util.PrefUtils;
import com.lidroid.xutils.ViewUtils;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MineDetail extends BaseMenuDetailPager implements OnClickListener {
	protected static final int REFRESH = 0;
	private UserData resultid;
	private String userid;
	private View user_info_group;
	private View to_login_group;
	private View loginButton;
	private String userinfo;
	private ServieceData resultinfo;
	private TextView UserName;
	private ImageView UserImage;
	private View view;
	private TextView UseraDress;

	public MineDetail(Activity activity) {
		super(activity);
	}

	// 初始化页面
	@Override
	public View initView() {
		view = View.inflate(mActivity, R.layout.mine_detail, null);
		ViewUtils.inject(view);
		user_info_group = (View) view.findViewById(R.id.ll_userinfo);
		to_login_group = (View) view.findViewById(R.id.to_logingroup);
		UserName = (TextView) view.findViewById(R.id.tv_subhead);
		UseraDress = (TextView) view.findViewById(R.id.tv_caption);
		UserImage = (ImageView) view.findViewById(R.id.iv_avatar);
		loginButton = view.findViewById(R.id.login_button);
		loginButton.setOnClickListener(this);
		LodingImage();
		return view;

	}

	// 重写Handler 调用主线程方法保存View
	private static class IHandler extends Handler {
		private final WeakReference<MineDetail> mActivity;

		public IHandler(MineDetail mineDetail) {
			mActivity = new WeakReference<MineDetail>(mineDetail);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				try {
					((MineDetail) mActivity.get()).View(msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
		}
	}

	// 网络获取图片
	public void LodingImage() {
		     Thread thread = new Thread(new Runnable() {
			       public void run() {
				    try {
					String Path = GlobalConnect.USERIMAGE_URL + "userid=" + resultid.userid;
					URL url = new URL(Path);
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(3000);// 连接超时
					connection.setReadTimeout(3000);// 读取超时
					connection.connect();// 设置连接
					if (connection.getResponseCode() == 200) {// 访问成功
						InputStream inputStream = connection.getInputStream();// 获取服务器请求的数据流
						Bitmap bitmap = BitmapFactory.decodeStream(inputStream);// 读取流成生成图片
						/**
						 * 设置消息机制，发送给Handler刷新
						 */
						Message msg = new Message();
						msg.arg1 = resultid.userid;
						msg.obj = bitmap;// 携带数据
						msg.what = 1;// 标志成功
						handler.sendMessage(msg);
					} else {
						Message msg = handler.obtainMessage();
						msg.what = 0;
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
				}
			}
		});
		thread.start();
	}

	// 调用主线程方法
	public void View(Message msg) throws IOException {
		System.out.println("子线程下载图片信息" + msg);
		SaveBitMap(msg);
	}

	// 本地保存图片下载图片
	private void SaveBitMap(Message msg) throws IOException {
		Object picname = msg.obj;
		int picId = msg.arg1;
		File file = new File("/sdcard/DCIM/Camera/" + picId + ".png");
		if (file.exists()) {
			file.delete();
		}
		FileOutputStream out;
		try {
			out = new FileOutputStream(file);
			if (((Bitmap) msg.obj).compress(Bitmap.CompressFormat.PNG, 90, out)) {
				out.flush();
				out.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private IHandler handler = new IHandler(this);

	// 本地获取图片
	private Bitmap GetImageFromSDcard() {
		Bitmap bitmap = null;
		String Parth = "/sdcard/DCIM/Camera/" + resultid.userid + ".png";
		try {
			File file = new File(Parth);
			if (file.exists()) {
				bitmap = BitmapFactory.decodeFile(Parth);
				System.out.println("shuchu" + bitmap);
			}
		} catch (Exception e) {
		}
		return bitmap;
	}

	// 数据加载
	@Override
	public void initData() {
		// 用户ID
		userid = PrefUtils.getString(mActivity, GlobalConnect.USER_URL, "");
		Gson gson = new Gson();
		resultid = gson.fromJson(userid, UserData.class);
		// 用户Info
		userinfo = PrefUtils.getString(mActivity, GlobalConnect.INFO_URL, "");
		Gson Usergson = new Gson();
		resultinfo = Usergson.fromJson(userinfo, ServieceData.class);
		// 页面判断
		if (resultid != null) {
			to_login_group.setVisibility(View.GONE);
			user_info_group.setVisibility(View.VISIBLE);
			UserName.setText(resultinfo.username);
			UseraDress.setText(resultinfo.address);
			UserImage.setImageBitmap(GetImageFromSDcard());
		} else {
			to_login_group.setVisibility(View.VISIBLE);
			user_info_group.setVisibility(View.GONE);
		}
	}

	// 点击事件
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_button:
			Intent intent = new Intent();
			intent.setClass(mActivity, LoginActivity.class);
			mActivity.startActivity(intent);
			break;
		    default:
			break;
		}
	}

}
