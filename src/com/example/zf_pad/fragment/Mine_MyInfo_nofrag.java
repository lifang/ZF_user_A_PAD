package com.example.zf_pad.fragment;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.zf_pad.Config;
import com.example.zf_pad.MyApplication;
import com.epalmpay.userPad.R;
import com.example.zf_pad.aadpter.AddressManagerAdapter;
import com.example.zf_pad.aadpter.ScoreAdapter;
import com.example.zf_pad.activity.AdressEdit;
import com.example.zf_pad.activity.ChangeEmail;
import com.example.zf_pad.activity.ChangePhone;
import com.example.zf_pad.activity.Exchange;
import com.example.zf_pad.activity.LoginActivity;
import com.example.zf_pad.activity.MainActivity;
import com.example.zf_pad.entity.AddressManager;
import com.example.zf_pad.entity.Score;
import com.example.zf_pad.trade.API;
import com.example.zf_pad.trade.CityProvinceActivity;
import com.example.zf_pad.trade.common.CommonUtil;
import com.example.zf_pad.trade.common.DialogUtil;
import com.example.zf_pad.trade.common.HttpCallback;
import com.example.zf_pad.trade.entity.City;
import com.example.zf_pad.trade.entity.Province;
import com.example.zf_pad.trade.widget.MTabWidget;
import com.example.zf_pad.trade.widget.MTabWidget.OnTabOnclik;
import com.example.zf_pad.util.MyToast;
import com.example.zf_pad.util.ScrollViewWithListView;
import com.example.zf_pad.util.StringUtil;
import com.example.zf_pad.util.Tools;
import com.example.zf_pad.util.XListView;
import com.example.zf_pad.util.XListView.IXListViewListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class Mine_MyInfo_nofrag extends Fragment implements OnTabOnclik,OnClickListener,IXListViewListener{
	private View view;
	private TextView tv_score,tv_manageradress,tv_info,tv_safe;
	public static int mRecordType=0;
	//private LinearLayout ll_myinfo;
	private MTabWidget mTabWidget;
	private Message msg;
	public static boolean isHiddenn=false;
	//基础信息,安全,地址管理,积分
	private LinearLayout baseinfo_Layout,changepaw_Layout,manageradress_Layout,score_Layout;
	//基础信息
	private TextView tv_city_select,changephone,changeemail;
	private String cityName;
	public static final int REQUEST_CITY = 1;
	private int cityId;
	private JSONObject result;
	public static Handler myHandler;
	private EditText et_name,et_phone,et_email,et_address;
	private List<Province> provinces;
	private List<City> mCities = new ArrayList<City>();
	private Button btn_save,btn_exit;
	public static String pawwword="";
	private int id;
	private ScrollView sLV;
	private SharedPreferences mySharedPreferences;
	private Activity mActivity;
	//安全
	private String password;
	private EditText et_oldpaw,et_newpaw,et_confirmpaw;
	private Button btn_save_chgpaw;
	//地址管理
	public static List<AddressManager> dataadress;
	private BaseAdapter addressadapter;
	private ScrollViewWithListView list;
	private Context context;
	private Button btn_add;
	public static boolean isclickitem=false;
	public static LinearLayout ll_address;
	private int j=0;
	public static int[] idd;
	public static int type=0;
	private boolean isAddress = false;
	private boolean isBaseinfo = false;
	private boolean isChangepaw = false;
	//积分
	private List<Score> datasco;
	private List<Score> moreList;
	private XListView xxlistview;
	private BaseAdapter scoreadapter;
	private int customerId;
	private int page=1;
	private int rows=10;
	private boolean isrefersh=false;
	private boolean isLoadMore=false;
	private Button btn_exchange;
	private TextView tv_total;
	private int totalscore=0;
	private boolean isStop=false;
	private int price1,sxfmoney;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		try {
			view = inflater.inflate(R.layout.f_mine_myinfo, container, false);
			init();
			id=MyApplication.NewUser.getId();
			customerId=MyApplication.NewUser.getId();
			Mine_baseinfo();
		} catch (InflateException e) {
			System.out.println(e);
		}

		return view;
	}
	@Override
	public void onStart() {
		super.onStart();
		myHandler=new Handler(){
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case 0:
					onLoad();
					if(datasco.size()!=0){
						xxlistview.setVisibility(View.VISIBLE);
						xxlistview.setAdapter(scoreadapter);
					}
					else{
						xxlistview.setVisibility(View.GONE);
					}
					break;
				case 1:
					onLoad();

					break;
				case 2:
					isclickitem=true;
					Intent intent=new Intent(mActivity,AdressEdit.class);
					intent.putExtra("position", AddressManagerAdapter.pp);
					startActivity(intent);

					break;
				default:
					break;
				}
			};
		};
	}
	@Override
	public void onResume() {
		super.onResume();
		//mTabWidget.updateTabs(mRecordType);
		if (isAddress == true) {
			isAddress = false;
			Mine_Address();
		}
		if (isBaseinfo == true) {
			//isBaseinfo = false;
			//Mine_baseinfo();	
		}
		if (isChangepaw == true) {
			isChangepaw = false;
			Mine_chgpaw();
		}
		if(Config.AderssManger){
			mTabWidget.updateTabs(2);
			Config.AderssMangerBACK=true;
		}
		if (!StringUtil.isNull(Config.changePhoneNum)) {
			et_phone.setVisibility(View.VISIBLE);
			changephone.setText("修改");
			et_phone.setText(Config.changePhoneNum);
			Config.changePhoneNum = "";
		}
		if (!StringUtil.isNull(Config.changeemail)) {
			et_email.setVisibility(View.VISIBLE);
			changeemail.setText("修改");
			et_email.setText(Config.changeemail);
			Config.changeemail = "";
		}
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = activity;
	}
	@Override
	public void onPause() {
		super.onPause();
	}
	private void init() {
		baseinfo_Layout = (LinearLayout) view.findViewById(R.id.baseinfo_Layout);
		changepaw_Layout = (LinearLayout) view.findViewById(R.id.changepaw_Layout);
		manageradress_Layout = (LinearLayout) view.findViewById(R.id.manageradress_Layout);
		score_Layout = (LinearLayout) view.findViewById(R.id.score_Layout);

		//msg=Mine_baseinfo.myHandler.obtainMessage();
		Log.e("viewR", String.valueOf(view));
		mTabWidget = (MTabWidget)view.findViewById(R.id.tab_widget);
		String[] tabs = getResources().getStringArray(R.array.mine_myinfo);
		for (int i = 0; i < tabs.length; i++) {
			mTabWidget.addTab(tabs[i]);
		}
		mTabWidget.updateTabs(0);
		mTabWidget.setonTabLintener(this);

	}
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Config.AderssManger=false;
	}
	@Override
	public void chang(int index) {
		mRecordType=index;
		switch (mRecordType) {
		case 0:
			baseinfo_Layout.setVisibility(View.VISIBLE);
			changepaw_Layout.setVisibility(View.GONE);
			manageradress_Layout.setVisibility(View.GONE);
			score_Layout.setVisibility(View.GONE);
			Mine_baseinfo();
			break;
		case 1:
			baseinfo_Layout.setVisibility(View.GONE);
			changepaw_Layout.setVisibility(View.VISIBLE);
			manageradress_Layout.setVisibility(View.GONE);
			score_Layout.setVisibility(View.GONE);
			Mine_chgpaw();
			break;
		case 2:
			baseinfo_Layout.setVisibility(View.GONE);
			changepaw_Layout.setVisibility(View.GONE);
			manageradress_Layout.setVisibility(View.VISIBLE);
			score_Layout.setVisibility(View.GONE);
			Mine_Address();
			break;
		case 3:
			baseinfo_Layout.setVisibility(View.GONE);
			changepaw_Layout.setVisibility(View.GONE);
			manageradress_Layout.setVisibility(View.GONE);
			score_Layout.setVisibility(View.VISIBLE);
			Mine_score();
			break;
		default:
			break;
		}
	}
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.tv_city_select:
			Intent intent = new Intent(mActivity,
					CityProvinceActivity.class);
			startActivityForResult(intent, com.example.zf_pad.fragment.Constants.ApplyIntent.REQUEST_CHOOSE_CITY);
			break;
		case R.id.btn_save:
			changeUserinfo();
			break;
		case R.id.changephone:
			Intent intent2 = new Intent(mActivity,ChangePhone.class);
			intent2.putExtra("key", 2);
			intent2.putExtra("name", et_phone.getText().toString());
			startActivityForResult(intent2, 2);
			break;
		case R.id.changeemail:
			Intent intent3 = new Intent(mActivity,ChangeEmail.class);
			intent3.putExtra("key",3);
			intent3.putExtra("name", et_email.getText().toString());
			startActivityForResult(intent3, 3);
			break;
		case R.id.btn_exit:
			mySharedPreferences = mActivity.getSharedPreferences(Config.SHARED,mActivity. MODE_PRIVATE);
			Config.isExit=true;
			MyApplication.NewUser=null;
			startActivity(new Intent(mActivity,MainActivity.class));

			Editor editor=mySharedPreferences.edit();
			editor.putBoolean("islogin", false);
			editor.putString("name", null);
			editor.putInt("id", -1);
			editor.commit();
			//Intent i = new Intent(mActivity, LoginActivity.class);
			//startActivity(i);
			break;
		case R.id.btn_save_chgpaw:
			if (check ()) {
				changepaw();
			}
			break;
		case R.id.btn_add:
			Intent intent4=new Intent(mActivity,AdressEdit.class);	
			startActivity(intent4);
			break;
		default:
			break;
		}


	}

	/*
	 * 基础信息
	 */
	private void Mine_baseinfo() {

		isBaseinfo = true;

		provinces = CommonUtil.readProvincesAndCities(mActivity);
		sLV=(ScrollView) view.findViewById(R.id.sLV);
		btn_exit=(Button) view.findViewById(R.id.btn_exit);
		btn_save=(Button) view.findViewById(R.id.btn_save);
		et_name=(EditText) view.findViewById(R.id.et_name);
		et_phone=(EditText) view.findViewById(R.id.et_phone);
		et_email=(EditText) view.findViewById(R.id.et_email);
		changephone = (TextView) view.findViewById(R.id.changephone);
		changeemail = (TextView) view.findViewById(R.id.changeemail);
		//et_address=(EditText) view.findViewById(R.id.et_address);
		tv_city_select=(TextView) view.findViewById(R.id.tv_city_select);
		tv_city_select.setOnClickListener(this);
		btn_save.setOnClickListener(this);
		btn_exit.setOnClickListener(this);
		changephone.setOnClickListener(this);
		changeemail.setOnClickListener(this);

		getUserInfo();
	}
	private void getUserInfo() {
		if(!Tools.isConnect(mActivity)){
			CommonUtil.toastShort(mActivity, "网络异常");
			return;
		}
		MyApplication.getInstance().getClient().post(API.GET_USERINFO+id, new AsyncHttpResponseHandler() {
			private Dialog loadingDialog;

			@Override
			public void onStart() {	
				super.onStart();
				loadingDialog = DialogUtil.getLoadingDialg(mActivity);
				loadingDialog.show();
			}
			@Override
			public void onFinish() {
				super.onFinish();
				loadingDialog.dismiss();
			}
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				String responseMsg = new String(responseBody)
				.toString();

				String code = null;
				try {
					JSONObject jsonobject = new JSONObject(responseMsg);
					code = jsonobject.getString("code");
					int a =jsonobject.getInt("code");
					if(a==Config.CODE){ 
						result=jsonobject.getJSONObject("result");
						Log.e("result", String.valueOf(result));

						try {
							pawwword=result.getString("password");

							et_phone.setText(result.getString("phone"));
						
							Log.e("2", tv_city_select.getText().toString());
							et_name.setText(result.getString("name"));
							et_email.setText(result.getString("email"));

							if (StringUtil.isNull(result.getString("phone"))) {
								et_phone.setVisibility(View.GONE);
								changephone.setText("去添加");
							}
							if (StringUtil.isNull(result.getString("email"))) {
								et_email.setVisibility(View.GONE);
								changeemail.setText("去添加");
							}
							if (!StringUtil.isNull(result.getString("city_id"))) {
								tv_city_select.setText(findcity(Integer.valueOf(result.getString("city_id"))));
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Toast.makeText(mActivity, "注册信息不完全", Toast.LENGTH_SHORT).show();
						}

					}
					else{
						code = jsonobject.getString("message");
						Toast.makeText(mActivity, code, 1000).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {

			}
		});
	}
	protected String findcity(int id) {
		// TODO Auto-generated method stub
		String a="苏州";
		List<Province> provinces = CommonUtil.readProvincesAndCities(mActivity);
		for (Province province : provinces) {
			List<City> cities = province.getCities();

			mCities.addAll(cities);

		}
		for(City cc:mCities ){
			if(cc.getId()==id){
				a=cc.getName();
			}
		}
		return a;
	}
	private void changeUserinfo() {
		if(!Tools.isConnect(mActivity)){
			CommonUtil.toastShort(mActivity, "网络异常");
			return;
		}
		API.changeuserinfo(mActivity, id, et_name.getText().toString(), 
				et_phone.getText().toString(), et_email.getText().toString(), cityId, 
				new HttpCallback(mActivity) {

			@Override
			public void onSuccess(Object data) {
				Toast.makeText(mActivity, "保存信息成功", 
						Toast.LENGTH_SHORT).show();

			}

			@Override
			public TypeToken getTypeToken() {
				// TODO Auto-generated method stub
				return null;
			}
		});

	}
	/*
	 * 安全
	 */
	private void Mine_chgpaw() {
		isChangepaw = true;

		et_oldpaw=(EditText) view.findViewById(R.id.et_oldpaw);
		et_newpaw=(EditText) view.findViewById(R.id.et_newpaw);
		et_confirmpaw=(EditText) view.findViewById(R.id.et_confirmpaw);
		btn_save_chgpaw=(Button) view.findViewById(R.id.btn_save_chgpaw);
		btn_save_chgpaw.setOnClickListener(this);
	}
	protected void changepaw() {
		if(!Tools.isConnect(mActivity)){
			CommonUtil.toastShort(mActivity, "网络异常");
			return;
		}
		API.changepaw(mActivity, id, StringUtil.Md5(et_oldpaw.getText().toString()), 
				StringUtil.Md5(et_newpaw.getText().toString()), 
				new HttpCallback(mActivity) {

			@Override
			public void onSuccess(Object data) {
				Toast.makeText(mActivity, "修改密码成功", Toast.LENGTH_SHORT).show();

			}

			@Override
			public TypeToken getTypeToken() {
				return null;
			}
		});

	}
	public static String MD5(String str) {
		MessageDigest md5 =null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch(Exception e) {
			e.printStackTrace();
			return "";
		}

		char[] charArray = str.toCharArray();
		byte[] byteArray =new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}
		byte[] md5Bytes = md5.digest(byteArray);

		StringBuffer hexValue =new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) &0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}
	private boolean check () {
		if (StringUtil.isNull(et_oldpaw.getText().toString().trim())) {
			MyToast.showToast(mActivity,"请输入您的原始密码");
			return false;
		}

		if (StringUtil.isNull(et_newpaw.getText().toString().trim())) {
			MyToast.showToast(mActivity,"请输入您的新密码");
			return false;
		}
		if (StringUtil.isNull(et_confirmpaw.getText().toString().trim())) {
			MyToast.showToast(mActivity,"请确认您的新密码");
			return false;
		}
		if (!et_newpaw.getText().toString().trim().equals(et_confirmpaw.getText().toString().trim())) {
			MyToast.showToast(mActivity,"两次输入的密码不一致");
			return false;
		}
		if (et_newpaw.getText().toString().length() < 6) {
			Toast.makeText(mActivity, "密码长度不能少于6位",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	/*
	 * 地址管理
	 */
	private void Mine_Address() {
		isAddress = true;

		ll_address=(LinearLayout) view.findViewById(R.id.ll_address);
		dataadress=new ArrayList<AddressManager>();
		addressadapter=new AddressManagerAdapter(dataadress, mActivity.getBaseContext());
		list=(ScrollViewWithListView) view.findViewById(R.id.list);
		btn_add=(Button) view.findViewById(R.id.btn_add);
		btn_add.setOnClickListener(this);

		getAddressData();
	}
	private void getAddressData() {
		if(!Tools.isConnect(mActivity)){
			CommonUtil.toastShort(mActivity, "网络异常");
			return;
		}
		MyApplication.getInstance().getClient().post(API.GET_ADRESS+id, new AsyncHttpResponseHandler() {
			private Dialog loadingDialog;

			@Override
			public void onStart() {	
				super.onStart();
				loadingDialog = DialogUtil.getLoadingDialg(mActivity);
				loadingDialog.show();
			}
			@Override
			public void onFinish() {
				super.onFinish();
				loadingDialog.dismiss();
			}
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				Mine_MyInfo_nofrag.isclickitem=false;
				String responseMsg = new String(responseBody)
				.toString();
				Log.e("print", responseMsg); 
				Gson gson = new Gson();						
				JSONObject jsonobject = null;
				String code = null;
				try {
					jsonobject = new JSONObject(responseMsg);
					code = jsonobject.getString("code");
					int a =jsonobject.getInt("code");
					if(a==Config.CODE){ 
						JSONArray result =jsonobject.getJSONArray("result");
						idd=new int[result.length()];
						for(int i=0;i<result.length();i++){
							type=result.getJSONObject(i).getInt("isDefault");
							Log.e("type", String.valueOf(type));
							idd[i]=result.getJSONObject(i).getInt("id");
							if(result.getJSONObject(i).getInt("isDefault")==1){
								dataadress.add(new AddressManager(i, 
										result.getJSONObject(i).getString("cityId"),
										result.getJSONObject(i).getString("city_parent_id"),
										result.getJSONObject(i).getString("receiver"),
										result.getJSONObject(i).getString("city"),
										result.getJSONObject(i).getString("address"),
										result.getJSONObject(i).getString("zipCode"),
										result.getJSONObject(i).getString("moblephone"),
										"默认"));
							}
							else{
								dataadress.add(new AddressManager(i, 
										result.getJSONObject(i).getString("cityId"),
										result.getJSONObject(i).getString("city_parent_id"),
										result.getJSONObject(i).getString("receiver"),
										result.getJSONObject(i).getString("city"),
										result.getJSONObject(i).getString("address"),
										result.getJSONObject(i).getString("zipCode"),
										result.getJSONObject(i).getString("moblephone"),
										"  "));
							}
						}
						list.setAdapter(addressadapter);
						addressadapter.notifyDataSetChanged();
					}
					else{
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(mActivity, String.valueOf(j), Toast.LENGTH_SHORT).show();
				}
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
			}
		});

	}
	/*
	 * 积分
	 */
	private void Mine_score() {


		tv_total=(TextView) view.findViewById(R.id.tv_total);
		btn_exchange=(Button) view.findViewById(R.id.btn_exchange);
		moreList=new ArrayList<Score>();
		datasco=new ArrayList<Score>();
		xxlistview=(XListView) view.findViewById(R.id.xlist);
		xxlistview.setPullLoadEnable(true);
		xxlistview.setXListViewListener(this);
		xxlistview.setDivider(null);
		scoreadapter=new ScoreAdapter(datasco, mActivity.getBaseContext());
		btn_exchange.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int total=0;
				for(int i=0;i<datasco.size();i++){
					total=total+Integer.parseInt(datasco.get(i).getGotscore());
				}
				Intent intent=new Intent(mActivity,Exchange.class);
				intent.putExtra("price", total);
				startActivity(intent);

			}
		});
		getData();
		getscore();
	}
	private void getData() {
		if(!Tools.isConnect(mActivity)){
			CommonUtil.toastShort(mActivity, "网络异常");
			return;
		}
		MyApplication.getInstance().getClient()
		.post(API.GET_SCORE_LIST+customerId+"/"+page+"/"+rows, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				if(isrefersh){
					page=1;
					isrefersh=false;
				}

				String responseMsg = new String(responseBody)
				.toString();
				String ss=responseMsg;
				Log.e("print", responseMsg);



				Gson gson = new Gson();

				JSONObject jsonobject = null;
				String code = null;
				try {
					jsonobject = new JSONObject(responseMsg);
					code = jsonobject.getString("code");
					int a =jsonobject.getInt("code");
					if(a==Config.CODE){  
						String res =jsonobject.getString("result");
						jsonobject = new JSONObject(res);
						totalscore=jsonobject.getInt("total");
						if(totalscore==0){
							myHandler.sendEmptyMessage(0);
							return;
						}
						Log.e("jsonobject", String.valueOf(jsonobject));
						JSONArray list=jsonobject.getJSONArray("list");
						if(list.length()==0){
							myHandler.sendEmptyMessage(0);
							isStop=true;
							/*	Toast.makeText(mActivity, ss, 
								Toast.LENGTH_SHORT).show();*/
							return;
						}
						if(list.length()==0&&isLoadMore){
							CommonUtil.toastShort(mActivity, "没有更多数据");
							isLoadMore=false;
						}
						for(int i=0;i<list.length();i++){
							if(list.getJSONObject(i).getInt("types")==1){
								datasco.add(new Score(i, 
										list.getJSONObject(i).getString("order_number"),
										list.getJSONObject(i).getString("payedAt"),
										list.getJSONObject(i).getString("quantity"), 
										list.getJSONObject(i).getString("target_type"), 
										"收入"));
							}
							else{
								datasco.add(new Score(i, 
										list.getJSONObject(i).getString("order_number"),
										list.getJSONObject(i).getString("payedAt"),
										list.getJSONObject(i).getString("quantity"), 
										list.getJSONObject(i).getString("target_type"), 
										"支出"));
							}
						}

						myHandler.sendEmptyMessage(0);



					}else{
						code = jsonobject.getString("message");
						Toast.makeText(mActivity, code, 1000).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				System.out.println("-onFailure---");
			}
		});
	}
	private void getscore() {
		if(!Tools.isConnect(mActivity)){
			CommonUtil.toastShort(mActivity, "网络异常");
			return;
		}
		//RequestParams params = new RequestParams();
		Map<String, Object> params = new HashMap<String, Object>();
		Gson gson = new Gson();
		params.put("customer_id", customerId);
		//params.setUseJsonStreamer(true);
		JSONObject jsonParams = new JSONObject(params);
		HttpEntity entity;
		try {
			entity = new StringEntity(jsonParams.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return;
		}
		System.out.println("---"+params.toString());
		MyApplication.getInstance().getClient()
		.post(getActivity(),API.GET_SCORE, null,entity,"application/json", new AsyncHttpResponseHandler(){
		//.post(API.GET_SCORE, params,new AsyncHttpResponseHandler() {
			private Dialog loadingDialog;

			@Override
			public void onStart() {	
				super.onStart();
				loadingDialog = DialogUtil.getLoadingDialg(mActivity);
				loadingDialog.show();
			}
			@Override
			public void onFinish() {
				super.onFinish();
				loadingDialog.dismiss();
			}
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				String responseMsg = new String(responseBody)
				.toString();
				Gson gson = new Gson();

				JSONObject jsonobject = null;
				String code = null;
				try {
					jsonobject = new JSONObject(responseMsg);
					code = jsonobject.getString("code");
					int a =jsonobject.getInt("code");
					if(a==Config.CODE){  
						String res =jsonobject.getString("result");
						jsonobject = new JSONObject(res);
						price1=jsonobject.getInt("quantityTotal");
						sxfmoney=jsonobject.getInt("dh_total");

						if (StringUtil.isNull(jsonobject.getInt("quantityTotal")+"")) 
							price1= 0;
						else 
							price1= jsonobject.getInt("quantityTotal");

						tv_total.setText("总积分:"+price1);
					}else{
						code = jsonobject.getString("message");
						Toast.makeText(mActivity, code, 1000).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				System.out.println("-onFailure---");
				Log.e("print", "-onFailure---" + error);
			}
		});

	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		//case REQUEST_CITY:
		//case com.example.zf_pad.fragment.Constants.ApplyIntent.REQUEST_CHOOSE_CITY:
		case com.example.zf_pad.fragment.Constants.ApplyIntent.REQUEST_CHOOSE_CITY:
			if(CityProvinceActivity.isClickconfirm){
				City mMerchantCity = (City) data.getSerializableExtra(com.example.zf_pad.fragment.Constants.CityIntent.SELECTED_CITY);
				cityId=mMerchantCity.getId() ;
				tv_city_select.setText(mMerchantCity.getName());
				Log.e("1", tv_city_select.getText().toString());
				CityProvinceActivity.isClickconfirm=false;
				/*cityId = data.getIntExtra(CITY_ID, 0);
			cityName = data.getStringExtra(CITY_NAME);
			tv_city_select.setText(cityName);*/
			}
			break;
		case 2:
			if(data!=null){
				String  a =data.getStringExtra("text");
				et_phone.setText(a);
				et_phone.setVisibility(View.VISIBLE);
				changephone.setText("修改");
			}
			break;
		case 3:
			if(data!=null){
				String  a =data.getStringExtra("text");
				et_email.setText(a);
				et_email.setVisibility(View.VISIBLE);
				changeemail.setText("修改");
			}
			break;
		default:
			break;
		}
	}
	@Override
	public void onRefresh() {
		if(!Tools.isConnect(mActivity)){
			CommonUtil.toastShort(mActivity, "网络异常");
			return;
		}
		isrefersh=true;
		page=1;
		datasco.clear();
		getData();
	}
	@Override
	public void onLoadMore() {
		if(!Tools.isConnect(mActivity)){
			CommonUtil.toastShort(mActivity, "网络异常");
			return;
		}
		if(isStop){
			CommonUtil.toastShort(mActivity, "无更多数据");
			onLoad();
			return;
		}
		isLoadMore=true;
		page+=1;
		getData();
	}
	protected void onLoad() {
		xxlistview.stopRefresh();
		xxlistview.stopLoadMore();
		xxlistview.setRefreshTime(Tools.getHourAndMin());

	}
}
