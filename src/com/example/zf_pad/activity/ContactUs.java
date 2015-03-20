package com.example.zf_pad.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.zf_pad.MyApplication;
import com.example.zf_pad.R;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class ContactUs extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m_lxwm);
		
		getdate();
	}
void getdate(){
	RequestParams params = new RequestParams();
	params.put("name","是");
	params.put("phone","22");
	params.put("content","22");
	//paychannelId
	params.setUseJsonStreamer(true);
	MyApplication.getInstance().getClient().post("http://114.215.149.242:18080/ZFMerchant/api/paychannel/intention/add", params, new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			String userMsg = new String(responseBody).toString();
 
			Log.i("ljp", userMsg);
			Gson gson = new Gson();
			//EventEntity
			JSONObject jsonobject = null;
			int code = 0;
			try {
				jsonobject = new JSONObject(userMsg);
				code = jsonobject.getInt("code");
				if(code==-2){
				
				}else if(code==1){
					Toast.makeText(getApplicationContext(), "成功",
							Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(getApplicationContext(), jsonobject.getString("message"),
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
			Toast.makeText(getApplicationContext(), headers.toString()+responseBody.toString()+"失败",
					Toast.LENGTH_SHORT).show();
			Log.e(headers.toString()+responseBody.toString()+headers.toString()+responseBody.toString(),"吾问无为谓吾问无为谓");
		}
	});
}
}
