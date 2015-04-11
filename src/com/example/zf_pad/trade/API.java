package com.example.zf_pad.trade;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import com.example.zf_pad.fragment.Mine_Address;
import com.example.zf_pad.Config;
import com.example.zf_pad.MyApplication;

import com.example.zf_pad.Posport;

import com.example.zf_pad.trade.common.HttpCallback;
import com.example.zf_pad.trade.common.HttpRequest;
import com.google.gson.Gson;

import com.example.zf_pad.trade.common.HttpCallback;
import com.example.zf_pad.trade.common.HttpRequest;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

import static com.example.zf_pad.fragment.Constants.AfterSaleType.CANCEL;
import static com.example.zf_pad.fragment.Constants.AfterSaleType.CHANGE;
import static com.example.zf_pad.fragment.Constants.AfterSaleType.LEASE;
import static com.example.zf_pad.fragment.Constants.AfterSaleType.MAINTAIN;
import static com.example.zf_pad.fragment.Constants.AfterSaleType.RETURN;
import static com.example.zf_pad.fragment.Constants.AfterSaleType.UPDATE;

public class API {

	static Gson gson = new Gson();

	public static final String SCHEMA = "http://";
	public static final String HOST = "114.215.149.242:18080";
	// public static final String HOST = "192.168.1.101:8080";
	public static String GET_USERINFO = SCHEMA + HOST
			+ "/ZFMerchant/api/customers/getOne/";
	// change userinfo
	public static String CHANGE_USERINFO = SCHEMA + HOST
			+ "/ZFMerchant/api/customers/update/";
	public static String CHANGE_PAW = SCHEMA + HOST
			+ "/ZFMerchant/api/customers/updatePassword";
	// get addresslist
	public static String GET_ADRESS = SCHEMA + HOST
			+ "/ZFMerchant/api/customers/getAddressList/";

	// creat merchant
	public static String CREAT_MERCHANT = SCHEMA + HOST
			+ "/ZFMerchant/api/merchant/insert/";
	// selection terminal list
	public static final String TERMINAL_LIST = SCHEMA + HOST
			+ "/ZFMerchant/api/trade/record/getTerminals/%d";
	// trade record list
	public static final String TRADE_RECORD_LIST = SCHEMA + HOST
			+ "/ZFMerchant/api/trade/record/getTradeRecords/%d/%s/%s/%s/%d/%d";
	// trade record statistic
	public static final String TRADE_RECORD_STATISTIC = SCHEMA + HOST
			+ "/ZFMerchant/api/trade/record/getTradeRecordTotal/%d/%s/%s/%s";
	// trade record detail
	public static final String TRADE_RECORD_DETAIL = SCHEMA + HOST
			+ "/ZFMerchant/api/trade/record/getTradeRecord/%d/%d";

	// After sale record list
	public static final String AFTER_SALE_MAINTAIN_LIST = SCHEMA + HOST
			+ "/ZFMerchant/api/cs/repair/getAll";
	public static final String AFTER_SALE_RETURN_LIST = SCHEMA + HOST
			+ "/ZFMerchant/api/return/getAll";
	public static final String AFTER_SALE_CANCEL_LIST = SCHEMA + HOST
			+ "/ZFMerchant/api/cs/cancels/getAll";
	public static final String AFTER_SALE_CHANGE_LIST = SCHEMA + HOST
			+ "/ZFMerchant/api/cs/change/getAll";
	public static final String AFTER_SALE_UPDATE_LIST = SCHEMA + HOST
			+ "/ZFMerchant/api/update/info/getAll";
	public static final String AFTER_SALE_LEASE_LIST = SCHEMA + HOST
			+ "/ZFMerchant/api/cs/lease/returns/getAll";

	// After sale record detail
	public static final String AFTER_SALE_MAINTAIN_DETAIL = SCHEMA + HOST
			+ "/ZFMerchant/api/cs/repair/getRepairById";
	public static final String AFTER_SALE_RETURN_DETAIL = SCHEMA + HOST
			+ "/ZFMerchant/api/return/getReturnById";
	public static final String AFTER_SALE_CANCEL_DETAIL = SCHEMA + HOST
			+ "/ZFMerchant/api/cs/cancels/getCanCelById";
	public static final String AFTER_SALE_CHANGE_DETAIL = SCHEMA + HOST
			+ "/ZFMerchant/api/cs/change/getChangeById";
	public static final String AFTER_SALE_UPDATE_DETAIL = SCHEMA + HOST
			+ "/ZFMerchant/api/update/info/getInfoById";
	public static final String AFTER_SALE_LEASE_DETAIL = SCHEMA + HOST
			+ "/ZFMerchant/api/cs/lease/returns/getById";

