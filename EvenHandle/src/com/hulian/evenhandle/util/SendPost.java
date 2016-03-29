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
			httpConn.setDoOutput(true); // ��Ҫ���
			httpConn.setDoInput(true); // ��Ҫ����
			httpConn.setUseCaches(false); // ��������
			httpConn.setRequestMethod("POST"); // ����POST��ʽ����
			// ������������
			httpConn.addRequestProperty("username", "admin");
			httpConn.addRequestProperty("password", "admin");
			httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			httpConn.setRequestProperty("Charset", "UTF-8");
			httpConn.connect();
			DataOutputStream dos = new DataOutputStream(httpConn.getOutputStream());
			dos.writeBytes(param);
			dos.flush();
			dos.close();
			int resultCode = httpConn.getResponseCode(); //��Ӧ״̬��
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
