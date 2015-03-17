package com.example.zf_pad.fragment;

import com.example.zf_pad.Config;
import com.example.zf_pad.R;
import com.example.zf_pad.aadpter.HuilvAdapter;
import com.example.zf_pad.util.ScrollViewWithListView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class f_good_detail extends Fragment implements OnClickListener{
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
		 view = inflater.inflate(R.layout.goodmore, container, false);
		 initView();
		return view;
	}
	private void initView() {
		ppxx = (TextView) view.findViewById(R.id.ppxx);
		wkxx = (TextView) view.findViewById(R.id.wkxx);
		TextView dcxx=(TextView) view.findViewById(R.id.dcxx);
		tv_qgd = (TextView)view. findViewById(R.id.tv_qgd);
		tv_jm = (TextView) view.findViewById(R.id.tv_jm);
		pos_lv1=(ScrollViewWithListView) view.findViewById(R.id.pos_lv1);
		pos_lv2=(ScrollViewWithListView) view.findViewById(R.id.pos_lv2);
		pos_lv3=(ScrollViewWithListView) view.findViewById(R.id.pos_lv3);
		lvAdapter1 = new HuilvAdapter(getActivity(),Config.celist);
		lvAdapter2 = new HuilvAdapter(getActivity(),Config.tDates,0);
		lvAdapter3 = new HuilvAdapter(getActivity(),Config.other_rate,1,"1");
		pos_lv1.setAdapter(lvAdapter1);
		pos_lv2.setAdapter(lvAdapter2);
		pos_lv3.setAdapter(lvAdapter3);
		ppxx.setText(Config.gfe.getModel_number() );
		wkxx.setText(Config.gfe.getShell_material() );
		dcxx.setText(Config.gfe.getBattery_info());
		tv_qgd.setText(Config.gfe.getSign_order_way());
		tv_jm.setText(Config.gfe.getEncrypt_card_way());
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

}
