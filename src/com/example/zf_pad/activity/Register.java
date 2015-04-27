package com.example.zf_pad.activity;
import com.example.zf_pad.MyApplication;
import com.example.zf_pad.R;
import com.example.zf_pad.trade.API;
import com.example.zf_pad.trade.CityProvinceActivity;
import com.example.zf_pad.trade.common.HttpCallback;
import com.example.zf_pad.trade.entity.City;
import com.example.zf_pad.trade.entity.Province;
import com.example.zf_pad.util.StringUtil;
import com.example.zf_pad.util.TitleMenuUtil;
import com.google.gson.reflect.TypeToken;
import static com.example.zf_pad.fragment.Constants.ApplyIntent.REQUEST_CHOOSE_CITY;
import static com.example.zf_pad.fragment.Constants.CityIntent.SELECTED_CITY;
import static com.example.zf_pad.fragment.Constants.CityIntent.SELECTED_PROVINCE;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends Activity implements OnClickListener {
	private TextView tv_code, tv_check, tv_jy_type;
	private EditText login_edit_email, login_edit_code, login_edit_pass,
			login_edit_pass2;
	private LinearLayout login_linear_deletemali, login_linear_deletcode,
			login_linear_deletpass, login_linear_deletpass2,
			login_linear_signin, ll_jy_type;
	private int Countmun = 120;
	private Thread myThread;
	private Boolean isRun = true;
	private ImageView img_check, img_check_n;
	public String vcode = "";
	private String url, email, pass;
	private Boolean chenckcode = false;
	private Runnable runnable;
	private boolean ismail = false;
	final Handler handler = new Handler() { // handle
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (Countmun == 0) {
					Toast.makeText(getApplicationContext(), Countmun, 1000)
							.show();
					isRun = false;
					tv_code.setClickable(true);

					tv_code.setText("发送验证码");
					System.out.println("destroy`" + Countmun);
				} else {
					Toast.makeText(getApplicationContext(), "cc" + Countmun,
							1000).show();
					Countmun--;
					tv_code.setText(Countmun + "秒后重新发送");
					System.out.println("Countmun`D2`" + Countmun);
				}

			}
			super.handleMessage(msg);
		}
	};
	private LinearLayout ll_isshow;
	private int cityid=MyApplication.getCITYID();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		new TitleMenuUtil(Register.this, "注册").show();
		System.out.println("Register4phone---");
		initView();
		tv_jy_type.setText("请选择地区");
		img_check.setVisibility(View.VISIBLE);
		img_check_n.setVisibility(View.GONE);
		chenckcode = true;
		runnable = new Runnable() {
			@Override
			public void run() {
				if (Countmun == 0) {
					Countmun = 120;
					tv_code.setClickable(true);
					tv_code.setText("发送验证码");
				} else {
					Countmun--;
					tv_code.setText(Countmun + "秒后重新发送");
					handler.postDelayed(this, 1000);
				}

			}
		};

	}

	private void initView() {
		ll_isshow = (LinearLayout) findViewById(R.id.isshow);
		tv_check = (TextView) findViewById(R.id.tv_check);
		tv_check.setOnClickListener(this);
		img_check = (ImageView) findViewById(R.id.img_check);
		img_check_n = (ImageView) findViewById(R.id.img_check_n);
		tv_code = (TextView) findViewById(R.id.tv_code);
		tv_code.setOnClickListener(this);
		tv_jy_type = (TextView) findViewById(R.id.tv_jy_type);

		ll_jy_type = (LinearLayout) findViewById(R.id.ll_jy_type);
		ll_jy_type.setOnClickListener(this);
		login_linear_signin = (LinearLayout) findViewById(R.id.login_linear_signin);
		login_linear_signin.setOnClickListener(this);
		login_linear_deletemali = (LinearLayout) findViewById(R.id.login_linear_deletemali);
		login_linear_deletemali.setOnClickListener(this);
		login_edit_email = (EditText) findViewById(R.id.login_edit_email);
		String s = getIntent().getStringExtra("phone");
		login_edit_email.setText(s);
		vcode = getIntent().getStringExtra("vcode");
	
		login_edit_email.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				if (s.length() > 0) {

					login_linear_deletemali.setVisibility(View.VISIBLE);
				} else {
					login_linear_deletemali.setVisibility(View.GONE);
				}
				if (s.toString().contains(".")) {
					ll_isshow.setVisibility(View.GONE);
					tv_code.setVisibility(View.INVISIBLE);
					ismail = true;
				} else {
					ll_isshow.setVisibility(View.VISIBLE);
					tv_code.setVisibility(View.VISIBLE);
					ismail = false;
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		// login_linear_deletpass

		login_edit_code = (EditText) findViewById(R.id.login_edit_code);

		// login_linear_deletpass
		login_linear_deletpass = (LinearLayout) findViewById(R.id.login_linear_deletpass);
		login_linear_deletpass.setOnClickListener(this);
		login_edit_pass = (EditText) findViewById(R.id.login_edit_pass);
		login_edit_pass.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (s.length() > 0) {

					login_linear_deletpass.setVisibility(View.VISIBLE);
				} else {
					login_linear_deletpass.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		login_linear_deletpass2 = (LinearLayout) findViewById(R.id.login_linear_deletpass2);
		login_linear_deletpass2.setOnClickListener(this);
		login_edit_pass2 = (EditText) findViewById(R.id.login_edit_pass2);
		login_edit_pass2.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (s.length() > 0) {

					login_linear_deletpass2.setVisibility(View.VISIBLE);
				} else {
					login_linear_deletpass2.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

	}

	private boolean check() {
		// TODO Auto-generated method stub
		email = StringUtil.replaceBlank(login_edit_email.getText().toString());
		if (email.length() == 0) {
			Toast.makeText(getApplicationContext(), "手机号不能为空",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (!ismail) {
			if (StringUtil.replaceBlank(login_edit_code.getText().toString())
					.length() == 0) {
				Toast.makeText(getApplicationContext(), "请输入验证码",
						Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		if (!ismail) {
			if (!login_edit_code.getText().toString().endsWith(vcode)) {
				Toast.makeText(getApplicationContext(), "验证码错误",
						Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		pass = StringUtil.replaceBlank(login_edit_pass.getText().toString());
		if (pass.length() == 0) {
			Toast.makeText(getApplicationContext(), "密码不能为空",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (!login_edit_pass2.getText().toString().equals(pass)) {
			Toast.makeText(getApplicationContext(), "二次密码不一样",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (tv_jy_type.getText().toString().equals("请选择地区")) {
			Toast.makeText(getApplicationContext(), "请选择地区", Toast.LENGTH_SHORT)
					.show();
			return false;
		}

		pass = StringUtil.Md5(pass);
		System.out.println("pass" + pass);
		return true;
	}

	private void getCode() {
		// TODO Auto-generated method stub

		handler.postDelayed(runnable, 1000);
		email = StringUtil.replaceBlank(login_edit_email.getText().toString());
		if (email.equals("") && email == null) {
			Toast.makeText(getApplicationContext(), "请输入邮箱或者手机号", 1000).show();
		} else {
			ggg(email);
		}
	}

	public class MyThread implements Runnable { // thread
		@Override
		public void run() {

			while (isRun) {
				System.out.println("run``" + Countmun);

				try {
					Thread.sleep(1000); // sleep 1000ms
					Message message = new Message();
					message.what = 1;
					handler.sendMessage(message);
				} catch (Exception e) {

				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.ll_jy_type:
			Intent intent = new Intent(Register.this,
					CityProvinceActivity.class);
			// intent.putExtra(SELECTED_PROVINCE, "江苏省");
			// intent.putExtra(SELECTED_CITY, "苏州市");
			startActivityForResult(intent, REQUEST_CHOOSE_CITY);
			break;
		case R.id.tv_code: // ��ȡ��֤��tv_check
			// tv_check.setVisibility(View.INVISIBLE);
			// email=StringUtil.replaceBlank(login_edit_email.getText().toString());
			// if(email.length()==0){
			// Toast.makeText(getApplicationContext(),
			// "Email cannot be empty��",
			// Toast.LENGTH_SHORT).show();
			// break;
			// }

			tv_code.setClickable(false);
			// tv_code.setText("120秒");
			getCode();
			break;
		case R.id.tv_check:
			System.out.println("vcode" + vcode);

			if (login_edit_code.getText().toString().equals(vcode)) {
				img_check.setVisibility(View.VISIBLE);
				img_check_n.setVisibility(View.GONE);
				chenckcode = true;
			} else {
				img_check.setVisibility(View.GONE);
				img_check_n.setVisibility(View.VISIBLE);
				chenckcode = false;
			}

			break;
		case R.id.login_linear_signin: // ��ȡ��֤��

			if (check()) {
				zhuche();

			}

			break;
		case R.id.login_linear_deletemali:
			login_edit_email.setText("");
			break;

		case R.id.login_linear_deletpass:
			login_edit_pass.setText("");
			break;
		case R.id.login_linear_deletpass2:
			login_edit_pass2.setText("");
			break;
		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK)
			return;
		switch (requestCode) {
		case REQUEST_CHOOSE_CITY:
			Province mMerchantProvince = (Province) data
					.getSerializableExtra(SELECTED_PROVINCE);
			City mMerchantCity = (City) data
					.getSerializableExtra(SELECTED_CITY);
			System.out.println(mMerchantCity.getId() + "mMerchantCity"
					+ mMerchantCity.toString());
			tv_jy_type.setText(mMerchantCity.getName());
			cityid = mMerchantCity.getId();
			break;

		default:
			break;
		}
	}

	public void zhuche() {
		API.zhuche(Register.this, email, pass, vcode, cityid, ismail,

		new HttpCallback(Register.this) {
			@Override
			public void onSuccess(Object data) {
				// TODO Auto-generated method stub
				Toast.makeText(Register.this, "注册成功", 1000).show();
				// Regist4phoneSucces//
				
				Intent i =new Intent(getApplicationContext(), RegSucceed.class);
				i.putExtra("value", email);
				if(ismail){
					i.putExtra("type", "0");
				}else{
					i.putExtra("type", "1");
				}
				startActivity(i);
				Register.this.finish();
			}

			@Override
			public TypeToken getTypeToken() {
				// TODO Auto-generated method stub
				return null;
			}
		});
	}

	public void ggg(String phonenumber) {
		API.AddAdres1(Register.this, phonenumber,

		new HttpCallback(Register.this) {

			@Override
			public void onSuccess(Object data) {
				// TODO Auto-generated method stub
				Toast.makeText(Register.this, "验证码发送成功", 1000).show();
				vcode = data.toString();
			}

			@Override
			public TypeToken getTypeToken() {
				// TODO Auto-generated method stub
				return null;
			}
		});
	}
}
