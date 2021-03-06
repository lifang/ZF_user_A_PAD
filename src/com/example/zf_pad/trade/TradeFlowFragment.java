package com.example.zf_pad.trade;

import static com.example.zf_pad.fragment.Constants.TradeIntent.CLIENT_NUMBER;
import static com.example.zf_pad.fragment.Constants.TradeIntent.END_DATE;
import static com.example.zf_pad.fragment.Constants.TradeIntent.REQUEST_TRADE_CLIENT;
import static com.example.zf_pad.fragment.Constants.TradeIntent.START_DATE;
import static com.example.zf_pad.fragment.Constants.TradeIntent.TRADE_RECORD_ID;
import static com.example.zf_pad.fragment.Constants.TradeIntent.TRADE_TYPE;
import static com.example.zf_pad.fragment.Constants.TradeType.CONSUME;
import static com.example.zf_pad.fragment.Constants.TradeType.LIFE_PAY;
import static com.example.zf_pad.fragment.Constants.TradeType.PHONE_PAY;
import static com.example.zf_pad.fragment.Constants.TradeType.REPAYMENT;
import static com.example.zf_pad.fragment.Constants.TradeType.TRANSFER;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.LayoutParams;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.epalmpay.userPad.R;
import com.example.zf_pad.MyApplication;
import com.example.zf_pad.aadpter.SelectStateAdapter;
import com.example.zf_pad.trade.common.HttpCallback;
import com.example.zf_pad.trade.common.Page;
import com.example.zf_pad.trade.entity.TradeClient;
import com.example.zf_pad.trade.entity.TradeRecord;
import com.example.zf_pad.util.StringUtil;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;


public class TradeFlowFragment extends Fragment implements View.OnClickListener {

	private int mTradeType;

	private View mTradeClient;
	private TextView mTradeClientName;
	
	private TextView textView1,textView2,textView3,textView4,textView5,textView6;

	private View mTradeStart;
	private TextView mTradeStartDate;
	private View mTradeEnd;
	private TextView mTradeEndDate;

	private Button mTradeSearch;
	private Button mTradeStatistic;
	private LinearLayout mTradeSearchContent;

	private String tradeClientName;
	private String tradeStartDate;
	private String tradeEndDate;

	private LayoutInflater mInflater;
	private ListView mRecordList;
	private TradeRecordListAdapter mAdapter;
	private List<TradeRecord> mRecords;
	private boolean hasSearched = false;
	private String mPageName;
	private DecimalFormat df;

	//设置popupWindow弹出后背景的阴影
	private WindowManager.LayoutParams lp;
	private PopupWindow popuSelState;
	private SelectStateAdapter<TradeClient> selStateAdapter;
	private List<TradeClient> dataTradeClient = new ArrayList<TradeClient>();
	
	public static TradeFlowFragment newInstance(int tradeType) {
		TradeFlowFragment fragment = new TradeFlowFragment();
		Bundle args = new Bundle();
		args.putInt(TRADE_TYPE, tradeType);
		fragment.setArguments(args);
		return fragment;
	}

	public TradeFlowFragment() {
		
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mTradeType = getArguments().getInt(TRADE_TYPE);
            mPageName = String.format("tradeflow %d", mTradeType);
		}
		df = (DecimalFormat)NumberFormat.getInstance();
		df.applyPattern("0.00");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_trade_flow_list, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		initViews(view);
		getTerminalList();
		lp = getActivity().getWindow().getAttributes(); // 设置popupWindow弹出后背景的阴影
		// restore the saved state
		if (!TextUtils.isEmpty(tradeClientName)) {
			mTradeClientName.setText(tradeClientName);
		}
		if (!TextUtils.isEmpty(tradeStartDate)) {
			mTradeStartDate.setText(tradeStartDate);
		}
		if (!TextUtils.isEmpty(tradeEndDate)) {
			mTradeEndDate.setText(tradeEndDate);
		}
		if (hasSearched) {
			mTradeSearchContent.setVisibility(View.VISIBLE);
			mAdapter.notifyDataSetChanged();
		}
		toggleButtons();
		
