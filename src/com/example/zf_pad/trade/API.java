package com.example.zf_pad.trade;

import android.content.Context;
import android.util.Log;



import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;


import com.example.zf_pad.fragment.mine_Address;

import com.example.zf_pad.Config;
import com.example.zf_pad.posport;

import com.example.zf_pad.trade.common.HttpCallback;
import com.example.zf_pad.trade.common.HttpRequest;
import com.google.gson.Gson;

import static com.example.zf_pad.fragment.Constants.AfterSaleType.CANCEL;
import static com.example.zf_pad.fragment.Constants.AfterSaleType.CHANGE;
import static com.example.zf_pad.fragment.Constants.AfterSaleType.LEASE;
import static com.example.zf_pad.fragment.Constants.AfterSaleType.MAINTAIN;
import static com.example.zf_pad.fragment.Constants.AfterSaleType.RETURN;
import static com.example.zf_pad.fragment.Constants.AfterSaleType.UPDATE;


public class API {

    public static final String SCHEMA = "http://";
    public static final String HOST = "114.215.149.242:18080";
//    public static final String HOST = "192.168.1.101:8080";

    // selection terminal list
    public static final String TERMINAL_LIST = SCHEMA + HOST + "/ZFMerchant/api/trade/record/getTerminals/%d";
    // trade record list
    public static final String TRADE_RECORD_LIST = SCHEMA + HOST + "/ZFMerchant/api/trade/record/getTradeRecords/%d/%s/%s/%s/%d/%d";
    // trade record statistic
    public static final String TRADE_RECORD_STATISTIC = SCHEMA + HOST + "/ZFMerchant/api/trade/record/getTradeRecordTotal/%d/%s/%s/%s";
    // trade record detail
	public static final String TRADE_RECORD_DETAIL = SCHEMA + HOST + "/ZFMerchant/api/trade/record/getTradeRecord/%d/%d";

    // After sale record list
    public static final String AFTER_SALE_MAINTAIN_LIST = SCHEMA + HOST + "/ZFMerchant/api/cs/repair/getAll";
    public static final String AFTER_SALE_RETURN_LIST = SCHEMA + HOST + "/ZFMerchant/api/return/getAll";
    public static final String AFTER_SALE_CANCEL_LIST = SCHEMA + HOST + "/ZFMerchant/api/cs/cancels/getAll";
    public static final String AFTER_SALE_CHANGE_LIST = SCHEMA + HOST + "/ZFMerchant/api/cs/change/getAll";
    public static final String AFTER_SALE_UPDATE_LIST = SCHEMA + HOST + "/ZFMerchant/api/update/info/getAll";
    public static final String AFTER_SALE_LEASE_LIST = SCHEMA + HOST + "/ZFMerchant/api/cs/lease/returns/getAll";

    // After sale record detail
    public static final String AFTER_SALE_MAINTAIN_DETAIL = SCHEMA + HOST + "/ZFMerchant/api/cs/repair/getRepairById";
    public static final String AFTER_SALE_RETURN_DETAIL = SCHEMA + HOST + "/ZFMerchant/api/return/getReturnById";
    public static final String AFTER_SALE_CANCEL_DETAIL = SCHEMA + HOST + "/ZFMerchant/api/cs/cancels/getCanCelById";
    public static final String AFTER_SALE_CHANGE_DETAIL = SCHEMA + HOST + "/ZFMerchant/api/cs/change/getChangeById";
    public static final String AFTER_SALE_UPDATE_DETAIL = SCHEMA + HOST + "/ZFMerchant/api/update/info/getInfoById";
    public static final String AFTER_SALE_LEASE_DETAIL = SCHEMA + HOST + "/ZFMerchant/api/cs/lease/returns/getById";