	// After sale record cancel apply
	public static final String AFTER_SALE_MAINTAIN_CANCEL = SCHEMA + HOST
			+ "/ZFMerchant/api/cs/repair/cancelApply";
	public static final String AFTER_SALE_RETURN_CANCEL = SCHEMA + HOST
			+ "/ZFMerchant/api/return/cancelApply";
	public static final String AFTER_SALE_CANCEL_CANCEL = SCHEMA + HOST
			+ "/ZFMerchant/api/cs/cancels/cancelApply";
	public static final String AFTER_SALE_CHANGE_CANCEL = SCHEMA + HOST
			+ "/ZFMerchant/api/cs/change/cancelApply";
	public static final String AFTER_SALE_UPDATE_CANCEL = SCHEMA + HOST
			+ "/ZFMerchant/api/update/info/cancelApply";
	public static final String AFTER_SALE_LEASE_CANCEL = SCHEMA + HOST
			+ "/ZFMerchant/api/cs/lease/returns/cancelApply";

	// After sale resubmit cancel
	public static final String AFTER_SALE_RESUBMIT_CANCEL = SCHEMA + HOST
			+ "/ZFMerchant/api/cs/cancels/resubmitCancel";

	// After sale add mark
	public static final String AFTER_SALE_MAINTAIN_ADD_MARK = SCHEMA + HOST
			+ "/ZFMerchant/api/cs/repair/addMark";
	public static final String AFTER_SALE_RETURN_ADD_MARK = SCHEMA + HOST
			+ "/ZFMerchant/api/return/addMark";
	public static final String AFTER_SALE_CHANGE_ADD_MARK = SCHEMA + HOST
			+ "/ZFMerchant/api/cs/change/addMark";
	public static final String AFTER_SALE_LEASE_ADD_MARK = SCHEMA + HOST
			+ "/ZFMerchant/api/cs/lease/returns/addMark";

	// Terminal list
	public static final String TERMINAL_APPLY_LIST = SCHEMA + HOST
			+ "/ZFMerchant/api/terminal/getApplyList";
	// Channel list
	public static final String CHANNEL_LIST = SCHEMA + HOST
			+ "/ZFMerchant/api/terminal/getFactories";
	// Terminal Add
	public static final String TERMINAL_ADD = SCHEMA + HOST
			+ "/ZFMerchant/api/terminal/addTerminal";
	// Terminal detail
	public static final String TERMINAL_DETAIL = SCHEMA + HOST
			+ "/ZFMerchant/api/terminal/getApplyDetails";

	// synchronise terminal
	public static final String TERMINAL_SYNC = SCHEMA + HOST
			+ "/ZFMerchant/api/terminal/synchronous";
	// find pos password
	public static final String TERMINAL_FIND_POS = SCHEMA + HOST
			+ "/ZFMerchant/api/terminal/Encryption";

	// Apply List
	public static final String APPLY_LIST = SCHEMA + HOST
			+ "/ZFMerchant/api/apply/getApplyList";
	// Apply Detail
	public static final String APPLY_DETAIL = SCHEMA + HOST
			+ "/ZFMerchant/api/apply/getApplyDetails";
	// Get the Merchant Detail
	public static final String APPLY_MERCHANT_DETAIL = SCHEMA + HOST
			+ "/ZFMerchant/api/apply/getMerchant";
	// Get the Channel List
	public static final String APPLY_CHANNEL_LIST = SCHEMA + HOST
			+ "/ZFMerchant/api/apply/getChannels";
	// Get the Bank List
	public static final String APPLY_BANK_LIST = SCHEMA + HOST
			+ "/ZFMerchant/api/apply/ChooseBank";

	// upload image url
	public static final String UPLOAD_IMAGE = SCHEMA + HOST
			+ "/ZFMerchant/api/comment/upload/tempImage";

