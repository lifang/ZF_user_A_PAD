package com.example.zf_pad.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.zf_pad.Config;
import com.example.zf_pad.MyApplication;
import com.epalmpay.userPad.R;
import com.example.zf_pad.aadpter.OrderAdapter;
import com.example.zf_pad.activity.OrderDetail;
import com.example.zf_pad.entity.OrderEntity;
import com.example.zf_pad.trade.common.DialogUtil;
import com.example.zf_pad.util.Tools;
import com.example.zf_pad.util.XListView;
import com.example.zf_pad.util.XListView.IXListViewListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Mine_Dd extends Fragment implements IXListViewListener,
		OnClickListener {
	private View view;
	private XListView Xlistview;
	private int page = 1;
	private int rows = Config.ROWS;
	private LinearLayout eva_nodata;
	private boolean onRefresh_number = true;
	private OrderAdapter myAdapter;
	String type = null;
	List<OrderEntity> myList = new ArrayList<OrderEntity>();
	List<OrderEntity> moreList = new ArrayList<OrderEntity>();
	private LinearLayout ll_DD;
	public static Handler myHandler;
	private Handler handler = new Handler(Looper.getMainLooper()) {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				onLoad();
				if (myList.size() == 0) {
					// norecord_text_to.setText("ï¿½ï¿½Ã»ï¿½ï¿½ï¿½ï¿½Øµï¿½ï¿½ï¿½Æ?);
					Xlistview.setVisibility(View.GONE);
					//eva_nodata.setVisibility(View.VISIBLE);
				}
				onRefresh_number = true;
				
				myAdapter.notifyDataSetChanged();
				break;
			case 1:
				Toast.makeText(getActivity(), (String) msg.obj,
						Toast.LENGTH_SHORT).show();

				break;
			case 2:
				Toast.makeText(getActivity(), "no 3g or wifi content",
						Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(getActivity(), " refresh too much",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	private TextView tv_gm;
	private TextView tv_zl;
	private Activity mactivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// view = inflater.inflate(R.layout.f_mine_myorder, container,false);

		if (view != null) {

			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null)
				parent.removeView(view);
		}

		try {
			view = inflater.inflate(R.layout.f_mine_myorder, container, false);
			initView();
			
		} catch (InflateException e) {

		}

		return view;
	}
@Override
public void onStart() {

	super.onStart();
	myHandler=new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what==1023){
				ll_DD.setVisibility(View.GONE);
				M_my.isHidden=true;
			}
		};
	};
	//ll_DD.setVisibility(View.VISIBLE);
	if(myList.size()!=0){
		myList.clear();
	}
	page = 1;
	getData();
}
@Override
public void onPause() {

	super.onPause();
	Log.e("onPause", "onPause");
	//ll_DD.setVisibility(View.GONE);
}
@Override
public void onStop() {
	super.onStop();
	Log.e("onStop", "onStop");
}
@Override
public void onDestroyView() {
	super.onDestroyView();
}
	private void initView() {
		ll_DD=(LinearLayout) view.findViewById(R.id.ll_DD);
		tv_gm = (TextView) view.findViewById(R.id.tv_gm);
		tv_zl = (TextView) view.findViewById(R.id.tv_zl);
		tv_gm.setOnClickListener(this);
		tv_zl.setOnClickListener(this);
		myAdapter = new OrderAdapter(getActivity(), myList,this);
		eva_nodata = (LinearLayout) view.findViewById(R.id.eva_nodata);
		Xlistview = (XListView) view.findViewById(R.id.x_listview);

		// refund_listview.getmFooterView().getmHintView().setText("ï¿½Ñ¾ï¿½Ã»ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿?);

		Xlistview.setPullLoadEnable(true);
		Xlistview.setXListViewListener(this);
		Xlistview.setDivider(null);
		Xlistview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(myList.size()!=0){
					Intent i = new Intent(mactivity, OrderDetail.class);
					i.putExtra("status", myList.get(position-1).getOrder_status());
					if(myList.get(position-1).getOrder_id()!=null){
						i.putExtra("id", Integer.parseInt(myList.get(position-1).getOrder_id()));
					}else{
						i.putExtra("id", 0);
					}
					
					i.putExtra("type", myList.get(position-1).getOrder_type());	
					
					startActivity(i);
				}

			}
		});
		Xlistview.setAdapter(myAdapter);
	}

	@Override
	public void onRefresh() {
		page = 1;
		myList.clear();
		getData();

	}

	@Override
	public void onLoadMore() {
		if (onRefresh_number) {
			page = page + 1;

			if (Tools.isConnect(getActivity())) {
				onRefresh_number = false;
				getData();
			} else {
				onRefresh_number = true;
				handler.sendEmptyMessage(2);
			}
		} else {
			handler.sendEmptyMessage(3);
		}

	}

	private void onLoad() {
		Xlistview.stopRefresh();
		Xlistview.stopLoadMore();
		Xlistview.setRefreshTime(com.example.zf_pad.util.Tools.getHourAndMin());
	}

	public void buttonClick() {
		page = 1;
		myList.clear();
		getData();
	}
	public  void DataChange(){
		page = 1;
		myList.clear();
		getData();
		
	}
	private void getData() {

		RequestParams params = new RequestParams();
		params.put("customer_id", MyApplication.NewUser.getId());
		params.put("page", page);
		params.put("p", type);
		params.put("pageSize", 5);

		// params.put("pageSize", 2);

		params.setUseJsonStreamer(true);

		MyApplication.getInstance().getClient()
				.post(Config.ORDERLIST, params, new AsyncHttpResponseHandler() {
					private Dialog loadingDialog;

					@Override
					public void onStart() {
						super.onStart();
						loadingDialog = DialogUtil
								.getLoadingDialg(mactivity);
						loadingDialog.show();
					}

					@Override
					public void onFinish() {
						super.onFinish();
						loadingDialog.dismiss();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						String responseMsg = new String(responseBody)
								.toString();
						Log.e("print", responseMsg);
						Gson gson = new Gson();
						JSONObject jsonobject = null;
						String code = null;
						try {
							jsonobject = new JSONObject(responseMsg);
							code = jsonobject.getString("code");
							int a = jsonobject.getInt("code");
							if (a == Config.CODE) {
								String res = jsonobject.getString("result");
								jsonobject = new JSONObject(res);
								moreList.clear();
								System.out.println("-jsonobject String()--"
										+ jsonobject.getString("content")
												.toString());
								moreList = gson.fromJson(
										jsonobject.getString("content")
												.toString(),
										new TypeToken<List<OrderEntity>>() {
										}.getType());
								System.out
										.println("-sendEmptyMessage String()--");
								myAdapter.notifyDataSetChanged();
								if(myList.size()!=0&&moreList.size()==0){
									Toast.makeText(getActivity(), "Ã»ÓÐ¸ü¶àÊý¾Ý!", 1000).show();
									Xlistview.getmFooterView().setState2(2);
									Xlistview.setPullLoadEnable(false);
								}
									
								myList.addAll(moreList);
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
						handler.sendEmptyMessage(0);
						System.out.println("-onFailure---");
						Log.e("print", "-onFailure---" + error);
					}
				});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_gm:
			Config.iszl=false;
			
			type = "1";
			tv_gm.setTextColor(getResources().getColor(R.color.text292929));
			tv_zl.setTextColor(getResources().getColor(R.color.text292929));
			tv_gm.setTextColor(getResources().getColor(R.color.o));
			page = 1;
			myList.clear();
			getData();
			myAdapter = new OrderAdapter(getActivity(), myList,this);
			Xlistview.setAdapter(myAdapter);
			break;
		case R.id.tv_zl:
			Config.iszl=true;
			type = "2";
			tv_gm.setTextColor(getResources().getColor(R.color.text292929));
			tv_zl.setTextColor(getResources().getColor(R.color.text292929));
			tv_zl.setTextColor(getResources().getColor(R.color.o));
			page = 1;
			myList.clear();
			getData();
			myAdapter = new OrderAdapter(getActivity(), myList,this);
			Xlistview.setAdapter(myAdapter);
			break;
		default:
			break;
		}

	}
	@Override
	public void onDestroy() {
		
		super.onDestroy();
		Log.e("onDestroy","onDestroy");
	}
	@Override
	public void onResume() {
		
		super.onResume();
	
	}
	@Override
	public void onAttach(Activity activity) {
		
		super.onAttach(activity);
		mactivity = activity;
	}
	
}
