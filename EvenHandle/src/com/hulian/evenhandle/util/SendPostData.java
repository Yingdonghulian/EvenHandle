package com.hulian.evenhandle.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SendPostData {
	private Map<String, String> params=null;
	private Map<String, File> files=null;
	private String actionUrl=null;
	private int resultcode=0;
	public SendPostData(){
		this.params = new HashMap<>();
		this.files= new HashMap<>();
	}
	public void addPostText(String name,String userid){
		params.put(name, userid);
	}
	public void addFile(String name,String path){
		File file1 = new File(path);
		files.put(name, file1);
	}
	public String send(){
		String re = null;
		try {
			re=post(this.actionUrl, this.params, this.files);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return re;
	}
	public int getResultCode(){
		return resultcode;
	}
	public void setUrl(String url){
		actionUrl=url;
	}

	public  String  post(String actionUrl, Map<String, String> params, Map<String, File> files) throws IOException {

		String BOUNDARY = java.util.UUID.randomUUID().toString();
		String PREFIX = "--", LINEND = "\r\n";
		String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "UTF-8";

		URL uri = new URL(actionUrl);
		HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
		conn.setReadTimeout(5 * 1000); 
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false); 
		conn.setRequestMethod("POST");
		conn.setRequestProperty("connection", "keep-alive");
		conn.setRequestProperty("Charsert", "UTF-8");
		conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append(PREFIX);
			sb.append(BOUNDARY);
			sb.append(LINEND);
			sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
			sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
			sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
			sb.append(LINEND);
			sb.append(entry.getValue());
			sb.append(LINEND);
		}

		DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
		outStream.write(sb.toString().getBytes());
		InputStream in = null;
		StringBuffer sb3 = null;
		if (files != null) {
			for (Map.Entry<String, File> file : files.entrySet()) {
				StringBuilder sb1 = new StringBuilder();
				sb1.append(PREFIX);
				sb1.append(BOUNDARY);
				sb1.append(LINEND);
				sb1.append(
						"Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getKey() + "\"" + LINEND);
				sb1.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINEND);
				sb1.append(LINEND);
				outStream.write(sb1.toString().getBytes());

				InputStream is = new FileInputStream(file.getValue());
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					outStream.write(buffer, 0, len);
				}

				is.close();
				outStream.write(LINEND.getBytes());
			}
			byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
			outStream.write(end_data);
			outStream.flush();
			int res = conn.getResponseCode();
			resultcode=res;
			if (res == 200) {
				sb3= new StringBuffer();
				String readLine = new String();
				BufferedReader responseReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
				while ((readLine = responseReader.readLine()) != null) {
					sb3.append(readLine).append("\n");
				}
				responseReader.close();
			}
			else{
				return "{\"flag\",\"∑¢ÀÕ ß∞‹\"}";
			}
			outStream.close();
			conn.disconnect();
		}
		return sb3.toString();
	}

}
