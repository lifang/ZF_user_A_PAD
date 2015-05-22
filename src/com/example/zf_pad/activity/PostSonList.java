package com.example.zf_pad.activity;
import com.example.zf_pad.BaseActivity;
import com.example.zf_pad.Config;
import com.example.zf_pad.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

public class PostSonList extends BaseActivity {
	private ListView list;
	private MyAdapter adapter;
	private Button bt_close;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.portson);
		Config.myson=null;
		list = (ListView)findViewById(R.id.list);
		adapter = new MyAdapter();
		list.setAdapter(adapter);
		bt_close = (Button)findViewById(R.id.close);
		bt_close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				PostSonList.this.finish();
				
			}
		});
	}
	
	class MyAdapter extends BaseAdapter{
		private LayoutInflater inflater;
		private TextView text;
		private CheckBox cb;
		@Override
		public int getCount() {

			return Config.son.size();
		}

		@Override
		public Object getItem(int position) {

			return Config.son.get(position);
		}

		@Override
		public long getItemId(int arg0) {

			return 0;
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2) {
			inflater = LayoutInflater.from(PostSonList.this);
			view = inflater.inflate(R.layout.portsonitem, null);
			text = (TextView)view.findViewById(R.id.text);
			cb = (CheckBox)view.findViewById(R.id.item_cb);
			cb.setId(position);
			cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton bt, boolean arg1) {
					if (arg1) {
						Config.myson=Config.son.get(bt.getId());
								//Toast.makeText(getApplicationContext(), Config.myson.getValue(), 1000).show();
						PostSonList.this.finish();
					} else {
						
					}

				}
			});
			text.setText(Config.son.get(position).getValue());
			return view;
		}
		
	}
	
}
