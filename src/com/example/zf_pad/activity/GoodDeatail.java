package com.example.zf_pad.activity;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.example.zf_pad.BaseActivity;
import com.example.zf_pad.Config;
import com.example.zf_pad.MyApplication;
import com.epalmpay.userPad.R;
import com.example.zf_pad.aadpter.ButtonGridviewAdapter;
import com.example.zf_pad.aadpter.HuilvAdapter;
import com.example.zf_pad.entity.ApplyneedEntity;
import com.example.zf_pad.entity.ChanelEntitiy;
import com.example.zf_pad.entity.FactoryEntity;
import com.example.zf_pad.entity.GoodinfoEntity;
import com.example.zf_pad.entity.PosEntity;
import com.example.zf_pad.entity.other_rate;
import com.example.zf_pad.entity.tDates;
import com.example.zf_pad.fragment.F_good_detail;
import com.example.zf_pad.fragment.Good_detail_apply;
import com.example.zf_pad.fragment.Good_detail_commet;
import com.example.zf_pad.fragment.Good_detail_zd;
import com.example.zf_pad.popwindow.FactoryPopWindow;
import com.example.zf_pad.popwindow.SetPopWindow;
import com.example.zf_pad.trade.common.DialogUtil;
import com.example.zf_pad.trade.entity.GriviewEntity;
import com.example.zf_pad.util.ImageCacheUtil;
import com.example.zf_pad.util.ScrollViewWithGView;
import com.example.zf_pad.util.ScrollViewWithListView;
import com.example.zf_pad.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

