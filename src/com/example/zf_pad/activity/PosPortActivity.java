package com.example.zf_pad.activity;



import java.util.ArrayList;
import java.util.List;

import com.example.zf_pad.R;
import com.example.zf_pad.aadpter.PosPortAdapter;
import com.example.zf_pad.entity.PostPortEntity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;



public class PosPortActivity extends Activity {
	public ExpandableListView listView;
	private List<PostPortEntity> portlist=new ArrayList<PostPortEntity>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pos_port1);
		initView();
	}

	private void initView() {
		listView = (ExpandableListView)findViewById(R.id.list);
		PostPortEntity pe1=new PostPortEntity();
		pe1.setTitle("1111");
		PostPortEntity pe2=new PostPortEntity();
		pe1.setTitle("2222");
		PostPortEntity pe3=new PostPortEntity();
		pe1.setTitle("3333");
		portlist.add(pe1);
		portlist.add(pe2);
		portlist.add(pe3);
		PosPortAdapter myadapter=new PosPortAdapter(this,portlist);
		myadapter.setListView(listView);
		listView.setAdapter(myadapter);
	}

}