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
import android.widget.LinearLayout;

public class m_my extends Fragment implements OnClickListener {
	private View view;
	private LinearLayout ll_dd;
	private LinearLayout ll_shjl;
	private LinearLayout ll_myinfo;
	private LinearLayout ll_mysh;
	private LinearLayout ll_plan;
	private mine_Dd m_dd;
	private mine_Shjl m_shjl;
	private mine_MyInfo m_info;
	private mine_MyMerChant m_sh;
	private mine_Plan m_plan;

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
			view = inflater.inflate(R.layout.f_mine, container, false);
			initView();
		} catch (InflateException e) {

		}

		return view;
	}

	private void initView() {
		ll_dd = (LinearLayout) view.findViewById(R.id.ll_dd);
		ll_shjl = (LinearLayout) view.findViewById(R.id.ll_shjl);
		ll_myinfo = (LinearLayout) view.findViewById(R.id.ll_myinfo);
		ll_mysh = (LinearLayout) view.findViewById(R.id.ll_mysh);
		ll_plan = (LinearLayout) view.findViewById(R.id.ll_plan);
		ll_dd.setOnClickListener(this);
		ll_shjl.setOnClickListener(this);
		ll_myinfo.setOnClickListener(this);
		ll_mysh.setOnClickListener(this);
		ll_plan.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ll_dd:

			if (m_dd == null)
				m_dd = new mine_Dd();
			getActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.f_mine, m_dd).commit();
			break;
		case R.id.ll_shjl:
			if (m_shjl == null)
			m_shjl = new mine_Shjl();
			getActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.f_mine, m_shjl).commit();
			break;
		case R.id.ll_myinfo:
			if(m_info==null)
			m_info = new mine_MyInfo();
			getActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.f_mine, m_info).commit();
			break;
		case R.id.ll_mysh:
			if(m_sh==null)
			m_sh = new mine_MyMerChant();
			getActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.f_mine, m_sh).commit();
			break;
		case R.id.ll_plan:
			if(m_plan==null)
			m_plan = new mine_Plan();
			getActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.f_mine, m_plan).commit();
			break;
		default:
			break;
		}

	}

	@Override
	public void onDestroyView() {
		Log.i("onDestroyView","onDestroyView");
		try {
			FragmentTransaction transaction = getActivity()
					.getSupportFragmentManager().beginTransaction();
			if (m_dd != null)
				transaction.remove(m_dd);
			if (m_shjl != null)
				transaction.remove(m_shjl);
			if (m_info != null)
				transaction.remove(m_info);
			if (m_sh != null)
				transaction.remove(m_sh);
			if (m_plan != null)
				transaction.remove(m_plan);
			transaction.commit();
		} catch (Exception e) {
		}

		super.onDestroyView();
	}
}
