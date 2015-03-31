package com.example.zf_pad.fragment;

import static com.example.zf_pad.fragment.Constants.CityIntent.CITY_ID;
import static com.example.zf_pad.fragment.Constants.CityIntent.CITY_NAME;

import com.example.zf_pad.R;
import com.example.zf_pad.activity.MainActivity;
import com.example.zf_pad.trade.CitySelectActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class Mine_baseinfo extends Fragment implements OnClickListener{
	private View view;
	private TextView tv_city_select;
	private String cityName;
	public static final int REQUEST_CITY = 1;
	private int cityId;
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
		view = inflater.inflate(R.layout.baseinfo, container, false);
		init();
	} catch (InflateException e) {

	}

	return view;
}
private void init() {
	tv_city_select=(TextView) view.findViewById(R.id.tv_city_select);
	tv_city_select.setOnClickListener(this);
}
@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
	// TODO Auto-generated method stub
	super.onActivityResult(requestCode, resultCode, data);
	switch (requestCode) {
	case REQUEST_CITY:
		cityId = data.getIntExtra(CITY_ID, 0);
		cityName = data.getStringExtra(CITY_NAME);
		tv_city_select.setText(cityName);
		break;

	default:
		break;
	}
}
@Override
public void onClick(View v) {
	switch (v.getId()) {
	case R.id.tv_city_select:
		Intent intent = new Intent(getActivity(),
				CitySelectActivity.class);
		intent.putExtra(CITY_NAME, cityName);
		startActivityForResult(intent, REQUEST_CITY);
		break;

	default:
		break;
	}
	
}
}
