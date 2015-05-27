package com.example.zf_pad.activity;


import android.app.Activity;
import android.os.Bundle;

import com.epalmpay.userPad.R;
import com.example.zf_pad.util.TitleMenuUtil;

public class FianceActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loan);
		new TitleMenuUtil(this, "我要理财").show();
	}
}
