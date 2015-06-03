package com.example.zf_pad.fragment;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.zf_pad.MyApplication;
import com.epalmpay.userPad.R;
import com.example.zf_pad.aadpter.ApplySearch;
import com.example.zf_pad.entity.ApplySerch;
import com.example.zf_pad.trade.API;
import com.example.zf_pad.trade.common.CommonUtil;
import com.example.zf_pad.trade.common.DialogUtil;
import com.example.zf_pad.trade.common.HttpCallback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class Mine_Plan extends Fragment implements OnClickListener{
	private View view;
	private List<ApplySerch> dataser;
	private BaseAdapter applyadapter;
	private ListView lv_result;
	private Button btn_serch;
	private EditText et_process;
	private TextView apply_progress_tips;
	private int id=MyApplication.NewUser.getId();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		/*if (view == null) {
			view = inflater.inflate(R.layout.f_mine_plan, null);	
		}*/
		if (view != null) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null)
				 parent.removeView(view);
		}
		try {
			view = inflater.inflate(R.layout.f_mine_plan, container, false);
			//解决Fragment内点击穿透问题
			view.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					return true;
				}
			});
			init();
		} catch (InflateException e) {
		
		}
		return view;
	}
	
	private void init() {
		apply_progress_tips=(TextView) view.findViewById(R.id.apply_progress_tips);
		et_process=(EditText) view.findViewById(R.id.et_process);
		btn_serch=(Button) view.findViewById(R.id.btn_serch);
		btn_serch.setOnClickListener(this);
		dataser=new ArrayList<ApplySerch>();
		applyadapter=new ApplySearch(dataser, getActivity().getBaseContext());
		lv_result=(ListView) view.findViewById(R.id.lv_result);
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_serch:
			if(et_process.getText().toString().equals("")){
				CommonUtil.toastShort(getActivity(), "手机号码不可为空");
				return;
			}
			RequestParams params = new RequestParams();
			params.put("id", id);
			params.put("phone", et_process.getText().toString());
			params.setUseJsonStreamer(true);
		
			MyApplication.getInstance().getClient().post(API.APPLY_PROGRESS, params, new AsyncHttpResponseHandler() {
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
					dataser.clear();
			        Log.e("print", responseMsg); 
			        Gson gson = new Gson();						
			        JSONObject jsonobject = null;
			        try {
						jsonobject = new JSONObject(responseMsg);
						
							JSONArray result=jsonobject.getJSONArray("result");
							if(result.length()!=0){
								apply_progress_tips.setVisibility(View.GONE);
								lv_result.setVisibility(View.VISIBLE);
							Log.e("result", String.valueOf(result));
							String[]num=new String[3];
							String[] status=new String[6];
							for(int i=0;i<result.length();i++){
								
								num[i]=result.getJSONObject(i).getString("serial_num");
								JSONArray statu=result.getJSONObject(i).getJSONArray("openStatus");
								Log.e("statu", String.valueOf(statu));
								
								int[] sss=new int[statu.length()];
								String[] ddd=new String[statu.length()];
								String[] aaa=new String[statu.length()];
								for(int j=0;j<statu.length();j++){
									sss[j]=statu.getJSONObject(j).getInt("status");
									if(sss[j]==1){
										ddd[j]="未开通";
									}
									else if(sss[j]==2){
										ddd[j]="审核中";
									}
									else if(sss[j]==3){
										ddd[j]="已开通";
									}
									aaa[j]=statu.getJSONObject(j).getString("trade_value");
									Log.e("aaa", String.valueOf(aaa[j]));
									Log.e("ddd", String.valueOf(ddd[j]));
									
								}
								
								if(aaa.length==1){
									dataser.add(new ApplySerch(i, result.getJSONObject(i).getString("serial_num"),
											aaa[0], ddd[0],
											"","",
											"",""));
								}
								else if(aaa.length==2){
									dataser.add(new ApplySerch(i, result.getJSONObject(i).getString("serial_num"),
											aaa[0], ddd[0], 
											aaa[1], ddd[1], 
											"", ""));
								}
								else if(aaa.length==3){
									dataser.add(new ApplySerch(i, result.getJSONObject(i).getString("serial_num"),
											aaa[0], ddd[0], 
											aaa[1], ddd[1], 
											aaa[2], ddd[2]));
								}
								else{
									dataser.add(new ApplySerch(i, result.getJSONObject(i).getString("serial_num"),
											"", "", 
											"", "", 
											"", ""));
								}
								
							}
							lv_result.setAdapter(applyadapter);
						}
							else{
								apply_progress_tips.setVisibility(View.VISIBLE);
								lv_result.setVisibility(View.GONE);
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
			break;

		default:
			break;
		}
		
	}
}
