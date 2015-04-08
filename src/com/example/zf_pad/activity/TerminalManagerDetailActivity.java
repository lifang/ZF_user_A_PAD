package com.example.zf_pad.activity;

import static com.example.zf_pad.fragment.Constants.TerminalIntent.TERMINAL_ID;
import static com.example.zf_pad.fragment.Constants.TerminalIntent.TERMINAL_NUMBER;
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
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.zf_pad.R;
import com.example.zf_pad.entity.TerminalApply;
import com.example.zf_pad.entity.TerminalComment;
import com.example.zf_pad.entity.TerminalDetail;
import com.example.zf_pad.entity.TerminalOpen;
import com.example.zf_pad.entity.TerminalRate;
import com.example.zf_pad.fragment.Constants;
import com.example.zf_pad.trade.API;
import com.example.zf_pad.trade.common.CommonUtil;
import com.example.zf_pad.trade.common.HttpCallback;
import com.example.zf_pad.util.TitleMenuUtil;
import com.google.gson.reflect.TypeToken;

@SuppressLint("NewApi")
public class TerminalManagerDetailActivity extends Activity {

	private int mTerminalStatus;
	private String mTerminalNumber;
	private int mTerminalId;

	private LayoutInflater mInflater;
	private TextView mStatus;
	private Button mBtnLeftTop;
	private Button mBtnLeftBottom;
	private Button mBtnRightTop;
	private Button mBtnRightBottom;
	private LinearLayout mCategoryContainer;
	private LinearLayout mCommentContainer;