    // After sale record cancel apply
    public static final String AFTER_SALE_MAINTAIN_CANCEL = SCHEMA + HOST + "/ZFMerchant/api/cs/repair/cancelApply";
    public static final String AFTER_SALE_RETURN_CANCEL = SCHEMA + HOST + "/ZFMerchant/api/return/cancelApply";
    public static final String AFTER_SALE_CANCEL_CANCEL = SCHEMA + HOST + "/ZFMerchant/api/cs/cancels/cancelApply";
    public static final String AFTER_SALE_CHANGE_CANCEL = SCHEMA + HOST + "/ZFMerchant/api/cs/change/cancelApply";
    public static final String AFTER_SALE_UPDATE_CANCEL = SCHEMA + HOST + "/ZFMerchant/api/update/info/cancelApply";
    public static final String AFTER_SALE_LEASE_CANCEL = SCHEMA + HOST + "/ZFMerchant/api/cs/lease/returns/cancelApply";

    // After sale resubmit cancel
    public static final String AFTER_SALE_RESUBMIT_CANCEL = SCHEMA + HOST + "/ZFMerchant/api/cs/cancels/resubmitCancel";

    // After sale add mark
    public static final String AFTER_SALE_MAINTAIN_ADD_MARK = SCHEMA + HOST + "/ZFMerchant/api/cs/repair/addMark";
    public static final String AFTER_SALE_RETURN_ADD_MARK = SCHEMA + HOST + "/ZFMerchant/api/return/addMark";
    public static final String AFTER_SALE_CHANGE_ADD_MARK = SCHEMA + HOST + "/ZFMerchant/api/cs/change/addMark";
    public static final String AFTER_SALE_LEASE_ADD_MARK = SCHEMA + HOST + "/ZFMerchant/api/cs/lease/returns/addMark";

    // Terminal list
    public static final String TERMINAL_APPLY_LIST = SCHEMA + HOST + "/ZFMerchant/api/terminal/getApplyList";
    // Channel list
    public static final String CHANNEL_LIST = SCHEMA + HOST + "/ZFMerchant/api/terminal/getFactories";
    // Terminal Add
    public static final String TERMINAL_ADD = SCHEMA + HOST + "/ZFMerchant/api/terminal/addTerminal";
    // Terminal detail
    public static final String TERMINAL_DETAIL = SCHEMA + HOST + "/ZFMerchant/api/terminal/getApplyDetails";

    // synchronise terminal
    public static final String TERMINAL_SYNC = SCHEMA + HOST + "/ZFMerchant/api/terminal/synchronous";
    // find pos password
    public static final String TERMINAL_FIND_POS = SCHEMA + HOST + "/ZFMerchant/api/terminal/Encryption";

    // Apply List
    public static final String APPLY_LIST = SCHEMA + HOST + "/ZFMerchant/api/apply/getApplyList";
    // Apply Detail
    public static final String APPLY_DETAIL = SCHEMA + HOST + "/ZFMerchant/api/apply/getApplyDetails";
    // Get the Merchant Detail
    public static final String APPLY_MERCHANT_DETAIL = SCHEMA + HOST + "/ZFMerchant/api/apply/getMerchant";
    // Get the Channel List
    public static final String APPLY_CHANNEL_LIST = SCHEMA + HOST + "/ZFMerchant/api/apply/getChannels";

    // upload image url
    public static final String UPLOAD_IMAGE = SCHEMA + HOST + "/ZFMerchant/api/comment/upload/tempImage";

    public static final String WNATBUY = SCHEMA + HOST + "/ZFMerchant/api/paychannel/intention/add";

 // Apply Opening Progress Query
 	public static final String APPLY_PROGRESS = SCHEMA + HOST + "/ZFMerchant/api/terminal/openStatus";

 	// Get merchant list
 	public static String GET_MERCHANTLIST=SCHEMA + HOST+"/ZFMerchant/api/merchant/findList/";
 	// Add address
 	public static final String Add_ress = SCHEMA + HOST + "/ZFMerchant/api/customers/insertAddress/";
 	// update address
 	public static final String update_ress = SCHEMA + HOST + "/ZFMerchant/api/customers/updateAddress/";
 	// get totalscore
 	public static String total_score = SCHEMA + HOST + "/ZFMerchant/api/customers/getIntegralTotal/";
 	// exchange score
 	public static String exchange_score = SCHEMA + HOST + "/ZFMerchant/api/customers/insertIntegralConvert";