@SuppressLint("ResourceAsColor") public class GoodDeatail extends FragmentActivity implements OnClickListener {
	private Button setting_btn_clear1, setting_btn_clear;
	private int id;
	private TextView tvc_zx, tvc_qy, eventsFinshTime, tv_detail, name,
	creat_tv, location, tv_time, tv_tel2;
	private LinearLayout titleback_linear_back;
	private ImageView image, search2, fac_img;
	private RelativeLayout ri_tel;
	private ArrayList<String> ma = new ArrayList<String>();
	private ArrayList<String> mal = new ArrayList<String>();
	private ViewPager view_pager;
	private MyAdapter adapter;
	private ImageView[] indicator_imgs;
	private View item;
	private LayoutInflater inflater;
	private RelativeLayout rl_imgs, rela_loc;
	private int index_ima = 0;
	private GoodinfoEntity gfe;
	private String chanel = "ͨ��3";
	private ArrayList<String> arelist = new ArrayList<String>();
	private int commentsCount=0;
	FactoryEntity factoryEntity;
	private TextView tv_bug, tv_lea, tv_title, content1, tv_pp, tv_xh, tv_ys,
	tv_price, tv_lx, tv_sjhttp, tv_spxx, fac_detai, ppxx, wkxx, dcxx,
	tv_qgd, tv_jm;
	private TextView countShopCar;
	private ScrollViewWithListView pos_lv1;
	private HuilvAdapter lvAdapter;
	private ArrayList<ChanelEntitiy> celist = new ArrayList<ChanelEntitiy>();
	private ArrayList<ChanelEntitiy> celist2 = new ArrayList<ChanelEntitiy>();
	private String phoneNumber, locName;
	private float lat, lng;
	private int paychannelId, goodId, quantity;
	List<View> list = new ArrayList<View>();
	private ScrollViewWithGView gview, gview1;
	private ButtonGridviewAdapter buttonAdapter;
	List<GriviewEntity> User_button = new ArrayList<GriviewEntity>();
	private Boolean islea = false;
	private List<String> piclist;
	private int opening_cost;
	private int all_price;
	private String tdname;
	private Handler handler = new Handler() {


		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				for (int i = 0; i < ma.size(); i++) {
					item = inflater.inflate(R.layout.item, null);
					list.add(item);
				}
				indicator_imgs = new ImageView[ma.size()];
				initIndicator();
				adapter.notifyDataSetChanged();
				if(gfe.getTitle()!=null)
					tv_title.setText(gfe.getTitle());
				content1.setText(gfe.getSecond_title());
				tv_pp.setText(gfe.getGood_brand()+gfe.getModel_number());
				// tv_xh.setText(gfe.getModel_number());
				// tv_ys.setText("已售"+gfe.getVolume_number());
				//tv_price.setText("��" + ((double)gfe.getPrice())/100);
				tv_lx.setText(gfe.getCategory());
				tv_sjhttp.setText(factoryEntity.getWebsite_url());
				// tv_spxx.setText(gfe.getDescription() );
				fac_detai.setText(factoryEntity.getDescription());
				// ppxx.setText(gfe.getModel_number() );
				// wkxx.setText(gfe.getShell_material() );
				// dcxx.setText(gfe.getBattery_info());
				// tv_qgd.setText(gfe.getSign_order_way());
				// tv_jm.setText(gfe.getEncrypt_card_way());
				// lvAdapter.notifyDataSetChanged();
				tv_pl.setText("����"+"("+commentsCount+")");
				if (islea == false) {
					all_price = gfe.getRetail_price()+opening_cost;
					tv_price.setText("�� "+StringUtil.getMoneyString(gfe.getRetail_price()+opening_cost));

				}else {
					//����
					all_price = gfe.getLease_deposit()+opening_cost;
					tv_price.setText("�� "+StringUtil.getMoneyString(gfe.getLease_deposit()+opening_cost));
					tv_zd.setVisibility(View.GONE);
				}
				String string=" ��" + df.format((double)(gfe.getPrice()) / 100)+" ";
				SpannableString sp = new SpannableString(string);
				sp.setSpan(new StrikethroughSpan(), 0, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

				jjyj.setText(sp);
				jjxj.setText("�� "+StringUtil.getMoneyString(gfe.getRetail_price()));
				ktfy.setText("�� "+StringUtil.getMoneyString(opening_cost));
				if(gfe.isHas_lease()){
					Config.canzl=false;
				}else{
					tv_zd.setVisibility(View.GONE);
					Config.canzl=true;
				}
				if(gfe.getQuantity()<=0){
					setting_btn_clear.setText("ȱ��");
					setting_btn_clear1.setVisibility(View.GONE);
					setting_btn_clear.setBackgroundColor(R.color.qhgray);
					View view=(View)findViewById(R.id.is_show);
					view.setVisibility(View.VISIBLE);
				}
				break;
			case 1:
				Toast.makeText(getApplicationContext(), (String) msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			case 2: //

				break;
			case 3:

				break;
			case 4:

				break;
			}
		}
	};
	private Intent i;
	private LinearLayout ll_Factory;
	private TextView tv_pl;
	private LinearLayout ll_sc;
	private TextView tv_zd;
	private TextView jjxj;
	private TextView jjyj;
	private TextView ktfy;
	private DecimalFormat df;
	private TextView tv_jj;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.good_detail);
		id = getIntent().getIntExtra("id", 0);
		df = (DecimalFormat) NumberFormat
				.getInstance();
		df.applyPattern("0.00");
		Config.goodId=id;
		Config.gid=id;
		innitView();
		getdata();
		System.out.println("-Xlistview--" + id);
	}

	private void innitView() {
		tv_jj = (TextView)findViewById(R.id.tv_jj);
		jjyj = (TextView)findViewById(R.id.jjyj);
		jjxj = (TextView)findViewById(R.id.jjxj);
		ktfy = (TextView)findViewById(R.id.ktfy);
		countShopCar = (TextView) findViewById(R.id.countShopCar);
		setting_btn_clear = (Button) findViewById(R.id.setting_btn_clear);
		setting_btn_clear.setOnClickListener(this);
		setting_btn_clear1 = (Button) findViewById(R.id.setting_btn_clear1);
		setting_btn_clear1.setOnClickListener(this);
		tv_lea = (TextView) findViewById(R.id.tv_lea);
		tv_lea.setOnClickListener(this);
		tv_bug = (TextView) findViewById(R.id.tv_bug);
		tv_bug.setOnClickListener(this);
		gview1 = (ScrollViewWithGView) findViewById(R.id.gv1);
		rl_imgs = (RelativeLayout) findViewById(R.id.rl_imgs);
		LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) rl_imgs
				.getLayoutParams();
		linearParams.width = Config.ScreenWidth / 2;
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		float density = dm.density;
		linearParams.height = (int) (Config.ScreenHeight - density*180);

		rl_imgs.setLayoutParams(linearParams);
		/*	ll_sc = (LinearLayout)findViewById(R.id.ll_sc);
		LinearLayout.LayoutParams linearParams1 = (LinearLayout.LayoutParams) ll_sc
				.getLayoutParams();
		linearParams1.height = (int) (Config.ScreenHeight - density*50);
		ll_sc.setLayoutParams(linearParams1);*/
		view_pager = (ViewPager) findViewById(R.id.view_pager);
		inflater = LayoutInflater.from(this);
		adapter = new MyAdapter(list);
		view_pager.setAdapter(adapter);
		view_pager.setOnPageChangeListener(new MyListener());

		pos_lv1 = (ScrollViewWithListView) findViewById(R.id.pos_lv1);

		titleback_linear_back = (LinearLayout) findViewById(R.id.titleback_linear_back);
		titleback_linear_back.setOnClickListener(this);
		search2 = (ImageView) findViewById(R.id.search2);
		search2.setOnClickListener(this);

		// tv_ys=(TextView) findViewById(R.id.tv_y1s);
		// tv_xh=(TextView) findViewById(R.id.tv_xh);
		tv_title = (TextView) findViewById(R.id.tv_title);
		content1 = (TextView) findViewById(R.id.content1);
		tv_pp = (TextView) findViewById(R.id.tv_PP);
		tv_price = (TextView) findViewById(R.id.tv_price);
		tv_lx = (TextView) findViewById(R.id.tv_lx);
		tv_sjhttp = (TextView) findViewById(R.id.tv_sjhttp);
		tv_sjhttp.setOnClickListener(this);
		// tv_spxx=(TextView) findViewById(R.id.tv_spxx);
		fac_detai = (TextView) findViewById(R.id.fac_detai);
		fac_img = (ImageView) findViewById(R.id.fac_img);

		TextView tv_ms = (TextView) findViewById(R.id.tv_ms);
		tv_ms.setOnClickListener(this);
		TextView tv_kt = (TextView) findViewById(R.id.tv_kt);
		tv_kt.setOnClickListener(this);
		tv_pl = (TextView) findViewById(R.id.tv_pl);
		tv_pl.setOnClickListener(this);
		tv_zd = (TextView) findViewById(R.id.tv_zd);
		tv_zd.setOnClickListener(this);
		TextView tv_jy = (TextView) findViewById(R.id.tv_jy);
		tv_jy.setOnClickListener(this);
		setting_btn_clear1 = (Button) findViewById(R.id.setting_btn_clear1);
		setting_btn_clear1.setOnClickListener(this);
		TextView tv_pic = (TextView) findViewById(R.id.tv_pic);
		tv_pic.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.mer_detail:
			FactoryPopWindow fact = new FactoryPopWindow(this,
					factoryEntity.getLogo_file_path(), factoryEntity.getName(),
					factoryEntity.getDescription());
			fact.showAtLocation(findViewById(R.id.main), Gravity.CENTER
					| Gravity.CENTER, 0, 0);
			break;
		case R.id.tv_bug:
			all_price = gfe.getRetail_price()+opening_cost;
			tv_price.setText("�� "+StringUtil.getMoneyString(gfe.getRetail_price()+opening_cost));
			islea = false;			
			setting_btn_clear1.setClickable(true);
			if (gfe.getQuantity()<=0) {
				setting_btn_clear.setText("ȱ��");
			}else {
				setting_btn_clear.setText("��������");
			}
			setting_btn_clear1.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.bg_shape));
			setting_btn_clear1.setTextColor(getResources().getColor(
					R.color.bgtitle));
			tv_bug.setTextColor(getResources().getColor(R.color.white));
			tv_lea.setTextColor(getResources().getColor(R.color.text292929));
			tv_lea.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.send_out_goods_shape));
			//tv_bug.setBackgroundDrawable(getResources().getDrawable(
			//		R.drawable.bg_shape));	
			tv_bug.setBackgroundColor(getResources().getColor(R.color.bgtitle));
			jjxj.setText("�� "+StringUtil.getMoneyString(gfe.getRetail_price()));
			tv_jj.setText("�����ּ�");
			break;
		case R.id.tv_lea:
			all_price = gfe.getLease_deposit()+opening_cost;
			tv_price.setText("�� "+StringUtil.getMoneyString(gfe.getLease_deposit()+opening_cost));

			islea = true;
			setting_btn_clear1.setClickable(false);
			if (gfe.getQuantity()<=0) {
				setting_btn_clear.setText("ȱ��");
			}else {
				setting_btn_clear.setText("��������");
			}
			setting_btn_clear1.setTextColor(getResources().getColor(
					R.color.bg0etitle));
			setting_btn_clear1.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.bg0e_shape));
			tv_bug.setTextColor(getResources().getColor(R.color.text292929));
			tv_lea.setTextColor(getResources().getColor(R.color.white));
			//tv_lea.setBackgroundDrawable(getResources().getDrawable(
			//		R.drawable.bg_shape));
			tv_lea.setBackgroundColor(getResources().getColor(R.color.bgtitle));
			tv_bug.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.send_out_goods_shape));
			jjxj.setText("�� "+StringUtil.getMoneyString(gfe.getLease_deposit()));
			tv_jj.setText("����Ѻ��");
			break;
		case R.id.setting_btn_clear1:
			if (Config.CheckIsLogin(GoodDeatail.this)) {
				if (null != gfe) {
					addGood();
				}
			}
			break;
		case R.id.tv_zd:
			i = new Intent(GoodDeatail.this, GoodDeatilMore.class);
			i.putExtra("type", 3);
			i.putExtra("comments", commentsCount);
			startActivity(i);
			break;
		case R.id.tv_ms:
			i = new Intent(GoodDeatail.this, GoodDeatilMore.class);
			i.putExtra("type", 0);
			i.putExtra("comments", commentsCount);
			startActivity(i);
			break;
		case R.id.tv_kt:
			i = new Intent(GoodDeatail.this, GoodDeatilMore.class);
			i.putExtra("type", 1);
			i.putExtra("comments", commentsCount);
			startActivity(i);
			break;
		case R.id.tv_pl:
			i = new Intent(GoodDeatail.this, GoodDeatilMore.class);
			i.putExtra("type", 2);
			i.putExtra("comments", commentsCount);

			startActivity(i);
			break;
		case R.id.tv_jy:
			i = new Intent(GoodDeatail.this, GoodDeatilMore.class);
			i.putExtra("type", 4);
			i.putExtra("comments", commentsCount);
			startActivity(i);
			break;
		case R.id.tv_pic:
			i = new Intent(GoodDeatail.this, GoodDeatilMore.class);
			i.putExtra("type", 5);
			i.putExtra("commets", commentsCount);
			startActivity(i);
			break;
		case R.id.titleback_linear_back:
			finish();
			break;
		case R.id.search2:
			if (Config.CheckIsLogin(GoodDeatail.this)) {
				Config.shopcar = true;
				Intent i = new Intent(GoodDeatail.this, MainActivity.class);
				startActivity(i);
			}
			break;
		case R.id.tv_sjhttp:
			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			Uri content_url = Uri.parse(tv_sjhttp.getText().toString());
			intent.setData(content_url);
			startActivity(intent);
			break;
		case R.id.setting_btn_clear: // tv_comment
			if(gfe.getQuantity()<=0){
				Toast.makeText(getApplicationContext(), "�ܱ�Ǹ������Ʒ���ڼӽ�������", 1000).show();
				return;
			}
			if (Config.CheckIsLogin(GoodDeatail.this)) {
				if (islea) {

					Intent i21 = new Intent(GoodDeatail.this, LeaseConfirm.class);
					i21.putExtra("getTitle", gfe.getTitle());
					i21.putExtra("price", gfe.getLease_deposit());
					i21.putExtra("model", gfe.getModel_number());
					i21.putExtra("chanel", tdname);
					i21.putExtra("paychannelId", paychannelId);
					i21.putExtra("commets", commentsCount);
					i21.putExtra("goodId", gfe.getId());
					i21.putExtra("brand", gfe.getGood_brand()+gfe.getModel_number());
					i21.putExtra("hpsf", opening_cost);
					if(piclist.size()!=0){
						i21.putExtra("piclist",piclist.get(0));
					}
					startActivity(i21);
				} else {

					Intent i2 = new Intent(GoodDeatail.this, GoodConfirm.class);
					i2.putExtra("getTitle", gfe.getTitle());
					i2.putExtra("price", gfe.getRetail_price()+opening_cost);
					i2.putExtra("model", gfe.getModel_number());
					i2.putExtra("chanel", tdname);
					i2.putExtra("paychannelId", paychannelId);
					i2.putExtra("goodId", gfe.getId());
					i2.putExtra("hpsf", opening_cost);
					i2.putExtra("brand", gfe.getGood_brand()+gfe.getModel_number());
					if(piclist.size()!=0){
						i2.putExtra("piclist",piclist.get(0));
					}
					startActivity(i2);
				}
			}

		default:
			break;
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		if (Config.countShopCar != 0) {
			countShopCar.setVisibility(View.VISIBLE);
			countShopCar.setText(Config.countShopCar+"");
		}else {
			countShopCar.setVisibility(View.GONE);
		}
	}
	private void getdata() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("goodId", id);
		params.put("city_id", MyApplication.getCITYID());

		JSONObject jsonParams = new JSONObject(params);
		HttpEntity entity;
		try {
			entity = new StringEntity(jsonParams.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {


			return;
		}

		MyApplication
		.getInstance()
		.getClient()
		.post(getApplicationContext(),Config.GOODDETAIL, null,entity,"application/json",
				new AsyncHttpResponseHandler() {
			private Dialog loadingDialog;

			@Override
			public void onStart() {

				super.onStart();
				loadingDialog = DialogUtil.getLoadingDialg(GoodDeatail.this);
				loadingDialog.show();
			}

			@Override
			public void onFinish() {

				super.onFinish();
				loadingDialog.dismiss();
			}

			@Override
			public void onSuccess(int statusCode,
					Header[] headers, byte[] responseBody) {

				String userMsg = new String(responseBody)
				.toString();
				//innitView();
				Log.i("ljp", userMsg);
				Gson gson = new Gson();
				// EventEntity
				JSONObject jsonobject = null;
				int code = 0;
				try {
					jsonobject = new JSONObject(userMsg);
					code = jsonobject.getInt("code");
					if (code == -2) {
						Intent i = new Intent(getApplication(),
								LoginActivity.class);
						startActivity(i);
					} else if (code == 1) {

						String res = jsonobject
								.getString("result");
						jsonobject = new JSONObject(res);

						piclist = gson.fromJson(
								jsonobject
								.getString("goodPics"),
								new TypeToken<List<String>>() {
								}.getType());
						User_button = gson.fromJson(
								jsonobject
								.getString("payChannelList"),
								new TypeToken<List<GriviewEntity>>() {
								}.getType());
						buttonAdapter = new ButtonGridviewAdapter(
								GoodDeatail.this, User_button,
								0);

						gview1.setAdapter(buttonAdapter);
						gview1.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(
									AdapterView<?> arg0,
									View arg1, int arg2,
									long arg3) {


								buttonAdapter.setIndex(arg2);
								getdataByChanel(User_button
										.get(arg2).getId());
								buttonAdapter
								.notifyDataSetChanged();
							}
						});
						Config.myList = gson.fromJson(
								jsonobject
								.getString("relativeShopList"),
								new TypeToken<List<PosEntity>>() {
								}.getType());
						gfe = gson.fromJson(
								jsonobject
								.getString("goodinfo"),
								new TypeToken<GoodinfoEntity>() {
								}.getType());
						factoryEntity = gson.fromJson(
								jsonobject.getString("factory"),
								new TypeToken<FactoryEntity>() {
								}.getType());


						commentsCount = jsonobject
								.getInt("commentsCount");
						Config.gfe = gfe;
						String res2 = jsonobject
								.getString("paychannelinfo");
						jsonobject = new JSONObject(res2);
						paychannelId = jsonobject.getInt("id");
						factoryEntity=gson.fromJson(jsonobject.getString("pcfactory"), new TypeToken<FactoryEntity>() {
						}.getType());
						ImageCacheUtil.IMAGE_CACHE.get(
								factoryEntity
								.getLogo_file_path(),
								fac_img);
						Config.celist = gson.fromJson(
								jsonobject
								.getString("standard_rates"),
								new TypeToken<List<ChanelEntitiy>>() {
								}.getType());
						Config.tDates = gson.fromJson(
								jsonobject.getString("tDates"),
								new TypeToken<List<tDates>>() {
								}.getType());
						Config.other_rate = gson.fromJson(
								jsonobject
								.getString("other_rate"),
								new TypeToken<List<other_rate>>() {
								}.getType());
						Config.pub = gson.fromJson(
								jsonobject
								.getString("requireMaterial_pub"),
								new TypeToken<List<ApplyneedEntity>>() {
								}.getType());
						;
						Config.single = gson.fromJson(
								jsonobject
								.getString("requireMaterial_pra"),
								new TypeToken<List<ApplyneedEntity>>() {
								}.getType());

						System.out.println("``celist`"
								+ celist.size());
						lvAdapter = new HuilvAdapter(
								GoodDeatail.this, celist);
						// pos_lv1.setAdapter(lvAdapter);

						for (int i = 0; i < piclist.size(); i++) {
							ma.add(piclist.get(i));
						}
						// User_button=gson.fromJson(jsonobject.getString("payChannelList"),
						// new TypeToken<List<GriviewEntity>>()
						// {
						// }.getType());
						// buttonAdapter=new
						// ButtonGridviewAdapter(GoodDeatail.this,
						// User_button,0);
						// gview1.setAdapter(buttonAdapter);

						arelist = gson.fromJson(
								jsonobject
								.getString("supportArea"),
								new TypeToken<List<String>>() {
								}.getType());
						String a = "";
						for (int i = 0; i < arelist.size(); i++) {
							a = a + arelist.get(i);
						}

						Config.suportare = a;

						if (jsonobject
								.getBoolean("support_cancel_flag")) {
							Config.suportcl = "֧��";

						} else {
							Config.suportcl = "��֧��";
						}
						Config.apply=jsonobject.getString("opening_datum");
						opening_cost = jsonobject.getInt("opening_cost");
						Config.support_type=jsonobject.getBoolean("support_type");
						tdname = jsonobject.getString("name");

						celist2 = gson.fromJson(
								jsonobject.getString("tDates"),
								new TypeToken<List<ChanelEntitiy>>() {
								}.getType());
						Config.celist2 = celist2;
						Config.tv_sqkt = jsonobject
								.getString("opening_requirement");
						ll_Factory = (LinearLayout) findViewById(R.id.mer_detail);
						ll_Factory
						.setOnClickListener(GoodDeatail.this);
						handler.sendEmptyMessage(0);
					} else {
						Toast.makeText(
								getApplicationContext(),
								jsonobject.getString("message"),
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {

					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(int statusCode,
					Header[] headers, byte[] responseBody,
					Throwable error) {

			}
		});

	}

	private void addGood() {

		//RequestParams params = new RequestParams();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customerId", MyApplication.NewUser.getId());
		params.put("goodId", gfe.getId());
		// paychannelId
		params.put("paychannelId", paychannelId);
		//params.setUseJsonStreamer(true);
		JSONObject jsonParams = new JSONObject(params);
		HttpEntity entity;
		try {
			entity = new StringEntity(jsonParams.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return;
		}
		MyApplication.getInstance().getClient()
		.post(getApplicationContext(),Config.goodadd, null,entity,"application/json", new AsyncHttpResponseHandler(){
			//.post(Config.goodadd, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {

				String userMsg = new String(responseBody).toString();

				Log.i("ljp", userMsg);
				Gson gson = new Gson();
				// EventEntity
				JSONObject jsonobject = null;
				int code = 0;
				try {
					jsonobject = new JSONObject(userMsg);
					code = jsonobject.getInt("code");
					if (code == -2) {
						Intent i = new Intent(getApplication(),
								LoginActivity.class);
						startActivity(i);
					} else if (code == 1) { 
						Config.countShopCar = Config.countShopCar + 1;

						if (Config.countShopCar != 0) {
							countShopCar.setVisibility(View.VISIBLE);
							countShopCar.setText(Config.countShopCar+"");
						}else {
							countShopCar.setVisibility(View.GONE);
						}

						Toast.makeText(getApplicationContext(),
								"������Ʒ�ɹ�", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getApplicationContext(),
								jsonobject.getString("message"),
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {

					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {


			}
		});

	}

	private void initIndicator() {

		ImageView imgView;
		View v = findViewById(R.id.indicator);// 线�?�水平布�?，负责动态调整导航图�?

		for (int i = 0; i < ma.size(); i++) {
			imgView = new ImageView(this);
			LinearLayout.LayoutParams params_linear = new LinearLayout.LayoutParams(
					10, 10);
			params_linear.setMargins(7, 10, 7, 10);
			imgView.setLayoutParams(params_linear);
			indicator_imgs[i] = imgView;

			if (i == 0) { // 初始化第�?个为选中状�??

				indicator_imgs[i]
						.setBackgroundResource(R.drawable.indicator_focused);
			} else {
				indicator_imgs[i].setBackgroundResource(R.drawable.indicator);
			}
			((ViewGroup) v).addView(indicator_imgs[i]);
		}

	}

	/**
	 * 适配器，负责装配 、销�? 数据 �? 组件 �?
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

			return mList.size();
		}

		/**
		 * Remove a page for the given position. 滑动过后就销�?
		 * ，销毁当前页的前�?个的前一个的页！ instantiateItem(View container, int
		 * position) This method was deprecated in API level . Use
		 * instantiateItem(ViewGroup, int)
		 */
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {

			container.removeView(mList.get(position));

		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {

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
			image.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(GoodDeatail.this,GoodDetailImgs.class);
					intent.putStringArrayListExtra("ma", (ArrayList<String>) ma);
					intent.putExtra("position_detail", position);
					startActivity(intent);
				}
			});

			return mList.get(position);
		}

	}

	private void getdataByChanel(int pcid) {

		//RequestParams params = new RequestParams();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pcid", pcid);
		System.out.println("---֧��ͨ��ID--" + pcid);
		JSONObject jsonParams = new JSONObject(params);
		HttpEntity entity;
		try {
			entity = new StringEntity(jsonParams.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return;
		}
		//params.setUseJsonStreamer(true);
		MyApplication
		.getInstance()
		.getClient()
		.post(getApplicationContext(),Config.paychannel_info, null,entity,"application/json", new AsyncHttpResponseHandler(){
			//		.post(Config.paychannel_info, params,
			//				new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode,
					Header[] headers, byte[] responseBody) {

				String userMsg = new String(responseBody)
				.toString();

				Log.i("ljp", userMsg);
				Gson gson = new Gson();
				// EventEntity
				JSONObject jsonobject = null;
				int code = 0;
				try {
					jsonobject = new JSONObject(userMsg);
					code = jsonobject.getInt("code");
					if (code == -2) {
						Intent i = new Intent(getApplication(),
								LoginActivity.class);
						startActivity(i);
					} else if (code == 1) {
						String res = jsonobject
								.getString("result");
						jsonobject = new JSONObject(res);

						Config.tv_sqkt = jsonobject
								.getString("opening_requirement");
						ImageCacheUtil.IMAGE_CACHE.get(
								factoryEntity.getLogo_file_path(),
								fac_img); 
						factoryEntity = gson.fromJson(jsonobject.getString("pcfactory"), new TypeToken<FactoryEntity>() {
						}.getType());
						if(factoryEntity.getLogo_file_path() != null){
							ImageCacheUtil.IMAGE_CACHE.get(factoryEntity.getLogo_file_path(),
									fac_img);
						}

						tv_sjhttp.setText(factoryEntity.getWebsite_url() );
						fac_detai.setText(factoryEntity.getDescription() );
						Config.celist = gson.fromJson(
								jsonobject
								.getString("standard_rates"),
								new TypeToken<List<ChanelEntitiy>>() {
								}.getType());
						Config.tDates = gson.fromJson(
								jsonobject.getString("tDates"),
								new TypeToken<List<tDates>>() {
								}.getType());
						Config.other_rate = gson.fromJson(
								jsonobject
								.getString("other_rate"),
								new TypeToken<List<other_rate>>() {
								}.getType());
						Config.pub = gson.fromJson(
								jsonobject
								.getString("requireMaterial_pub"),
								new TypeToken<List<ApplyneedEntity>>() {
								}.getType());
						;
						Config.single = gson.fromJson(
								jsonobject
								.getString("requireMaterial_pra"),
								new TypeToken<List<ApplyneedEntity>>() {
								}.getType());

						System.out.println("``celist`"
								+ celist.size());
						lvAdapter = new HuilvAdapter(
								GoodDeatail.this, celist);
						// pos_lv1.setAdapter(lvAdapter);

						// User_button=gson.fromJson(jsonobject.getString("payChannelList"),
						// new TypeToken<List<GriviewEntity>>()
						// {
						// }.getType());
						// buttonAdapter=new
						// ButtonGridviewAdapter(GoodDeatail.this,
						// User_button,0);
						// gview1.setAdapter(buttonAdapter);

						arelist = gson.fromJson(
								jsonobject
								.getString("supportArea"),
								new TypeToken<List<String>>() {
								}.getType());
						String a = "";
						for (int i = 0; i < arelist.size(); i++) {
							a = a + arelist.get(i);
						}

						Config.suportare = a;

						if (jsonobject
								.getBoolean("support_cancel_flag")) {
							Config.suportcl = "֧��";

						} else {
							Config.suportcl = "��֧��";
						}
						opening_cost=jsonobject.getInt("opening_cost");
						Config.support_type=jsonobject.getBoolean("support_type");
						ktfy.setText("�� "+StringUtil.getMoneyString(opening_cost));
						tdname = jsonobject.getString("name");
						if (islea == false) {
							//����
							all_price = gfe.getRetail_price()+opening_cost;
							tv_price.setText("�� "+StringUtil.getMoneyString(gfe.getRetail_price()+opening_cost));
						}else {
							//����
							all_price = gfe.getLease_deposit()+opening_cost;
							tv_price.setText("�� "+StringUtil.getMoneyString(gfe.getLease_deposit()+opening_cost));
						}
					} else {
						Toast.makeText(
								getApplicationContext(),
								jsonobject.getString("message"),
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {

					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(int statusCode,
					Header[] headers, byte[] responseBody,
					Throwable error) {


			}
		});

	}

	private class MyListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int state) {

			if (state == 0) {
				// new MyAdapter(null).notifyDataSetChanged();
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {


		}

		@Override
		public void onPageSelected(int position) {

			for (int i = 0; i < indicator_imgs.length; i++) {

				indicator_imgs[i].setBackgroundResource(R.drawable.indicator);

			}

			index_ima = position;
			indicator_imgs[position]
					.setBackgroundResource(R.drawable.indicator_focused);
		}

	}

}
