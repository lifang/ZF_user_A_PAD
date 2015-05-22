package com.example.zf_pad.activity;

import static com.example.zf_pad.fragment.Constants.AfterSaleType.CANCEL;
import static com.example.zf_pad.fragment.Constants.CityIntent.CITY_NAME;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.example.zf_pad.Config;
import com.example.zf_pad.MyApplication;
import com.example.zf_pad.R;
import com.example.zf_pad.aadpter.ShopcarAdapter;
import com.example.zf_pad.entity.PicEntity;
import com.example.zf_pad.entity.PostPortEntity;
import com.example.zf_pad.entity.UserEntity;
import com.example.zf_pad.fragment.M_MianFragment;
import com.example.zf_pad.fragment.M_my;
import com.example.zf_pad.fragment.M_shopcar;
import com.example.zf_pad.fragment.M_wdxx;
import com.example.zf_pad.popwindow.SetPopWindow;
import com.example.zf_pad.trade.API;
import com.example.zf_pad.trade.AfterSaleDetailActivity;
import com.example.zf_pad.trade.ApplyListActivity;
import com.example.zf_pad.trade.CitySelectActivity;
import com.example.zf_pad.trade.MyApplyDetail;
import com.example.zf_pad.trade.TradeFlowActivity;
import com.example.zf_pad.trade.common.CommonUtil;
import com.example.zf_pad.trade.common.HttpCallback;
import com.example.zf_pad.trade.entity.City;
import com.example.zf_pad.trade.entity.Province;
import com.example.zf_pad.util.ImageCacheUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
	private LocationClient mLocationClient;
	private TextView LocationResult;
	private RelativeLayout main_rl_pos, main_rl_renzhen, main_rl_zdgl,
	main_rl_jyls, main_rl_Forum, main_rl_wylc, main_rl_xtgg,
	main_rl_lxwm;
	private RelativeLayout re_shopcar;
	private RelativeLayout re_myinfo;
	private RelativeLayout re_mine;
	private RelativeLayout re_sy;
	private M_MianFragment f_sy;
	private M_my M_my;
	private LinearLayout set;
	private ImageView im_sy;
	private ImageView im_ghc;
	private ImageView im_mess;
	private ImageView im_wd;
	private M_shopcar f_gwc;
	private M_wdxx f_wdxx;

	private TextView textsy;
	private TextView textghc;
	private TextView textmes;
	private TextView textwd;
	
	private TextView countShopCar;
	
	private Button bt_close;
	private PopupWindow popupWindow;
	private String cityName;
	private int cityId;
	private TextView cityTextView;
	public static final int REQUEST_CITY = 1;
	public static final int REQUEST_CITY_WHEEL = 2;
	private Province province;
	private City city;
	private View citySelect;
	private int flag = 0;
	private SharedPreferences mySharedPreferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Config.cityId = 394;
		Config.CITY = "上海";
		Config.isFRIST = false;
		Display display = getWindowManager().getDefaultDisplay();
		Config.ScreenWidth = display.getWidth();
		Config.ScreenHeight = display.getHeight();
		
		mySharedPreferences = getSharedPreferences("CountShopCar", MODE_PRIVATE);
		Config.countShopCar = mySharedPreferences.getInt("countShopCar", 0);
		/*
		 * UserEntity ue=new UserEntity(); ue.setId(80);
		 * MyApplication.NewUser=ue;
		 */
		Log.i("111", "width=" + Config.ScreenWidth + "height="
				+ Config.ScreenHeight);
		// if (f_sy == null)
		f_sy = new M_MianFragment();

		getSupportFragmentManager().beginTransaction()
		.replace(R.id.m_fragment, f_sy).commit();
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
		countShopCar = (TextView) findViewById(R.id.countShopCar);
		
		textsy = (TextView) findViewById(R.id.textsy);
		textghc = (TextView) findViewById(R.id.textghc);
		textmes = (TextView) findViewById(R.id.textmes);
		textwd = (TextView) findViewById(R.id.textwd);

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

		case R.id.main_rl_sy:

			if (flag != 0) {

				flag = 0;
				changTabBg();
				im_sy.setBackgroundResource(R.drawable.home);
				textsy.setTextColor(getResources().getColor(R.color.o));
				// if (f_sy == null)
				f_sy = new M_MianFragment();
				getSupportFragmentManager().beginTransaction()
				.replace(R.id.m_fragment, f_sy).commit();
				MyApplication.hideSoftKeyboard(this);
			}
			break;
		case R.id.main_rl_gwc:
			if (flag != 1) {
				if (Config.CheckIsLogin(MainActivity.this)) {
					changTabBg();
					flag = 1;
					im_ghc.setBackgroundResource(R.drawable.shopping);
					textghc.setTextColor(getResources().getColor(R.color.o));
					if (f_gwc == null)
						f_gwc = new M_shopcar();

					getSupportFragmentManager().beginTransaction()
					.replace(R.id.m_fragment, f_gwc).commit();
					MyApplication.hideSoftKeyboard(this);
					Config.countShopCar = 0;
					countShopCar.setVisibility(View.GONE);
					
					mySharedPreferences = getSharedPreferences("CountShopCar",MODE_PRIVATE); 
					SharedPreferences.Editor editor = mySharedPreferences.edit(); 
					editor.putInt("countShopCar", Config.countShopCar); 
					editor.commit();

				}
			}


			break;
		case R.id.main_rl_pos1:
			if (flag != 2) {
				if (Config.CheckIsLogin(MainActivity.this)) {
					changTabBg();
					flag = 2;
					im_mess.setBackgroundResource(R.drawable.message);
					textmes.setTextColor(getResources().getColor(R.color.o));
					if (f_wdxx == null)
						f_wdxx = new M_wdxx();

					getSupportFragmentManager().beginTransaction()
					.replace(R.id.m_fragment, f_wdxx).commit();
				}
			}

			break;
		case R.id.main_rl_my:
			if (flag != 3) {
				if (Config.CheckIsLogin(MainActivity.this)) {
					Config.MyTab = 0;
					flag = 3;
					changTabBg();
					im_wd.setBackgroundResource(R.drawable.mine);
					textwd.setTextColor(getResources().getColor(R.color.o));
					if (M_my == null) {
						M_my = new M_my();
					}
					getSupportFragmentManager().beginTransaction()

					.replace(R.id.m_fragment, M_my).commit();
					getSupportFragmentManager().beginTransaction()
					.replace(R.id.m_fragment, M_my).commit();
				}

				
				MyApplication.hideSoftKeyboard(this);

			}

			// else{
			// Toast.makeText(getApplication(), "请先登陆", 1000).show();
			// }
			break;
		case R.id.set:
			showSet();
			MyApplication.hideSoftKeyboard(this);
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

	public void ToIndex() {
		if (f_sy == null)
			f_sy = new M_MianFragment();

		getSupportFragmentManager().beginTransaction()
		.replace(R.id.m_fragment, f_sy).commit();
	}

	@Override
	protected void onRestart() {

		super.onRestart();
		Log.e("here", "here");
		if (Config.shopcar) {
			changTabBg();
			im_ghc.setBackgroundResource(R.drawable.shopping);
			if (f_gwc == null)
				f_gwc = new M_shopcar();

			getSupportFragmentManager().beginTransaction()
			.replace(R.id.m_fragment, f_gwc).commit();
			Config.countShopCar = 0;
			countShopCar.setVisibility(View.GONE);
			
			mySharedPreferences = getSharedPreferences("CountShopCar",MODE_PRIVATE); 
			SharedPreferences.Editor editor = mySharedPreferences.edit(); 
			editor.putInt("countShopCar", Config.countShopCar); 
			editor.commit();
		}
		if (Config.AderssManger) {
			changTabBg();
			im_wd.setBackgroundResource(R.drawable.mine);
			textwd.setTextColor(getResources().getColor(R.color.o));
			if (M_my == null) {
				M_my = new M_my();
			}
			getSupportFragmentManager().beginTransaction()

			.replace(R.id.m_fragment, M_my).commit();
		}

		if (Config.isExit) {
			changTabBg();
			im_sy.setBackgroundResource(R.drawable.home);
			f_sy = new M_MianFragment();

			getSupportFragmentManager().beginTransaction()
			.replace(R.id.m_fragment, f_sy).commit();
			Config.isExit=false;

		}
	}

	@Override
	protected void onResume() {
		super.onResume();	
		MobclickAgent.onResume(this);
		if(Config.ismer){
			changTabBg();
			im_wd.setBackgroundResource(R.drawable.mine);
			textwd.setTextColor(getResources().getColor(R.color.o));
			if (M_my == null){
				M_my = new M_my();
			}
			flag=3;
			getSupportFragmentManager().beginTransaction()

			.replace(R.id.m_fragment, M_my).commit();
		}
		if (Config.countShopCar != 0) {
			countShopCar.setVisibility(View.VISIBLE);
			countShopCar.setText(Config.countShopCar+"");
		}else {
			countShopCar.setVisibility(View.GONE);
		}
		
		if (Config.shopcar) {
			changTabBg();
			im_ghc.setBackgroundResource(R.drawable.shopping);
			if (f_gwc == null)
				f_gwc = new M_shopcar();
			flag=1;
			getSupportFragmentManager().beginTransaction()
			.replace(R.id.m_fragment, f_gwc).commit();
			Config.countShopCar = 0;
			countShopCar.setVisibility(View.GONE);
		}
		if (Config.AderssManger) {
			changTabBg();
			im_wd.setBackgroundResource(R.drawable.mine);
			textwd.setTextColor(getResources().getColor(R.color.o));
			if (M_my == null){
				M_my = new M_my();
			}
			flag=3;
			getSupportFragmentManager().beginTransaction()

			.replace(R.id.m_fragment, M_my).commit();
		}

		if(Config.isExit){
			changTabBg();
			im_sy.setBackgroundResource(R.drawable.home);
			//if(M_my!=null)
			//getSupportFragmentManager().beginTransaction().remove(M_my);
			//if(f_sy==null)
			f_sy = new M_MianFragment();
			flag=0;
			getSupportFragmentManager().beginTransaction()
			.replace(R.id.m_fragment, f_sy).commit();
			Config.isExit=false;

		}
		mySharedPreferences = getSharedPreferences("CountShopCar",MODE_PRIVATE); 
		SharedPreferences.Editor editor = mySharedPreferences.edit(); 
		editor.putInt("countShopCar", Config.countShopCar); 
		editor.commit();
	}

	@Override
	protected void onStop() {
		super.onStop();
		Config.shopcar = false;
	}
	// 返回键  
	private long exitTime = 0;  
	@Override  
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {


			if (Config.AderssMangerBACK) {
				changTabBg();
				im_ghc.setBackgroundResource(R.drawable.shopping);
				textghc.setTextColor(getResources().getColor(R.color.o));
				/*
			M_shopcar f_gwc = new M_shopcar();

			getSupportFragmentManager().beginTransaction()
					.replace(R.id.m_fragment, f_gwc).commit();*/
				startActivity(new Intent(MainActivity.this,ConfirmOrder.class));
				Config.AderssMangerBACK=false;
				flag=1;
			}
			if(!f_sy.isVisible()){

				f_sy = new M_MianFragment();

				getSupportFragmentManager().beginTransaction()
				.replace(R.id.m_fragment, f_sy).commit();
				changTabBg();
				flag=0;
				im_sy.setBackgroundResource(R.drawable.home);
			}else{
				if ((System.currentTimeMillis() - exitTime) > 2000) {  
					Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();  
					exitTime = System.currentTimeMillis();  
				} else {  
					finish();  
					System.exit(0);  
				}  
			}
		}  
		return true;  
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
    
}
