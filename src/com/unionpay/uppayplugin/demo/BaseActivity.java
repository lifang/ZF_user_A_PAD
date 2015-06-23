package com.unionpay.uppayplugin.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.example.zf_pad.Config;
import com.example.zf_pad.MyApplication;
import com.example.zf_pad.activity.OrderDetail;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public abstract class BaseActivity extends Activity implements Callback,
		Runnable {
	public static final String LOG_TAG = "PayDemo";
	private Context mContext = null;
	private int mGoodsIdx = 0;
	private Handler mHandler = null;
	private ProgressDialog mLoadingDialog = null;

	public static final int PLUGIN_VALID = 0;
	public static final int PLUGIN_NOT_INSTALLED = -1;
	public static final int PLUGIN_NEED_UPGRADE = 2;

	protected String outTradeNo;
	protected String price;
	protected int orderId;
	String str = null;
	String pay_result = null;
	String type = null;
	
	public abstract void doStartUnionPayPlugin(Activity activity, String tn,
			String mode);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		mHandler = new Handler(this);
		outTradeNo = this.getIntent().getStringExtra("outTradeNo");
		price = this.getIntent().getStringExtra("price");
		orderId = this.getIntent().getIntExtra("orderId",-1);
		type = this.getIntent().getStringExtra("type");
		Log.e("==orderId==", ""+orderId);
		mLoadingDialog = ProgressDialog.show(mContext, // context
				"", // title
				"���Ժ�...", // message
				true); // �����Ƿ��ǲ�ȷ���ģ���ֻ�ʹ����������й�

		/*************************************************
		 * ����1�������翪ʼ,��ȡ������ˮ�ż�TN
		 ************************************************/
		new Thread(BaseActivity.this).start();

	}


	@Override
	public boolean handleMessage(Message msg) {
		
		if (mLoadingDialog.isShowing()) {
			mLoadingDialog.dismiss();
		}
		if (msg.what == 1) {
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("֧�����֪ͨ");
			builder.setMessage(pay_result);
			builder.setInverseBackgroundForced(true);
			// builder.setCustomTitle();
			builder.setNegativeButton("ȷ��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					// finish();
					if (str.equalsIgnoreCase("success")) {
						
						MyApplication.getInstance().setHasOrderPaid(true);
						Intent intent = new Intent(BaseActivity.this,
								OrderDetail.class);
						intent.putExtra("status", 2);
						intent.putExtra("id", orderId);
						intent.putExtra("type", type);
						startActivity(intent);
						finish();

					} else if (str.equalsIgnoreCase("fail")) {

//						Intent intent = new Intent(BaseActivity.this,
//								OrderDetail.class);
//						intent.putExtra("status", 1);
//						intent.putExtra("id", orderId);
//						startActivity(intent);
						finish();

					} else if (str.equalsIgnoreCase("cancel")) {

//						Intent intent = new Intent(BaseActivity.this,
//								OrderDetail.class);
//						intent.putExtra("status", 1);
//						intent.putExtra("id", orderId);
//						startActivity(intent);
						finish();

					}
				}
			});
			builder.create().show();
			
		} else {
			Log.e(LOG_TAG, "==tn==" + msg.obj);
			String tn = "";
			if (msg.obj == null || ((String) msg.obj).length() == 0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("������ʾ");
				builder.setMessage("��������ʧ��,������!");
				builder.setNegativeButton("ȷ��",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				builder.create().show();
			} else {

				tn = (String) msg.obj;
				/*************************************************
				 * ����2��ͨ����������������֧�����
				 ************************************************/
				doStartUnionPayPlugin(this, tn, Config.UNION_MEDE);
			}
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		/*************************************************
		 * ����3�����������ֻ�֧���ؼ����ص�֧�����
		 ************************************************/
		if (data == null) {
			return;
		}

		/*
		 * ֧���ؼ������ַ���:success��fail��cancel �ֱ����֧���ɹ���֧��ʧ�ܣ�֧��ȡ��
		 */
		str = data.getExtras().getString("pay_result");
		if (str.equalsIgnoreCase("success")) {
			
			//֧���ɹ�ѡ��֧��ҳ�����
			MyApplication.getInstance().clearHistoryForPay();
			
			pay_result = "֧���ɹ�!";
			mLoadingDialog = ProgressDialog.show(mContext, // context
					"", // title
					"���Ժ�...", // message
					true);
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Map<String, String> param = new HashMap<String, String>();
					param.put("ordernumber", outTradeNo);
					param.put("payType", "2");
					String result = postRequest(Config.UNION_SUCESS_URL, param);
					Message msg = mHandler.obtainMessage();
					msg.what = 1;
					msg.obj = result;
					mHandler.sendMessage(msg);
					Log.e("֧���ɹ�������==", "==result==" + result);
				}
			}).start();

		} else if (str.equalsIgnoreCase("fail")) {
			pay_result = "֧��ʧ��!";
			Message msg = mHandler.obtainMessage();
			msg.what = 1;
			mHandler.sendMessage(msg);
		} else if (str.equalsIgnoreCase("cancel")) {
			pay_result = "֧��ȡ��!";
			Message msg = mHandler.obtainMessage();
			msg.what = 1;
			mHandler.sendMessage(msg);
		}
		
	}

	@Override
	public void run() {
		Map<String, String> param = new HashMap<String, String>();
		param.put("frontOrBack", "back");
		param.put("orderId", outTradeNo);
		param.put("txnAmt", price);
		param.put("wap", "wap");
		param.put("txnType", "01");
		param.put("payType", "2");
		param.put("android", "android");

		String tn = postRequest(Config.UNION_TN_URL, param);

		Message msg = mHandler.obtainMessage();
		msg.what = 2;
		msg.obj = tn;
		mHandler.sendMessage(msg);
	}

	int startpay(Activity act, String tn, int serverIdentifier) {
		return 0;
	}

	private String postRequest(String url, Map<String, String> params) {
		String result = null;
		HttpPost httpPost = new HttpPost(url);
		// Log.e("http_post�����ַ", TN_URL_01);
		try {
			// ����ʱ
			httpPost.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, 100 * 1000);
			// ��Ӧ��ʱ
			httpPost.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
					1000 * 10);

			List<NameValuePair> sendParams = new ArrayList<NameValuePair>();
			Iterator<String> it = params.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				sendParams.add(new BasicNameValuePair(key, params.get(key)));
			}
			if (sendParams != null && !sendParams.equals("")
					&& sendParams.size() != 0) {
				httpPost.setEntity(new UrlEncodedFormEntity(sendParams,
						HTTP.UTF_8));

				Log.e("request", sendParams.toString() + "================");
			}
			HttpClient httpClient = new DefaultHttpClient();

			/** �������󲢵ȴ���Ӧ */
			HttpResponse httpResponse = httpClient.execute(httpPost);
			/** ״̬��Ϊ200 ��ʾok */
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			Log.e("==statusCode==", "" + statusCode);
			if (statusCode == 200) {
				HttpEntity httpEntity = httpResponse.getEntity();
				result = EntityUtils.toString(httpEntity).trim();
				if (null != httpEntity) {
					httpEntity.consumeContent();
				}
				return result;
			}
			return result;
		} catch (Exception e) {
			return "";
		}
	}
}
