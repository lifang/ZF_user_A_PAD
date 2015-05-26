package com.example.zf_pad.fragment;





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

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zf_pad.MyApplication;
import com.epalmpay.userPad.R;
import com.example.zf_pad.trade.API;
import com.example.zf_pad.trade.AfterSaleDetailActivity;
import com.example.zf_pad.trade.AfterSaleMarkActivity;
import com.example.zf_pad.trade.AfterSalePayActivity;
import com.example.zf_pad.trade.common.CommonUtil;
import com.example.zf_pad.trade.common.DialogUtil;
import com.example.zf_pad.trade.common.HttpCallback;
import com.example.zf_pad.trade.common.Pageable;
import com.example.zf_pad.trade.entity.AfterSaleRecord;
import com.example.zf_pad.trade.widget.MTabWidget;
import com.example.zf_pad.trade.widget.MTabWidget.OnTabOnclik;
import com.example.zf_pad.trade.widget.XListView;
import com.example.zf_pad.trade.widget.XListView.IXListViewListener;
import com.example.zf_pad.util.StringUtil;
import com.example.zf_pad.util.Tools;
import com.google.gson.reflect.TypeToken;

public class Mine_Shjl extends Fragment implements OnTabOnclik,IXListViewListener{
	private View view;
	Activity mActivity;
	int mRecordType=0;

