package com.example.zf_pad;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.zf_pad.activity.LoginActivity;
import com.example.zf_pad.entity.ApplyneedEntity;
import com.example.zf_pad.entity.ChanelEntitiy;
import com.example.zf_pad.entity.GoodinfoEntity;
import com.example.zf_pad.entity.Goodlist;
import com.example.zf_pad.entity.PosEntity;
import com.example.zf_pad.entity.other_rate;
import com.example.zf_pad.entity.tDates;
import com.example.zf_pad.trade.entity.City;

public class Config {


	// public final static String PATHS =
	// "http://114.215.149.242:18080/ZFMerchant/api/";
	// http://121.40.84.2:28080/ZFAgent/api
	//public final static String PATHS = "http://121.40.84.2:8180/ZFMerchant/api/";
	public final static String PATHS = "http://121.40.64.167:8080/api/";

	//视频通话
	//public static final String VIDEO_SERVER_IP = "121.40.84.2";
	public static final String VIDEO_SERVER_IP = "121.40.64.120";
	public static final int VIDEO_SERVER_PORT = 8906;
	
	public static final String URL_NOTICE_VIDEO = "http://admin.ebank007.com/notice/video";
	//public static final String URL_NOTICE_VIDEO = "http://121.40.84.2:8180/zfmanager/notice/video";
	/*
	 * alipay
	*/
	//商户PID
	public static final String PARTNER = "2088811347108355";
	//商户收款账号
	public static final String SELLER = "ebank007@epalmpay.cn";
	//商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALjI06X8hEw9LiLqTsqmjZAqwSq/VIGJKNQgIeCCr/oReR4OePe5i2u+89PpcFe6kF2v6gWulb4WNdHYw3Iiux56sm7jUQPC1hVYXG8tiaVEb3YhX2y0YGQUS18drBBGzHnlOQlrrmlBh9ugQFzLio2NwUWo0yfcXlLoKYyteDBVAgMBAAECgYBpjW441rHLyvbbwvQXFmSvAX0uKfTfubW01lYDpSNYuTpyTNoUx8w4U+98EVC3DD8DBUWs0TmAR7eeky+xtt0jZ1O8bpHUzRi02NOw2p1ZyAHN28rvUpultfInBpbqgJDvMoWIX4AeqWQcs4gbAbPyEaWvgYM53uW7eo9CtcFMgQJBAOHGVL8Xe9agkiGwYT8e9068+xjXiloAKgQjps8fxLfMCd34sI1tEjyz0jIZ+AK4pGvU1JJdtx7s70INnubqoY0CQQDRhbFcxqaz2c+S2WUQNduFah5EZt/vdWxo4+6EHrXNdAjT7nVyA8CzreRXcPEKQZ+RhuXyXGqSLDJGKYPGQIPpAkBSmqfjCoqKqlEM9mV+HKxLKKWOHz5FU44L2adsXKkyvfpWNmkSNXfYscoT/qBZDolJ0qK7soIPVIztU+JxhiL5AkAC037U9YkCHAoEvRHz6gYQAqJt4cVbgYX41Do/Zfqlzs7frPPAmfRbeBkAZPGbZc81M1CeuEhnuFjlQWIZpn0hAkEAu1Q+fNm01qqVJ0YCMeyUoLqin/rmRAsY93cDNk82ZxY+gc3YDlcvF5qqQqcqiSSHBZkAtQqFTzx3taybP2MKjw==";
	//支付宝公钥
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
	//异步通知接口  
//	public static final String NOTIFT_URL = "http://121.40.84.2:8080/ZFMerchant/app_notify_url.jsp"; 
	public static final String NOTIFT_URL = "http://121.40.64.167:8080/ZFMerchant/app_notify_url.jsp"; 
	//维修异步通知接口  
	//public static final String REPAIR_NOTIFT_URL = "http://121.40.84.2:8080/ZFMerchant/repair_app_notify_url.jsp"; 
	public static final String REPAIR_NOTIFT_URL = "http://121.40.64.167:8080/ZFMerchant/repair_app_notify_url.jsp"; 
	//支付成功跳转页面
	//public static final String RETURN_URL = "http://121.40.84.2:8080/ZFMerchant/return_url.jsp"; 
	public static final String RETURN_URL = "http://121.40.64.167:8080/ZFMerchant/return_url.jsp"; 
	

