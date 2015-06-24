package com.example.zf_pad.activity;

import static com.example.zf_pad.fragment.Constants.ApplyIntent.SELECTED_ID;
import static com.example.zf_pad.fragment.Constants.TerminalIntent.HAVE_VIDEO;
import static com.example.zf_pad.fragment.Constants.TerminalIntent.REQUEST_DETAIL;
import static com.example.zf_pad.fragment.Constants.TerminalIntent.TERMINAL_ID;
import static com.example.zf_pad.fragment.Constants.TerminalIntent.TERMINAL_STATUS;
import static com.example.zf_pad.fragment.Constants.TerminalStatus.CANCELED;
import static com.example.zf_pad.fragment.Constants.TerminalStatus.OPENED;
import static com.example.zf_pad.fragment.Constants.TerminalStatus.PART_OPENED;
import static com.example.zf_pad.fragment.Constants.TerminalStatus.STOPPED;
import static com.example.zf_pad.fragment.Constants.TerminalStatus.UNOPENED;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zf_pad.BaseActivity;
import com.example.zf_pad.MyApplication;
import com.epalmpay.userPad.R;
import com.example.zf_pad.entity.TerminalApply;
import com.example.zf_pad.entity.TerminalComment;
import com.example.zf_pad.entity.TerminalDetail;
import com.example.zf_pad.entity.TerminalManagerEntity;
import com.example.zf_pad.entity.TerminalOpen;
import com.example.zf_pad.entity.TerminalRate;
import com.example.zf_pad.trade.API;
import com.example.zf_pad.trade.ApplyListActivity;
import com.example.zf_pad.trade.MyApplyDetail;
import com.example.zf_pad.trade.common.CommonUtil;
import com.example.zf_pad.trade.common.HttpCallback;
import com.example.zf_pad.util.TitleMenuUtil;
import com.example.zf_pad.video.VideoActivity;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

@SuppressLint("NewApi")
public class TerminalManagerDetailActivity extends BaseActivity {

	private int mTerminalStatus;
	private int mTerminalId;

	private LayoutInflater mInflater;
	private TextView mStatus;
	private Button mBtnLeftTop;
	private Button mBtnLeftBottom;
	private Button mBtnRightTop;
	private Button mBtnRightBottom;
	private LinearLayout mCategoryContainer;
	private LinearLayout mCommentContainer;
	private LinearLayout mTerminalContainer;

	private View.OnClickListener mSyncListener;
	private View.OnClickListener mOpenListener;
	private View.OnClickListener mReOpenListener;
	private View.OnClickListener mPosListener;
	private View.OnClickListener mVideoListener;

	private int isVideo, status;
	private Boolean appidBoolean, videoBoolean;
	private String xieyiString;

	DisplayImageOptions options = MyApplication.getDisplayOption();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		isVideo = getIntent().getIntExtra(HAVE_VIDEO, 0);
		mTerminalId = getIntent().getIntExtra(TERMINAL_ID, 0);
		mTerminalStatus = getIntent().getIntExtra(TERMINAL_STATUS, 0);
		xieyiString = getIntent().getStringExtra("xieyi");

		setContentView(R.layout.activity_terminal_detail);
		new TitleMenuUtil(this,
				getString(R.string.title_terminal_detail_management)).show();

