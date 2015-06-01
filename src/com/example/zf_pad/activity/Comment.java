package com.example.zf_pad.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zf_pad.BaseActivity;
import com.example.zf_pad.Config;
import com.example.zf_pad.MyApplication;
import com.epalmpay.userPad.R;
import com.example.zf_pad.aadpter.CommentAdapter;
import com.example.zf_pad.entity.Answer;
import com.example.zf_pad.entity.Goodlist;
import com.example.zf_pad.util.ScrollViewWithListView;
import com.example.zf_pad.util.TitleMenuUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class Comment extends BaseActivity{
	private ScrollViewWithListView lv;
	private Button btn_pay;
	private TextView next_sure;
	//Answer
	List<Answer>  as = new ArrayList<Answer>();
	private CommentAdapter posAdapter;
	List<Goodlist>  goodlist = new ArrayList<Goodlist>();
	private int id;
	private Button bt_close;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ordercomment);
		lv=(ScrollViewWithListView) findViewById(R.id.pos_lv1);
		goodlist=Config.list;
		posAdapter=new CommentAdapter(Comment.this,goodlist);
		lv.setAdapter(posAdapter);
		id = Config.GoodComment;
		bt_close = (Button)findViewById(R.id.close);
		bt_close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Comment.this.finish();
			}
		});
		btn_pay=(Button) findViewById(R.id.btn_pay);
		btn_pay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				submit();
			}	 
		});
	}
	public void submit(){
		for(int i=0;i<goodlist.size();i++){
			Answer aaa=new Answer();
 			aaa.setContent(goodlist.get(i).getContent());
 			aaa.setCustomer_id(MyApplication.NewUser.getId());
 			aaa.setGood_id(Integer.parseInt( goodlist.get(i).getGood_id()));
 			aaa.setScore(Integer.parseInt( goodlist.get(i).getScore()));
 			as.add(aaa);
			System.out.println(goodlist.get(i).getScore()+"---submit---"+goodlist.get(i).getContent()+"id-"+goodlist.get(i).getGood_id());
		}
		Map<String, Object> params = new HashMap<String, Object>();

		Gson gson = new Gson();
		try {
			params.put("json", new JSONArray(gson.toJson(as)));
			params.put("id", id);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	  
		JSONObject jsonParams = new JSONObject(params);
		HttpEntity entity;
		try {
			entity = new StringEntity(jsonParams.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {


			return;
		}
		String url = Config.Comment;
		//RequestParams params = new RequestParams();
	
		//params.setUseJsonStreamer(true);
		System.out.println("---"+params.toString());
		MyApplication.getInstance().getClient()
				.post(getApplicationContext(),url, null,entity,"application/json", new AsyncHttpResponseHandler() {

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
								Toast.makeText(getApplicationContext(), "评论成功", 1000).show();
								finish();
							}else{ 
								
								Toast.makeText(getApplicationContext(), "评论失败", 1000).show();
								finish();
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
}
