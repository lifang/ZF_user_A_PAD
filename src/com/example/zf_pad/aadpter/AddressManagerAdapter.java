package com.example.zf_pad.aadpter;

import java.util.List;

import com.epalmpay.userPad.R;
import com.example.zf_pad.activity.AdressEdit;
import com.example.zf_pad.entity.AddressManager;
import com.example.zf_pad.fragment.Mine_Address;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AddressManagerAdapter extends BaseAdapter{
	private List<AddressManager> dataadress;
	private Context context;
	private LayoutInflater mInflater;
	public static int pp=0;
	public static Handler myHandler;
	public AddressManagerAdapter(List<AddressManager> dataadress,Context context){
		super();
		this.dataadress=dataadress;
		this.context=context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataadress.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dataadress.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return dataadress.get(position).getId();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHoldel holdel;
		if(convertView == null){
			mInflater=LayoutInflater.from(context);
			convertView=mInflater.inflate(R.layout.manageradressitem, null);
			holdel=new ViewHoldel();
			holdel.consignee=(TextView) convertView.findViewById(R.id.consignee);
			holdel.area=(TextView) convertView.findViewById(R.id.area);
			holdel.detailadress=(TextView) convertView.findViewById(R.id.detailadress);
			holdel.zipcode=(TextView) convertView.findViewById(R.id.zipcode);
			holdel.phone=(TextView) convertView.findViewById(R.id.phone); 
			holdel.defau=(TextView) convertView.findViewById(R.id.defau);
			holdel.change=(TextView) convertView.findViewById(R.id.change);
			convertView.setTag(holdel);
			}
		else{
			holdel=(ViewHoldel) convertView.getTag();
		}
		holdel.consignee.setText(dataadress.get(position).getConsignee());
		holdel.area.setText(dataadress.get(position).getArea());
		holdel.detailadress.setText(dataadress.get(position).getDetailadress());
		holdel.zipcode.setText(dataadress.get(position).getZipcode());
		holdel.phone.setText(dataadress.get(position).getPhone());
		holdel.defau.setText(dataadress.get(position).getIsdefau());
		Log.e("holdel.defau", String.valueOf(Mine_Address.type));
		/*myHandler=new Handler(){
			public void handleMessage(Message msg) {
				if(msg.what==1){
					holdel.defau.setVisibility(View.GONE);
				}
			};
		};*/
		holdel.change.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pp=position;
				Message msg=Mine_Address.myHandler.obtainMessage();
				msg.what=1;
				msg.sendToTarget();
				
			}
		});
		return convertView;
	}
	public static class ViewHoldel{
		TextView consignee;
		TextView area;
		TextView detailadress;
		TextView zipcode;
		TextView phone;
		TextView defau;
		TextView change;
	}
}
