package com.example.zf_pad.activity;

import static com.example.zf_pad.fragment.Constants.TerminalIntent.REQUEST_DETAIL;
import static com.example.zf_pad.fragment.Constants.TerminalIntent.TERMINAL_ID;
import static com.example.zf_pad.fragment.Constants.TerminalIntent.TERMINAL_STATUS;
import static com.example.zf_pad.fragment.Constants.TerminalStatus.CANCELED;
import static com.example.zf_pad.fragment.Constants.TerminalStatus.OPENED;
import static com.example.zf_pad.fragment.Constants.TerminalStatus.PART_OPENED;
import static com.example.zf_pad.fragment.Constants.TerminalStatus.STOPPED;
import static com.example.zf_pad.fragment.Constants.TerminalStatus.UNOPENED;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zf_pad.R;
import com.example.zf_pad.entity.TerminalManagerEntity;
import com.example.zf_pad.trade.API;
import com.example.zf_pad.trade.common.CommonUtil;
import com.example.zf_pad.trade.common.HttpCallback;
import com.example.zf_pad.util.TitleMenuUtil;
import com.google.gson.reflect.TypeToken;

;
public class TerminalManagerActivity extends Activity {

	// private LayoutInflater layoutInflater;
	// private ListView listview;
	// private List<TerminalManagerEntity> listData;

	private LayoutInflater mInflater;
	private ListView mTerminalList;
	private List<TerminalManagerEntity> mTerminalItems;
	private TerminalListAdapter mAdapter;

