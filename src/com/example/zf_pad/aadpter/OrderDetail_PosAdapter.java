package com.example.zf_pad.aadpter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import u.aly.df;

import com.epalmpay.userPad.R;
import com.example.zf_pad.entity.Goodlist;
import com.example.zf_pad.util.ImageCacheUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class OrderDetail_PosAdapter extends BaseAdapter {
	private Context context;
	private List<Goodlist> list;
	private LayoutInflater inflater;
	private int state;
	private DecimalFormat df;
	private ViewHolder holder = null;

	public OrderDetail_PosAdapter(Context context, List<Goodlist> list,
			int state) {
		df = (DecimalFormat)NumberFormat.getInstance();
		df.applyPattern("0.00");
		this.context = context;
		this.list = list;
		this.state = state;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		inflater = LayoutInflater.from(context);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.order_detail_positem, null);
			holder.content = (TextView) convertView
					.findViewById(R.id.content_pp);
			holder.btn_ishow = (Button) convertView
					.findViewById(R.id.btn_ishow);
			holder.tv_price = (TextView) convertView
					.findViewById(R.id.tv_price);
			holder.tv_x = (TextView) convertView.findViewById(R.id.tv_x);
			holder.tv_brand = (TextView) convertView
					.findViewById(R.id.content2);
			holder.tv_td = (TextView) convertView.findViewById(R.id.tv_td);
			holder.im = (ImageView) convertView.findViewById(R.id.evevt_img);
			holder.ktfy = (TextView) convertView.findViewById(R.id.ktfy);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}	
		holder.ktfy.setText("(º¬¿ªÍ¨·Ñ£¤"+df.format(list.get(position).getGood_opening_cost()*1.0f/100)+")");
		holder.tv_brand.setText(list.get(position).getGood_brand());
		holder.tv_td.setText(list.get(position).getGood_channel());
		holder.content.setText(list.get(position).getGood_name());
		holder.tv_price.setText("£¤ "
				+ ((double) Integer.valueOf(list.get(position)
						.getGood_actualprice())) / 100);
		holder.tv_x.setText("X " + list.get(position).getGood_num() + "");
		holder.btn_ishow.setVisibility(state == 3 ? View.GONE : View.GONE);
		ImageCacheUtil.IMAGE_CACHE.get(list.get(position).getGood_logo(),
				holder.im);

		return convertView;
	}

	public final class ViewHolder {
		public TextView content, tv_price, tv_x, tv_brand, tv_td, ktfy;
		public Button btn_ishow;
		public ImageView im;
	}

}
