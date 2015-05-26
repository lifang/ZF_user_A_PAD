package com.example.zf_pad.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zf_pad.BaseActivity;
import com.example.zf_pad.Config;
import com.example.zf_pad.MyApplication;
import com.epalmpay.userPad.R;
import com.example.zf_pad.aadpter.MessageAdapter;
import com.example.zf_pad.entity.MessageEntity;
import com.example.zf_pad.trade.common.DialogUtil;
import com.example.zf_pad.util.TitleMenuUtil;
import com.example.zf_pad.util.Tools;
import com.example.zf_pad.util.XListView;
import com.example.zf_pad.util.XListView.IXListViewListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;



public class SystemMessage extends BaseActivity implements  IXListViewListener{
	private XListView Xlistview;
	private int page=1;
	private int rows=Config.ROWS;
	private LinearLayout eva_nodata;
	private boolean onRefresh_number = true;
	private MessageAdapter myAdapter;
	private TextView next_sure;
	List<MessageEntity> myList = new ArrayList<MessageEntity>();
	List<MessageEntity> moreList = new ArrayList<MessageEntity>();
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				onLoad( );
				
				if(myList.size()==0){
				//	norecord_text_to.setText("您没有相关的商品");
					Xlistview.setVisibility(View.GONE);
					eva_nodata.setVisibility(View.VISIBLE);
				}else {
					Xlistview.setVisibility(View.VISIBLE);
					eva_nodata.setVisibility(View.GONE);
				}
				onRefresh_number = true; 
			 	myAdapter.notifyDataSetChanged();
				break;
			case 1:
				Toast.makeText(getApplicationContext(), (String) msg.obj,
						Toast.LENGTH_SHORT).show();
			 
				break;
			case 2: // 网络有问题
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
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.my_message);				
			initView();			
			getData();
		}

		private void initView() {
			new TitleMenuUtil(SystemMessage.this, "系统公告").show();
	
			eva_nodata=(LinearLayout) findViewById(R.id.eva_nodata);
			Xlistview=(XListView) findViewById(R.id.x_listview);
			// refund_listview.getmFooterView().getmHintView().setText("已经没有数据了");
			Xlistview.setPullLoadEnable(true);
			Xlistview.setXListViewListener(this);
			Xlistview.setDivider(null);

			Xlistview.setOnItemClickListener(new OnItemClickListener() {

				private boolean flag=true;

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					/*try {
						myList.get(position).getId();
					} catch (Exception e) {
						flag = false;
					}*/
					if (flag) {
						Intent i = new Intent(SystemMessage.this, MymsgDetail.class);
	 					i.putExtra("id", myList.get(position - 1).getId());
	 					i.putExtra("type", "1");
	 					startActivity(i);
					}
 					
				}
			});
			myAdapter=new MessageAdapter(SystemMessage.this, myList,1);
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
				page = page+1;
				
				onRefresh_number = false;
				getData();
				
//				if (Tools.isConnect(getApplicationContext())) {
//					onRefresh_number = false;
//					getData();
//				} else {
//					onRefresh_number = true;
//					handler.sendEmptyMessage(2);
//				}
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
		/*
		 * 请求数据
		 */
		private void getData() {
			RequestParams params = new RequestParams();
		  
			//params.put("customer_id", MyApplication.NewUser.getId());
			params.put("page", page);
			params.put("rows", rows);
			params.setUseJsonStreamer(true);
			MyApplication.getInstance().getClient()
					.post(Config.SYSMSGLIST, params, new AsyncHttpResponseHandler() {
						private Dialog loadingDialog;

						@Override
						public void onStart() {
							
							super.onStart();
							loadingDialog = DialogUtil.getLoadingDialg(SystemMessage.this);
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
							System.out.println("-onSuccess---");
							String responseMsg = new String(responseBody)
									.toString();
							Log.e("LJP", responseMsg);
							Gson gson = new Gson();
							JSONObject jsonobject = null;
							int code = 0;
							
							try {
								jsonobject = new JSONObject(responseMsg);
								code = jsonobject.getInt("code");
								String res = jsonobject.getString("result");
								if(code==-2){
								 
								}else if(code==1){
									System.out.println("`res``" + res);
									jsonobject = new JSONObject(res);
									moreList.clear();
									moreList = gson.fromJson(
											jsonobject.getString("content"),
											new TypeToken<List<MessageEntity>>() {
											}.getType());

									if (moreList.size() == 0&&myList.size()!=0) {
										Toast.makeText(SystemMessage.this, "没有更多数据",
												Toast.LENGTH_SHORT).show();
										Xlistview.getmFooterView().setState2(2);
										Xlistview.setPullLoadEnable(false);
									}

									myList.addAll(moreList);
									handler.sendEmptyMessage(0);
									
								}else{
									Toast.makeText(getApplicationContext(), jsonobject.getString("message"),
											Toast.LENGTH_SHORT).show();
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
}
