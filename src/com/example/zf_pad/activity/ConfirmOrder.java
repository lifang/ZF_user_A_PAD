package com.example.zf_pad.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zf_pad.BaseActivity;
import com.example.zf_pad.Config;
import com.example.zf_pad.MyApplication;
import com.epalmpay.userPad.R;
import com.example.zf_pad.aadpter.ChooseAdressAdapter;
import com.example.zf_pad.aadpter.ShopcarOrderAdapter;
import com.example.zf_pad.entity.AdressEntity;
import com.example.zf_pad.entity.MyShopCar.Good;
import com.example.zf_pad.entity.TestEntitiy;
import com.example.zf_pad.trade.API;
import com.example.zf_pad.trade.common.HttpCallback;
import com.example.zf_pad.util.ScrollViewWithListView;
import com.example.zf_pad.util.StringUtil;
import com.example.zf_pad.util.TitleMenuUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class ConfirmOrder extends BaseActivity implements OnClickListener {
	List<AdressEntity> myList = new ArrayList<AdressEntity>();
	List<AdressEntity> moreList = new ArrayList<AdressEntity>();
	private ScrollViewWithListView pos_lv, his_lv;
	List<TestEntitiy> poslist = new ArrayList<TestEntitiy>();
	private Button btn_pay;
	private List<Good> comfirmList=new ArrayList<Good>();
	private String howMoney,comment,invoice_info;
	private int addressId=-1;
	private int  is_need_invoice=1;//否需要发票（1要，0不要
	private int invoice_type=0;//发票类型（0公司  1个人）
	// private OrderDetail_PosAdapter posAdapter;
	int hj=0;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				// posAdapter.notifyDataSetChanged();

				break;
			case 1:
				Toast.makeText(getApplicationContext(), (String) msg.obj,
						Toast.LENGTH_SHORT).show();

				break;
			case 2: // 网络有问题
				Toast.makeText(getApplicationContext(),
						"no 3g or wifi content", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(getApplicationContext(), " refresh too much",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	private EditText ed_fp;
	private CheckBox ck;
	private ChooseAdressAdapter myAdapter;
	private ScrollViewWithListView sclist;
	private ScrollViewWithListView shop_lsit;
	private ShopcarOrderAdapter shopadapter;
	private TextView tv_count;
	private int index=0;
	private TextView tv_hj;
	private TextView tv_totle;
	private EditText et_comment;
	private Button bt_add;
	private Button bt_mange;
	private LinearLayout order_isshow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_comfirm);
		comfirmList=MyApplication.getComfirmList();
		initView();
	//	getData();
	}

	private void getData() {

		MyApplication
				.getInstance()
				.getClient()
				.post(Config.ChooseAdress + MyApplication.NewUser.getId()+"",
						new AsyncHttpResponseHandler() {

							@Override
							public void onSuccess(int statusCode,
									Header[] headers, byte[] responseBody) {
								String responseMsg = new String(responseBody)
										.toString();
								Log.e("print", responseMsg);
								System.out.println("----" + responseMsg);

								Gson gson = new Gson();

								JSONObject jsonobject = null;
								String code = null;
								try {
									jsonobject = new JSONObject(responseMsg);
									code = jsonobject.getString("code");
									int a = jsonobject.getInt("code");
									if (a == Config.CODE) {
										String res = jsonobject
												.getString("result");

										moreList = gson
												.fromJson(
														res,
														new TypeToken<List<AdressEntity>>() {
														}.getType());

										for(int i =0;i<moreList.size();i++){
		 									if(moreList.get(i).getIsDefault().equals("1")) {
		 										//tv_name,tv_tel,tv_adresss;
		 										addressId=moreList.get(i).getId();
		 										
		 									}
		 								}

										myList.addAll(moreList);
										if(addressId==-1&&myList.size()!=0){
											myList.get(0).setIsDefault("1");
											addressId = myList.get(0).getId();
										}
										
										
										myAdapter.notifyDataSetChanged();
									} else {
										code = jsonobject.getString("message");
										Toast.makeText(getApplicationContext(),
												code, 1000).show();
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									;
									e.printStackTrace();

								}

							}

							@Override
							public void onFailure(int statusCode,
									Header[] headers, byte[] responseBody,
									Throwable error) {
								// TODO Auto-generated method stub
								System.out.println("-onFailure---");
								Log.e("print", "-onFailure---" + error);
							}
						});
	}


	private void initView() {
		order_isshow = (LinearLayout)findViewById(R.id.order_isshow);
		bt_add = (Button)findViewById(R.id.bt_add);
		bt_add.setOnClickListener(this);
		bt_mange = (Button)findViewById(R.id.bt_mange);
		bt_mange.setOnClickListener(this);
		tv_count = (TextView)findViewById(R.id.tv_count);
		tv_totle = (TextView)findViewById(R.id.tv_totle);
		for(Good good:comfirmList){
			index=index+good.getQuantity();
			hj=hj+((good.getRetail_price()+good.getOpening_cost())*good.getQuantity());
		}
		tv_count.setText("共计："+index+"件");
		tv_hj = (TextView)findViewById(R.id.tv_hj);
		tv_hj.setText("合计:￥"+StringUtil.getMoneyString(hj));
		tv_totle.setText("实付:￥"+StringUtil.getMoneyString(hj));
		shop_lsit = (ScrollViewWithListView)findViewById(R.id.shopcar_list);
		shopadapter = new ShopcarOrderAdapter(getApplicationContext(), MyApplication.getComfirmList());
		shop_lsit.setAdapter(shopadapter);
		sclist = (ScrollViewWithListView)findViewById(R.id.pos_lv1);
		myAdapter = new ChooseAdressAdapter(this, myList);
		sclist.setAdapter(myAdapter);
		sclist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				addressId=myList.get(position).getId();
				myAdapter.chang();
				myList.get(position).setIscheck(true);
				myAdapter.notifyDataSetChanged();
			}
		});
	
		new TitleMenuUtil(this, "订单确认").show();
		ck = (CheckBox)findViewById(R.id.item_cb);
		ck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					is_need_invoice = 1;
			
					order_isshow.setVisibility(View.VISIBLE);
				} else {
					is_need_invoice = 0;
					
					order_isshow.setVisibility(View.GONE);
				}
				
			}
		});
		pos_lv = (ScrollViewWithListView) findViewById(R.id.pos_lv1);
		// posAdapter=new OrderDetail_PosAdapter(ConfirmOrder.this, poslist);
		// pos_lv.setAdapter(posAdapter);
		btn_pay = (Button) findViewById(R.id.btn_pay);
		btn_pay.setOnClickListener(this);
		Spinner sp = (Spinner) findViewById(R.id.spinner);
		final String arr[] = new String[] { "公司", "个人" };

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, arr);
        sp.setAdapter(arrayAdapter);  
 
          
        //注册事件  
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {  
  
            @Override  
            public void onItemSelected(AdapterView<?> parent, View view,  
                    int position, long id) {  
                Spinner spinner=(Spinner) parent;  
                //Toast.makeText(getApplicationContext(), "xxxx"+spinner.getItemAtPosition(position), Toast.LENGTH_LONG).show(); 
                invoice_type=position;
            }  
  
            @Override  
            public void onNothingSelected(AdapterView<?> parent) {  
               // Toast.makeText(getApplicationContext(), "没有改变的处理", Toast.LENGTH_LONG).show();  
            }  
  
        });  
        ed_fp = (EditText)findViewById(R.id.et_titel);
    } 
	
	public void getpay(){
		
		et_comment = (EditText)findViewById(R.id.ed_comment);
		comment=et_comment.getText().toString();
		int [] cartid=new int[comfirmList.size()];
		for(int i=0;i<comfirmList.size();i++){
			 
			cartid[i]=comfirmList.get(i).getId();
		}
		if(ck.isChecked()){
			is_need_invoice=1;
			
		}else{
			is_need_invoice=0;
			
		}
		
		//comment=et_comment.getText().toString();
		//invoice_info =et_info.getText().toString();
		invoice_info=ed_fp.getText().toString();
		API.CARTFIRM(ConfirmOrder.this, MyApplication.NewUser.getId(),cartid,
				addressId,comment,is_need_invoice,invoice_type,invoice_info,Config.token,
        		
                new HttpCallback  (ConfirmOrder.this) {

					@Override
					public void onSuccess(Object data) {
						Intent i1 =new Intent (ConfirmOrder.this,PayFromCar.class);
						i1.putExtra("orderId", Integer.valueOf(data.toString()));
						i1.putExtra("type", 1);
						startActivity(i1);
						finish();
					}

					@Override
					public TypeToken getTypeToken() {
						// TODO Auto-generated method stub
						return  null;
					}
                });
	}
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btn_pay:
			getpay();
		
			break;
		case R.id.bt_add:
			startActivity(new Intent(ConfirmOrder.this,AdressEdit.class));
			break;
		case R.id.bt_mange:
			Config.AderssManger=true;
			startActivity(new Intent(ConfirmOrder.this,MainActivity.class));
			break;
		default:
			break;
		}
	}
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		
		super.onSaveInstanceState(outState);
	}
	@Override
	protected void onResume() {
	
		super.onResume();
		myList.clear();
		getData();
	}
}