	public static final String GETCODE4PHONE = SCHEMA + HOST + "/ZFMerchant/api/user/sendPhoneVerificationCodeReg";
	public static final String ZHUCHE = SCHEMA + HOST + "/ZFMerchant/api/user/userRegistration";
	public static final String GETEMAILPASS = SCHEMA + HOST + "/ZFMerchant/api/user/sendEmailVerificationCode";

	public static void getTerminalList(
			Context context,
			int customerId,
			HttpCallback callback) {
		new HttpRequest(context, callback).post(String.format(TERMINAL_LIST, customerId));
	}

	public static void getTradeRecordList(
			Context context,
			int tradeTypeId,
			String terminalNumber,
			String startTime,
			String endTime,
			int page,
			int rows,
			HttpCallback callback) {
		new HttpRequest(context, callback).post(String.format(TRADE_RECORD_LIST,
				tradeTypeId, terminalNumber, startTime, endTime, page, rows));
	}

	public static void getTradeRecordStatistic(
			Context context,
			int tradeTypeId,
			String terminalNumber,
			String startTime,
			String endTime,
			HttpCallback callback) {
		new HttpRequest(context, callback).post(String.format(TRADE_RECORD_STATISTIC,
				tradeTypeId, terminalNumber, startTime, endTime));
	}

	public static void getTradeRecordDetail(
			Context context,
			int typeId,
			int recordId,
			HttpCallback callback) {
		new HttpRequest(context, callback).post(String.format(TRADE_RECORD_DETAIL, typeId, recordId));
	}

    public static void getAfterSaleRecordList(
            Context context,
            int recordType,
            int customId,
            int page,
            int pageSize,
            HttpCallback callback) {
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
                throw new IllegalArgumentException("illegal argument [recordType], within [0-5]");
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("customer_id", customId);
        params.put("page", page);
        params.put("pageSize", pageSize);
        new HttpRequest(context, callback).post(url, params);
    }

    public static void getAfterSaleRecordDetail(
            Context context,
            int recordType,
            int recordId,
            HttpCallback callback) {
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
                throw new IllegalArgumentException("illegal argument [recordType], within [0-5]");
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", recordId);
        new HttpRequest(context, callback).post(url, params);
    }

    public static void cancelAfterSaleApply(
            Context context,
            int recordType,
            int recordId,
            HttpCallback callback) {
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
                throw new IllegalArgumentException("illegal argument [recordType], within [0-5]");
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", recordId);
        new HttpRequest(context, callback).post(url, params);
    }

    public static void resubmitCancel(
            Context context,
            int recordId,
            HttpCallback callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", recordId);
        new HttpRequest(context, callback).post(AFTER_SALE_RESUBMIT_CANCEL, params);
    }

    public static void addMark(
            Context context,
            int recordType,
            int recordId,
            int customerId,
            String company,
            String number,
            HttpCallback callback) {
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
                throw new IllegalArgumentException("illegal argument [recordType], within [0,1,3,5]");
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", recordId);
        params.put("customer_id", customerId);
        params.put("computer_name", company);
        params.put("track_number", number);
        new HttpRequest(context, callback).post(url, params);
    }

    public static void getTerminalApplyList(
            Context context,
            int customerId,
            int page,
            int pageSize,
            HttpCallback callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("customersId", customerId);
        params.put("indexPage", page);
        params.put("pageNum", pageSize);
        new HttpRequest(context, callback).post(TERMINAL_APPLY_LIST, params);
    }
    public static void test(
            Context context,
            String customerId,

            HttpCallback callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("codeNumber", customerId);
        new HttpRequest(context, callback).post("http://114.215.149.242:18080/ZFMerchant/api/user/sendPhoneVerificationCodeFind", params);
    }
    public static void getChannelList(
            Context context,
            HttpCallback callback) {
        new HttpRequest(context, callback).post(CHANNEL_LIST);
    }

