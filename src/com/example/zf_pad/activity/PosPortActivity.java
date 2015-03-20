package com.example.zf_pad.activity;



import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.zf_pad.Config;
import com.example.zf_pad.MyApplication;
import com.example.zf_pad.R;
import com.example.zf_pad.aadpter.PosPortAdapter;
import com.example.zf_pad.entity.PosItem;
import com.example.zf_pad.entity.PosSelectEntity;
import com.example.zf_pad.entity.PostPortEntity;
import com.example.zf_pad.util.TitleMenuUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.Toast;

public class PosPortActivity extends Activity {
	public ExpandableListView listView;
	private List<PostPortEntity> portlist=new ArrayList<PostPortEntity>();
	private List<PostPortEntity> glist = new ArrayList<PostPortEntity>();
	private PosSelectEntity pse;
	private PosSelectEntity pse1;
	private PosPortAdapter myadapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pos_port1);
		new TitleMenuUtil(this, "筛选").show();
		initView();
		getData();
	}
	private void getData() {
		RequestParams params = new RequestParams("city_id", 1);	 
		params.setUseJsonStreamer(true);
		MyApplication.getInstance().getClient()
				.post(Config.POSPORT, params, new AsyncHttpResponseHandler() {

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
							if(a==1){  				 
								String res =jsonobject.getString("result");			
								pse= gson.fromJson(res, new TypeToken <PosSelectEntity> () {
			 					}.getType());
								
								pse1=gson.fromJson(res, new TypeToken <PosSelectEntity> () {
			 					}.getType());
								initData();
							}else{
								code = jsonobject.getString("message");
								Toast.makeText(getApplicationContext(), code, 1000).show();
							}
						} catch (JSONException e) {	
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

	protected void initData() {
		PostPortEntity pe=new PostPortEntity();
		pe.setTitle("Pos品牌");
		pe.setChildlist(pse.getBrands());
		portlist.add(pe);

		PostPortEntity pe1=new PostPortEntity();
		pe1.setTitle("Pos类型");
		//pe1.setChildlist(pse.getBrands());
		portlist.add(pe1);

		PostPortEntity pe2=new PostPortEntity();
		pe2.setTitle("支付通道");
		pe2.setChildlist(pse.getPay_channel());
		portlist.add(pe2);

		PostPortEntity pe3=new PostPortEntity();
		pe3.setTitle("支付卡类型");
		pe3.setChildlist(pse.getPay_card());
		portlist.add(pe3);

		PostPortEntity pe4=new PostPortEntity();
		pe4.setTitle("支付交易类型");
		pe4.setChildlist(pse.getTrade_type());
		portlist.add(pe4);

		PostPortEntity pe5=new PostPortEntity();
		pe5.setTitle("签购单方式");
		pe5.setChildlist(pse.getSale_slip());
		portlist.add(pe5);

		PostPortEntity pe6=new PostPortEntity();
		pe6.setTitle("到账日期");
		pe6.setChildlist(pse.gettDate());
		portlist.add(pe6);
		
		PostPortEntity pe0=new PostPortEntity();
		pe0.setTitle("Pos品牌");
		pe0.setChildlist(pse1.getBrands());
		glist.add(pe0);

		PostPortEntity pe7=new PostPortEntity();
		pe7.setTitle("Pos类型");
		//pe1.setChildlist();
		glist.add(pe7);

		PostPortEntity pe8=new PostPortEntity();
		pe8.setTitle("支付通道");
		pe8.setChildlist(pse1.getPay_channel());
		glist.add(pe8);

		PostPortEntity pe9=new PostPortEntity();
		pe9.setTitle("支付卡类型");
		pe9.setChildlist(pse1.getPay_card());
		glist.add(pe9);

		PostPortEntity pe10=new PostPortEntity();
		pe10.setTitle("支付交易类型");
		pe10.setChildlist(pse1.getTrade_type());
		glist.add(pe10);

		PostPortEntity pe11=new PostPortEntity();
		pe11.setTitle("签购单方式");
		pe11.setChildlist(pse1.getSale_slip());
		glist.add(pe11);

		PostPortEntity pe12=new PostPortEntity();
		pe12.setTitle("到账日期");
		pe12.setChildlist(pse1.gettDate());
		glist.add(pe12);

		myadapter.change();
		myadapter.notifyDataSetChanged();
		
	}

	private void initView() {
		listView = (ExpandableListView)findViewById(R.id.list);
		myadapter = new PosPortAdapter(this,portlist,glist);
		myadapter.setListView(listView);
		listView.setAdapter(myadapter);
	}

}