	public static final String WNATBUY = SCHEMA + HOST
			+ "/ZFMerchant/api/paychannel/intention/add";

	// Apply Opening Progress Query
	public static final String APPLY_PROGRESS = SCHEMA + HOST
			+ "/ZFMerchant/api/terminal/openStatus";
	// Get merchant list
	public static String GET_MERCHANTLIST = SCHEMA + HOST
			+ "/ZFMerchant/api/merchant/getList/";
	// Add address
	public static final String Add_ress = SCHEMA + HOST
			+ "/ZFMerchant/api/customers/insertAddress/";
	// update address
	public static final String update_ress = SCHEMA + HOST
			+ "/ZFMerchant/api/customers/updateAddress/";
	// get totalscore
	public static String total_score = SCHEMA + HOST
			+ "/ZFMerchant/api/customers/getIntegralTotal/";
	// exchange score
	public static String exchange_score = SCHEMA + HOST
			+ "/ZFMerchant/api/customers/insertIntegralConvert";

	public static final String GETCODE4PHONE = SCHEMA + HOST
			+ "/ZFMerchant/api/user/sendPhoneVerificationCodeReg";
	public static final String ZHUCHE = SCHEMA + HOST
			+ "/ZFMerchant/api/user/userRegistration";
	public static final String GETEMAILPASS = SCHEMA + HOST
			+ "/ZFMerchant/api/user/sendEmailVerificationCode";
	// Apply Submit
	public static final String APPLY_SUBMIT = SCHEMA + HOST
			+ "/ZFMerchant/api/apply/addOpeningApply";
	// delect merchant
	public static String DELECT_MERCHANTLIST = SCHEMA + HOST
			+ "/ZFMerchant/api/merchant/delete/";
	// update merchant
	 	public static String UPDATE_MERCHANT=SCHEMA + HOST
	 			+"/ZFMerchant/api/merchant/update/";
	// update file
	 	public static String UPDATE_FILE=SCHEMA + HOST
	 			+"/ZFMerchant/api/merchant/upload/file";
	public static void getTerminalList(Context context, int customerId,
			HttpCallback callback) {
		new HttpRequest(context, callback).post(String.format(TERMINAL_LIST,
				customerId));
	}

	public static void getTradeRecordList(Context context, int tradeTypeId,
			String terminalNumber, String startTime, String endTime, int page,
			int rows, HttpCallback callback) {
		new HttpRequest(context, callback).post(String.format(
				TRADE_RECORD_LIST, tradeTypeId, terminalNumber, startTime,
				endTime, page, rows));
	}

	public static void getTradeRecordStatistic(Context context,
			int tradeTypeId, String terminalNumber, String startTime,
			String endTime, HttpCallback callback) {
		new HttpRequest(context, callback).post(String.format(
				TRADE_RECORD_STATISTIC, tradeTypeId, terminalNumber, startTime,
				endTime));
	}

	public static void getTradeRecordDetail(Context context, int typeId,
			int recordId, HttpCallback callback) {
		new HttpRequest(context, callback).post(String.format(
				TRADE_RECORD_DETAIL, typeId, recordId));
	}

