package com.example.zf_pad.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.zf_pad.BaseActivity;
import com.example.zf_pad.Config;
import com.example.zf_pad.MyApplication;
import com.example.zf_pad.R;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.zf_pad.aadpter.SearchAdapter;
import com.example.zf_pad.entity.HotEntity;
import com.example.zf_pad.entity.PostPortEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

/***
 * 搜索
 * 
 * @author Lijinpeng
 * 
 *         comdo
 */
public class PosSearch1 extends BaseActivity implements OnEditorActionListener {
	private String lat, lng, CName;
	private EditText et;

	private LinearLayout search_linear_yuyin, search_linear_delete;
	private static int REQUEST_CODE = 0;
	private Button back, titleright, etsbtn_clear;
	private SharedPreferences mySharedPreferences = null;
	private Editor editor;
	private int a = 0;
	private RelativeLayout ets_rl_r1, ets_rl_clear;
	private TextView tvtv, ets_histvshow, ml_maplocation;
	String poiStr = "", sessionId, sign;// 搜索记录
	List<String> data = new ArrayList<String>();
	List<String> key = new ArrayList<String>();
	List<String> mhot = new ArrayList<String>();
	List<HotEntity> hotlist = new ArrayList<HotEntity>();
	private String sss = "没有数据";
	private String destinationLat, destinationLng;
	private SearchAdapter searchAdapter;
	private LinearLayout eva_nodata;
	private long merchantId;
	private int currentPage = 1;
	private String name;
	private boolean onRefresh_number = true;
	private GridView gr_search;
	private TextView clear;
	private GridView gr_hot;
	private SearchAdapter hotAdapter;
	private LinearLayout ll_close;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pos_search1);
		mySharedPreferences = getSharedPreferences("pos_search", MODE_PRIVATE);
		ll_close = (LinearLayout)findViewById(R.id.titleback_linear_back);
		ll_close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
				
			}
		});
		editor = mySharedPreferences.edit();
		et = (EditText) findViewById(R.id.serch_edit);
		et.setOnEditorActionListener(this);
		poiStr = mySharedPreferences.getString("poiStr", "");
		gr_search = (GridView) findViewById(R.id.gr_serch);
		searchAdapter = new SearchAdapter(PosSearch1.this, data);
		gr_search.setAdapter(searchAdapter);
		ml_maplocation = (TextView) findViewById(R.id.delete);
		ml_maplocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		clear = (TextView) findViewById(R.id.clear);
		clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				DeletaData();
				data.clear();
				poiStr="";
			}
		});
		init();
		gr_hot = (GridView) findViewById(R.id.gr_hot);
		hotAdapter = new SearchAdapter(PosSearch1.this, mhot);
		gr_hot.setAdapter(hotAdapter);
		gr_hot.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Intent i=new Intent(PosSearch1.this,GoodDeatail.class);
				i.putExtra("id", Integer.parseInt(hotlist.get(position).getId()));
				startActivity(i);
				
			}
		});
	
		getData();
	}
	private void init() {
		if (poiStr == "" || poiStr == null) {	

		} else {
			System.out.println("加载历史记录··4··");
 
			if (poiStr.contains(",")) {
				String[] serach = poiStr.split(",");
				for (int i = (serach.length - 1); i >= 0; i--) {
					data.add(serach[i]);
					a++;
				}				
			} else {
				data.add(poiStr);
		
			}
			searchAdapter.notifyDataSetChanged();
			gr_search.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {

					if (data.get(arg2).endsWith("没有搜索记录")) {

						// Toast.makeText(EditTextSearch.this, "请在输入框输入正确条件",
 
					} else if(data.get(arg2).endsWith("清除搜索记录")){
						 DeletaData();
					} else {
						// 判断历史记录是否 需要添加
						CName = data.get(arg2);
						et.setText(CName);
						getData(data.get(arg2));
 
					}
					
				}
			});
		}


	}
	private void getData() {
	
		MyApplication.getInstance().getClient()
				.post(Config.POSTHOT, new AsyncHttpResponseHandler() {

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

							} else if (code == 1) {
								
								hotlist=gson.fromJson(
										jsonobject.getString("result"),
										new TypeToken<List<HotEntity>>() {
										}.getType());
								for(HotEntity hot:hotlist){
									mhot.add(hot.getTitle());
								}
								hotAdapter.notifyDataSetChanged();
								//handler.sendEmptyMessage(0);
							} else {
								/*Toast.makeText(DoctorActivity.this,
										jsonobject.getString("message"),
										Toast.LENGTH_SHORT).show();*/
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
	// add
	public void addData(String name) {
		String[] serach = poiStr.split(",");
		data.clear();
		for (int i = (serach.length - 1); i >= 0; i--) {
			data.add(serach[i]);
			a++;
		}
		for(int i=0;i<data.size();i++){
			if(data.get(i).equals(name)){
				data.remove(i);
				//data.add(name);
			}
		}
		data.add(name);
		poiStr="";
		for(String str:data){
			if (poiStr != null && !poiStr.equals("")) {
				poiStr += "," + str;
			} else {
				poiStr += str;
			}
			editor.putString("poiStr", poiStr);
			editor.commit();
		}
/*		if (!poiStr.contains(name)) {
			if (poiStr != null && !poiStr.equals("")) {
				poiStr += "," + name;
			} else {
				poiStr += name;
			}
			editor.putString("poiStr", poiStr);
			editor.commit();
		}*/
	}

	// 删除记录
	public void DeletaData() {
		editor = mySharedPreferences.edit();
		editor.putString("poiStr", "");
		editor.commit();// 提交
		data.clear();

		searchAdapter.notifyDataSetChanged();
	}

	private void getData(String name) {

	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		name = et.getText().toString();
		System.out.println(" content111" + name);
		if (actionId == EditorInfo.IME_ACTION_DONE) {
			System.out.println(" content221" + name);
		}
		// addData(name);
		System.out.println("---" + actionId);
		switch (actionId) {
		case 0:
			name = et.getText().toString();
			System.out.println(" content" + name);
			addData(name);

			Intent intent2 = new Intent();
			intent2.putExtra("text", name);
			PosSearch1.this.setResult(2, intent2);
			finish();

			return true;

		}

		return false;

	}
}
