package com.example.zf_pad.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.zf_pad.Config;
import com.example.zf_pad.R;
import com.example.zf_pad.aadpter.JiaoyiHuilvAdapter;
import com.example.zf_pad.entity.ChanelEntitiy;
import com.example.zf_pad.util.TitleMenuUtil;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ListView;
public class Good_detail_trade extends Fragment implements OnClickListener{
	private View view;
	private ListView lv;
	private JiaoyiHuilvAdapter myAdapter;
	private List<ChanelEntitiy> celist = new ArrayList<ChanelEntitiy>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 view = inflater.inflate(R.layout.act_tradereate, container, false);
		 initView();
		return view;
	}
	private void initView() {
		lv=(ListView) view.findViewById(R.id.lv);
		
		ChanelEntitiy ce=new ChanelEntitiy();
		ce.setName("Ω·À„ ±º‰");
		ce.setService_rate( 10000);
		celist.clear();
		celist.addAll(Config.celist2);
		myAdapter =new  JiaoyiHuilvAdapter(getActivity(),celist);
		lv.setAdapter(myAdapter);

	}

	@Override
	public void onClick(View arg0) {
		
	}

}
