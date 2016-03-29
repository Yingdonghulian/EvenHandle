package com.hulian.evenhandle.globalService;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.hulian.evenhandle.bean.UserData;
import com.hulian.evenhandle.global.GlobalConnect;
import com.hulian.evenhandle.pagerdetail.LoginActivity;
import com.hulian.evenhandle.util.CacheUtils;
import com.hulian.evenhandle.util.PrefUtils;
import com.hulian.evenhandle.util.SendPost;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class UserServiceImpl implements UserService {
	public static final String TAG = "UserServiceImpl";
	private UserData userinfo;
	private String result;

	@Override
	public void userLoing(String userName, String passWord, Context UserActivity) throws Exception {
		Log.d(TAG, userName);
		Log.d(TAG, passWord);
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(GlobalConnect.USER_URL);
		NameValuePair nameValuePair = new BasicNameValuePair("username", userName);
		NameValuePair passwordValuePair = new BasicNameValuePair("password", passWord);
		List<NameValuePair> valuePair = new ArrayList<NameValuePair>();
		valuePair.add(nameValuePair);
		valuePair.add(passwordValuePair);
		post.setEntity(new UrlEncodedFormEntity(valuePair, HTTP.UTF_8));
		// ��Ӧ
		HttpResponse respon = client.execute(post);
		int statuscode = respon.getStatusLine().getStatusCode();
		if (statuscode != HttpStatus.SC_OK) {
			throw new ServicerulesExcetion(LoginActivity.RQUEST_LOGIN_MESSAGE);
		}
		result = EntityUtils.toString(respon.getEntity(), HTTP.UTF_8);
	
		//���ػ����¼����Userid
		CacheUtils.setCache(GlobalConnect.USER_URL, result, UserActivity);
		String cache = CacheUtils.getCache(GlobalConnect.USER_URL, UserActivity);
		System.out.println("�洢����" + cache);
		if(!TextUtils.isEmpty(cache)){
			Gson gson = new Gson();
			userinfo = gson.fromJson(cache, UserData.class);
		}else{
			// ��ȡ���ݽ���
			Gson gson = new Gson();
			userinfo = gson.fromJson(result, UserData.class);
		}
		if (userinfo.flag.equals("success")) {
			SendPost newpost=new SendPost();    
			newpost.setUrl(GlobalConnect.INFO_URL);
			newpost.addPostText("userid", userinfo.userid);	//���Ҫ�͵�form����
			String result=newpost.send();				//�������ݲ���ȡ���صĽ��
			int resultCode=newpost.getResultCode();		//��ȡ��Ӧ״̬�룬�������ʱʹ��
			System.out.println(resultCode);				//�����Ӧ״̬��
			System.out.println(result);					//������ص�����
			CacheUtils.setCache(GlobalConnect.INFO_URL, result, UserActivity);	//���ػ���
		  //String Cache = CacheUtils.getCache(GlobalConnect.INFO_URL, UserActivity);
								
		} else {
			throw new ServicerulesExcetion(LoginActivity.FAILED_LOGIN_MESSAGE);
		}
	}

}
