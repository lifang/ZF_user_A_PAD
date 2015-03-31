package com.example.zf_pad.activity;

import android.os.Bundle;

import com.example.zf_pad.BaseActivity;
import com.example.zf_pad.R;
import com.example.zf_pad.util.TitleMenuUtil;

public class CreatMerchant extends BaseActivity{
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.creatdetail);
	new TitleMenuUtil(CreatMerchant.this, "创建商户").show();
}
}