	public static void getAfterSaleRecordList(Context context, int recordType,
			int customId, int page, int pageSize, HttpCallback callback) {
		String url;
		switch (recordType) {
		case MAINTAIN:
			url = AFTER_SALE_MAINTAIN_LIST;
			break;
		case RETURN:
			url = AFTER_SALE_RETURN_LIST;
			break;
		case CANCEL:
			url = AFTER_SALE_CANCEL_LIST;
			break;
		case CHANGE:
			url = AFTER_SALE_CHANGE_LIST;
			break;
		case UPDATE:
			url = AFTER_SALE_UPDATE_LIST;
			break;
		case LEASE:
			url = AFTER_SALE_LEASE_LIST;
			break;
		default:
			throw new IllegalArgumentException(
					"illegal argument [recordType], within [0-5]");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customer_id", customId);
		params.put("page", page);
		params.put("pageSize", pageSize);
		new HttpRequest(context, callback).post(url, params);
	}

	public static void getAfterSaleRecordDetail(Context context,
			int recordType, int recordId, HttpCallback callback) {
		String url;
		switch (recordType) {
		case MAINTAIN:
			url = AFTER_SALE_MAINTAIN_DETAIL;
			break;
		case RETURN:
			url = AFTER_SALE_RETURN_DETAIL;
			break;
		case CANCEL:
			url = AFTER_SALE_CANCEL_DETAIL;
			break;
		case CHANGE:
			url = AFTER_SALE_CHANGE_DETAIL;
			break;
		case UPDATE:
			url = AFTER_SALE_UPDATE_DETAIL;
			break;
		case LEASE:
			url = AFTER_SALE_LEASE_DETAIL;
			break;
		default:
			throw new IllegalArgumentException(
					"illegal argument [recordType], within [0-5]");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", recordId);
		new HttpRequest(context, callback).post(url, params);
	}

	public static void cancelAfterSaleApply(Context context, int recordType,
			int recordId, HttpCallback callback) {
		String url;
		switch (recordType) {
		case MAINTAIN:
			url = AFTER_SALE_MAINTAIN_CANCEL;
			break;
		case RETURN:
			url = AFTER_SALE_RETURN_CANCEL;
			break;
		case CANCEL:
			url = AFTER_SALE_CANCEL_CANCEL;
			break;
		case CHANGE:
			url = AFTER_SALE_CHANGE_CANCEL;
			break;
		case UPDATE:
			url = AFTER_SALE_UPDATE_CANCEL;
			break;
		case LEASE:
			url = AFTER_SALE_LEASE_CANCEL;
			break;
		default:
			throw new IllegalArgumentException(
					"illegal argument [recordType], within [0-5]");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", recordId);
		new HttpRequest(context, callback).post(url, params);
	}

	public static void resubmitCancel(Context context, int recordId,

	HttpCallback callback) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", recordId);
		new HttpRequest(context, callback).post(AFTER_SALE_RESUBMIT_CANCEL,
				params);
	}

	public static void addMark(Context context, int recordType, int recordId,
			int customerId, String company, String number, HttpCallback callback) {
		String url;
		switch (recordType) {
		case MAINTAIN:
			url = AFTER_SALE_MAINTAIN_ADD_MARK;
			break;
		case RETURN:
			url = AFTER_SALE_RETURN_ADD_MARK;
			break;
		case CHANGE:
			url = AFTER_SALE_CHANGE_ADD_MARK;
			break;
		case LEASE:
			url = AFTER_SALE_LEASE_ADD_MARK;
			break;
		default:
			throw new IllegalArgumentException(
					"illegal argument [recordType], within [0,1,3,5]");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", recordId);
		params.put("customer_id", customerId);
		params.put("computer_name", company);
		params.put("track_number", number);
		new HttpRequest(context, callback).post(url, params);
	}

	public static void getTerminalApplyList(Context context, int customerId,
			int page, int rows, HttpCallback callback) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customersId", customerId);
		params.put("page", page);
		params.put("rows", rows);
		new HttpRequest(context, callback).post(TERMINAL_APPLY_LIST, params);
	}

	public static void test(Context context, String customerId,

	HttpCallback callback) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codeNumber", customerId);
		new HttpRequest(context, callback)
				.post("http://114.215.149.242:18080/ZFMerchant/api/user/sendPhoneVerificationCodeFind",
						params);
	}

	public static void getChannelList(Context context, HttpCallback callback) {
		new HttpRequest(context, callback).post(CHANNEL_LIST);
	}

	public static void addTerminal(Context context, int customerId,
			int payChannelId, String terminalNumber, String merchantName,
			HttpCallback callback) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customerId", customerId);
		params.put("payChannelId", payChannelId);
		params.put("serialNum", terminalNumber);
		params.put("title", merchantName);
		new HttpRequest(context, callback).post(TERMINAL_ADD, params);
	}

