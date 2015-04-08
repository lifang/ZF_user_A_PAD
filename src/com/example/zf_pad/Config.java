package com.example.zf_pad;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.Intent;

import com.example.zf_pad.activity.LoginActivity;
import com.example.zf_pad.entity.ApplyneedEntity;
import com.example.zf_pad.entity.ChanelEntitiy;
import com.example.zf_pad.entity.GoodinfoEntity;
import com.example.zf_pad.entity.Goodlist;
import com.example.zf_pad.entity.OrderDetailEntity;
import com.example.zf_pad.entity.PosEntity;
import com.example.zf_pad.entity.tDates;
import com.example.zf_pad.entity.other_rate;
import com.example.zf_pad.trade.entity.City;

public class Config {

	public final static String PATHS = "http://114.215.149.242:18080/ZFMerchant/api/";
	public final static String IMAGE_PATH = "";
	public static String checkVersion = PATHS + "";
	public static int ROWS = 10;
	public static Boolean shopcar = false;
	public static String INDEXIMG = PATHS + "index/sysshufflingfigure/getList";
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
	public static GoodinfoEntity gfe = null;
	public static ArrayList<ChanelEntitiy> celist = new ArrayList<ChanelEntitiy>();
	public static ArrayList<tDates> tDates = new ArrayList<tDates>();
	public static ArrayList<other_rate> other_rate = new ArrayList<other_rate>();
	public static List<ApplyneedEntity> pub = new LinkedList<ApplyneedEntity>();
	public static List<ApplyneedEntity> single = new LinkedList<ApplyneedEntity>();
	public static List<PosEntity> myList = new ArrayList<PosEntity>();
	public static List<Goodlist> list = new ArrayList<Goodlist>();
	public static String suportare;
	public static String suportcl;
	public static String tv_sqkt;
	public static int goodId;
	public static String commentsCount;
	public static String CITY = "…œ∫£";
	public static boolean iszl = false;
	public static boolean isFRIST = false;
	public static int cityId = 0;
	public static List<City> mCities = new ArrayList<City>();

	public static boolean CheckIsLogin(Context c) {
		if (MyApplication.NewUser != null) {
			return true;
		} else {
			Intent i = new Intent(c, LoginActivity.class);
			c.startActivity(i);
			return false;
		}

	}

}
