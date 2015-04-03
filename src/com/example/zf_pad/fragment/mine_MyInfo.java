package com.example.zf_pad.fragment;


import com.example.zf_pad.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class mine_MyInfo extends Fragment implements OnClickListener{
	private View view;
	private TextView tv_score,tv_manageradress,tv_info,tv_safe;
	private Mine_score score;
	private mine_Address address;
	private Mine_baseinfo info;
	private Mine_chgpaw chgpaw;
	private FragmentTransaction transaction ;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// view = inflater.inflate(R.layout.f_mine, container,false);
		// initView();

		if (view != null) {
			Log.i("222222", "11111111");
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null)
				parent.removeView(view);
		}
		try {
			view = inflater.inflate(R.layout.f_mine_myinfo, container, false);
			
		} catch (InflateException e) {

		}

		return view;
	}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		init();
	}
	
	private void init() {
		tv_safe=(TextView) view.findViewById(R.id.tv_safe);
		tv_score=(TextView) view.findViewById(R.id.tv_score);
		tv_manageradress=(TextView) view.findViewById(R.id.tv_manageradress);
		tv_info=(TextView) view.findViewById(R.id.tv_info);
		tv_score.setOnClickListener(this);
		tv_manageradress.setOnClickListener(this);
		tv_info.setOnClickListener(this);
		tv_safe.setOnClickListener(this);
		transaction = getActivity()
				.getSupportFragmentManager().beginTransaction();
	}
	@Override
	public void onDestroyView() {
		try {
		
		if (address != null)
			transaction.remove(address);
		if (score != null)
			transaction.remove(score);
		if (info != null)
			transaction.remove(info);
		if(chgpaw!=null)
			transaction.remove(chgpaw);
		transaction.commit();
		} catch (Exception e) {
		}
		super.onDestroyView();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_score:
			if (score == null)
				score = new Mine_score();
			getActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.fm, score).commit();
			//mine_Address.ll_address.setVisibility(View.GONE);
				
			break;
		case R.id.tv_manageradress:
			if(address==null)
				address=new mine_Address();
			getActivity().getSupportFragmentManager().beginTransaction()
			.replace(R.id.fm, address).commit();
			break;
		case R.id.tv_info:
			if(info==null)
				info=new Mine_baseinfo();
			getActivity().getSupportFragmentManager().beginTransaction()
			.replace(R.id.fm, info).commit();
			break;
		case R.id.tv_safe:
			if(chgpaw==null)
				chgpaw=new Mine_chgpaw();
			getActivity().getSupportFragmentManager().beginTransaction()
			.replace(R.id.fm, chgpaw).commit();
			break;
		default:
			break;
		}
		
	}
	
}
