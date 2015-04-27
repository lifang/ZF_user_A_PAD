package com.example.zf_pad.aadpter;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.zf_pad.Config;
import com.example.zf_pad.MyApplication;
import com.example.zf_pad.R;
import com.example.zf_pad.activity.OrderDetail;
import com.example.zf_pad.activity.PayFromCar;
import com.example.zf_pad.entity.OrderEntity;
import com.example.zf_pad.fragment.Mine_Dd;
import com.example.zf_pad.util.AlertDialog;
import com.example.zf_pad.util.ImageCacheUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
public class OrderAdapter extends BaseAdapter{
	private Context context;
	private List<OrderEntity> list;
	private LayoutInflater inflater;
	private ViewHolder holder = null;
	private Mine_Dd dd;
	public OrderAdapter(Context context, List<OrderEntity> list,Mine_Dd dd) {
		this.context = context;
		this.list = list;
		this.dd=dd;
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
 		if(convertView == null){
			holder = new ViewHolder();
 			convertView = inflater.inflate(R.layout.order_item, null);
 			holder.isshow=(TextView)convertView.findViewById(R.id.tv_isshow);
 			
 			holder.content = (TextView) convertView.findViewById(R.id.content_pp);
 		holder.tv_ddbh = (TextView) convertView.findViewById(R.id.tv_ddbh);		 
 		holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);		
 		holder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);	
 		 
 		holder.ll_ishow = (LinearLayout) convertView.findViewById(R.id.ll_ishow);
 		
 		holder.tv_sum = (TextView) convertView.findViewById(R.id.tv_sum);		
 		holder.tv_psf = (TextView) convertView.findViewById(R.id.tv_psf);		
 		holder.tv_pay = (TextView) convertView.findViewById(R.id.tv_pay);		
 		
