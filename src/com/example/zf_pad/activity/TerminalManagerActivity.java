package com.example.zf_pad.activity;

import static com.example.zf_pad.fragment.Constants.TerminalIntent.HAVE_VIDEO;
import static com.example.zf_pad.fragment.Constants.TerminalIntent.REQUEST_ADD;
import static com.example.zf_pad.fragment.Constants.TerminalIntent.TERMINAL_ID;
import static com.example.zf_pad.fragment.Constants.TerminalIntent.TERMINAL_STATUS;
import static com.example.zf_pad.fragment.Constants.TerminalStatus.CANCELED;
import static com.example.zf_pad.fragment.Constants.TerminalStatus.OPENED;
import static com.example.zf_pad.fragment.Constants.TerminalStatus.PART_OPENED;
import static com.example.zf_pad.fragment.Constants.TerminalStatus.STOPPED;
import static com.example.zf_pad.fragment.Constants.TerminalStatus.UNOPENED;
import static com.example.zf_pad.fragment.Constants.TerminalIntent.REQUEST_DETAIL;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.epalmpay.userPad.R;
import com.example.zf_pad.BaseActivity;
import com.example.zf_pad.MyApplication;
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

		// LinearLayout listHeader = (LinearLayout) mInflater.inflate(
		// R.layout.terminal_list_header, null);
		//
		// mTerminalList.addHeaderView(listHeader);

		// init the XListView
		mTerminalList.initHeaderAndFooter();
		mTerminalList.setXListViewListener(this);
		mTerminalList.setPullLoadEnable(true);
		mTerminalList.setDivider(null);
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
			if ((item.getPos() != null && !"".equals(item.getPos()))
					|| (item.getPosname() != null && !"".equals(item
							.getPosname()))) {
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
			if (item.getPayChannel() != null
					&& !"".equals(item.getPayChannel()))
				holder.tv_paychannel.setText(item.getPayChannel());
			else
				holder.tv_paychannel.setText("-");

			String[] status = getResources().getStringArray(
					R.array.terminal_status);
			holder.tv_openstate.setText(status[item.getOpenState()]);

			// add buttons according to status
			holder.llButtons.removeAllViews();
			if (!"2".equals(item.getType())) {

				Boolean appidBoolean = !"".equals(item.getAppid());
				Boolean videoBoolean = 1 == item.getHasVideoVerify();

				switch (item.getOpenState()) {
				case OPENED:
					holder.llButtonContainer.setVisibility(View.VISIBLE);

					addButton(holder.llButtons);
					addButton(holder.llButtons);
					if (appidBoolean) {
						if (videoBoolean) {

							addButton(holder.llButtons,
									R.string.terminal_button_sync, item,
									mSyncListener);
							addButton(holder.llButtons,
									R.string.terminal_button_video, item,
									mVideoListener);
						} else {

							addButton(holder.llButtons);
							addButton(holder.llButtons,
									R.string.terminal_button_sync, item,
									mSyncListener);
						}

					} else {

						addButton(holder.llButtons);
						if (videoBoolean) {
							addButton(holder.llButtons,
									R.string.terminal_button_video, item,
									mVideoListener);
						} else {

							addButton(holder.llButtons);
						}
					}

					// addButton(holder.llButtons, R.string.terminal_button_pos,
					// item, mPosListener);
					break;
				case PART_OPENED:
					holder.llButtonContainer.setVisibility(View.VISIBLE);
					
					addButton(holder.llButtons);
					if (appidBoolean) {
						if (videoBoolean) {
							addButton(holder.llButtons,
									R.string.terminal_button_sync, item,
									mSyncListener);
							addButton(holder.llButtons,
									R.string.terminal_button_video, item,
									mVideoListener);
						} else {

							addButton(holder.llButtons);
							addButton(holder.llButtons,
									R.string.terminal_button_sync, item,
									mSyncListener);
						}
					} else {

						addButton(holder.llButtons);
						if (videoBoolean) {
							addButton(holder.llButtons,
									R.string.terminal_button_video, item,
									mVideoListener);
						} else {

							addButton(holder.llButtons);
						}
					}

					addButton(holder.llButtons,
							R.string.terminal_button_reopen, item,
							mOpenListener);

					// addButton(holder.llButtons, R.string.terminal_button_pos,
					// item, mPosListener);
					break;
				case UNOPENED:
					holder.llButtonContainer.setVisibility(View.VISIBLE);

					addButton(holder.llButtons);
					if (appidBoolean) {

						if (videoBoolean) {
							addButton(holder.llButtons,
									R.string.terminal_button_sync, item,
									mSyncListener);
							addButton(holder.llButtons,
									R.string.terminal_button_reopen, item,
									mOpenListener);
							addButton(holder.llButtons,
									R.string.terminal_button_video, item,
									mVideoListener);
						} else {

							addButton(holder.llButtons);
							addButton(holder.llButtons,
									R.string.terminal_button_sync, item,
									mSyncListener);
							addButton(holder.llButtons,
									R.string.terminal_button_reopen, item,
									mOpenListener);
						}
					} else {

						addButton(holder.llButtons);
						if (videoBoolean) {
							addButton(holder.llButtons,
									R.string.terminal_button_open, item,
									mOpenListener);
							addButton(holder.llButtons,
									R.string.terminal_button_video, item,
									mVideoListener);
						} else {

							addButton(holder.llButtons);
							addButton(holder.llButtons,
									R.string.terminal_button_open, item,
									mOpenListener);
						}
					}
					break;
				case CANCELED:
					holder.llButtonContainer.setVisibility(View.VISIBLE);
					addButton(holder.llButtons);
					if (appidBoolean) {
						if (videoBoolean) {
							addButton(holder.llButtons,
									R.string.terminal_button_sync, item,
									mSyncListener);
							addButton(holder.llButtons,
									R.string.terminal_button_video, item,
									mVideoListener);
						} else {

							addButton(holder.llButtons);
							addButton(holder.llButtons,
									R.string.terminal_button_sync, item,
									mSyncListener);
						}
					} else {

						addButton(holder.llButtons);
						if (videoBoolean) {
							addButton(holder.llButtons,
									R.string.terminal_button_video, item,
									mVideoListener);
						} else {
							addButton(holder.llButtons);
						}
					}

					addButton(holder.llButtons,
							R.string.terminal_button_reopen, item,
							mOpenListener);
					break;
				case STOPPED:
					holder.llButtonContainer.setVisibility(View.VISIBLE);
					addButton(holder.llButtons);
					addButton(holder.llButtons);
					addButton(holder.llButtons);
					addButton(holder.llButtons);
					break;
				}
			} else {

				holder.llButtonContainer.setVisibility(View.VISIBLE);

				addButton(holder.llButtons);
				addButton(holder.llButtons);
				addButton(holder.llButtons);
				addButton(holder.llButtons);
			}
			convertView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if ("2".equals(item.getType())) {
						// 通过添加其他终端 进来的终端，是没有详情，也没有操作按钮
						Toast.makeText(TerminalManagerActivity.this,
								"自主开通终端无详情", Toast.LENGTH_SHORT).show();
						return;
					} else {
						Intent intent = new Intent(
								TerminalManagerActivity.this,
								TerminalManagerDetailActivity.class);

						intent.putExtra(HAVE_VIDEO, item.getHasVideoVerify());
						intent.putExtra(TERMINAL_ID, item.getId());
						intent.putExtra(TERMINAL_STATUS, item.getOpenState());

						startActivity(intent);
					}
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
				if (item.getOpenState() == UNOPENED
						&& "".equals(item.getAppid())) {

					openDialog(item);

				} else {
					if (item.getOpenstatus() != null
							&& !"".equals(item.getAppid())
							&& Integer.parseInt(item.getOpenstatus()) == 6) {
						CommonUtil.toastShort(TerminalManagerActivity.this,
								"正在第三方审核,请耐心等待...");

					} else {
						Intent intent = new Intent(
								TerminalManagerActivity.this,
								MyApplyDetail.class);
						intent.putExtra(TERMINAL_ID, item.getId());
						startActivityForResult(intent, REQUEST_DETAIL);
					}
				}

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
				// 添加视频审核
				TerminalManagerEntity item = (TerminalManagerEntity) view
						.getTag();
				if (item.getOpenState() == UNOPENED
						&& "".equals(item.getAppid())) {

					CommonUtil.toastShort(TerminalManagerActivity.this,
							"请先申请开通");

				} else {
					Intent intent = new Intent(TerminalManagerActivity.this,
							VideoActivity.class);
					intent.putExtra(TERMINAL_ID, item.getId());
					startActivity(intent);
				}
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
						loadFinished();
					}

					@Override
					public TypeToken<Page<TerminalManagerEntity>> getTypeToken() {
						return new TypeToken<Page<TerminalManagerEntity>>() {
						};
					}
				});
	}

	private void openDialog(final TerminalManagerEntity item) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(
				TerminalManagerActivity.this);

		LayoutInflater factory = LayoutInflater.from(this);
		View view = factory.inflate(R.layout.protocoldialog, null);
		builder.setView(view);

		final AlertDialog dialog = builder.create();
		dialog.show();
		final CheckBox cb = (CheckBox) view.findViewById(R.id.cb);

		Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

		Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);

		TextView tv_protocol = (TextView) view.findViewById(R.id.tv_protocol);

		tv_protocol.setText(item.getOpeningProtocol());

		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		btn_confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!cb.isChecked()) {

					CommonUtil.toastShort(TerminalManagerActivity.this,
							"请仔细阅读开通协议，并接受协议");

				} else {

					dialog.dismiss();
					Intent intent = new Intent(TerminalManagerActivity.this,
							MyApplyDetail.class);
					intent.putExtra(TERMINAL_ID, item.getId());
					startActivityForResult(intent, REQUEST_DETAIL);
				}

			}
		});
	}

	private void loadFinished() {
		mTerminalList.stopRefresh();
		mTerminalList.stopLoadMore();
		mTerminalList.setRefreshTime(Tools.getHourAndMin());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK)
			return;
		switch (requestCode) {
		case REQUEST_DETAIL: {
			onRefresh();
			break;
		}
		}
	}

	@Override
	public void onRefresh() {
		page = 0;
		mTerminalItems.clear();
		mTerminalList.setPullLoadEnable(true);
		loadData();
	}

	@Override
	public void onLoadMore() {
		if (mTerminalItems.size() >= total) {
			mTerminalList.setPullLoadEnable(false);
			mTerminalList.stopLoadMore();
			CommonUtil.toastShort(this,
					getResources().getString(R.string.no_more_data));
		} else {
			loadData();
		}
	}

}