	private XListView mListView;
	private RecordListAdapter mAdapter;
	private List<AfterSaleRecord> mEntities;
	private int currentPage = 1;
	private final int pageSize = 10;
	private int page = 0;
	private int total = 0;
	private final int rows = 10;
	private MTabWidget mTabWidget;
	private LayoutInflater mInflater;
   // private LinearLayout ll_shjl;
	// cancel apply button listener
	private View.OnClickListener mCancelApplyListener;
	// submit mark button listener
	private View.OnClickListener mSubmitMarkListener;
	// pay maintain button listener;
	private View.OnClickListener mPayMaintainListener;
	// submit cancel button listener
	private View.OnClickListener mSubmitCancelListener;
	private TextView tv1;
	private TextView tv2;
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
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//ll_shjl.setVisibility(View.VISIBLE);
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//ll_shjl.setVisibility(View.GONE);
	}
	private void init() {
		mInflater = LayoutInflater.from(getActivity());
		mEntities = new ArrayList<AfterSaleRecord>();
		mAdapter = new RecordListAdapter();
		// init the XListView

		mListView.setAdapter(mAdapter);
		initButtonListeners();
		loadData();
	}
	private void loadData() {
		API.getAfterSaleRecordList(mActivity, mRecordType, MyApplication.NewUser.getId(), page + 1, rows, new HttpCallback<Pageable<AfterSaleRecord>>(mActivity) {
			private Dialog loadingDialog;

			@Override
			public void onSuccess(Pageable<AfterSaleRecord> data) {
				if (null != data.getContent()) {
					mEntities.addAll(data.getContent());
				}
				if (data.getContent().size() == 0&&mEntities.size()!=0) {
					Toast.makeText(getActivity(), "没有更多数据",
							Toast.LENGTH_SHORT).show();
				
				}
				page++;
				total = data.getTotal();
				mAdapter.notifyDataSetChanged();
			}

			@Override
			public void preLoad() {
				loadingDialog = DialogUtil.getLoadingDialg(mActivity);
				loadingDialog.show();
			}

			@Override
			public void postLoad() {
				loadFinished();
				loadingDialog.dismiss();
			}

			@Override
			public TypeToken<Pageable<AfterSaleRecord>> getTypeToken() {
				return new TypeToken<Pageable<AfterSaleRecord>>() {
				};
			}
		});

	}
	private void loadFinished() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime(Tools.getHourAndMin());
	}
	private void initView() {
		//ll_shjl=(LinearLayout) view.findViewById(R.id.ll_shjl);
		 mTabWidget = (MTabWidget)view.findViewById(R.id.tab_widget);
		 // add tabs to the TabWidget
        String[] tabs = getResources().getStringArray(R.array.mine_shjl_tabs);
        for (int i = 0; i < tabs.length; i++) {
            mTabWidget.addTab(tabs[i]);
        }
        mTabWidget.updateTabs(0);
        mTabWidget.setonTabLintener(this);
        tv1 = (TextView)view.findViewById(R.id.tv1);
        tv2 = (TextView)view.findViewById(R.id.tv2);
        mListView = (XListView)view.findViewById(R.id.after_sale_list);
		mListView.initHeaderAndFooter();
		mListView.setXListViewListener(this);
		mListView.setPullLoadEnable(true);
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
			try {
				if (!StringUtil.isNull(data.getStatus())) {
					holder.tvStatus.setText(status[Integer.valueOf(data.getStatus())]);
				}
			} catch (Exception e) {
			}
			

			switch (mRecordType) {
				case MAINTAIN:
					if (data.getStatus().equals("1")) {
						holder.llButtonContainer.setVisibility(View.VISIBLE);
						holder.btnLeft.setVisibility(View.VISIBLE);
						holder.btnRight.setVisibility(View.VISIBLE);
						holder.btnCenter.setVisibility(View.GONE);
						holder.btnCenterBlank.setVisibility(View.GONE);

						holder.btnLeft.setText(getString(R.string.button_cancel_apply));
						holder.btnLeft.setTag(data);
						holder.btnLeft.setOnClickListener(mCancelApplyListener);

						holder.btnRight.setText(getString(R.string.button_pay));
						holder.btnRight.setTag(data);
						holder.btnRight.setOnClickListener(mPayMaintainListener);
					} else if (data.getStatus().equals("2")) {
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
					if (data.getStatus().equals("1")) {
						holder.llButtonContainer.setVisibility(View.VISIBLE);
						holder.btnLeft.setVisibility(View.GONE);
						holder.btnRight.setVisibility(View.GONE);
						holder.btnCenter.setVisibility(View.GONE);
						holder.btnCenterBlank.setVisibility(View.VISIBLE);

						holder.btnCenterBlank.setText(R.string.button_cancel_apply);
						holder.btnCenterBlank.setTag(data);
						holder.btnCenterBlank.setOnClickListener(mCancelApplyListener);
					} else if (data.getStatus().equals("5")) {
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
					if (data.getStatus().equals("1")) {
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
					if (data.getStatus().equals("1")) {
						holder.llButtonContainer.setVisibility(View.VISIBLE);
						holder.btnLeft.setVisibility(View.GONE);
						holder.btnRight.setVisibility(View.GONE);
						holder.btnCenter.setVisibility(View.GONE);
						holder.btnCenterBlank.setVisibility(View.VISIBLE);

						holder.btnCenterBlank.setText(R.string.button_cancel_apply);
						holder.btnCenterBlank.setTag(data);
						holder.btnCenterBlank.setOnClickListener(mCancelApplyListener);
					} else if (data.getStatus().equals("2")) {
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
						record.setStatus(5+"");
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
				final AfterSaleRecord record = (AfterSaleRecord) view.getTag();
				Intent i1 =new Intent (mActivity,AfterSalePayActivity.class);
				System.out.println("orderId：："+record.getId());
				i1.putExtra("orderId", record.getId()+"");
				i1.putExtra(RECORD_TYPE, mRecordType);
				startActivity(i1);	
			}
		};
 
		mSubmitCancelListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final AfterSaleRecord record = (AfterSaleRecord) v.getTag();
				API.resubmitCancel(mActivity, record.getId(), new HttpCallback(mActivity) {
					@Override
					public void onSuccess(Object obj) {
						record.setStatus(1+"");
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
								record.setStatus(status+"");
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
		switch (mRecordType) {
		case 0:
			tv1.setText("维修单号");
			tv2.setText("维修状态");
			break;
		case 1:
			tv1.setText("注销单号");
			tv2.setText("注销状态");
			break;
		case 2:
			tv1.setText("退货单号");
			tv2.setText("退货状态");
			break;
		case 3:
			tv1.setText("换货单号");
			tv2.setText("换货状态");
			break;
		case 4:
			tv1.setText("更新资料单号");
			tv2.setText("更新资料状态");
			break;
		case 5:
			tv1.setText("租赁单号");
			tv2.setText("租赁状态");
			break;

		default:
			break;
		}
		page = 0;
		if(mRecordType==1){
			mRecordType=2;	
		}else		
		if(mRecordType==2)
			mRecordType=1;
		init();
		
	}
	@Override
	public void onRefresh() {
		page = 0;
		mEntities.clear();
		loadData();
		
	}
	@Override
	public void onLoadMore() {
		if (mEntities.size() >= total) {
			mListView.stopLoadMore();
			CommonUtil.toastShort(mActivity, "没有更多数据");
		} else {
			loadData();
		}
		
	}
/*	@Override
	public void onResume() {
		super.onResume();
		page = 0;
		mEntities.clear();
		loadData();
	}*/
}
