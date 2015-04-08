package com.example.zf_pad.activity;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
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
import com.example.zf_pad.R;
import com.example.zf_pad.aadpter.ButtonGridviewAdapter;
import com.example.zf_pad.aadpter.HuilvAdapter;
import com.example.zf_pad.entity.ApplyneedEntity;
import com.example.zf_pad.entity.ChanelEntitiy;
import com.example.zf_pad.entity.FactoryEntity;
import com.example.zf_pad.entity.GoodinfoEntity;
import com.example.zf_pad.entity.PosEntity;
import com.example.zf_pad.entity.other_rate;
import com.example.zf_pad.entity.tDates;
import com.example.zf_pad.fragment.f_good_detail;
import com.example.zf_pad.fragment.good_detail_apply;
import com.example.zf_pad.fragment.good_detail_commet;
import com.example.zf_pad.fragment.good_detail_zd;
import com.example.zf_pad.popwindow.FactoryPopWindow;
import com.example.zf_pad.popwindow.SetPopWindow;
import com.example.zf_pad.trade.entity.GriviewEntity;
import com.example.zf_pad.util.ImageCacheUtil;
import com.example.zf_pad.util.ScrollViewWithGView;
import com.example.zf_pad.util.ScrollViewWithListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class GoodDeatail extends FragmentActivity implements OnClickListener {
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
	GoodinfoEntity gfe;
	private String chanel = "Õ®µ¿3";
	private ArrayList<String> arelist = new ArrayList<String>();
	private int commentsCount;
	FactoryEntity factoryEntity;
	private TextView tv_bug, tv_lea, tv_title, content1, tv_pp, tv_xh, tv_ys,
			tv_price, tv_lx, tv_sjhttp, tv_spxx, fac_detai, ppxx, wkxx, dcxx,
			tv_qgd, tv_jm;
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
				tv_title.setText(gfe.getTitle());
				content1.setText(gfe.getSecond_title());
				tv_pp.setText(gfe.getGood_brand());
				// tv_xh.setText(gfe.getModel_number());
				// tv_ys.setText("Â∑≤ÂîÆ"+gfe.getVolume_number());
				tv_price.setText("£§" + gfe.getPrice());
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.good_detail);
	
		id = getIntent().getIntExtra("id", 0);
		innitView();
		getdata();
		System.out.println("-Xlistview--" + id);
	}

	private void innitView() {
		
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
		TextView tv_pl = (TextView) findViewById(R.id.tv_pl);
		tv_pl.setOnClickListener(this);
		TextView tv_zd = (TextView) findViewById(R.id.tv_zd);
		tv_zd.setOnClickListener(this);
		TextView tv_jy = (TextView) findViewById(R.id.tv_jy);
		tv_jy.setOnClickListener(this);
		setting_btn_clear1 = (Button) findViewById(R.id.setting_btn_clear1);
		setting_btn_clear1.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.mer_detail:
			FactoryPopWindow fact = new FactoryPopWindow(this,factoryEntity.getLogo_file_path(),factoryEntity.getName(),factoryEntity.getDescription());
			fact.showAtLocation(findViewById(R.id.main), Gravity.CENTER
					| Gravity.CENTER, 0, 0);
			break;
		case R.id.tv_bug:
			islea = false;
			setting_btn_clear1.setClickable(true);
			setting_btn_clear.setText("¡¢º¥π∫¬Ú");
			setting_btn_clear1.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.bg_shape));
			setting_btn_clear1.setTextColor(getResources().getColor(
					R.color.bgtitle));
			tv_bug.setTextColor(getResources().getColor(R.color.bgtitle));
			tv_lea.setTextColor(getResources().getColor(R.color.text292929));
			tv_lea.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.send_out_goods_shape));
			tv_bug.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.bg_shape));
			break;
		case R.id.tv_lea:
			// tv_bug
			islea = true;
			setting_btn_clear1.setClickable(false);
			setting_btn_clear.setText("¡¢º¥◊‚¡ﬁ");
			setting_btn_clear1.setTextColor(getResources().getColor(
					R.color.bg0etitle));
			setting_btn_clear1.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.bg0e_shape));
			tv_bug.setTextColor(getResources().getColor(R.color.text292929));
			tv_lea.setTextColor(getResources().getColor(R.color.bgtitle));
			tv_lea.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.bg_shape));
			tv_bug.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.send_out_goods_shape));
			break;
		case R.id.setting_btn_clear1:
			addGood();
			break;
		case R.id.tv_zd:
			i = new Intent(GoodDeatail.this, GoodDeatilMore.class);
			i.putExtra("type", 3);
			startActivity(i);
			break;
		case R.id.tv_ms:
			i = new Intent(GoodDeatail.this, GoodDeatilMore.class);
			i.putExtra("type", 0);
			startActivity(i);
			break;
		case R.id.tv_kt:
			i = new Intent(GoodDeatail.this, GoodDeatilMore.class);
			i.putExtra("type", 1);
			startActivity(i);
			break;
		case R.id.tv_pl:
			i = new Intent(GoodDeatail.this, GoodDeatilMore.class);
			i.putExtra("type", 2);
			startActivity(i);
			break;
		case R.id.tv_jy:
			i = new Intent(GoodDeatail.this, GoodDeatilMore.class);
			i.putExtra("type", 4);
			startActivity(i);
			break;
		case R.id.titleback_linear_back:
			finish();
			break;
		case R.id.search2:
			Config.shopcar=true;
			Intent i = new Intent(GoodDeatail.this, MainActivity.class);
			startActivity(i);
			break;
		case R.id.tv_sjhttp:
			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			Uri content_url = Uri.parse(tv_sjhttp.getText().toString());
			intent.setData(content_url);
			startActivity(intent);
			break;
		case R.id.setting_btn_clear: // tv_comment
			if (islea) {

				Intent i21 = new Intent(GoodDeatail.this, LeaseConfirm.class);
				i21.putExtra("getTitle", gfe.getTitle());
				i21.putExtra("price", gfe.getPrice());
				i21.putExtra("model", gfe.getModel_number());
				i21.putExtra("chanel", chanel);
				i21.putExtra("paychannelId", paychannelId);
				i21.putExtra("goodId", gfe.getId());
				startActivity(i21);
			} else {

				Intent i2 = new Intent(GoodDeatail.this, GoodConfirm.class);
				i2.putExtra("getTitle", gfe.getTitle());
				i2.putExtra("price", gfe.getPrice());
				i2.putExtra("model", gfe.getModel_number());
				i2.putExtra("chanel", chanel);
				i2.putExtra("paychannelId", paychannelId);
				i2.putExtra("goodId", gfe.getId());
				startActivity(i2);
			}
		default:
			break;
		}
	}

	private void getdata() {
		RequestParams params = new RequestParams();
		params.put("goodId", id);
		params.put("city_id", MyApplication.getCITYID());

		params.setUseJsonStreamer(true);
		MyApplication
				.getInstance()
				.getClient()
				.post(Config.GOODDETAIL, params,
						new AsyncHttpResponseHandler() {

							@Override
							public void onSuccess(int statusCode,
									Header[] headers, byte[] responseBody) {
								// TODO Auto-generated method stub
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
										List<String> piclist = gson.fromJson(
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
												// TODO Auto-generated method
												// stub
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
										// ImageCacheUtil.IMAGE_CACHE.get(
										// factoryEntity.getLogo_file_path(),
										// fac_img); ÂõæÁâáË∑ØÂæÑÂæÖÂÆö
										//
										commentsCount = jsonobject
												.getInt("commentsCount");
										Config.gfe = gfe;
										String res2 = jsonobject
												.getString("paychannelinfo");
										jsonobject = new JSONObject(res2);
										 paychannelId=jsonobject.getInt("id");
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
										if (jsonobject
												.getBoolean("support_type")) {
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
										} else {
											Config.suportare = "≤ª÷ß≥÷";
											
										}
										if (jsonobject
												.getBoolean("support_cancel_flag")) {
											Config.suportcl = "÷ß≥÷";

										} else {
											Config.suportcl = "≤ª÷ß≥÷";
										}
										celist2 = gson.fromJson(
												jsonobject.getString("tDates"),
												new TypeToken<List<ChanelEntitiy>>() {
												}.getType());
										Config.celist = celist2;
										Config.tv_sqkt = jsonobject
												.getString("opening_requirement");
										ll_Factory = (LinearLayout)findViewById(R.id.mer_detail);
										ll_Factory.setOnClickListener(GoodDeatail.this);
										handler.sendEmptyMessage(0);
									} else {
										Toast.makeText(
												getApplicationContext(),
												jsonobject.getString("message"),
												Toast.LENGTH_SHORT).show();
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}

							@Override
							public void onFailure(int statusCode,
									Header[] headers, byte[] responseBody,
									Throwable error) {
								// TODO Auto-generated method stub
							}
						});

	}

	private void addGood() {

		RequestParams params = new RequestParams();
		params.put("customerId", MyApplication.NewUser.getId());
		params.put("goodId", gfe.getId());
		// paychannelId
		params.put("paychannelId", paychannelId);
		params.setUseJsonStreamer(true);
		MyApplication.getInstance().getClient()
				.post(Config.goodadd, params, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						// TODO Auto-generated method stub
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
								Toast.makeText(getApplicationContext(),
										"ÃÌº”…Ã∆∑≥…π¶", Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(getApplicationContext(),
										jsonobject.getString("message"),
										Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						// TODO Auto-generated method stub

					}
				});

	}

	private void initIndicator() {

		ImageView imgView;
		View v = findViewById(R.id.indicator);// Á∫øÊ?ßÊ∞¥Âπ≥Â∏ÉÂ±?ÔºåË¥üË¥£Âä®ÊÄÅË∞ÉÊï¥ÂØºËà™ÂõæÊ†?

		for (int i = 0; i < ma.size(); i++) {
			imgView = new ImageView(this);
			LinearLayout.LayoutParams params_linear = new LinearLayout.LayoutParams(
					10, 10);
			params_linear.setMargins(7, 10, 7, 10);
			imgView.setLayoutParams(params_linear);
			indicator_imgs[i] = imgView;

			if (i == 0) { // ÂàùÂßãÂåñÁ¨¨‰∏?‰∏™‰∏∫ÈÄâ‰∏≠Áä∂Ê??

				indicator_imgs[i]
						.setBackgroundResource(R.drawable.indicator_focused);
			} else {
				indicator_imgs[i].setBackgroundResource(R.drawable.indicator);
			}
			((ViewGroup) v).addView(indicator_imgs[i]);
		}

	}

	/**
	 * ÈÄÇÈÖçÂô®ÔºåË¥üË¥£Ë£ÖÈÖç „ÄÅÈîÄÊØ? Êï∞ÊçÆ Âí? ÁªÑ‰ª∂ „Ä?
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
		 * Remove a page for the given position. ÊªëÂä®ËøáÂêéÂ∞±ÈîÄÊØ?
		 * ÔºåÈîÄÊØÅÂΩìÂâçÈ°µÁöÑÂâç‰∏?‰∏™ÁöÑÂâç‰∏Ä‰∏™ÁöÑÈ°µÔºÅ instantiateItem(View container, int
		 * position) This method was deprecated in API level . Use
		 * instantiateItem(ViewGroup, int)
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
			image.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//
					// Intent i=new Intent(AroundDetail.this,VPImage.class);
					//
					// i.putExtra("index", index_ima);
					// i.putExtra("mal", mal);
					// startActivityForResult(i, 9);
				}
			});

			return mList.get(position);
		}

	}

	private void getdataByChanel(int pcid) {

		RequestParams params = new RequestParams();
		params.put("pcid", pcid);
		System.out.println("---÷ß∏∂Õ®µ¿ID--" + pcid);

		params.setUseJsonStreamer(true);
		MyApplication
				.getInstance()
				.getClient()
				.post(Config.paychannel_info, params,
						new AsyncHttpResponseHandler() {

							@Override
							public void onSuccess(int statusCode,
									Header[] headers, byte[] responseBody) {
								// TODO Auto-generated method stub
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
										// ImageCacheUtil.IMAGE_CACHE.get(
										// factoryEntity.getLogo_file_path(),
										// fac_img); ÂõæÁâáË∑ØÂæÑÂæÖÂÆö
										//
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
										if (jsonobject
												.getBoolean("support_type")) {
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
										} else {
											Config.suportare = "≤ª÷ß≥÷";
										
										}
										if (jsonobject
												.getBoolean("support_cancel_flag")) {
											Config.suportcl = "÷ß≥÷";

										} else {
											Config.suportcl = "≤ª÷ß≥÷";
										}

									} else {
										Toast.makeText(
												getApplicationContext(),
												jsonobject.getString("message"),
												Toast.LENGTH_SHORT).show();
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}

							@Override
							public void onFailure(int statusCode,
									Header[] headers, byte[] responseBody,
									Throwable error) {
								// TODO Auto-generated method stub

							}
						});

	}

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

			// ÊîπÂèòÊâ?ÊúâÂØºËà™ÁöÑËÉåÊôØÂõæÁâá‰∏∫ÔºöÊú™È?â‰∏≠
			for (int i = 0; i < indicator_imgs.length; i++) {

				indicator_imgs[i].setBackgroundResource(R.drawable.indicator);

			}

			// ÊîπÂèòÂΩìÂâçËÉåÊôØÂõæÁâá‰∏∫ÔºöÈÄâ‰∏≠
			index_ima = position;
			indicator_imgs[position]
					.setBackgroundResource(R.drawable.indicator_focused);
		}

	}

}
