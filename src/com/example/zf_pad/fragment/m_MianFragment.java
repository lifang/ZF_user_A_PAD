package com.example.zf_pad.fragment;

import java.util.ArrayList;
import java.util.List;


import com.example.zf_pad.R;
import com.example.zf_pad.aadpter.MessageAdapter;
import com.example.zf_pad.activity.LoginActivity;
import com.example.zf_pad.entity.TestEntitiy;
import com.example.zf_pad.util.XListView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class m_MianFragment extends Fragment implements OnClickListener{
	private View view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
			//view = inflater.inflate(R.layout.f_main,container,false);
			
		
		
		if (view != null) {
	        ViewGroup parent = (ViewGroup) view.getParent();
	        if (parent != null)
	            parent.removeView(view);
	    }
	    try {
	        view = inflater.inflate(R.layout.f_main, container, false);
	        ImageView im=(ImageView)view.findViewById(R.id.testbutton);
	        im.setOnClickListener(this);
	    } catch (InflateException e) {
	        
	    }
	    return view;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.testbutton:
			Intent i=new Intent(getActivity(),LoginActivity.class);
			startActivity(i);
			
			break;

		default:
			break;
		}
		
	}
	@Override
	public void onAttach(Activity activity) {
		
		super.onAttach(activity);
	}
}
