package com.example.zf_pad.activity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zf_pad.BaseActivity;
import com.example.zf_pad.Config;
import com.example.zf_pad.MyApplication;
import com.epalmpay.userPad.R;
import com.example.zf_pad.aadpter.ChooseAdressAdapter;
import com.example.zf_pad.entity.AdressEntity;
import com.example.zf_pad.trade.API;
import com.example.zf_pad.trade.common.HttpCallback;
import com.example.zf_pad.util.ImageCacheUtil;
import com.example.zf_pad.util.ScrollViewWithListView;
import com.example.zf_pad.util.TitleMenuUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class GoodConfirm extends BaseActivity implements OnClickListener {
	List<AdressEntity> myList = new ArrayList<AdressEntity>();
	List<AdressEntity> moreList = new ArrayList<AdressEntity>();
	private String Url = Config.ChooseAdress;
	private TextView tv_sjr, tv_tel, tv_adress;
	private LinearLayout ll_choose;
	private TextView tv_pop, tv_totle, title2, retail_price, showCountText,
			tv_pay, tv_count;
	private Button btn_pay;
	private ImageView reduce, add;
	PopupWindow menuWindow;
	private int pirce;
	private int goodId, paychannelId, quantity, addressId=-1, is_need_invoice = 0;
	private EditText buyCountEdit, comment_et, et_titel;
	private CheckBox item_cb;
	private int invoice_type = 0;
	private String comment, invoice_info;
	private ScrollViewWithListView sclist;
	private ChooseAdressAdapter myAdapter;
	private EditText et_comment;
	private Button bt_add;
	private ImageView event_img;
	private TextView tv_brand;
	private TextView tv_chanel;
	private LinearLayout ll_fp;
	private DecimalFormat df;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.good_comfirm);
		new TitleMenuUtil(GoodConfirm.this, "订单确认").show();
		DecimalFormat df = (DecimalFormat)NumberFormat.getInstance();
		df.applyPattern("0.00");
		
		initView();
		title2.setText(getIntent().getStringExtra("getTitle"));
		pirce = getIntent().getIntExtra("price", 0);
		retail_price.setText("￥" +df.format( pirce*1.0f/100));
		goodId = getIntent().getIntExtra("goodId", 1);
		paychannelId = getIntent().getIntExtra("paychannelId", 1);
		tv_pay.setText("实付：￥ " + ((double)pirce)/100);
		tv_totle.setText("实付：￥ " + ((double)pirce)/100);
		tv_chanel.setText(getIntent().getStringExtra("chanel"));
		System.out.println("=paychannelId==" + paychannelId);
		//getData1();
		String img_url=getIntent().getStringExtra("piclist");
		ImageCacheUtil.IMAGE_CACHE.get(img_url,
 				event_img);
		tv_brand.setText(getIntent().getStringExtra("brand"));
	}

	private void initView() {
		ll_fp = (LinearLayout)findViewById(R.id.ll_fp);
		tv_chanel = (TextView)findViewById(R.id.wayName);
		tv_brand = (TextView)findViewById(R.id.content2);
		event_img = (ImageView)findViewById(R.id.evevt_img);
		bt_add = (Button)findViewById(R.id.bt_add);
		bt_add.setOnClickListener(this);
		sclist = (ScrollViewWithListView)findViewById(R.id.pos_lv1);
		myAdapter = new ChooseAdressAdapter(this, myList);
		sclist.setAdapter(myAdapter);
		sclist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				addressId=myList.get(position).getId();
				myAdapter.chang();
				myList.get(position).setIscheck(true);
				myAdapter.notifyDataSetChanged();
			}
		});
		add = (ImageView) findViewById(R.id.add);
		reduce = (ImageView) findViewById(R.id.reduce);
		reduce.setOnClickListener(this);
		add.setOnClickListener(this);

		tv_totle = (TextView) findViewById(R.id.tv_totle);
		//showCountText = (TextView) findViewById(R.id.showCountText);

		tv_count = (TextView) findViewById(R.id.tv_count);
		tv_tel = (TextView) findViewById(R.id.tv_tel);
		tv_adress = (TextView) findViewById(R.id.tv_adress);
		title2 = (TextView) findViewById(R.id.title2);
		retail_price = (TextView) findViewById(R.id.retail_price);
		btn_pay = (Button) findViewById(R.id.btn_pay);
		btn_pay.setOnClickListener(this);
		tv_pay = (TextView) findViewById(R.id.tv_pay);
		et_titel = (EditText) findViewById(R.id.et_titel);
		buyCountEdit = (EditText) findViewById(R.id.buyCountEdit);
		// comment_et=(EditText) findViewById(R.id.comment_et);
		item_cb = (CheckBox) findViewById(R.id.item_cb);
		item_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					is_need_invoice = 1;
					et_titel.setEnabled(true);
					ll_fp.setVisibility(View.VISIBLE);
				} else {
					is_need_invoice = 0;
					et_titel.setEnabled(false);
					ll_fp.setVisibility(View.GONE);
				}
			}
		});
		buyCountEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				//showCountText.setText(arg0.toString());
				if(buyCountEdit.getText().toString().equals("0"))
					buyCountEdit.setText("");
				tv_count.setText("共计:   " + arg0 + "件");
				if (buyCountEdit.getText().toString().equals("")) {
					quantity = 0;
				} else {
					quantity = Integer.parseInt(buyCountEdit.getText()
							.toString());
				}

				tv_totle.setText("实付：￥ " + ((double)pirce)/100 * quantity);
				tv_pay.setText("实付：￥ " + ((double)pirce)/100 * quantity);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
		Spinner sp = (Spinner) findViewById(R.id.spinner);
		final String arr[] = new String[] { "公司", "个人" };

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, arr);
		sp.setAdapter(arrayAdapter);


		// 注册事件
		sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Spinner spinner = (Spinner) parent;
				invoice_type=position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				Toast.makeText(getApplicationContext(), "没有改变的处理",
						Toast.LENGTH_LONG).show();
			}

		});
	}

	private void getData1() {

		MyApplication
				.getInstance()
				.getClient()
				.post(Config.ChooseAdress + MyApplication.NewUser.getId()+"",
						new AsyncHttpResponseHandler() {

							@Override
							public void onSuccess(int statusCode,
									Header[] headers, byte[] responseBody) {
								String responseMsg = new String(responseBody)
										.toString();
								Log.e("print", responseMsg);
								System.out.println("----" + responseMsg);

								Gson gson = new Gson();

								JSONObject jsonobject = null;
								String code = null;
								try {
									jsonobject = new JSONObject(responseMsg);
									code = jsonobject.getString("code");
									int a = jsonobject.getInt("code");
									if (a == Config.CODE) {
										String res = jsonobject
												.getString("result");

										moreList = gson
												.fromJson(
														res,
														new TypeToken<List<AdressEntity>>() {
														}.getType());

										for(int i =0;i<moreList.size();i++){
		 									if(moreList.get(i).getIsDefault().equals("1")) {
		 										//tv_name,tv_tel,tv_adresss;
		 										addressId=moreList.get(i).getId();
		 										
		 									}
		 								}

										 myList.addAll(moreList);
										
										myAdapter.notifyDataSetChanged();
									} else {
										code = jsonobject.getString("message");
										Toast.makeText(getApplicationContext(),
												code, 1000).show();
									}
								} catch (JSONException e) {
									
									e.printStackTrace();

								}

							}

							@Override
							public void onFailure(int statusCode,
									Header[] headers, byte[] responseBody,
									Throwable error) {
								System.out.println("-onFailure---");
								Log.e("print", "-onFailure---" + error);
							}
						});
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.bt_add:
			startActivity(new Intent(GoodConfirm.this,AdressEdit.class));			
			break;
		case R.id.btn_pay:
			if(addressId!=-1){
				confirmGood();
			}else{
				Toast.makeText(getApplicationContext(), "请选择地址", 1000).show();
			}
			
			break;
		case R.id.add:
			quantity = Integer.parseInt(buyCountEdit.getText().toString()) + 1;
			buyCountEdit.setText(quantity + "");
			break;
		case R.id.reduce:
			if (quantity <= 1) {
				break;
			}
			quantity = Integer.parseInt(buyCountEdit.getText().toString()) - 1;
			buyCountEdit.setText(quantity + "");
			break;
		default:
			break;
		}
	}



	private void confirmGood() {
		et_comment = (EditText)findViewById(R.id.ed_comment);
		comment=et_comment.getText().toString();
		if(!buyCountEdit.getText().toString().trim().equals("")){
			quantity = Integer.parseInt(buyCountEdit.getText().toString().trim());
		}else{
			quantity = 1;
		}

		// comment=comment_et.getText().toString();
		RequestParams params = new RequestParams();
		params.put("customerId", MyApplication.NewUser.getId());
		params.put("goodId", goodId);
		params.put("paychannelId", paychannelId);
		params.put("addressId", addressId);
		params.put("quantity", quantity);
		params.put("comment", comment);
		params.put("is_need_invoice", is_need_invoice);
		params.put("invoice_type", invoice_type);
		params.put("invoice_info", et_titel.getText().toString());
		params.setUseJsonStreamer(true);
		// int customerId,
		// int goodId,
		// int paychannelId,
		// int quantity,
		// int addressId,
		// String comment,
		// int is_need_invoice,
		// int invoice_type,
		// String invoice_info,
		invoice_info = et_titel.getText().toString();
		Log.e("goodId=" + goodId + "paychannelId=" + paychannelId + "quantity="
				+ quantity + "is_need_invoice=" + is_need_invoice, "ccccccccc");
		API.GOODCONFIRM(GoodConfirm.this,MyApplication.NewUser.getId(),goodId,paychannelId,
				quantity,addressId,comment,is_need_invoice,invoice_type,invoice_info,
        		
				
                new HttpCallback  (GoodConfirm.this) {

					@Override
					public void onSuccess(Object data) {
						Intent i1 =new Intent (GoodConfirm.this,PayFromCar.class);
						String orderId = data.toString();
						try {
							i1.putExtra("orderId", Integer.parseInt(orderId));
						} catch (Exception e) {
						
						}
						
						i1.putExtra("type", 0);
						startActivity(i1);	
						finish();
					 
					}

					@Override
					public TypeToken getTypeToken() {
						return  null;
					}
                });

	}
	@Override
	protected void onResume() {
	
		super.onResume();
		myList.clear();
		getData1();
	}
}
