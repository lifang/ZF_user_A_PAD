package com.example.zf_pad.fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zf_pad.Config;
import com.example.zf_pad.MyApplication;
import com.example.zf_pad.R;
import com.example.zf_pad.aadpter.ShopcarAdapter;
import com.example.zf_pad.activity.ConfirmOrder;
import com.example.zf_pad.entity.MyShopCar;
import com.example.zf_pad.entity.MyShopCar.Good;
import com.example.zf_pad.trade.common.DialogUtil;
import com.example.zf_pad.util.Tools;
import com.example.zf_pad.util.XListView;
import com.example.zf_pad.util.XListView.IXListViewListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class M_shopcar extends Fragment  implements IXListViewListener,OnClickListener{
	private View view;
	private XListView Xlistview;
	private int page = 1;
	private int rows = Config.ROWS;
	private LinearLayout eva_nodata;
	private boolean onRefresh_number = true;
	private ShopcarAdapter myAdapter;
	private List<Good> myShopList=new ArrayList<Good>();
	private Dialog loadingDialog;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				onLoad();
				
				break;
			case 1:
				Toast.makeText(getActivity(), (String) msg.obj,
						Toast.LENGTH_SHORT).show();

				break;
			case 2: 
				Toast.makeText(getActivity(),
						"no 3g or wifi content", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(getActivity(), " refresh too much",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	private CheckBox cb;
	private TextView tv_gj;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
			//view = inflater.inflate(R.layout.f_main,container,false);
		/*if (view != null) {
	        ViewGroup parent = (ViewGroup) view.getParent();
	        if (parent != null)
	            parent.removeView(view);
	    }*/
	    try {
	        view = inflater.inflate(R.layout.f_shopcar, container, false);
	        initView();
	       
	    } catch (InflateException e) {
	        
	    }
	    return view;
	}
	@Override
	public void onStart() {
		
		super.onStart();
//		myShopList.clear();
//		getData();
	}
	private void initView() {
		tv_gj = (TextView)view.findViewById(R.id.tv_gj);
		view.findViewById(R.id.confirm).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MyApplication.getComfirmList().clear();
				for(Good good:myShopList){
					if(good.isChecked()){
						MyApplication.getComfirmList().add(good);
					}
				}
			//MyApplication.setComfirmList(myShopList);
				if(MyApplication.getComfirmList().size()!=0){
					Intent i = new Intent(getActivity(), ConfirmOrder.class);
					startActivity(i);
				}else{
					Toast.makeText(getActivity(), "ÇëÑ¡ÔñÒª½áËãÉÌÆ·", 1000).show();
				}
				
			}
		});
		cb = (CheckBox)view.findViewById(R.id.item_cb);
		//myAdapter = new ShopcarAdapter(getActivity(), myShopList);
		eva_nodata = (LinearLayout)view.findViewById(R.id.eva_nodata);
		Xlistview = (XListView)view.findViewById(R.id.x_listview);
		// refund_listview.getmFooterView().getmHintView().setText("ï¿½Ñ¾ï¿½Ã»ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½");
		Xlistview.setPullLoadEnable(false);
		Xlistview.setPullRefreshEnable(false);
		Xlistview.setXListViewListener(this);
		Xlistview.setDivider(null);
		Xlistview.getmFooterView().setState2(0);
		Xlistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Intent i = new Intent(ShopCar.this, OrderDetailPG.class);
				// startActivity(i);
			}
		});
		Xlistview.setAdapter(myAdapter);
	}

	@Override
	public void onClick(View arg0) {
		
	}
	private void onLoad() {
		Xlistview.stopRefresh();
		Xlistview.stopLoadMore();
		Xlistview.setRefreshTime(Tools.getHourAndMin());
	}

	public void buttonClick() {
		page = 1;
		myShopList.clear();
		getData();
	}
	private void getData() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customerId", MyApplication.NewUser.getId()+"");
		//RequestParams params = new RequestParams("customerId", MyApplication.NewUser.getId()+"");
		//params.setUseJsonStreamer(true);
		JSONObject jsonParams = new JSONObject(params);
		HttpEntity entity;
		try {
			entity = new StringEntity(jsonParams.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return;
		}
		
		MyApplication.getInstance().getClient()
				.post(getActivity(),Config.SHOPCARLIST, null,entity,"application/json", new AsyncHttpResponseHandler() {
					

					@Override
					public void onStart() {
						super.onStart();
						loadingDialog = DialogUtil
								.getLoadingDialg(getActivity());
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

						MyShopCar myShopCar = MyShopCar.getShopCar(responseMsg);
						if (myShopCar != null) {
							myShopList = myShopCar.getResult();
							if (myShopCar.getResult() != null
									&& myShopCar.getResult().size() != 0) {
								
								onRefresh_number = true;
								myAdapter = new ShopcarAdapter(getActivity(), myShopList);
								Xlistview.setAdapter(myAdapter);
								myAdapter.notifyDataSetChanged();
							}
						} else {
							Xlistview.setVisibility(View.GONE);
							eva_nodata.setVisibility(View.VISIBLE);
						}
						
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						System.out.println("-onFailure---");
						Log.e("print", "-onFailure---" + error);
					}
				});
 
		handler.sendEmptyMessage(0);
	}
	@Override
	public void onRefresh() {
//		page = 1;
//		myShopList.clear();
//		getData();
		
	}
	@Override
	public void onLoadMore() {
		
	}
@Override
public void onResume() {
	
	super.onResume();
	myShopList.clear();
	getData();
	cb.setChecked(false);
}

}
