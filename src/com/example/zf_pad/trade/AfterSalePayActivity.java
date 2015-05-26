package com.example.zf_pad.trade;

import static com.example.zf_pad.fragment.Constants.AfterSaleIntent.RECORD_ID;
import static com.example.zf_pad.fragment.Constants.AfterSaleIntent.RECORD_TYPE;
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

import com.epalmpay.userPad.R;
import com.example.zf_pad.alipay.RepairPayActivity;
import com.example.zf_pad.trade.common.HttpCallback;
import com.example.zf_pad.trade.entity.RepairPayEntity;
import com.example.zf_pad.util.DialogUtil;
import com.example.zf_pad.util.DialogUtil.CallBackChange;
import com.example.zf_pad.util.TitleMenuUtil;
import com.google.gson.reflect.TypeToken;
/**
 * Created by Leo on 2015/3/2.
 */
public class AfterSalePayActivity extends RepairPayActivity implements OnClickListener {
	private TextView tv_pay;
	private LinearLayout titleback_linear_back, ll_request;
	private String orderId = "";
	private String outTradeNo;
	private String subject;
	private String body;
	private String price;
	
	private String type = "";
	
	private int mRecordType;
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				tv_pay.setText("￥  "+price);
				break;
			}
		}
	};

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay);
		mRecordType = getIntent().getIntExtra(RECORD_TYPE, 0);
		orderId = getIntent().getExtras().getString("orderId", "");
		new TitleMenuUtil(AfterSalePayActivity.this, "选择支付方式").show();

		if (orderId.equals("")) {
			Toast.makeText(this, "没有传订单id", Toast.LENGTH_SHORT).show();
		}

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
		Dialog dialog = new DialogUtil(AfterSalePayActivity.this,
				"是否放弃付款？").getCheck(new CallBackChange() {

					@Override
					public void change() {
						Intent intent = new Intent(AfterSalePayActivity.this,AfterSaleDetailActivity.class);
						intent.putExtra(RECORD_TYPE, mRecordType);
						intent.putExtra(RECORD_ID, Integer.valueOf(orderId));
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

		API.getRepairPay(this, Integer.parseInt(orderId), 
				new HttpCallback<RepairPayEntity>(this) {

			@Override
			public void onSuccess(RepairPayEntity data) {
					subject = data.getMiaoshu();
					body = subject;
				outTradeNo = data.getApply_num();
				price = data.getRepair_price();
				price = String.format("%.2f", Integer.parseInt(price)/100f);
				handler.sendEmptyMessage(0);
			}
			@Override
			public TypeToken<RepairPayEntity> getTypeToken() {
				return new TypeToken<RepairPayEntity>() {
				};
			}
		});
	}

	
	@Override
	public void success() {
		Intent intent = new Intent(AfterSalePayActivity.this,AfterSaleDetailActivity.class);
		intent.putExtra(RECORD_TYPE, mRecordType);
		intent.putExtra(RECORD_ID, Integer.valueOf(orderId));
		startActivity(intent);
		finish();
	}
	@Override
	public void handling() {
		Intent intent = new Intent(AfterSalePayActivity.this,AfterSaleDetailActivity.class);
		intent.putExtra(RECORD_TYPE, mRecordType);
		intent.putExtra(RECORD_ID, Integer.valueOf(orderId));
		startActivity(intent);
		finish();
	}
	@Override
	public void fail() {
//		Intent intent = new Intent(AfterSalePayActivity.this,AfterSaleDetailActivity.class);
//		intent.putExtra(RECORD_TYPE, mRecordType);
//		intent.putExtra(RECORD_ID, Integer.valueOf(orderId));
//		startActivity(intent);
//		finish(); 
	}
}
