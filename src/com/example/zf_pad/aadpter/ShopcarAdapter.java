package com.example.zf_pad.aadpter;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.example.zf_pad.Config;
import com.example.zf_pad.MyApplication;
import com.epalmpay.userPad.R;
import com.example.zf_pad.entity.MyShopCar.Good;
import com.example.zf_pad.util.AlertDialog;
import com.example.zf_pad.util.ImageCacheUtil;
import com.example.zf_pad.util.StringUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ShopcarAdapter extends BaseAdapter {
	private Context context;
	private List<Good> list;
	private LayoutInflater inflater;
	private ViewHolder holder = null;
	private TextView howMoney;
	private TextView tv_gj;
	private Activity activity;
	private int currentHowMoney;
	private CheckBox selectAll_cb;
	private AlertDialog ad;
	private boolean isSelectAll = false;

	public ShopcarAdapter(Context context, List<Good> list) {
		this.context = context;
		this.list = list;
		activity = (Activity) context;
		currentHowMoney = 0;
		howMoney = (TextView) activity.findViewById(R.id.howMoney);
		tv_gj = (TextView)activity.findViewById(R.id.tv_gj);

		selectAll_cb = (CheckBox) activity.findViewById(R.id.item_cb);
		selectAll_cb.setOnClickListener(onClickListenerAll);
		//selectAll_cb.setOnCheckedChangeListener(onCheckedChangeListener);
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
		//	if (convertView == null) {
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
		holder.evevt_img = (ImageView)
				convertView.findViewById(R.id.evevt_img);

		//holder.editBtn = (TextView) convertView.findViewById(R.id.editView);
		//holder.editBtn.setOnClickListener(onClick);
		holder.ll_select = (LinearLayout) convertView
				.findViewById(R.id.ll_select);
		//holder.editBtn.setTag(holder);
		holder.reduce = (TextView) convertView.findViewById(R.id.reduce);
		holder.buyCountEdit = (EditText) convertView
				.findViewById(R.id.buyCountEdit);
		holder.showCountText = (TextView) convertView
				.findViewById(R.id.showCountText);
		holder.Model_numberTextView = (TextView) convertView
				.findViewById(R.id.Model_numberTextView);
		holder.wayNameTextView = (TextView) convertView
				.findViewById(R.id.wayNameTextView);
		holder.add = (TextView) convertView.findViewById(R.id.add);

		holder.reduce.setTag(holder);
		holder.add.setTag(holder);

		int h = context.getResources().getDisplayMetrics().heightPixels;
		int w = context.getResources().getDisplayMetrics().widthPixels;
		
		holder.reduce.setOnClickListener(onClick);
		holder.add.setOnClickListener(onClick);
		holder.retail_price = (TextView) convertView
				.findViewById(R.id.retail_price);
		//holder.delete = convertView.findViewById(R.id.delete);
		//			convertView.setTag(holder);
		//		} else {
		//			holder = (ViewHolder) convertView.getTag();
		//		}

		Good good = list.get(position);

		holder.checkBox.setTag(position);
		holder.checkBox.setChecked(good.isChecked());
		holder.title.setText(good.getTitle());
		holder.showCountText.setText("X  " + good.getQuantity());
		holder.buyCountEdit.setText("" + good.getQuantity());
		holder.buyCountEdit.getText();
		holder.retail_price.setText("¥ " + StringUtil.getMoneyString(good.getRetail_price()+good.getOpening_cost()));
		//holder.retail_price.setText("￥ " + ((double)good.getRetail_price())/100);
		//holder.wayName.setText(good.getName());
		//holder.Model_number.setText(good.getModel_number());
		holder.wayNameTextView.setText(good.getName());
		holder.Model_numberTextView.setText(good.getGood_brands()+good.getModel_number());

		if (!StringUtil.isNull(good.getUrl_path())) {
			ImageCacheUtil.IMAGE_CACHE.get(good.getUrl_path(), holder.evevt_img);
		}
		for (int i = 0; i < list.size(); i++) {
			Boolean aBoolean = list.get(i).isChecked();
			if (aBoolean == false) {
				selectAll_cb.setChecked(false);
			}
		}
		holder.buyCountEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
//				if (!StringUtil.isNull(s.toString())) {
//					list.get(position).setQuantity(Integer.valueOf(s.toString()
//							.replaceAll("^(0+)", "")));
//				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}
			@Override
			public void afterTextChanged(Editable s) {
				String text = s.toString();
				int len = s.toString().length(); 
				if (len == 1 && text.equals("0")) { 
					s.clear(); 
				}
			}
		});

		holder.buyCountEdit.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (!StringUtil.isNull(v.getText().toString())) {
					changeContent(position, Integer.valueOf(v.getText().toString()));
					notifyDataSetChanged();
				}
				return false;
			}
		});
		return convertView;
	}

	private OnClickListener onClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			ViewHolder hoder = (ViewHolder) v.getTag();
			int position = hoder.position;
			Good editGood = list.get(position);
			int quantity = editGood.getQuantity();
			//int quantity = Integer.parseInt(holder.buyCountEdit.getText().toString());
			switch (v.getId()) {

			case R.id.reduce:
				if (quantity > 1) {
					editGood.setQuantity(--quantity);
					hoder.buyCountEdit.setText(editGood.getQuantity() + "");
					hoder.showCountText.setText("X  " + editGood.getQuantity());
					computeMoney();
					System.out.println("λ��---"+position+quantity);
					changeContent(position, quantity);
				}

				break;
			case R.id.add:
				editGood.setQuantity(++quantity);
				hoder.buyCountEdit.setText(editGood.getQuantity() + "");
				hoder.showCountText.setText("X  " + editGood.getQuantity());
				computeMoney();
				changeContent(position, quantity);
				break;
			}
		}
	};

	public void del(int id,final int position) {
		String url =  Config.SHOPDELETE;
		//RequestParams params = new RequestParams();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);

		//params.setUseJsonStreamer(true);
		JSONObject jsonParams = new JSONObject(params);
		HttpEntity entity;
		try {
			entity = new StringEntity(jsonParams.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return;
		}
		MyApplication.getInstance().getClient()
		.post(context,url, null,entity,"application/json", new AsyncHttpResponseHandler(){
		//.post(url, params, new AsyncHttpResponseHandler() {

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
	private OnClickListener onClickListenerAll = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (isSelectAll == false) {
				isSelectAll = true;
				selectAll_cb.setChecked(true);
			}else {
				isSelectAll = false;
				selectAll_cb.setChecked(false);
			}
			for (int index = 0; index < list.size(); index++) {
				list.get(index).setChecked(isSelectAll);
			}
			computeMoney();
			notifyDataSetChanged();
		}
	};
	private int flag=0;
	private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
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
			if (list.size() != 0) {
				Good good = list.get(position);
				good.setChecked(isChecked);
				computeMoney();
				int mflag = 0;
				for (int i = 0; i < list.size(); i++) {
					Boolean aBoolean = list.get(i).isChecked();
					if (aBoolean == false) {
						selectAll_cb.setChecked(false);
					}else {
						mflag++;
					}
				}
				if (mflag == list.size()) {
					selectAll_cb.setChecked(true);
				}
			}
		}
	};

	public void changeContent(final int index,final int cont){

		String url =  Config.Car_edit;
	//	RequestParams params = new RequestParams();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", list.get(index).getId());
		params.put("quantity", cont);
		//params.setUseJsonStreamer(true);
		JSONObject jsonParams = new JSONObject(params);
		HttpEntity entity;
		try {
			entity = new StringEntity(jsonParams.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return;
		}
		MyApplication.getInstance().getClient()
		.post(context,url, null,entity,"application/json", new AsyncHttpResponseHandler(){
		//.post(url, params, new AsyncHttpResponseHandler() {

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
		private TextView reduce;
		private TextView add;
		public TextView Model_number,Model_numberTextView;
		public TextView wayName,del,wayNameTextView;
		public ImageView evevt_img;
	}
	private void computeMoney(){
		currentHowMoney = 0;
		int currentQuantity = 0;
		for(Good good: list){
			if(good.isChecked()){
				currentHowMoney += (good.getRetail_price()+good.getOpening_cost())*good.getQuantity();
				currentQuantity += good.getQuantity();
			}
		}
		tv_gj.setText("共计 ： " + currentQuantity + "件商品");
		howMoney.setText("合计 ： ￥" + StringUtil.getMoneyString(currentHowMoney) );
	}
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}
}
