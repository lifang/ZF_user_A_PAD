package com.example.zf_pad.activity;

import static com.example.zf_pad.fragment.Constants.AfterSaleType.CANCEL;
import static com.example.zf_pad.fragment.Constants.CityIntent.CITY_NAME;

import java.util.ArrayList;
import java.util.List;

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
import com.example.zf_pad.fragment.m_MianFragment;
import com.example.zf_pad.fragment.m_my;
import com.example.zf_pad.fragment.m_shopcar;
import com.example.zf_pad.fragment.m_wdxx;
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
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
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
	// vp
	private ArrayList<String> mal = new ArrayList<String>();
	private ArrayList<PicEntity> myList = new ArrayList<PicEntity>();
	private ViewPager view_pager;
	private MyAdapter adapter;
	private ImageView[] indicator_imgs;// 存放引到图片数组
	private View item;
	private LayoutInflater inflater;
	private ImageView image;
	private int index_ima = 0;
	private ArrayList<String> ma = new ArrayList<String>();
	List<View> list = new ArrayList<View>();
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				for (int i = 0; i < myList.size(); i++) {
					item = inflater.inflate(R.layout.item, null);
					list.add(item);
					ma.add(myList.get(i).getPicture_url());
				}
				indicator_imgs = new ImageView[ma.size()];
				initIndicator();
				adapter.notifyDataSetChanged();
				break;
			case 1:
				Toast.makeText(getApplicationContext(), (String) msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			case 2: // 网络有问题
				Toast.makeText(getApplicationContext(), "网络未连接",
						Toast.LENGTH_SHORT).show();
				break;
			case 3:

				break;
			case 4:

				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();
		Log.i("111", "width=" + width + "height=" + height);
		initView();
		getdata();

	//地图功能
		
		mLocationClient = ((MyApplication)getApplication()).mLocationClient;
		
		LocationResult = (TextView)findViewById(R.id.tv_city);
		 ((MyApplication)getApplication()).mLocationResult = LocationResult;
		InitLocation();
		mLocationClient.start();
		
		
		 System.out.println("当前城市 ID----" +MyApplication.getCITYID());
		//getdata1();
	}
	private void InitLocation(){
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
		option.setCoorType("gcj02");//返回的定位结果是百度经纬度，默认值gcj02
		int span=1000;
 
		option.setScanSpan(span);//设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}

		// getdata1();
	


	private void getdata1() {
		API.test(MainActivity.this, "18762091710", new HttpCallback(
				MainActivity.this) {
			@Override
			public void onSuccess(Object data) {
				Toast.makeText(getApplicationContext(), "成功", 1000).show();
				Log.e("code", data.toString());
			}

			@Override
			public TypeToken getTypeToken() {
				return null;
			}
		});

	}

	private void getdata() {

		MyApplication
				.getInstance()
				.getClient()
				.post("http://114.215.149.242:18080/ZFMerchant/api/index/sysshufflingfigure/getList",
						new AsyncHttpResponseHandler() {

							@Override
							public void onSuccess(int statusCode,
									Header[] headers, byte[] responseBody) {
								System.out.println("-onSuccess---");
								String responseMsg = new String(responseBody)
										.toString();
								Log.e("LJP", responseMsg);

								Gson gson = new Gson();

								JSONObject jsonobject = null;
								String code = null;
								try {
									jsonobject = new JSONObject(responseMsg);
									code = jsonobject.getString("code");
									int a = jsonobject.getInt("code");
									if (a == Config.CODE) {
										String res = jsonobject
												.getString("result");
										// jsonobject = new JSONObject(res);
										myList = gson
												.fromJson(
														res,
														new TypeToken<List<PicEntity>>() {
														}.getType());
										handler.sendEmptyMessage(0);
									} else {
										code = jsonobject.getString("message");
										Toast.makeText(getApplicationContext(),
												code, 1000).show();
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}

							@Override
							public void onFailure(int statusCode,
									Header[] headers, byte[] responseBody,
									Throwable error) {
								error.printStackTrace();
							}
						});

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
		textsy = (TextView) findViewById(R.id.textsy);
		textghc = (TextView) findViewById(R.id.textghc);
		textmes = (TextView) findViewById(R.id.textmes);
		textwd = (TextView) findViewById(R.id.textwd);
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
		view_pager = (ViewPager) findViewById(R.id.view_pager);

		inflater = LayoutInflater.from(this);
		adapter = new MyAdapter(list);

		view_pager.setAdapter(adapter);
		// 绑定动作监听器：如翻页的动画
		view_pager.setOnPageChangeListener(new MyListener());

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
			//if(LoginActivity.islogin){
				changTabBg();
				im_wd.setBackgroundResource(R.drawable.mine);
				textwd.setTextColor(getResources().getColor(R.color.o));
				if (m_my == null)
					m_my = new m_my();

				getSupportFragmentManager().beginTransaction()
						.replace(R.id.m_fragment, m_my).commit();
			//}
			//else{
			//	Toast.makeText(getApplication(), "请先登陆", 1000).show();
			//}
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
		case R.id.main_rl_lxwm:
			startActivity(new Intent(MainActivity.this, ContactUs.class));

		case R.id.main_rl_zdgl:
			startActivity(new Intent(MainActivity.this, MyApplyDetail.class));
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
		if (Config.shopcar) {
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
		Config.shopcar = false;
	}

	private void initIndicator() {

		ImageView imgView;
		View v = findViewById(R.id.indicator);// 线性水平布局，负责动态调整导航图标

		for (int i = 0; i < ma.size(); i++) {
			imgView = new ImageView(this);
			LinearLayout.LayoutParams params_linear = new LinearLayout.LayoutParams(
					10, 10);
			params_linear.setMargins(7, 10, 7, 10);
			imgView.setLayoutParams(params_linear);
			indicator_imgs[i] = imgView;

			if (i == 0) { // 初始化第一个为选中状态

				indicator_imgs[i]
						.setBackgroundResource(R.drawable.indicator_focused);
			} else {
				indicator_imgs[i].setBackgroundResource(R.drawable.indicator);
			}
			((ViewGroup) v).addView(indicator_imgs[i]);
		}

	}

	/**
	 * 适配器，负责装配 、销毁 数据 和 组件 。
	 */
	private class MyAdapter extends PagerAdapter {

		private List<View> mList;
		private int index;

		public MyAdapter(List<View> list) {
			mList = list;

		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		/**
		 * Return the number of views available.
		 */
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		/**
		 * Remove a page for the given position. 滑动过后就销毁 ，销毁当前页的前一个的前一个的页！
		 * instantiateItem(View container, int position) This method was
		 * deprecated in API level . Use instantiateItem(ViewGroup, int)
		 */
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView(mList.get(position));

		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		/**
		 * Create the page for the given position.
		 */
		@Override
		public Object instantiateItem(final ViewGroup container,
				final int position) {

			View view = mList.get(position);
			image = ((ImageView) view.findViewById(R.id.image));

			ImageCacheUtil.IMAGE_CACHE.get(ma.get(position), image);

			container.removeView(mList.get(position));
			container.addView(mList.get(position));
			setIndex(position);
			// image.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// // TODO Auto-generated method stub
			// // Toast.makeText(getApplicationContext(), index_ima+"----",
			// 1000).show();
			// Intent i=new Intent(AroundDetail.this,VPImage.class);
			// // i.putExtra("image_url", ma.get(index_ima));
			// i.putExtra("index", index_ima);
			// i.putExtra("mal", mal);
			// startActivityForResult(i, 9);
			// }
			// });

			return mList.get(position);
		}
	}

	/**
	 * 动作监听器，可异步加载图片
	 *
	 */
	private class MyListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int state) {
			// TODO Auto-generated method stub
			if (state == 0) {
				// new MyAdapter(null).notifyDataSetChanged();
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int position) {

			// 改变所有导航的背景图片为：未选中
			for (int i = 0; i < indicator_imgs.length; i++) {

				indicator_imgs[i].setBackgroundResource(R.drawable.indicator);

			}

			// 改变当前背景图片为：选中
			index_ima = position;
			indicator_imgs[position]
					.setBackgroundResource(R.drawable.indicator_focused);
		}

	}
}
