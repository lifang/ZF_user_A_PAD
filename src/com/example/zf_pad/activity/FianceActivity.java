package com.example.zf_pad.activity;


import com.example.zf_pad.R;
import com.example.zf_pad.util.TitleMenuUtil;

import android.app.Activity;
import android.os.Bundle;

public class FianceActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loan);
		new TitleMenuUtil(this, "我要理财").show();
	}
}
