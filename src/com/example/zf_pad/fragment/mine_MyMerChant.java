package com.example.zf_pad.fragment;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.zf_pad.Config;
import com.example.zf_pad.MyApplication;
import com.example.zf_pad.R;
import com.example.zf_pad.aadpter.ShopAdapter;
import com.example.zf_pad.activity.CreatMerchant;
import com.example.zf_pad.entity.Shopname;
import com.example.zf_pad.trade.API;
import com.example.zf_pad.trade.common.DialogUtil;
import com.example.zf_pad.trade.common.HttpCallback;
import com.example.zf_pad.util.Tools;
import com.example.zf_pad.util.XListView;
import com.example.zf_pad.util.XListView.IXListViewListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class mine_MyMerChant extends Fragment implements IXListViewListener{
	private View view;
	private XListView xxlistview;
	private List<Shopname> datasho;
	private BaseAdapter shoaadapter;
	private int page=1;
	private int rows=3;
	public static Handler myHandler;
	private Button btn_creat;
	private int[] id;
	public static boolean isFromItem=false;
	private int customerId=MyApplication.NewUser.getId();
	private LinearLayout ll_merchant;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		/*if (view == null) {
			view = inflater.inflate(R.layout.f_mine_mymer, null);	
		}*/
		if (view != null) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null)
				 parent.removeView(view);
		}
		try {
			view = inflater.inflate(R.layout.f_mine_mymer, container, false);
			init();
			
		} catch (InflateException e) {
		
		}
		return view;
	}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if(datasho.size()!=0){
			datasho.clear();
		}
		getData();
		myHandler=new Handler(){
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case 0:
					onLoad( );
					xxlistview.setAdapter(shoaadapter);
					break;
				case 1:
					delect();
					break;
				case 2:
					isFromItem=true;
					Intent intent=new Intent(getActivity(),CreatMerchant.class);
					intent.putExtra("position", id[ShopAdapter.pp]);
					startActivity(intent);
					break;
				default:
					break;
				}
				
			};
		};
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ll_merchant.setVisibility(View.VISIBLE);
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		ll_merchant.setVisibility(View.GONE);
	}
	protected void delect() {
		int[] ids=new int[1];
		ids[0]=id[ShopAdapter.pp];
		Gson gson = new Gson();
		RequestParams params = new RequestParams();
		try {
			params.put("ids", new JSONArray(gson.toJson(ids)));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		params.setUseJsonStreamer(true);
		MyApplication.getInstance().getClient().post(API.DELECT_MERCHANTLIST, params, new AsyncHttpResponseHandler() {
			private Dialog loadingDialog;

			@Override
			public void onStart() {	
				super.onStart();
				loadingDialog = DialogUtil.getLoadingDialg(getActivity());
				loadingDialog.show();
			}
			@Override
			public void onFinish() {
				super.onFinish();
				loadingDialog.dismiss();
			}
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				String responseMsg = new String(responseBody)
				.toString();
				JSONObject jsonobject = null;
				int code = 0;
				try {
					jsonobject = new JSONObject(responseMsg);
					code = jsonobject.getInt("code");
					if(code==1){
						datasho.remove(ShopAdapter.pp);
						shoaadapter.notifyDataSetChanged();
						Log.e("size", datasho.size()+"");
					}
					else{
						Toast.makeText(getActivity(), jsonobject.getString("message"),
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
	protected void onLoad() {
		xxlistview.stopRefresh();
		xxlistview.stopLoadMore();
		xxlistview.setRefreshTime(Tools.getHourAndMin());
		
	}
	private void getData() {
		
		MyApplication.getInstance().getClient().post(API.GET_MERCHANTLIST+customerId+"/"+page+"/"+rows, new AsyncHttpResponseHandler() {
			private Dialog loadingDialog;

			@Override
			public void onStart() {	
				super.onStart();
				loadingDialog = DialogUtil.getLoadingDialg(getActivity());
				loadingDialog.show();
			}
			@Override
			public void onFinish() {
				super.onFinish();
				loadingDialog.dismiss();
			}
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				String responseMsg = new String(responseBody)
				.toString();
				Log.e("responseMsg", responseMsg);
				Gson gson = new Gson();
				
				JSONObject jsonobject = null;
				String code = null;
				try {
					jsonobject = new JSONObject(responseMsg);
					code = jsonobject.getString("code");
					int a =jsonobject.getInt("code");
					if(a==Config.CODE){  
						JSONArray list=jsonobject.getJSONObject("result").getJSONArray("list");
						id=new int[list.length()];
						for(int i=0;i<list.length();i++){
							id[i]=list.getJSONObject(i).getInt("id");
							if(list.getJSONObject(i).getString("legal_person_name").equals("")){
								datasho.add(new Shopname(i, "未获得商户名"));
							}
							else{
								datasho.add(new Shopname(i, list.getJSONObject(i).getString("legal_person_name")));
							}
						}
						if(datasho.size()!=0){
							myHandler.sendEmptyMessage(0);
							
						}
		 					  
	 				 
	 			 
					}else{
						code = jsonobject.getString("message");
						Toast.makeText(getActivity(), code, 1000).show();
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
				Log.e("url", API.GET_MERCHANTLIST+Constants.TEST_CUSTOMER+"/"+page+"/"+rows);
			}
		});
		}
	private void init() {
		ll_merchant=(LinearLayout) view.findViewById(R.id.ll_merchant);
		btn_creat=(Button) view.findViewById(R.id.btn_creat);
		xxlistview=(XListView) view.findViewById(R.id.list);
		
		xxlistview.setPullLoadEnable(true);
		xxlistview.setXListViewListener(this);
		xxlistview.setDivider(null);
		datasho=new ArrayList<Shopname>();
		shoaadapter=new ShopAdapter(datasho, getActivity().getBaseContext());
		btn_creat.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(getActivity(),CreatMerchant.class);
				
				startActivity(intent);
			}
		});
	}
	@Override
	public void onRefresh() {
		page=1;
		datasho.clear();
		getData();
		
	}
	@Override
	public void onLoadMore() {
		page+=1;
		getData();
		
		
	}
}
