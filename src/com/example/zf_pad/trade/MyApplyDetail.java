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
import static com.example.zf_pad.fragment.Constants.ApplyIntent.SELECTED_CHANNEL_ID;
import static com.example.zf_pad.fragment.Constants.CityIntent.SELECTED_CITY;
import static com.example.zf_pad.fragment.Constants.CityIntent.SELECTED_PROVINCE;
import static com.example.zf_pad.fragment.Constants.TerminalIntent.TERMINAL_ID;

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
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zf_pad.MyApplication;
import com.example.zf_pad.R;
import com.example.zf_pad.entity.BankEntity.Bank;
import com.example.zf_pad.trade.common.CommonUtil;
import com.example.zf_pad.trade.common.HttpCallback;
import com.example.zf_pad.trade.common.TextWatcherAdapter;
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
import com.example.zf_pad.util.RegText;
import com.example.zf_pad.util.ScrollViewWithGView;
import com.example.zf_pad.util.StringUtil;
import com.example.zf_pad.util.TitleMenuUtil;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

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
	private MerchantForApply mMerchant = new MerchantForApply();
	private LayoutInflater mInflater;
	private TextView toPublic, toPrivate, mPosBrand, mPosModel, mSerialNum,
			mChannel, uploadingTextView;
	private String[] mMerchantKeys, mBankKeys;
	private LinearLayout mContainer, mMerchantContainer_0,
			mMerchantContainer_1, mMerchantContainer_2, mCustomerContainer_1,
			mCustomerContainer_2;
	private Button mApplySubmit;
	private int mMerchantId, mMerchantGender = 1, mTerminalId, mTerminalStatus;
	private Province mMerchantProvince;
	private City mMerchantCity;
	private String photoPath, mBankKey, mUploadKey, mMerchantBirthday,
			mUploadUri;
	private ImageButton uploadingImageButton;
	private Bank mChosenBank;
	private ApplyChannel mChosenChannel;
	private ApplyChannel.Billing mChosenBilling;

	private View clickView;
	private List<String> mImageUrls = new ArrayList<String>();
	private List<String> mImageNames = new ArrayList<String>();
	private List<MyApplyMaterial> myApplyMaterials1 = new ArrayList<MyApplyMaterial>();
	private List<MyApplyMaterial> myApplyMaterials2 = new ArrayList<MyApplyMaterial>();
	private LinkedHashMap<Integer, MyApplyMaterial> mMaterials = new LinkedHashMap<Integer, MyApplyMaterial>();
	private ScrollViewWithGView gridView, gridView_;
	private ApplyListAdapter1 adapter1;
	private ApplyListAdapter2 adapter2;

	private Boolean isBankName = false;
	private Boolean isShopName = false;
	private String shopName;
	private String bankCode;
	private int mPayChannelID = 0;
	private int supportRequirementType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_detail);
		new TitleMenuUtil(this, getString(R.string.title_apply_open)).show();
		mTerminalId = getIntent().getIntExtra(TERMINAL_ID, 0);

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
		mChannel = (TextView) findViewById(R.id.apply_detail_channel);

		gridView = (ScrollViewWithGView) findViewById(R.id.gridview);
		gridView_ = (ScrollViewWithGView) findViewById(R.id.gridview_);

		mContainer = (LinearLayout) findViewById(R.id.apply_detail_container);
		mMerchantContainer_0 = (LinearLayout) findViewById(R.id.mMerchantContainer_0);
		mMerchantContainer_1 = (LinearLayout) findViewById(R.id.mMerchantContainer_1);
		mMerchantContainer_2 = (LinearLayout) findViewById(R.id.mMerchantContainer_2);
		mCustomerContainer_1 = (LinearLayout) findViewById(R.id.mCustomerContainer_1);
		mCustomerContainer_2 = (LinearLayout) findViewById(R.id.mCustomerContainer_2);

		mApplySubmit = (Button) findViewById(R.id.apply_submit);

		mApplySubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				List<Map<String, Object>> totalParams = new ArrayList<Map<String, Object>>();

				Map<String, Object> params = new HashMap<String, Object>();
				params.put("terminalId", mTerminalId);
				params.put("status", mTerminalStatus == 2 ? 2 : 1);
				params.put("applyCustomerId", MyApplication.NewUser.getId());
				params.put("publicPrivateStatus", mApplyType);

				params.put("merchantId", mMerchantId);
				params.put("name", getItemValue(mMerchantKeys[1]));
				params.put("merchantName", getItemValue(mMerchantKeys[2]));
				params.put("sex", mMerchantGender);
				params.put("birthday", getItemValue(mMerchantKeys[4]));
				if (!RegText.isIdentityCard(getItemValue(mMerchantKeys[5]))) {
					CommonUtil.toastShort(MyApplyDetail.this, "身份证号格式不正确");
					return;
				}
				params.put("cardId", getItemValue(mMerchantKeys[5]));
				if (!RegText.isMobileNO(getItemValue(mMerchantKeys[6]))) {
					CommonUtil.toastShort(MyApplyDetail.this, "手机号码格式不正确");
					return;
				}
				params.put("phone", getItemValue(mMerchantKeys[6]));
				if (!RegText.isEmail(getItemValue(mMerchantKeys[7]))) {
					CommonUtil.toastShort(MyApplyDetail.this, "邮箱格式不正确");
					return;
				}
				params.put("email", getItemValue(mMerchantKeys[7]));
				params.put("cityId",
						null != mMerchantCity ? mMerchantCity.getId() : 0);

				params.put("bankNum", getItemValue(mBankKeys[0]));
				params.put("bankName", getItemValue(mBankKeys[1]));

				params.put("bank_name", getItemValue(mBankKeys[2]));
				if (mChosenBank != null)
					params.put("bankCode", mChosenBank.getNo());
				else
					params.put("bankCode", bankCode);
				if (mApplyType == APPLY_PUBLIC) {
					params.put("registeredNo", getItemValue(mBankKeys[3]));
					params.put("organizationNo", getItemValue(mBankKeys[4]));
				} else {
					params.put("registeredNo", "");
					params.put("organizationNo", "");
				}
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
						LinearLayout item = (LinearLayout) mContainer
								.findViewWithTag(key);
						String value = (String) item
								.getTag(R.id.apply_detail_key);
						material.setValue(value);
					} else if (material.getTypes() == TYPE_IMAGE) {
						String key = material.getName();
						LinearLayout item = (LinearLayout) mContainer
								.findViewWithTag(key);
						// if (item.findViewById(R.id.apply_detail_value) !=
						// null) {
						// material.setValue((String) item.findViewById(
						// R.id.apply_detail_value).getTag());
						// } else
						if (item.findViewById(R.id.apply_detail_view) != null) {
							material.setValue((String) item.findViewById(
									R.id.apply_detail_view).getTag());
						} else {
							System.out.println(item);
						}
					}
					// String value = (String) item
					// .getTag(R.id.apply_detail_key);
					// material.setValue(value);}
					if (TextUtils.isEmpty(material.getValue()))
						continue;
					// image types' value have been set in advance
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("key", material.getName());
					param.put("value", material.getValue());
					param.put("types", material.getTypes());
					param.put("openingRequirementId",
							material.getOpeningRequirementId());
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

		API.getApplyDetail(this, MyApplication.NewUser.getId(), mTerminalId,
				applyType, new HttpCallback<My_ApplyDetail>(this) {
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
							mChannel.setText(terminalDetail.getChannelName());
							mPayChannelID = terminalDetail.getChannelId();
							supportRequirementType = terminalDetail
									.getSupportRequirementType();
							if (supportRequirementType == 1) {
								toPrivate.setVisibility(View.GONE);
							} else if (supportRequirementType == 2) {

								mApplyType = APPLY_PRIVATE;
								toPublic.setVisibility(View.GONE);
								toPrivate.setBackgroundDrawable(getResources()
										.getDrawable(R.drawable.tab_bg));
							}
						}

						mMerchantContainer_0.removeAllViews();
						mMerchantContainer_1.removeAllViews();
						mMerchantContainer_2.removeAllViews();
						mCustomerContainer_1.removeAllViews();
						mCustomerContainer_2.removeAllViews();
						initMerchantDetailKeys();
						View merchantChoose_1 = mMerchantContainer_0
								.findViewWithTag(mMerchantKeys[0]);
						merchantChoose_1
								.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										startChooseItemActivity(
												REQUEST_CHOOSE_MERCHANT,
												getString(R.string.title_apply_choose_merchant),
												mMerchantId, mTerminalId);
									}
								});

						myApplyMaterials1 = new ArrayList<MyApplyMaterial>();

						myApplyMaterials2 = new ArrayList<MyApplyMaterial>();

						mMaterials.clear();

						// set the customer details
						setCustomerDetail(materials, customerDetails);

						adapter1 = new ApplyListAdapter1(myApplyMaterials1);
						adapter2 = new ApplyListAdapter2(myApplyMaterials2);
						adapter1.notifyDataSetChanged();
						adapter2.notifyDataSetChanged();
						gridView.setAdapter(adapter1);
						gridView_.setAdapter(adapter2);

						if (openingInfos != null) {

							mMerchantId = openingInfos.getMerchant_id();
							bankCode = openingInfos.getAccount_bank_code();
							setData(openingInfos);
						}
						updateUIWithValidation();
					}

					@Override
					public void onFailure(String message) {
						CommonUtil.toastShort(MyApplyDetail.this, message);
						finish();
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
		setItemValue(mMerchantKeys[0], openingInfos.getMerchant_name());
		setItemValue(mMerchantKeys[1], openingInfos.getName());
		setItemValue(mMerchantKeys[2], openingInfos.getMerchant_name());
		setItemValue(mMerchantKeys[3], items[openingInfos.getSex() % 2]);
		mMerchantGender = openingInfos.getSex() % 2;
		setItemValue(mMerchantKeys[4], openingInfos.getBirthday());
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

		setItemValue(mBankKeys[0], openingInfos.getAccount_bank_num());
		setItemValue(mBankKeys[1], openingInfos.getMerchant_name());
		setItemValue(mBankKeys[2], openingInfos.getBank_name());
		if (mApplyType == APPLY_PUBLIC) {
			setItemValue(mBankKeys[3], openingInfos.getTax_registered_no());
			setItemValue(mBankKeys[4], openingInfos.getOrganization_code_no());
		}
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
							if (data == null)
								mMerchant = new MerchantForApply();
							else
								mMerchant = data;
							setMerchantDetailValues(data);
							// mApplySubmit.setEnabled(true);
							updateUIWithValidation();
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
			mChosenBank = (Bank) data.getSerializableExtra(SELECTED_BANK);
			if (null != mChosenBank) {
				LinearLayout item = (LinearLayout) mContainer
						.findViewWithTag(mBankKeys[2]);
				item.setTag(R.id.apply_detail_key, mChosenBank.getNo());
				setItemValue(mBankKeys[2], mChosenBank.getName());
			}

			break;
		}
		case REQUEST_UPLOAD_IMAGE:
		case REQUEST_TAKE_PHOTO: {

			final Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					if (msg.what == 1) {

						String uri = (String) msg.obj;
						if (null != uploadingTextView) {
							// uploadingTextView.setBackgroundResource(R.drawable.check_it);
							// uploadingTextView
							// .setText("");
							// uploadingTextView.setClickable(false);
							uploadingTextView.setVisibility(View.GONE);
							uploadingImageButton.setVisibility(View.VISIBLE);
							uploadingImageButton.setTag(uri);
							uploadingImageButton
									.setOnClickListener(new onWatchListener());

						} else {

							clickView.setTag(uri);
							clickView.setOnClickListener(new onWatchListener());
						}
						for (MyApplyMaterial material : mMaterials.values()) {
							if (material.getTypes() == TYPE_IMAGE
									&& material.getName().equals(mUploadKey)) {
								material.setValue(uri);
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
					params.put("img", img);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				AsyncHttpClient client = new AsyncHttpClient();
				client.setTimeout(10000);// 设置超时时间
				client.setMaxConnections(10);
				client.post(API.APPLY_UPDATE_FILE + mTerminalId, params,
						new AsyncHttpResponseHandler() {

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
		if (mApplyType == APPLY_PUBLIC) {
			final boolean enabled = !TextUtils
					.isEmpty(getItemValue(mMerchantKeys[1]))
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
		} else {
			final boolean enabled = !TextUtils
					.isEmpty(getItemValue(mMerchantKeys[1]))
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
					&& (null != mChosenChannel && null != mChosenBilling);
			mApplySubmit.setEnabled(enabled);
		}
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
		if (item == null) {
			item = (LinearLayout) mMerchantContainer_1.findViewWithTag(key);
		}
		if (item == null) {
			item = (LinearLayout) mMerchantContainer_2.findViewWithTag(key);
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
		if (item == null) {
			item = (LinearLayout) mMerchantContainer_0.findViewWithTag(key);
		}
		if (item == null) {
			item = (LinearLayout) mMerchantContainer_1.findViewWithTag(key);
		}
		if (item == null) {
			item = (LinearLayout) mMerchantContainer_2.findViewWithTag(key);
		}
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
		isShopName = true;
		mMerchantContainer_2.addView(getDetailItem(ITEM_EDIT, mMerchantKeys[2],
				null));

		View merchantGender = getDetailItem(ITEM_CHOOSE, mMerchantKeys[3], null);
		merchantGender.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MyApplyDetail.this);
				final String[] items = getResources().getStringArray(
						R.array.apply_detail_gender);
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
		isBankName = true;
		mCustomerContainer_2.addView(getDetailItem(ITEM_EDIT, mBankKeys[1],
				null));
		View chooseBank = getDetailItem(ITEM_CHOOSE, mBankKeys[2], null);
		chooseBank.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MyApplyDetail.this,
						ApplyBankActivity.class);
				intent.putExtra(TERMINAL_ID, mTerminalId);
				intent.putExtra(SELECTED_BANK, mChosenBank);
				startActivityForResult(intent, REQUEST_CHOOSE_BANK);
			}
		});
		mCustomerContainer_1.addView(chooseBank);
		if (mApplyType == APPLY_PUBLIC) {
			mCustomerContainer_2.addView(getDetailItem(ITEM_EDIT, mBankKeys[3],
					null));
			mCustomerContainer_1.addView(getDetailItem(ITEM_EDIT, mBankKeys[4],
					null));
		}
		//
		// View chooseChannel = getDetailItem(ITEM_CHOOSE,
		// getString(R.string.apply_detail_channel), null);
		View chooseChannel = getDetailItem(ITEM_CHOOSE, mBankKeys[5], null);
		chooseChannel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyApplyDetail.this,
						ApplyChannelActivity.class);
				intent.putExtra(SELECTED_CHANNEL_ID, mPayChannelID);
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
		setItemValue(mBankKeys[1], merchant.getTitle());
		setItemValue(mBankKeys[2], merchant.getBankOpenAccount());
		if (mApplyType == APPLY_PUBLIC) {
			setItemValue(mBankKeys[3], merchant.getTaxRegisteredNo());
			setItemValue(mBankKeys[4], merchant.getOrganizationCodeNo());
		}
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
	private void startChooseItemActivity(final int requestCode,
			final String title, final int selectedId, int mTerminalId) {
		Intent intent = new Intent(MyApplyDetail.this,
				ApplyChooseActivity.class);
		intent.putExtra(CHOOSE_TITLE, title);
		intent.putExtra(SELECTED_ID, selectedId);
		intent.putExtra(CHOOSE_ITEMS, mTerminalId);
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

		for (final MyApplyMaterial material : mMaterials.values()) {

			switch (material.getTypes()) {

			case TYPE_TEXT:

				myApplyMaterials2.add(material);

				break;
			case TYPE_IMAGE:

				myApplyMaterials1.add(material);

				break;

			case TYPE_BANK:

				myApplyMaterials2.add(material);

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
			if (isBankName) {
				isBankName = false;
				etValue.setFocusable(false);
				etValue.setEnabled(false);

			}
			if (isShopName) {
				shopName = value;
				isShopName = false;
				etValue.addTextChangedListener(new TextWatcherAdapter() {
					public void afterTextChanged(final Editable gitDirEditText) {
						updateUIWithValidation();
						LinearLayout item = (LinearLayout) mContainer
								.findViewWithTag(mBankKeys[1]);
						EditText etBankName = (EditText) item
								.findViewById(R.id.apply_detail_value);
						etBankName.setText(gitDirEditText.toString());

					}
				});
			} else {
				etValue.addTextChangedListener(new TextWatcherAdapter() {
					public void afterTextChanged(final Editable gitDirEditText) {

						updateUIWithValidation();
					}
				});
			}
			if (!TextUtils.isEmpty(key))
				tvKey.setText(key);
			if (!TextUtils.isEmpty(value))
				if (isBankName) {
					etValue.setText(shopName);
				} else {
					etValue.setText(value);
				}
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
			ibView.setTag(value);
			if (!TextUtils.isEmpty(key))
				tvKey.setText(key);
			ibView.setOnClickListener(new onWatchListener());
		}
		}
	}

	private class onWatchListener implements View.OnClickListener {

		@Override
		public void onClick(View view) {
			clickView = view;
			final String uri = (String) view.getTag();
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
						ImageCacheUtil.IMAGE_CACHE.get(uri, view);
						build.create().show();
						break;
					}
					case 1: {
						Intent intent = new Intent();
						if (Build.VERSION.SDK_INT < 19) {
							intent = new Intent(Intent.ACTION_GET_CONTENT);
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

	class ApplyListAdapter1 extends BaseAdapter {

		private List<MyApplyMaterial> materialList1;

		ApplyListAdapter1(List<MyApplyMaterial> materialList1) {

			this.materialList1 = materialList1;
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public MyApplyMaterial getItem(int i) {
			return materialList1.get(i);
		}

		@Override
		public int getCount() {
			return materialList1.size();
		}

		@Override
		public View getView(final int i, View arg1, ViewGroup arg2) {

			String imageName = getItem(i).getName();
			String imageUrl = getItem(i).getValue();
			if (!TextUtils.isEmpty(imageUrl)) {
				mImageNames.add(imageName);
				mImageUrls.add(imageUrl);

				return getDetailItem(ITEM_VIEW, imageName, imageUrl);

			} else {

				return getDetailItem(ITEM_UPLOAD, imageName, imageUrl);

			}
		}

	};

	class ApplyListAdapter2 extends BaseAdapter {

		private List<MyApplyMaterial> materialList2;

		ApplyListAdapter2(List<MyApplyMaterial> materialList2) {

			this.materialList2 = materialList2;
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public MyApplyMaterial getItem(int i) {
			return materialList2.get(i);
		}

		@Override
		public int getCount() {
			return materialList2.size();
		}

		@Override
		public View getView(final int i, View arg1, ViewGroup arg2) {

			switch (getItem(i).getTypes()) {

			case TYPE_TEXT:

				return getDetailItem(ITEM_EDIT, getItem(i).getName(),
						getItem(i).getValue());

			case TYPE_BANK:

				View chooseBank = getDetailItem(ITEM_CHOOSE, getItem(i)
						.getName(), null);
				chooseBank.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent = new Intent(MyApplyDetail.this,
								ApplyBankActivity.class);
						intent.putExtra(TERMINAL_ID, mTerminalId);
						intent.putExtra(SELECTED_BANK, mChosenBank);
						startActivityForResult(intent, REQUEST_CHOOSE_BANK);
					}
				});
				return chooseBank;

			default:
				return null;
			}
		}

	};

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageStart(this.toString());
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(this.toString());
		MobclickAgent.onResume(this);
	}
}
