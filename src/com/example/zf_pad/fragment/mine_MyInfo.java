package com.example.zf_pad.fragment;


import com.example.zf_pad.R;
import com.example.zf_pad.trade.widget.MTabWidget;
import com.example.zf_pad.trade.widget.MTabWidget.OnTabOnclik;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Mine_MyInfo extends Fragment implements OnTabOnclik{
	private View view;
	private TextView tv_score,tv_manageradress,tv_info,tv_safe;
	private Mine_score score;
	private Mine_Address address;
	private Mine_baseinfo info;
	private Mine_chgpaw chgpaw;
	private FragmentTransaction transaction ;
	int mRecordType=0;
	private LinearLayout ll_myinfo;
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
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null)
				parent.removeView(view);
		}
		try {
			view = inflater.inflate(R.layout.f_mine_myinfo, container, false);
			init();
		} catch (InflateException e) {

		}

		return view;
	}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ll_myinfo.setVisibility(View.VISIBLE);
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		ll_myinfo.setVisibility(View.GONE);
	}
	private void init() {
		ll_myinfo=(LinearLayout) view.findViewById(R.id.ll_myinfo);
		MTabWidget mTabWidget = (MTabWidget)view.findViewById(R.id.tab_widget);
		 // add tabs to the TabWidget
       String[] tabs = getResources().getStringArray(R.array.mine_myinfo);
       for (int i = 0; i < tabs.length; i++) {
           mTabWidget.addTab(tabs[i]);
       }
       mTabWidget.updateTabs(0);
       mTabWidget.setonTabLintener(this);
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
	public void chang(int index) {
		mRecordType=index;
		switch (mRecordType) {
		case 0:
			if(info==null)
				info=new Mine_baseinfo();
			getActivity().getSupportFragmentManager().beginTransaction()
			.replace(R.id.fm, info).commit();
			break;
		case 1:
			if(chgpaw==null)
				chgpaw=new Mine_chgpaw();
			getActivity().getSupportFragmentManager().beginTransaction()
			.replace(R.id.fm, chgpaw).commit();
			break;
		case 2:
			if(address==null)
				address=new Mine_Address();
			getActivity().getSupportFragmentManager().beginTransaction()
			.replace(R.id.fm, address).commit();
			break;
		case 3:
			if (score == null)
				score = new Mine_score();
			getActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.fm, score).commit();
			break;
		default:
			break;
		}
	}
	
}
