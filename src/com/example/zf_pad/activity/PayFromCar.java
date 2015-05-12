package com.example.zf_pad.activity;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zf_pad.R;
import com.example.zf_pad.alipay.PayActivity;
import com.example.zf_pad.entity.Goodlist;
import com.example.zf_pad.entity.OrderDetailEntity;
import com.example.zf_pad.trade.API;
import com.example.zf_pad.trade.common.HttpCallback;
import com.example.zf_pad.util.DialogUtil;
import com.example.zf_pad.util.DialogUtil.CallBackChange;
import com.example.zf_pad.util.TitleMenuUtil;
import com.google.gson.reflect.TypeToken;
public class PayFromCar extends PayActivity implements OnClickListener{
	private TextView tv_pay;
	private LinearLayout titleback_linear_back, ll_request;
	private int orderId;
	private String outTradeNo;
	private String subject;
	private String body;
	private String price;
	private int type;
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				tv_pay.setText("��  "+price);
				break;
			}
		}
	};

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay);
		orderId = getIntent().getIntExtra("orderId", 0);
		type = getIntent().getIntExtra("type", 0);
		new TitleMenuUtil(PayFromCar.this, "ѡ��֧����ʽ").show();

		tv_pay=(TextView) findViewById(R.id.tv_pay);

		titleback_linear_back = (LinearLayout) findViewById(R.id.titleback_linear_back);
		titleback_linear_back.setOnClickListener(this);
		ll_request = (LinearLayout) findViewById(R.id.ll_request);
		ll_request.setOnClickListener(this);
		
		getData();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.titleback_linear_back:
				dialogIntent();
			break;
		case R.id.ll_request:
				pay(outTradeNo, subject, body, price);
			break;
		default:
			break;
		}
	}

	private void dialogIntent() {
		Dialog dialog = new DialogUtil(PayFromCar.this,
				"�Ƿ�������").getCheck(new CallBackChange() {

					@Override
					public void change() {
						Intent intent = new Intent(PayFromCar.this,OrderDetail.class);
						intent.putExtra("status",1);
						intent.putExtra("id", orderId);
						intent.putExtra("type", type+"");
						startActivity(intent);
						finish();
					}
				});
		dialog.show();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getRepeatCount() == 0) {
				dialogIntent();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void getData() {

		API.getMyOrderById(this,orderId, 
				new HttpCallback<List<OrderDetailEntity>>(this) {

			@Override
			public void onSuccess(List<OrderDetailEntity> data) {
				OrderDetailEntity orderDetail = data.get(0);
				List<Goodlist> goodlist= orderDetail.getOrder_goodsList();
				if (goodlist.size()>0) {
					subject = goodlist.get(0).getGood_name();
					body = subject;
				}
				outTradeNo = orderDetail.getOrder_number();
				price = orderDetail.getOrder_totalprice();
				price = String.format("%.2f", Integer.parseInt(price)/100f);
				handler.sendEmptyMessage(0);
			}
			@Override
			public TypeToken<List<OrderDetailEntity>> getTypeToken() {
				return new TypeToken<List<OrderDetailEntity>>() {
				};
			}
		});
	}

	
	@Override
	public void success() {
		Intent intent = new Intent(PayFromCar.this,OrderDetail.class);
		intent.putExtra("status",2);
		intent.putExtra("id", orderId);
		intent.putExtra("type", type+"");
		startActivity(intent);
		finish();
	}
	@Override
	public void handling() {
		Intent intent = new Intent(PayFromCar.this,OrderDetail.class);
		intent.putExtra("status",1);
		intent.putExtra("id", orderId);
		intent.putExtra("type", type+"");
		startActivity(intent);
		finish();
	}
	@Override
	public void fail() {
//		Intent intent = new Intent(PayFromCar.this,OrderDetail.class);
//		intent.putExtra("status",1);
//		intent.putExtra("id", orderId);
//		intent.putExtra("type", type+"");
//		startActivity(intent);
//		finish();
	}
}
