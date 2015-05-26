package com.example.zf_pad.aadpter;

import java.util.List;

import com.epalmpay.userPad.R;
import com.example.zf_pad.entity.ChanelEntitiy;
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

public class JiaoyiHuilvAdapter extends BaseAdapter {
	private Context context;
	private List<tDates> list;
	private LayoutInflater inflater;
	private ViewHolder holder = null;

	public JiaoyiHuilvAdapter(Context context, List<tDates> list) {
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
			convertView = inflater.inflate(R.layout.jyhl_item, null);
			holder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
		 
			holder.tv_price = (TextView) convertView
					.findViewById(R.id.tv_price);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		 
	 
		holder.tv_name.setText(list.get(position).getName());
		holder.tv_price.setText((double)list.get(position).getService_rate()/10+"��");
		 if(list.get(position).getService_rate()==10000){
				holder.tv_price.setText("�ʽ����ѣ�/�죩");
		 }
	  
		return convertView;
	}

	public final class ViewHolder {
		public TextView tv_name, tv_price,tv_dec;
		 

	}
}