    public static void addTerminal(
            Context context,
            int customerId,
            int payChannelId,
            String terminalNumber,
            String merchantName,
            HttpCallback callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("customerId", customerId);
        params.put("payChannelId", payChannelId);
        params.put("serialNum", terminalNumber);
        params.put("title", merchantName);
        new HttpRequest(context, callback).post(TERMINAL_ADD, params);
    }

    public static void getTerminalDetail(
            Context context,
            int terminalId,
            HttpCallback callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("terminalsId", terminalId + "");
        new HttpRequest(context, callback).post(TERMINAL_DETAIL, params);
    }

    public static void findPosPassword(
            Context context,
            int terminalId,
            HttpCallback callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("terminalid", terminalId);
        new HttpRequest(context, callback).post(TERMINAL_DETAIL, params);
    }

    public static void getApplyList(
            Context context,
            int customerId,
            int page,
            int pageSize,
            HttpCallback callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("customersId", customerId);
        params.put("indexPage", page);
        params.put("pageNum", pageSize);
        new HttpRequest(context, callback).post(APPLY_LIST, params);
    }

    public static void getApplyDetail(
            Context context,
            int customerId,
            int terminalId,
            int status,
            HttpCallback callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("customerId", customerId);
        params.put("terminalsId", terminalId);
        params.put("status", status);
        new HttpRequest(context, callback).post(APPLY_DETAIL, params);
    }

    public static void getApplyMerchantDetail(
            Context context,
            int merchantId,
            HttpCallback callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("merchantId", merchantId);
        new HttpRequest(context, callback).post(APPLY_MERCHANT_DETAIL, params);
    }

    public static void getApplyChannelList(
            Context context,
            HttpCallback callback) {
        new HttpRequest(context, callback).post(APPLY_CHANNEL_LIST);
    }

