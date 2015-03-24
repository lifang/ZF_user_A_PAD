package com.example.zf_pad.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zf_pad.BaseActivity;
import com.example.zf_pad.Config;
import com.example.zf_pad.MyApplication;
import com.example.zf_pad.R;
import com.example.zf_pad.entity.AdressEntity;
import com.example.zf_pad.util.TitleMenuUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class GoodConfirm extends BaseActivity implements OnClickListener{
	List<AdressEntity>  myList = new ArrayList<AdressEntity>();
	List<AdressEntity>  moreList = new ArrayList<AdressEntity>();
	private String Url=Config.ChooseAdress;
	private TextView tv_sjr,tv_tel,tv_adress;
	private LinearLayout ll_choose;
	private TextView tv_pop,tv_totle,title2,retail_price,showCountText,tv_pay,tv_count;
	private Button btn_pay;
	private String comment;
	private ImageView reduce,add;
	PopupWindow menuWindow;
	private int pirce;
	private int goodId,paychannelId,quantity,addressId,is_need_invoice=0;
	private EditText buyCountEdit,comment_et,et_titel;
	private CheckBox item_cb;
	private int invoice_type=0;//发票类型�?0公司  1个人�?
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_comfirm);
		new TitleMenuUtil(GoodConfirm.this, "����ȷ��").show();
		//			i2.putExtra("getTitle", gfe.getGood_brand());
