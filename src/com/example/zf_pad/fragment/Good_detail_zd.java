package com.example.zf_pad.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.zf_pad.Config;
import com.example.zf_pad.R;
import com.example.zf_pad.aadpter.AppleNeedAdapter;
import com.example.zf_pad.aadpter.HuilvAdapter;
import com.example.zf_pad.entity.ApplyneedEntity;
import com.example.zf_pad.entity.GoodinfoEntity;
import com.example.zf_pad.util.ScrollViewWithListView;
import com.example.zf_pad.util.TitleMenuUtil;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class Good_detail_zd extends Fragment implements OnClickListener{
	private View view;
	public GoodinfoEntity gfe=new GoodinfoEntity();
	private TextView tv1,tv2,tv3,tv4,tv5;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 view = inflater.inflate(R.layout.act_leas_in, container, false);
		 initView();
		return view;
	}
	private void initView() {
		gfe=Config.gfe;
		System.out.println("````"+gfe.getId());
		tv1=(TextView)view. findViewById(R.id.tv1);
		tv2=(TextView) view.findViewById(R.id.tv2);
		tv3=(TextView) view.findViewById(R.id.tv3);
		tv4=(TextView)view.findViewById(R.id.tv4);
		tv5=(TextView)view. findViewById(R.id.tv5);
		
		tv5.setText(gfe.getLease_agreement()+"");
		tv4.setText(gfe.getDescription()+"");
		tv3.setText(gfe.getLease_price()+"");
		tv2.setText(gfe.getReturn_time()+"");
		tv1.setText(gfe.getLease_time()+"");

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

}
