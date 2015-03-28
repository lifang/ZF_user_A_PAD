package com.example.zf_pad.aadpter;

import java.util.List;

import com.example.zf_pad.Config;
import com.example.zf_pad.R;
import com.example.zf_pad.activity.OrderDetail;
import com.example.zf_pad.entity.OrderEntity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
public class OrderAdapter extends BaseAdapter{
	private Context context;
	private List<OrderEntity> list;
	private LayoutInflater inflater;
	private ViewHolder holder = null;
	public OrderAdapter(Context context, List<OrderEntity> list) {
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
 		
 		 
			convertView.setTag(holder);
 		}else{
 		holder = (ViewHolder)convertView.getTag();
 	}
 		if(Config.iszl){
 			holder.isshow.setVisibility(View.VISIBLE);
 		}else{
 			holder.isshow.setVisibility(View.GONE);
 		}
 		holder.tv_price.setText(list.get(position).getOrder_goodsList().get(0).getGood_price().equals("")
 				?"":"��"+list.get(position).getOrder_goodsList().get(0).getGood_price());
 		holder.content2.setText(list.get(position).getOrder_goodsList().get(0).getGood_brand());
 		holder.tv_gtd.setText(list.get(position).getOrder_goodsList().get(0).getGood_channel());
 		holder.content_pp.setText(list.get(position).getOrder_goodsList().get(0).getGood_name()); 		 
 		holder.tv_goodnum.setText(list.get(position).getOrder_goodsList().get(0).getGood_num().equals("")?
 				"":"X   "+list.get(position).getOrder_goodsList().get(0).getGood_num());		
 		holder.tv_pay.setText("ʵ������"+list.get(position).getOrder_totalPrice()/100);
 		holder.tv_psf.setText("���ͷѣ���"+list.get(position).getOrder_psf()	);
 		holder.tv_ddbh.setText("�������: "+list.get(position).getOrder_number()	);
 		holder.tv_time.setText(list.get(position).getOrder_createTime()	);
 		 holder.tv_sum.setText("����:   "+list.get(position).getOrder_totalNum()	+"��");
 		switch (list.get(position).getOrder_status()) {
		case 1:
			holder.tv_status.setText("δ����");
			holder.ll_ishow.setVisibility(View.VISIBLE);
			break;
		case 2:
			holder.tv_status.setText("�Ѹ���");
			holder.ll_ishow.setVisibility(View.GONE);
			break;
		case 3:
			holder.tv_status.setText("�ѷ���");
			holder.ll_ishow.setVisibility(View.GONE);
			break;
		case 4:
			holder.tv_status.setText("������");
			holder.ll_ishow.setVisibility(View.GONE);
			break;
		case 5:
			holder.tv_status.setText("��ȡ��");
			holder.ll_ishow.setVisibility(View.GONE);
			break;
		case 6:
			holder.tv_status.setText("���׹ر�");
			holder.ll_ishow.setVisibility(View.GONE);
			break;
		default:
		 
			holder.ll_ishow.setVisibility(View.GONE);
			break;
		}
 	 
 		holder.tv_ddbh.setText("�������: "+list.get(position).getOrder_number()	);
 		holder.tv_ddbh.setText("�������: "+list.get(position).getOrder_number()	);
 		holder.tv_ddbh.setText("�������: "+list.get(position).getOrder_number()	);
 		
 		convertView.setId(position);
  		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int index=v.getId();
				Intent i = new Intent(context, OrderDetail.class);
				i.putExtra("status", list.get(index).getOrder_status());
				i.putExtra("id", list.get(index).getOrder_id());
				//Toast.makeText(context, list.get(index).getOrder_status()+"+"+list.get(index).getOrder_id(), 1000).show();
				context.startActivity(i);
			}
		});
		
		return convertView;
	}

	public final class ViewHolder {
		public TextView tv_goodnum,tv_price,content,tv_ddbh,tv_time,tv_status,tv_sum,tv_psf,tv_pay,tv_gtd,content2,content_pp,isshow;
		private LinearLayout ll_ishow;
	}
}