	public final static String IMAGE_PATH = "";
	public static String checkVersion = PATHS + "";
	public static int ROWS = 10;
	public static String token = "123";
	public static Boolean shopcar = false;
	public static Boolean AderssManger = false;
	public static Boolean AderssMangerBACK = false;
	public static String INDEXIMG = PATHS + "index/sysshufflingfigure/getList";
	public static String getmes = PATHS + "message/receiver/getAll";
	public static String MSGEDLONE = PATHS + "message/receiver/deleteById";
	public static String MSGEDLALL = PATHS + "message/receiver/batchDelete";
	public static String MSGREAD = PATHS + "message/receiver/batchRead";
	public final static String LOGIN = PATHS + "user/studentLogin";
	public static int GOODID = -1;
	public static int UserID = 1;
	public static boolean iszd = false;
	// user/userRegistration
	public final static String UserRegistration = PATHS
			+ "user/userRegistration";
	public static final String SHARED = "zfandroid";
	public static final String FINDPASS = null;
	public final static String RegistgetCode = PATHS
			+ "user/sendPhoneVerificationCode/";
	public static final String GRTONE = PATHS + "customers/getOne/";
	// http://114.215.149.242:18080/ZFMerchant/api/message/receiver/getById
	public static final String getMSGById = PATHS + "message/receiver/getById";
	public static final String Car_edit = PATHS + "cart/update";
	public static final int CODE = 1;
	public static final String getMyOrderAll = PATHS + "order/getMyOrderAll";
	public static final String batchRead = PATHS + "message/receiver/batchRead";
	public static final String GOODDETAIL = PATHS + "good/goodinfo";
	public static final String SYSMSGLIST = PATHS + "web/message/getAll";
	public static final String SYSMSGDT = PATHS + "web/message/getById";
	public static final String POSLIST = PATHS + "good/list";

	public static final String Comment = PATHS + "order/batchSaveComment";
	public static final String POSPORT = PATHS + "good/search";
	public static final String ORDERLIST = PATHS + "order/getMyOrderAll";
	public static final String ORDERDETAIL = PATHS + "order/getMyOrderById";
	public static String goodcomment = PATHS + "comment/list";
	public static final String goodadd = PATHS + "cart/add";
	public static final String SHOPCARLIST = PATHS + "cart/list";
	public static final String paychannel_info = PATHS + "paychannel/info";
	public static final String ChooseAdress = PATHS
			+ "customers/getAddressList/";
	public static final String SHOPORDER = PATHS + "order/shop";
	public static final String SHOPCART = PATHS + "order/cart";
	public static final String SHOPDELETE = PATHS + "cart/delete";
	public final static String updatePassword = PATHS + "user/updatePassword";
	public static final String FINDPASSGETCODE = PATHS
			+ "user/sendPhoneVerificationCodeFind";
	public static final String ORDERCANL = PATHS + "order/cancelMyOrder";
	public static final String ZDORDER = PATHS + "order/lease";

	public static final String POSTHOT = PATHS + "index/pos_list";

	public static final String MERCHANTINFO = PATHS + "merchant/getOne/";

	public static GoodinfoEntity gfe = null;
	public static ArrayList<ChanelEntitiy> celist = new ArrayList<ChanelEntitiy>();
	public static ArrayList<ChanelEntitiy> celist2 = new ArrayList<ChanelEntitiy>();
	public static ArrayList<tDates> tDates = new ArrayList<tDates>();
	public static ArrayList<other_rate> other_rate = new ArrayList<other_rate>();
	public static List<ApplyneedEntity> pub = new LinkedList<ApplyneedEntity>();
	public static List<ApplyneedEntity> single = new LinkedList<ApplyneedEntity>();
	public static List<PosEntity> myList = new ArrayList<PosEntity>();
	public static List<Goodlist> list = new ArrayList<Goodlist>();
	public static int ScreenWidth = 0;
	public static int ScreenHeight = 0;
	public static String suportare;
	public static String suportcl;
	public static String tv_sqkt;
	public static int goodId;
	public static String commentsCount;
	public static String CITY = "上海";
	public static boolean iszl = false;
	public static boolean isFRIST = false;
	public static boolean isExit = false;
	public static int cityId = 0;
	public static int MyTab = 0;
	public static List<City> mCities = new ArrayList<City>();
	private static SharedPreferences mySharedPreferences;

	public static boolean CheckIsLogin(Context c) {
		mySharedPreferences = c.getSharedPreferences(Config.SHARED,
				c.MODE_PRIVATE);

		/*
		 * if (mySharedPreferences.getBoolean("islogin", false)) { return true;
		 * } else { Intent i = new Intent(c, LoginActivity.class);
		 * c.startActivity(i); return false; }
		 */
		if (MyApplication.NewUser!=null) {
			return true;
		} else {
			Intent i = new Intent(c, LoginActivity.class);
			c.startActivity(i);
			return false;
		}

	}

}
