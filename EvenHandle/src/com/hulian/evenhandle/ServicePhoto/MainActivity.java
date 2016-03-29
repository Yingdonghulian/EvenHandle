package com.hulian.evenhandle.ServicePhoto;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.Gson;
import com.hulian.evenhandle.R;
import com.hulian.evenhandle.ServicePhoto.util.Bimp;
import com.hulian.evenhandle.ServicePhoto.util.FileUtils;
import com.hulian.evenhandle.ServicePhoto.util.ImageItem;
import com.hulian.evenhandle.ServicePhoto.util.PublicWay;
import com.hulian.evenhandle.ServicePhoto.util.Res;
import com.hulian.evenhandle.bean.ServieceData;
import com.hulian.evenhandle.bean.UserData;
import com.hulian.evenhandle.global.GlobalConnect;
import com.hulian.evenhandle.util.PrefUtils;
import com.hulian.evenhandle.util.SendPostData;
import com.lidroid.xutils.view.annotation.ContentView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.UserManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 首页面activity
 * 
 */
public class MainActivity extends Activity implements OnClickListener {
	private GridView noScrollgridview;
	private GridAdapter adapter;
	private View parentView;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	private UserData resultid;
	public static Bitmap bimap;
	private String userid;
	private String userinfor;
	private ServieceData resultinfor;
	private Button sendMessage;
	private ImageButton butBack;
	private String path;
	private static final int TAKE_PICTURE = 0x000001;
	private Spinner selectSpinner;
	private EditText userMessage;
	private ImageItem takePhoto;
	private String fileName;
	private int resultCode;
	private String takepath;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Res.init(this);
		bimap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused);
		PublicWay.activityList.add(this);
		parentView = getLayoutInflater().inflate(R.layout.activity_selectimg, null);
		setContentView(parentView);
		Init();
		initData();

	}

	private void initView() {
		TextView username = (TextView) findViewById(R.id.username);
		username.setText(resultinfor.username);
		TextView useraddress = (TextView) findViewById(R.id.useraddress);
		useraddress.setText(resultinfor.address);
		ImageView UserImage = (ImageView) findViewById(R.id.img);
		UserImage.setImageBitmap(getImageFromSDcard());
		sendMessage = (Button) findViewById(R.id.activity_selectimg_send);
		selectSpinner = (Spinner) findViewById(R.id.type);
		userMessage = (EditText) findViewById(R.id.repair_edt);
		butBack = (ImageButton) findViewById(R.id.repair_butback);
		sendMessage.setOnClickListener(this);
		butBack.setOnClickListener(this);
	}

	// 获取本地数据
	private void initData() {
		userid = PrefUtils.getString(getApplicationContext(), GlobalConnect.USER_URL, "");
		userinfor = PrefUtils.getString(getApplicationContext(), GlobalConnect.INFO_URL, "");
		PareData(userinfor, userid);
		initView();
		getImageFromSDcard();
	}

	// 读取本地保存的用户头像
	private Bitmap getImageFromSDcard() {
		Bitmap bitmap = null;
		String Parth = "/sdcard/DCIM/Camera/" + resultid.userid + ".png";
		try {
			File file = new File(Parth);
			if (file.exists()) {
				bitmap = BitmapFactory.decodeFile(Parth);
			}
		} catch (Exception e) {
		}
		return bitmap;
	}

	// 获取个人数据
	private void PareData(String userinfor, String userid) {
		Gson Gsoninfo = new Gson();
		resultinfor = Gsoninfo.fromJson(userinfor, ServieceData.class);
		System.out.println("服务用户个人数据" + resultinfor);
		Gson GsonId = new Gson();
		resultid = GsonId.fromJson(userid, UserData.class);
		System.out.println("服务用户数据" + resultid);
	}

	// 调用方法进入选择照片获取方式
	public void Init() {
		pop = new PopupWindow(MainActivity.this);
		View view = getLayoutInflater().inflate(R.layout.item_popupwindows, null);
		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);
		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
		parent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				photo();
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, AlbumActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		// 图片获取显示 浏览
		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (arg2 == Bimp.tempSelectBitmap.size()) {
					Log.i("ddddddd", "----------");
					ll_popup.startAnimation(
							AnimationUtils.loadAnimation(MainActivity.this, R.anim.activity_translate_in));
					pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
				} else {
					Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
					intent.putExtra("position", "1");
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});

	}

	// 适配器
	HashMap<Integer, String> map = new HashMap();

	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
		  //  loading();
			adapter.notifyDataSetChanged();
		}

		public int getCount() {
			if (Bimp.tempSelectBitmap.size() == 9) {
				return 9;
			}
			return (Bimp.tempSelectBitmap.size() + 1);
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_published_grida, parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (Bimp.tempSelectBitmap != null) {
				if (position == Bimp.tempSelectBitmap.size()) {
					holder.image.setImageBitmap(
							BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused));
					if (position == 9) {
						holder.image.setVisibility(View.GONE);
					}
				} else {
					holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
					String imagePath = Bimp.tempSelectBitmap.get(position).getImagePath();// 这是相册选中的图片路径
					map.put(position, imagePath);
				
				}
			}
			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.tempSelectBitmap.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							Bimp.max += 1;
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
						    break;// 在这添加
						}
					}
				}
			}).start();
		}
	}

	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

	protected void onRestart() {
		adapter.update();
		super.onRestart();
	}



	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (Bimp.tempSelectBitmap.size() < 9 && resultCode == RESULT_OK) {
				new Thread(new Runnable() {
					public void run() {
						fileName = String.valueOf(System.currentTimeMillis() + ".jpg");
						Bitmap bm = (Bitmap) data.getExtras().get("data");
						takepath = FileUtils.saveBitmap(bm, fileName);				          
						takePhoto = new ImageItem();
						takePhoto.setBitmap(bm);
						takePhoto.setImagePath(takepath);
						Bimp.tempSelectBitmap.add(takePhoto);
					}
				}).start();
			}
			break;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			for (int i = 0; i < PublicWay.activityList.size(); i++) {
				if (null != PublicWay.activityList.get(i)) {
					PublicWay.activityList.get(i).finish();
				}
			}
			System.exit(0);
		}
		return true;
	}

	// 页面监听事件
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.activity_selectimg_send:
			Thread thread = new Thread(new Runnable() {
				public void run() {
					SendMessage();
				}
			});
			if (resultCode!=200) {
				Toast.makeText(this, "发送成功", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "发送失败", Toast.LENGTH_SHORT).show();
			}
			thread.start();
			break;
		case R.id.repair_butback:
			finish();
		default:
			break;
		}
	}

	private void SendMessage() {
		String GetSelectMessage = selectSpinner.getSelectedItem().toString();
		String GetUserMessage = userMessage.getText().toString();
		SendPostData newpost = new SendPostData();
		newpost.setUrl(GlobalConnect.SEND_MESSAGE);
		newpost.addPostText("userid", String.valueOf(resultid.userid));
		newpost.addPostText("title", resultinfor.username + "的报修单");
		newpost.addPostText("text", GetUserMessage);
		newpost.addPostText("classify", GetSelectMessage);
		newpost.addPostText("position", resultinfor.address);    
//     	   for (int i = 0; i < map.size(); i++) {
//			path = (String) map.get(i);		
//			switch (map.size()) {
//			case 1:
//				newpost.addFile("nam1", path);
//				System.out.println(""+path);
//				break;
//			case 2:
//				newpost.addFile("nam1", path);
//				newpost.addFile("nam2", path);
//				break;
//			case 3:
//				newpost.addFile("nam1", path);
//				newpost.addFile("nam2", path);
//				newpost.addFile("nam3", path);
//				break;
//			case 4:
//				newpost.addFile("nam1", path);
//				newpost.addFile("nam2", path);
//				newpost.addFile("nam3", path);
//				newpost.addFile("nam4", path);
//				break;
//			case 5:
//				newpost.addFile("nam1", path);
//				newpost.addFile("nam2", path);
//				newpost.addFile("nam3", path);
//				newpost.addFile("nam4", path);
//				newpost.addFile("nam5", path);
//				break;
//			case 6:
//				newpost.addFile("nam1", path);
//				newpost.addFile("nam2", path);
//				newpost.addFile("nam3", path);
//				newpost.addFile("nam4", path);
//				newpost.addFile("nam5", path);
//				newpost.addFile("nam6", path);
//				break;
//			case 7:
//				newpost.addFile("nam1", path);
//				newpost.addFile("nam2", path);
//				newpost.addFile("nam3", path);
//				newpost.addFile("nam4", path);
//				newpost.addFile("nam5", path);
//				newpost.addFile("nam6", path);
//				newpost.addFile("nam7", path);
//				break;
//			case 8:
//				newpost.addFile("nam1", path);
//				newpost.addFile("nam2", path);
//				newpost.addFile("nam3", path);
//				newpost.addFile("nam4", path);
//				newpost.addFile("nam5", path);
//				newpost.addFile("nam6", path);
//				newpost.addFile("nam7", path);
//				newpost.addFile("nam8", path);
//				break;
//			case 9:
//				newpost.addFile("nam1", path);
//				newpost.addFile("nam2", path);
//				newpost.addFile("nam3", path);
//				newpost.addFile("nam4", path);
//				newpost.addFile("nam5", path);
//				newpost.addFile("nam6", path);
//				newpost.addFile("nam7", path);
//				newpost.addFile("nam8", path);
//				newpost.addFile("nam9", path);
//				break;
//			    default:	
//				break;
//			}	
//		}
		newpost.addFile("nam9", takepath);
		String result = newpost.send();
		resultCode = newpost.getResultCode();
		System.out.println(resultCode);// 返回状态代码
		System.out.println(result); // 返回的结果
	}
}
