package com.example.zf_pad.activity;
import com.example.zf_pad.R;
import com.example.zf_pad.trade.API;
import com.example.zf_pad.trade.common.HttpCallback;
import com.example.zf_pad.util.RegText;
import com.example.zf_pad.util.StringUtil;
import com.example.zf_pad.util.TitleMenuUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ContactUs extends Activity {
	private EditText login_edit_name1, login_edit_name, et_contetn;
	private Button btn_exit;
	private String name, phone, content;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m_lxwm);
		new TitleMenuUtil(ContactUs.this, "联系我们").show();
		login_edit_name = (EditText) findViewById(R.id.login_edit_name);
		login_edit_name1 = (EditText) findViewById(R.id.login_edit_name1);
		et_contetn = (EditText) findViewById(R.id.et_contetn);
		btn_exit = (Button) findViewById(R.id.btn_exit);
		btn_exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				name = StringUtil.replaceBlank(login_edit_name.getText()
						.toString());
				phone = StringUtil.replaceBlank(login_edit_name1.getText()
						.toString());
				content = StringUtil.replaceBlank(et_contetn.getText()
						.toString());
				if(check()){
					ggg();
				}
			}

		});
	}

	private boolean check () {
		// TODO Auto-generated method stub
		// content
		if (name.length() == 0) {
			Toast.makeText(getApplicationContext(), "请输入您的称呼",
					Toast.LENGTH_SHORT).show();
			return false;
		}

		if (phone.length() == 0) {
			Toast.makeText(getApplicationContext(), "请输入联系方式",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if(!RegText.isMobileNO(phone)){
			Toast.makeText(getApplicationContext(), "请输入正确的联系方式",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (content.length() == 0) {
			Toast.makeText(getApplicationContext(), "请输入您的意向",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	public void ggg(){


        API.ApiWantBug(ContactUs.this, name,phone , content,
        		
                new HttpCallback(ContactUs.this) {
           

					@Override
					public void onSuccess(Object data) {
						// TODO Auto-generated method stub
						Toast.makeText(ContactUs.this, "提交成功", 1000).show();
						//finish();
					}

					@Override
					public TypeToken getTypeToken() {
						// TODO Auto-generated method stub
						return null;
					}
                });
	}
}
