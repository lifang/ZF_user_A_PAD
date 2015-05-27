package com.example.zf_pad.fragment;

import java.security.MessageDigest;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.zf_pad.MyApplication;
import com.epalmpay.userPad.R;
import com.example.zf_pad.activity.LoginActivity;
import com.example.zf_pad.trade.API;
import com.example.zf_pad.trade.common.CommonUtil;
import com.example.zf_pad.trade.common.HttpCallback;
import com.example.zf_pad.util.MyToast;
import com.example.zf_pad.util.StringUtil;
import com.example.zf_pad.util.Tools;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Mine_chgpaw extends Fragment{
	private View view;
	private String password;
	private EditText et_oldpaw,et_newpaw,et_confirmpaw;
	private Button btn_save;
	private int id;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// view = inflater.inflate(R.layout.f_mine, container,false);
		// initView();

		if (view != null) {
			Log.i("222222", "11111111");
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null)
				parent.removeView(view);
		}
		try {
			view = inflater.inflate(R.layout.changepaw, container, false);
			id=MyApplication.NewUser.getId();
		} catch (InflateException e) {

		}

		return view;
	}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		init();
		btn_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (check ()) {
					changepaw();
				}

			}
		});
	}
	protected void changepaw() {
		if(!Tools.isConnect(getActivity())){
			CommonUtil.toastShort(getActivity(), "网络异常");
			return;
		}
		API.changepaw(getActivity(), id, StringUtil.Md5(et_oldpaw.getText().toString()), 
				StringUtil.Md5(et_newpaw.getText().toString()), 
				new HttpCallback(getActivity()) {

			@Override
			public void onSuccess(Object data) {
				Toast.makeText(getActivity(), "修改密码成功", Toast.LENGTH_SHORT).show();

			}

			@Override
			public TypeToken getTypeToken() {
				// TODO Auto-generated method stub
				return null;
			}
		});

	}
	private void init() {
		et_oldpaw=(EditText) view.findViewById(R.id.et_oldpaw);
		et_newpaw=(EditText) view.findViewById(R.id.et_newpaw);
		et_confirmpaw=(EditText) view.findViewById(R.id.et_confirmpaw);
		btn_save=(Button) view.findViewById(R.id.btn_save_chgpaw);

	}
	public static String MD5(String str) {
		MessageDigest md5 =null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch(Exception e) {
			e.printStackTrace();
			return "";
		}

		char[] charArray = str.toCharArray();
		byte[] byteArray =new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}
		byte[] md5Bytes = md5.digest(byteArray);

		StringBuffer hexValue =new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) &0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}
	private boolean check () {
		if (StringUtil.isNull(et_oldpaw.getText().toString().trim())) {
			MyToast.showToast(getActivity(),"请输入您的原始密码");
			return false;
		}

		if (StringUtil.isNull(et_newpaw.getText().toString().trim())) {
			MyToast.showToast(getActivity(),"请输入您的新密码");
			return false;
		}
		if (StringUtil.isNull(et_confirmpaw.getText().toString().trim())) {
			MyToast.showToast(getActivity(),"请确认您的新密码");
			return false;
		}
		if (!et_newpaw.getText().toString().trim().equals(et_confirmpaw.getText().toString().trim())) {
			MyToast.showToast(getActivity(),"两次输入的密码不一致");
			return false;
		}
		if (et_newpaw.getText().toString().length() < 6) {
			Toast.makeText(getActivity(), "密码长度不能少于6位",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
}
