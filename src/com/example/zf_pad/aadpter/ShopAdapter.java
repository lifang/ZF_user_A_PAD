package com.example.zf_pad.aadpter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.zf_pad.R;
import com.example.zf_pad.aadpter.ApplySearch.ViewHoldel;
import com.example.zf_pad.entity.Shopname;

public class ShopAdapter extends BaseAdapter{
	private List<Shopname> datasho;
	private Context context;
	private LayoutInflater mInflater;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHoldel holdel;
		if(convertView == null){
			mInflater=LayoutInflater.from(context);
			convertView=mInflater.inflate(R.layout.merchant_item, null);
			holdel=new ViewHoldel();
			holdel.tv_shopname=(TextView) convertView.findViewById(R.id.tv_shopname);
		}
		else{
			holdel=(ViewHoldel) convertView.getTag();
		}
		return convertView;
	}
	public static class ViewHoldel{
		TextView tv_shopname;
		Button btn_delect;
	}
}
