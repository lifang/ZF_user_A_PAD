package com.example.zf_pad.activity;

import com.example.zf_pad.BaseActivity;
import com.example.zf_pad.R;
import com.example.zf_pad.util.TitleMenuUtil;

import android.os.Bundle;
public class PayFromCar extends BaseActivity{
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.pay);
			new TitleMenuUtil(PayFromCar.this, "选择支付方式").show();
		}
}
