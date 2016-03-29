package com.hulian.evenhandle.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
public class SendPost {
	private String urlPath = null;
	private String param = null;
	private int resultCode;

	public void addPostText(String name, int userid) {
		if (param == null) {
			param = name + "=" + userid;
			
		} else {
			param = param+"&" + name + "=" + userid;
		}
	}
	public void setUrl(String urlPath) {
		this.urlPath = urlPath;
	}
	public int getResultCode(){
		return this.resultCode;
	}
	public String  send() {
		String result="";
		try {
			URL url = new URL(urlPath);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setDoOutput(true); // 需要输出
			httpConn.setDoInput(true); // 需要输入
			httpConn.setUseCaches(false); // 不允许缓存
			httpConn.setRequestMethod("POST"); // 设置POST方式连接
			// 设置请求属性
			httpConn.addRequestProperty("username", "admin");
			httpConn.addRequestProperty("password", "admin");
			httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			httpConn.setRequestProperty("Charset", "UTF-8");
			httpConn.connect();
			DataOutputStream dos = new DataOutputStream(httpConn.getOutputStream());
			dos.writeBytes(param);
			dos.flush();
			dos.close();
			int resultCode = httpConn.getResponseCode(); //响应状态码
			this.resultCode=resultCode;
			if (HttpURLConnection.HTTP_OK == resultCode) {
				StringBuffer sb = new StringBuffer();
				String readLine = new String();
				BufferedReader responseReader = new BufferedReader(
						new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
				while ((readLine = responseReader.readLine()) != null) {
					sb.append(readLine).append("\n");
				}
				responseReader.close();
				result=sb.toString();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;

	}
}
