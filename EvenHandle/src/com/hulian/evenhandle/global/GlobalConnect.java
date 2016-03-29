package com.hulian.evenhandle.global;
/**
 * 定义全局参数
 * @author Liu-
 *
 */
public class GlobalConnect {
	public  static final String SERVER_URL = "http://192.168.1.106:8080/zhbj";
	public static final String CATEGORIES_URL = SERVER_URL + "/categories.json";//获取分类信
	public static final String USERIMAGE_URL="http://115.159.114.122/getuserpic?";
	public static final String SERVICE_URL="http://115.159.114.122";
	public static final String INFO_URL=SERVICE_URL+"/getuserinf";
	public static final String USER_URL=SERVICE_URL+"/userlogin";
	public static final String SEND_MESSAGE=SERVICE_URL+"/getnew";

}