	private View.OnClickListener mSyncListener;
	private View.OnClickListener mOpenListener;
	private View.OnClickListener mPosListener;
	private View.OnClickListener mVideoListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_terminal_manage);
		new TitleMenuUtil(this, getString(R.string.title_terminal_management))
				.show();
		// DEBUG
		// initView();
		initViews();
		initBtnListeners();
		loadData();

	}

	// private void initView() {
	//
	// layoutInflater = LayoutInflater.from(this);
	//
	// listview = (ListView) findViewById(R.id.terminal_list);
	//
	// listData = new ArrayList<TerminalManagerEntity>();
	//
	// // debug data
	// for (int i = 0; i < 4; i++) {
	//
	// TerminalManagerEntity dto = new TerminalManagerEntity();
	//
	// dto.setPosPortID("1234567890000001");
	// dto.setPos("泰山TS900");
	// dto.setPayChannel("快钱宝");
	// dto.setOpenState("部分开通");
	//
	// listData.add(dto);
	// }
	//
	// LinearLayout headerview = (LinearLayout) layoutInflater.inflate(
	// R.layout.terminal_list_header, null);
	//
	// listview.addHeaderView(headerview);
	//
	// listview.setAdapter(new TerminalManagerAdapter(
	// TerminalManagerActivity.this, listData));
	//
	// listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	//
	// @Override
	// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
	// long arg3) {
	//
	// arg0.getTag();
	//
	// Intent it = new Intent(TerminalManagerActivity.this,
	// TerminalManagerDetailActivity.class);
	//
	// startActivity(it);
	// }
	// });
	// }

	private void initViews() {
		mInflater = LayoutInflater.from(this);
		mTerminalList = (ListView) findViewById(R.id.terminal_list);
		mTerminalItems = new ArrayList<TerminalManagerEntity>();
		mAdapter = new TerminalListAdapter();

		LinearLayout listHeader = (LinearLayout) mInflater.inflate(
				R.layout.terminal_list_header, null);

		mTerminalList.addHeaderView(listHeader);
		mTerminalList.setAdapter(mAdapter);
	}

	class TerminalListAdapter extends BaseAdapter {
		TerminalListAdapter() {
		}

		@Override
		public int getCount() {
			return mTerminalItems.size();
		}

		@Override
		public TerminalManagerEntity getItem(int i) {
			return mTerminalItems.get(i);
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public View getView(int i, View convertView, ViewGroup viewGroup) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.terminal_list, null);
				holder = new ViewHolder();
				holder.tv_terminal_id = (TextView) convertView
						.findViewById(R.id.tv_terminal_id);
				holder.tv_postype = (TextView) convertView
						.findViewById(R.id.tv_postype);
				holder.tv_paychannel = (TextView) convertView
						.findViewById(R.id.tv_paychannel);
				holder.tv_openstate = (TextView) convertView
						.findViewById(R.id.tv_openstate);
				holder.llButtonContainer = (LinearLayout) convertView
						.findViewById(R.id.terminal_button_container);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			final TerminalManagerEntity item = getItem(i);
			holder.tv_terminal_id.setText(item.getPosPortID());
			holder.tv_postype.setText(item.getPos());
			holder.tv_paychannel.setText(item.getPayChannel());
			String[] status = getResources().getStringArray(
					R.array.terminal_status);
			holder.tv_openstate.setText(status[item.getOpenState()]);

			// add buttons according to status
			holder.llButtonContainer.removeAllViews();
			switch (item.getOpenState()) {
			case OPENED:
				holder.llButtonContainer.setVisibility(View.VISIBLE);
				addButton(holder.llButtonContainer,
						R.string.terminal_button_video, item, mVideoListener);
				addButton(holder.llButtonContainer,
						R.string.terminal_button_pos, item, mPosListener);
				break;
			case PART_OPENED:
				holder.llButtonContainer.setVisibility(View.VISIBLE);
				addButton(holder.llButtonContainer,
						R.string.terminal_button_sync, item, mSyncListener);
				addButton(holder.llButtonContainer,
						R.string.terminal_button_reopen, item, mOpenListener);
				addButton(holder.llButtonContainer,
						R.string.terminal_button_video, item, mVideoListener);
				addButton(holder.llButtonContainer,
						R.string.terminal_button_pos, item, mPosListener);
				break;
			case UNOPENED:
				holder.llButtonContainer.setVisibility(View.VISIBLE);
				addButton(holder.llButtonContainer,
						R.string.terminal_button_open, item, mOpenListener);
				addButton(holder.llButtonContainer,
						R.string.terminal_button_video, item, mVideoListener);
				break;
			case CANCELED:
				holder.llButtonContainer.setVisibility(View.GONE);
				break;
			case STOPPED:
				holder.llButtonContainer.setVisibility(View.VISIBLE);
				addButton(holder.llButtonContainer,
						R.string.terminal_button_sync, item, mSyncListener);
				break;
			}
			convertView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent = new Intent(TerminalManagerActivity.this,
							TerminalManagerDetailActivity.class);

					// TODO:
					intent.putExtra(TERMINAL_ID, item.getPosPortID());
					intent.putExtra(TERMINAL_STATUS, item.getOpenState());
					startActivityForResult(intent, REQUEST_DETAIL);
				}
			});
			return convertView;
		}

		private void addButton(LinearLayout ll, int res, Object tag,
				View.OnClickListener listener) {
			Button button = new Button(TerminalManagerActivity.this);
			button.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.shape_o));
			button.setText(res);
			button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
			button.setTextColor(getResources().getColorStateList(
					Color.parseColor("#FD8936")));
			if (null != tag) {
				button.setTag(tag);
			}
			if (null != listener) {
				button.setOnClickListener(listener);
			}
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 1);
			if (ll.getChildCount() > 0) {
				lp.setMargins(0, 0, 5, 0);
			}
			ll.addView(button, lp);
		}
	}

	static class ViewHolder {
		public TextView tv_terminal_id;
		public TextView tv_postype;
		public TextView tv_paychannel;
		public TextView tv_openstate;
		public LinearLayout llButtonContainer;
	}

	private void initBtnListeners() {
		mSyncListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				CommonUtil.toastShort(TerminalManagerActivity.this,
						"synchronising...");
			}
		};
		mOpenListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				TerminalManagerEntity item = (TerminalManagerEntity) view
						.getTag();
				// TODO:
				// Intent intent = new Intent(TerminalManagerActivity.this,
				// ApplyDetailActivity.class);
				// intent.putExtra(TERMINAL_ID, item.getId());
				// intent.putExtra(TERMINAL_STATUS, item.getStatus());
				// startActivity(intent);
			}
		};
		mPosListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				TerminalManagerEntity item = (TerminalManagerEntity) view
						.getTag();
				API.findPosPassword(TerminalManagerActivity.this, item
						.getPosPortID(), new HttpCallback(
						TerminalManagerActivity.this) {
					@Override
					public void onSuccess(Object data) {

					}

					@Override
					public TypeToken getTypeToken() {
						return null;
					}
				});
			}
		};
		mVideoListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				CommonUtil.toastShort(TerminalManagerActivity.this,
						"not yet completed...");
			}
		};
	}

	private void loadData() {

		API.getTerminalApplyList(this, 80, 1, 10,
				new HttpCallback<List<TerminalManagerEntity>>(this) {
					@Override
					public void onSuccess(List<TerminalManagerEntity> data) {
						mTerminalItems.addAll(data);
						mAdapter.notifyDataSetChanged();
					}

					@Override
					public TypeToken<List<TerminalManagerEntity>> getTypeToken() {
						return new TypeToken<List<TerminalManagerEntity>>() {
						};
					}
				});
	}

}
