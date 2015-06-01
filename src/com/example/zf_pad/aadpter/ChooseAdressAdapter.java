package com.example.zf_pad.aadpter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.epalmpay.userPad.R;
import com.example.zf_pad.entity.AdressEntity;

public class ChooseAdressAdapter extends BaseAdapter {
	private Context context;
	private List<AdressEntity> list;
	private LayoutInflater inflater;
	private ViewHolder holder = null;

	public ChooseAdressAdapter(Context context, List<AdressEntity> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		inflater = LayoutInflater.from(context);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.choose_adress_item, null);
			holder.tv_title = (TextView) convertView
					.findViewById(R.id.adress_name);
			 holder.tv_adress = (TextView) convertView.findViewById(R.id.tv_adress);
			 holder.tv_tel = (TextView) convertView.findViewById(R.id.tv_tel);
			 holder.tv_yb= (TextView) convertView.findViewById(R.id.tv_yb);
			 holder.tv_city = (TextView) convertView.findViewById(R.id.tv_city);
			 holder.tv_default = (TextView) convertView.findViewById(R.id.tv_default);
			holder.ll_isshow = (LinearLayout) convertView.findViewById(R.id.ll_isshow);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_tel.setText(list.get(position).getMoblephone());
		holder.tv_title.setText(list.get(position).getReceiver());
		holder.tv_yb.setText(list.get(position).getZipCode());
		holder.tv_city.setText(list.get(position).getCity());
		if(list.get(position).getIsDefault().equals("1")){
			holder.ll_isshow .setVisibility(View.VISIBLE);	
			holder.tv_default.setVisibility(View.GONE);
		}else{
			holder.ll_isshow .setVisibility(View.INVISIBLE);
			if(list.get(position).getIscheck()){
				holder.ll_isshow .setVisibility(View.VISIBLE);			 
			}else{
				holder.ll_isshow .setVisibility(View.INVISIBLE);
				holder.tv_default.setVisibility(View.GONE);
			}
		}
	
		holder.tv_adress.setText(list.get(position).getAddress()); 
		return convertView;
	}
	public void chang(){
		for(AdressEntity ae:list){
			ae.setIsDefault("0");
			ae.setIscheck(false);
		}		
	}
	public final class ViewHolder {
		public TextView tv_title, tv_time,tv_tel,tv_adress,tv_yb,tv_city,tv_default;
		public LinearLayout ll_isshow;

	}
}
