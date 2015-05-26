package com.example.zf_pad.aadpter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.epalmpay.userPad.R;
import com.example.zf_pad.aadpter.ApplySearch.ViewHoldel;
import com.example.zf_pad.activity.CreatMerchant;
import com.example.zf_pad.entity.Shopname;
import com.example.zf_pad.fragment.Mine_MyMerChant;

public class ShopAdapter extends BaseAdapter{
	private List<Shopname> datasho;
	private Context context;
	private LayoutInflater mInflater;
	public static int pp=0;
	public ShopAdapter(List<Shopname> datasho,Context context){
		super();
		this.datasho=datasho;
		this.context=context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datasho.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return datasho.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return datasho.get(position).getId();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHoldel holdel;
		if(convertView == null){
			mInflater=LayoutInflater.from(context);
			convertView=mInflater.inflate(R.layout.merchant_item, null);
			holdel=new ViewHoldel();
			holdel.tv_shopname=(TextView) convertView.findViewById(R.id.tv_shopname);
			holdel.btn_delect=(Button) convertView.findViewById(R.id.btn_delect);
			convertView.setTag(holdel);
		}
		else{
			holdel=(ViewHoldel) convertView.getTag();
		}
		holdel.tv_shopname.setText(datasho.get(position).getShopname());
		holdel.btn_delect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pp=position;
				Message msg=Mine_MyMerChant.myHandler.obtainMessage();
				msg.what=1;
				msg.sendToTarget();
			}
		});
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				pp=position;
				Message msg=Mine_MyMerChant.myHandler.obtainMessage();
				msg.what=2;
				msg.sendToTarget();
				
			}
		});
		return convertView;
	}
	public static class ViewHoldel{
		TextView tv_shopname;
		Button btn_delect;
	}
}
