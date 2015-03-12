package com.example.zf_pad.trade;

import com.example.zf_pad.R;
import com.example.zf_pad.util.TitleMenuUtil;

import android.app.Activity;
import android.os.Bundle;



/**
 * Created by Leo on 2015/3/2.
 */
public class AfterSalePayActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_after_sale_pay);
		new TitleMenuUtil(this, getString(R.string.title_after_sale_pay)).show();
	}
}