	public static void ApiWantBug(
			Context context,
			String  name,
			String phone,
			String content,
			HttpCallback callback) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		params.put("phone", phone);
		params.put("content", content);
		new HttpRequest(context, callback).post(WNATBUY, params);
	}
	public static void Login1(
			Context context,
			String  username,
			String  passsword,
			HttpCallback callback) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", username);
		params.put("password", passsword);
		new HttpRequest(context, callback).post(Config.LOGIN, params);
	}
	public static void GOODCONFIRM(
			Context context,
			int customerId,
			int goodId,
			int paychannelId,
			int quantity,
			int addressId,
			String  comment,
			int is_need_invoice,
			int invoice_type,
			String  invoice_info,
		
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
		System.out.println("����--"+params.toString());
		new HttpRequest(context, callback).post(Config.SHOPORDER, params);
	}
	public static void CARTFIRM(
			Context context,
			int customerId,
			int [] cartid,
			int addressId,
			String  comment,
			
			int is_need_invoice,
			int invoice_type,
			String  invoice_info,
		
			HttpCallback callback) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customerId", 80); 
	 	int aa[]=new int []{138,140};
		params.put("cartid",  aa); 
		params.put("addressId", addressId);
		params.put("comment", comment);
		params.put("is_need_invoice", is_need_invoice);
		params.put("invoice_type", invoice_type);
		params.put("invoice_info", invoice_info);
		System.out.println("����--"+params.toString());
		new HttpRequest(context, callback).post(Config.SHOPORDER, params);
	}

    public static void queryApplyProgress(
    		Context context,
    		int id,
			String phone,
			HttpCallback callback){
    	Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("phone", phone);
		new HttpRequest(context, callback).post(APPLY_PROGRESS, params);
    		/*Context context,
			int customerId,
			String phone,
			HttpCallback callback) {
    	String url="http://114.215.149.242:18080/ZFMerchant/api/terminal/openStatus";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", customerId);
		params.put("phone", phone);
		new HttpRequest(context, callback).post(url, params);*/
    }

    public static void getmerchantlist(
			Context context,
			int customerId,
			int page,
			int rows,
			HttpCallback callback) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customerId", customerId);
		params.put("page", page);
		params.put("rows", rows);
		GET_MERCHANTLIST=GET_MERCHANTLIST+customerId+"/";
		GET_MERCHANTLIST=GET_MERCHANTLIST+page+"/";
		GET_MERCHANTLIST=GET_MERCHANTLIST+rows;
		Log.e("GET_MERCHANTLIST", GET_MERCHANTLIST);
		new HttpRequest(context, callback).post(GET_MERCHANTLIST);
	}
    public static void AddAdres(
			Context context,
			String  cityId,
			String  receiver,
			String 	moblephone,
			String 	zipCode,
			String 	address,
			int 	isDefault,
			int 	customerId,
			HttpCallback callback) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("cityId", cityId);
		params.put("receiver", receiver);
		params.put("moblephone", moblephone);
		params.put("zipCode", zipCode);
		params.put("address", address);
		params.put("isDefault", isDefault);
        if(!mine_Address.isclickitem){
        	params.put("customerId", customerId);
		}
        else{
        	params.put("id", customerId);
        }
		System.out.println("--ccc----"+params);
		Log.e("params", String.valueOf(params));
		if(!mine_Address.isclickitem){
			new HttpRequest(context, callback).post(Add_ress, params);
		}
		else{
			new HttpRequest(context, callback).post(update_ress, params);
		}
	}
    public static void gettotalscore(
    		Context context,
    		int customerId,
    		HttpCallback callback){
    	total_score=total_score+80;
    	new HttpRequest(context, callback).post(total_score);
    }
    public static void exchange(
    		Context context,
    		int customerId,
    		String name,
    		String phone,
    		int price,
    		HttpCallback callback){
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("customerId", customerId);
    	params.put("name", name);
    	params.put("phone", phone);
    	params.put("price", price);
    	new HttpRequest(context, callback).post(exchange_score, params);
    }

    public static void AddAdres1(
			Context context,
			String  codeNumber,
	 
			HttpCallback callback) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codeNumber", codeNumber);
 
		new HttpRequest(context, callback).post(GETCODE4PHONE, params);
	}
    public static void zhuche(
			Context context,
			String  username,
			 
			String  password,
			String  code,
			int  cityId,
			Boolean accountType,
			HttpCallback callback) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", username);
		 
		params.put("password", password);
		params.put("code", code);
		params.put("cityId", cityId);
		params.put("accountType", accountType);

		new HttpRequest(context, callback).post(ZHUCHE, params);
	}
    public static void PhonefindPass(
			Context context,
			String  password,
			String code,
			String username,
			HttpCallback callback) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("password", password);
		params.put("code", code);
		params.put("username", username);
		new HttpRequest(context, callback).post(Config.updatePassword, params);
	}
	public static void getEmailPass(
			Context context,
			String  codeNumber,
	 
			HttpCallback callback) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codeNumber", codeNumber);
 
		new HttpRequest(context, callback).post(GETEMAILPASS, params);
	}
	static Gson gson = new Gson();
	public static void PostSearch(
			Context context,
			String  keys,
			int city_id,
			int rows,
			int page,
			int orderType,		
			HttpCallback callback) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("keys", keys);
		params.put("city_id", city_id);
		params.put("rows", rows);
		params.put("page", page);
		params.put("orderType", orderType);
		params.put("has_purchase", posport.has_purchase);
		params.put("minPrice",posport.minPrice);
		params.put("maxPrice",posport.maxPrice);
		try {
			params.put("brands_id", new JSONArray(gson.toJson(posport.brands_id)));
			params.put("category", new JSONArray(gson.toJson(posport.category)));
			params.put("pay_channel_id", new JSONArray(gson.toJson(posport.pay_channel_id)));
			params.put("pay_card_id", new JSONArray(gson.toJson(posport.pay_card_id)));
			params.put("trade_type_id", new JSONArray(gson.toJson(posport.trade_type_id)));
			params.put("sale_slip_id", new JSONArray(gson.toJson(posport.sale_slip_id)));
			params.put("tDate", new JSONArray(gson.toJson(posport.tDate)));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new HttpRequest(context, callback).post(Config.POSLIST, params);
	}

}
