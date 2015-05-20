package com.example.zf_pad.trade;

import static com.example.zf_pad.fragment.Constants.TerminalIntent.REQUEST_DETAIL;
import static com.example.zf_pad.fragment.Constants.TerminalIntent.TERMINAL_ID;
import static com.example.zf_pad.fragment.Constants.TerminalIntent.TERMINAL_NUMBER;
import static com.example.zf_pad.fragment.Constants.TerminalIntent.TERMINAL_STATUS;
import static com.example.zf_pad.fragment.Constants.TerminalStatus.PART_OPENED;
import static com.example.zf_pad.fragment.Constants.TerminalStatus.UNOPENED;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zf_pad.MyApplication;
import com.example.zf_pad.R;
import com.example.zf_pad.entity.TerminalManagerEntity;
import com.example.zf_pad.trade.common.CommonUtil;
import com.example.zf_pad.trade.common.HttpCallback;
import com.example.zf_pad.util.TitleMenuUtil;
import com.example.zf_pad.util.Tools;
import com.example.zf_pad.util.XListView;
import com.example.zf_pad.video.VideoActivity;
import com.google.gson.reflect.TypeToken;

public class ApplyListActivity extends Activity implements
		XListView.IXListViewListener {

	private LayoutInflater mInflater;
	private XListView mApplyList;
	private List<TerminalManagerEntity> mTerminalItems;
	private ApplyListAdapter mAdapter;

	private int page = 0;
	private int total = 0;
	private final int rows = 10;
	private boolean noMoreData = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apply_list);
		new TitleMenuUtil(this, getString(R.string.title_apply_open)).show();
		initViews();
		loadData();
	}

	private void initViews() {
		mInflater = LayoutInflater.from(this);
		mApplyList = (XListView) findViewById(R.id.apply_list);
		mTerminalItems = new ArrayList<TerminalManagerEntity>();
		mAdapter = new ApplyListAdapter();

		LinearLayout listHeader = (LinearLayout) mInflater.inflate(
				R.layout.apply_list_header, null);

		mApplyList.addHeaderView(listHeader);
		// View header = new View(this);
		// mApplyList.addHeaderView(header);
		// init the XListView
		mApplyList.initHeaderAndFooter();
		mApplyList.setXListViewListener(this);
		mApplyList.setPullLoadEnable(true);

		mApplyList.setAdapter(mAdapter);
	}

	private void loadData() {
		API.getApplyList(this, MyApplication.NewUser.getId(), page + 1, rows,
				new HttpCallback<List<TerminalManagerEntity>>(this) {
					@Override
					public void onSuccess(List<TerminalManagerEntity> data) {
						if (null == data || data.size() <= 0)
							noMoreData = true;

						mTerminalItems.addAll(data);
						page++;
						mAdapter.notifyDataSetChanged();
					}

					@Override
					public void preLoad() {
					}

					@Override
					public void postLoad() {
						loadFinished();
					}

					@Override
					public TypeToken<List<TerminalManagerEntity>> getTypeToken() {
						return new TypeToken<List<TerminalManagerEntity>>() {
						};
					}
				});
	}

	class ApplyListAdapter extends BaseAdapter {
		ApplyListAdapter() {
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
				convertView = mInflater.inflate(R.layout.apply_list_item, null);
				holder = new ViewHolder();
				holder.tvTerminalNumber = (TextView) convertView
						.findViewById(R.id.apply_terminal_number);
				holder.tv_postype = (TextView) convertView
						.findViewById(R.id.apply_pos_number);
				holder.tvPayChannel = (TextView) convertView
						.findViewById(R.id.apply_pay_channel);
				holder.tvTerminalStatus = (TextView) convertView
						.findViewById(R.id.apply_terminal_status);
				holder.btnOpen = (Button) convertView
						.findViewById(R.id.apply_button_open);
				holder.btnVideo = (Button) convertView
						.findViewById(R.id.apply_button_video);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final TerminalManagerEntity item = getItem(i);
			holder.tvTerminalNumber.setText(item.getPosPortID());

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
			if (item.getPayChannel() == null) {

				holder.tvPayChannel.setText("-");
			} else {

				holder.tvPayChannel.setText(item.getPayChannel());
			}
			String[] status = getResources().getStringArray(
					R.array.terminal_status);
			holder.tvTerminalStatus.setText(status[item.getOpenState()]);

			if (item.getOpenState() == UNOPENED) {
				holder.btnOpen.setEnabled(true);
				holder.btnOpen.setText(getString(R.string.apply_button_open));
			} else if (item.getOpenState() == PART_OPENED) {
				holder.btnOpen.setEnabled(true);
				holder.btnOpen.setText(getString(R.string.apply_button_reopen));
			} else {
				holder.btnOpen.setEnabled(false);
			}
			holder.btnOpen.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if (item.getOpenState() == UNOPENED) {

						openDialog(item);

					} else {
						Intent intent = new Intent(ApplyListActivity.this,
								MyApplyDetail.class);
						intent.putExtra(TERMINAL_ID, item.getId());
						intent.putExtra(TERMINAL_NUMBER, item.getPosPortID());
						intent.putExtra(TERMINAL_STATUS, item.getOpenState());
						startActivityForResult(intent, REQUEST_DETAIL);
					}

				}

			});
			holder.btnVideo.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent = new Intent(ApplyListActivity.this,
							VideoActivity.class);
					intent.putExtra(TERMINAL_ID, item.getId());
					startActivity(intent);
				}
			});
			return convertView;
		}
	}

	static class ViewHolder {
		public TextView tvTerminalNumber;
		public TextView tvTerminalStatus;
		public TextView tv_postype;
		public TextView tvPayChannel;
		public Button btnOpen;
		public Button btnVideo;
	}

	private void openDialog(final TerminalManagerEntity item) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(
				ApplyListActivity.this);

		LayoutInflater factory = LayoutInflater.from(this);
		View view = factory.inflate(R.layout.protocoldialog, null);
		// builder.setTitle("自定义输入框");
		// builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
		// {
		// public void onClick(DialogInterface dialog, int whichButton) {
		// }
		// });
		// builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
		// {
		// public void onClick(DialogInterface dialog, int whichButton) {
		// }
		// });
		builder.setView(view);

		final AlertDialog dialog = builder.create();
		dialog.show();
		final CheckBox cb = (CheckBox) view.findViewById(R.id.cb);

		Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

		Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);

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

					CommonUtil.toastShort(ApplyListActivity.this,
							"请仔细阅读开通协议，并接受协议");

				} else {

					dialog.dismiss();
					Intent intent = new Intent(ApplyListActivity.this,
							MyApplyDetail.class);
					intent.putExtra(TERMINAL_ID, item.getId());
					intent.putExtra(TERMINAL_NUMBER, item.getPosPortID());
					intent.putExtra(TERMINAL_STATUS, item.getOpenState());
					startActivityForResult(intent, REQUEST_DETAIL);
				}

			}
		});
//		dialog.show();

	}

	@Override
	public void onRefresh() {
		page = 0;
		mTerminalItems.clear();
		loadData();
	}

	@Override
	public void onLoadMore() {
		if (noMoreData) {
			mApplyList.stopLoadMore();
			CommonUtil.toastShort(this, "no more data");
		} else {
			loadData();
		}
	}

	private void loadFinished() {
		mApplyList.stopRefresh();
		mApplyList.stopLoadMore();
		mApplyList.setRefreshTime(Tools.getHourAndMin());
	}
}
