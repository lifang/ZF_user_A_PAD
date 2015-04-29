package com.example.zf_pad.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.zf_pad.Config;
import com.example.zf_pad.MyApplication;
import com.example.zf_pad.R;
import com.example.zf_pad.aadpter.AppleNeedAdapter;
import com.example.zf_pad.aadpter.GoodCommentAdapter;
import com.example.zf_pad.aadpter.HuilvAdapter;
import com.example.zf_pad.entity.ApplyneedEntity;
import com.example.zf_pad.entity.GoodCommentEntity;
import com.example.zf_pad.util.ScrollViewWithListView;
import com.example.zf_pad.util.TitleMenuUtil;
import com.example.zf_pad.util.Tools;
import com.example.zf_pad.util.XListView;
import com.example.zf_pad.util.XListView.IXListViewListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Good_detail_commet extends Fragment implements  IXListViewListener{
	private View view;
	private XListView Xlistview;
	private int page=1;
	private int rows=Config.ROWS;
	private int goodId;
	private String title;
	private LinearLayout eva_nodata;
	private boolean onRefresh_number = true;
	private GoodCommentAdapter myAdapter;
	List<GoodCommentEntity>  myList = new ArrayList<GoodCommentEntity>();
	List<GoodCommentEntity>  moreList = new ArrayList<GoodCommentEntity>();
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
				Toast.makeText(getActivity(),  " refresh too much",
						Toast.LENGTH_SHORT).show();
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
		 view = inflater.inflate(R.layout.good_commet, container, false);

			initView();
			getData(); 
		return view;
	}
	private void initView() {
		myAdapter=new GoodCommentAdapter(getActivity(), myList);
		eva_nodata=(LinearLayout)view.findViewById(R.id.eva_nodata);
		Xlistview=(XListView)view. findViewById(R.id.x_listview);
		// refund_listview.getmFooterView().getmHintView().setText("�Ѿ�û�������");
		Xlistview.setPullLoadEnable(true);
		Xlistview.setXListViewListener(this);
		Xlistview.setDivider(null);

		Xlistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
			//	Intent i = new Intent(GoodComment.this, OrderDetailPG.class);
			//	startActivity(i);
			}
		});
		Xlistview.setAdapter(myAdapter);
	}


	@Override
	public void onRefresh() {
		page = 1;
		 System.out.println("onRefresh1");
		myList.clear();
		 System.out.println("onRefresh2");
		getData();
		
	}
	@Override
	public void onLoadMore() {
		if (onRefresh_number) {
			page = page+1;
			
		//	onRefresh_number = false;
		//	getData();
			
			if (Tools.isConnect(getActivity())) {
				onRefresh_number = false;
				getData();
			} else {
				onRefresh_number = true;
				handler.sendEmptyMessage(2);
			}
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
		String url = Config.goodcomment;
		RequestParams params = new RequestParams();
		params.put("goodId", Config.goodId);
		params.put("page", page);
	 	params.put("rows", rows);
	 	System.out.println("---"+page);
		params.setUseJsonStreamer(true);

		MyApplication.getInstance().getClient()
				.post(url, params, new AsyncHttpResponseHandler() {

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
								
								moreList= gson.fromJson(jsonobject.getString("list"), new TypeToken<List<GoodCommentEntity>>() {
			 					}.getType());
			 				 
								myList.addAll(moreList);
				 				handler.sendEmptyMessage(0);
 		 					  
			 				 
			 			 
							}else{
								code = jsonobject.getString("message");
								Toast.makeText(getActivity(), code, 1000).show();
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
	//	System.out.println("getData");
	//	handler.sendEmptyMessage(0);
	}
}
