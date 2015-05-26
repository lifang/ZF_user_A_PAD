package com.example.zf_pad.fragment;

import com.example.zf_pad.Config;
import com.epalmpay.userPad.R;
import com.example.zf_pad.aadpter.HuilvAdapter;
import com.example.zf_pad.util.ScrollViewWithListView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class Good_detail_main extends Fragment implements OnClickListener{
	private View view;
	private HuilvAdapter lvAdapter1,lvAdapter2,lvAdapter3;
	private ScrollViewWithListView  pos_lv1,pos_lv2,pos_lv3;
	private TextView ppxx;
	private TextView wkxx;
	private TextView tv_qgd;
	private TextView tv_jm;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 view = inflater.inflate(R.layout.goodmore1, container, false);
		 
		return view;
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

}
