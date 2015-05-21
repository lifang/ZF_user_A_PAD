package com.example.zf_pad.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.zf_pad.Config;
import com.example.zf_pad.R;
import com.example.zf_pad.aadpter.AppleNeedAdapter;
import com.example.zf_pad.aadpter.HuilvAdapter;
import com.example.zf_pad.entity.ApplyneedEntity;
import com.example.zf_pad.util.ScrollViewWithListView;
import com.example.zf_pad.util.TitleMenuUtil;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class Good_detail_apply extends Fragment implements OnClickListener{
	private View view;
	private AppleNeedAdapter myAdapter,myAdapter2;
	private ScrollViewWithListView   pos_lv1,pos_lv2;
	List<ApplyneedEntity>  pubList = new ArrayList<ApplyneedEntity>();
	List<ApplyneedEntity>  singleList = new ArrayList<ApplyneedEntity>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 view = inflater.inflate(R.layout.apply_need, container, false);
		 initView();
		return view;
	}
	private void initView() {
		
		pos_lv1=(ScrollViewWithListView)view. findViewById(R.id.pos_lv1);
		pos_lv2=(ScrollViewWithListView)view. findViewById(R.id.pos_lv2);
		pubList=Config.pub;
		singleList=Config.single;
		System.out.println("`pubList.size()``"+pubList.size());
		myAdapter=new AppleNeedAdapter(getActivity(), singleList);
		myAdapter2=new AppleNeedAdapter(getActivity(), pubList);
		pos_lv1.setAdapter(myAdapter);
		pos_lv2.setAdapter(myAdapter2);
	}

	@Override
	public void onClick(View arg0) {
		
	}

}
