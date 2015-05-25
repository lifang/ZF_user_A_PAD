package com.example.zf_pad.activity;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.example.zf_pad.BaseActivity;
import com.example.zf_pad.MyApplication;
import com.epalmpay.userPad.R;
import com.example.zf_pad.entity.AddressManager;
import com.example.zf_pad.fragment.Mine_Address;
import com.example.zf_pad.trade.API;
import com.example.zf_pad.trade.CityProvinceActivity;
import com.example.zf_pad.trade.common.CommonUtil;
import com.example.zf_pad.trade.common.HttpCallback;
import com.example.zf_pad.trade.entity.City;
import com.example.zf_pad.util.StringUtil;
import com.example.zf_pad.util.TitleMenuUtil;
import com.example.zf_pad.util.Tools;
import com.google.gson.reflect.TypeToken;

public class AdressEdit extends BaseActivity{
//	cityId
//	receiver
//	moblephone
//	zipCode
//	address
//	isDefault
//	customerId
	private Button adresslist;
	private EditText tv1,tv2,tv3,tv5;
	//private int id=MyApplication.NewUser.getId();
	private int Cityid=MyApplication.NewUser.getCityId();
	private String name,tel,stringcode ,address;
	private int isDefault=1;
	private TextView tv4;
	private CheckBox item_cb;
	private LinearLayout mi_r4;
	private int id=MyApplication.NewUser.getId();
	private int pp;
	private ImageView search;
	private int[] ids=new int[1];
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adress_edit);
		initView();
		if(Mine_Address.isclickitem){
			new TitleMenuUtil(AdressEdit.this, "修改地址").show();
		}
		else{
			new TitleMenuUtil(AdressEdit.this, "新增地址").show();
		}
		
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Mine_Address.isclickitem=false;
	}
	private Boolean check() {
		// TODO Auto-generated method stub
		name=StringUtil.replaceBlank(tv1.getText().toString());
		if(name.length()==0){
			Toast.makeText(getApplicationContext(), "用户名不能为空",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		tel=StringUtil.replaceBlank(tv2.getText().toString());
		if(tel.length()==0){
			Toast.makeText(getApplicationContext(), "手机号不能为空",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (!StringUtil.isMobile(tel)) {
			Toast.makeText(getApplicationContext(), "请输入正确的手机号", 
					Toast.LENGTH_SHORT).show();
			return false;
		}
		stringcode=StringUtil.replaceBlank(tv3.getText().toString());
		if(stringcode.length()==0){
			Toast.makeText(getApplicationContext(), "邮编不能为空",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (!StringUtil.isZipNO(stringcode)) {
			Toast.makeText(getApplicationContext(), "请输入正确的邮编",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		address=StringUtil.replaceBlank(tv5.getText().toString());
		if(address.length()==0){
			Toast.makeText(getApplicationContext(), "请输入详细地址",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	private void getData() {
		if(!Tools.isConnect(getApplicationContext())){
			CommonUtil.toastShort(getApplicationContext(), "网络异常");
			return;
		}
        API.AddAdres(AdressEdit.this, Cityid+"" ,name,tel,stringcode , address ,isDefault,id,
        		
                new HttpCallback(AdressEdit.this) {
           

					@Override
					public void onSuccess(Object data) {
						Log.e("data", String.valueOf(data));
						//Toast.makeText(AdressEdit.this, "添加地址成功", 1000).show(); 

						finish();
					}

					@Override
					public TypeToken getTypeToken() {
						return null;
					}
                });
	}
	private void initView() {
		// TODO Auto-generated method stub
		search=(ImageView) findViewById(R.id.search);
		item_cb=(CheckBox) findViewById(R.id.item_cb);
		tv1=(EditText) findViewById(R.id.tv1);
		tv2=(EditText) findViewById(R.id.tv2);
		tv3=(EditText) findViewById(R.id.tv3);
		tv5=(EditText) findViewById(R.id.tv5);
		tv4=(TextView) findViewById(R.id.tv4);
		
		//tv4.setText(MyApplication.getCITYNAME());
		if(Mine_Address.isclickitem){
			search.setVisibility(View.VISIBLE);
			Bundle bundle=this.getIntent().getExtras();
			pp=bundle.getInt("position");
			tv1.setText(Mine_Address.dataadress.get(pp).getConsignee());
			tv2.setText(Mine_Address.dataadress.get(pp).getPhone());
			tv3.setText(Mine_Address.dataadress.get(pp).getZipcode());
			tv4.setText(Mine_Address.dataadress.get(pp).getArea());
			tv5.setText(Mine_Address.dataadress.get(pp).getDetailadress());
			if(Mine_Address.dataadress.get(pp).getIsdefau().equals("默认")){
				//item_cb.setBackgroundResource(R.drawable.cb_y);
				item_cb.setChecked(true);
			}
			else{
				//item_cb.setBackgroundResource(R.drawable.cb_n1);
				item_cb.setChecked(false);
			}
			
		}
		adresslist=(Button) findViewById(R.id.adresslist);
		adresslist.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(check()){
					if(Mine_Address.isclickitem){
						changeData();
					}
					else{
						getData();
					}
					
				}
			}
		});
		search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				ids[0]=Mine_Address.idd[pp];
				API.delectaddress(AdressEdit.this, ids, new HttpCallback(AdressEdit.this) {

					@Override
					public void onSuccess(Object data) {
					//CommonUtil.toastShort(AdressEdit.this, "删除地址成功");
					finish();
					}

					@Override
					public TypeToken getTypeToken() {
						// TODO Auto-generated method stub
						return null;
					}
				});
				
			}
		});
		item_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
					isDefault=1;
				}else{
					isDefault=2;
				}
			}
		});
		mi_r4=(LinearLayout) findViewById(R.id.mi_r4);
		mi_r4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AdressEdit.this, CityProvinceActivity.class);
				 
				startActivityForResult(intent, 
						com.example.zf_pad.fragment.Constants.ApplyIntent.REQUEST_CHOOSE_CITY);
			}
		});
	}
	protected void changeData() {

		if(!Tools.isConnect(getApplicationContext())){
			CommonUtil.toastShort(getApplicationContext(), "网络异常");
			return;
		}
		API.changeAdres(AdressEdit.this, Mine_Address.idd[pp], Cityid+"", name, tel, 
				stringcode, address, isDefault, new HttpCallback(AdressEdit.this) {

					@Override
					public void onSuccess(Object data) {
						//Toast.makeText(AdressEdit.this, "修改地址成功", Toast.LENGTH_SHORT).show();
						//mine_Address.dataadress
						Log.e("da", String.valueOf(Mine_Address.dataadress));
						finish();
					}

					@Override
					public TypeToken getTypeToken() {
						// TODO Auto-generated method stub
						return null;
					}
				});
		
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		System.out.println("resultCode"+resultCode+requestCode);
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==com.example.zf_pad.fragment.Constants.ApplyIntent.REQUEST_CHOOSE_CITY){
			if(CityProvinceActivity.isClickconfirm){
			System.out.println("REQUEST_CHOOSE_CITY"+resultCode+requestCode);
			City mMerchantCity = (City) data.getSerializableExtra(com.example.zf_pad.fragment.Constants.CityIntent.SELECTED_CITY);
			tv4.setText(mMerchantCity.getName());
			Cityid=mMerchantCity.getId() ;	 
			CityProvinceActivity.isClickconfirm=false;
		}
		}
	}
}

