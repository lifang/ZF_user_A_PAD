package com.example.zf_pad.fragment;

import static com.example.zf_pad.fragment.Constants.CityIntent.CITY_ID;
import static com.example.zf_pad.fragment.Constants.CityIntent.CITY_NAME;
import static com.example.zf_pad.fragment.Constants.CityIntent.SELECTED_CITY;
import static com.example.zf_pad.fragment.Constants.CityIntent.SELECTED_PROVINCE;

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
import com.example.zf_pad.aadpter.MessageAdapter;
import com.example.zf_pad.activity.ContactUs;
import com.example.zf_pad.activity.LoginActivity;
import com.example.zf_pad.activity.MainActivity;
import com.example.zf_pad.activity.MyWebView;
import com.example.zf_pad.activity.PosListActivity;
import com.example.zf_pad.activity.SystemMessage;
import com.example.zf_pad.activity.TerminalManagerActivity;
import com.example.zf_pad.entity.PicEntity;
import com.example.zf_pad.entity.TestEntitiy;
import com.example.zf_pad.trade.ApplyListActivity;
import com.example.zf_pad.trade.CitySelectActivity;
import com.example.zf_pad.trade.MyApplyDetail;
import com.example.zf_pad.trade.TradeFlowActivity;
import com.example.zf_pad.trade.entity.City;
import com.example.zf_pad.trade.entity.Province;
import com.example.zf_pad.util.ImageCacheUtil;
import com.example.zf_pad.util.XListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class M_MianFragment extends Fragment implements OnClickListener {
	private View view;
	private RelativeLayout main_rl_pos, main_rl_renzhen, main_rl_zdgl,
			main_rl_jyls, main_rl_Forum, main_rl_wylc, main_rl_xtgg,
			main_rl_lxwm;
	private String cityName;
	private int cityId;
	private TextView cityTextView;
	public static final int REQUEST_CITY = 1;
	public static final int REQUEST_CITY_WHEEL = 2;
	private Province province;
	private City city;
	private View citySelect;
	private LocationClient mLocationClient;
	private TextView LocationResult;
	// vp
	private ArrayList<String> mal = new ArrayList<String>();
	private ArrayList<PicEntity> myList = new ArrayList<PicEntity>();
	private ViewPager view_pager;
	private MyAdapter adapter;
	private ImageView[] indicator_imgs;// �������ͼƬ����
	private View item;
	private LayoutInflater inflater;
	private ImageView image;
	private int index_ima = 0;
	private ArrayList<String> ma = new ArrayList<String>();
	private List<City> mCities = new ArrayList<City>();
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
				Toast.makeText(getActivity(), (String) msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			case 2: // ����������
				Toast.makeText(getActivity(), "����δ����", Toast.LENGTH_SHORT)
						.show();
				break;
			case 3:

				break;
			case 4:

				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// view = inflater.inflate(R.layout.f_main,container,false);

		if (view != null) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null)
				parent.removeView(view);
		}
		try {
			view = inflater.inflate(R.layout.f_main, container, false);
			ImageView im = (ImageView) view.findViewById(R.id.testbutton);
			im.setOnClickListener(this);
			initView();
			if (!Config.isFRIST) {
				mLocationClient = ((MyApplication) getActivity()
						.getApplication()).mLocationClient;

				LocationResult = (TextView) view.findViewById(R.id.tv_city);
				((MyApplication) getActivity().getApplication()).mLocationResult = LocationResult;
				InitLocation();
				mLocationClient.start();

				System.out.println("��ǰ���� ID----" + MyApplication.getCITYID());
			}
			getdata();
		} catch (InflateException e) {

		}
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.testbutton:
			Intent i = new Intent(getActivity(), LoginActivity.class);
			startActivity(i);

			break;
		case R.id.titleback_linear_back:
			Intent intent = new Intent(getActivity(), CitySelectActivity.class);
			cityName = cityTextView.getText().toString();
			intent.putExtra(CITY_NAME, cityName);
			startActivityForResult(intent, REQUEST_CITY);
			break;
		case R.id.main_rl_jyls: // ������ˮ
			if (Config.CheckIsLogin(getActivity())) {
				startActivity(new Intent(getActivity(), TradeFlowActivity.class));
			}
			break;
		case R.id.main_rl_pos: // ����pos��

			startActivity(new Intent(getActivity(), PosListActivity.class));
			
			break;
		case R.id.main_rl_renzhen: // ��ͨ��֤
			if (Config.CheckIsLogin(getActivity())) {
			startActivity(new Intent(getActivity(), ApplyListActivity.class));
			}
			break;
		case R.id.main_rl_xtgg: // ϵͳ����

			startActivity(new Intent(getActivity(), SystemMessage.class));
			break;
		case R.id.main_rl_lxwm:
			startActivity(new Intent(getActivity(), ContactUs.class));
			break;
		case R.id.main_rl_zdgl: // �ն�����
			if (Config.CheckIsLogin(getActivity())) {
			startActivity(new Intent(getActivity(),
					TerminalManagerActivity.class));
			}
			break;
		default:
			break;
		}

	}

	private void InitLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// ���ö�λģʽ
		option.setCoorType("gcj02");// ���صĶ�λ����ǰٶȾ�γ�ȣ�Ĭ��ֵgcj02
		int span = 1000;

		option.setScanSpan(span);// ���÷���λ����ļ��ʱ��Ϊ5000ms
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}

	private void initView() {

		main_rl_pos = (RelativeLayout) view.findViewById(R.id.main_rl_pos);
		main_rl_pos.setOnClickListener(this);
		main_rl_renzhen = (RelativeLayout) view
				.findViewById(R.id.main_rl_renzhen);
		main_rl_renzhen.setOnClickListener(this);
		main_rl_zdgl = (RelativeLayout) view.findViewById(R.id.main_rl_zdgl);
		main_rl_zdgl.setOnClickListener(this);
		main_rl_jyls = (RelativeLayout) view.findViewById(R.id.main_rl_jyls);
		main_rl_jyls.setOnClickListener(this);
		main_rl_Forum = (RelativeLayout) view.findViewById(R.id.main_rl_Forum);
		main_rl_Forum.setOnClickListener(this);
		main_rl_wylc = (RelativeLayout) view.findViewById(R.id.main_rl_wylc);
		main_rl_wylc.setOnClickListener(this);
		main_rl_lxwm = (RelativeLayout) view.findViewById(R.id.main_rl_lxwm);
		main_rl_lxwm.setOnClickListener(this);
		main_rl_xtgg = (RelativeLayout) view.findViewById(R.id.main_rl_xtgg);
		main_rl_xtgg.setOnClickListener(this);
		cityTextView = (TextView) view.findViewById(R.id.tv_city);
		cityTextView.setText(Config.CITY);
		citySelect = view.findViewById(R.id.titleback_linear_back);

		citySelect.setOnClickListener(this);
		view_pager = (ViewPager) view.findViewById(R.id.view_pager);

		inflater = LayoutInflater.from(getActivity());
		adapter = new MyAdapter(list);

		view_pager.setAdapter(adapter);
		// �󶨶������������緭ҳ�Ķ���
		view_pager.setOnPageChangeListener(new MyListener());

	}

	// getdata1();
	private void getdata() {

		MyApplication.getInstance().getClient()
				.post(Config.INDEXIMG, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
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
								String res = jsonobject.getString("result");
								// jsonobject = new JSONObject(res);
								myList = gson.fromJson(res,
										new TypeToken<List<PicEntity>>() {
										}.getType());
								handler.sendEmptyMessage(0);
							} else {
								code = jsonobject.getString("message");
								Toast.makeText(getActivity(), code, 1000)
										.show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						error.printStackTrace();
					}
				});

	}

	private void initIndicator() {

		// ImageView imgView;
		View v = view.findViewById(R.id.indicator);// ����ˮƽ���֣�����̬��������ͼ��

		for (int i = 0; i < ma.size(); i++) {
			try {
				ImageView imgView = new ImageView(getActivity());

				LinearLayout.LayoutParams params_linear = new LinearLayout.LayoutParams(
						10, 10);
				params_linear.setMargins(7, 10, 7, 10);
				imgView.setLayoutParams(params_linear);
				indicator_imgs[i] = imgView;
				if (i == 0) { // ��ʼ����һ��Ϊѡ��״̬

					indicator_imgs[i]
							.setBackgroundResource(R.drawable.indicator_focused);
				} else {
					indicator_imgs[i]
							.setBackgroundResource(R.drawable.indicator);
				}
				((ViewGroup) v).addView(indicator_imgs[i]);
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

	}

	/**
	 * ������������װ�� ������ ���� �� ��� ��
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
		 * Remove a page for the given position. ������������� �����ٵ�ǰҳ��ǰһ����ǰһ����ҳ��
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
			image.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent i = new Intent(getActivity(), MyWebView.class);
					i.putExtra("title", "����");
					i.putExtra("url", myList.get(position).getWebsite_url());
					startActivity(i);

				}
			});
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
	 * ���������������첽����ͼƬ
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

			// �ı����е����ı���ͼƬΪ��δѡ��
			for (int i = 0; i < indicator_imgs.length; i++) {

				indicator_imgs[i].setBackgroundResource(R.drawable.indicator);

			}

			// �ı䵱ǰ����ͼƬΪ��ѡ��
			index_ima = position;
			indicator_imgs[position]
					.setBackgroundResource(R.drawable.indicator_focused);
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != getActivity().RESULT_OK)
			return;
		switch (requestCode) {
		case REQUEST_CITY:
			cityId = data.getIntExtra(CITY_ID, 0);
			cityName = data.getStringExtra(CITY_NAME);
			cityTextView.setText(cityName);
			Config.CITY = cityName;
			break;
		case REQUEST_CITY_WHEEL:
			province = (Province) data.getSerializableExtra(SELECTED_PROVINCE);
			city = (City) data.getSerializableExtra(SELECTED_CITY);
			cityTextView.setText(city.getName());
			break;
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
}
