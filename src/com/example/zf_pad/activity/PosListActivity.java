package com.example.zf_pad.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.zf_pad.Config;
import com.example.zf_pad.R;
import com.example.zf_pad.aadpter.PosAdapter;
import com.example.zf_pad.aadpter.PosAdapter1;
import com.example.zf_pad.aadpter.PosPortAdapter;
import com.example.zf_pad.entity.PosEntity;
import com.example.zf_pad.entity.TestEntitiy;
import com.example.zf_pad.util.Tools;
import com.example.zf_pad.util.XListView;
import com.example.zf_pad.util.XListView.IXListViewListener;
import com.example.zf_pad.util.XListViewFooter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewDebug.IntToString;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PosListActivity extends Activity implements OnClickListener,IXListViewListener{
	private ImageView pos_select,search2,img3;	

	private XListView Xlistview;
	private int page=1;
	private int rows=Config.ROWS;
	private LinearLayout eva_nodata,ll_xxyx,ll_mr,ll_updown,ll_pj;
	private boolean onRefresh_number = true;
	private PosAdapter myAdapter;
	private String keys=null;
	private TextView next_sure,tv_mr,tv_2,tv_3,tv_4;
	private Boolean isDown=true;
	private int orderType=0;
	private EditText et_search;
	private int list_port=0;
	private int maxPrice=0,minPrice=0;
	List<PosEntity>  myList = new ArrayList<PosEntity>();
	List<PosEntity>  moreList = new ArrayList<PosEntity>();
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				onLoad( );
				
				if(myList.size()==0){
				//	norecord_text_to.setText("��û����ص���Ʒ");
					Xlistview.setVisibility(View.GONE);
					eva_nodata.setVisibility(View.VISIBLE);
				}
				onRefresh_number = true; 
			 	myAdapter.notifyDataSetChanged();
			 	myAdapter1.notifyDataSetChanged();
				break;
			case 1:
				Toast.makeText(getApplicationContext(), (String) msg.obj,
						Toast.LENGTH_SHORT).show();
			 
				break;
			case 2: // 
				Toast.makeText(getApplicationContext(), "no 3g or wifi content",
						Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(getApplicationContext(),  " refresh too much",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	private ImageView port1;

	private ImageView port2;

	private PosAdapter1 myAdapter1;

	private Intent i;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.poslist_activity);
		initView();
		getData();
	}
	private void initView() {
		// TODO Auto-generated method stub
		ll_mr=(LinearLayout) findViewById(R.id.ll_mr);
		ll_mr.setOnClickListener(this);
		ll_xxyx=(LinearLayout) findViewById(R.id.ll_xxyx);
		ll_xxyx.setOnClickListener(this);
		ll_updown=(LinearLayout) findViewById(R.id.ll_updown);
		ll_updown.setOnClickListener(this);
		ll_pj=(LinearLayout) findViewById(R.id.ll_pj);
		ll_pj.setOnClickListener(this);
		tv_mr=(TextView) findViewById(R.id.tv_mr);
		tv_2=(TextView) findViewById(R.id.tv_2);
		tv_3=(TextView) findViewById(R.id.tv_3);
		tv_4=(TextView) findViewById(R.id.tv_4);
		img3=(ImageView) findViewById(R.id.img3);
		
		et_search=(EditText) findViewById(R.id.et_search);
		et_search.setOnClickListener(this);
		pos_select=(ImageView) findViewById(R.id.pos_select);
		pos_select.setOnClickListener(this);
		search2=(ImageView) findViewById(R.id.search2);
		search2.setOnClickListener(this);
		
		myAdapter=new PosAdapter(PosListActivity.this, myList);
		myAdapter1 = new PosAdapter1(PosListActivity.this, myList);
		eva_nodata=(LinearLayout) findViewById(R.id.eva_nodata);
		Xlistview=(XListView) findViewById(R.id.x_listview);
		// refund_listview.getmFooterView().getmHintView().setText("�Ѿ�û�������");
		Xlistview.setPullLoadEnable(true);
		Xlistview.setXListViewListener(this);
		Xlistview.setDivider(null);
		Xlistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent i =new Intent (PosListActivity.this,GoodDeatail.class);
				i.putExtra("id", myList.get(position-1).getId());
				System.out.println("-Xlistview--"+id);
				startActivity(i);
			}
		});
		Xlistview.setAdapter(myAdapter);
		changList();
	}
	private void changList() {
		port1 = (ImageView)findViewById(R.id.port_list1);
		port2 = (ImageView)findViewById(R.id.port_list2);
		port1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(list_port==1){
					port1.setBackgroundResource(R.drawable.pos_px1);
					port2.setBackgroundResource(R.drawable.pos_pxf);
					Xlistview.setAdapter(myAdapter);
					}
				list_port=0;
			}
		});
		port2.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("NewApi") @Override
			public void onClick(View arg0) {
				if(list_port==0){
					port1.setBackground(getResources().getDrawable(R.drawable.pos_px));
					port2.setBackground(getResources().getDrawable(R.drawable.pos_pxf1));
					myAdapter1 = new PosAdapter1(PosListActivity.this, myList);
					Xlistview.setAdapter(myAdapter1);
					}
				list_port=1;
			}
		});
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.pos_select:
			i = new Intent(PosListActivity.this,PosPortActivity.class);
			startActivityForResult(i, 1);
			break;
			//search2
		case R.id.search2:
			Intent ii =new Intent(PosListActivity.this,MainActivity.class);
			Config.shopcar=true;
			startActivity(ii);
			break;
		case R.id.et_search:
			//Intent ii =  new Intent(PosListActivity.this,PosSearch.class);
			//startActivityForResult(ii, 2);
			
		 
			break;	
		case R.id.ll_mr:
			orderType=0;
			tv_mr.setTextColor(getResources().getColor(R.color.bgtitle));
			tv_2.setTextColor(getResources().getColor(R.color.bg_575D5F));
			tv_3.setTextColor(getResources().getColor(R.color.bg_575D5F));
			tv_4.setTextColor(getResources().getColor(R.color.bg_575D5F));
			myList.clear();
			getData();
			break;	
		case R.id.ll_xxyx:
			orderType=1;
			tv_mr.setTextColor(getResources().getColor(R.color.bg_575D5F));
			tv_2.setTextColor(getResources().getColor(R.color.bgtitle));
			tv_3.setTextColor(getResources().getColor(R.color.bg_575D5F));
			tv_4.setTextColor(getResources().getColor(R.color.bg_575D5F));
			myList.clear();
			getData();
			break;	
		case R.id.ll_updown:
			if(isDown){
				orderType=2;
				isDown=false;
				img3.setBackgroundDrawable(getResources().getDrawable(R.drawable.ti_down));
			}else{
				orderType=3;
				isDown=true;
				img3.setBackgroundDrawable(getResources().getDrawable(R.drawable.ti_up));
			}
			 
			tv_mr.setTextColor(getResources().getColor(R.color.bg_575D5F));
			tv_2.setTextColor(getResources().getColor(R.color.bg_575D5F));
			tv_3.setTextColor(getResources().getColor(R.color.bgtitle));
			tv_4.setTextColor(getResources().getColor(R.color.bg_575D5F));
			myList.clear();
			getData();
			break;	
		case R.id.ll_pj:
			orderType=4;
			tv_mr.setTextColor(getResources().getColor(R.color.bg_575D5F));
			tv_2.setTextColor(getResources().getColor(R.color.bg_575D5F));
			tv_3.setTextColor(getResources().getColor(R.color.bg_575D5F));
			tv_4.setTextColor(getResources().getColor(R.color.bgtitle));
			myList.clear();
			getData();
			break;	
		default:
			break;
		}
	}	
	
	
	
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		page = 1;
		 System.out.println("onRefresh1");
		myList.clear();
		 System.out.println("onRefresh2");
		getData();
	}


	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		if (onRefresh_number) {
			page = page+1;
			
			onRefresh_number = false;
			getData();
			
 
		}
		else {
			handler.sendEmptyMessage(3);
		}
	}
	private void onLoad() {
		Xlistview.stopRefresh();
		Xlistview.stopLoadMore();
		Xlistview.setRefreshTime(Tools.getHourAndMin());
	}

	public void buttonClick() {
		page = 1;
		myList.clear();
		getData();
	}
	 
	private void getData() {

		RequestParams params = new RequestParams();
		params.put("city_id", 1);
		params.put("orderType", orderType);
	 	params.put("keys", keys);
		params.put("minPrice", minPrice);
	 	params.put("maxPrice", maxPrice);
		System.out.println("keys```"+keys+orderType);
		params.setUseJsonStreamer(true);

		new AsyncHttpClient()
				.post(Config.POSLIST, params, new AsyncHttpResponseHandler() {

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
							int a =jsonobject.getInt("code");
							if(a==Config.CODE){  
								String res =jsonobject.getString("result");
								jsonobject = new JSONObject(res);
								
								moreList= gson.fromJson(jsonobject.getString("list"), new TypeToken<List<PosEntity>>() {
			 					}.getType());
			 				 
								myList.addAll(moreList);
				 				handler.sendEmptyMessage(0);
			 					  
			 				 
			 			 
							}else{
								code = jsonobject.getString("message");
								Toast.makeText(getApplicationContext(), code, 1000).show();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							 ;	
							e.printStackTrace();
							
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						// TODO Auto-generated method stub
						System.out.println("-onFailure---");
						Log.e("print", "-onFailure---" + error);
					}
				});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (resultCode) {
		case 1:
			if(data!=null){
				System.out.println("进入条件选择回调···");
				minPrice=data.getIntExtra("minPrice", 0);
				maxPrice=data.getIntExtra("maxPrice", 1000000);
				System.out.println(maxPrice+"进入条件选择回调···"+minPrice); 
				myList.clear();
				getData();
			}
			
			break;
		case 2:
			if(data!=null){
				String  a =data.getStringExtra("text");
				keys=a;
				et_search.setText(a);
				getData();
			}
			
			break;
		default:
			break;

		}
		super.onActivityResult(requestCode, resultCode, data);

	}
}
