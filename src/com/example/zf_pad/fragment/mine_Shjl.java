package com.example.zf_pad.fragment;





import java.util.ArrayList;
import java.util.List;



import static com.example.zf_pad.fragment.Constants.AfterSaleIntent.RECORD_ID;
import static com.example.zf_pad.fragment.Constants.AfterSaleIntent.RECORD_STATUS;
import static com.example.zf_pad.fragment.Constants.AfterSaleIntent.RECORD_TYPE;
import static com.example.zf_pad.fragment.Constants.AfterSaleIntent.REQUEST_DETAIL;
import static com.example.zf_pad.fragment.Constants.AfterSaleIntent.REQUEST_MARK;
import static com.example.zf_pad.fragment.Constants.AfterSaleType.CANCEL;
import static com.example.zf_pad.fragment.Constants.AfterSaleType.CHANGE;
import static com.example.zf_pad.fragment.Constants.AfterSaleType.LEASE;
import static com.example.zf_pad.fragment.Constants.AfterSaleType.MAINTAIN;
import static com.example.zf_pad.fragment.Constants.AfterSaleType.RETURN;
import static com.example.zf_pad.fragment.Constants.AfterSaleType.UPDATE;

import com.example.zf_pad.trade.AfterSaleMarkActivity;
import com.example.zf_pad.trade.AfterSalePayActivity;
import com.example.zf_pad.R;

import com.example.zf_pad.trade.API;
import com.example.zf_pad.trade.AfterSaleDetailActivity;

