package com.example.zf_pad.trade;

import static com.example.zf_pad.fragment.Constants.ApplyIntent.CHOOSE_ITEMS;
import static com.example.zf_pad.fragment.Constants.ApplyIntent.CHOOSE_TITLE;
import static com.example.zf_pad.fragment.Constants.ApplyIntent.SELECTED_ID;
import static com.example.zf_pad.fragment.Constants.ApplyIntent.SELECTED_TITLE;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zf_pad.Config;
import com.example.zf_pad.R;
import com.example.zf_pad.trade.common.CommonUtil;
import com.example.zf_pad.trade.common.HttpCallback;
import com.example.zf_pad.trade.common.PageMerchane;
import com.example.zf_pad.trade.entity.ApplyChooseItem;
import com.example.zf_pad.util.TitleMenuUtil;
import com.example.zf_pad.util.Tools;
import com.google.gson.reflect.TypeToken;

public class ApplyChooseActivity extends Activity {

	private int page = 0;
	private int total = 0;
	private final int rows = 10;
	private int mTerminalId, selectedId;
	private ListView mList;
	private List<ApplyChooseItem> mItems = new ArrayList<ApplyChooseItem>();
	private LayoutInflater mInflater;
	private MyListAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_simple_list);

		mInflater = LayoutInflater.from(this);
		mList = (ListView) findViewById(R.id.list);
		String title = getIntent().getStringExtra(CHOOSE_TITLE);
		mTerminalId = getIntent().getIntExtra(CHOOSE_ITEMS, 0);
		selectedId = getIntent().getIntExtra(SELECTED_ID, 0);

		new TitleMenuUtil(this, title).show();
		mAdapter = new MyListAdapter();
		mList.setAdapter(mAdapter);
		loadData();

	}

	private void loadData() {

		API.getApplyMerchantDetail(ApplyChooseActivity.this, mTerminalId,
				new HttpCallback<PageMerchane<ApplyChooseItem>>(
						ApplyChooseActivity.this) {

					@Override
					public void onSuccess(PageMerchane<ApplyChooseItem> data) {
						if (data != null) {
							mItems.addAll(data.getList());
							mAdapter.notifyDataSetChanged();
						}

					}

					@Override
					public TypeToken<PageMerchane<ApplyChooseItem>> getTypeToken() {

						return new TypeToken<PageMerchane<ApplyChooseItem>>() {
						};
					}
				});

	}

	class MyListAdapter extends BaseAdapter {
		MyListAdapter() {
		}

		@Override
		public int getCount() {
			return mItems.size();
		}

		@Override
		public ApplyChooseItem getItem(int i) {
			return mItems.get(i);
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public View getView(int i, View convertView, ViewGroup viewGroup) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater
						.inflate(R.layout.simple_list_item, null);
				holder = new ViewHolder();
				holder.tvName = (TextView) convertView
						.findViewById(R.id.item_name);
				holder.tvID = (TextView) convertView.findViewById(R.id.item_id);
				holder.ivSelected = (ImageView) convertView
						.findViewById(R.id.item_selected);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			ApplyChooseItem item = getItem(i);
			holder.tvName.setText(item.getTitle());
			holder.tvID.setText(item.getId() + "");
			convertView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					TextView tvId = (TextView) v.findViewById(R.id.item_id);
					TextView tvTitle = (TextView) v
							.findViewById(R.id.item_name);
					Intent intent = new Intent();
					intent.putExtra(SELECTED_ID,
							Integer.parseInt(tvId.getText().toString()));
					intent.putExtra(SELECTED_TITLE, tvTitle.getText()
							.toString());
					setResult(RESULT_OK, intent);
					finish();
				}
			});
			return convertView;
		}
	}

	static class ViewHolder {
		public TextView tvName;
		public TextView tvID;
		public ImageView ivSelected;
	}

}
