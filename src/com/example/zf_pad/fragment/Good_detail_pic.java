package com.example.zf_pad.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.zf_pad.Config;
import com.epalmpay.userPad.R;
import com.example.zf_pad.aadpter.JiaoyiHuilvAdapter;
import com.example.zf_pad.entity.GoodPic;
import com.example.zf_pad.trade.common.HttpCallback;
import com.example.zf_pad.util.ImageCacheUtil;
import com.google.gson.reflect.TypeToken;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
public class Good_detail_pic extends Fragment implements OnClickListener{
	private View view;
	private ListView lv;
	private JiaoyiHuilvAdapter myAdapter;
	public static List<GoodPic> piclist=new ArrayList<GoodPic>();
	private ListView list;
	private MyAdapter adapter;
	private int GoodId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GoodId=Config.gid;
		getdata();
	}
	private void getdata() {
		
	Config.GOODPICLIST(getActivity(),GoodId,new HttpCallback<List<GoodPic>>(getActivity()){

		@Override
		public void onSuccess(List<GoodPic> data) {		
			piclist.addAll(data);
			adapter.notifyDataSetChanged();
		}

		@Override
		public TypeToken getTypeToken() {
			
			return new TypeToken<List<GoodPic>>() {
			};
		}
		
	});
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 view = inflater.inflate(R.layout.gooddetail_pic, container, false);
		 list = (ListView)view.findViewById(R.id.list);
		 adapter = new MyAdapter();
		 list.setAdapter(adapter);
		 list.setDivider(null);
		return view;
	}
	@Override
	public void onClick(View arg0) {
		
		
	}
	class MyAdapter extends BaseAdapter{
		private LayoutInflater inflater;
		private ImageView im;
		@Override
		public int getCount() {
			
			return piclist.size();
		}

		@Override
		public Object getItem(int arg0) {

			return null;
		}

		@Override
		public long getItemId(int arg0) {
			
			return 0;
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2) {
			inflater = LayoutInflater.from(getActivity());
			view = inflater.inflate(R.layout.good_detail_pic_item, null);
			im = (ImageView)view.findViewById(R.id.pic);
			ImageCacheUtil.IMAGE_CACHE.get(piclist.get(position).getUrlPath(), im);
			return view;
		}
		
	}
	
}
