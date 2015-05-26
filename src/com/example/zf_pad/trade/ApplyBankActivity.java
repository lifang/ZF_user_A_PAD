package com.example.zf_pad.trade;

import static com.example.zf_pad.fragment.Constants.ApplyIntent.SELECTED_BANK;
import static com.example.zf_pad.fragment.Constants.TerminalIntent.TERMINAL_ID;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.epalmpay.userPad.R;
import com.example.zf_pad.BaseActivity;
import com.example.zf_pad.entity.Bank;
import com.example.zf_pad.trade.common.CommonUtil;
import com.example.zf_pad.trade.common.HttpCallback;
import com.example.zf_pad.util.Tools;
import com.example.zf_pad.util.XListView;
import com.google.gson.reflect.TypeToken;

/**
 * Created by Leo on 2015/3/16.
 */
public class ApplyBankActivity extends BaseActivity implements
		View.OnClickListener, XListView.IXListViewListener {

	private LinearLayout titleback_linear_back;
	private TextView titleback_text_title, shuruneirong;
	private ImageView titleback_image_back;

	private String mTerminalNumber, keyword = "";

	private EditText mBankInput;
	private ImageButton mBankSearch;
	private LinearLayout mResultContainer;

	private List<Bank> mBanks = new ArrayList<Bank>();
	private XListView mBankList;
	private BankListAdapter mAdapter;
	private Bank mChosenBank;
	private int page = 0;
	private int pageSize = 20;
	private int mTerminalId;
	private List<Bank> bank;

	private boolean noMoreData = false;
	private String pullType = "loadData";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apply_bank);
		// new TitleMenuUtil(this, getString(R.string.title_apply_choose_bank))
		// .show();
		mTerminalId = getIntent().getIntExtra(TERMINAL_ID, 0);
		mChosenBank = (Bank) getIntent().getSerializableExtra(SELECTED_BANK);

		titleback_linear_back = (LinearLayout) findViewById(R.id.titleback_linear_back);
		titleback_text_title = (TextView) findViewById(R.id.titleback_text_title);
		titleback_image_back = (ImageView) findViewById(R.id.titleback_image_back);
		shuruneirong = (TextView) findViewById(R.id.shuruneirong);
		titleback_linear_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ApplyBankActivity.this.finish();
			}
		});

		titleback_image_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ApplyBankActivity.this.finish();
			}
		});

		titleback_text_title
				.setText(getString(R.string.title_apply_choose_bank));

		shuruneirong.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Bank bank = new Bank();
				bank.setName(mBankInput.getText().toString());
				Intent intent = new Intent();
				intent.putExtra(SELECTED_BANK, bank);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		mBankInput = (EditText) findViewById(R.id.apply_bank_input);
		mBankSearch = (ImageButton) findViewById(R.id.apply_bank_search);
		mBankSearch.setOnClickListener(this);
		mResultContainer = (LinearLayout) findViewById(R.id.apply_bank_result_container);
		mBankList = (XListView) findViewById(R.id.apply_bank_list);
		bank = new ArrayList<Bank>();
		mAdapter = new BankListAdapter();
		mBankList.initHeaderAndFooter();
		mBankList.setXListViewListener(this);
		mBankList.setPullLoadEnable(true);
		mBankList.setAdapter(mAdapter);

		mBankList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int i, long l) {
				Bank bank = (Bank) view.getTag(R.id.item_id);
				Intent intent = new Intent();
				intent.putExtra(SELECTED_BANK, bank);
				setResult(RESULT_OK, intent);
				finish();
			}
		});

		// loadData();
	}

	private void loadData() {

		API.getApplyBankList(this, page, keyword, pageSize, String
				.valueOf(mTerminalId), new HttpCallback<List<Bank>>(this) {
			@Override
			public void onSuccess(List<Bank> data) {
				mResultContainer.setVisibility(View.VISIBLE);
				mBanks.clear();
				if (null != data && data.size() > 0)
					mBanks.addAll(data);
				mAdapter.notifyDataSetChanged();
			}

			@Override
			public TypeToken<List<Bank>> getTypeToken() {
				return new TypeToken<List<Bank>>() {
				};
			}
		});
	}

	@Override
	public void onClick(View view) {
		keyword = mBankInput.getText().toString();
		mBanks.clear();
		loadData();
	}

	private class BankListAdapter extends BaseAdapter {
		private BankListAdapter() {
		}

		@Override
		public int getCount() {
			return mBanks.size();
		}

		@Override
		public Bank getItem(int i) {
			return mBanks.get(i);
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public View getView(int i, View convertView, ViewGroup viewGroup) {
			ViewHolder holder = new ViewHolder();
			if (null == convertView) {
				convertView = LayoutInflater.from(ApplyBankActivity.this)
						.inflate(R.layout.simple_list_item, null);
				holder.icon = (ImageView) convertView
						.findViewById(R.id.item_selected);
				holder.name = (TextView) convertView
						.findViewById(R.id.item_name);
				holder.id = (TextView) convertView.findViewById(R.id.item_id);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Bank bank = getItem(i);

			if (null != bank) {
				holder.id.setText(bank.getNo());
				holder.name.setText(bank.getName());
				if (null != mChosenBank
						&& bank.getNo().equals(mChosenBank.getNo())) {
					holder.icon.setVisibility(View.VISIBLE);
					holder.icon.setImageDrawable(getResources().getDrawable(
							R.drawable.icon_selected));
				} else {
					holder.icon.setVisibility(View.INVISIBLE);
				}
				convertView.setTag(R.id.item_id, bank);
			}

			return convertView;
		}
	}

	private static class ViewHolder {
		ImageView icon;
		TextView name;
		TextView id;
	}

	private void loadFinished() {
		mBankList.stopRefresh();
		mBankList.stopLoadMore();
		mBankList.setRefreshTime(Tools.getHourAndMin());
	}

	@Override
	public void onRefresh() {
		page = 0;
		pullType = "onRefresh";
		noMoreData = false;
		mBankList.setPullLoadEnable(true);
		loadData();
	}

	@Override
	public void onLoadMore() {
		pullType = "onLoadMore";
		if (noMoreData) {
			mBankList.stopLoadMore();
			mBankList.setPullLoadEnable(false);
			CommonUtil.toastShort(this,
					getResources().getString(R.string.no_more_data));
		} else {
			loadData();
		}

	}
}