	private View.OnClickListener mSyncListener;
	private View.OnClickListener mOpenListener;
	private View.OnClickListener mPosListener;
	private View.OnClickListener mVideoListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mTerminalId = getIntent().getIntExtra(TERMINAL_ID, 0);
		mTerminalNumber = getIntent().getStringExtra(TERMINAL_NUMBER);
		mTerminalStatus = getIntent().getIntExtra(TERMINAL_STATUS, 0);

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
	}

	private void initBtnListeners() {
		mSyncListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				CommonUtil.toastShort(TerminalManagerDetailActivity.this,
						"synchronising...");
			}
		};
		mOpenListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO:
				// Intent intent = new
				// Intent(TerminalManagerDetailActivity.this,
				// ApplyDetailActivity.class);
				// intent.putExtra(TERMINAL_ID, mTerminalId);
				// intent.putExtra(TERMINAL_STATUS, mTerminalStatus);
				// startActivity(intent);
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
				CommonUtil.toastShort(TerminalManagerDetailActivity.this,
						"not yet completed...");
			}
		};
	}

	private void loadData() {
		API.getTerminalDetail(this, mTerminalId, Constants.TEST_CUSTOMER, new HttpCallback<TerminalDetail>(this) {
			@Override
			public void onSuccess(TerminalDetail data) {
				TerminalApply apply = data.getApplyDetails();
				List<TerminalComment> comments = data.getTrackRecord();
				List<TerminalRate> rates = data.getRates();
				List<TerminalOpen> openDetails = data.getOpeningDetails();

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
			public TypeToken<TerminalDetail> getTypeToken() {
				return new TypeToken<TerminalDetail>() {
				};
			}
		});
	}

	private void setStatusAndButtons(TerminalApply apply) {
		int status = null == apply ? mTerminalStatus : apply.getStatus();
		String[] terminalStatus = getResources().getStringArray(
				R.array.terminal_status);
		mStatus.setText(terminalStatus[status]);
		switch (status) {
		case OPENED:
			mBtnRightTop.setVisibility(View.VISIBLE);
			mBtnRightBottom.setVisibility(View.VISIBLE);

			mBtnRightTop.setText(getString(R.string.terminal_button_video));
			mBtnRightTop.setOnClickListener(mVideoListener);
			mBtnRightBottom.setText(getString(R.string.terminal_button_pos));
			mBtnRightBottom.setOnClickListener(mPosListener);
			break;
		case PART_OPENED:
			mBtnLeftTop.setVisibility(View.VISIBLE);
			mBtnLeftBottom.setVisibility(View.VISIBLE);
			mBtnRightTop.setVisibility(View.VISIBLE);
			mBtnRightBottom.setVisibility(View.VISIBLE);

			mBtnLeftTop.setText(getString(R.string.terminal_button_sync));
			mBtnLeftTop.setOnClickListener(mSyncListener);
			mBtnLeftBottom.setText(getString(R.string.terminal_button_reopen));
			mBtnLeftBottom.setOnClickListener(mOpenListener);
			mBtnRightTop.setText(getString(R.string.terminal_button_video));
			mBtnRightTop.setOnClickListener(mVideoListener);
			mBtnRightBottom.setText(getString(R.string.terminal_button_pos));
			mBtnRightBottom.setOnClickListener(mPosListener);
			break;
		case UNOPENED:
			mBtnLeftTop.setVisibility(View.VISIBLE);
			mBtnLeftBottom.setVisibility(View.INVISIBLE);
			mBtnRightTop.setVisibility(View.VISIBLE);
			mBtnRightBottom.setVisibility(View.VISIBLE);

			mBtnLeftTop.setText(getString(R.string.terminal_button_sync));
			mBtnLeftTop.setOnClickListener(mSyncListener);
			mBtnRightTop.setText(getString(R.string.terminal_button_open));
			mBtnRightTop.setOnClickListener(mOpenListener);
			mBtnRightBottom.setText(getString(R.string.terminal_button_video));
			mBtnRightBottom.setOnClickListener(mVideoListener);
			break;
		case CANCELED:
			break;
		case STOPPED:
			mBtnRightTop.setVisibility(View.VISIBLE);

			mBtnRightTop.setText(getString(R.string.terminal_button_sync));
			mBtnRightTop.setOnClickListener(mSyncListener);
			break;
		}
	}

	private LinearLayout setTerminalInfo(TerminalApply apply) {

		if (null == apply) {
			LinkedHashMap<String, String> pairs = new LinkedHashMap<String, String>();
			pairs.put(getString(R.string.terminal_no_detail), "");
			return renderCategoryTemplate(R.string.terminal_category_apply,
					pairs);
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
		return renderCategoryTemplate(R.string.terminal_category_apply, pairs);
	}

	private void addRatesTable(LinearLayout category, List<TerminalRate> rates) {

		TableLayout terminal_rates = (TableLayout) findViewById(R.id.terminal_rates);

		if (null == category || null == rates || rates.size() <= 0) {
			terminal_rates.setVisibility(View.GONE);
			return;
		}

		for (int i = 0; i < rates.size(); i++) {

			TerminalRate rate = rates.get(i);

			TableRow tableRow = new TableRow(this);
			LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			if (i == rates.size() - 1)
				lp.setMargins(1, 0, 1, 1);
			lp.setMargins(1, 0, 1, 0);
			tableRow.setLayoutParams(lp);
			tableRow.setPadding(5, 5, 5, 5);
			tableRow.setBackgroundColor(Color.parseColor("#ffffff"));
			TextView typeTv = createRateText();
			TextView rateTv = createRateText();
			TextView statusTv = createRateText();

			String[] status = getResources().getStringArray(
					R.array.terminal_status);
			typeTv.setText(rate.getType());
			rateTv.setText(rate.getBaseRate()
					+ getString(R.string.notation_percent));
			statusTv.setText(status[rate.getStatus()]);
			tableRow.addView(typeTv);
			tableRow.addView(rateTv);
			tableRow.addView(statusTv);
			terminal_rates.addView(tableRow);
		}
	}

	private void setOpenInfo(List<TerminalOpen> openDetails) {

		LinkedHashMap<String, String> pairs = new LinkedHashMap<String, String>();
		if (openDetails == null || openDetails.size() <= 0) {
			renderCategoryTemplate(R.string.terminal_category_open, pairs);
			return;
		}

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
		LinearLayout category = renderCategoryTemplate(
				R.string.terminal_category_open, pairs);

		View.OnClickListener onViewPhotoListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO:
				// int position = (Integer) view.getTag();
				// Intent intent = new
				// Intent(TerminalManagerDetailActivity.this,
				// ShowWebImageActivity.class);
				// intent.putExtra(IMAGE_NAMES, StringUtil.join(imageNames,
				// ","));
				// intent.putExtra(IMAGE_URLS, StringUtil.join(imageUrls, ","));
				// intent.putExtra(POSITION, position);
				// startActivity(intent);
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
				icon.setTag(i);
				icon.setOnClickListener(onViewPhotoListener);
				key.setText(photoOpen.getKey());
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
				icon.setTag(i);
				icon.setOnClickListener(onViewPhotoListener);
				key.setText(photoOpen.getKey());
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
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 36);
		return tv;
	}

	private TextView createRateText() {
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, 1);
		TextView tv = new TextView(this);
		tv.setLayoutParams(lp);
		tv.setGravity(Gravity.CENTER);
		tv.setTextColor(Color.parseColor("#292929"));
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 36);
		return tv;
	}

	private LinearLayout renderCategoryTemplate(int titleRes,
			LinkedHashMap<String, String> pairs) {
//		LinearLayout terminal_category = (LinearLayout) findViewById(R.id.terminal_category);
//		TextView title = (TextView) findViewById(R.id.category_title);
//		LinearLayout keyContainer = (LinearLayout) findViewById(R.id.category_key_container);
//		LinearLayout valueContainer = (LinearLayout) findViewById(R.id.category_value_container);
//
//		title.setText(getString(titleRes));
		
		LinearLayout terminalCategory = (LinearLayout) mInflater.inflate(R.layout.after_sale_detail_category, null);
		mCategoryContainer.addView(terminalCategory);

		TextView title = (TextView) terminalCategory.findViewById(R.id.category_title);
		LinearLayout keyContainer = (LinearLayout) terminalCategory.findViewById(R.id.category_key_container);
		LinearLayout valueContainer = (LinearLayout) terminalCategory.findViewById(R.id.category_value_container);

		title.setText(getString(titleRes));
		for (Map.Entry<String, String> pair : pairs.entrySet()) {
			TextView key = createCategoryText();
			key.setText(pair.getKey());
			keyContainer.addView(key);

			TextView value = createCategoryText();
			value.setText(pair.getValue());
			valueContainer.addView(value);
		}
		return terminalCategory;
	}

}
