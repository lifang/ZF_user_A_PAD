package com.example.zf_pad.fragment;

import com.example.zf_pad.Config;
import com.example.zf_pad.R;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class M_my extends Fragment implements OnClickListener {
	private View view;
	private RelativeLayout ll_dd;
	private RelativeLayout ll_shjl;
	private RelativeLayout ll_myinfo;
	private RelativeLayout ll_mysh;
	private RelativeLayout ll_plan;
	private Mine_Dd m_dd;
	private Mine_Shjl m_shjl;
	private Mine_MyInfo m_info;
	private Mine_MyMerChant m_sh;
	private Mine_Plan m_plan;
	private Mine_chgpaw m_chgpaw;
	private Mine_Address m_address;
	private Mine_score m_score;
	private ImageView im1,im2,im3,im4,im5;
	private TextView tvdd;
	private TextView tvshjv;
	private TextView tvwdxx;
	private TextView tvwdsh;
	private TextView tvsq;
    private Message msg;
    public static boolean isHidden=false;
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
			view = inflater.inflate(R.layout.f_mine, container, false);
			
			initView();
		} catch (InflateException e) {

		}

		return view;
	}

	@Override
	public void onStart() {
		
		super.onStart();
		switch (Config.MyTab) {
		
		case 0:
			setback();
			im1.setVisibility(View.VISIBLE);
			tvdd.setTextColor(getResources().getColor(R.color.o));
			if(m_dd==null)
			m_dd = new Mine_Dd();

			getActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.f_mine, m_dd).commit();
			break;
		case 1:
			setback();
			im2.setVisibility(View.VISIBLE);
			tvshjv.setTextColor(getResources().getColor(R.color.o));
			break;
		case 2:
			setback();
			im3.setVisibility(View.VISIBLE);
			tvwdxx.setTextColor(getResources().getColor(R.color.o));
			break;
		case 3:
			setback();
			im4.setVisibility(View.VISIBLE);
			tvwdsh.setTextColor(getResources().getColor(R.color.o));
			break;
		case 4:
			setback();
			im5.setVisibility(View.VISIBLE);
			tvsq.setTextColor(getResources().getColor(R.color.o));
			break;
		default:
			break;
		}
		
	}
@Override
public void onPause() {
	// TODO Auto-generated method stub
	super.onPause();
}
	private void initView() {
		if(Mine_Dd.myHandler!=null){
		msg=Mine_Dd.myHandler.obtainMessage();
		msg.what=1023;
		}
		ll_dd = (RelativeLayout) view.findViewById(R.id.ll_dd);
		ll_shjl = (RelativeLayout) view.findViewById(R.id.ll_shjl);
		ll_myinfo = (RelativeLayout) view.findViewById(R.id.ll_myinfo);
		ll_mysh = (RelativeLayout) view.findViewById(R.id.ll_mysh);
		ll_plan = (RelativeLayout) view.findViewById(R.id.ll_plan);
		ll_dd.setOnClickListener(this);
		ll_shjl.setOnClickListener(this);
		ll_myinfo.setOnClickListener(this);
		ll_mysh.setOnClickListener(this);
		ll_plan.setOnClickListener(this);

		im1 = (ImageView) view.findViewById(R.id.im1);
		im2 = (ImageView) view.findViewById(R.id.im2);
		im3 = (ImageView) view.findViewById(R.id.im3);
		im4 = (ImageView) view.findViewById(R.id.im4);
		im5 = (ImageView) view.findViewById(R.id.im5);

		tvdd = (TextView) view.findViewById(R.id.textdd);
		tvshjv = (TextView) view.findViewById(R.id.textshjl);
		tvwdxx = (TextView) view.findViewById(R.id.textwdxx);
		tvwdsh = (TextView) view.findViewById(R.id.textwdsh);
		tvsq = (TextView) view.findViewById(R.id.textsq);

	}

	private void setback() {

		im1.setVisibility(View.GONE);
		im2.setVisibility(View.GONE);
		im3.setVisibility(View.GONE);
		im4.setVisibility(View.GONE);
		im5.setVisibility(View.GONE);
		tvdd.setTextColor(getResources().getColor(R.color.white));
		tvshjv.setTextColor(getResources().getColor(R.color.white));
		tvwdxx.setTextColor(getResources().getColor(R.color.white));
		tvwdsh.setTextColor(getResources().getColor(R.color.white));
		tvsq.setTextColor(getResources().getColor(R.color.white));
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ll_dd:

			// if (m_dd == null)
			m_dd = new Mine_Dd();

			getActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.f_mine, m_dd).commit();

			setback();
			im1.setVisibility(View.VISIBLE);
			tvdd.setTextColor(getResources().getColor(R.color.o));
			Config.MyTab=0;
			break;
		case R.id.ll_shjl:
			// if (m_shjl == null)
			m_shjl = new Mine_Shjl();

			getActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.f_mine, m_shjl).commit();
			setback();
			im2.setVisibility(View.VISIBLE);
			tvshjv.setTextColor(getResources().getColor(R.color.o));
			if(!isHidden)
				msg.sendToTarget();
			Config.MyTab=1;
			break;
			
		case R.id.ll_myinfo:
			if(Mine_MyInfo.mRecordType!=0){
				return;
			}
			if (m_info == null)
				m_info = new Mine_MyInfo();
			getActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.f_mine, m_info).commit();
			setback();
			im3.setVisibility(View.VISIBLE);
			tvwdxx.setTextColor(getResources().getColor(R.color.o));
			if(!isHidden){
				msg.sendToTarget();	
			}
			Config.MyTab=2;
			break;
		case R.id.ll_mysh:
			// if (m_sh == null)
			m_sh = new Mine_MyMerChant();

			getActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.f_mine, m_sh).commit();
			setback();
			im4.setVisibility(View.VISIBLE);
			tvwdsh.setTextColor(getResources().getColor(R.color.o));
			if(!isHidden)
			msg.sendToTarget();
			Config.MyTab=3;
			break;
		case R.id.ll_plan:
			// if (m_plan == null)
			m_plan = new Mine_Plan();

			getActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.f_mine, m_plan).commit();

			setback();
			im5.setVisibility(View.VISIBLE);
			tvsq.setTextColor(getResources().getColor(R.color.o));
			if(!isHidden)
			msg.sendToTarget();
			Config.MyTab=4;
			break;
		default:
			break;
		}

	}

	@Override
	public void onDestroyView() {
		Log.i("onDestroyView", "onDestroyView");
		try {
			FragmentTransaction transaction = getActivity()
					.getSupportFragmentManager().beginTransaction();
			;
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
@Override
public void onAttach(Activity activity) {
	super.onAttach(activity);
}
	@Override
	public void onResume() {
		
		super.onResume();
		
		if (Config.AderssManger) {
			if (m_info == null)
				m_info = new Mine_MyInfo();

			getActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.f_mine, m_info).commit();
			setback();
			im3.setVisibility(View.VISIBLE);
			tvwdxx.setTextColor(getResources().getColor(R.color.o));
		}

	}

}
