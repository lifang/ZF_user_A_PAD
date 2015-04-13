package com.example.zf_pad.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zf_pad.BaseActivity;
import com.example.zf_pad.Config;
import com.example.zf_pad.MyApplication;
import com.example.zf_pad.R;
import com.example.zf_pad.fragment.Mine_MyMerChant;
import com.example.zf_pad.trade.CityProvinceActivity;
import com.example.zf_pad.trade.common.CommonUtil;
import com.example.zf_pad.trade.common.DialogUtil;
import com.example.zf_pad.trade.entity.City;
import com.example.zf_pad.trade.entity.Province;
import com.example.zf_pad.util.ImageCacheUtil;
import com.example.zf_pad.util.TitleMenuUtil;
import com.example.zf_pad.util.Tools;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class EditMerchant extends BaseActivity implements OnClickListener{
	private TextView tv_shopname,tv_name,tv_id_number,tv_license_code,tv_tax_id_number,
	tv_certificate_no,tv_bank,tv_licencenum_bank,tv_adress;
	private int id;
	private List<City> mCities = new ArrayList<City>();
	private String[] imgPath=new String[7];
	private Button btn_legal_photo,btn_license_photos,btn_legal_back_photos,
	btn_tax_regist,btn_person_photograph,btn_organization_code_photos,btn_bank_license_photos,
	btn_edit;
	private int tag;
	public static boolean isEdit=false;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.editmerchant);
	new TitleMenuUtil(EditMerchant.this, "商户详情").show();
	init();
}
@Override
protected void onStart() {
	// TODO Auto-generated method stub
	super.onStart();
	getData();
}
private void getData() {
	if(!Tools.isConnect(getApplicationContext())){
		CommonUtil.toastShort(getApplicationContext(), "网络异常");
		return;
	}
	Intent intent=getIntent();
	id=intent.getIntExtra("position", 0);
	MyApplication.getInstance().getClient().post(Config.MERCHANTINFO+id, new AsyncHttpResponseHandler() {
		private Dialog loadingDialog;

		@Override
		public void onStart() {	
			super.onStart();
			loadingDialog = DialogUtil.getLoadingDialg(EditMerchant.this);
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
			Log.e("responseMsg", responseMsg);
			Gson gson = new Gson();
			
			JSONObject jsonobject = null;
			String code = null;
			try {
				jsonobject = new JSONObject(responseMsg);
				code = jsonobject.getString("code");
				int a =jsonobject.getInt("code");
				if(a==Config.CODE){
					JSONObject result=jsonobject.getJSONObject("result");
					/*if(result.length()!=17){
						CommonUtil.toastShort(getApplicationContext(), "服务器返回数据不完全");
						return;
					}
					title=result.getString("title");
					legal_person_name=result.getString("legal_person_name");
					legal_person_card_id=result.getString("legal_person_card_id");
					business_license_no=result.getString("business_license_no");
					tax_registered_no=result.getString("tax_registered_no");
					organization_code_no=result.getString("organization_code_no");
					account_bank_name=result.getString("account_bank_name");
					bank_open_account=result.getString("bank_open_account");*/
					
					tv_shopname.setText(result.getString("title"));
					tv_name.setText(result.getString("legal_person_name"));
					tv_id_number.setText(result.getString("legal_person_card_id"));
					tv_license_code.setText(result.getString("business_license_no"));
					tv_tax_id_number.setText(result.getString("tax_registered_no"));
					tv_certificate_no.setText(result.getString("organization_code_no"));
					tv_bank.setText(result.getString("account_bank_name"));
					tv_licencenum_bank.setText(result.getString("bank_open_account"));
					tv_adress.setText(findcity(result.getInt("id")));
					imgPath[2]=result.getString("body_photo_path");
					imgPath[3]=result.getString("license_no_pic_path");
					imgPath[4] =result.getString("tax_no_pic_path");
					imgPath[5]=result.getString("org_code_no_pic_path");
					imgPath[6]=result.getString("account_pic_path");
					imgPath[1] =result.getString("card_id_back_photo_path");
					imgPath[0]=result.getString("card_id_front_photo_path");
					
					}
				else{
				    code = jsonobject.getString("message");
				    Toast.makeText(getApplicationContext(), code, 1000).show();
					
				}
				}
			catch (JSONException e) {
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
	
}
protected String findcity(int id) {
	// TODO Auto-generated method stub
	String a="苏州";
    List<Province> provinces = CommonUtil.readProvincesAndCities(EditMerchant.this);
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
private void init() {
	btn_edit=(Button) findViewById(R.id.btn_edit);
	btn_legal_photo=(Button) findViewById(R.id.btn_legal_photo);
	btn_license_photos=(Button) findViewById(R.id.btn_license_photos);
	btn_legal_back_photos=(Button) findViewById(R.id.btn_legal_back_photos);
	btn_tax_regist=(Button) findViewById(R.id.btn_tax_regist);
	btn_person_photograph=(Button) findViewById(R.id.btn_person_photograph);
	btn_organization_code_photos=(Button) findViewById(R.id.btn_organization_code_photos);
	btn_bank_license_photos=(Button) findViewById(R.id.btn_bank_license_photos);
	tv_shopname=(TextView) findViewById(R.id.tv_shopname);
	tv_name=(TextView) findViewById(R.id.tv_name);
	tv_id_number=(TextView) findViewById(R.id.tv_id_number);
	tv_license_code=(TextView) findViewById(R.id.tv_license_code);
	tv_tax_id_number=(TextView) findViewById(R.id.tv_tax_id_number);
	tv_certificate_no=(TextView) findViewById(R.id.tv_certificate_no);
	tv_bank=(TextView) findViewById(R.id.tv_bank);
	tv_licencenum_bank=(TextView) findViewById(R.id.tv_licencenum_bank);
	tv_adress=(TextView) findViewById(R.id.tv_adress);
	btn_legal_photo.setOnClickListener(this);
	btn_license_photos.setOnClickListener(this);
	btn_legal_back_photos.setOnClickListener(this);
	btn_tax_regist.setOnClickListener(this);
	btn_person_photograph.setOnClickListener(this);
	btn_organization_code_photos.setOnClickListener(this);
	btn_bank_license_photos.setOnClickListener(this);
	btn_edit.setOnClickListener(this);
}
@Override
public void onClick(View v) {
	switch (v.getId()) {
	case R.id.btn_edit:
		isEdit=true;
		Intent intent=new Intent(getApplicationContext(),CreatMerchant.class);
		intent.putExtra("title", tv_shopname.getText().toString());
		intent.putExtra("legal_person_name", tv_name.getText().toString());
		intent.putExtra("legal_person_card_id", tv_id_number.getText().toString());
		intent.putExtra("business_license_no", tv_license_code.getText().toString());
		intent.putExtra("tax_registered_no", tv_tax_id_number.getText().toString());
		intent.putExtra("organization_code_no", tv_certificate_no.getText().toString());
		intent.putExtra("account_bank_name", tv_bank.getText().toString());
		intent.putExtra("bank_open_account", tv_licencenum_bank.getText().toString());
		intent.putExtra("city", tv_adress.getText().toString());
		intent.putExtra("card_id_front_photo_path", imgPath[0]);
		intent.putExtra("card_id_back_photo_path", imgPath[1]);
		intent.putExtra("body_photo_path", imgPath[2]);
		intent.putExtra("license_no_pic_path", imgPath[3]);
		intent.putExtra("tax_no_pic_path", imgPath[4]);
		intent.putExtra("org_code_no_pic_path", imgPath[5]);
		intent.putExtra("account_pic_path", imgPath[6]);
		startActivity(intent);
		break;
	case R.id.btn_legal_photo:
		tag=1;
		openimg(tag);
		break;
	case R.id.btn_license_photos:
		tag=2;
		openimg(tag);
		break;
	case R.id.btn_legal_back_photos:
		tag=3;
		openimg(tag);
		break;
	case R.id.btn_tax_regist:
		tag=4;
		openimg(tag);
		break;
	case R.id.btn_person_photograph:
		tag=5;
		openimg(tag);
		break;
	case R.id.btn_organization_code_photos:
		tag=6;
		openimg(tag);
		break;
	case R.id.btn_bank_license_photos:
		tag=7;
		openimg(tag);
		break;
	
	default:
		break;
	}
	
}
protected void openimg(int tag) {
	AlertDialog.Builder build = new AlertDialog.Builder(EditMerchant.this);
	LayoutInflater factory = LayoutInflater.from(this);
	final View textEntryView = factory.inflate(R.layout.img, null);
	 //build.setTitle("自定义输入框");
     build.setView(textEntryView);
     final ImageView view=(ImageView) textEntryView.findViewById(R.id.imag);
     int ppp=tag-1;
     ImageCacheUtil.IMAGE_CACHE.get(imgPath[ppp], view);
     build.create().show();
}
}
