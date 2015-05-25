package com.example.zf_pad.util;

import com.epalmpay.userPad.R;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class AlertDialog {
	Context context;
	android.app.AlertDialog ad;
	TextView titleView;
	TextView messageView;
	LinearLayout buttonLayout1;
	private LinearLayout buttonLayout2;
	public AlertDialog(Context context) {
		// TODO Auto-generated constructor stub
		this.context=context;
		ad=new android.app.AlertDialog.Builder(context).create();
		ad.show();
		//�ؼ������������,ʹ��window.setContentView,�滻�����Ի��򴰿ڵĲ���
		Window window = ad.getWindow();
		window.setContentView(R.layout.alertdialog);
		titleView=(TextView)window.findViewById(R.id.title);
		messageView=(TextView)window.findViewById(R.id.message);
		buttonLayout1=(LinearLayout)window.findViewById(R.id.buttonLayout1);
		buttonLayout2 = (LinearLayout)window.findViewById(R.id.buttonLayout2);
	}
	public void setTitle(int resId)
	{
		titleView.setText(resId);
	}
	public void setTitle(String title) {
		titleView.setText(title);
	}
	public void setMessage(int resId) {
		messageView.setText(resId);
	}
 
	public void setMessage(String message)
	{
		messageView.setText(message);
	}
	/**
	 * ���ð�ť
	 * @param text
	 * @param listener
	 */
	public void setPositiveButton(String text,final View.OnClickListener listener)
	{
		Button button=new Button(context);
		LinearLayout.LayoutParams params=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		button.setLayoutParams(params);
		button.setBackgroundResource(R.drawable.dialog);
		button.setText(text);
		button.setTextColor(Color.BLUE);
		button.setTextSize(14);
		button.setOnClickListener(listener);
		buttonLayout1.addView(button);
	}
	
	/**
	 * ���ð�ť
	 * @param text
	 * @param listener
	 */
	public void setNegativeButton(String text,final View.OnClickListener listener)
	{
		Button button=new Button(context);
		LinearLayout.LayoutParams params=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		button.setLayoutParams(params);
		button.setBackgroundResource(R.drawable.dialog);
		button.setText(text);
		button.setTextColor(Color.BLUE);
		button.setTextSize(16);
		button.setOnClickListener(listener);
		if(buttonLayout2.getChildCount()>0)
		{
			params.setMargins(20, 0, 0, 0);
			button.setLayoutParams(params);
			buttonLayout2.addView(button, 1);
		}else{
			button.setLayoutParams(params);
			buttonLayout2.addView(button);
		}
		
	}
	/**
	 * �رնԻ���
	 */
	public void dismiss() {
		ad.dismiss();
	}
	
}