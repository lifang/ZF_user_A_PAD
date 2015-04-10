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
import com.example.zf_pad.aadpter.AddressManagerAdapter;
import com.example.zf_pad.activity.AdressEdit;
import com.example.zf_pad.entity.AddressManager;
import com.example.zf_pad.entity.OrderEntity;
import com.example.zf_pad.trade.API;
import com.example.zf_pad.trade.common.CommonUtil;
import com.example.zf_pad.trade.common.DialogUtil;
import com.example.zf_pad.util.Tools;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Mine_Address extends Fragment implements OnClickListener{
	private View view;
	public static List<AddressManager> dataadress;
	
	private BaseAdapter addressadapter;
	private ListView list;
	private Context context;
	private Button btn_add;
	public static boolean isclickitem=false;
	public static LinearLayout ll_address;
	public static Handler myHandler;
	private int j=0;
	private int id=MyApplication.NewUser.getId();
	public static int[] idd;
	public static int type=0;
	//private TextView info,safe,manageradress,score;
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	if (view != null) {
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null)
			 parent.removeView(view);
	}
	try {
		view = inflater.inflate(R.layout.manageradress, container, false);
		
	} catch (InflateException e) {
	
	}
	return view;
}
@Override
public void onStart() {
	// TODO Auto-generated method stub
	super.onStart();
	init();
	getData();
	myHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==1){
				isclickitem=true;
				Intent intent=new Intent(getActivity(),AdressEdit.class);
				intent.putExtra("position", AddressManagerAdapter.pp);
				startActivity(intent);
			}
		};
	};
}
private void getData() {
	if(!Tools.isConnect(getActivity())){
		CommonUtil.toastShort(getActivity(), "Õ¯¬Á“Ï≥£");
		return;
	}
		MyApplication.getInstance().getClient().post(API.GET_ADRESS+id, new AsyncHttpResponseHandler() {
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
				Mine_Address.isclickitem=false;
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
						JSONArray result =jsonobject.getJSONArray("result");
						idd=new int[result.length()];
						for(int i=0;i<result.length();i++){
							type=result.getJSONObject(i).getInt("isDefault");
							Log.e("type", String.valueOf(type));
			               /* if(result.getJSONObject(i).getInt("isDefault")==1){
			                	Message msg=AddressManagerAdapter.myHandler.obtainMessage();
			                	msg.what=1;
			                	msg.sendToTarget();
			                }*/
							idd[i]=result.getJSONObject(i).getInt("id");
							if(result.getJSONObject(i).getInt("isDefault")==1){
								dataadress.add(new AddressManager(i, 
										result.getJSONObject(i).getString("receiver"),
										result.getJSONObject(i).getString("city"),
										result.getJSONObject(i).getString("address"),
										result.getJSONObject(i).getString("zipCode"),
										result.getJSONObject(i).getString("moblephone"),
										"ƒ¨»œ"));
							}
							else{
								dataadress.add(new AddressManager(i, 
										result.getJSONObject(i).getString("receiver"),
										result.getJSONObject(i).getString("city"),
										result.getJSONObject(i).getString("address"),
										result.getJSONObject(i).getString("zipCode"),
										result.getJSONObject(i).getString("moblephone"),
										"  "));
							}
							/*dataadress.add(new AddressManager(i, 
									result.getJSONObject(i).getString("receiver"),
									result.getJSONObject(i).getString("city"), 
									result.getJSONObject(i).getString("address"), 
									result.getJSONObject(i).getString("zipCode"),
									result.getJSONObject(i).getString("moblephone")));*/
							
						}
					
						list.setAdapter(addressadapter);
					}
					else{
						
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(getActivity(), String.valueOf(j), Toast.LENGTH_SHORT).show();
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
private void init() {
	ll_address=(LinearLayout) view.findViewById(R.id.ll_address);
	dataadress=new ArrayList<AddressManager>();
	addressadapter=new AddressManagerAdapter(dataadress, getActivity().getBaseContext());
	list=(ListView) view.findViewById(R.id.list);
	btn_add=(Button) view.findViewById(R.id.btn_add);
	btn_add.setOnClickListener(this);
	}
@Override
public void onClick(View v) {
	switch (v.getId()) {
	case R.id.btn_add:
	Intent intent=new Intent(getActivity(),AdressEdit.class);	
	startActivity(intent);
		break;

	default:
		break;
	}
	
}
}