//		i2.putExtra("price", gfe.getPrice());
//		i2.putExtra("model", gfe.getModel_number());
//		i2.putExtra("chanel", chanel);
//		i2.putExtra("chanelID", paychannelId);
//		i2.putExtra("id", gfe.getId());
//		startActivity(i2);
		initView();
		title2.setText(getIntent().getStringExtra("getTitle"));
		pirce=getIntent().getIntExtra("price", 0);
		retail_price.setText("�?"+ pirce);
		goodId=getIntent().getIntExtra("goodId", 1);
		paychannelId=getIntent().getIntExtra("paychannelId", 1);
		tv_pay.setText("实付：￥ "+pirce); 
		tv_totle.setText("实付：￥ "+pirce); 
		getData();
		getData1();
	}

	private void initView() {
		// TODO Auto-generated method stub
		add=(ImageView) findViewById(R.id.add);
		reduce=(ImageView) findViewById(R.id.reduce);
		reduce.setOnClickListener(this);
		add.setOnClickListener(this);

		tv_totle=(TextView) findViewById(R.id.tv_totle);
		showCountText=(TextView) findViewById(R.id.showCountText);
		
		tv_count=(TextView) findViewById(R.id.tv_count);
		tv_tel=(TextView) findViewById(R.id.tv_tel);
		tv_adress=(TextView) findViewById(R.id.tv_adress);
		title2=(TextView) findViewById(R.id.title2);
		retail_price=(TextView) findViewById(R.id.retail_price);
		btn_pay=(Button) findViewById(R.id.btn_pay);
		btn_pay.setOnClickListener(this);
		tv_pay=(TextView) findViewById(R.id.tv_pay);
		et_titel=(EditText) findViewById(R.id.et_titel);
		buyCountEdit=(EditText) findViewById(R.id.buyCountEdit);
		//comment_et=(EditText) findViewById(R.id.comment_et);
		item_cb=(CheckBox) findViewById(R.id.item_cb);
		item_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
					is_need_invoice=1;
				}else{
					is_need_invoice=0;
				}
			}
		});
		buyCountEdit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				showCountText.setText(arg0.toString());
				tv_count.setText("共计:   "+arg0+"�?");
				 if( buyCountEdit.getText().toString().equals("")){
					 quantity=0;
				 }else{
					 quantity= Integer.parseInt( buyCountEdit.getText().toString() );
				 }
			 
				 tv_totle.setText("实付：￥ "+pirce*quantity); 
				tv_pay.setText("实付：￥ "+pirce*quantity); 
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private void getData() {
		// TODO Auto-generated method stub
	 
		 
			RequestParams params = new RequestParams();
			params.put("customerId", 80);
			 
			params.setUseJsonStreamer(true);
			System.out.println("---getData-");
			Url=Url+"80";
			MyApplication.getInstance().getClient()
					.get(Url, new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int statusCode, Header[] headers,
								byte[] responseBody) {
							String responseMsg = new String(responseBody)
									.toString();
							Log.e("print", responseMsg);
							System.out.println("----"+responseMsg);
						 
							 
							Gson gson = new Gson();
							
							JSONObject jsonobject = null;
							String code = null;
							try {
								jsonobject = new JSONObject(responseMsg);
								code = jsonobject.getString("code");
								int a =jsonobject.getInt("code");
								if(a==Config.CODE){  
	 								String res =jsonobject.getString("result");
//									jsonobject = new JSONObject(res);
					/*				
	 								moreList= gson.fromJson(res, new TypeToken<List<AdressEntity>>() {
	 			 					}.getType());
				 				 
	 								myList.addAll(moreList);
	 				 			 for(int i=0;i<myList.size();i++){
	 				 				 if(myList.get(i).getIsDefault()==1){
	 				 					tv_sjr.setText("收件人： "+myList.get(i).getReceiver());
	 				 					tv_tel.setText(myList.get(i).getMoblephone());
	 				 					tv_adress.setText("地址�?"+myList.get(i).getAddress());
	 				 					addressId=myList.get(i).getId();
	 				 				 }
	 				 			 }
				 					*/  
				 				 
				 			 
								}else{
									code = jsonobject.getString("message");
									Toast.makeText(getApplicationContext(), code, 1000).show();
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								 ;	
								e.printStackTrace();
								
							}

						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								byte[] responseBody, Throwable error) {
							// TODO Auto-generated method stub
							System.out.println("-onFailure---");
							Log.e("print", "-onFailure---" + error);
						}
					});
	 
			 
	 
	}
	private void getData1() { 

		// TODO Auto-generated method stub


 
		MyApplication.getInstance().getClient()
				.post(Config.ChooseAdress+"80", new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						String responseMsg = new String(responseBody)
								.toString();
						Log.e("print", responseMsg);
						System.out.println("----"+responseMsg);
					 
						 
						Gson gson = new Gson();
						
						JSONObject jsonobject = null;
						String code = null;
						try {
							jsonobject = new JSONObject(responseMsg);
							code = jsonobject.getString("code");
							int a =jsonobject.getInt("code");
							if(a==Config.CODE){  
 								String res =jsonobject.getString("result");

								
 								moreList= gson.fromJson(res, new TypeToken<List<AdressEntity>>() {
 			 					}.getType());
			 				 
 								for(int i =0;i<moreList.size();i++){
 									if(moreList.get(i).getIsDefault()==1) {
 										//tv_name,tv_tel,tv_adresss;
 										addressId=moreList.get(i).getId();
 										tv_adress.setText("收件地址 �? "+moreList.get(i).getAddress());
 										tv_sjr.setText("收件�? �? "+moreList.get(i).getReceiver());
 										tv_tel.setText( moreList.get(i).getMoblephone());
 									}
 								}
 								
 								
 								
 								
// 							myList.addAll(moreList);
// 				 				handler.sendEmptyMessage(0);
			 					  
			 				 
			 			 
							}else{
								code = jsonobject.getString("message");
								Toast.makeText(getApplicationContext(), code, 1000).show();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							 ;	
							e.printStackTrace();
							
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						// TODO Auto-generated method stub
						System.out.println("-onFailure---");
						Log.e("print", "-onFailure---" + error);
					}
				});
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {

		case R.id.btn_pay:
		   confirmGood();
			Intent i1 =new Intent (GoodConfirm.this,PayFromCar.class);
			startActivity(i1);
			break;
		case R.id.add:
			quantity= Integer.parseInt( buyCountEdit.getText().toString() )+1;
			buyCountEdit.setText(quantity+"");
				break;
		case R.id.reduce:
			if(quantity==0){
				break;
			}
			quantity= Integer.parseInt( buyCountEdit.getText().toString() )-1;
			buyCountEdit.setText(quantity+"");
				break;
		default:
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==11){
			if(data!=null){

				addressId=data.getIntExtra("id", addressId);
				tv_adress.setText("收件地址 �? "+data.getStringExtra("adree"));
				tv_sjr.setText("收件�? �? "+data.getStringExtra("name"));
				tv_tel.setText( data.getStringExtra("tel"));
			}
		}
	}
	private void confirmGood() {
		// TODO Auto-generated method stub
		 
		 //quantity addressId comment is_need_invoice et_titel  
		quantity= Integer.parseInt( buyCountEdit.getText().toString() );
		comment=comment_et.getText().toString();
		RequestParams params = new RequestParams();
		params.put("customerId", 80);
		params.put("goodId", goodId);
		params.put("paychannelId", paychannelId);
		params.put("addressId", addressId);
		params.put("quantity", quantity);
		params.put("comment", comment);
		params.put("is_need_invoice", is_need_invoice);
		params.put("invoice_type", invoice_type);
		params.put("invoice_info", et_titel.getText().toString());
		params.setUseJsonStreamer(true);
		 
		String Urla=Config.SHOPORDER;
		MyApplication.getInstance().getClient()
				.get(Urla, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						String responseMsg = new String(responseBody)
								.toString();
						Log.e("print", responseMsg);
						System.out.println("----"+responseMsg);
					 
						 
						Gson gson = new Gson();
						
						JSONObject jsonobject = null;
						String code = null;
						try {
							jsonobject = new JSONObject(responseMsg);
							code = jsonobject.getString("code");
							int a =jsonobject.getInt("code");
							if(a==Config.CODE){  
 							 
			 				 
			 			 
							}else{
								code = jsonobject.getString("message");
								Toast.makeText(getApplicationContext(), code, 1000).show();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							 ;	
							e.printStackTrace();
							
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						// TODO Auto-generated method stub
						System.out.println("-onFailure---");
						Log.e("print", "-onFailure---" + error);
					}
				});
 
	}
}