		switch (mTradeType) {
		case TRANSFER:
			textView2.setText("手续费");
			textView3.setVisibility(View.GONE);
			break;
		case CONSUME:
			textView2.setText("付款账号");
			textView3.setText("收款账号");
			break;
		case REPAYMENT:
			textView2.setText("付款账号");
			textView3.setText("转入账号");
			break;
		case LIFE_PAY:
			textView2.setText("账户名");
			textView3.setText("账户号码");
			break;
		case PHONE_PAY:
			textView2.setText("手机号码");
			textView3.setVisibility(View.GONE);
			break;

		default:
			break;
		}
	}

	private void initViews(View view) {
		mInflater = LayoutInflater.from(getActivity());
		View header = mInflater.inflate(R.layout.fragment_trade_flow, null);
		mTradeClient = header.findViewById(R.id.trade_client);
		mTradeClientName = (TextView) header.findViewById(R.id.trade_client_name);

		mTradeStart = header.findViewById(R.id.trade_start);
		mTradeStartDate = (TextView) header.findViewById(R.id.trade_start_date);
		mTradeEnd = header.findViewById(R.id.trade_end);
		mTradeEndDate = (TextView) header.findViewById(R.id.trade_end_date);

		mTradeSearch = (Button) header.findViewById(R.id.trade_search);
		mTradeStatistic = (Button) header.findViewById(R.id.trade_statistic);
		mTradeSearchContent = (LinearLayout) header.findViewById(R.id.trade_search_content);

		textView1 = (TextView) header.findViewById(R.id.textView1);
		textView2 = (TextView) header.findViewById(R.id.textView2);
		textView3 = (TextView) header.findViewById(R.id.textView3);
		textView4 = (TextView) header.findViewById(R.id.textView4);
		textView5 = (TextView) header.findViewById(R.id.textView5);
		textView6 = (TextView) header.findViewById(R.id.textView6);
		
		mTradeClient.setOnClickListener(this);
		mTradeStart.setOnClickListener(this);
		mTradeEnd.setOnClickListener(this);
		mTradeSearch.setOnClickListener(this);
		mTradeStatistic.setOnClickListener(this);

		if (null == mRecords) {
			mRecords = new ArrayList<TradeRecord>();
		}

		mRecordList = (ListView) view.findViewById(R.id.trade_record_list);
		mAdapter = new TradeRecordListAdapter();
        mRecordList.addHeaderView(header);
		mRecordList.setAdapter(mAdapter);

		mRecordList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				TradeRecord record = (TradeRecord) view.getTag(R.id.trade_status);
				Intent intent = new Intent(getActivity(), TradeDetailActivity.class);
				intent.putExtra(TRADE_RECORD_ID, record.getId());
				 intent.putExtra(TRADE_TYPE, mTradeType);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK) return;
		switch (requestCode) {
			case REQUEST_TRADE_CLIENT:
				String clientName = data.getStringExtra(CLIENT_NUMBER);
				mTradeClientName.setText(clientName);
				tradeClientName = clientName;
				toggleButtons();
				break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.trade_client:
				popSelectTradeClient();
//				Intent i = new Intent(getActivity(), TradeClientActivity.class);
//				i.putExtra(CLIENT_NUMBER, tradeClientName);
//				startActivityForResult(i, REQUEST_TRADE_CLIENT);
				break;
			case R.id.trade_start:
				showDatePicker(tradeStartDate, true);
				break;
			case R.id.trade_end:
				showDatePicker(tradeEndDate, false);
				break;
			case R.id.trade_search:
				hasSearched = true;
				mTradeSearchContent.setVisibility(View.VISIBLE);
				API.getTradeRecordList(getActivity(),
						mTradeType, tradeClientName, tradeStartDate, tradeEndDate, 1, 100,
						new HttpCallback<Page<TradeRecord>>(getActivity()) {

							@Override
							public void onSuccess(Page<TradeRecord> data) {
						        mRecords.clear();
                                mRecords.addAll(data.getList());
                                mAdapter.notifyDataSetChanged();
							}


                            @Override
                            public TypeToken<Page<TradeRecord>> getTypeToken() {
                                return new TypeToken<Page<TradeRecord>>() {
                                };
                            }
						});
				break;
			case R.id.trade_statistic:
				Intent intent = new Intent(getActivity(), TradeStatisticActivity.class);
				intent.putExtra(TRADE_TYPE, mTradeType);
				intent.putExtra(CLIENT_NUMBER, tradeClientName);
				intent.putExtra(START_DATE, tradeStartDate);
				intent.putExtra(END_DATE, tradeEndDate);
				startActivity(intent);
				break;
		}
	}
	private void popSelectTradeClient() {
		View selectView = LayoutInflater.from(getActivity()).inflate(
				R.layout.pop_tradeflow_serial, null);
		ListView listViewState = (ListView) selectView.findViewById(R.id.list_one);
		selStateAdapter = new SelectStateAdapter<TradeClient>(getActivity(), dataTradeClient);
		listViewState.setAdapter(selStateAdapter);
		listViewState.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				mTradeClientName.setText(dataTradeClient.get(position).getSerialNum());
				tradeClientName = dataTradeClient.get(position).getSerialNum();
				toggleButtons();
				popuSelState.dismiss();
			}
		});

		popuSelState = new PopupWindow(selectView);
		popuSelState.setWidth(mTradeClient.getWidth());
		popuSelState.setHeight(LayoutParams.WRAP_CONTENT);
		popuSelState.setOutsideTouchable(true);
		popuSelState.setFocusable(true);
		lp.alpha = 0.4f;
		getActivity().getWindow().setAttributes(lp);
		popuSelState.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				lp.alpha = 1.0f;
				getActivity().getWindow().setAttributes(lp);
			}
		});
		popuSelState.setBackgroundDrawable(new ColorDrawable());
		popuSelState.showAsDropDown(mTradeClient);
	
	}
	private void getTerminalList() {
	    API.getTerminalList(getActivity(), MyApplication.NewUser.getId(),
	    		new HttpCallback<List<TradeClient>>(getActivity()) {

				@Override
				public void onSuccess(List<TradeClient> data) {
					if (null != data) {
						dataTradeClient = data;
					}
					
				}

				@Override
				public TypeToken<List<TradeClient>> getTypeToken() {
					return new TypeToken<List<TradeClient>>() {
					};
				}
			});
	}
	/**
	 * enable or disable the buttons
	 *
	 * @param
	 * @return
	 */
	private void toggleButtons() {
	
		boolean shouldEnable = !TextUtils.isEmpty(tradeClientName)
				&& !TextUtils.isEmpty(tradeStartDate)
				&& !TextUtils.isEmpty(tradeEndDate);
		mTradeSearch.setEnabled(shouldEnable);
		mTradeStatistic.setEnabled(shouldEnable);
	}

	/**
	 * show the date picker
	 *
	 * @param date        the chosen date
	 * @param isStartDate if true to choose the start date, else the end date
	 * @return
	 */
	private void showDatePicker(final String date, final boolean isStartDate) {

		final Calendar c = Calendar.getInstance();
		if (TextUtils.isEmpty(date)) {
			c.setTime(new Date());
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				c.setTime(sdf.parse(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		new DialogFragment() {
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				int year = c.get(Calendar.YEAR);
				int month = c.get(Calendar.MONTH);
				int day = c.get(Calendar.DAY_OF_MONTH);
				return new DatePickerDialog(getActivity(),
						new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker datePicker,
							                      int year, int month, int day) {
								month = month + 1;
								String dateStr = year + "-"
										+ (month < 10 ? "0" + month : month) + "-"
										+ (day < 10 ? "0" + day : day);
								if (isStartDate) {
									mTradeStartDate.setText(dateStr);
									tradeStartDate = dateStr;
								} else {
									if (!TextUtils.isEmpty(tradeStartDate) && dateStr.compareTo(tradeStartDate) < 0) {
										Toast.makeText(getActivity(), getString(R.string.toast_end_date_error), Toast.LENGTH_SHORT).show();
										return;
									}
									mTradeEndDate.setText(dateStr);
									tradeEndDate = dateStr;
								}
								toggleButtons();
							}
						}, year, month, day);
			}
		}.show(getActivity().getSupportFragmentManager(), "DatePicker");
	}

	class TradeRecordListAdapter extends BaseAdapter {
		TradeRecordListAdapter() {
		}

		@Override
		public int getCount() {
			return mRecords.size();
		}

		@Override
		public TradeRecord getItem(int i) {
			return mRecords.get(i);
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public View getView(int i, View convertView, ViewGroup viewGroup) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.trade_flow_item, null);
				holder = new ViewHolder();
				holder.tvStatus = (TextView) convertView.findViewById(R.id.trade_status);
				holder.tvTime = (TextView) convertView.findViewById(R.id.trade_time);
				holder.tvAccount = (TextView) convertView.findViewById(R.id.trade_account);
				holder.tvReceiveAccount = (TextView) convertView.findViewById(R.id.trade_receive_account);
				holder.tvClientNumber = (TextView) convertView.findViewById(R.id.trade_client_number);
				holder.tvAmount = (TextView) convertView.findViewById(R.id.trade_amount);
				// this 2 text values are according to the trade type
				//holder.tvAccountKey = (TextView) convertView.findViewById(R.id.trade_account_key);
				//holder.tvReceiveAccountKey = (TextView) convertView.findViewById(R.id.trade_receive_account_key);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final TradeRecord record = getItem(i);
			convertView.setTag(R.id.trade_status, record);
			switch (mTradeType) {	
			case TRANSFER:
				holder.tvAccount.setText(getString(R.string.notation_yuan) + df.format(record.getPoundage()*1.0f/100));
				holder.tvReceiveAccount.setVisibility(View.GONE);
				break;
			case REPAYMENT:
			case CONSUME:
				holder.tvAccount.setText(StringUtil.replaceNum(record.getPayFromAccount()));
				holder.tvReceiveAccount.setText(StringUtil.replaceNum(record.getPayIntoAccount()));
				break;
			case LIFE_PAY:
				holder.tvAccount.setText(StringUtil.replaceName(record.getAccountName()));
				holder.tvReceiveAccount.setText(StringUtil.replaceNum(record.getAccountNumber()));
				break;
			case PHONE_PAY:
				holder.tvAccount.setVisibility(View.GONE);
				holder.tvReceiveAccount.setText(StringUtil.replaceNum(record.getPhone()));
				break;
				}

			holder.tvStatus.setText(getResources().getStringArray(R.array.trade_status)[record.getTradedStatus()]);
			holder.tvTime.setText(record.getTradedTimeStr());
			holder.tvClientNumber.setText(record.getTerminalNumber());
			holder.tvAmount.setText(getString(R.string.notation_yuan) + df.format(record.getAmount()*1.0f/100));

			return convertView;
		}
	}

	static class ViewHolder {
		public TextView tvStatus;
		public TextView tvTime;
		public TextView tvAccountKey;
		public TextView tvReceiveAccountKey;
		public TextView tvAccount;
		public TextView tvReceiveAccount;
		public TextView tvClientNumber;
		public TextView tvAmount;
	}
	
	
	@Override
	public void onPause() {
  			// TODO Auto-generated method stub
  			super.onPause();
  			MobclickAgent.onPageEnd( mPageName );
  		}
    
    @Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart( mPageName );	
	}
}

