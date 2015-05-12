package com.example.zf_pad.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.zf_pad.Config;
import com.example.zf_pad.MyApplication;
import com.example.zf_pad.R;
import com.example.zf_pad.Posport;
import com.example.zf_pad.aadpter.PosPortAdapter;
import com.example.zf_pad.entity.PosItem;
import com.example.zf_pad.entity.PosSelectEntity;
import com.example.zf_pad.entity.PostPortEntity;
import com.example.zf_pad.entity.category;
import com.example.zf_pad.trade.common.DialogUtil;
import com.example.zf_pad.util.TitleMenuUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

public class PosPortActivity extends Activity implements OnClickListener {
	public ExpandableListView listView;
	private List<PostPortEntity> portlist = new ArrayList<PostPortEntity>();
	private List<PostPortEntity> glist = new ArrayList<PostPortEntity>();
	private PosSelectEntity pse;
	private PosSelectEntity pse1;
	private PosPortAdapter myadapter;
	private List<PosItem> types;
	private List types2;
	private Button bt_cancel, bt_confirm;
	private PostPortEntity pe;
	private PostPortEntity pe1;
	private PostPortEntity pe2;
	private PostPortEntity pe3;
	private PostPortEntity pe4;
	private PostPortEntity pe5;
	private PostPortEntity pe6;
	private PostPortEntity pe0;
	private PostPortEntity pe7;
	private PostPortEntity pe8;
	private PostPortEntity pe9;
	private PostPortEntity pe10;
	private PostPortEntity pe11;
	private PostPortEntity pe12;
	private CheckBox has_purchase;
	private boolean isload = false;
	private EditText ed_min;
	private EditText ed_max;
	  private Dialog loadingDialog;
	  private boolean isClick=true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pos_port1);
		new TitleMenuUtil(this, "筛选").show();
		   loadingDialog = DialogUtil.getLoadingDialg(this); 
		   
		   Config.myson=null;
		initView();
		getData();
	}

	private void getData() {
		RequestParams params = new RequestParams("city_id", MyApplication.getCITYID());
		params.setUseJsonStreamer(true);
		MyApplication.getInstance().getClient()
				.post(Config.POSPORT, params, new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						  loadingDialog.show();
					}
					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
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
							int a = jsonobject.getInt("code");
							if (a == 1) {
								String res = jsonobject.getString("result");
								pse = gson.fromJson(res,
										new TypeToken<PosSelectEntity>() {
										}.getType());

								pse1 = gson.fromJson(res,
										new TypeToken<PosSelectEntity>() {
										}.getType());
								isload = true;
								//inittype();
								initData();
								
							} else {
								code = jsonobject.getString("message");
								Toast.makeText(getApplicationContext(), code,
										1000).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						
						System.out.println("-onFailure---");
						Log.e("print", "-onFailure---" + error);
					}
				});
	
	}

	void inittype() {
		types = new ArrayList<PosItem>();
		types2 = new ArrayList<PosItem>();
	/*	for (category c : pse.getCategory()) {

			for (category ch : c.getClist()) {
				PosItem p = new PosItem();
				p.setId(ch.getId());
				p.setValue(ch.getValue());
				types.add(p);
			}
	
		}
		for (category c : pse1.getCategory()) {

			for (category ch : c.getClist()) {
				PosItem p = new PosItem();

				p.setId(ch.getId());
				p.setValue(ch.getValue());
				types2.add(p);
			}
	
		}
*/
	}

	/**
	 * 
	 */
	protected void initData() {
		pe = new PostPortEntity();
		pe.setTitle("Pos品牌");
		pe.setChildlist(pse.getBrands());
		portlist.add(pe);

		pe1 = new PostPortEntity();
		pe1.setTitle("Pos类型");

		//pe1.setChildlist(types);
		pe1.setChildlist(pse.getCategory());
		portlist.add(pe1);

		pe2 = new PostPortEntity();
		pe2.setTitle("支付通道");
		pe2.setChildlist(pse.getPay_channel());
		portlist.add(pe2);

		pe3 = new PostPortEntity();
		pe3.setTitle("支付卡类型");
		pe3.setChildlist(pse.getPay_card());
		portlist.add(pe3);

		pe4 = new PostPortEntity();
		pe4.setTitle("支持交易类型");
		pe4.setChildlist(pse.getTrade_type());
		portlist.add(pe4);

		pe5 = new PostPortEntity();
		pe5.setTitle("签购单方式");
		pe5.setChildlist(pse.getSale_slip());
		portlist.add(pe5);

		pe6 = new PostPortEntity();
		pe6.setTitle("到账日期");
		pe6.setChildlist(pse.gettDate());
		portlist.add(pe6);

		pe0 = new PostPortEntity();
		pe0.setTitle("Pos品牌");
		pe0.setChildlist(pse1.getBrands());
		glist.add(pe0);

		pe7 = new PostPortEntity();
		pe7.setTitle("Pos类型");
		pe7.setChildlist(pse1.getCategory());
		// Toast.makeText(getApplicationContext(),
		// pe1.getChildlist().get(0).getValue()+"ccc", 1000).show();
		glist.add(pe7);

		pe8 = new PostPortEntity();
		pe8.setTitle("支付通道");
		pe8.setChildlist(pse1.getPay_channel());
		glist.add(pe8);

		pe9 = new PostPortEntity();
		pe9.setTitle("支付卡类型");
		pe9.setChildlist(pse1.getPay_card());
		glist.add(pe9);

		pe10 = new PostPortEntity();
		pe10.setTitle("支持交易类型");
		pe10.setChildlist(pse1.getTrade_type());
		glist.add(pe10);

		pe11 = new PostPortEntity();
		pe11.setTitle("签购单方式");
		pe11.setChildlist(pse1.getSale_slip());
		glist.add(pe11);

		pe12 = new PostPortEntity();
		pe12.setTitle("到账日期");
		pe12.setChildlist(pse1.gettDate());
		glist.add(pe12);

		myadapter.change();
		myadapter.notifyDataSetChanged();

	}

	private void initView() {
		ed_min = (EditText) findViewById(R.id.ed_min);
		ed_max = (EditText) findViewById(R.id.ed_max);
		ed_min.setText(String.valueOf(Posport.minPrice));
		ed_max.setText(String.valueOf(Posport.maxPrice));
		has_purchase = (CheckBox) findViewById(R.id.has_purchase);
		has_purchase.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean b) {
				if (b) {
					Posport.has_purchase = 1;
				} else {
					Posport.has_purchase = 0;
				}
			}
		});
		if (Posport.has_purchase == 0) {
			has_purchase.setChecked(false);
		} else {
			has_purchase.setChecked(true);
		}
		bt_cancel = (Button) findViewById(R.id.bt_cancel);
		bt_cancel.setOnClickListener(this);
		bt_confirm = (Button) findViewById(R.id.bt_confirm);
		bt_confirm.setOnClickListener(this);
		listView = (ExpandableListView) findViewById(R.id.list);
		listView.setGroupIndicator(null);
		myadapter = new PosPortAdapter(this, portlist, glist);
		myadapter.setListView(listView);
		listView.setAdapter(myadapter);
	}

	@Override
	public void onClick(View v) {
		
		 
		switch (v.getId()) {
		case R.id.bt_cancel:
			this.finish();
			break;
		case R.id.bt_confirm:
			if(isClick){
				isClick=false;
			initport();
			if(Config.myson==null){
				Config.lx=-1;
			}else{
				Config.lx=Config.myson.getId();
			}
			
			if (isload) {
				// pos品牌
				List<PosItem> tem=new ArrayList<PosItem>();
				for (PosItem p : pe0.getChildlist()) {
					if (p.getIsCheck()) {
						tem.add(p);
					}
				}
				
				for (PosItem p : pe.getChildlist()) {
					if (p.getIsCheck()) {
						tem.add(p);
					}
				}
				
				Posport.brands_id = new int[tem.size()];
				for(int i=0;i<tem.size();i++){
					Posport.brands_id[i]=tem.get(i).getId();
				
				}
				// pos类型
				tem=new ArrayList<PosItem>();
				for (PosItem p : pe1.getChildlist()) {
					if (p.getIsCheck()) {
						tem.add(p);
					
					}
				}
				for (PosItem p : pe7.getChildlist()) {
					if (p.getIsCheck()) {
						tem.add(p);
					}
				}
				Posport.category = new int[tem.size()];
				for(int i=0;i<tem.size();i++){
					Posport.category[i]=tem.get(i).getId();
				
				}
				// 支付通道
				tem=new ArrayList<PosItem>();
				for (PosItem p : pe2.getChildlist()) {
					if (p.getIsCheck()) {
						tem.add(p);
					}
				}
				for (PosItem p : pe8.getChildlist()) {
					if (p.getIsCheck()) {
						tem.add(p);
					}
				}
				Posport.pay_channel_id = new int[tem.size()];
				for(int i=0;i<tem.size();i++){
					Posport.pay_channel_id[i]=tem.get(i).getId();
				
				}
				// 支付卡类型
				tem=new ArrayList<PosItem>();
				for (PosItem p : pe3.getChildlist()) {
					if (p.getIsCheck()) {
						tem.add(p);
		
					}
				}
				for (PosItem p : pe9.getChildlist()) {
					if (p.getIsCheck()) {
						tem.add(p);
					}
				}
				Posport.pay_card_id = new int[tem.size()];
				for(int i=0;i<tem.size();i++){
					Posport.pay_card_id[i]=tem.get(i).getId();
				
				}
				// 支付交易类型
				tem=new ArrayList<PosItem>();
				for (PosItem p : pe4.getChildlist()) {
					if (p.getIsCheck()) {
						tem.add(p);
					}
				}
				for (PosItem p : pe10.getChildlist()) {
					if (p.getIsCheck()) {
						tem.add(p);
					}
				}
				Posport.trade_type_id = new int[tem.size()];
				for(int i=0;i<tem.size();i++){
					Posport.trade_type_id[i]=tem.get(i).getId();
				
				}
				// 签购单方式
				tem=new ArrayList<PosItem>();
				for (PosItem p : pe5.getChildlist()) {
					if (p.getIsCheck()) {
						tem.add(p);
					}
				}
				for (PosItem p : pe11.getChildlist()) {
					if (p.getIsCheck()) {
						tem.add(p);
					}
				}
				Posport.sale_slip_id = new int[tem.size()];
				for(int i=0;i<tem.size();i++){
					Posport.sale_slip_id[i]=tem.get(i).getId();
				
				}
				// 到账日期
				tem=new ArrayList<PosItem>();
				for (PosItem p : pe6.getChildlist()) {
					if (p.getIsCheck()) {
						tem.add(p);
					}
				}
				for (PosItem p : pe12.getChildlist()) {
					if (p.getIsCheck()) {
						tem.add(p);
					}
				}
				Posport.tDate = new int[tem.size()];
				for(int i=0;i<tem.size();i++){
					Posport.tDate[i]=tem.get(i).getId();
				
				}
			}
			if(!check(ed_min.getText().toString())){
				Toast.makeText(getApplicationContext(), "请输入数字", 1000).show();
				break;
			}
			if(!check(ed_max.getText().toString())){
				Toast.makeText(getApplicationContext(), "请输入数字", 1000).show();
				break;
			}
			Posport.minPrice=Double.valueOf(ed_min.getText().toString());
			Posport.maxPrice=Double.valueOf(ed_max.getText().toString());
			String str="";
			for(int i=0;i<Posport.brands_id.length;i++){
				str=Posport.brands_id[i]+"+"+str;
			}
			Log.i("ccccc",str + "");
			Intent intent = new Intent();

			PosPortActivity.this.setResult(0, intent);
			finish();
			}
			break;

		default:
			break;
		}

	}

	private void initport() {
		Posport.brands_id = new int[0];
		Posport.category = new int[0];
		Posport.pay_channel_id = new int[0];
		Posport.pay_card_id = new int[0];
		Posport.trade_type_id = new int[0];
		Posport.sale_slip_id = new int[0];
		Posport.tDate = new int[0];
		Config.lx=-1;

	}

	boolean check(String str) {
		try {
			double min = Double.valueOf(str);// 把字符串强制转换为数字
			return true;// 如果是数字，返回True
		} catch (Exception e) {
			
			return false;// 如果抛出异常，返回False
		}
	}
	@Override
	protected void onResume() {
		//Toast.makeText(getApplicationContext(), Config.portindex+"", 1000).show();
		super.onResume();
		if(Config.portindex!=-1&&Config.myson!=null){
			//pe1.getChildlist().get(Config.portindex).setValue(Config.myson.getValue());
			if(Config.portindex>3){
				pe1.getChildlist().get(Config.portindex-4).setValue(Config.myson.getValue());
			}else{
				pe7.getChildlist().get(Config.portindex).setValue(Config.myson.getValue());
			}
			
		}		
			myadapter.notifyDataSetChanged();

	}
}