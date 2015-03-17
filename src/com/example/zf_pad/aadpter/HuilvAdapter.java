package com.example.zf_pad.aadpter;

import java.util.List;

import com.example.zf_pad.R;
import com.example.zf_pad.entity.ChanelEntitiy;
import com.example.zf_pad.entity.other_rate;
import com.example.zf_pad.entity.tDates;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HuilvAdapter extends BaseAdapter {
	private Context context;
	private List<ChanelEntitiy> list;
	private List<tDates> list1;
	private List<other_rate> list2;
	private LayoutInflater inflater;
	private ViewHolder holder = null;
	private int type=3;

	public HuilvAdapter(Context context, List<ChanelEntitiy> list) {
		this.context = context;
		this.list = list;
	}

	public HuilvAdapter(Context context, List<tDates> list, int type) {
		this.context = context;
		this.list1=list;
		this.type = type;
	}

	public HuilvAdapter(Context context, List<other_rate> list, int type,
			String s) {
		this.context = context;
		this.list2 = list;
		this.type = type;
	}

	@Override
	public int getCount() {
		if(type==0){
			return list1.size();
		}else if(type==1){
			return list2.size();
		}else{
			return list.size();
		}
		
	}

	@Override
	public Object getItem(int position) {
		if(type==0){
			return list1.get(position);
		}else if(type==1){
			return list2.get(position);
		}else{
			return list.get(position);
		}
		
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
			convertView = inflater.inflate(R.layout.gooddetail_item, null);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_dec = (TextView) convertView.findViewById(R.id.tv_dec);
			holder.tv_price = (TextView) convertView
					.findViewById(R.id.tv_price);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (type == 0) {
			holder.tv_name.setText(list1.get(position).getName());

			holder.tv_price
					.setText(list1.get(position).getService_rate() + "%");
			holder.tv_dec.setText(list1.get(position).getDescription());
		} else if (type == 1) {
			holder.tv_name.setText(list2.get(position).getTrade_value());

			holder.tv_price
					.setText(list2.get(position).getTerminal_rate() + "%");
			holder.tv_dec.setText(list2.get(position).getDescription());
		} else {
			holder.tv_name.setText(list.get(position).getName());
			System.out.println("list.get(position).getName()---"
					+ list.get(position).getName());
			holder.tv_price.setText(list.get(position).getService_rate() + "%");
			holder.tv_dec.setText(list.get(position).getDescription());
		}

		return convertView;
	}

	public final class ViewHolder {
		public TextView tv_name, tv_price, tv_dec;

	}
}
