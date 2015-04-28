package com.example.zf_pad.aadpter;

import java.util.List;

import org.apache.http.Header;

import com.example.zf_pad.Config;
import com.example.zf_pad.MyApplication;
import com.example.zf_pad.R;
import com.example.zf_pad.entity.MyShopCar.Good;
import com.example.zf_pad.util.AlertDialog;
import com.example.zf_pad.util.ImageCacheUtil;
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

public class ShopcarAdapter extends BaseAdapter {
	private Context context;
	private List<Good> list;
	private LayoutInflater inflater;
	private ViewHolder holder = null;
	private TextView howMoney;
	private Activity activity;
	private int currentHowMoney;
	private CheckBox selectAll_cb;
	private AlertDialog ad;
	
	public ShopcarAdapter(Context context, List<Good> list) {
		this.context = context;
		this.list = list;
		activity = (Activity) context;
		currentHowMoney = 0;
		howMoney = (TextView) activity.findViewById(R.id.howMoney);
		tv_gj = (TextView)activity.findViewById(R.id.tv_gj);
		selectAll_cb = (CheckBox) activity.findViewById(R.id.item_cb);
		selectAll_cb.setOnCheckedChangeListener(onCheckedChangeListener);
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
			holder.position = position;
			convertView = inflater.inflate(R.layout.sopping_caritem, null);
			holder.del=(TextView)convertView.findViewById(R.id.del);
			holder.del.setId(position);
			holder.del.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(final View v) {
					final int id=list.get(v.getId()).getId();
					
					ad = new AlertDialog(context);
					ad.setTitle("提示");
					ad.setMessage("确定要删除?");
					ad.setPositiveButton("取消", new OnClickListener() {				
						@Override
						public void onClick(View arg0) {
							ad.dismiss();				
						}
					});
					ad.setNegativeButton("确定", new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							ad.dismiss();
							del(id,v.getId());						
						}
					});					
				}
			});
			holder.checkBox = (CheckBox) convertView.findViewById(R.id.item_cb);
			holder.checkBox.setOnCheckedChangeListener(onCheckedChangeListener);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.wayName = (TextView) convertView.findViewById(R.id.wayName);
			holder.Model_number = (TextView) convertView
					.findViewById(R.id.Model_number);
			 holder.title = (TextView) convertView.findViewById(R.id.title);
			 holder.evevt_img = (ImageView)
			 convertView.findViewById(R.id.evevt_img);
			
			//holder.editBtn = (TextView) convertView.findViewById(R.id.editView);
			//holder.editBtn.setOnClickListener(onClick);
			holder.ll_select = (LinearLayout) convertView
					.findViewById(R.id.ll_select);
			//holder.editBtn.setTag(holder);
			holder.reduce = convertView.findViewById(R.id.reduce);
			holder.buyCountEdit = (EditText) convertView
					.findViewById(R.id.buyCountEdit);
			holder.showCountText = (TextView) convertView
					.findViewById(R.id.showCountText);
			
			holder.add = convertView.findViewById(R.id.add);

			holder.reduce.setTag(holder);
			holder.add.setTag(holder);

			holder.reduce.setOnClickListener(onClick);
			holder.add.setOnClickListener(onClick);
			holder.retail_price = (TextView) convertView
					.findViewById(R.id.retail_price);
			//holder.delete = convertView.findViewById(R.id.delete);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.checkBox.setTag(position);
		Good good = list.get(position);
		holder.checkBox.setChecked(good.isChecked());

		holder.title.setText(good.getTitle());
		holder.showCountText.setText("X  " + good.getQuantity());
		holder.buyCountEdit.setText("" + good.getQuantity());
		holder.buyCountEdit.getText();
		holder.retail_price.setText("￥ " + ((double)good.getRetail_price())/100);
		holder.wayName.setText(good.getName());
		holder.Model_number.setText(good.getModel_number());
		 ImageCacheUtil.IMAGE_CACHE.get(good.getUrl_path(), holder.evevt_img);
		return convertView;
	}

	private OnClickListener onClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			ViewHolder hoder = (ViewHolder) v.getTag();
			int position = hoder.position;
			Good editGood = list.get(position);
		  	int quantity = editGood.getQuantity();
		 	//int quantity = Integer.parseInt(holder.buyCountEdit.getText().toString());
			switch (v.getId()) {
	
			case R.id.reduce:

				if (quantity > 0) {
					editGood.setQuantity(--quantity);
					hoder.buyCountEdit.setText(editGood.getQuantity() + "");
					hoder.showCountText.setText("X  " + editGood.getQuantity());
					if (hoder.checkBox.isChecked()) {
						currentHowMoney -= editGood.getRetail_price();
						howMoney.setText("合计:" + ((double)currentHowMoney)/100);
						//tv_gj.setText("共计"+editGood.getQuantity()+"件");
					}
					
				    changeContent(position, quantity);
				}
				break;
			case R.id.add:
				editGood.setQuantity(++quantity);
				hoder.buyCountEdit.setText(editGood.getQuantity() + "");
				hoder.showCountText.setText("X  " + editGood.getQuantity());
				if (hoder.checkBox.isChecked()) {
					currentHowMoney += editGood.getRetail_price();
					howMoney.setText("合计:" + ((double)currentHowMoney)/100);
					//tv_gj.setText("共计"+editGood.getQuantity()+"件");
				}
				 changeContent(position, quantity);
				break;

			}

		}

	};

	public void del(int id,final int position) {
			String url =  Config.SHOPDELETE;
			RequestParams params = new RequestParams();
			params.put("id", id);
	
			params.setUseJsonStreamer(true);

			MyApplication.getInstance().getClient()
					.post(url, params, new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int statusCode, Header[] headers,
								byte[] responseBody) {
							String responseMsg = new String(responseBody)
									.toString();
							list.remove(position);
						 notifyDataSetChanged();
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								byte[] responseBody, Throwable error) {
							// TODO Auto-generated method stub
							System.out.println("-onFailure---");
							Log.e("print", "-onFailure---" + error);
						}
					});
	}
	private int flag=0;
	private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			if (selectAll_cb == buttonView) {
				for (int index = 0; index < list.size(); index++) {
					list.get(index).setChecked(isChecked);
				}

				notifyDataSetChanged();
			} else {
				if(isChecked){
					flag++;
				}else{
					flag--;
				}
				if(flag==0){
					selectAll_cb.setChecked(false);
				}else if(flag==list.size()){
					selectAll_cb.setChecked(true);
				}
				int position = (Integer) buttonView.getTag();
				Good good = list.get(position);
				good.setChecked(isChecked);
				currentHowMoney += (isChecked ? good.getRetail_price()
						* good.getQuantity() : -good.getRetail_price()
						* good.getQuantity());
				howMoney.setText("合计:" + ((double)currentHowMoney)/100);
				//tv_gj.setText("共计"+good.getQuantity()+"件");
				Log.e("print", "currentHowMoney:"+currentHowMoney);
			}

		}
	};
	private TextView tv_gj;
	public void changeContent(final int index,final int cont){
		 
			String url =  Config.Car_edit;
			RequestParams params = new RequestParams();
			params.put("id", list.get(index).getId());
			params.put("quantity", cont);
			params.setUseJsonStreamer(true);

			MyApplication.getInstance().getClient()
					.post(url, params, new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int statusCode, Header[] headers,
								byte[] responseBody) {
							String responseMsg = new String(responseBody)
									.toString();
							Log.e("print", responseMsg);

						 list.get(index).setQuantity(cont);
						 notifyDataSetChanged();

						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								byte[] responseBody, Throwable error) {
							// TODO Auto-generated method stub
							System.out.println("-onFailure---");
							Log.e("print", "-onFailure---" + error);
						}
					});
	 
		 
		 
	}
	public final class ViewHolder {
		private int position;
		private CheckBox checkBox;
		private TextView title;
		private TextView editBtn;
		private LinearLayout ll_select;
		private TextView retail_price;
		private View delete;
		private EditText buyCountEdit;
		private TextView showCountText;
		private View reduce;
		private View add;
		public TextView Model_number;
		public TextView wayName,del;
		public ImageView evevt_img;
	}
}
