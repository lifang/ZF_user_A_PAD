package com.example.zf_pad.activity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zf_pad.Config;
import com.example.zf_pad.MyApplication;
import com.example.zf_pad.R;
import com.example.zf_pad.entity.UserEntity;
import com.example.zf_pad.trade.API;
import com.example.zf_pad.trade.common.HttpCallback;
import com.example.zf_pad.util.StringUtil;
import com.google.gson.reflect.TypeToken;
public class LoginActivity extends Activity implements OnClickListener {
	private String name,pass,url,deviceToken;
	private ImageView loginImage;
	private CheckBox isremeber_cb;
	private Boolean isRemeber = true;
	private TextView login_text_forget, login_info;
	private EditText login_edit_name, login_edit_pass;
	private LinearLayout login_linear_deletename, login_linear_deletepass,zhuche_ll,
			login_linear_login, msg;
	private String sign, pass1, usename, passsword;
	public static SharedPreferences mySharedPreferences;
	private Editor editor;
	private Boolean isFirst;
	private String sessionId;
	public static boolean islogin=false;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				// showDialog();
				break;
			case 1:
				Toast.makeText(getApplicationContext(), (String) msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			case 2: // ����������
				Toast.makeText(getApplicationContext(), R.string.no_internet,
						Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(getApplicationContext(),
						R.string.refresh_toomuch, Toast.LENGTH_SHORT).show();
				break;
			case 4:
				 
				break;
			}
		}
	};
	private Button close;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.popwin_login);
		
		initView();
		//new ClientUpdate(LoginActivity.this).checkSetting();
	
	}

	private void initView() {
		mySharedPreferences = getSharedPreferences(Config.SHARED, MODE_PRIVATE);
		editor = mySharedPreferences.edit();
 
		login_text_forget = (TextView) findViewById(R.id.login_text_forget);
		//login_text_forget.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		login_text_forget.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {			
				Intent i = new Intent(getApplicationContext(),
						FindPass.class);
				startActivity(i);
			}
		});
		msg = (LinearLayout) findViewById(R.id.msg);
		login_info = (TextView) findViewById(R.id.login_info);
		 
		zhuche_ll= (LinearLayout) findViewById(R.id.zhuche_ll);
		zhuche_ll.setOnClickListener(this);

		login_edit_name = (EditText) findViewById(R.id.login_edit_name);
		login_edit_pass = (EditText) findViewById(R.id.login_edit_pass);
		
		login_linear_deletename = (LinearLayout) findViewById(R.id.login_linear_deletename);
		login_linear_deletepass = (LinearLayout) findViewById(R.id.login_linear_deletepass);
		login_linear_deletepass.setOnClickListener(this);
		login_linear_deletename.setOnClickListener(this);
		login_edit_name.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				msg.setVisibility(View.INVISIBLE);
				if (s.length() > 0) {
					login_linear_deletename.setVisibility(View.VISIBLE);
				} else {
					login_linear_deletename.setVisibility(View.GONE);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		login_edit_pass.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				msg.setVisibility(View.INVISIBLE);
				if (s.length() > 0)
					login_linear_deletepass.setVisibility(View.VISIBLE);
				else
					login_linear_deletepass.setVisibility(View.GONE);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		 
		login_linear_login = (LinearLayout) findViewById(R.id.login_linear_login);
		login_linear_login.setOnClickListener(this);
		isFirst = mySharedPreferences.getBoolean("isRemeber", false);
		if (isFirst) {
			login_edit_pass.setText(mySharedPreferences.getString("password",
					""));
			login_edit_name.setText(mySharedPreferences.getString("username",
					""));
		}else{
			login_edit_name.setText(mySharedPreferences.getString("username",
					""));
		}
		close = (Button)findViewById(R.id.close);
		close.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.login_linear_login: 
			if(check()){
				login();
			}
			break;
		case R.id.zhuche_ll: 
		// 注册
		startActivity(new Intent(LoginActivity.this,Register.class));
		break;
		case R.id.login_linear_deletename:
			login_edit_name.setText("");
			break;
		case R.id.login_linear_deletepass:
			login_edit_pass.setText("");
			break;
		case R.id.close:
			this.finish();
			break;
		default:
			break;
		}
	}

	private void login() {
 		System.out.println("usename`` `" + usename);
 		System.out.println("passsword`` `" + passsword);
		 API.Login1(LoginActivity.this,usename,passsword,
	        		
	                new HttpCallback<UserEntity> (LoginActivity.this) {

						@Override
						public void onSuccess(UserEntity data) {
							islogin=true;
							// TODO Auto-generated method stub
							System.out.println("id```"+data.getId());
							MyApplication.NewUser = data;
		 					editor.putBoolean("islogin", true);
			 				editor.putString("name", data.getUsername());
			 				editor.putInt("id", data.getId());
			 				editor.commit();
			 				System.out.println(mySharedPreferences.getBoolean("islogin", false)+"---");
			 				MyApplication.NewUser=data;
			 					
								finish();	
							
						}

						@Override
						public TypeToken getTypeToken() {
							// TODO Auto-generated method stub
							return  new TypeToken<UserEntity>() {
							};
						}
	                });

	}
	private boolean check() {
		// TODO Auto-generated method stub
		usename=StringUtil.replaceBlank(login_edit_name.getText().toString());
		if(usename.length()==0){
			Toast.makeText(getApplicationContext(), "用户名不能为空",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		passsword=StringUtil.replaceBlank(login_edit_pass.getText().toString());
		if(passsword.length()==0){
			Toast.makeText(getApplicationContext(), "请输入用户密码",
					Toast.LENGTH_SHORT).show();
			return false;
		}
	 	passsword=StringUtil.Md5(passsword);
		System.out.println("---login-"+passsword);
		return true;
	}

}
