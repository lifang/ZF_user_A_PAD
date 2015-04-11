package com.example.zf_pad.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zf_pad.BaseActivity;
import com.example.zf_pad.R;
import com.example.zf_pad.util.TitleMenuUtil;

public class EditMerchant extends BaseActivity{
	private TextView tv_shopname,tv_name,tv_id_number,tv_license_code,tv_tax_id_number,
	tv_certificate_no,tv_bank,tv_licencenum_bank;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.editmerchant);
	new TitleMenuUtil(EditMerchant.this, "…ÃªßœÍ«È").show();
	init();
}
private void init() {
	tv_shopname=(TextView) findViewById(R.id.tv_shopname);
	tv_name=(TextView) findViewById(R.id.tv_name);
	tv_id_number=(TextView) findViewById(R.id.tv_id_number);
	tv_license_code=(TextView) findViewById(R.id.tv_license_code);
	tv_tax_id_number=(TextView) findViewById(R.id.tv_tax_id_number);
	tv_certificate_no=(TextView) findViewById(R.id.tv_certificate_no);
	tv_bank=(TextView) findViewById(R.id.tv_bank);
	tv_licencenum_bank=(TextView) findViewById(R.id.tv_licencenum_bank);
	
}
}
