package com.hulian.evenhandle.pagerdetail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.Gson;
import com.hulian.evenhandle.R;
import com.hulian.evenhandle.bean.ServieceData;
import com.hulian.evenhandle.bean.UserData;
import com.hulian.evenhandle.global.GlobalConnect;
import com.hulian.evenhandle.util.PrefUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.DialogInterface;
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
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.format.DateFormat;
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
	private static final int IMAGE_TWO = 2;
	private Button selectImageBtn;
	private Button button;
	private static final int IMAGE_SELECT = 1;
	private TextView username;
	private TextView useraddress;
	private ImageView img,imgv;
	private TextView time;
	private ImageView UserImage;
	public static final int SETMAIN = 1; // 设置一个what标记
	public static final int SETCHILD = 2; // 设置what的标记]
	private Handler mainHandler, childHandler;
	private static final int SET = 1; // 操作的what状态
	private ServieceData resultinfo;
	private UserData resultid;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.srervice_detail);
		initData();
			
		username = (TextView) findViewById(R.id.username);
		useraddress = (TextView) findViewById(R.id.useraddress);
		selectImageBtn = (Button) this.findViewById(R.id.select);
		imgv=(ImageView) findViewById(R.id.iv);
		time = (TextView) findViewById(R.id.data);
		
	}
	//加载用户信息
	public void initData() {
		String userid = PrefUtils.getString(getApplicationContext(), GlobalConnect.USER_URL, "");
		String userinfo = PrefUtils.getString(getApplicationContext(), GlobalConnect.INFO_URL, "");
		PareData(userinfo);
		PareDataUserId(userid);
		initView();
		getImageFromSDcard();
	}
	private Bitmap getImageFromSDcard()  
	{  
	  Bitmap  bitmap = null;  
		String Parth="/sdcard/DCIM/Camera/"+resultid.userid+".png";
	    try  
	    {  
	        File file = new File(Parth);  
	        if(file.exists())  
	        {  
	            bitmap = BitmapFactory.decodeFile(Parth);  
                 System.out.println("shuchu"+bitmap);             
	        }  
	    } catch (Exception e)  
	    {  
	        // TODO: handle exception  
	    }             
	    return bitmap;  
	}  
	
	// 数据解析
	private void PareData(String userinfo) {
		Gson gson = new Gson();
		resultinfo = gson.fromJson(userinfo, ServieceData.class);
		System.out.println("服务用户个人数据" + resultinfo);
	}

	 private void PareDataUserId(String userid) {
		Gson gson = new Gson();
		resultid = gson.fromJson(userid, UserData.class);
		System.out.println("服务用户数据" + resultid);
	}
	 //显示用户数据
		public void initView(){
	    	TextView username=(TextView) findViewById(R.id.username);
	    	username.setText(resultinfo.username);
	    	TextView useraddress=(TextView) findViewById(R.id.useraddress);
	    	useraddress.setText(resultinfo.address);
	    	ImageView	UserImage = (ImageView) findViewById(R.id.img);
	     	UserImage.setImageBitmap(getImageFromSDcard());
	    }
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
		}
}
