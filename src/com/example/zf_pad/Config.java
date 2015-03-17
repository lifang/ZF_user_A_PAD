package com.example.zf_pad;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.example.zf_pad.entity.ApplyneedEntity;
import com.example.zf_pad.entity.ChanelEntitiy;
import com.example.zf_pad.entity.GoodinfoEntity;
import com.example.zf_pad.entity.tDates;
import com.example.zf_pad.entity.other_rate;


public class Config {

	public final static String PATHS = "http://114.215.149.242:18080/ZFMerchant/api/";
	public final static String IMAGE_PATH = "";
	public static String checkVersion = PATHS + "";
	public static int ROWS = 10;
	public static Boolean shopcar = false;
	public static String getmes = PATHS + "message/receiver/getAll";
	public static String MSGEDLONE = PATHS + "message/receiver/deleteById";
	public static String MSGEDLALL = PATHS + "message/receiver/batchDelete";
	public static String MSGREAD = PATHS + "message/receiver/batchRead";
	public final static String LOGIN = PATHS + "user/studentLogin";
	public static final int CITY_ID = 1;
	public static int UserID = 1;
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

	public static final String POSPORT = PATHS + "good/search";
	public static final String ORDERLIST = PATHS + "order/getMyOrderAll";
	public static final String ORDERDETAIL = PATHS + "order/getMyOrderById";
	public static String goodcomment = PATHS + "comment/list";
	public static final String goodadd = PATHS + "cart/add";
	public static final String SHOPCARLIST = PATHS + "cart/list";
	public static GoodinfoEntity gfe = null;
	public static ArrayList<ChanelEntitiy> celist = new ArrayList<ChanelEntitiy>();
	public static ArrayList<tDates> tDates = new ArrayList<tDates>();
	public static ArrayList<other_rate> other_rate = new ArrayList<other_rate>();
	public static List<ApplyneedEntity> pub = new LinkedList<ApplyneedEntity>();
	public static List<ApplyneedEntity> single = new LinkedList<ApplyneedEntity>();
	public static int goodId;
	public static String commentsCount;
}
