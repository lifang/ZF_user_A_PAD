package com.example.zf_pad.aadpter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import com.example.zf_pad.R;
import com.example.zf_pad.activity.GoodDeatail;
import com.example.zf_pad.activity.PosListActivity;
import com.example.zf_pad.entity.PosEntity;

public class PosAdapter1 extends BaseAdapter {
	private Context context;
	private List<PosEntity> list;
	private LayoutInflater inflater;
	private ViewHolder holder = null;
	private int listSize;

	public PosAdapter1(Context context, List<PosEntity> list) {
		this.context = context;
		this.list = list;
		

	}

	@Override
	public int getCount() {
		listSize = (int) Math.ceil(((double) list.size()) / 4);
		return listSize;
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
			convertView = inflater.inflate(R.layout.pos_item1, null);
			holder.ll_m1 = (LinearLayout) convertView.findViewById(R.id.ll_m1);
			holder.ll_m2 = (LinearLayout) convertView.findViewById(R.id.ll_m2);
			holder.ll_m3 = (LinearLayout) convertView.findViewById(R.id.ll_m3);
			holder.ll_m4 = (LinearLayout) convertView.findViewById(R.id.ll_m4);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.tv_price = (TextView) convertView
					.findViewById(R.id.tv_price);
			holder.ys = (TextView) convertView.findViewById(R.id.ys);
			holder.tv_td = (TextView) convertView.findViewById(R.id.tv_td);
			holder.title2 = (TextView) convertView.findViewById(R.id.title2);
			holder.tv_price2 = (TextView) convertView
					.findViewById(R.id.tv_price2);
			holder.ys2 = (TextView) convertView.findViewById(R.id.ys2);
			holder.tv_td2 = (TextView) convertView.findViewById(R.id.tv_td2);
			holder.title3 = (TextView) convertView.findViewById(R.id.title3);
			holder.tv_price3 = (TextView) convertView
					.findViewById(R.id.tv_price3);
			holder.ys3 = (TextView) convertView.findViewById(R.id.ys3);
			holder.tv_td3 = (TextView) convertView.findViewById(R.id.tv_td3);
			holder.title4 = (TextView) convertView.findViewById(R.id.title4);
			holder.tv_price4 = (TextView) convertView
					.findViewById(R.id.tv_price4);
			holder.ys4 = (TextView) convertView.findViewById(R.id.ys4);
			holder.tv_td4 = (TextView) convertView.findViewById(R.id.tv_td4);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.ll_m1.setVisibility(View.VISIBLE);
		holder.ll_m2.setVisibility(View.VISIBLE);
		holder.ll_m3.setVisibility(View.VISIBLE);
		holder.ll_m4.setVisibility(View.VISIBLE);
		holder.ll_m1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Toast.makeText(context,list.get(position*4).getTitle(), 1000).show();
				Intent i =new Intent (context,GoodDeatail.class);
				i.putExtra("id", list.get(position*4).getId());
				context.startActivity(i);
			}
		});
		holder.ll_m2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Toast.makeText(context,position*4+1+"", 1000).show();
				Intent i =new Intent (context,GoodDeatail.class);
				i.putExtra("id", list.get(position*4+1).getId());
				context.startActivity(i);
			}
		});
		holder.ll_m3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Toast.makeText(context,position*4+2+"", 1000).show();
				Intent i =new Intent (context,GoodDeatail.class);
				i.putExtra("id", list.get(position*4+2).getId());
				context.startActivity(i);
			}
		});
		holder.ll_m4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Toast.makeText(context,list.get(position*4+3).getPay_channe()+"", 1000).show();
				Intent i =new Intent (context,GoodDeatail.class);
				i.putExtra("id", list.get(position*4+3).getId());
				context.startActivity(i);
			}
		});
		if (position == listSize - 1) {
			int index = list.size() - position * 4;

			switch (index) {

			case 1:
				holder.title.setText(list.get(position*4).getTitle());
				holder.tv_price.setText("￥"
						+ list.get(position*4).getRetail_price() / 100 + "");
				holder.tv_td.setText(list.get(position*4).getPay_channe());
				holder.ys.setText("已售" + list.get(position*4).getVolume_number());
				holder.ll_m2.setVisibility(View.INVISIBLE);
				holder.ll_m3.setVisibility(View.INVISIBLE);
				holder.ll_m4.setVisibility(View.INVISIBLE);
				break;
			case 2:
				holder.title.setText(list.get(position*4).getTitle());
				holder.tv_price.setText("￥"
						+ list.get(position*4).getRetail_price() / 100 + "");
				holder.tv_td.setText(list.get(position*4).getPay_channe());
				holder.ys.setText("已售" + list.get(position*4).getVolume_number());

				holder.title2.setText(list.get(position*4+1).getTitle());
				holder.tv_price2.setText("￥"
						+ list.get(position*4 + 1).getRetail_price() / 100 + "");
				holder.tv_td2.setText(list.get(position*4 + 1).getPay_channe());
				holder.ys2.setText("已售"
						+ list.get(position*4 + 1).getVolume_number());

				holder.ll_m3.setVisibility(View.INVISIBLE);
				holder.ll_m4.setVisibility(View.INVISIBLE);
				break;
			case 3:
				holder.title.setText(list.get(position*4).getTitle());
				holder.tv_price.setText("￥"
						+ list.get(position*4).getRetail_price() / 100 + "");
				holder.tv_td.setText(list.get(position*4).getPay_channe());
				holder.ys.setText("已售" + list.get(position*4).getVolume_number());

				holder.title2.setText(list.get(position*4+1).getTitle());
				holder.tv_price2.setText("￥"
						+ list.get(position*4 + 1).getRetail_price() / 100 + "");
				holder.tv_td2.setText(list.get(position*4 + 1).getPay_channe());
				holder.ys2.setText("已售"
						+ list.get(position*4 + 1).getVolume_number());

				holder.title3.setText(list.get(position*4 + 2).getTitle());
				holder.tv_price3.setText("￥"
						+ list.get(position*4 + 2).getRetail_price() / 100 + "");
				holder.tv_td3.setText(list.get(position*4 + 2).getPay_channe());
				holder.ys3.setText("已售"
						+ list.get(position*4 + 2).getVolume_number());
				holder.ll_m4.setVisibility(View.INVISIBLE);
				break;
			case 4:
				holder.title.setText(list.get(position*4).getTitle());
				holder.tv_price.setText("￥"
						+ list.get(position*4).getRetail_price() / 100 + "");
				holder.tv_td.setText(list.get(position*4).getPay_channe());
				holder.ys.setText("已售" + list.get(position*4).getVolume_number());

				holder.title2.setText(list.get(position*4+1).getTitle());
				holder.tv_price2.setText("￥"
						+ list.get(position*4 + 1).getRetail_price() / 100 + "");
				holder.tv_td2.setText(list.get(position*4 + 1).getPay_channe());
				holder.ys2.setText("已售"
						+ list.get(position*4 + 1).getVolume_number());

				holder.title3.setText(list.get(position*4 + 2).getTitle());
				holder.tv_price3.setText("￥"
						+ list.get(position*4 + 2).getRetail_price() / 100 + "");
				holder.tv_td3.setText(list.get(position*4 + 2).getPay_channe());
				holder.ys3.setText("已售"
						+ list.get(position*4 + 2).getVolume_number());

				holder.title4.setText(list.get(position*4 + 3).getTitle());
				holder.tv_price4.setText("￥"
						+ list.get(position*4 + 3).getRetail_price() / 100 + "");
				holder.tv_td4.setText(list.get(position*4 + 3).getPay_channe());
				holder.ys4.setText("已售"
						+ list.get(position*4 + 3).getVolume_number());
				break;

			}

		} else {

			holder.title.setText(list.get(position*4).getTitle());
			holder.tv_price.setText("￥" + list.get(position*4).getRetail_price()
					/ 100 + "");
			holder.tv_td.setText(list.get(position*4).getPay_channe());
			holder.ys.setText("已售" + list.get(position*4).getVolume_number());

			holder.title2.setText(list.get(position*4+1).getTitle());
			holder.tv_price2.setText("￥"
					+ list.get(position*4 + 1).getRetail_price() / 100 + "");
			holder.tv_td2.setText(list.get(position*4 + 1).getPay_channe());
			holder.ys2.setText("已售" + list.get(position*4 + 1).getVolume_number());

			holder.title3.setText(list.get(position*4 + 2).getTitle());
			holder.tv_price3.setText("￥"
					+ list.get(position*4 + 2).getRetail_price() / 100 + "");
			holder.tv_td3.setText(list.get(position*4 + 2).getPay_channe());
			holder.ys3.setText("已售" + list.get(position*4 + 2).getVolume_number());

			holder.title4.setText(list.get(position*4 + 3).getTitle());
			holder.tv_price4.setText("￥"
					+ list.get(position*4 + 3).getRetail_price() / 100 + "");
			holder.tv_td4.setText(list.get(position*4 + 3).getPay_channe());
			holder.ys4.setText("已售" + list.get(position*4 + 3).getVolume_number());
		}

		return convertView;
	}

	public final class ViewHolder {
		public TextView title, ys, tv_price, tv_td;
		public TextView title2, ys2, tv_price2, tv_td2;
		public TextView title3, ys3, tv_price3, tv_td3;
		public TextView title4, ys4, tv_price4, tv_td4;
		public LinearLayout ll_m1, ll_m2, ll_m3, ll_m4;
		public ImageView im1, im2, im3, im4;
	}
}
