package com.example.zf_pad.aadpter;

import java.util.List;

import com.example.zf_pad.R;
import com.example.zf_pad.entity.Score;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ScoreAdapter extends BaseAdapter{
	private List<Score> datasco;
	private Context context;
	private LayoutInflater mInflater;
	public ScoreAdapter(List<Score> datasco,Context context){
		super();
		this.datasco=datasco;
		this.context=context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datasco.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return datasco.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return datasco.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHoldel holdel;
		if(convertView == null){
			mInflater=LayoutInflater.from(context);
			convertView=mInflater.inflate(R.layout.soreitem, null);
			holdel=new ViewHoldel();
			holdel.number=(TextView) convertView.findViewById(R.id.number);
			holdel.time=(TextView) convertView.findViewById(R.id.time);
			holdel.gotscore=(TextView) convertView.findViewById(R.id.gotscore);
			holdel.scoretype=(TextView) convertView.findViewById(R.id.scoretype);
			convertView.setTag(holdel);
			}
		else{
			holdel=(ViewHoldel) convertView.getTag();
		}
		holdel.number.setText(datasco.get(position).getNumber());
		holdel.time.setText(datasco.get(position).getTime());
		holdel.gotscore.setText(datasco.get(position).getMoney());
		holdel.scoretype.setText(datasco.get(position).getScoretype());
		return convertView;
	}
	public static class ViewHoldel{
		TextView number;
		TextView time;
		TextView gotscore;
		TextView scoretype;
	}
}
