package com.example.zf_pad.trade;

import static com.example.zf_pad.fragment.Constants.ApplyIntent.CHOOSE_ITEMS;
import static com.example.zf_pad.fragment.Constants.ApplyIntent.CHOOSE_TITLE;
import static com.example.zf_pad.fragment.Constants.ApplyIntent.REQUEST_CHOOSE_BANK;
import static com.example.zf_pad.fragment.Constants.ApplyIntent.REQUEST_CHOOSE_CHANNEL;
import static com.example.zf_pad.fragment.Constants.ApplyIntent.REQUEST_CHOOSE_CITY;
import static com.example.zf_pad.fragment.Constants.ApplyIntent.REQUEST_CHOOSE_MERCHANT;
import static com.example.zf_pad.fragment.Constants.ApplyIntent.REQUEST_TAKE_PHOTO;
import static com.example.zf_pad.fragment.Constants.ApplyIntent.REQUEST_UPLOAD_IMAGE;
import static com.example.zf_pad.fragment.Constants.ApplyIntent.SELECTED_BANK;
import static com.example.zf_pad.fragment.Constants.ApplyIntent.SELECTED_BILLING;
import static com.example.zf_pad.fragment.Constants.ApplyIntent.SELECTED_CHANNEL;
import static com.example.zf_pad.fragment.Constants.ApplyIntent.SELECTED_ID;
import static com.example.zf_pad.fragment.Constants.ApplyIntent.SELECTED_TITLE;
import static com.example.zf_pad.fragment.Constants.CityIntent.SELECTED_CITY;
import static com.example.zf_pad.fragment.Constants.CityIntent.SELECTED_PROVINCE;
import static com.example.zf_pad.fragment.Constants.TerminalIntent.TERMINAL_ID;
import static com.example.zf_pad.fragment.Constants.TerminalIntent.TERMINAL_NUMBER;
import static com.example.zf_pad.fragment.Constants.TerminalIntent.TERMINAL_STATUS;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zf_pad.MyApplication;
import com.example.zf_pad.R;
import com.example.zf_pad.trade.common.CommonUtil;
import com.example.zf_pad.trade.common.HttpCallback;
import com.example.zf_pad.trade.common.TextWatcherAdapter;
import com.example.zf_pad.trade.entity.ApplyBank;
import com.example.zf_pad.trade.entity.ApplyChannel;
import com.example.zf_pad.trade.entity.ApplyChooseItem;
import com.example.zf_pad.trade.entity.City;
import com.example.zf_pad.trade.entity.MerchantForApply;
import com.example.zf_pad.trade.entity.MyApplyCustomerDetail;
import com.example.zf_pad.trade.entity.MyApplyMaterial;
import com.example.zf_pad.trade.entity.MyApplyTerminalDetail;
import com.example.zf_pad.trade.entity.My_ApplyDetail;
import com.example.zf_pad.trade.entity.OpeningInfos;
import com.example.zf_pad.trade.entity.Province;
import com.example.zf_pad.util.ImageCacheUtil;
import com.example.zf_pad.util.StringUtil;
import com.example.zf_pad.util.TitleMenuUtil;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MyApplyDetail extends FragmentActivity {

	private static final int TYPE_TEXT = 1;
	private static final int TYPE_IMAGE = 2;
	private static final int TYPE_BANK = 3;

	private static final int APPLY_PUBLIC = 1;
	private static final int APPLY_PRIVATE = 2;
	private int mApplyType;

	private static final int ITEM_EDIT = 1;
	private static final int ITEM_CHOOSE = 2;
	private static final int ITEM_UPLOAD = 3;
	private static final int ITEM_VIEW = 4;
	private static final int ITEM_BLANK = 5;

	private int mTerminalId;
	private int mTerminalStatus;

	private MerchantForApply mMerchant;

	private LayoutInflater mInflater;

	private TextView toPublic;
	private TextView toPrivate;
	private TextView mPosBrand;
	private TextView mPosModel;
	private TextView mSerialNum;

	private String[] mMerchantKeys;
	private String[] mBankKeys;

	private LinearLayout mContainer;
	private LinearLayout mMerchantContainer_0;
	private LinearLayout mMerchantContainer_1;
	private LinearLayout mMerchantContainer_2;
	// private LinearLayout mMerchantContainer;
	private LinearLayout mCustomerContainer_1;
	private LinearLayout mCustomerContainer_2;
	// private LinearLayout mCustomerContainer;
	private LinearLayout mMaterialContainer_1_1;
	private LinearLayout mMaterialContainer_1_2;
	private LinearLayout mMaterialContainer_1_3;
	private LinearLayout mMaterialContainer_2_1;
	private LinearLayout mMaterialContainer_2_2;

	private Button mApplySubmit;

	private int mMerchantId;
	private int mMerchantGender = 1;
	private String mMerchantBirthday;
	private Province mMerchantProvince;
	private City mMerchantCity;
//	private int mChannelId;

	private String mTerminalNumber;
	private String photoPath;
	private TextView uploadingTextView;
	private ImageButton uploadingImageButton;
	private ApplyBank mChosenBank;
	private ApplyChannel mChosenChannel;
	private ApplyChannel.Billing mChosenBilling;
	private String mBankKey;
	private String mUploadKey;
	private ArrayList<ApplyChooseItem> mChannelItems = new ArrayList<ApplyChooseItem>();

	private List<String> mImageUrls = new ArrayList<String>();
	private List<String> mImageNames = new ArrayList<String>();

	private LinkedHashMap<Integer, MyApplyMaterial> mMaterials = new LinkedHashMap<Integer, MyApplyMaterial>();

	private String mUploadUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_detail);
		new TitleMenuUtil(this, getString(R.string.title_apply_open)).show();
		mTerminalId = getIntent().getIntExtra(TERMINAL_ID, 0);
		mTerminalNumber = getIntent().getStringExtra(TERMINAL_NUMBER);
		mTerminalStatus = getIntent().getIntExtra(TERMINAL_STATUS, 0);

		initViews();
		loadData(mApplyType);
	}

	private void initViews() {
		mInflater = LayoutInflater.from(this);

		toPublic = (TextView) findViewById(R.id.toPublic);
		toPrivate = (TextView) findViewById(R.id.toPrivate);

		toPublic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				toPublic.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.tab_bg));
				toPrivate.setBackgroundDrawable(null);
				mApplyType = APPLY_PUBLIC;
				loadData(APPLY_PUBLIC);

			}
		});

		toPrivate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				toPrivate.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.tab_bg));
				toPublic.setBackgroundDrawable(null);
				mApplyType = APPLY_PRIVATE;
				loadData(APPLY_PRIVATE);

			}
		});

		mApplyType = APPLY_PUBLIC;

		mPosBrand = (TextView) findViewById(R.id.apply_detail_brand);
		mPosModel = (TextView) findViewById(R.id.apply_detail_model);
		mSerialNum = (TextView) findViewById(R.id.apply_detail_serial);

		mContainer = (LinearLayout) findViewById(R.id.apply_detail_container);
		mMerchantContainer_0 = (LinearLayout) findViewById(R.id.mMerchantContainer_0);
		mMerchantContainer_1 = (LinearLayout) findViewById(R.id.mMerchantContainer_1);
		mMerchantContainer_2 = (LinearLayout) findViewById(R.id.mMerchantContainer_2);
		// mMerchantContainer = (LinearLayout)
		// findViewById(R.id.apply_detail_merchant_container);
		mCustomerContainer_1 = (LinearLayout) findViewById(R.id.mCustomerContainer_1);
		mCustomerContainer_2 = (LinearLayout) findViewById(R.id.mCustomerContainer_2);
		// mCustomerContainer = (LinearLayout)
		// findViewById(R.id.apply_detail_customer_container);
		mMaterialContainer_1_1 = (LinearLayout) findViewById(R.id.apply_detail_material_container_1_1);
		mMaterialContainer_1_2 = (LinearLayout) findViewById(R.id.apply_detail_material_container_1_2);
		mMaterialContainer_1_3 = (LinearLayout) findViewById(R.id.apply_detail_material_container_1_3);
		mMaterialContainer_2_1 = (LinearLayout) findViewById(R.id.apply_detail_material_container_2_1);
		mMaterialContainer_2_2 = (LinearLayout) findViewById(R.id.apply_detail_material_container_2_2);

		mApplySubmit = (Button) findViewById(R.id.apply_submit);

		mApplySubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				List<Map<String, Object>> totalParams = new ArrayList<Map<String, Object>>();

				Map<String, Object> params = new HashMap<String, Object>();
				params.put("terminalId", mTerminalId);
				params.put("status", mTerminalStatus == 2 ? 2 : 1);
				params.put("applyCustomerId", mMerchant.getCustomerId());
				params.put("publicPrivateStatus", mApplyType);

				params.put("merchantId", mMerchantId);
				params.put("name", getItemValue(mMerchantKeys[1]));
				params.put("merchantName", getItemValue(mMerchantKeys[2]));
				params.put("sex", mMerchantGender);
				params.put("birthday", getItemValue(mMerchantKeys[4]));
				params.put("cardId", getItemValue(mMerchantKeys[5]));
				params.put("phone", getItemValue(mMerchantKeys[6]));
				params.put("email", getItemValue(mMerchantKeys[7]));
				params.put("cityId",
						null != mMerchantCity ? mMerchantCity.getId() : 0);

				params.put("bankName", getItemValue(mBankKeys[0]));
				params.put("bankCode", getItemValue(mBankKeys[1]));
				params.put("bankNum", getItemValue(mBankKeys[2]));
				params.put("registeredNo", getItemValue(mBankKeys[3]));
				params.put("organizationNo", getItemValue(mBankKeys[4]));
				if (null != mChosenChannel)
					params.put("channel", mChosenChannel.getId());
				if (null != mChosenBilling)
					params.put("billingId", mChosenBilling.id);

				totalParams.add(params);
				for (MyApplyMaterial material : mMaterials.values()) {
					// text type
					if (material.getTypes() == TYPE_TEXT) {
						String key = material.getName();
						String value = getItemValue(key);
						material.setValue(value);
					}
					// bank type
					else if (material.getTypes() == TYPE_BANK) {
						String key = material.getName();
						LinearLayout item = (LinearLayout) mContainer.findViewWithTag(key);
						String value = (String) item.getTag(R.id.apply_detail_key);
						material.setValue(value);
					}
					if (TextUtils.isEmpty(material.getValue())) continue;
					// image types' value have been set in advance
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("key", material.getName());
					param.put("value", material.getValue());
					param.put("types", material.getTypes());
					param.put("openingRequirementId", material.getOpeningRequirementId());
					param.put("targetId", material.getId());

					totalParams.add(param);
				}

				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("paramMap", totalParams);
				API.submitApply(MyApplyDetail.this, paramMap, new HttpCallback(
						MyApplyDetail.this) {
					@Override
					public void onSuccess(Object data) {
						CommonUtil.toastShort(MyApplyDetail.this,
								data.toString());
						finish();
					}

					@Override
					public TypeToken getTypeToken() {
						return null;
					}
				});
			}
		});
	}

	@SuppressLint({ "NewApi", "ResourceAsColor" })
	private void loadData(int applyType) {

		mMerchantContainer_0.removeAllViews();
		mMerchantContainer_1.removeAllViews();
		mMerchantContainer_2.removeAllViews();
		// mMerchantContainer.removeAllViews();
		mCustomerContainer_1.removeAllViews();
		mCustomerContainer_2.removeAllViews();
		// mCustomerContainer.removeAllViews();
		mMaterialContainer_1_1.removeAllViews();
		mMaterialContainer_1_2.removeAllViews();
		mMaterialContainer_1_3.removeAllViews();
		mMaterialContainer_2_1.removeAllViews();
		mMaterialContainer_2_2.removeAllViews();
		// mMaterialContainer_1.removeAllViews();
		// mMaterialContainer_2.removeAllViews();
		// mMaterialContainer_3.removeAllViews();
		// mMaterialContainer_4.removeAllViews();
		// mMaterialContainer_5.removeAllViews();
		// mMaterialContainer_6.removeAllViews();
		// mMaterialContainer_7.removeAllViews();
		// mMaterialContainer_8.removeAllViews();
		// mMaterialContainer_9.removeAllViews();
		initMerchantDetailKeys();

		API.getApplyDetail(this, MyApplication.NewUser.getId(), mTerminalId, applyType,
				new HttpCallback<My_ApplyDetail>(this) {
					@Override
					public void onSuccess(My_ApplyDetail data) {
						MyApplyTerminalDetail terminalDetail = data
								.getTerminalDetail();
						final List<ApplyChooseItem> merchants = data
								.getMerchants();
						List<MyApplyMaterial> materials = data.getMaterials();
						List<MyApplyCustomerDetail> customerDetails = data
								.getCustomerDetails();

						final OpeningInfos openingInfos = data
								.getOpeningInfos();
						if (null != terminalDetail) {
							mPosBrand.setText(terminalDetail.getBrandName());
							mPosModel.setText(terminalDetail.getModelNumber());
							mSerialNum.setText(terminalDetail.getSerialNumber());
						}
						// set the choosing merchant listener
						// View merchantChoose = mMerchantContainer
						// .findViewWithTag(mMerchantKeys[0]);
						View merchantChoose_1 = mMerchantContainer_0
								.findViewWithTag(mMerchantKeys[0]);
						// merchantChoose
						// .setOnClickListener(new View.OnClickListener() {
						// @Override
						// public void onClick(View v) {
						// startChooseItemActivity(
						// REQUEST_CHOOSE_MERCHANT,
						// getString(R.string.title_apply_choose_merchant),
						// mMerchantId,
						// (ArrayList<ApplyChooseItem>) merchants);
						// }
						// });
						merchantChoose_1
								.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										startChooseItemActivity(
												REQUEST_CHOOSE_MERCHANT,
												getString(R.string.title_apply_choose_merchant),
												mMerchantId,
												(ArrayList<ApplyChooseItem>) merchants);
									}
								});
						// set the customer details
						setCustomerDetail(materials, customerDetails);
						if (openingInfos != null) {
							setData(openingInfos);
						}
						updateUIWithValidation();
					}

					@Override
					public void onFailure(String message) {

						super.onFailure(message);
					}

					@Override
					public TypeToken<My_ApplyDetail> getTypeToken() {
						return new TypeToken<My_ApplyDetail>() {
						};
					}
				});

	}
	private void setData(final OpeningInfos openingInfos) {
		final String[] items = getResources().getStringArray(
				R.array.apply_detail_gender);
		setItemValue(mMerchantKeys[1], openingInfos.getName());
		setItemValue(mMerchantKeys[2], openingInfos.getMerchant_name());
		setItemValue(mMerchantKeys[3], items[openingInfos.getSex() % 2]);
		mMerchantGender = openingInfos.getSex() % 2;
		setItemValue(mMerchantKeys[4],
				String.valueOf(openingInfos.getBirthday()));
		setItemValue(mMerchantKeys[5], openingInfos.getCard_id());
		setItemValue(mMerchantKeys[6], openingInfos.getPhone());
		setItemValue(mMerchantKeys[7], openingInfos.getEmail());
		CommonUtil.findCityById(this, openingInfos.getCity_id(),
				new CommonUtil.OnCityFoundListener() {
					@Override
					public void onCityFound(Province province, City city) {
						mMerchantProvince = province;
						mMerchantCity = city;
						Log.e("--mMerchantCity--", mMerchantCity.getName());
						setItemValue(mMerchantKeys[8], city.getName());
						updateUIWithValidation();
					}
				});

		setItemValue(mBankKeys[0], openingInfos.getAccount_bank_name());
		setItemValue(mBankKeys[1], openingInfos.getAccount_bank_num());
		setItemValue(mBankKeys[2], openingInfos.getAccount_bank_code());
		setItemValue(mBankKeys[3], openingInfos.getTax_registered_no());
		setItemValue(mBankKeys[4], openingInfos.getOrganization_code_no());
		setItemValue(mBankKeys[5],
				StringUtil.formatNull(openingInfos.getChannelname())
						+ StringUtil.formatNull(openingInfos.getBillingname()));
		mChosenChannel = new ApplyChannel();
		mChosenChannel.setId(openingInfos.getPay_channel_id());
		mChosenChannel.setName(openingInfos.getChannelname());
		mChosenBilling = mChosenChannel.new Billing();
		mChosenBilling.id = openingInfos.getBilling_cyde_id();
		mChosenBilling.name = openingInfos.getBillingname();
	}
	@Override
	protected void onActivityResult(final int requestCode, int resultCode,
			final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK)
			return;
		switch (requestCode) {
		case REQUEST_CHOOSE_MERCHANT: {
			mMerchantId = data.getIntExtra(SELECTED_ID, 0);
			setItemValue(mMerchantKeys[0], data.getStringExtra(SELECTED_TITLE));
			API.getApplyMerchantDetail(MyApplyDetail.this, mMerchantId,
					new HttpCallback<MerchantForApply>(MyApplyDetail.this) {
						@Override
						public void onSuccess(MerchantForApply data) {
							mMerchant = data;
							setMerchantDetailValues(data);
							mApplySubmit.setEnabled(true);
						}

						@Override
						public TypeToken<MerchantForApply> getTypeToken() {
							return new TypeToken<MerchantForApply>() {
							};
						}
					});
			break;
		}
		case REQUEST_CHOOSE_CITY: {
			mMerchantProvince = (Province) data
					.getSerializableExtra(SELECTED_PROVINCE);
			mMerchantCity = (City) data.getSerializableExtra(SELECTED_CITY);
			setItemValue(mMerchantKeys[8], mMerchantCity.getName());
			break;
		}
		case REQUEST_CHOOSE_CHANNEL: {
			mChosenChannel = (ApplyChannel) data
					.getSerializableExtra(SELECTED_CHANNEL);
			mChosenBilling = (ApplyChannel.Billing) data
					.getSerializableExtra(SELECTED_BILLING);
			setItemValue(getString(R.string.apply_detail_channel),
					mChosenChannel.getName());
			break;
		}

		case REQUEST_CHOOSE_BANK: {
			mChosenBank = (ApplyBank) data.getSerializableExtra(SELECTED_BANK);
			if (null != mChosenBank) {
				LinearLayout item = (LinearLayout) mContainer
						.findViewWithTag(mBankKey);
				item.setTag(R.id.apply_detail_key, mChosenBank.getCode());
				setItemValue(mBankKey, mChosenBank.getName());
			}

			break;
		}
		case REQUEST_UPLOAD_IMAGE:
		case REQUEST_TAKE_PHOTO: {

			final Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					if (msg.what == 1) {
						CommonUtil.toastShort(MyApplyDetail.this,
								(String) msg.obj);

						mUploadUri = (String) msg.obj;
						if (null != uploadingTextView) {
							// uploadingTextView.setBackgroundResource(R.drawable.check_it);
							// uploadingTextView
							// .setText("");
							// uploadingTextView.setClickable(false);
							uploadingTextView.setVisibility(View.GONE);
							uploadingImageButton.setVisibility(View.VISIBLE);
							uploadingImageButton
									.setOnClickListener(new onWatchListener());

						}
						for (MyApplyMaterial material : mMaterials.values()) {
							if (material.getTypes() == TYPE_IMAGE && material.getName().equals(mUploadKey)) {
								material.setValue(mUploadUri);
								break;
							}
						}
					} else {
						CommonUtil.toastShort(MyApplyDetail.this,
								getString(R.string.toast_upload_failed));
						if (null != uploadingTextView) {
							uploadingTextView
									.setText(getString(R.string.apply_upload_again));
							uploadingTextView.setClickable(true);
						}
					}

				}
			};
			if (null != uploadingTextView) {
				uploadingTextView.setText(getString(R.string.apply_uploading));
				uploadingTextView.setClickable(false);
			}
			String realPath = "";
			if (requestCode == REQUEST_TAKE_PHOTO) {
				realPath = photoPath;
			} else {
				Uri uri = data.getData();
				if (uri != null) {
					// realPath = getRealPathFromURI(uri);
					Cursor cursor = getContentResolver().query(uri, null, null,
							null, null);
					cursor.moveToFirst();
					realPath = cursor.getString(cursor.getColumnIndex("_data"));
					Log.e("localSelectPath", realPath);
					cursor.close();
				}
			}
			if (TextUtils.isEmpty(realPath)) {
				handler.sendEmptyMessage(0);
				return;
			}

			File file = new File(realPath);
			if (file.exists() && file.length() > 0) {

				File img = new File(realPath);
				RequestParams params = new RequestParams();
				try {
					params.put("fileImg", img);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				AsyncHttpClient client = new AsyncHttpClient();
				client.setTimeout(10000);// 设置超时时间
				client.setMaxConnections(10);
				client.post(
						API.UPDATE_FILE,
						params, new AsyncHttpResponseHandler() {

							@Override
							public void onSuccess(int statusCode,
									Header[] headers, byte[] responseBody) {

								try {
									CommonUtil.toastShort(
											getApplicationContext(), "上传成功");
									String str = new String(responseBody);
									JSONObject jo = new JSONObject(str);
									String url = jo.getString("result");
									Message msg = new Message();
									msg.what = 1;
									msg.obj = url;
									handler.sendMessage(msg);
								} catch (JSONException e) {
									handler.sendEmptyMessage(0);
								}
							}

							@Override
							public void onFailure(int statusCode,
									Header[] headers, byte[] responseBody,
									Throwable error) {

								handler.sendEmptyMessage(0);
							}
						});
			} else {
				CommonUtil.toastShort(getApplicationContext(), "文件不存在");
			}
			break;
		}
		}
		updateUIWithValidation();
	}
	private void updateUIWithValidation() {
		final boolean enabled = mMerchantId > 0
				&& !TextUtils.isEmpty(getItemValue(mMerchantKeys[1]))
				&& !TextUtils.isEmpty(getItemValue(mMerchantKeys[2]))
				&& (mMerchantGender == 0 || mMerchantGender == 1)
				&& !TextUtils.isEmpty(getItemValue(mMerchantKeys[4]))
				&& !TextUtils.isEmpty(getItemValue(mMerchantKeys[5]))
				&& !TextUtils.isEmpty(getItemValue(mMerchantKeys[6]))
				&& !TextUtils.isEmpty(getItemValue(mMerchantKeys[7]))
				&& null != mMerchantCity
				&& !TextUtils.isEmpty(getItemValue(mBankKeys[0]))
				&& !TextUtils.isEmpty(getItemValue(mBankKeys[1]))
				&& !TextUtils.isEmpty(getItemValue(mBankKeys[2]))
				&& !TextUtils.isEmpty(getItemValue(mBankKeys[3]))
				&& !TextUtils.isEmpty(getItemValue(mBankKeys[4]))
				&& (null != mChosenChannel && null != mChosenBilling);
		mApplySubmit.setEnabled(enabled);
	}
	public String getRealPathFromURI(Uri contentUri) {
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = this.managedQuery(contentUri, proj, null, null,
					null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} catch (Exception e) {
			return contentUri.getPath();
		}
	}

	/**
	 * set the item value by key
	 * 
	 * @param key
	 * @param value
	 */
	private void setItemValue(String key, String value) {
		LinearLayout item = (LinearLayout) mContainer.findViewWithTag(key);

		if (item == null) {
			item = (LinearLayout) mMerchantContainer_0.findViewWithTag(key);
		}
		TextView tvValue = (TextView) item
				.findViewById(R.id.apply_detail_value);
		tvValue.setText(value);
	}

	/**
	 * get the item value by key
	 * 
	 * @param key
	 * @return
	 */
	private String getItemValue(String key) {
		LinearLayout item = (LinearLayout) mContainer.findViewWithTag(key);
		TextView tvValue = (TextView) item
				.findViewById(R.id.apply_detail_value);
		return tvValue.getText().toString();
	}

	/**
	 * firstly init the merchant category with item keys, and after user select
	 * the merchant the values will be set
	 */
	@SuppressLint("NewApi")
	private void initMerchantDetailKeys() {
		// the first category
		mMerchantKeys = getResources().getStringArray(
				R.array.my_apply_detail_merchant_keys);

		mMerchantContainer_0.addView(getDetailItem(ITEM_CHOOSE,
				mMerchantKeys[0], null));
		mMerchantContainer_1.addView(getDetailItem(ITEM_EDIT, mMerchantKeys[1],
				null));
		mMerchantContainer_2.addView(getDetailItem(ITEM_EDIT, mMerchantKeys[2],
				null));

		View merchantGender = getDetailItem(ITEM_CHOOSE, mMerchantKeys[3], null);
		merchantGender.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(MyApplyDetail.this);
				final String[] items = getResources().getStringArray(R.array.apply_detail_gender);
				builder.setItems(items, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						setItemValue(mMerchantKeys[3], items[which]);
						mMerchantGender = which == 1 ? 0 : 1;
						updateUIWithValidation();
					}
				});
				builder.show();
			}
		});
		mMerchantContainer_1.addView(merchantGender);

		View merchantBirthday = getDetailItem(ITEM_CHOOSE, mMerchantKeys[4],
				null);
		merchantBirthday.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CommonUtil.showDatePicker(MyApplyDetail.this,
						mMerchantBirthday, new CommonUtil.OnDateSetListener() {
							@Override
							public void onDateSet(String date) {
								setItemValue(mMerchantKeys[4], date);
								updateUIWithValidation();
							}
						});
			}
		});
		mMerchantContainer_2.addView(merchantBirthday);
		mMerchantContainer_1.addView(getDetailItem(ITEM_EDIT, mMerchantKeys[5],
				null));
		mMerchantContainer_2.addView(getDetailItem(ITEM_EDIT, mMerchantKeys[6],
				null));
		mMerchantContainer_1.addView(getDetailItem(ITEM_EDIT, mMerchantKeys[7],
				null));

		View merchantCity = getDetailItem(ITEM_CHOOSE, mMerchantKeys[8], null);
		merchantCity.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyApplyDetail.this,
						CityProvinceActivity.class);
				intent.putExtra(SELECTED_PROVINCE, mMerchantProvince);
				intent.putExtra(SELECTED_CITY, mMerchantCity);
				startActivityForResult(intent, REQUEST_CHOOSE_CITY);
			}
		});
		mMerchantContainer_2.addView(merchantCity);
		mMerchantContainer_1.setBackground(null);
		mMerchantContainer_2.setBackground(null);

		// the second category
		mBankKeys = getResources().getStringArray(
				R.array.my_apply_detail_bank_keys);

		mCustomerContainer_1.addView(getDetailItem(ITEM_EDIT, mBankKeys[0],
				null));
		mCustomerContainer_2.addView(getDetailItem(ITEM_EDIT, mBankKeys[1],
				null));
		mCustomerContainer_1.addView(getDetailItem(ITEM_EDIT, mBankKeys[2],
				null));
		mCustomerContainer_2.addView(getDetailItem(ITEM_EDIT, mBankKeys[3],
				null));
		mCustomerContainer_1.addView(getDetailItem(ITEM_EDIT, mBankKeys[4],
				null));
		//
		// View chooseChannel = getDetailItem(ITEM_CHOOSE,
		// getString(R.string.apply_detail_channel), null);
		View chooseChannel = getDetailItem(ITEM_CHOOSE, mBankKeys[5], null);
		chooseChannel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyApplyDetail.this,
						ApplyChannelActivity.class);
				intent.putExtra(SELECTED_CHANNEL, mChosenChannel);
				intent.putExtra(SELECTED_BILLING, mChosenBilling);
				startActivityForResult(intent, REQUEST_CHOOSE_CHANNEL);
			}

		});
		mCustomerContainer_2.addView(chooseChannel);

	}

	/**
	 * set the item values after user select a merchant
	 * 
	 * @param merchant
	 */
	private void setMerchantDetailValues(MerchantForApply merchant) {
		setItemValue(mMerchantKeys[1], merchant.getLegalPersonName());
		setItemValue(mMerchantKeys[2], merchant.getTitle());
		setItemValue(mMerchantKeys[5], merchant.getLegalPersonCardId());
		setItemValue(mMerchantKeys[6], merchant.getPhone());
		CommonUtil.findCityById(this, merchant.getCityId(),
				 new CommonUtil.OnCityFoundListener() {
				 @Override
				 public void onCityFound(Province province, City city) {
				 mMerchantProvince = province;
				 mMerchantCity = city;
				 Log.e("--mMerchantCity--", mMerchantCity.getName());
				 setItemValue(mMerchantKeys[8], city.getName());
				 updateUIWithValidation();
				 }
				 });
		setItemValue(mBankKeys[0], merchant.getAccountBankName());
		setItemValue(mBankKeys[1], merchant.getAccountBankNum());
		setItemValue(mBankKeys[2], merchant.getBankOpenAccount());
		setItemValue(mBankKeys[3], merchant.getTaxRegisteredNo());
		setItemValue(mBankKeys[4], merchant.getOrganizationCodeNo());
	}

	/**
	 * start the {@link ApplyChooseActivity} to choose item
	 * 
	 * @param requestCode
	 *            handle the return item according to request code
	 * @param title
	 *            the started activity title
	 * @param selectedId
	 *            the id of the selected item
	 * @param items
	 *            the items to choose
	 */
	private void startChooseItemActivity(int requestCode, String title,
			int selectedId, ArrayList<ApplyChooseItem> items) {
		Intent intent = new Intent(MyApplyDetail.this,
				ApplyChooseActivity.class);
		intent.putExtra(CHOOSE_TITLE, title);
		intent.putExtra(SELECTED_ID, selectedId);
		intent.putExtra(CHOOSE_ITEMS, items);
		startActivityForResult(intent, requestCode);
	}

	/**
	 * set the customer's details after the first request returned
	 * 
	 * @param customerDetails
	 */
	private void setCustomerDetail(List<MyApplyMaterial> materials,
			List<MyApplyCustomerDetail> customerDetails) {
		if (null == materials || materials.size() <= 0)
			return;
		for (MyApplyMaterial material : materials) {
			mMaterials.put(material.getId(), material);
		}
		if (null != customerDetails && customerDetails.size() > 0) {
			for (MyApplyCustomerDetail customerDetail : customerDetails) {
				MyApplyMaterial material = mMaterials.get(customerDetail
						.getTargetId());
				if (null != material) {
					material.setValue(customerDetail.getValue());
				}
			}
		}
		int pic = 1;
		int txt = 1;

		for (final MyApplyMaterial material : mMaterials.values()) {

			switch (material.getTypes()) {
			case TYPE_TEXT:

				switch (txt) {
				case 1:

					mMaterialContainer_2_1.addView(getDetailItem(ITEM_EDIT,
							material.getName(), material.getValue()));

					break;

				case 2:
					mMaterialContainer_2_2.addView(getDetailItem(ITEM_EDIT,
							material.getName(), material.getValue()));

					break;
				}
				txt++;
				break;
			case TYPE_IMAGE:
				String imageName = material.getName();
				String imageUrl = material.getValue();
				if (!TextUtils.isEmpty(imageUrl)) {
					mImageNames.add(imageName);
					mImageUrls.add(imageUrl);

					switch (pic) {
					case 1:

						mMaterialContainer_1_1.addView(getDetailItem(ITEM_VIEW,
								imageName, imageUrl));

						break;

					case 2:
						mMaterialContainer_1_2.addView(getDetailItem(ITEM_VIEW,
								imageName, imageUrl));

						break;

					case 3:
						mMaterialContainer_1_3.addView(getDetailItem(ITEM_VIEW,
								imageName, imageUrl));

						break;
					}
				} else {

					switch (pic) {
					case 1:

						mMaterialContainer_1_1.addView(getDetailItem(
								ITEM_UPLOAD, imageName, imageUrl));

						break;

					case 2:
						mMaterialContainer_1_2.addView(getDetailItem(
								ITEM_UPLOAD, imageName, imageUrl));

						break;

					case 3:
						mMaterialContainer_1_3.addView(getDetailItem(
								ITEM_UPLOAD, imageName, imageUrl));

						break;
					}

				}
				pic++;
				break;
			case TYPE_BANK:
				View chooseBank = getDetailItem(ITEM_CHOOSE,
						material.getName(), null);
				chooseBank.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						mBankKey = material.getName();
						Intent intent = new Intent(MyApplyDetail.this,
								ApplyBankActivity.class);
						intent.putExtra(TERMINAL_ID, mTerminalId);
						intent.putExtra(SELECTED_BANK, mChosenBank);
						startActivityForResult(intent, REQUEST_CHOOSE_BANK);
					}
				});

				switch (txt) {
				case 1:

					mMaterialContainer_2_1.addView(chooseBank);

					break;

				case 2:
					mMaterialContainer_2_2.addView(chooseBank);

					break;
				}
				txt++;
				break;
			}
		}

	}


	private LinearLayout getDetailItem(int itemType, String key, String value) {
		LinearLayout item;
		switch (itemType) {
		case ITEM_EDIT:
			item = (LinearLayout) mInflater.inflate(
					R.layout.my_apply_detail_item_edit, null);
			break;
		case ITEM_CHOOSE:
			item = (LinearLayout) mInflater.inflate(
					R.layout.my_apply_detail_item_choose, null);
			break;
		case ITEM_UPLOAD:
			item = (LinearLayout) mInflater.inflate(
					R.layout.my_apply_detail_item_upload, null);
			break;
		case ITEM_VIEW:
			item = (LinearLayout) mInflater.inflate(
					R.layout.my_apply_detail_item_view, null);
			break;
		case ITEM_BLANK:
			return new LinearLayout(this);
		default:
			item = (LinearLayout) mInflater.inflate(
					R.layout.my_apply_detail_item_edit, null);
		}
		item.setTag(key);
		setupItem(item, itemType, key, value);
		return item;
	}

	private void setupItem(LinearLayout item, int itemType, final String key,
			final String value) {
		switch (itemType) {
		case ITEM_EDIT: {
			TextView tvKey = (TextView) item
					.findViewById(R.id.apply_detail_key);
			EditText etValue = (EditText) item
					.findViewById(R.id.apply_detail_value);
			etValue.addTextChangedListener(new TextWatcherAdapter() {
				public void afterTextChanged(final Editable gitDirEditText) {
					updateUIWithValidation();
				}
			});
			if (!TextUtils.isEmpty(key))
				tvKey.setText(key);
			if (!TextUtils.isEmpty(value))
				etValue.setText(value);
			break;
		}
		case ITEM_CHOOSE: {
			TextView tvKey = (TextView) item
					.findViewById(R.id.apply_detail_key);
			TextView tvValue = (TextView) item
					.findViewById(R.id.apply_detail_value);

			if (!TextUtils.isEmpty(key))
				tvKey.setText(key);
			if (!TextUtils.isEmpty(value))
				tvValue.setText(value);
			break;
		}
		case ITEM_UPLOAD: {
			TextView tvKey = (TextView) item
					.findViewById(R.id.apply_detail_key);
			final TextView tvValue = (TextView) item
					.findViewById(R.id.apply_detail_value);
			final ImageButton uploadingSuccess = (ImageButton) item
					.findViewById(R.id.apply_detail_view);

			if (!TextUtils.isEmpty(key))
				tvKey.setText(key);
			tvValue.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					uploadingTextView = tvValue;
					uploadingImageButton = uploadingSuccess;
					AlertDialog.Builder builder = new AlertDialog.Builder(
							MyApplyDetail.this);
					final String[] items = getResources().getStringArray(
							R.array.apply_detail_upload);
					builder.setItems(items,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									mUploadKey = key;
									switch (which) {
									case 0: {
										Intent intent = new Intent();
										if (Build.VERSION.SDK_INT < 19) {
											intent = new Intent(
													Intent.ACTION_GET_CONTENT);
											intent.setType("image/*");
										} else {
											intent = new Intent(
													Intent.ACTION_PICK,
													android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
										}
										startActivityForResult(intent,
												REQUEST_UPLOAD_IMAGE);
										break;
									}
									case 1: {
										String state = Environment
												.getExternalStorageState();
										if (state
												.equals(Environment.MEDIA_MOUNTED)) {
											Intent intent = new Intent(
													MediaStore.ACTION_IMAGE_CAPTURE);
											File outDir = Environment
													.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
											if (!outDir.exists()) {
												outDir.mkdirs();
											}
											File outFile = new File(outDir,
													System.currentTimeMillis()
															+ ".jpg");
											photoPath = outFile
													.getAbsolutePath();
											intent.putExtra(
													MediaStore.EXTRA_OUTPUT,
													Uri.fromFile(outFile));
											intent.putExtra(
													MediaStore.EXTRA_VIDEO_QUALITY,
													1);
											startActivityForResult(intent,
													REQUEST_TAKE_PHOTO);
										} else {
											CommonUtil
													.toastShort(
															MyApplyDetail.this,
															getString(R.string.toast_no_sdcard));
										}
										break;
									}
									}
								}
							});
					builder.show();

				}
			});
			break;
		}
		case ITEM_VIEW: {
			TextView tvKey = (TextView) item
					.findViewById(R.id.apply_detail_key);
			ImageButton ibView = (ImageButton) item
					.findViewById(R.id.apply_detail_view);
			mUploadUri = value;
			if (!TextUtils.isEmpty(key))
				tvKey.setText(key);
			ibView.setOnClickListener(new onWatchListener()

			);
		}
		}
	}

	private class onWatchListener implements View.OnClickListener {

		@Override
		public void onClick(View arg0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					MyApplyDetail.this);
			final String[] items = getResources().getStringArray(
					R.array.apply_detail_view);
			builder.setItems(items, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case 0: {

						AlertDialog.Builder build = new AlertDialog.Builder(
								MyApplyDetail.this);
						LayoutInflater factory = LayoutInflater
								.from(MyApplyDetail.this);
						final View textEntryView = factory.inflate(
								R.layout.show_view, null);
						// build.setTitle("自定义输入框");
						build.setView(textEntryView);
						final ImageView view = (ImageView) textEntryView
								.findViewById(R.id.imag);
						ImageCacheUtil.IMAGE_CACHE.get(mUploadUri, view);
						build.create().show();
						break;
					}
					case 1: {
						Intent intent = new Intent();
						if (Build.VERSION.SDK_INT < 19) {
							intent = new Intent(
									Intent.ACTION_GET_CONTENT);
							intent.setType("image/*");
						} else {
							intent = new Intent(
									Intent.ACTION_PICK,
									android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						}
						startActivityForResult(intent, REQUEST_UPLOAD_IMAGE);
						break;
					}
					case 2: {
						String state = Environment.getExternalStorageState();
						if (state.equals(Environment.MEDIA_MOUNTED)) {
							Intent intent = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							File outDir = Environment
									.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
							if (!outDir.exists()) {
								outDir.mkdirs();
							}
							File outFile = new File(outDir, System
									.currentTimeMillis() + ".jpg");
							photoPath = outFile.getAbsolutePath();
							intent.putExtra(MediaStore.EXTRA_OUTPUT,
									Uri.fromFile(outFile));
							intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
							startActivityForResult(intent, REQUEST_TAKE_PHOTO);
						} else {
							CommonUtil.toastShort(MyApplyDetail.this,
									getString(R.string.toast_no_sdcard));
						}
						break;
					}
					}
				}
			});
			builder.show();

		}

	}

}
