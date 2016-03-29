package com.hulian.evenhandle.pagerdetail;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.google.gson.Gson;
import com.hulian.evenhandle.PagerActicity;
import com.hulian.evenhandle.R;
import com.hulian.evenhandle.base.impl.Homepager;
import com.hulian.evenhandle.base.impl.Minepager;
import com.hulian.evenhandle.bean.UserData;
import com.hulian.evenhandle.detailcontent.HomeContentDetail;
import com.hulian.evenhandle.global.GlobalConnect;
import com.hulian.evenhandle.globalService.ServicerulesExcetion;
import com.hulian.evenhandle.globalService.UserService;
import com.hulian.evenhandle.globalService.UserServiceImpl;
import com.hulian.evenhandle.menudetail.HomeMenuDetail;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class LoginActivity extends Activity implements OnClickListener, OnCheckedChangeListener {

	private ToggleButton mTgBtnShowPsw;
	private EditText mEditPsw;
	private EditText mEditUid;
	private ImageView mBtnClearUid;
	private ImageView mBtnClearPsw;
	private ImageButton Back;
	private UserService userservice = new UserServiceImpl();
	public static final int FLAG_LPGIN_SUCCESS = 1;
	public static final String ERROR_LOGIN_MESSAGE = "登录出错";
	public static final String SUCCESS_LOGIN_MESSAGE = "登录成功";
	public static final String FAILED_LOGIN_MESSAGE = "登录密码或帐号错误";
	public static final String RQUEST_LOGIN_MESSAGE = "登录密码或帐号错误";
	private static ProgressDialog Dialog;
	private Button mBtnLogin;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.lay2);
		initUI();
		setOnListener();
		initUid();
	}

	/**
	 * 初始化记住的用户名
	 */
	private void initUid() {
		SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
		String uid = sp.getString("uid", "");
		mEditUid.setText(uid);
	}

	private void setOnListener() {
		mEditUid.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (mEditUid.getText().toString().length() > 0) {
					mBtnClearUid.setVisibility(View.VISIBLE);
					if (mEditPsw.getText().toString().length() > 0) {
						mBtnLogin.setEnabled(true);
					} else {
						mBtnLogin.setEnabled(false);
					}
				} else {
					mBtnLogin.setEnabled(false);
					mBtnClearUid.setVisibility(View.INVISIBLE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		mEditPsw.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (mEditPsw.getText().toString().length() > 0) {
					mBtnClearPsw.setVisibility(View.VISIBLE);
					if (mEditUid.getText().toString().length() > 0) {
						mBtnLogin.setEnabled(true);
					} else {
						mBtnLogin.setEnabled(false);
					}
				} else {
					mBtnLogin.setEnabled(false);
					mBtnClearPsw.setVisibility(View.INVISIBLE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		mBtnLogin.setOnClickListener(this);
		mBtnClearUid.setOnClickListener(this);
		mBtnClearPsw.setOnClickListener(this);
		mTgBtnShowPsw.setOnCheckedChangeListener(this);
		findViewById(R.id.loginbtn_back).setOnClickListener(this);
		findViewById(R.id.btn_login_wb).setOnClickListener(this);
		findViewById(R.id.tv_quick_sign_up).setOnClickListener(this);
	}

	private void initUI() {
		mBtnLogin = (Button) findViewById(R.id.btn_login);
		mEditUid = (EditText) findViewById(R.id.edit_uid);
		mEditPsw = (EditText) findViewById(R.id.edit_psw);
		mBtnClearUid = (ImageView) findViewById(R.id.img_login_clear_uid);
		mBtnClearPsw = (ImageView) findViewById(R.id.img_login_clear_psw);
		mTgBtnShowPsw = (ToggleButton) findViewById(R.id.tgbtn_show_psw);
		Back = (ImageButton) findViewById(R.id.loginbtn_back);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login: // 登录
			login();
			break;
		case R.id.loginbtn_back: // 返回
			finish();
			break;
		case R.id.btn_login_wb: // 微博登录
			// loginWB();
			break;
		case R.id.img_login_clear_uid: // 清除用户名
			clearText(mEditUid);
			break;
		case R.id.img_login_clear_psw: // 清除密码
			clearText(mEditPsw);
			break;
		case R.id.tv_quick_sign_up: // 快速注册
			startActivity(new Intent(this, SignUpActivity.class));
			break;

		default:
			break;
		}
	}


	/**
	 * 清空控件文本
	 * 
	 * @param mEditUid2
	 */
	private void clearText(EditText edit) {
		edit.setText("");
	}

	/**
	 * 登录按钮
	 */
	private void login() {
		final String userName = mEditUid.getText().toString();
		final String passWord = mEditPsw.getText().toString();
		if (Dialog == null) {
			Dialog = new ProgressDialog(LoginActivity.this);
		}
		Dialog.setCancelable(false);
		
		Dialog.show();
		// 子线程
		Thread thred = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					userservice.userLoing(userName, passWord, LoginActivity.this);
					handler.sendEmptyMessage(FLAG_LPGIN_SUCCESS);
				} catch (ServicerulesExcetion e) {
					e.printStackTrace();
					Message msg = new Message();
					Bundle data = new Bundle();
					data.putSerializable("ErrorMsg", e.getMessage());
					msg.setData(data);
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = new Message();
					Bundle data = new Bundle();
					data.putSerializable("ErrorMsg", ERROR_LOGIN_MESSAGE);
					msg.setData(data);
					handler.sendMessage(msg);
				}
			}
		});
		thred.start();
	}

	private static class IHandler extends Handler {
		private final WeakReference<Activity> mActivity;

		public IHandler(LoginActivity activity) {
			mActivity = new WeakReference<Activity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			if (Dialog != null) {
				Dialog.dismiss();
			}
			int flag = msg.what;
			switch (flag) {
			case 0:
				String errorMsg = (String) msg.getData().getSerializable("ErrorMsg");
				((LoginActivity) mActivity.get()).showTip(errorMsg);
				break;
			case FLAG_LPGIN_SUCCESS:
				((LoginActivity) mActivity.get()).showTip(SUCCESS_LOGIN_MESSAGE);
				break;
			default:
				break;
			}
		}
	}

	private IHandler handler = new IHandler(this);

	/**
	 * 是否显示密码
	 */
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			// 显示密码
			mEditPsw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
		} else {
			// 隐藏密码
			mEditPsw.setTransformationMethod(PasswordTransformationMethod.getInstance());
		}
	}
	public void showTip(String str) {
		if (str.equals(SUCCESS_LOGIN_MESSAGE)) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), PagerActicity.class);
			this.startActivity(intent);			
			LoginActivity.this.finish();
		} else {
			Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
		}
	}

}
