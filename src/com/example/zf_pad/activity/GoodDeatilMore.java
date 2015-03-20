package com.example.zf_pad.activity;
import com.example.zf_pad.Config;
import com.example.zf_pad.R;
import com.example.zf_pad.aadpter.HuilvAdapter;
import com.example.zf_pad.fragment.f_good_detail;
import com.example.zf_pad.fragment.good_detail_apply;
import com.example.zf_pad.fragment.good_detail_commet;
import com.example.zf_pad.fragment.good_detail_zd;
import com.example.zf_pad.util.ScrollViewWithListView;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GoodDeatilMore extends FragmentActivity implements OnClickListener{

	private f_good_detail detail;
	private good_detail_apply apply;
	private good_detail_commet commet;
	private good_detail_zd zd;
	private int type;
	private TextView tv_ms;
	private TextView tv_kt;
	private TextView tv_pl;
	private TextView tv_zd;
	private TextView tv_jy;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goodmoremain);
		type = getIntent().getIntExtra("type", 0);
		initView();
		switch (type) {
		case 0:
			if (detail == null)
				detail = new f_good_detail();
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.f_good_detail, detail).commit();
			tv_ms.setTextColor(getResources().getColor(R.color.o));
			break;
		case 1:
			if (apply == null)
				apply = new good_detail_apply();
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.f_good_detail, apply).commit();
			tv_kt.setTextColor(getResources().getColor(R.color.o));
			break;
		case 2:
			if (commet == null)
				commet = new good_detail_commet();
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.f_good_detail, commet).commit();
			tv_pl.setTextColor(getResources().getColor(R.color.o));
			break;
		case 3:
			if (zd == null)
				zd = new good_detail_zd();
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.f_good_detail, zd).commit();
			tv_zd.setTextColor(getResources().getColor(R.color.o));
			break;
		default:
			break;
		}
		
	}

	private void initView() {
		
		tv_ms = (TextView) findViewById(R.id.tv_ms);
		tv_ms.setOnClickListener(this);
		tv_kt = (TextView) findViewById(R.id.tv_kt);
		tv_kt.setOnClickListener(this);
		tv_pl = (TextView) findViewById(R.id.tv_pl);
		tv_pl.setOnClickListener(this);
		tv_zd = (TextView) findViewById(R.id.tv_zd);
		tv_zd.setOnClickListener(this);
		tv_jy = (TextView) findViewById(R.id.tv_jy);
		tv_jy.setOnClickListener(this);

		
	}
	private void changColor(){
		tv_ms.setTextColor(getResources().getColor(R.color.text292929));
		tv_kt.setTextColor(getResources().getColor(R.color.text292929));
		tv_pl.setTextColor(getResources().getColor(R.color.text292929));
		tv_zd.setTextColor(getResources().getColor(R.color.text292929));
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_zd:
			if (zd == null)
				zd = new good_detail_zd();
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.f_good_detail, zd).commit();
			changColor();
			tv_zd.setTextColor(getResources().getColor(R.color.o));
			break;
		case R.id.tv_ms:
			if (detail == null)
				detail = new f_good_detail();
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.f_good_detail, detail).commit();
			changColor();
			tv_ms.setTextColor(getResources().getColor(R.color.o));
			break;
		case R.id.tv_kt:
			if (apply == null)
				apply = new good_detail_apply();
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.f_good_detail, apply).commit();
			changColor();
			tv_kt.setTextColor(getResources().getColor(R.color.o));
			break;
		case R.id.tv_pl:
			//Config.goodId = gfe.getId();
			//Config.commentsCount = commentsCount + "";
			if (commet == null)
				commet = new good_detail_commet();
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.f_good_detail, commet).commit();
			changColor();
			tv_pl.setTextColor(getResources().getColor(R.color.o));
			break;

		default:
			break;
		}
		
	}

}
