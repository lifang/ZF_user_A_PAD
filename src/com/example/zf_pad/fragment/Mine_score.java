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
import com.example.zf_pad.aadpter.ScoreAdapter;
import com.example.zf_pad.activity.Exchange;
import com.example.zf_pad.entity.Score;
import com.example.zf_pad.trade.common.CommonUtil;
import com.example.zf_pad.util.Tools;
import com.example.zf_pad.util.XListView;
import com.example.zf_pad.util.XListView.IXListViewListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Mine_score extends Fragment implements IXListViewListener{
	private View view;
	private List<Score> datasco;
	private List<Score> moreList;
	private XListView xxlistview;
	private BaseAdapter scoreadapter;
	private int customerId=MyApplication.NewUser.getId();
	private int page=1;
	private int rows=2;
	private Handler myHandler;
	private boolean isrefersh=false;
	private int a=1;
	private boolean isLoadMore=false;
	private Button btn_exchange;
	private TextView tv_total;
	private int totalscore=0;
	private boolean isStop=false;
	private TextView apply_progress_tips;
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {

	// view = inflater.inflate(R.layout.f_mine, container,false);
	// initView();

	if (view != null) {
		Log.i("222222", "11111111");
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null)
			parent.removeView(view);
	}
	try {
		view = inflater.inflate(R.layout.score, container, false);
		init();
		getData();
	} catch (InflateException e) {

	}

	return view;
}
@Override
public void onStart() {
	// TODO Auto-generated method stub
	super.onStart();
	myHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				onLoad();
				tv_total.setText("总积分:"+totalscore);
				if(datasco.size()!=0){
					xxlistview.setVisibility(View.VISIBLE);
					apply_progress_tips.setVisibility(View.GONE);
				xxlistview.setAdapter(scoreadapter);
				}
				else{
					apply_progress_tips.setVisibility(View.VISIBLE);
					xxlistview.setVisibility(View.GONE);
				}
				break;
			case 1:
				onLoad();
				
				break;
			default:
				break;
			}
		};
	};

}
protected void onLoad() {
	xxlistview.stopRefresh();
	xxlistview.stopLoadMore();
	xxlistview.setRefreshTime(Tools.getHourAndMin());
	
}
private void getData() {
	if(!Tools.isConnect(getActivity())){
		CommonUtil.toastShort(getActivity(), "网络异常");
		return;
	}
	String url = "http://114.215.149.242:18080/ZFMerchant/api/customers/getIntegralList/";
	url=url+customerId+"/"+page+"/"+rows;
	MyApplication.getInstance().getClient()
	.post(url, new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			if(isrefersh){
				page=a;
				rows=2;
				isrefersh=false;
			}
			
			String responseMsg = new String(responseBody)
					.toString();
			String ss=responseMsg;
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
					totalscore=jsonobject.getInt("total");
					if(totalscore==0){
						myHandler.sendEmptyMessage(0);
						return;
					}
					Log.e("jsonobject", String.valueOf(jsonobject));
					JSONArray list=jsonobject.getJSONArray("list");
					if(list.length()==0){
						myHandler.sendEmptyMessage(0);
						isStop=true;
					/*	Toast.makeText(getActivity(), ss, 
								Toast.LENGTH_SHORT).show();*/
						return;
					}
					if(list.length()==0&&isLoadMore){
						CommonUtil.toastShort(getActivity(), "没有更多数据");
						isLoadMore=false;
					}
					for(int i=0;i<list.length();i++){
						if(list.getJSONObject(i).getInt("types")==1){
							datasco.add(new Score(i, 
									list.getJSONObject(i).getString("order_number"),
									list.getJSONObject(i).getString("payedAt"),
									list.getJSONObject(i).getString("quantity"), 
									list.getJSONObject(i).getString("target_type"), 
									"收入"));
						}
						else{
							datasco.add(new Score(i, 
									list.getJSONObject(i).getString("order_number"),
									list.getJSONObject(i).getString("payedAt"),
									list.getJSONObject(i).getString("quantity"), 
									list.getJSONObject(i).getString("target_type"), 
									"支出"));
						}
					}
					
	 				myHandler.sendEmptyMessage(0);
	 					  
 				 
 			 
				}else{
					code = jsonobject.getString("message");
					Toast.makeText(getActivity(), code, 1000).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block	
				e.printStackTrace();
				CommonUtil.toastShort(getActivity(), "服务器返回数据不完整，缺少属性");
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
private void init() {
	apply_progress_tips=(TextView) view.findViewById(R.id.apply_progress_tips);
	tv_total=(TextView) view.findViewById(R.id.tv_total);
	btn_exchange=(Button) view.findViewById(R.id.btn_exchange);
	moreList=new ArrayList<Score>();
	datasco=new ArrayList<Score>();
	xxlistview=(XListView) view.findViewById(R.id.list);
	xxlistview.setPullLoadEnable(true);
	xxlistview.setXListViewListener(this);
	xxlistview.setDivider(null);
	scoreadapter=new ScoreAdapter(datasco, getActivity().getBaseContext());
	btn_exchange.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			int total=0;
			for(int i=0;i<datasco.size();i++){
				total=total+Integer.parseInt(datasco.get(i).getGotscore());
			}
			Intent intent=new Intent(getActivity(),Exchange.class);
			intent.putExtra("price", total);
			startActivity(intent);
			
		}
	});
}
@Override
public void onRefresh() {
	if(!Tools.isConnect(getActivity())){
		CommonUtil.toastShort(getActivity(), "网络异常");
		return;
	}
	isrefersh=true;
	a=page;
	rows=a*rows;
	page=1;
	Log.e("rows", String.valueOf(rows));
	datasco.clear();
	getData();
}
@Override
public void onLoadMore() {
	if(!Tools.isConnect(getActivity())){
		CommonUtil.toastShort(getActivity(), "网络异常");
		return;
	}
	if(isStop){
		CommonUtil.toastShort(getActivity(), "无更多数据");
		onLoad();
		return;
	}
	isLoadMore=true;
	page+=1;
	getData();
	
}
}