		initViews();
		initBtnListeners();
		loadData();
	}

	private void initViews() {
		mInflater = LayoutInflater.from(this);
		mStatus = (TextView) findViewById(R.id.terminal_detail_status);
		mBtnLeftTop = (Button) findViewById(R.id.terminal_button_left_top);
		mBtnLeftBottom = (Button) findViewById(R.id.terminal_button_left_bottom);
		mBtnRightTop = (Button) findViewById(R.id.terminal_button_right_top);
		mBtnRightBottom = (Button) findViewById(R.id.terminal_button_right_bottom);
		mCategoryContainer = (LinearLayout) findViewById(R.id.terminal_category_container);
		mCommentContainer = (LinearLayout) findViewById(R.id.terminal_comment_container);
		mTerminalContainer = (LinearLayout) findViewById(R.id.terminal_category);
	}

	private void initBtnListeners() {
		mSyncListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				API.synchronous(TerminalManagerDetailActivity.this, String
						.valueOf(mTerminalId), new HttpCallback(
						TerminalManagerDetailActivity.this) {

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
		mOpenListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				openDialog();

			}
		};
		mReOpenListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(TerminalManagerDetailActivity.this,
						MyApplyDetail.class);
				intent.putExtra(TERMINAL_ID, mTerminalId);
				startActivityForResult(intent, REQUEST_DETAIL);
			}
		};

		mPosListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				API.findPosPassword(TerminalManagerDetailActivity.this,
						mTerminalId, new HttpCallback(
								TerminalManagerDetailActivity.this) {
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
				if (status == UNOPENED && !appidBoolean) {

					CommonUtil.toastShort(TerminalManagerDetailActivity.this,
							"请先申请开通");

				} else {
					Intent intent = new Intent(
							TerminalManagerDetailActivity.this,
							VideoActivity.class);
					intent.putExtra(TERMINAL_ID, mTerminalId);
					startActivity(intent);
				}
			}
		};
	}

	private void openDialog() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(
				TerminalManagerDetailActivity.this);

		LayoutInflater factory = LayoutInflater.from(this);
		View view = factory.inflate(R.layout.protocoldialog, null);

		builder.setView(view);

		final AlertDialog dialog = builder.create();
		dialog.show();
		final CheckBox cb = (CheckBox) view.findViewById(R.id.cb);

		Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

		Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);

		TextView tv_protocol = (TextView) view.findViewById(R.id.tv_protocol);

		tv_protocol.setText(xieyiString);

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

					CommonUtil.toastShort(TerminalManagerDetailActivity.this,
							"请仔细阅读开通协议，并接受协议");

				} else {

					dialog.dismiss();
					Intent intent = new Intent(
							TerminalManagerDetailActivity.this,
							MyApplyDetail.class);
					intent.putExtra(TERMINAL_ID, mTerminalId);
					startActivityForResult(intent, REQUEST_DETAIL);
				}

			}
		});
		// dialog.show();

	}

	private void loadData() {
		API.getTerminalDetail(this, mTerminalId, MyApplication.NewUser.getId(),
				new HttpCallback<TerminalDetail>(this) {
					@Override
					public void onSuccess(TerminalDetail data) {
						TerminalApply apply = data.getApplyDetails();
						List<TerminalComment> comments = data.getTrackRecord();
						List<TerminalRate> rates = data.getRates();
						List<TerminalOpen> openDetails = data
								.getOpeningDetails();

						// set the status and buttons
						setStatusAndButtons(apply);
						// render terminal info
						LinearLayout terminalCategory = setTerminalInfo(apply);
						// add rate's table
						addRatesTable(terminalCategory, rates);
						// render terminal open info
						setOpenInfo(openDetails);
						// add terminal trace records
						addComments(comments);
					}

					@Override
					public void onFailure(String message) {

						super.onFailure(message);
					}

					@Override
					public TypeToken<TerminalDetail> getTypeToken() {
						return new TypeToken<TerminalDetail>() {
						};
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK)
			return;
		switch (requestCode) {
		case REQUEST_DETAIL: {
			if (null != data) {
				mTerminalId = data.getIntExtra(TERMINAL_ID, 0);
				loadData();
			}
			break;
		}
		}
	}

	private void setStatusAndButtons(TerminalApply apply) {
		status = null == apply ? mTerminalStatus : apply.getStatus();
		String[] terminalStatus = getResources().getStringArray(
				R.array.terminal_status);
		mStatus.setText(terminalStatus[status]);

		appidBoolean = !"".equals(apply.getAppId()) && apply.getAppId() != 0;
		videoBoolean = 1 == isVideo;

		switch (status) {
		case OPENED:
			// mBtnRightBottom.setVisibility(View.VISIBLE);
			// mBtnRightBottom.setText(getString(R.string.terminal_button_pos));
			// mBtnRightBottom.setOnClickListener(mPosListener);
			if (appidBoolean) {
				if (videoBoolean) {

					// mBtnLeftTop.setVisibility(View.INVISIBLE);
					// mBtnLeftBottom.setVisibility(View.VISIBLE);
					// mBtnLeftBottom
					// .setText(getString(R.string.terminal_button_sync));
					// mBtnLeftBottom.setOnClickListener(mSyncListener);
					// mBtnRightTop.setVisibility(View.VISIBLE);
					// mBtnRightTop
					// .setText(getString(R.string.terminal_button_video));
					// mBtnRightTop.setOnClickListener(mVideoListener);
					mBtnLeftTop.setVisibility(View.INVISIBLE);
					mBtnLeftBottom.setVisibility(View.INVISIBLE);
					mBtnRightTop.setVisibility(View.VISIBLE);
					mBtnRightTop
							.setText(getString(R.string.terminal_button_sync));
					mBtnRightTop.setOnClickListener(mSyncListener);
					mBtnRightBottom.setVisibility(View.VISIBLE);
					mBtnRightBottom
							.setText(getString(R.string.terminal_button_video));
					mBtnRightBottom.setOnClickListener(mVideoListener);

				} else {
					// mBtnRightTop.setVisibility(View.VISIBLE);
					// mBtnRightTop
					// .setText(getString(R.string.terminal_button_sync));
					// mBtnRightTop.setOnClickListener(mSyncListener);
					mBtnRightBottom.setVisibility(View.VISIBLE);
					mBtnRightBottom
							.setText(getString(R.string.terminal_button_sync));
					mBtnRightBottom.setOnClickListener(mSyncListener);
				}

			} else {
				if (videoBoolean) {
					// mBtnRightTop.setVisibility(View.VISIBLE);
					// mBtnRightTop
					// .setText(getString(R.string.terminal_button_video));
					// mBtnRightTop.setOnClickListener(mVideoListener);
					mBtnRightBottom.setVisibility(View.VISIBLE);
					mBtnRightBottom
							.setText(getString(R.string.terminal_button_video));
					mBtnRightBottom.setOnClickListener(mVideoListener);
				}
			}
			break;
		case PART_OPENED:
			if (appidBoolean) {

				if (videoBoolean) {

					mBtnLeftTop.setVisibility(View.INVISIBLE);
					// mBtnLeftTop.setVisibility(View.VISIBLE);
					// mBtnLeftTop
					// .setText(getString(R.string.terminal_button_sync));
					// mBtnLeftTop.setOnClickListener(mSyncListener);
					// mBtnLeftBottom.setVisibility(View.VISIBLE);
					// mBtnLeftBottom
					// .setText(getString(R.string.terminal_button_video));
					// mBtnLeftBottom.setOnClickListener(mVideoListener);
					mBtnLeftBottom.setVisibility(View.VISIBLE);
					mBtnLeftBottom
							.setText(getString(R.string.terminal_button_sync));
					mBtnLeftBottom.setOnClickListener(mSyncListener);
					mBtnLeftBottom.setVisibility(View.VISIBLE);
					mBtnLeftBottom
							.setText(getString(R.string.terminal_button_video));
					mBtnLeftBottom.setOnClickListener(mVideoListener);
				} else {
					mBtnLeftTop.setVisibility(View.INVISIBLE);
					mBtnLeftBottom.setVisibility(View.INVISIBLE);
					// mBtnLeftBottom.setVisibility(View.VISIBLE);
					// mBtnLeftBottom
					// .setText(getString(R.string.terminal_button_sync));
					// mBtnLeftBottom.setOnClickListener(mSyncListener);
					mBtnRightTop.setVisibility(View.VISIBLE);
					mBtnRightTop
							.setText(getString(R.string.terminal_button_sync));
					mBtnRightTop.setOnClickListener(mSyncListener);
				}

			} else {

				mBtnLeftTop.setVisibility(View.INVISIBLE);
				mBtnLeftBottom.setVisibility(View.INVISIBLE);
				if (videoBoolean) {
					// mBtnLeftBottom.setVisibility(View.VISIBLE);
					// mBtnLeftBottom
					// .setText(getString(R.string.terminal_button_video));
					// mBtnLeftBottom.setOnClickListener(mVideoListener);
					mBtnRightTop.setVisibility(View.VISIBLE);
					mBtnRightTop
							.setText(getString(R.string.terminal_button_video));
					mBtnRightTop.setOnClickListener(mVideoListener);
				} else {
					// mBtnLeftBottom.setVisibility(View.INVISIBLE);
					mBtnRightTop.setVisibility(View.INVISIBLE);
				}

			}
			mBtnRightBottom.setVisibility(View.VISIBLE);
			mBtnRightBottom.setText(getString(R.string.terminal_button_reopen));
			mBtnRightBottom.setOnClickListener(mReOpenListener);
			// mBtnRightTop.setVisibility(View.VISIBLE);
			// mBtnRightTop.setText(getString(R.string.terminal_button_reopen));
			// mBtnRightTop.setOnClickListener(mReOpenListener);
			// mBtnRightBottom.setVisibility(View.VISIBLE);
			// mBtnRightBottom.setText(getString(R.string.terminal_button_pos));
			// mBtnRightBottom.setOnClickListener(mPosListener);
			break;
		case UNOPENED:
			if (appidBoolean) {
				if (videoBoolean) {

					mBtnLeftBottom.setVisibility(View.VISIBLE);
					mBtnLeftBottom
							.setText(getString(R.string.terminal_button_sync));
					mBtnLeftBottom.setOnClickListener(mSyncListener);
					mBtnRightTop.setVisibility(View.VISIBLE);
					mBtnRightTop
							.setText(getString(R.string.terminal_button_reopen));
					mBtnRightTop.setOnClickListener(mReOpenListener);
					mBtnRightBottom.setVisibility(View.VISIBLE);
					mBtnRightBottom
							.setText(getString(R.string.terminal_button_video));
					mBtnRightBottom.setOnClickListener(mVideoListener);
				} else {
					mBtnRightTop.setVisibility(View.VISIBLE);
					mBtnRightTop
							.setText(getString(R.string.terminal_button_sync));
					mBtnRightTop.setOnClickListener(mSyncListener);
					mBtnRightBottom.setVisibility(View.VISIBLE);
					mBtnRightBottom
							.setText(getString(R.string.terminal_button_reopen));
					mBtnRightBottom.setOnClickListener(mReOpenListener);
				}
			} else {

				if (videoBoolean) {
					mBtnRightTop.setVisibility(View.VISIBLE);
					mBtnRightTop
							.setText(getString(R.string.terminal_button_open));
					mBtnRightTop.setOnClickListener(mOpenListener);
					mBtnRightBottom.setVisibility(View.VISIBLE);
					mBtnRightBottom
							.setText(getString(R.string.terminal_button_video));
					mBtnRightBottom.setOnClickListener(mVideoListener);
				} else {

					mBtnRightBottom.setVisibility(View.VISIBLE);
					mBtnRightBottom
							.setText(getString(R.string.terminal_button_open));
					mBtnRightBottom.setOnClickListener(mOpenListener);
				}
			}

			break;
		case CANCELED:
			if (appidBoolean) {
				if (videoBoolean) {
					mBtnLeftBottom.setVisibility(View.VISIBLE);
					mBtnLeftBottom
							.setText(getString(R.string.terminal_button_sync));
					mBtnLeftBottom.setOnClickListener(mSyncListener);
					mBtnRightTop.setVisibility(View.VISIBLE);
					mBtnRightTop
							.setText(getString(R.string.terminal_button_video));
					mBtnRightTop.setOnClickListener(mVideoListener);
					mBtnRightBottom.setVisibility(View.VISIBLE);
					mBtnRightBottom
							.setText(getString(R.string.terminal_button_reopen));
					mBtnRightBottom.setOnClickListener(mReOpenListener);
				} else {
					mBtnRightTop.setVisibility(View.VISIBLE);
					mBtnRightTop
							.setText(getString(R.string.terminal_button_sync));
					mBtnRightTop.setOnClickListener(mSyncListener);
					mBtnRightBottom.setVisibility(View.VISIBLE);
					mBtnRightBottom
							.setText(getString(R.string.terminal_button_reopen));
					mBtnRightBottom.setOnClickListener(mReOpenListener);
				}
			} else {
				if (videoBoolean) {

					mBtnRightTop.setVisibility(View.VISIBLE);
					mBtnRightTop
							.setText(getString(R.string.terminal_button_reopen));
					mBtnRightTop.setOnClickListener(mReOpenListener);
					mBtnRightBottom.setVisibility(View.VISIBLE);
					mBtnRightBottom
							.setText(getString(R.string.terminal_button_video));
					mBtnRightBottom.setOnClickListener(mVideoListener);
				} else {

					mBtnRightBottom.setVisibility(View.VISIBLE);
					mBtnRightBottom
							.setText(getString(R.string.terminal_button_reopen));
					mBtnRightBottom.setOnClickListener(mReOpenListener);
				}
			}
			break;
		case STOPPED:

			break;
		}
	}

	private LinearLayout setTerminalInfo(TerminalApply apply) {

		if (null == apply) {
			LinkedHashMap<String, String> pairs = new LinkedHashMap<String, String>();
			pairs.put(getString(R.string.terminal_no_detail), "");
			return renderCategoryTemplate(pairs, false);
		}
		LinkedHashMap<String, String> pairs = new LinkedHashMap<String, String>();
		String[] keys = getResources().getStringArray(
				R.array.terminal_apply_keys);
		pairs.put(keys[0], apply.getTerminalNum());
		pairs.put(keys[1], apply.getBrandName());
		pairs.put(keys[2], apply.getModelNumber());
		pairs.put(keys[3], apply.getFactorName());
		pairs.put(keys[4], apply.getTitle());
		pairs.put(keys[5], apply.getPhone());
		pairs.put(keys[6], apply.getOrderNumber());
		pairs.put(keys[7], apply.getCreatedAt());
		return renderCategoryTemplate(pairs, false);
	}

	private void addRatesTable(LinearLayout category, List<TerminalRate> rates) {

		LinearLayout layout = (LinearLayout) findViewById(R.id.terminal_rates);
		if (null == category || null == rates || rates.size() <= 0)
			return;
		LinearLayout header = (LinearLayout) mInflater.inflate(
				R.layout.terminal_rates_header, null);
		layout.addView(header);
		for (int i = 0; i < rates.size(); i++) {
			LinearLayout column = (LinearLayout) mInflater.inflate(
					R.layout.terminal_rates_column, null);

			LinearLayout base = (LinearLayout) column
					.findViewById(R.id.baseline);
			TextView typeTv = (TextView) column
					.findViewById(R.id.terminal_column_type);
			TextView rateTv = (TextView) column
					.findViewById(R.id.terminal_column_rate);
			TextView statusTv = (TextView) column
					.findViewById(R.id.terminal_column_status);
			String[] status = getResources().getStringArray(
					R.array.terminal_status);
			typeTv.setText(rates.get(i).getType());
			if (rates.get(i).getTrade_type() == 1)
				rateTv.setText(rates.get(i).getBaseRate()
						+ rates.get(i).getServiceRate()
						+ getString(R.string.qianfenhao));
			if (rates.get(i).getTrade_type() == 2)
				rateTv.setText(rates.get(i).getTerminalRate()
						+ getString(R.string.qianfenhao));
			// TODO:
			if (rates.get(i).getStatus() == 0)
				statusTv.setText(status[3]);
			else
				statusTv.setText(status[1]);
			if (i != rates.size() - 1)
				base.setVisibility(View.INVISIBLE);
			layout.addView(column);
		}
	}

	private void setOpenInfo(List<TerminalOpen> openDetails) {

		if (openDetails == null || openDetails.size() <= 0) {

			LinkedHashMap<String, String> pairs = new LinkedHashMap<String, String>();
			pairs.put(getString(R.string.terminal_no_open_detail), "");
			renderCategoryTemplate(pairs, true);
			return;
		}

		LinkedHashMap<String, String> pairs = new LinkedHashMap<String, String>();
		List<TerminalOpen> photoOpens = new ArrayList<TerminalOpen>();
		final List<String> imageUrls = new ArrayList<String>();
		final List<String> imageNames = new ArrayList<String>();
		for (TerminalOpen openDetail : openDetails) {
			if (null == openDetail)
				continue;
			if (openDetail.getTypes() == 1) {
				pairs.put(openDetail.getKey(), openDetail.getValue());
			} else {
				photoOpens.add(openDetail);
				imageNames.add(openDetail.getKey());
				imageUrls.add(openDetail.getValue());
			}
		}
		LinearLayout category = renderCategoryTemplate(pairs, true);

		View.OnClickListener onViewPhotoListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				AlertDialog.Builder build = new AlertDialog.Builder(
						TerminalManagerDetailActivity.this);
				LayoutInflater factory = LayoutInflater
						.from(TerminalManagerDetailActivity.this);
				final View textEntryView = factory.inflate(R.layout.show_view,
						null);
				build.setView(textEntryView);
				final ImageView view = (ImageView) textEntryView
						.findViewById(R.id.imag);
				// ImageCacheUtil.IMAGE_CACHE.get((String) v.getTag(), view);
				ImageLoader.getInstance().displayImage((String) v.getTag(),
						view, options);
				build.create().show();
			}
		};

		LinearLayout column = null;
		for (int i = 0; i < photoOpens.size(); i++) {
			TerminalOpen photoOpen = photoOpens.get(i);
			if (i % 2 == 0) {
				column = (LinearLayout) mInflater.inflate(
						R.layout.terminal_open_column, null);
				TextView key = (TextView) column
						.findViewById(R.id.terminal_open_key_left);
				ImageButton icon = (ImageButton) column
						.findViewById(R.id.terminal_open_icon_left);
				icon.setTag(imageUrls.get(i));
				icon.setOnClickListener(onViewPhotoListener);
				key.setText(photoOpen.getKey());
				key.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
				if (i == photoOpens.size() - 1) {
					category.addView(column);
					column.findViewById(R.id.terminal_open_right)
							.setVisibility(View.INVISIBLE);
				}
			} else {
				TextView key = (TextView) column
						.findViewById(R.id.terminal_open_key_right);
				ImageButton icon = (ImageButton) column
						.findViewById(R.id.terminal_open_icon_right);
				icon.setTag(imageUrls.get(i));
				icon.setOnClickListener(onViewPhotoListener);
				key.setText(photoOpen.getKey());
				key.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
				category.addView(column);
			}
		}
	}

	private void addComments(List<TerminalComment> comments) {
		if (null != comments && comments.size() > 0) {
			for (TerminalComment comment : comments) {
				if (null == comment)
					continue;
				LinearLayout commentLayout = (LinearLayout) mInflater.inflate(
						R.layout.after_sale_detail_comment, null);
				TextView content = (TextView) commentLayout
						.findViewById(R.id.comment_content);
				TextView person = (TextView) commentLayout
						.findViewById(R.id.comment_person);
				TextView time = (TextView) commentLayout
						.findViewById(R.id.comment_time);
				content.setText(comment.getContent());
				person.setText(comment.getName());
				time.setText(comment.getCreateAt());
				mCommentContainer.addView(commentLayout);
			}
		}
	}

	private TextView createCategoryText() {
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, 2, 0, 0);
		TextView tv = new TextView(this);
		tv.setLayoutParams(lp);
		tv.setTextColor(Color.parseColor("#292929"));
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
		return tv;
	}

	@SuppressLint("ResourceAsColor")
	private LinearLayout renderCategoryTemplate(
			LinkedHashMap<String, String> pairs, Boolean isOpen) {

		LinearLayout terminalCategory = (LinearLayout) mInflater.inflate(
				R.layout.after_sale_detail_category_ter, null);

		if (isOpen) {
			mCategoryContainer.addView(terminalCategory);
		} else {
			mTerminalContainer.addView(terminalCategory);
		}

		LinearLayout keyContainer = (LinearLayout) terminalCategory
				.findViewById(R.id.category_key_container);
		LinearLayout valueContainer = (LinearLayout) terminalCategory
				.findViewById(R.id.category_value_container);

		for (Map.Entry<String, String> pair : pairs.entrySet()) {
			TextView key = createCategoryText();
			key.setText(pair.getKey());
			key.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			keyContainer.addView(key);

			TextView value = createCategoryText();
			value.setText(pair.getValue());
			value.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			value.setTextColor(R.color.text6c6c6c6);
			valueContainer.addView(value);
		}
		return terminalCategory;
	}
}
