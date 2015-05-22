package com.example.zf_pad.activity;

import com.example.zf_pad.BaseActivity;
import com.example.zf_pad.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FindpassmailSucces extends BaseActivity {
	private String type="";
	private String value="";
	private LinearLayout ll_mail;
	private LinearLayout ll_tel;
	private TextView tv_tel;
	private TextView tv_mail;
	private LinearLayout ll_land;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.passsucceed);
		type = getIntent().getStringExtra("type");
		value = getIntent().getStringExtra("value");
		initView();
	}
	private void initView() {
		ll_land = (LinearLayout)findViewById(R.id.login_linear_signin);
		ll_tel = (LinearLayout)findViewById(R.id.ll_tel);
		ll_mail = (LinearLayout)findViewById(R.id.ll_mail);
		tv_tel = (TextView)findViewById(R.id.tv_tel);
		tv_mail = (TextView)findViewById(R.id.tv_mail);
		if(type.equals("0")){
			ll_mail.setVisibility(View.VISIBLE);
			tv_mail.setText(value);
		}else if(type.equals("1")){
			ll_tel.setVisibility(View.VISIBLE);
			tv_tel.setText(value);
		}
		ll_land.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i=new Intent(FindpassmailSucces.this,LoginActivity.class);
				startActivity(i);
				FindpassmailSucces.this.finish();
			}
		});
		
	}
}
