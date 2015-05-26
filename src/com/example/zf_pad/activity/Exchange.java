package com.example.zf_pad.activity;

import java.net.URI;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.zf_pad.BaseActivity;
import com.example.zf_pad.Config;
import com.example.zf_pad.MyApplication;
import com.epalmpay.userPad.R;
import com.example.zf_pad.trade.API;
import com.example.zf_pad.trade.common.CommonUtil;
import com.example.zf_pad.trade.common.DialogUtil;
import com.example.zf_pad.trade.common.HttpCallback;
import com.example.zf_pad.util.StringUtil;
import com.example.zf_pad.util.TitleMenuUtil;
import com.example.zf_pad.util.Tools;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

public class Exchange extends BaseActivity implements OnClickListener{
	private TextView next_sure,tv_xyjf,tv_sxf;
	private EditText et_name,et_tel,t_y;
	private String name,phone,prices;
	private Button btn_exit;
	private int price1,sxfmoney;
	private int customerId=MyApplication.NewUser.getId();
	private JSONObject js;
	private Handler myHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_exchange);
		initView();
		getscore();
		//price1=getIntent().getIntExtra("price", 0);
		
	}
@Override
protected void onStart() {
	// TODO Auto-generated method stub
	super.onStart();
	myHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				tv_xyjf.setText(price1+"");
				tv_sxf.setText(tv_sxf.getText().toString()+String.valueOf(sxfmoney));
				break;

			default:
				break;
			}
		};
	};
}
 private void getscore() {
	 if(!Tools.isConnect(getApplicationContext())){
			CommonUtil.toastShort(getApplicationContext(), "网络异常");
			return;
		}
	  RequestParams params = new RequestParams();
		Gson gson = new Gson();
	
			params.put("customer_id", customerId);
		
	  
		params.setUseJsonStreamer(true);
		System.out.println("---"+params.toString());
	 MyApplication.getInstance().getClient()
		.post(API.GET_SCORE, params,new AsyncHttpResponseHandler() {
			private Dialog loadingDialog;

			@Override
			public void onStart() {	
				super.onStart();
				loadingDialog = DialogUtil.getLoadingDialg(Exchange.this);
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
					int a =jsonobject.getInt("code");
					if(a==Config.CODE){  
						String res =jsonobject.getString("result");
						jsonobject = new JSONObject(res);
						price1=jsonobject.getInt("quantityTotal");
						sxfmoney=jsonobject.getInt("dh_total");
					    myHandler.sendEmptyMessage(1);
					}else{
						code = jsonobject.getString("message");
						Toast.makeText(getApplicationContext(), code, 1000).show();
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
				System.out.println("-onFailure---");
				Log.e("print", "-onFailure---" + error);
			}
		});
		
	}

	private void initView() {
		tv_sxf=(TextView) findViewById(R.id.tv_sxf);
		tv_xyjf=(TextView) findViewById(R.id.tv_xyjf);
		et_name=(EditText) findViewById(R.id.et_name1);
		et_tel=(EditText) findViewById(R.id.et_tel);
		t_y=(EditText) findViewById(R.id.et_y1);
		next_sure=(TextView) findViewById(R.id.next_sure);
		next_sure.setOnClickListener(this);
		btn_exit=(Button) findViewById(R.id.btn_exit);
		btn_exit.setOnClickListener(this);
		next_sure.setText("提交");
		next_sure.setVisibility(View.VISIBLE);
		new TitleMenuUtil(Exchange.this, "兑换积分").show();
		 
		
	}

	private void getData() {
		if(!Tools.isConnect(getApplicationContext())){
			CommonUtil.toastShort(getApplicationContext(), "网络异常");
			return;
		}
		name=et_name.getText().toString();
		phone=et_tel.getText().toString();
		prices=t_y.getText().toString();
		//int price=Integer.parseInt(prices);
		RequestParams params = new RequestParams();
		
		if(StringUtil.replaceBlank(name).length()==0){
			Toast.makeText(getApplicationContext(), "请输入姓名", 1000).show();
			return;
		}
		if(StringUtil.replaceBlank(phone).length()==0){
			Toast.makeText(getApplicationContext(), "请输入手机号", 1000).show();
			return;
		}
		if(StringUtil.replaceBlank(prices).length()==0){
			Toast.makeText(getApplicationContext(), "请输入金额", 1000).show();
			return;
		}
		if(Integer.parseInt(t_y.getText().toString())>sxfmoney){
			CommonUtil.toastShort(Exchange.this, "超过最大可兑换积分数");
			return;
		}
		
		API.exchange(Exchange.this, customerId, name, phone, Integer.parseInt(prices), new HttpCallback(Exchange.this) {
			@Override
			public void onSuccess(Object data) {
				Toast.makeText(getApplicationContext(), "兑换成功", 1000).show();
				
			}

			@Override
			public TypeToken getTypeToken() {
				// TODO Auto-generated method stub
				return null;
			}
		});

	 
	
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.next_sure:
		
			getData();
			break;
		case R.id.btn_exit:
		
			getData();
			break;
		default:
			break;
		}
	}
}

