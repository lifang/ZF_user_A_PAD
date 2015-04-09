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
import com.example.zf_pad.R;
import com.example.zf_pad.aadpter.ApplySearch;
import com.example.zf_pad.entity.ApplySerch;
import com.example.zf_pad.trade.API;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
	private LinearLayout ll_plan;
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
			init();
		} catch (InflateException e) {
		
		}
		return view;
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		ll_plan.setVisibility(View.GONE);
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ll_plan.setVisibility(View.VISIBLE);
	}
	private void init() {
		ll_plan=(LinearLayout) view.findViewById(R.id.ll_plan);
		apply_progress_tips=(TextView) view.findViewById(R.id.apply_progress_tips);
		et_process=(EditText) view.findViewById(R.id.et_process);
		btn_serch=(Button) view.findViewById(R.id.btn_serch);
		btn_serch.setOnClickListener(this);
		dataser=new ArrayList<ApplySerch>();
		applyadapter=new ApplySearch(dataser, getActivity().getBaseContext());
		lv_result=(ListView) view.findViewById(R.id.lv_result);
		lv_result.setAdapter(applyadapter);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_serch:
			RequestParams params = new RequestParams();
			params.put("id", 80);
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
										ddd[j]="�����";
									}
									else if(sss[j]==2){
										ddd[j]="�ѿ�ͨ";
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
			/*API.queryApplyProgress(getActivity(), Constants.TEST_CUSTOMER, 
					et_process.getText().toString() , new HttpCallback(getActivity()) {});*/
			/*API.queryApplyProgress(getActivity(), Constants.TEST_CUSTOMER, et_process.getText().toString(), 
					new HttpCallback<List<ApplySerch>>(getActivity()) {

						@Override
						public void onSuccess(List<ApplySerch> data) {
							Log.e("data", String.valueOf(data));
							
						}

						@Override
						public TypeToken<List<ApplySerch>> getTypeToken() {
							// TODO Auto-generated method stub
							return new TypeToken<List<ApplySerch>>() {
							};
						}
					});
*/
						

					
						/*

						@Override
						public void onSuccess(List<ApplySerch> data) {
							if(data.size()!=0){
								Log.e("dataser", String.valueOf(data));
							}
							dataser.clear();
							if(data.size()!=0){
								dataser.addAll(data);
								Log.e("dataser", String.valueOf(dataser));
								applyadapter.notifyDataSetChanged();
								lv_result.setAdapter(applyadapter);
							}
							else{
								
							}
							
						}

						@Override
						public TypeToken<List<ApplySerch>> getTypeToken() {
							return new TypeToken<List<ApplySerch>>() {
							};
						}*/
			break;

		default:
			break;
		}
		
	}
}
