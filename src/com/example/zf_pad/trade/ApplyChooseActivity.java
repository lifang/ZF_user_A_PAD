package com.example.zf_pad.trade;

import static com.example.zf_pad.fragment.Constants.ApplyIntent.CHOOSE_ITEMS;
import static com.example.zf_pad.fragment.Constants.ApplyIntent.CHOOSE_TITLE;
import static com.example.zf_pad.fragment.Constants.ApplyIntent.SELECTED_ID;
import static com.example.zf_pad.fragment.Constants.ApplyIntent.SELECTED_TITLE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.epalmpay.userPad.R;
import com.example.zf_pad.BaseActivity;
import com.example.zf_pad.trade.entity.ApplyChooseItem;
import com.example.zf_pad.util.TitleMenuUtil;

public class ApplyChooseActivity extends BaseActivity {

	private int selectedId;
	private ListView mList;
	private List<ApplyChooseItem> chooseItems = new ArrayList<ApplyChooseItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_simple_list);

		mList = (ListView) findViewById(R.id.list);
		String title = getIntent().getStringExtra(CHOOSE_TITLE);
		selectedId = getIntent().getIntExtra(SELECTED_ID, 0);
		chooseItems = (List<ApplyChooseItem>) getIntent().getSerializableExtra(
				CHOOSE_ITEMS);
		new TitleMenuUtil(this, title).show();

		loadData();

	}

	private void loadData() {
		final List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		for (ApplyChooseItem chooseItem : chooseItems) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("id", chooseItem.getId());
			item.put("name", chooseItem.getTitle());
			item.put("selected",
					chooseItem.getId() == selectedId ? R.drawable.icon_selected
							: null);
			items.add(item);
		}
		final SimpleAdapter adapter = new SimpleAdapter(this, items,
				R.layout.simple_list_item, new String[] { "id", "name",
						"selected" }, new int[] { R.id.item_id, R.id.item_name,
						R.id.item_selected });
		mList.setAdapter(adapter);
		mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int arg2,
					long arg3) {
				TextView tvId = (TextView) v.findViewById(R.id.item_id);
				TextView tvTitle = (TextView) v.findViewById(R.id.item_name);
				Intent intent = new Intent();
				intent.putExtra(SELECTED_ID,
						Integer.parseInt(tvId.getText().toString()));
				intent.putExtra(SELECTED_TITLE, tvTitle.getText().toString());
				setResult(RESULT_OK, intent);
				finish();

			}
		});

	}

}
