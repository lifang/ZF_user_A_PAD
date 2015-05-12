package com.example.zf_pad.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zf_pad.MyApplication;
import com.example.zf_pad.R;
import com.example.zf_pad.trade.API;
import com.example.zf_pad.trade.common.HttpCallback;
import com.example.zf_pad.util.MyToast;
import com.example.zf_pad.util.StringUtil;
import com.example.zf_pad.util.TitleMenuUtil;
import com.google.gson.reflect.TypeToken;

public class ChangePhone extends Activity implements OnClickListener{

	private EditText login_edit_name,login_edit_name1,login_edit_name2;
	private Button btn_exit;
	private TextView tv_msg;
	String phoneOld,phoneCode,phone2,getPhoneCode;
	private LinearLayout login_Layout_name2;
	private View view_x1,view_x2;	

	private int Countmun=120;
	private Boolean isRun=true;
	private int index;
	private Runnable runnable;
	final Handler handler = new Handler(){          // handle  
		public void handleMessage(Message msg){  
			switch (msg.what) {  
			case 1:  
				if(Countmun==0){

					isRun=false;
					tv_msg.setClickable(true);

					tv_msg.setText("������֤��");
					System.out.println("destroy`"+Countmun);
				}else{
					Countmun--;  
					tv_msg.setText(  Countmun+"������·���");  
					System.out.println("Countmun`D2`"+Countmun);
				}
			}  
			super.handleMessage(msg);  
		}  
	}; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.changephone);
		MyApplication.getInstance().addActivity(this);
		index=getIntent().getIntExtra("key", 1);
		String name =getIntent().getStringExtra("name");

		new TitleMenuUtil(ChangePhone.this, "�޸��ֻ���").show();
		login_edit_name=(EditText) findViewById(R.id.login_edit_name);
		login_Layout_name2 = (LinearLayout) findViewById(R.id.login_Layout_name2);
		view_x1 = findViewById(R.id.view_x1);
		view_x2 = findViewById(R.id.view_x2);
		if (StringUtil.isNull(name)) {
			login_edit_name.setVisibility(View.VISIBLE);
			login_edit_name.setEnabled(true);
			login_Layout_name2.setVisibility(View.GONE);
			view_x1.setVisibility(View.GONE);
			view_x2.setVisibility(View.GONE);
		}else {
			login_edit_name.setVisibility(View.VISIBLE);
			login_edit_name.setEnabled(false);
			login_Layout_name2.setVisibility(View.VISIBLE);
			view_x1.setVisibility(View.VISIBLE);
			view_x2.setVisibility(View.VISIBLE);
		}

		login_edit_name1=(EditText) findViewById(R.id.login_edit_name1);
		login_edit_name2=(EditText) findViewById(R.id.login_edit_name2);
		tv_msg = (TextView) findViewById(R.id.tv_msg);
		btn_exit=(Button) findViewById(R.id.btn_exit);
		btn_exit.setOnClickListener(this);
		tv_msg.setOnClickListener(this);
		login_edit_name.setText(name);

		runnable = new Runnable() {  
			@Override  
			public void run() {  
				if(Countmun==0){

					Countmun=120;
					tv_msg.setClickable(true);
					tv_msg.setText("������֤��");
				}else{

					Countmun--;  
					tv_msg.setText( Countmun+"������·���");  

					handler.postDelayed(this, 1000);  
				}
			}  
		};
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_exit:
			if(check()){
				Intent intent2 = new Intent();
				intent2.putExtra("text", phone2);
				setResult(index, intent2);
				finish();
			}
			break;
		case R.id.tv_msg:
			if (!StringUtil.isNull(login_edit_name.getText().toString().trim())) {
				API.getPhoneCode(ChangePhone.this, login_edit_name.getText().toString().trim(),
						new HttpCallback(ChangePhone.this) {		       
					@Override
					public void onSuccess(Object data) {
						tv_msg.setClickable(false);
						getPhoneCode = data+"";
						handler.postDelayed(runnable, 1000); 
						MyToast.showToast(getApplicationContext(),"��֤�뷢�ͳɹ�");
					}
					@Override
					public TypeToken getTypeToken() {
						return null;
					}
				});
			}else {
				MyToast.showToast(getApplicationContext(),"������ԭ�ֻ���");
			}
			break;
		default:
			break;
		}
	}

	private boolean check () {
		phoneOld=StringUtil.replaceBlank(login_edit_name.getText().toString());
		phoneCode=StringUtil.replaceBlank(login_edit_name1.getText().toString());
		phone2=StringUtil.replaceBlank(login_edit_name2.getText().toString());
		if (phoneOld.length() == 0) {
			MyToast.showToast(getApplicationContext(),"����������ԭ�ֻ���");
			return false;
		}

		if (phoneCode.length() == 0) {
			MyToast.showToast(getApplicationContext(),"��������֤��");
			return false;
		}
		if (!phoneCode.equals(getPhoneCode)) {
			MyToast.showToast(getApplicationContext(),"��֤�����");
			return false;
		}
		if (phone2.length() == 0) {
			MyToast.showToast(getApplicationContext(),"�������������ֻ���");
			return false;
		}
		if (!StringUtil.isMobile(phone2)) {
			MyToast.showToast(getApplicationContext(),"��������ȷ���ֻ�����");
			return false;
		}
		return true;
	}
}