import com.example.zf_pad.trade.common.CommonUtil;
import com.example.zf_pad.trade.common.HttpCallback;
import com.example.zf_pad.trade.common.Page;
import com.example.zf_pad.trade.entity.AfterSaleRecord;
import com.example.zf_pad.trade.widget.MTabWidget;
import com.example.zf_pad.trade.widget.MTabWidget.OnTabOnclik;
import com.example.zf_pad.trade.widget.MyTabWidget;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class mine_Shjl extends Fragment implements OnTabOnclik{
	private View view;
	Activity mActivity;
	int mRecordType=0;

	private ListView mListView;
	private RecordListAdapter mAdapter;
	private List<AfterSaleRecord> mEntities;
	private int currentPage = 1;
	private int total = 0;
	private final int pageSize = 10;

	private LayoutInflater mInflater;

	// cancel apply button listener
	private View.OnClickListener mCancelApplyListener;
	// submit mark button listener
	private View.OnClickListener mSubmitMarkListener;
	// pay maintain button listener;
	private View.OnClickListener mPayMaintainListener;
	// submit cancel button listener
	private View.OnClickListener mSubmitCancelListener;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		/*if (view == null) {
			view = inflater.inflate(R.layout.f_mine_shjl, null);	
		}*/
		if (view != null) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null)
				 parent.removeView(view);
		}
		try {
			view = inflater.inflate(R.layout.f_mine_shjl, container, false);
			initView();
			init();
		} catch (InflateException e) {
		
		}
		return view;
	}
	private void init() {
		mInflater = LayoutInflater.from(getActivity());
		mEntities = new ArrayList<AfterSaleRecord>();
		mListView = (ListView)view.findViewById(R.id.after_sale_list);
		mAdapter = new RecordListAdapter();
		mListView.setAdapter(mAdapter);

		initButtonListeners();
		
		API.getAfterSaleRecordList(mActivity, mRecordType, 80, 1, 10, new HttpCallback<Page<AfterSaleRecord>>(mActivity) {
			@Override
			public void onSuccess(Page<AfterSaleRecord> data) {
				mEntities.addAll(data.getContent());
				currentPage = data.getCurrentPage();
				total = data.getTotal();		
				mAdapter.notifyDataSetChanged();
				
			}

			@Override
			public TypeToken<Page<AfterSaleRecord>> getTypeToken() {
				return new TypeToken<Page<AfterSaleRecord>>() {
				};
			}
		});
	
		
	}
	private void initView() {
		MTabWidget mTabWidget = (MTabWidget)view.findViewById(R.id.tab_widget);
		 // add tabs to the TabWidget
        String[] tabs = getResources().getStringArray(R.array.mine_shjl_tabs);
        for (int i = 0; i < tabs.length; i++) {
            mTabWidget.addTab(tabs[i]);
        }
        mTabWidget.updateTabs(0);
        mTabWidget.setonTabLintener(this);
        
	}
	class RecordListAdapter extends BaseAdapter {
		RecordListAdapter() {
		}

		@Override
		public int getCount() {
			return mEntities.size();
		}

		@Override
		public AfterSaleRecord getItem(int position) {
			return mEntities.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.f_mine_shjl_item, null);
				holder = new ViewHolder();
				
				holder.tvNumber = (TextView) convertView.findViewById(R.id.after_sale_number);
				holder.tvTime = (TextView) convertView.findViewById(R.id.after_sale_time);
				holder.tvTerminal = (TextView) convertView.findViewById(R.id.after_sale_terminal);
				holder.tvStatus = (TextView) convertView.findViewById(R.id.after_sale_status);
				holder.llButtonContainer = (LinearLayout) convertView.findViewById(R.id.after_sale_button_container);
				holder.btnLeft = (Button) convertView.findViewById(R.id.after_sale_button_left);
				holder.btnRight = (Button) convertView.findViewById(R.id.after_sale_button_right);
				holder.btnCenter = (Button) convertView.findViewById(R.id.after_sale_button_center);
				holder.btnCenterBlank = (Button) convertView.findViewById(R.id.after_sale_button_center_blank);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			final AfterSaleRecord data = getItem(position);
			String[] numberTitles = getResources().getStringArray(R.array.after_sale_number);
			
			holder.tvNumber.setText(data.getApplyNum());
			holder.tvTime.setText(data.getCreateTime());
			holder.tvTerminal.setText(data.getTerminalNum());
			String[] status = getResources().getStringArray(
					mRecordType == MAINTAIN ? R.array.maintain_status
							: mRecordType == RETURN ? R.array.return_status
							: mRecordType == CANCEL ? R.array.cancel_status
							: mRecordType == CHANGE ? R.array.change_status
							: mRecordType == UPDATE ? R.array.update_status
							: R.array.lease_status
			);
			holder.tvStatus.setText(status[data.getStatus()]);

			switch (mRecordType) {
				case MAINTAIN:
					if (data.getStatus() == 1) {
						holder.llButtonContainer.setVisibility(View.VISIBLE);
						holder.btnLeft.setVisibility(View.VISIBLE);
						holder.btnRight.setVisibility(View.VISIBLE);
						holder.btnCenter.setVisibility(View.GONE);
						holder.btnCenterBlank.setVisibility(View.GONE);

						holder.btnLeft.setText(getString(R.string.button_cancel_apply));
						holder.btnLeft.setTag(data);
						holder.btnLeft.setOnClickListener(mCancelApplyListener);

						holder.btnRight.setText(getString(R.string.button_pay));
						holder.btnRight.setOnClickListener(mPayMaintainListener);
					} else if (data.getStatus() == 2) {
						holder.llButtonContainer.setVisibility(View.VISIBLE);
						holder.btnLeft.setVisibility(View.GONE);
						holder.btnRight.setVisibility(View.GONE);
						holder.btnCenter.setVisibility(View.VISIBLE);
						holder.btnCenterBlank.setVisibility(View.GONE);

						holder.btnCenter.setText(getString(R.string.button_submit_flow));
						holder.btnCenter.setTag(data);
						holder.btnCenter.setOnClickListener(mSubmitMarkListener);
					} else {
						holder.llButtonContainer.setVisibility(View.GONE);
					}
					break;
				case CANCEL:
					if (data.getStatus() == 1) {
						holder.llButtonContainer.setVisibility(View.VISIBLE);
						holder.btnLeft.setVisibility(View.GONE);
						holder.btnRight.setVisibility(View.GONE);
						holder.btnCenter.setVisibility(View.GONE);
						holder.btnCenterBlank.setVisibility(View.VISIBLE);

						holder.btnCenterBlank.setText(R.string.button_cancel_apply);
						holder.btnCenterBlank.setTag(data);
						holder.btnCenterBlank.setOnClickListener(mCancelApplyListener);
					} else if (data.getStatus() == 5) {
						holder.llButtonContainer.setVisibility(View.VISIBLE);
						holder.btnLeft.setVisibility(View.GONE);
						holder.btnRight.setVisibility(View.GONE);
						holder.btnCenter.setVisibility(View.VISIBLE);
						holder.btnCenterBlank.setVisibility(View.GONE);

						holder.btnCenter.setText(R.string.button_submit_cancel);
						holder.btnCenter.setTag(data);
						holder.btnCenter.setOnClickListener(mSubmitCancelListener);
					} else {
						holder.llButtonContainer.setVisibility(View.GONE);
					}
					break;
				case UPDATE:
					if (data.getStatus() == 1) {
						holder.llButtonContainer.setVisibility(View.VISIBLE);
						holder.btnLeft.setVisibility(View.GONE);
						holder.btnRight.setVisibility(View.GONE);
						holder.btnCenter.setVisibility(View.GONE);
						holder.btnCenterBlank.setVisibility(View.VISIBLE);

						holder.btnCenterBlank.setText(getString(R.string.button_cancel_apply));
						holder.btnCenterBlank.setTag(data);
						holder.btnCenterBlank.setOnClickListener(mCancelApplyListener);
					} else {
						holder.llButtonContainer.setVisibility(View.GONE);
					}
					break;
				case RETURN:
				case CHANGE:
				case LEASE:
					if (data.getStatus() == 1) {
						holder.llButtonContainer.setVisibility(View.VISIBLE);
						holder.btnLeft.setVisibility(View.GONE);
						holder.btnRight.setVisibility(View.GONE);
						holder.btnCenter.setVisibility(View.GONE);
						holder.btnCenterBlank.setVisibility(View.VISIBLE);

						holder.btnCenterBlank.setText(R.string.button_cancel_apply);
						holder.btnCenterBlank.setTag(data);
						holder.btnCenterBlank.setOnClickListener(mCancelApplyListener);
					} else if (data.getStatus() == 2) {
						holder.llButtonContainer.setVisibility(View.VISIBLE);
						holder.btnLeft.setVisibility(View.GONE);
						holder.btnRight.setVisibility(View.GONE);
						holder.btnCenter.setVisibility(View.VISIBLE);
						holder.btnCenterBlank.setVisibility(View.GONE);

						holder.btnCenter.setText(R.string.button_submit_flow);
						holder.btnCenter.setTag(data);
						holder.btnCenter.setOnClickListener(mSubmitMarkListener);
					} else {
						holder.llButtonContainer.setVisibility(View.GONE);
					}
					break;
			}

			convertView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent = new Intent(mActivity, AfterSaleDetailActivity.class);
					intent.putExtra(RECORD_TYPE, mRecordType);
					intent.putExtra(RECORD_ID, data.getId());
					startActivityForResult(intent, REQUEST_DETAIL);
				}
			});

			return convertView;
		}
	}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.mActivity=activity;
	}
	private void initButtonListeners() {
		mCancelApplyListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final AfterSaleRecord record = (AfterSaleRecord) v.getTag();
				API.cancelAfterSaleApply(mActivity, mRecordType, record.getId(), new HttpCallback(mActivity) {
					@Override
					public void onSuccess(Object data) {
						record.setStatus(5);
						mAdapter.notifyDataSetChanged();
						CommonUtil.toastShort(mActivity, getString(R.string.toast_cancel_apply_success));
					}

					@Override
					public TypeToken getTypeToken() {
						return null;
					}
				});
			}
		};

		mSubmitMarkListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AfterSaleRecord record = (AfterSaleRecord) v.getTag();
				Intent intent = new Intent(mActivity, AfterSaleMarkActivity.class);
				intent.putExtra(RECORD_TYPE, mRecordType);
				intent.putExtra(RECORD_ID, record.getId());
				startActivityForResult(intent, REQUEST_MARK);
			}
		};

		mPayMaintainListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(mActivity, AfterSalePayActivity.class));
			}
		};

		mSubmitCancelListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final AfterSaleRecord record = (AfterSaleRecord) v.getTag();
				API.resubmitCancel(mActivity, record.getId(), new HttpCallback(mActivity) {
					@Override
					public void onSuccess(Object obj) {
						record.setStatus(1);
						mAdapter.notifyDataSetChanged();
						CommonUtil.toastShort(mActivity, getString(R.string.toast_resubmit_cancel_success));
					}

					@Override
					public TypeToken getTypeToken() {
						return null;
					}
				});
			}
		};
	}

	static class ViewHolder {
		
		public TextView tvNumber;
		public TextView tvTime;
		public TextView tvTerminal;
		public TextView tvStatus;
		public LinearLayout llButtonContainer;
		public Button btnLeft;
		public Button btnRight;
		public Button btnCenter;
		public Button btnCenterBlank;
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == mActivity.RESULT_OK) {
			switch (requestCode) {
				case REQUEST_DETAIL:
					int id = data.getIntExtra(RECORD_ID, 0);
					int status = data.getIntExtra(RECORD_STATUS, 0);
					if (id > 0 && status > 0) {
						for (AfterSaleRecord record : mEntities) {
							if (record.getId() == id) {
								record.setStatus(status);
								mAdapter.notifyDataSetChanged();
							}
						}
					}
					break;
				case REQUEST_MARK:
					CommonUtil.toastShort(mActivity, getString(R.string.toast_add_mark_success));
					break;
			}

		}
	}
	@Override
	public void chang(int index) {
		mRecordType=index;
		if(mRecordType==1){
			mRecordType=2;	
		}else		
		if(mRecordType==2)
			mRecordType=1;
		init();
		
	}
}
