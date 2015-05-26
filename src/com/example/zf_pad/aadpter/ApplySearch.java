package com.example.zf_pad.aadpter;

import java.util.List;

import com.epalmpay.userPad.R;
import com.example.zf_pad.entity.ApplySerch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ApplySearch extends BaseAdapter{
	private List<ApplySerch> dataser;
	private Context context;
	private LayoutInflater mInflater;
	public ApplySearch(List<ApplySerch> dataser,Context context){
		super();
		this.dataser=dataser;
		this.context=context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataser.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dataser.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return dataser.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHoldel holdel;
		if(convertView == null){
			mInflater=LayoutInflater.from(context);
			convertView=mInflater.inflate(R.layout.serchitem, null);
			holdel=new ViewHoldel();
			holdel.tv_ternumber= (TextView) convertView.findViewById(R.id.tv_ternumber);
			holdel.tv_consumption= (TextView) convertView.findViewById(R.id.tv_consumption);
			holdel.tv_conmoney= (TextView) convertView.findViewById(R.id.tv_conmoney);
			holdel.tv_traaccounts= (TextView) convertView.findViewById(R.id.tv_traaccounts);
			holdel.tv_tramoney= (TextView) convertView.findViewById(R.id.tv_tramoney);
			holdel.tv_repayment= (TextView) convertView.findViewById(R.id.tv_repayment);
			holdel.tv_repmoney= (TextView) convertView.findViewById(R.id.tv_repmoney);
			convertView.setTag(holdel);
			}
		else{
			holdel=(ViewHoldel) convertView.getTag();
		}
		holdel.tv_ternumber.setText(dataser.get(position).getTernumber());
		holdel.tv_consumption.setText(dataser.get(position).getConsumption());
		holdel.tv_conmoney.setText(dataser.get(position).getConmoney());
		holdel.tv_traaccounts.setText(dataser.get(position).getTraaccounts());
		holdel.tv_tramoney.setText(dataser.get(position).getTramoney());
		holdel.tv_repayment.setText(dataser.get(position).getRepayment());
		holdel.tv_repmoney.setText(dataser.get(position).getRepmoney());
		return convertView;
	}
	public static class ViewHoldel{
		TextView tv_ternumber;
		TextView tv_consumption;
		TextView tv_conmoney;
		TextView tv_traaccounts;
		TextView tv_tramoney;
		TextView tv_repayment;
		TextView tv_repmoney;
	}

}
