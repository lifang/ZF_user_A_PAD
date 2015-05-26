package com.example.zf_pad.activity;


import com.example.zf_pad.R;
import com.example.zf_pad.util.TitleMenuUtil;

import android.app.Activity;
import android.os.Bundle;

public class LoanActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loan);
		new TitleMenuUtil(this, "ÎÒÒª´û¿î").show();
	}
}
