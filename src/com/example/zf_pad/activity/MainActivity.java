package com.example.zf_pad.activity;

import static com.example.zf_pad.fragment.Constants.CityIntent.CITY_NAME;

import com.example.zf_pad.Config;
import com.example.zf_pad.R;
import com.example.zf_pad.aadpter.ShopcarAdapter;
import com.example.zf_pad.entity.PostPortEntity;
import com.example.zf_pad.fragment.m_MianFragment;
import com.example.zf_pad.fragment.m_my;
import com.example.zf_pad.fragment.m_shopcar;
import com.example.zf_pad.fragment.m_wdxx;
import com.example.zf_pad.popwindow.SetPopWindow;
import com.example.zf_pad.trade.ApplyListActivity;
import com.example.zf_pad.trade.CitySelectActivity;
import com.example.zf_pad.trade.TradeFlowActivity;
import com.example.zf_pad.trade.entity.City;
import com.example.zf_pad.trade.entity.Province;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import static com.example.zf_pad.fragment.Constants.CityIntent.SELECTED_CITY;
import static com.example.zf_pad.fragment.Constants.CityIntent.SELECTED_PROVINCE;
import static com.example.zf_pad.fragment.Constants.CityIntent.CITY_ID;
import static com.example.zf_pad.fragment.Constants.CityIntent.CITY_NAME;

