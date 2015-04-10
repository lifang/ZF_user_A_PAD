package com.example.zf_pad.aadpter;

import java.util.List;

import com.example.zf_pad.MyApplication;
import com.example.zf_pad.R;
import com.example.zf_pad.entity.MessageEntity;
import com.example.zf_pad.entity.TestEntitiy;

import android.app.Activity;
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

public class MessageAdapter extends BaseAdapter {
	private Context context;
	private List<MessageEntity> list;
	private LayoutInflater inflater;
	private ViewHolder holder = null;
	private int type;
	private CheckBox cb_all;
	private int flag=0;
	Activity a;
	public MessageAdapter(Context context, List<MessageEntity> list,CheckBox c) {
		this.context = context;
		this.list = list;
		this.cb_all=c;
	}
	public MessageAdapter(Context context, List<MessageEntity> list,int type) {
		this.context = context;
		this.list = list;
		this.type=type;
	
		
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
			convertView = inflater.inflate(R.layout.message_item, null);
			holder.tv_title = (TextView) convertView
					.findViewById(R.id.tv_title);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.item_cb = (CheckBox) convertView.findViewById(R.id.item_cb);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_title.setText(list.get(position).getTitle());
		holder.tv_time.setText(list.get(position).getCreate_at());
		
		
		if(type!=1){
		
			if(list.get(position).getStatus()){
				holder.tv_title.setTextColor(context.getResources().getColor(R.color.NoRead));
				holder.tv_time.setTextColor(context.getResources().getColor(R.color.NoRead));
			}
			//list.get(position).setIscheck(holder.item_cb.isChecked());
			if(list.get(position).getIscheck()){
				holder.item_cb.setChecked(true);
			}else{
				holder.item_cb.setChecked(false);
			}
			
			holder.item_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {	
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					list.get(position).setIscheck(isChecked);
					if(isChecked){
						flag++;
					}else{
						flag--;
					}
					if(flag==list.size()){
						cb_all.setChecked(true);
					}else if(flag==0){
						cb_all.setChecked(false);
					}
				}
			});
	
		}else{
			holder.item_cb.setVisibility(View.GONE);
		}	
		return convertView;
	}
	public final class ViewHolder {
		public TextView tv_title, tv_time;
		public CheckBox item_cb;

	}
}