 		holder.tv_gtd = (TextView) convertView.findViewById(R.id.tv_gtd);
 		holder.content2 = (TextView) convertView.findViewById(R.id.content2);
 		holder.content_pp = (TextView) convertView.findViewById(R.id.content_pp);
 		holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
 		holder.tv_goodnum = (TextView) convertView.findViewById(R.id.tv_goodnum);
		holder.btn_cancle= (Button) convertView.findViewById(R.id.btn_cancle);
 		holder.btn_pay= (Button) convertView.findViewById(R.id.btn_pay);
 		holder.im=(ImageView)convertView.findViewById(R.id.evevt_img);
 		//holder.btn_cancle.setTag(list.get(position).getOrder_id());
 		holder.btn_cancle.setId(Integer.parseInt(list.get(position).getOrder_id()));
 		convertView.setId(position);
			convertView.setTag(holder);
 		}else{
 		holder = (ViewHolder)convertView.getTag();
 	}
 	
 			
 		if(list.get(position).getOrder_type().equals("2")){
 			holder.isshow.setVisibility(View.VISIBLE);
 		}else{
 			holder.isshow.setVisibility(View.GONE);
 		}
 		if(list.get(position).getOrder_goodsList().size()!=0){
 		
 		 ImageCacheUtil.IMAGE_CACHE.get(list.get(position).getOrder_goodsList().get(0).getGood_logo(), holder.im);
 		holder.tv_price.setText(list.get(position).getOrder_goodsList().get(0).getGood_price().equals("")
 				?"":"￥"+check(list.get(position).getOrder_goodsList().get(0).getGood_price())/100);
 		holder.content2.setText(list.get(position).getOrder_goodsList().get(0).getGood_brand());
 		holder.tv_gtd.setText(list.get(position).getOrder_goodsList().get(0).getGood_channel());
 		holder.content_pp.setText(list.get(position).getOrder_goodsList().get(0).getGood_name()); 		 
 		holder.tv_goodnum.setText(list.get(position).getOrder_goodsList().get(0).getGood_num().equals("")?
 				"":"X   "+list.get(position).getOrder_goodsList().get(0).getGood_num());
 		}
 		holder.tv_pay.setText("实付：￥"+list.get(position).getOrder_totalPrice()/100);
 		holder.tv_psf.setText("配送费：￥"+list.get(position).getOrder_psf()	);
 		holder.tv_ddbh.setText("订单编号: "+list.get(position).getOrder_number()	);
 		holder.tv_time.setText(list.get(position).getOrder_createTime()	);
 		 holder.tv_sum.setText("共计:   "+list.get(position).getOrder_totalNum()	+"件");
 		switch (list.get(position).getOrder_status()) {
		case 1:
			holder.tv_status.setText("未付款");
			holder.ll_ishow.setVisibility(View.VISIBLE);
			break;
		case 2:
			holder.tv_status.setText("已付款");
			holder.ll_ishow.setVisibility(View.GONE);
			break;
		case 3:
			holder.tv_status.setText("已发货");
			holder.ll_ishow.setVisibility(View.GONE);
			break;
		case 4:
			holder.tv_status.setText("已评价");
			holder.ll_ishow.setVisibility(View.GONE);
			break;
		case 5:
			holder.tv_status.setText("已取消");
			holder.ll_ishow.setVisibility(View.GONE);
			break;
		case 6:
			holder.tv_status.setText("交易关闭");
			holder.ll_ishow.setVisibility(View.GONE);
			break;
		default:
		 
			holder.ll_ishow.setVisibility(View.GONE);
			break;
		}
 	 
 		holder.tv_ddbh.setText("订单编号: "+list.get(position).getOrder_number()	);
 		holder.tv_ddbh.setText("订单编号: "+list.get(position).getOrder_number()	);
 		holder.tv_ddbh.setText("订单编号: "+list.get(position).getOrder_number()	);
	holder.btn_cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(final View v) {
				final AlertDialog ad = new AlertDialog(context);
				ad.setTitle("提示");
				ad.setMessage("确认取消?");
				ad.setPositiveButton("取消", new OnClickListener() {				
					@Override
					public void onClick(View arg0) {
						ad.dismiss();				
					}
				});
				ad.setNegativeButton("确定", new OnClickListener() {
					
					@Override
					public void onClick(final View arg0) {
						ad.dismiss();
						RequestParams params = new RequestParams();
						params.put("id", v.getId());
						 System.out.println("`getTag``"+v.getId());
						 
						params.setUseJsonStreamer(true);

						MyApplication.getInstance().getClient()
								.post(Config.ORDERCANL, params, new AsyncHttpResponseHandler() {

									@Override
									public void onSuccess(int statusCode, Header[] headers,
											byte[] responseBody) {
										String responseMsg = new String(responseBody)
												.toString();
										Log.e("print", responseMsg);			 
										Gson gson = new Gson();								
										JSONObject jsonobject = null;
										String code = null;
										try {
											jsonobject = new JSONObject(responseMsg);
											code = jsonobject.getString("code");
											int a =jsonobject.getInt("code");
											if(a==Config.CODE){  
												//String res =jsonobject.getString("result");
												//jsonobject = new JSONObject(res);
												//holder.isshow.setVisibility(View.GONE);
												//list.get((Integer) arg0.getTag()).setOrder_status(6);
												//OrderAdapter.this.notifyDataSetChanged();
												dd.DataChange();
											}else{
												code = jsonobject.getString("message");
												Toast.makeText(context, code, 1000).show();
											}
										} catch (JSONException e) {
											 ;	
											e.printStackTrace();									
										}
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
				});
				
			}
		});
 		holder.btn_pay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(context,PayFromCar.class);
				i.putExtra("orderId", list.get(position).getOrder_id());
				context.startActivity(i);
			}
		});
 	/*	convertView.setId(position);
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//int index=v.getId();
				if(list.size()!=0){
					Intent i = new Intent(context, OrderDetail.class);
					i.putExtra("status", list.get(v.getId()).getOrder_status());
					i.putExtra("id", list.get(v.getId()).getOrder_id());
					i.putExtra("type", list.get(v.getId()).getOrder_type());
					context.startActivity(i);
				}			
				//Toast.makeText(context, list.get(index).getOrder_status()+"+"+list.get(index).getOrder_id(), 1000).show();
				
			}
		});*/
		
		return convertView;
	}

	public  class ViewHolder {
		public TextView tv_goodnum,tv_price,content,tv_ddbh,tv_time,tv_status,tv_sum,tv_psf,tv_pay,tv_gtd,content2,content_pp,isshow;
		private LinearLayout ll_ishow;
		public Button btn_cancle,btn_pay;
		public ImageView im;
	}
	double check(String str) {
		try {

			double min = Double.valueOf(str);// 把字符串强制转换为数字
			return min;// 如果是数字，返回True
		} catch (Exception e) {
			
			return 0;// 如果抛出异常，返回False
		}
	}
}