public class MainActivity extends FragmentActivity implements OnClickListener {
	private RelativeLayout main_rl_pos, main_rl_renzhen, main_rl_zdgl,
			main_rl_jyls, main_rl_Forum, main_rl_wylc, main_rl_xtgg,
			main_rl_lxwm, main_rl_my, main_rl_pos1, main_rl_gwc;
	private RelativeLayout re_shopcar;
	private RelativeLayout re_myinfo;
	private RelativeLayout re_mine;
	private RelativeLayout re_sy;
	private m_MianFragment f_sy;
	private m_my m_my;
	private LinearLayout set;
	private ImageView im_sy;
	private ImageView im_ghc;
	private ImageView im_mess;
	private ImageView im_wd;
	private m_shopcar f_gwc;
	private m_wdxx f_wdxx;
	private Button bt_close;
	private PopupWindow popupWindow;
	private LayoutInflater inflater;
	private String cityName;
	private int cityId;
	private TextView cityTextView;
	public static final int REQUEST_CITY = 1;
	public static final int REQUEST_CITY_WHEEL = 2;
	private Province province;
	private City city;
	private View citySelect;
	private TextView textsy;
	private TextView textghc;
	private TextView textmes;
	private TextView textwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initView();

	}

	private void changTabBg() {
		im_sy.setBackgroundResource(R.drawable.home2);
		im_ghc.setBackgroundResource(R.drawable.shopping2);
		im_mess.setBackgroundResource(R.drawable.message2);
		im_wd.setBackgroundResource(R.drawable.mine2);
		textsy.setTextColor(getResources().getColor(R.color.white));
		textghc.setTextColor(getResources().getColor(R.color.white));
		textmes.setTextColor(getResources().getColor(R.color.white));
		textwd.setTextColor(getResources().getColor(R.color.white));

	}

	private void initView() {
		textsy = (TextView)findViewById(R.id.textsy);
		textghc = (TextView)findViewById(R.id.textghc);
		textmes = (TextView)findViewById(R.id.textmes);
		textwd = (TextView)findViewById(R.id.textwd);		

		main_rl_pos = (RelativeLayout) findViewById(R.id.main_rl_pos);
		main_rl_pos.setOnClickListener(this);
		main_rl_renzhen = (RelativeLayout) findViewById(R.id.main_rl_renzhen);
		main_rl_renzhen.setOnClickListener(this);
		main_rl_zdgl = (RelativeLayout) findViewById(R.id.main_rl_zdgl);
		main_rl_zdgl.setOnClickListener(this);
		main_rl_jyls = (RelativeLayout) findViewById(R.id.main_rl_jyls);
		main_rl_jyls.setOnClickListener(this);
		main_rl_Forum = (RelativeLayout) findViewById(R.id.main_rl_Forum);
		main_rl_Forum.setOnClickListener(this);
		main_rl_wylc = (RelativeLayout) findViewById(R.id.main_rl_wylc);
		main_rl_wylc.setOnClickListener(this);
		main_rl_lxwm = (RelativeLayout) findViewById(R.id.main_rl_lxwm);
		main_rl_lxwm.setOnClickListener(this);
		main_rl_xtgg = (RelativeLayout) findViewById(R.id.main_rl_xtgg);
		main_rl_xtgg.setOnClickListener(this);
		cityTextView = (TextView) findViewById(R.id.tv_city);
		citySelect = findViewById(R.id.titleback_linear_back);

		citySelect.setOnClickListener(this);

		im_sy = (ImageView) findViewById(R.id.laa1);
		im_ghc = (ImageView) findViewById(R.id.igw);
		im_mess = (ImageView) findViewById(R.id.im_mess);
		im_wd = (ImageView) findViewById(R.id.im_wd);

		re_sy = (RelativeLayout) findViewById(R.id.main_rl_sy);
		re_shopcar = (RelativeLayout) findViewById(R.id.main_rl_gwc);
		re_myinfo = (RelativeLayout) findViewById(R.id.main_rl_pos1);
		re_mine = (RelativeLayout) findViewById(R.id.main_rl_my);
		re_sy.setOnClickListener(this);
		re_shopcar.setOnClickListener(this);
		re_myinfo.setOnClickListener(this);
		re_mine.setOnClickListener(this);

		set = (LinearLayout) findViewById(R.id.set);
		set.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.titleback_linear_back:
			Intent intent = new Intent(MainActivity.this,
					CitySelectActivity.class);
			intent.putExtra(CITY_NAME, cityName);
			startActivityForResult(intent, REQUEST_CITY);
			break;
		case R.id.main_rl_sy:
			changTabBg();
			im_sy.setBackgroundResource(R.drawable.home);
			textsy.setTextColor(getResources().getColor(R.color.o));
			if (f_sy == null)
				f_sy = new m_MianFragment();

			getSupportFragmentManager().beginTransaction()
					.replace(R.id.m_fragment, f_sy).commit();
			break;
		case R.id.main_rl_gwc:
			changTabBg();
			im_ghc.setBackgroundResource(R.drawable.shopping);
			textghc.setTextColor(getResources().getColor(R.color.o));
			if (f_gwc == null)
				f_gwc = new m_shopcar();

			getSupportFragmentManager().beginTransaction()
					.replace(R.id.m_fragment, f_gwc).commit();
			break;
		case R.id.main_rl_pos1:
			changTabBg();
			im_mess.setBackgroundResource(R.drawable.message);
			textmes.setTextColor(getResources().getColor(R.color.o));
			if (f_wdxx == null)
				f_wdxx = new m_wdxx();

			getSupportFragmentManager().beginTransaction()
					.replace(R.id.m_fragment, f_wdxx).commit();
			break;
		case R.id.main_rl_my:
			changTabBg();
			im_wd.setBackgroundResource(R.drawable.mine);
			textwd.setTextColor(getResources().getColor(R.color.o));
			if (m_my == null)
				m_my = new m_my();

			getSupportFragmentManager().beginTransaction()
					.replace(R.id.m_fragment, m_my).commit();
			break;
		case R.id.set:
			showSet();
			break;

		case R.id.main_rl_jyls: // 交易流水

			startActivity(new Intent(MainActivity.this, TradeFlowActivity.class));
			break;
		case R.id.main_rl_pos: // 购买pos机

			startActivity(new Intent(MainActivity.this, PosListActivity.class));
			break;
		case R.id.main_rl_renzhen: // 开通认证

			startActivity(new Intent(MainActivity.this, ApplyListActivity.class));
			break;
		case R.id.main_rl_xtgg: // 系统公告

			startActivity(new Intent(MainActivity.this, SystemMessage.class));
			break;

		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK)
			return;
		switch (requestCode) {
		case REQUEST_CITY:
			cityId = data.getIntExtra(CITY_ID, 0);
			cityName = data.getStringExtra(CITY_NAME);
			cityTextView.setText(cityName);
			break;
		case REQUEST_CITY_WHEEL:
			province = (Province) data.getSerializableExtra(SELECTED_PROVINCE);
			city = (City) data.getSerializableExtra(SELECTED_CITY);
			cityTextView.setText(city.getName());
			break;
		}
	}

	private void showSet() {
		SetPopWindow set = new SetPopWindow(this);
		set.showAtLocation(findViewById(R.id.main), Gravity.CENTER
				| Gravity.CENTER, 0, 0);
	}

	@Override
	protected void onResume() {
		super.onResume();	
		if(Config.shopcar){
			changTabBg();
			im_ghc.setBackgroundResource(R.drawable.shopping);
			if (f_gwc == null)
				f_gwc = new m_shopcar();

			getSupportFragmentManager().beginTransaction()
					.replace(R.id.m_fragment, f_gwc).commit();
		}
	}
	@Override
	protected void onStop() {
		super.onStop();
		Config.shopcar=false;
	}
}