	public static void getTerminalDetail(Context context, int terminalId,
			int customerId, HttpCallback callback) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("terminalsId", terminalId);
		params.put("customerId", customerId);
		new HttpRequest(context, callback).post(TERMINAL_DETAIL, params);
	}

	public static void findPosPassword(Context context, int terminalId,
			HttpCallback callback) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("terminalid", terminalId);
		new HttpRequest(context, callback).post(TERMINAL_DETAIL, params);
	}

	public static void getApplyList(Context context, int customerId, int page,
			int rows, HttpCallback callback) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customersId", customerId);
		params.put("page", page);
		params.put("rows", rows);
		new HttpRequest(context, callback).post(APPLY_LIST, params);
	}

	public static void getApplyDetail(Context context, int customerId,
			int terminalId, int status, HttpCallback callback) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customerId", customerId);
		params.put("terminalsId", terminalId);
		params.put("status", status);
		new HttpRequest(context, callback).post(APPLY_DETAIL, params);
	}

	public static void getApplyMerchantDetail(Context context, int merchantId,
			HttpCallback callback) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("merchantId", merchantId);
		new HttpRequest(context, callback).post(APPLY_MERCHANT_DETAIL, params);
	}

	public static void getApplyChannelList(Context context,
			HttpCallback callback) {
		new HttpRequest(context, callback).post(APPLY_CHANNEL_LIST);
	}

	public static void ApiWantBug(Context context, String name, String phone,
			String content, HttpCallback callback) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		params.put("phone", phone);
		params.put("content", content);
		new HttpRequest(context, callback).post(WNATBUY, params);
	}

	public static void Login1(Context context, String username,
			String passsword, HttpCallback callback) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", username);
		params.put("password", passsword);
		new HttpRequest(context, callback).post(Config.LOGIN, params);
	}

	public static void GOODCONFIRM(Context context, int customerId, int goodId,
			int paychannelId, int quantity, int addressId, String comment,
			int is_need_invoice, int invoice_type, String invoice_info,

			HttpCallback callback) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customerId", customerId);
		params.put("goodId", goodId);
		params.put("paychannelId", paychannelId);
		params.put("quantity", quantity);
		params.put("addressId", addressId);
		params.put("comment", comment);
		params.put("is_need_invoice", is_need_invoice);
		params.put("invoice_type", invoice_type);
		params.put("invoice_info", invoice_info);
		System.out.println("参数--" + params.toString());
		new HttpRequest(context, callback).post(Config.SHOPORDER, params);
		// new HttpRequest(context, callback).post(Config.ZDORDER, params);
	}

	public static void GOODCONFIRM1(Context context, int customerId,
			int goodId, int paychannelId, int quantity, int addressId,
			String comment, int is_need_invoice, int invoice_type,
			String invoice_info,

			HttpCallback callback) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customerId", customerId);
		params.put("goodId", goodId);
		params.put("paychannelId", paychannelId);
		params.put("quantity", quantity);
		params.put("addressId", addressId);
		params.put("comment", comment);
		params.put("is_need_invoice", is_need_invoice);
		params.put("invoice_type", invoice_type);
		params.put("invoice_info", invoice_info);
		System.out.println("参数--" + params.toString());
		// new HttpRequest(context, callback).post(Config.SHOPORDER, params);
		new HttpRequest(context, callback).post(Config.ZDORDER, params);
	}

	public static void CARTFIRM(Context context, int customerId, int[] cartid,
			int addressId, String comment,

			int is_need_invoice, int invoice_type, String invoice_info,
			String token,

			HttpCallback callback) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customerId", 80);
		// int aa[] = new int[] { 138, 140 };
		try {
			params.put("cartid", new JSONArray(gson.toJson(cartid)));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		params.put("addressId", addressId);
		params.put("comment", comment);
		params.put("is_need_invoice", is_need_invoice);
		params.put("invoice_type", invoice_type);
		params.put("invoice_info", invoice_info);
		params.put("token", token);
		System.out.println("参数--" + params.toString());
		new HttpRequest(context, callback).post(Config.SHOPCART, params);
	}

	public static void queryApplyProgress(Context context, int id,
			String phone, HttpCallback callback) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("phone", phone);
		new HttpRequest(context, callback).post(APPLY_PROGRESS, params);
		/*
		 * Context context, int customerId, String phone, HttpCallback callback)
		 * { String
		 * url="http://114.215.149.242:18080/ZFMerchant/api/terminal/openStatus"
		 * ; Map<String, Object> params = new HashMap<String, Object>();
		 * params.put("id", customerId); params.put("phone", phone); new
		 * HttpRequest(context, callback).post(url, params);
		 */
	}

	public static void getmerchantlist(Context context, int customerId,
			int page, int rows, HttpCallback callback) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customerId", customerId);
		params.put("page", page);
		params.put("rows", rows);
		GET_MERCHANTLIST = GET_MERCHANTLIST + customerId + "/";
		GET_MERCHANTLIST = GET_MERCHANTLIST + page + "/";
		GET_MERCHANTLIST = GET_MERCHANTLIST + rows;
		Log.e("GET_MERCHANTLIST", GET_MERCHANTLIST);
		new HttpRequest(context, callback).post(GET_MERCHANTLIST);
	}

	public static void AddAdres(Context context, String cityId,
			String receiver, String moblephone, String zipCode, String address,
			int isDefault, int customerId, HttpCallback callback) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("cityId", cityId);
		params.put("receiver", receiver);
		params.put("moblephone", moblephone);
		params.put("zipCode", zipCode);
		params.put("address", address);
		params.put("isDefault", isDefault);
		params.put("customerId", customerId);
		System.out.println("--ccc----" + params);
		Log.e("params", String.valueOf(params));
		new HttpRequest(context, callback).post(Add_ress, params);
	}

	public static void changeAdres(Context context, int id, String cityId,
			String receiver, String moblephone, String zipCode, String address,
			int isDefault, HttpCallback callback) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("cityId", cityId);
		params.put("receiver", receiver);
		params.put("moblephone", moblephone);
		params.put("zipCode", zipCode);
		params.put("address", address);
		params.put("isDefault", isDefault);
		Log.e("params", String.valueOf(params));
		new HttpRequest(context, callback).post(update_ress, params);
	}

	public static void gettotalscore(Context context, int customer_id,
			HttpCallback callback) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customer_id", customer_id);

		new HttpRequest(context, callback).post(total_score, params);
	}

	public static void exchange(Context context, int customerId, String name,
			String phone, int price, HttpCallback callback) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customerId", customerId);
		params.put("name", name);
		params.put("phone", phone);
		params.put("price", price);
		new HttpRequest(context, callback).post(exchange_score, params);
	}

	public static void AddAdres1(Context context, String codeNumber,

	HttpCallback callback) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codeNumber", codeNumber);

		new HttpRequest(context, callback).post(GETCODE4PHONE, params);
	}

	public static void zhuche(Context context, String username,

	String password, String code, int cityId, Boolean accountType,
			HttpCallback callback) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", username);

		params.put("password", password);
		params.put("code", code);
		params.put("cityId", cityId);
		params.put("accountType", accountType);

		new HttpRequest(context, callback).post(ZHUCHE, params);
	}

	public static void PhonefindPass(Context context, String password,
			String code, String username, HttpCallback callback) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("password", password);
		params.put("code", code);
		params.put("username", username);
		new HttpRequest(context, callback).post(Config.updatePassword, params);
	}

	public static void getEmailPass(Context context, String codeNumber,

	HttpCallback callback) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codeNumber", codeNumber);

		new HttpRequest(context, callback).post(GETEMAILPASS, params);
	}

	public static void PostSearch(Context context, String keys, int city_id,
			int rows, int page, int orderType, HttpCallback callback) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("keys", keys);
		params.put("city_id", city_id);
		params.put("rows", rows);
		params.put("page", page);
		params.put("orderType", orderType);

		params.put("has_purchase", Posport.has_purchase);
		params.put("minPrice", Posport.minPrice);
		params.put("maxPrice", Posport.maxPrice);
		try {
			params.put("brands_id",
					new JSONArray(gson.toJson(Posport.brands_id)));
			params.put("category", new JSONArray(gson.toJson(Posport.category)));
			params.put("pay_channel_id",
					new JSONArray(gson.toJson(Posport.pay_channel_id)));
			params.put("pay_card_id",
					new JSONArray(gson.toJson(Posport.pay_card_id)));
			params.put("trade_type_id",
					new JSONArray(gson.toJson(Posport.trade_type_id)));
			params.put("sale_slip_id",
					new JSONArray(gson.toJson(Posport.sale_slip_id)));
			params.put("tDate", new JSONArray(gson.toJson(Posport.tDate)));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new HttpRequest(context, callback).post(Config.POSLIST, params);
		System.out.println("参数--" + params.toString());
	}

	public static void getUserinfo(Context context,

	HttpCallback callback) {
		GET_USERINFO = GET_USERINFO + 80;
		new HttpRequest(context, callback).post(GET_USERINFO);
	}

	public static void changeuserinfo(Context context, int id, String name,
			String phone, String email, int cityId, HttpCallback callback) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("name", name);
		params.put("phone", phone);
		params.put("email", email);
		params.put("cityId", cityId);
		new HttpRequest(context, callback).post(CHANGE_USERINFO, params);
	}

	public static void changepaw(Context context, int id, String passwordOld,
			String password, HttpCallback callback) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("passwordOld", passwordOld);
		params.put("password", password);
		new HttpRequest(context, callback).post(CHANGE_PAW, params);
	}

	public static void submitApply(Context context, Map<String, Object> params,
			HttpCallback callback) {
		new HttpRequest(context, callback).post(APPLY_SUBMIT, params);
	}

	public static void insertmerchant(Context context, String title,
			String legalPersonName, String legalPersonCardId,
			String businessLicenseNo, String taxRegisteredNo,
			String organizationCodeNo, int cityId, String accountBankName,
			String bankOpenAccount, String cardIdFrontPhotoPath,
			String cardIdBackPhotoPath, String bodyPhotoPath,
			String licenseNoPicPath, String taxNoPicPath,
			String orgCodeNoPicPath, String accountPicPath, int customerId,
			HttpCallback callback) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("title", title);
		params.put("legalPersonName", legalPersonName);
		params.put("legalPersonCardId", legalPersonCardId);
		params.put("businessLicenseNo", businessLicenseNo);
		params.put("taxRegisteredNo", taxRegisteredNo);
		params.put("organizationCodeNo", organizationCodeNo);
		params.put("cityId", cityId);
		params.put("accountBankName", accountBankName);
		params.put("bankOpenAccount", bankOpenAccount);
		params.put("cardIdFrontPhotoPath", cardIdFrontPhotoPath);
		params.put("cardIdBackPhotoPath", cardIdBackPhotoPath);
		params.put("bodyPhotoPath", bodyPhotoPath);
		params.put("licenseNoPicPath", licenseNoPicPath);
		params.put("taxNoPicPath", taxNoPicPath);
		params.put("orgCodeNoPicPath", orgCodeNoPicPath);
		params.put("accountPicPath", accountPicPath);
		params.put("customerId", customerId);
		new HttpRequest(context, callback).post(CREAT_MERCHANT, params);
	}

	public static void updatemerchant(Context context, String title,
			String legalPersonName, String legalPersonCardId,
			String businessLicenseNo, String taxRegisteredNo,
			String organizationCodeNo, int cityId, String accountBankName,
			String bankOpenAccount, String cardIdFrontPhotoPath,
			String cardIdBackPhotoPath, String bodyPhotoPath,
			String licenseNoPicPath, String taxNoPicPath,
			String orgCodeNoPicPath, String accountPicPath, int id,
			HttpCallback callback) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("title", title);
		params.put("legalPersonName", legalPersonName);
		params.put("legalPersonCardId", legalPersonCardId);
		params.put("businessLicenseNo", businessLicenseNo);
		params.put("taxRegisteredNo", taxRegisteredNo);
		params.put("organizationCodeNo", organizationCodeNo);
		params.put("cityId", cityId);
		params.put("accountBankName", accountBankName);
		params.put("bankOpenAccount", bankOpenAccount);
		params.put("cardIdFrontPhotoPath", cardIdFrontPhotoPath);
		params.put("cardIdBackPhotoPath", cardIdBackPhotoPath);
		params.put("bodyPhotoPath", bodyPhotoPath);
		params.put("licenseNoPicPath", licenseNoPicPath);
		params.put("taxNoPicPath", taxNoPicPath);
		params.put("orgCodeNoPicPath", orgCodeNoPicPath);
		params.put("accountPicPath", accountPicPath);
		params.put("id", id);
		new HttpRequest(context, callback).post(UPDATE_MERCHANT, params);
	}
	public static void updateFile(
			Context context,
			File fileImg,
			HttpCallback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("fileImg", fileImg);
		new HttpRequest(context, callback).post(UPDATE_FILE, params);
	}

	public static void getApplyBankList(Context context, String terminalNumber,
			HttpCallback callback) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("serial_num", terminalNumber);
		new HttpRequest(context, callback).post(APPLY_BANK_LIST, params);
	}
}
