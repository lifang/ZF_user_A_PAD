package com.example.zf_pad.activity;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.epalmpay.userPad.R;
import com.example.zf_pad.BaseActivity;
import com.example.zf_pad.Config;
import com.example.zf_pad.MyApplication;
import com.example.zf_pad.aadpter.OrderDetail_PosAdapter;
import com.example.zf_pad.aadpter.RecordAdapter;
import com.example.zf_pad.entity.Goodlist;
import com.example.zf_pad.entity.MarkEntity;
import com.example.zf_pad.entity.OrderDetailEntity;
import com.example.zf_pad.util.AlertDialog;
import com.example.zf_pad.util.AlertMessDialog;
import com.example.zf_pad.util.ScrollViewWithListView;
import com.example.zf_pad.util.TitleMenuUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class OrderDetail extends BaseActivity implements OnClickListener {
	private ScrollViewWithListView pos_lv, his_lv;
	List<Goodlist> goodlist = new ArrayList<Goodlist>();
	List<MarkEntity> relist = new ArrayList<MarkEntity>();
	private OrderDetail_PosAdapter posAdapter;
	private RecordAdapter reAdapter;
	private LinearLayout ll_ishow;
	private Button btn_ishow;
	private List<OrderDetailEntity> ode = new ArrayList<OrderDetailEntity>();
	private TextView tv_status, tv_sjps, tv_psf, tv_reperson, tv_tel,
			tv_adress, tv_ly, tv_fplx, fptt, tv_ddbh, tv_pay, tv_time, tv_gj,
			tv_money;
	private int status, id;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				OrderDetailEntity entity = ode.get(0);
				tv_sjps.setText("实付金额(含配送费) ：￥ "
						+df.format(check(entity.getOrder_totalprice())/100) );
				tv_psf.setText("含配送费   ：￥ " + df.format(check(entity.getOrder_psf())));
				tv_reperson.setText("收   件  人  ：   " + entity.getOrder_receiver());
				tv_tel.setText(entity.getOrder_receiver_phone());
				if(entity.getOrder_address()==null){
					tv_adress.setText("收货地址   ：   " );
				}else{
					tv_adress.setText("收货地址   ：   " + entity.getOrder_address());
				}
				
				tv_ly.setText("留         言  ：   " + entity.getOrder_comment());
				tv_fplx.setText(entity.getOrder_invoce_type().equals("1") ? "发票类型  ： 个人"
						: "发票类型   ： 公司");
				fptt.setText("发票抬头   ：   " + entity.getOrder_invoce_info());
				tv_ddbh.setText("订单编号  ：   " + entity.getOrder_number());
				if (entity.getOrder_payment_type().equals("1")) {
					tv_pay.setText("支付方式  ：   支付宝");
				} else if (entity.getOrder_payment_type().equals("2")) {
					tv_pay.setText("支付方式  ：   银联");
				} else if (entity.getOrder_payment_type().equals("3")) {
					tv_pay.setText("支付方式  ：   现金");
				} else {
					tv_pay.setText("支付方式  ：   ");
				}
				tv_time.setText("实付金额  ：   ￥" + check(entity.getOrder_totalprice())/100);
				tv_money.setText("订单日期  ：   " + entity.getOrder_createTime());
				tv_gj.setText("共计  ：   " + entity.getOrder_totalNum() + "件商品");
				switch (entity.getOrder_status()) {
				case 1:
					tv_status.setText("订单状态   : 未付款");
					
					break;
				case 2:
					tv_status.setText("订单状态   : 已付款");
					btn_ishow.setVisibility(View.VISIBLE);
					break;
				case 3:
					tv_status.setText("订单状态   : 已发货");
					btn_ishow.setVisibility(View.VISIBLE);
					bt_pj.setVisibility(View.VISIBLE);
					btn_ishow_wl.setVisibility(View.VISIBLE);
					break;
				case 4:
					tv_status.setText("订单状态   : 已评价");
					btn_ishow.setVisibility(View.VISIBLE);
					bt_pj.setVisibility(View.GONE);
					break;
				case 5:
					tv_status.setText("订单状态   : 已取消");

					break;
				case 6:

					tv_status.setText("订单状态  : 交易关闭");
					break;
				default:
					break;
				}
				break;
			case 1:
				Toast.makeText(getApplicationContext(), (String) msg.obj,
						Toast.LENGTH_SHORT).show();

				break;
			case 2: // 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
				Toast.makeText(getApplicationContext(),
						"no 3g or wifi content", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(getApplicationContext(), " refresh too much",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	private Button bt_pj;
	private TextView tv_price;
	private TextView tv_sl;
	private TextView tv_comment;
	private AlertMessDialog amd;
	private String type="1";
	private Button bt_pay;
	private Button bt_cancel;
	private DecimalFormat df;
	private Button btn_ishow_wl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_detail);
		df = (DecimalFormat) NumberFormat
				.getInstance();
		df.applyPattern("0.00");
		status = getIntent().getIntExtra("status", 0);
		id = getIntent().getIntExtra("id", 0);
		type =getIntent().getStringExtra("type");
		//Toast.makeText(getApplicationContext(), id+"", 1000).show();
		if(type.equals("2")){
			new TitleMenuUtil(OrderDetail.this, "租赁订单详情").show();
		}else{
			new TitleMenuUtil(OrderDetail.this, "订单详情").show();
		}
		
		getData();
	}

	private void getData() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		System.out.println("id```" + id);
		JSONObject jsonParams = new JSONObject(params);
		HttpEntity entity;
		try {
			entity = new StringEntity(jsonParams.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			
		
			return;
		}
		MyApplication
				.getInstance()
				.getClient()
				.post(getApplicationContext(),Config.ORDERDETAIL,null,entity,"application/json",
						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(int statusCode,
									Header[] headers, byte[] responseBody) {
								String responseMsg = new String(responseBody)
										.toString();
								Log.e("print", responseMsg);
								Gson gson = new Gson();

								JSONObject jsonobject = null;
								String code = null;
								try {
									jsonobject = new JSONObject(responseMsg);
									code = jsonobject.getString("code");
									int a = jsonobject.getInt("code");
									if (a == Config.CODE) {
										initView();
										String res = jsonobject
												.getString("result");
										// jsonobject = new JSONObject(res);
										System.out.println("````" + res);
										ode = gson
												.fromJson(
														res,
														new TypeToken<List<OrderDetailEntity>>() {
														}.getType());
										System.out
												.println("```ode`"
														+ ode.get(0)
																.getOrder_receiver_phone());
										// jsonobject = new JSONObject(res);
										goodlist = ode.get(0)
												.getOrder_goodsList();
										relist = ode.get(0).getComments()
												.getContent();
										System.out.println("```relist`"
												+ relist.size());
										System.out.println("```goodlist`"
												+ goodlist.size());

										posAdapter = new OrderDetail_PosAdapter(
												OrderDetail.this, goodlist,
												status);
										pos_lv.setAdapter(posAdapter);
										if(relist.size()==0){
											tv_comment.setVisibility(View.GONE);
										}
										reAdapter = new RecordAdapter(
												OrderDetail.this, relist);
										his_lv.setAdapter(reAdapter);

										handler.sendEmptyMessage(0);

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
	private void initView() {
		btn_ishow_wl = (Button)findViewById(R.id.btn_ishow_wl);
		btn_ishow_wl.setOnClickListener(this);
		bt_pay = (Button)findViewById(R.id.bt_pay);
		bt_pay.setOnClickListener(this);
		bt_cancel = (Button)findViewById(R.id.bt_cancel);
		tv_comment = (TextView)findViewById(R.id.tv_comment);
		bt_cancel.setOnClickListener(this);
		tv_money = (TextView) findViewById(R.id.tv_money);
		bt_pj = (Button) findViewById(R.id.btn_pj);
		bt_pj.setOnClickListener(this);
		tv_gj = (TextView) findViewById(R.id.tv_gj);
		btn_ishow = (Button) findViewById(R.id.btn_ishow);
		btn_ishow.setOnClickListener(this);
		tv_ddbh = (TextView) findViewById(R.id.tv_ddbh);
		fptt = (TextView) findViewById(R.id.fptt);
		tv_pay = (TextView) findViewById(R.id.tv_pay);
		tv_fplx = (TextView) findViewById(R.id.tv_fplx);
		tv_ly = (TextView) findViewById(R.id.tv_ly);
		tv_adress = (TextView) findViewById(R.id.tv_adress);
		tv_reperson = (TextView) findViewById(R.id.tv_reperson);
		tv_tel = (TextView) findViewById(R.id.tv_tel);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_psf = (TextView) findViewById(R.id.tv_psf);
		tv_sjps = (TextView) findViewById(R.id.tv_sjps);
		tv_status = (TextView) findViewById(R.id.tv_status);
		ll_ishow = (LinearLayout) findViewById(R.id.ll_ishow);
		ll_ishow.setVisibility(status == 1 ? View.VISIBLE : View.INVISIBLE); // 只有状态是1
																				// 才有下面的按钮
		pos_lv = (ScrollViewWithListView) findViewById(R.id.pos_lv1);
		his_lv = (ScrollViewWithListView) findViewById(R.id.his_lv);
		tv_price = (TextView)findViewById(R.id.tv_price);
		tv_sl = (TextView)findViewById(R.id.tv_quantity);
		if(type.equals("2")){
			tv_price.setText("押金");
			tv_sl.setText("租赁数量");
		}
		switch (status) {
		case 1:
			tv_status.setText("订单状态   : 未付款");
			
			break;
		case 2:
			tv_status.setText("订单状态   : 已付款");
			btn_ishow.setVisibility(View.VISIBLE);
			break;
		case 3:
			tv_status.setText("订单状态   : 已发货");
			btn_ishow.setVisibility(View.VISIBLE);
			bt_pj.setVisibility(View.VISIBLE);
			btn_ishow_wl.setVisibility(View.VISIBLE);
			break;
		case 4:
			tv_status.setText("订单状态   : 已评价");
			btn_ishow.setVisibility(View.VISIBLE);
			break;
		case 5:
			tv_status.setText("订单状态   : 已取消");

			break;
		case 6:

			tv_status.setText("订单状态  : 交易关闭");
			break;
		default:
			break;
		}
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_ishow_wl:
			amd = new AlertMessDialog(OrderDetail.this);
			amd.setTitle("物流公司: "+ode.get(0).getLogistics_name());
			amd.setMessage("物流单号: "+ode.get(0).getLogistics_number());
			amd.setNegativeButton("确认", new OnClickListener() {

				@Override
				public void onClick(View v) {
					amd.dismiss();

				}
			});
			break;
		case R.id.btn_pj:
			Config.list = ode.get(0).getOrder_goodsList();
			if (Config.list.size() != 0) {
				Config.GoodComment=id;
				startActivity(new Intent(OrderDetail.this, Comment.class));
			}
			break;
		case R.id.btn_ishow:
			amd = new AlertMessDialog(OrderDetail.this);
			amd.setTitle("查看终端号");
			amd.setMessage(ode.get(0).getTerminals());
			amd.setNegativeButton("确认", new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					amd.dismiss();
					
				}
			});
			break;
		case R.id.bt_pay:
			Intent i = new Intent(getApplicationContext(),PayFromCar.class);
			i.putExtra("orderId", id);
			i.putExtra("type", "1");
			startActivity(i);
			finish();
			break;
		case R.id.bt_cancel:
			final AlertDialog ad = new AlertDialog(OrderDetail.this);
			ad.setTitle("提示");
			ad.setMessage("确认取消?");
			ad.setPositiveButton("取消", new OnClickListener() {				
				@Override
				public void onClick(View arg0) {
					ad.dismiss();				
				}
			});
			ad.setNegativeButton("确定", new OnClickListener() {
				
				@Override
				public void onClick(final View arg0) {
					ad.dismiss();
					RequestParams params = new RequestParams();
					params.put("id", id);
					 System.out.println("`getTag``"+id);
					 
					params.setUseJsonStreamer(true);

					MyApplication.getInstance().getClient()
							.post(Config.ORDERCANL, params, new AsyncHttpResponseHandler() {

								@Override
								public void onSuccess(int statusCode, Header[] headers,
										byte[] responseBody) {
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
											Toast.makeText(OrderDetail.this, jsonobject.getString("message"), 1000).show();
											ll_ishow.setVisibility(View.INVISIBLE);
										}else{
											code = jsonobject.getString("message");
											Toast.makeText(OrderDetail.this, code, 1000).show();
										}
									} catch (JSONException e) {
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
			});
			break;
		default:
			break;
		}
	}
	double check(String str) {
		try {

			double min = Double.valueOf(str);// 把字符串强制转换为数字
			return min;// 如果是数字，返回True
		} catch (Exception e) {
			
			return 0;// 如果抛出异常，返回False
		}
	}
	@Override
	protected void onResume() {		
		super.onResume();
		getData();
	}
}
