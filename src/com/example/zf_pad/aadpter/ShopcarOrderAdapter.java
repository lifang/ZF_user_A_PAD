package com.example.zf_pad.aadpter;

import java.util.List;

import org.apache.http.Header;

import com.example.zf_pad.Config;
import com.example.zf_pad.MyApplication;
import com.epalmpay.userPad.R;
import com.example.zf_pad.entity.MyShopCar.Good;
import com.example.zf_pad.util.AlertDialog;
import com.example.zf_pad.util.StringUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ShopcarOrderAdapter extends BaseAdapter {
	private Context context;
	private List<Good> list;
	private LayoutInflater inflater;
	private ViewHolder holder = null;
	private TextView howMoney;
	private Activity activity;
	private int currentHowMoney;
	private CheckBox selectAll_cb;
	private AlertDialog ad;
	public ShopcarOrderAdapter(Context context, List<Good> list) {
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
	public View getView(int position, View convertView, ViewGroup parent) {
		inflater = LayoutInflater.from(context);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.shoporderitem, null);	
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.wayName = (TextView) convertView.findViewById(R.id.wayName);
			holder.content2 = (TextView) convertView.findViewById(R.id.content2);
			holder.wayNameTextView = (TextView) convertView.findViewById(R.id.wayNameTextView);
			holder.Model_number = (TextView) convertView
					.findViewById(R.id.Model_number);
			holder.showCountText=(TextView)convertView.findViewById(R.id.showCountText);
			 holder.evevt_img = (ImageView)
			 convertView.findViewById(R.id.evevt_img);
			//holder.editBtn = (TextView) convertView.findViewById(R.id.editView);
			//holder.editBtn.setOnClickListener(onClick);

			//holder.editBtn.setTag(holder);

		
	
			holder.retail_price = (TextView) convertView
					.findViewById(R.id.retail_price);
			//holder.delete = convertView.findViewById(R.id.delete);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		Good good = list.get(position);
		holder.title.setText(good.getTitle());
		holder.showCountText.setText("X "+String.valueOf(good.getQuantity()));
		holder.retail_price.setText("¥ " + StringUtil.getMoneyString(good.getRetail_price()+good.getOpening_cost()));
		holder.wayNameTextView.setText(good.getName());
		holder.content2.setText(good.getModel_number());
		return convertView;
	}

	public final class ViewHolder {
		private int position;
		private CheckBox checkBox;
		private TextView title;
		private TextView editBtn;
		private LinearLayout ll_select;
		private TextView retail_price;
		private View delete;
		private View reduce;
		private View add;
		public TextView Model_number,showCountText;
		public TextView wayName,del,content2,wayNameTextView;
		public ImageView evevt_img;
	}
}
