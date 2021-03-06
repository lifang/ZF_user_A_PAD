package com.example.zf_pad.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.epalmpay.userPad.R;
import com.example.zf_pad.Config;
import com.example.zf_pad.MyApplication;
import com.example.zf_pad.activity.ChangeEmail;
import com.example.zf_pad.activity.ChangePhone;
import com.example.zf_pad.activity.LoginActivity;
import com.example.zf_pad.activity.MainActivity;
import com.example.zf_pad.trade.API;
import com.example.zf_pad.trade.CityProvinceActivity;
import com.example.zf_pad.trade.common.CommonUtil;
import com.example.zf_pad.trade.common.DialogUtil;
import com.example.zf_pad.trade.common.HttpCallback;
import com.example.zf_pad.trade.entity.City;
import com.example.zf_pad.trade.entity.Province;
import com.example.zf_pad.util.StringUtil;
import com.example.zf_pad.util.Tools;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class Mine_baseinfo extends Fragment implements OnClickListener{
	private View view;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (view != null) {
			Log.i("222222", "11111111");
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null)
				parent.removeView(view);
		}
		try {
			view = inflater.inflate(R.layout.baseinfo, container, false);
			id=MyApplication.NewUser.getId();
			init();
			getUserInfo();
		} catch (InflateException e) {

		}

		return view;
	}
	@Override
	public void onResume() {
		super.onResume();

		if (!StringUtil.isNull(Config.changePhoneNum)) {
			et_phone.setText(Config.changePhoneNum);
			Config.changePhoneNum = "";
		}
		if (!StringUtil.isNull(Config.changeemail)) {
			et_email.setText(Config.changeemail);
			Config.changeemail = "";
		}
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		sLV.setVisibility(View.VISIBLE);
		myHandler=new Handler(){
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case 3:
					try {
						Log.e("res", String.valueOf(result));
						pawwword=result.getString("password");


						et_phone.setText(result.getString("phone"));
						tv_city_select.setText(findcity(result.getInt("city_id")));
						Log.e("2", tv_city_select.getText().toString());
						et_name.setText(result.getString("name"));
						et_email.setText(result.getString("email"));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Toast.makeText(mActivity, "注册信息不完全", Toast.LENGTH_SHORT).show();
					}

					break;
					/*case 1024:
				sLV.setVisibility(View.GONE);
				Mine_MyInfo.isHiddenn=true;
				break;
			case 1025:
				sLV.setVisibility(View.VISIBLE);
				Mine_MyInfo.isHiddenn=false;*/
				default:
					break;
				}
			};
		};
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
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

						myHandler.sendEmptyMessage(3);
					}
					else{
						code = jsonobject.getString("message");
						Toast.makeText(mActivity, code, 1000).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub

			}
		});
		/*API.getUserinfo(mActivity,  new HttpCallback(mActivity) {

		@Override
		public void onSuccess(Object data) {
			//Toast.makeText(mActivity, "修改密码成功", 1000).show();
			JsonObject result=new JsonObject();
		}

		@Override
		public TypeToken getTypeToken() {
			// TODO Auto-generated method stub
			return null;
		}
	});*/

	}
	private void init() {
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
		case 2:
			if(data!=null){
				String  a =data.getStringExtra("text");
				et_phone.setText(a);
			}
			break;
		case 3:

			if(data!=null){
				String  a =data.getStringExtra("text");
				et_email.setText(a);
			}
			break;
		default:
			break;
		}
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
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_city_select:
			Intent intent = new Intent(mActivity,
					CityProvinceActivity.class);
			//intent.putExtra(CITY_NAME, cityName);
			//startActivityForResult(intent, REQUEST_CITY);
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
			/*
			 * 清除本fragment
			 */
			FragmentTransaction transaction = getActivity()
					.getSupportFragmentManager().beginTransaction();
			if (this != null)
				transaction.remove(this);
			transaction.commit();

			startActivity(new Intent(getActivity(),MainActivity.class));

			Editor editor=mySharedPreferences.edit();
			editor.putBoolean("islogin", false);
			editor.putString("name", null);
			editor.putInt("id", -1);
			editor.commit();
			Intent i = new Intent(mActivity, LoginActivity.class);
			startActivity(i);
			//mActivity.finish();
			break;
		default:
			break;
		}

	}
}
