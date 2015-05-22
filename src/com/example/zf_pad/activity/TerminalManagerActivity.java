package com.example.zf_pad.activity;

import static com.example.zf_pad.fragment.Constants.TerminalIntent.REQUEST_ADD;
import static com.example.zf_pad.fragment.Constants.TerminalIntent.TERMINAL_ID;
import static com.example.zf_pad.fragment.Constants.TerminalIntent.TERMINAL_NUMBER;
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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zf_pad.BaseActivity;
import com.example.zf_pad.MyApplication;
import com.example.zf_pad.R;
import com.example.zf_pad.entity.TerminalManagerEntity;
import com.example.zf_pad.trade.API;
import com.example.zf_pad.trade.MyApplyDetail;
import com.example.zf_pad.trade.common.CommonUtil;
import com.example.zf_pad.trade.common.HttpCallback;
import com.example.zf_pad.trade.common.Page;
import com.example.zf_pad.util.Tools;
import com.example.zf_pad.util.XListView;
import com.example.zf_pad.video.VideoActivity;
import com.google.gson.reflect.TypeToken;

public class TerminalManagerActivity extends BaseActivity implements
		XListView.IXListViewListener {

	private LinearLayout titleback_linear_back;
	private TextView titleback_text_title;
	private ImageView titleback_image_back, addTerminal;

	private LayoutInflater mInflater;
	private XListView mTerminalList;
	private List<TerminalManagerEntity> mTerminalItems;
	private TerminalListAdapter mAdapter;

	private int page = 0;
	private int total = 0;
	private final int rows = 10;

	private View.OnClickListener mSyncListener;
	private View.OnClickListener mOpenListener;
	private View.OnClickListener mPosListener;
	private View.OnClickListener mVideoListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_terminal_manage);

		initViews();
		initBtnListeners();
		loadData();

	}

	private void initViews() {
		mInflater = LayoutInflater.from(this);
		mTerminalList = (XListView) findViewById(R.id.terminal_list);
		titleback_linear_back = (LinearLayout) findViewById(R.id.titleback_linear_back);
		titleback_text_title = (TextView) findViewById(R.id.titleback_text_title);
		titleback_image_back = (ImageView) findViewById(R.id.titleback_image_back);
		addTerminal = (ImageView) findViewById(R.id.add);
		mTerminalItems = new ArrayList<TerminalManagerEntity>();
		mAdapter = new TerminalListAdapter();

		LinearLayout listHeader = (LinearLayout) mInflater.inflate(
				R.layout.terminal_list_header, null);

		mTerminalList.addHeaderView(listHeader);

		// init the XListView
		mTerminalList.initHeaderAndFooter();
		mTerminalList.setXListViewListener(this);
		mTerminalList.setPullLoadEnable(true);

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
				holder.llButtons = (LinearLayout) convertView
						.findViewById(R.id.terminal_buttons);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final TerminalManagerEntity item = getItem(i);
			holder.tv_terminal_id.setText(item.getPosPortID());
			if (item.getPos() != null || item.getPosname() != null) {
				if (item.getPos() == null) {
					holder.tv_postype.setText(item.getPosname());
				} else if (item.getPosname() == null) {
					holder.tv_postype.setText(item.getPos());
				} else {
					holder.tv_postype
							.setText(item.getPos() + item.getPosname());
				}
			} else {
				holder.tv_postype.setText("-");
			}
			holder.tv_paychannel.setText(item.getPayChannel());
			String[] status = getResources().getStringArray(
					R.array.terminal_status);
			holder.tv_openstate.setText(status[item.getOpenState()]);

			// add buttons according to status
			holder.llButtons.removeAllViews();
			switch (item.getOpenState()) {
			case OPENED:
				holder.llButtonContainer.setVisibility(View.VISIBLE);

				addButton(holder.llButtons);
				addButton(holder.llButtons);
				addButton(holder.llButtons, R.string.terminal_button_video,
						item, mVideoListener);
				addButton(holder.llButtons, R.string.terminal_button_pos, item,
						mPosListener);
				break;
			case PART_OPENED:
				holder.llButtonContainer.setVisibility(View.VISIBLE);
				addButton(holder.llButtons, R.string.terminal_button_sync,
						item, mSyncListener);
				addButton(holder.llButtons, R.string.terminal_button_reopen,
						item, mOpenListener);
				addButton(holder.llButtons, R.string.terminal_button_video,
						item, mVideoListener);
				addButton(holder.llButtons, R.string.terminal_button_pos, item,
						mPosListener);
				break;
			case UNOPENED:
				holder.llButtonContainer.setVisibility(View.VISIBLE);

				addButton(holder.llButtons);
				addButton(holder.llButtons, R.string.terminal_button_sync,
						item, mSyncListener);
				addButton(holder.llButtons, R.string.terminal_button_open,
						item, mOpenListener);
				addButton(holder.llButtons, R.string.terminal_button_video,
						item, mVideoListener);
				break;
			case CANCELED:
				holder.llButtonContainer.setVisibility(View.VISIBLE);

				addButton(holder.llButtons);
				addButton(holder.llButtons);
				addButton(holder.llButtons);
				addButton(holder.llButtons);
				break;
			case STOPPED:
				holder.llButtonContainer.setVisibility(View.VISIBLE);

				addButton(holder.llButtons);
				addButton(holder.llButtons);
				addButton(holder.llButtons);
				addButton(holder.llButtons, R.string.terminal_button_sync,
						item, mSyncListener);
				break;
			}
			convertView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent = new Intent(TerminalManagerActivity.this,
							TerminalManagerDetailActivity.class);

					// TODO:
					intent.putExtra(TERMINAL_ID, item.getId());
					intent.putExtra(TERMINAL_NUMBER, item.getPosPortID());
					intent.putExtra(TERMINAL_STATUS, item.getOpenState());

					startActivity(intent);
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
			button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			button.setTextColor(getResources().getColorStateList(
					R.color.mybutton));
			if (null != tag) {
				button.setTag(tag);
			}
			if (null != listener) {
				button.setOnClickListener(listener);
			}
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0,
					LayoutParams.MATCH_PARENT, 1);
			if (ll.getChildCount() > 0) {
				lp.setMargins(10, 0, 0, 0);
			}
			ll.addView(button, lp);
		}

		private void addButton(LinearLayout ll) {
			Button button = new Button(TerminalManagerActivity.this);
			button.setVisibility(View.INVISIBLE);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0,
					LayoutParams.MATCH_PARENT, 1);
			if (ll.getChildCount() > 0) {
				lp.setMargins(10, 0, 0, 0);
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
		public LinearLayout llButtons;
	}

	private void initBtnListeners() {

		titleback_linear_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TerminalManagerActivity.this.finish();
			}
		});

		titleback_image_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TerminalManagerActivity.this.finish();
			}
		});

		titleback_text_title
				.setText(getString(R.string.title_terminal_management));

		addTerminal.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivityForResult(new Intent(TerminalManagerActivity.this,
						TerminalAddActivity.class), REQUEST_ADD);
			}
		});
		mSyncListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				TerminalManagerEntity item = (TerminalManagerEntity) view
						.getTag();
				API.synchronous(TerminalManagerActivity.this, String
						.valueOf(item.getId()), new HttpCallback(
						TerminalManagerActivity.this) {

					@Override
					public void onSuccess(Object data) {
						CommonUtil.toastShort(TerminalManagerActivity.this,
								data.toString());
					}

					@Override
					public TypeToken getTypeToken() {
						return null;
					}
				});

			}
		};
		mOpenListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				TerminalManagerEntity item = (TerminalManagerEntity) view
						.getTag();
				Intent intent = new Intent(TerminalManagerActivity.this,
						MyApplyDetail.class);
				intent.putExtra(TERMINAL_ID, item.getId());
				intent.putExtra(TERMINAL_NUMBER, item.getPosPortID());
				intent.putExtra(TERMINAL_STATUS, item.getOpenState());
				startActivity(intent);
			}
		};
		mPosListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				TerminalManagerEntity item = (TerminalManagerEntity) view
						.getTag();
				API.findPosPassword(TerminalManagerActivity.this, item.getId(),
						new HttpCallback(TerminalManagerActivity.this) {
							@Override
							public void onSuccess(Object data) {
								final String password = data.toString();
								final AlertDialog.Builder builder = new AlertDialog.Builder(
										TerminalManagerActivity.this);
								builder.setMessage(password);
								builder.setPositiveButton(
										getString(R.string.button_copy),
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialogInterface,
													int i) {
												CommonUtil
														.copy(TerminalManagerActivity.this,
																password);
												dialogInterface.dismiss();
												CommonUtil
														.toastShort(
																TerminalManagerActivity.this,
																getString(R.string.toast_copy_password));
											}
										});
								builder.setNegativeButton(
										getString(R.string.button_cancel),
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialogInterface,
													int i) {
												dialogInterface.dismiss();
											}
										});
								builder.show();
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
				// ÃÌº” ”∆µ…Û∫À
				TerminalManagerEntity item = (TerminalManagerEntity) view
						.getTag();

				Intent intent = new Intent(TerminalManagerActivity.this,
						VideoActivity.class);
				intent.putExtra(TERMINAL_ID, item.getId());
				startActivity(intent);
			}
		};
	}

	private void loadData() {

		API.getTerminalApplyList(this, MyApplication.NewUser.getId(), page + 1,
				rows, new HttpCallback<Page<TerminalManagerEntity>>(this) {
					@Override
					public void onSuccess(Page<TerminalManagerEntity> data) {
						if (null != data.getList()) {
							mTerminalItems.addAll(data.getList());
						}
						total = data.getTotal();
						page++;
						mAdapter.notifyDataSetChanged();
					}

					@Override
					public void onFailure(String message) {

						super.onFailure(message);
					}

					@Override
					public void preLoad() {
					}

					@Override
					public void postLoad() {
						loadFinished();
					}

					@Override
					public TypeToken<Page<TerminalManagerEntity>> getTypeToken() {
						return new TypeToken<Page<TerminalManagerEntity>>() {
						};
					}
				});
	}

	private void loadFinished() {
		mTerminalList.stopRefresh();
		mTerminalList.stopLoadMore();
		mTerminalList.setRefreshTime(Tools.getHourAndMin());
	}

	@Override
	public void onRefresh() {
		page = 0;
		mTerminalItems.clear();
		loadData();
	}

	@Override
	public void onLoadMore() {
		if (mTerminalItems.size() >= total) {
			mTerminalList.stopLoadMore();
			CommonUtil.toastShort(this, "no more data");
		} else {
			loadData();
		}
	}

}
