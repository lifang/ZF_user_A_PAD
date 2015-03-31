package com.example.zf_pad.aadpter;

import java.util.List;

import com.example.zf_pad.R;
import com.example.zf_pad.entity.AddressManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AddressManagerAdapter extends BaseAdapter{
	private List<AddressManager> dataadress;
	private Context context;
	private LayoutInflater mInflater;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHoldel holdel;
		if(convertView == null){
			mInflater=LayoutInflater.from(context);
			convertView=mInflater.inflate(R.layout.manageradressitem, null);
			holdel=new ViewHoldel();
			holdel.consignee=(TextView) convertView.findViewById(R.id.consignee);
			holdel.area=(TextView) convertView.findViewById(R.id.area);
			holdel.detailadress=(TextView) convertView.findViewById(R.id.detailadress);
			holdel.zipcode=(TextView) convertView.findViewById(R.id.zipcode);
			holdel.phone=(TextView) convertView.findViewById(R.id.phone);  
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
		return convertView;
	}
	public static class ViewHoldel{
		TextView consignee;
		TextView area;
		TextView detailadress;
		TextView zipcode;
		TextView phone;
	}
}
