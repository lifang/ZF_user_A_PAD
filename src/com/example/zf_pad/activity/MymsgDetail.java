package com.example.zf_pad.activity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zf_pad.BaseActivity;
import com.example.zf_pad.Config;
import com.example.zf_pad.MyApplication;
import com.epalmpay.userPad.R;
import com.example.zf_pad.entity.MessageEntity;
import com.example.zf_pad.util.DialogUtil;
import com.example.zf_pad.util.DialogUtil.CallBackChange;
import com.example.zf_pad.util.StringUtil;
import com.example.zf_pad.util.TitleMenuUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MymsgDetail extends BaseActivity {
	private TextView tv_titel, tv_time, tv_content;
	private int id;
	private String url;
	private ImageView search;
	private RequestParams params;
	private String type;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_detail);
		
		if(getIntent().getStringExtra("id")!=null||getIntent().getStringExtra("id")!="")
		id = Integer.parseInt(getIntent().getStringExtra("id"));
		if(getIntent().getStringExtra("type")!=null)
			type=getIntent().getStringExtra("type");
		if(type!=null){
			new TitleMenuUtil(MymsgDetail.this, "消息详情").show();
			if(!StringUtil.isNull(Config.notificationMsgID)){
				if (id == Integer.valueOf(Config.notificationMsgID)) {
					Config.notificationMsgID = "";
				}
			}
		}else{
			new TitleMenuUtil(MymsgDetail.this, "系统公告").show();
		}
		new TitleMenuUtil(MymsgDetail.this, "消息详情").show();
		initView();
		getData();
	}

	private void getData() {
		Map<String, Object> params;
		if(type==null){
		//	params = new RequestParams();
			params = new HashMap<String, Object>();
			params.put("customer_id", MyApplication.NewUser.getId());
			params.put("id", id);
			//params.setUseJsonStreamer(true);
			url=Config.getMSGById;
		}else{
//			params = new RequestParams();
			params = new HashMap<String, Object>();
			params.put("id", id);
			//params.setUseJsonStreamer(true);
			url=Config.SYSMSGDT;	
		}
		JSONObject jsonParams = new JSONObject(params);
		HttpEntity entity;
		try {
			entity = new StringEntity(jsonParams.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return;
		}
		MyApplication.getInstance().getClient()
		.post(getApplicationContext(),url, null,entity,"application/json", new AsyncHttpResponseHandler(){
		//.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				System.out.println("-onSuccess---");
				String responseMsg = new String(responseBody).toString();
				Log.e("LJP", responseMsg);
				Gson gson = new Gson();
				JSONObject jsonobject = null;
				int code = 0;
				try {
					jsonobject = new JSONObject(responseMsg);

					code = jsonobject.getInt("code");

					if (code == -2) {
						Toast.makeText(getApplicationContext(),
								jsonobject.getString("message"),
								Toast.LENGTH_SHORT).show();
					} else if (code == 1) {

						String res = jsonobject.getString("result");
						System.out.println("`res``" + res);
						jsonobject = new JSONObject(res);

						MessageEntity a = gson.fromJson(res,
								new TypeToken<MessageEntity>() {
								}.getType());
						tv_titel.setText(a.getTitle());
						tv_time.setText(a.getCreate_at());
						tv_content.setText(a.getContent());

					} else {
						Toast.makeText(getApplicationContext(),
								jsonobject.getString("message"),
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
				error.printStackTrace();
			}
		});

	}

	private void delOne() {
		//params = new RequestParams();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customer_id", MyApplication.NewUser.getId());
		params.put("id", id);
		//params.setUseJsonStreamer(true);
		JSONObject jsonParams = new JSONObject(params);
		HttpEntity entity;
		try {
			entity = new StringEntity(jsonParams.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return;
		}
		MyApplication.getInstance().getClient()
		.post(getApplicationContext(),Config.MSGEDLONE, null,entity,"application/json", new AsyncHttpResponseHandler(){
		//.post(Config.MSGEDLONE, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				System.out.println("-onSuccess---");
				String responseMsg = new String(responseBody).toString();
				Log.e("LJP", responseMsg);
				Gson gson = new Gson();
				JSONObject jsonobject = null;
				int code = 0;
				try {
					jsonobject = new JSONObject(responseMsg);

					code = jsonobject.getInt("code");

					if (code == -2) {

					} else if (code == 1) {

						MymsgDetail.this.finish();

					} else {
						Toast.makeText(getApplicationContext(),
								jsonobject.getString("message"),
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
				error.printStackTrace();
				System.out.println("-onFailure---");
			}
		});

		
	}
	private void initView() {

		tv_titel = (TextView) findViewById(R.id.msg_title);
		tv_time = (TextView) findViewById(R.id.msg_time);
		tv_content = (TextView) findViewById(R.id.msg_conten);
		search = (ImageView) findViewById(R.id.search);
		if(type==null)
		search.setVisibility(View.VISIBLE);
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Dialog ddd = new DialogUtil(MymsgDetail.this, "确认删除？")
						.getCheck(new CallBackChange() {

							@Override
							public void change() {
								delOne();

							}

						});
				ddd.show();
			}
		});

	}

}
