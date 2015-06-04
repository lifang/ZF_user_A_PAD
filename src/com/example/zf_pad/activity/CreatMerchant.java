package com.example.zf_pad.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zf_pad.BaseActivity;
import com.example.zf_pad.Config;
import com.example.zf_pad.MyApplication;
import com.epalmpay.userPad.R;
import com.example.zf_pad.entity.OrderDetailEntity;
import com.example.zf_pad.fragment.Mine_MyMerChant;
import com.example.zf_pad.trade.API;
import com.example.zf_pad.trade.CityProvinceActivity;
import com.example.zf_pad.trade.common.CommonUtil;
import com.example.zf_pad.trade.common.DialogUtil;
import com.example.zf_pad.trade.common.HttpCallback;
import com.example.zf_pad.trade.entity.City;
import com.example.zf_pad.trade.entity.Province;
import com.example.zf_pad.util.ImageCacheUtil;
import com.example.zf_pad.util.TitleMenuUtil;
import com.example.zf_pad.util.Tools;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

@SuppressLint("NewApi")
public class CreatMerchant extends BaseActivity implements OnClickListener {
	private TextView tv_adress;
	private int cityId;
	private EditText et_shopname, et_name, et_id_number, et_license_code,
			et_tax_id_number, et_certificate_no, et_bank, et_licencenum_bank;
	private Button btn_creat, btn_legal_photo, btn_license_photos,
			btn_legal_back_photos, btn_tax_regist, btn_person_photograph,
			btn_organization_code_photos, btn_bank_license_photos;
	private boolean iscamera = false;
	private File file;
	private String localCameraPath = "";
	public static final String IMAGE_UNSPECIFIED = "image/*";
	private String imageDir;
	private String localSelectPath;
	private int tag;
	private String[] imgPath = new String[7];
	private List<City> mCities = new ArrayList<City>();
	private int id;
	private String[] imgLocalPath = new String[7];
	public static boolean isdown = true;
	private RelativeLayout rl, rldown;
	private String title = "", legal_person_name = "",
			legal_person_card_id = "", business_license_no = "",
			tax_registered_no = "", organization_code_no = "",
			account_bank_name = "", bank_open_account = "",
			cardIdFrontPhotoPath = "", cardIdBackPhotoPath = "",
			bodyPhotoPath = "", licenseNoPicPath = "", taxNoPicPath = "",
			orgCodeNoPicPath = "", accountPicPath = "";
	// private AlertDialog dialog;
	// private AlertDialog.Builder builder;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				startActivity(new Intent(CreatMerchant.this, MainActivity.class));
				Config.MyTab = 3;
				break;
			case 1:
				Toast.makeText(getApplicationContext(), (String) msg.obj,
						Toast.LENGTH_SHORT).show();

				break;
			case 2: // 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
				Toast.makeText(getApplicationContext(),
						"no 3g or wifi content", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(getApplicationContext(), " refresh too much",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.creatdetail);
		if (EditMerchant.isEdit) {
			new TitleMenuUtil(CreatMerchant.this, "修改商户信息").show();
		} else {
			new TitleMenuUtil(CreatMerchant.this, "创建商户").show();
		}

		init();

	}

	@Override
	protected void onStart() {
		super.onStart();
		if (EditMerchant.isEdit) {
			getmerchantInfo();
			// EditMerchant.isEdit=false;
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Mine_MyMerChant.isFromItem = false;
		isdown = true;
		Log.e("isdown", String.valueOf(isdown));
	}

	private void getmerchantInfo() {
		Intent intent = getIntent();
		et_shopname.setText(intent.getExtras().getString("title"));
		et_name.setText(intent.getExtras().getString("legal_person_name"));
		et_id_number.setText(intent.getExtras().getString(
				"legal_person_card_id"));
		et_license_code.setText(intent.getExtras().getString(
				"business_license_no"));
		et_tax_id_number.setText(intent.getExtras().getString(
				"tax_registered_no"));
		et_certificate_no.setText(intent.getExtras().getString(
				"organization_code_no"));
		tv_adress.setText(intent.getExtras().getString("city"));
		et_bank.setText(intent.getExtras().getString("account_bank_name"));
		et_licencenum_bank.setText(intent.getExtras().getString(
				"bank_open_account"));
		imgPath[0] = intent.getExtras().getString("card_id_front_photo_path");
		imgPath[1] = intent.getExtras().getString("card_id_back_photo_path");
		imgPath[2] = intent.getExtras().getString("body_photo_path");
		imgPath[3] = intent.getExtras().getString("license_no_pic_path");
		imgPath[4] = intent.getExtras().getString("tax_no_pic_path");
		imgPath[5] = intent.getExtras().getString("org_code_no_pic_path");
		imgPath[6] = intent.getExtras().getString("account_pic_path");
		id = intent.getExtras().getInt("id");
		isdown = true;
		btn_legal_photo.setBackgroundResource(R.drawable.check_it);
		btn_legal_photo.setText("");
		btn_license_photos.setBackgroundResource(R.drawable.check_it);
		btn_license_photos.setText("");
		btn_legal_back_photos.setBackgroundResource(R.drawable.check_it);
		btn_legal_back_photos.setText("");
		btn_tax_regist.setBackgroundResource(R.drawable.check_it);
		btn_tax_regist.setText("");
		btn_person_photograph.setBackgroundResource(R.drawable.check_it);
		btn_person_photograph.setText("");
		btn_organization_code_photos.setBackgroundResource(R.drawable.check_it);
		btn_organization_code_photos.setText("");
		btn_bank_license_photos.setBackgroundResource(R.drawable.check_it);
		btn_bank_license_photos.setText("");
		btn_creat.setText("修改");
		/*
		 * isdown=true; if(!Tools.isConnect(getApplicationContext())){
		 * CommonUtil.toastShort(getApplicationContext(), "网络异常"); return; }
		 * Intent intent=getIntent(); id=intent.getIntExtra("position", 0);
		 * MyApplication.getInstance().getClient().post(Config.MERCHANTINFO+id,
		 * new AsyncHttpResponseHandler() { private Dialog loadingDialog;
		 * 
		 * @Override public void onStart() { super.onStart(); loadingDialog =
		 * DialogUtil.getLoadingDialg(CreatMerchant.this); loadingDialog.show();
		 * }
		 * 
		 * @Override public void onFinish() { super.onFinish();
		 * loadingDialog.dismiss(); }
		 * 
		 * @Override public void onSuccess(int statusCode, Header[] headers,
		 * byte[] responseBody) { String responseMsg = new String(responseBody)
		 * .toString(); Log.e("responseMsg", responseMsg); Gson gson = new
		 * Gson();
		 * 
		 * JSONObject jsonobject = null; String code = null; try { jsonobject =
		 * new JSONObject(responseMsg); code = jsonobject.getString("code"); int
		 * a =jsonobject.getInt("code"); if(a==Config.CODE){ JSONObject
		 * result=jsonobject.getJSONObject("result"); if(result.length()!=17){
		 * CommonUtil.toastShort(getApplicationContext(), "服务器返回数据不完全"); return;
		 * } title=result.getString("title");
		 * legal_person_name=result.getString("legal_person_name");
		 * legal_person_card_id=result.getString("legal_person_card_id");
		 * business_license_no=result.getString("business_license_no");
		 * tax_registered_no=result.getString("tax_registered_no");
		 * organization_code_no=result.getString("organization_code_no");
		 * account_bank_name=result.getString("account_bank_name");
		 * bank_open_account=result.getString("bank_open_account");
		 * tv_shopname.setText(result.getString("title"));
		 * tv_name.setText(result.getString("legal_person_name"));
		 * tv_id_number.setText(result.getString("legal_person_card_id"));
		 * tv_license_code.setText(result.getString("business_license_no"));
		 * tv_tax_id_number.setText(result.getString("tax_registered_no"));
		 * tv_certificate_no.setText(result.getString("organization_code_no"));
		 * tv_bank.setText(result.getString("account_bank_name"));
		 * tv_licencenum_bank.setText(result.getString("bank_open_account"));
		 * et_shopname.setText(result.getString("title"));
		 * et_name.setText(result.getString("legal_person_name"));
		 * et_id_number.setText(result.getString("legal_person_card_id"));
		 * et_license_code.setText(result.getString("business_license_no"));
		 * et_tax_id_number.setText(result.getString("tax_registered_no"));
		 * et_certificate_no.setText(result.getString("organization_code_no"));
		 * tv_adress.setText(findcity(result.getInt("id")));
		 * et_bank.setText(result.getString("account_bank_name"));
		 * et_licencenum_bank.setText(result.getString("bank_open_account"));
		 * 
		 * imgPath[2]=result.getString("body_photo_path");
		 * imgPath[3]=result.getString("license_no_pic_path"); imgPath[4]
		 * =result.getString("tax_no_pic_path");
		 * imgPath[5]=result.getString("org_code_no_pic_path");
		 * imgPath[6]=result.getString("account_pic_path");
		 * //imgPath[0]=result.getString("card_id_front_photo_path");
		 * //imgPath[1] =result.getString("card_id_back_photo_path");
		 * btn_legal_photo.setBackgroundResource(R.drawable.check_it);
		 * btn_legal_photo.setText("");
		 * btn_license_photos.setBackgroundResource(R.drawable.check_it);
		 * btn_license_photos.setText("");
		 * btn_legal_back_photos.setBackgroundResource(R.drawable.check_it);
		 * btn_legal_back_photos.setText("");
		 * btn_tax_regist.setBackgroundResource(R.drawable.check_it);
		 * btn_tax_regist.setText("");
		 * btn_person_photograph.setBackgroundResource(R.drawable.check_it);
		 * btn_person_photograph.setText("");
		 * btn_organization_code_photos.setBackgroundResource
		 * (R.drawable.check_it); btn_organization_code_photos.setText("");
		 * btn_bank_license_photos.setBackgroundResource(R.drawable.check_it);
		 * btn_bank_license_photos.setText(""); btn_creat.setText("编辑"); } else{
		 * code = jsonobject.getString("message");
		 * Toast.makeText(getApplicationContext(), code, 1000).show();
		 * 
		 * } } catch (JSONException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * }
		 * 
		 * @Override public void onFailure(int statusCode, Header[] headers,
		 * byte[] responseBody, Throwable error) { // TODO Auto-generated method
		 * stub
		 * 
		 * } });
		 */}

	protected String findcity(int id) {
		// TODO Auto-generated method stub
		String a = "苏州";
		List<Province> provinces = CommonUtil
				.readProvincesAndCities(CreatMerchant.this);
		for (Province province : provinces) {
			List<City> cities = province.getCities();

			mCities.addAll(cities);

		}
		for (City cc : mCities) {
			if (cc.getId() == id) {
				a = cc.getName();
			}
		}
		return a;
	}

	private void init() {
		/*
		 * tv_shopname=(TextView) findViewById(R.id.tv_shopname);
		 * tv_name=(TextView) findViewById(R.id.tv_name);
		 * tv_id_number=(TextView) findViewById(R.id.tv_id_number);
		 * tv_license_code=(TextView) findViewById(R.id.tv_license_code);
		 * tv_tax_id_number=(TextView) findViewById(R.id.tv_tax_id_number);
		 * tv_certificate_no=(TextView) findViewById(R.id.tv_certificate_no);
		 * tv_bank=(TextView) findViewById(R.id.tv_bank);
		 * tv_licencenum_bank=(TextView) findViewById(R.id.tv_licencenum_bank);
		 */
		btn_legal_photo = (Button) findViewById(R.id.btn_legal_photo);
		btn_license_photos = (Button) findViewById(R.id.btn_license_photos);
		btn_legal_back_photos = (Button) findViewById(R.id.btn_legal_back_photos);
		btn_tax_regist = (Button) findViewById(R.id.btn_tax_regist);
		btn_person_photograph = (Button) findViewById(R.id.btn_person_photograph);
		btn_organization_code_photos = (Button) findViewById(R.id.btn_organization_code_photos);
		btn_bank_license_photos = (Button) findViewById(R.id.btn_bank_license_photos);
		btn_creat = (Button) findViewById(R.id.btn_creat);
		et_shopname = (EditText) findViewById(R.id.et_shopname);
		et_name = (EditText) findViewById(R.id.et_name);
		et_id_number = (EditText) findViewById(R.id.et_id_number);
		et_license_code = (EditText) findViewById(R.id.et_license_code);
		et_tax_id_number = (EditText) findViewById(R.id.et_tax_id_number);
		et_certificate_no = (EditText) findViewById(R.id.et_certificate_no);
		et_bank = (EditText) findViewById(R.id.et_bank);
		et_licencenum_bank = (EditText) findViewById(R.id.et_licencenum_bank);
		tv_adress = (TextView) findViewById(R.id.tv_adress);
		tv_adress.setOnClickListener(this);
		btn_creat.setOnClickListener(this);
		btn_legal_photo.setOnClickListener(this);
		btn_license_photos.setOnClickListener(this);
		btn_legal_back_photos.setOnClickListener(this);
		btn_tax_regist.setOnClickListener(this);
		btn_person_photograph.setOnClickListener(this);
		btn_organization_code_photos.setOnClickListener(this);
		btn_bank_license_photos.setOnClickListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case com.example.zf_pad.fragment.Constants.ApplyIntent.REQUEST_CHOOSE_CITY:
			if (CityProvinceActivity.isClickconfirm) {
				City mMerchantCity = (City) data
						.getSerializableExtra(com.example.zf_pad.fragment.Constants.CityIntent.SELECTED_CITY);
				cityId = mMerchantCity.getId();
				tv_adress.setText(mMerchantCity.getName());
				CityProvinceActivity.isClickconfirm = false;
			}

			break;
		case 1001:
			Log.e("localCameraPath", String.valueOf(localCameraPath));
			switch (tag) {
			case 1:
				try {
					imgLocalPath[0] = localCameraPath;
					uploadFile(localCameraPath, tag, btn_legal_photo);

					// uploadFile(localSelectPath);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/*
				 * btn_legal_photo.setBackgroundResource(R.drawable.check_it);
				 * btn_legal_photo.setText(""); imgPath[0]=localCameraPath;
				 */

				break;
			case 2:
				try {
					imgLocalPath[1] = localCameraPath;
					uploadFile(localCameraPath, tag, btn_license_photos);

					// uploadFile(localSelectPath);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/*
				 * btn_license_photos.setBackgroundResource(R.drawable.check_it);
				 * btn_license_photos.setText(""); imgPath[1]=localCameraPath;
				 */

				break;
			case 3:
				try {
					imgLocalPath[2] = localCameraPath;
					uploadFile(localCameraPath, tag, btn_legal_back_photos);

					// uploadFile(localSelectPath);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/*
				 * btn_legal_back_photos.setBackgroundResource(R.drawable.check_it
				 * ); btn_legal_back_photos.setText("");
				 * imgPath[2]=localCameraPath;
				 */

				break;
			case 4:
				try {
					imgLocalPath[3] = localCameraPath;
					uploadFile(localCameraPath, tag, btn_tax_regist);

					// uploadFile(localSelectPath);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/*
				 * btn_tax_regist.setBackgroundResource(R.drawable.check_it);
				 * btn_tax_regist.setText(""); imgPath[3]=localCameraPath;
				 */

				break;
			case 5:
				try {
					imgLocalPath[4] = localCameraPath;
					uploadFile(localCameraPath, tag, btn_person_photograph);

					// uploadFile(localSelectPath);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/*
				 * btn_person_photograph.setBackgroundResource(R.drawable.check_it
				 * ); btn_person_photograph.setText("");
				 * imgPath[4]=localCameraPath;
				 */

				break;
			case 6:
				try {
					imgLocalPath[5] = localCameraPath;
					uploadFile(localCameraPath, tag,
							btn_organization_code_photos);

					// uploadFile(localSelectPath);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/*
				 * btn_organization_code_photos.setBackgroundResource(R.drawable.
				 * check_it); btn_organization_code_photos.setText("");
				 * imgPath[5]=localCameraPath;
				 */

				break;
			case 7:
				try {
					imgLocalPath[6] = localCameraPath;
					uploadFile(localCameraPath, tag, btn_bank_license_photos);

					// uploadFile(localSelectPath);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/*
				 * btn_bank_license_photos.setBackgroundResource(R.drawable.check_it
				 * ); btn_bank_license_photos.setText("");
				 * imgPath[6]=localCameraPath;
				 */

				break;
			default:
				break;
			}
			break;
		case 1002:
			if (data != null) {
				Uri selectedImage = data.getData();
				if (selectedImage != null) {
					Cursor cursor = getContentResolver().query(selectedImage,
							null, null, null, null);
					cursor.moveToFirst();
					int columnIndex = cursor.getColumnIndex("_data");
					localSelectPath = cursor.getString(columnIndex);
					Log.e("localSelectPath", localSelectPath);
					cursor.close();
					switch (tag) {
					case 1:
						try {
							imgLocalPath[0] = localSelectPath;
							uploadFile(localSelectPath, tag, btn_legal_photo);

							// uploadFile(localSelectPath);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					case 2:
						try {
							imgLocalPath[1] = localSelectPath;
							uploadFile(localSelectPath, tag, btn_license_photos);

							// uploadFile(localSelectPath);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						/*
						 * btn_license_photos.setBackgroundResource(R.drawable.
						 * check_it); btn_license_photos.setText("");
						 * imgPath[1]=localSelectPath;
						 */
						break;
					case 3:
						try {
							imgLocalPath[2] = localSelectPath;
							uploadFile(localSelectPath, tag,
									btn_legal_back_photos);

							// uploadFile(localSelectPath);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						/*
						 * btn_legal_back_photos.setBackgroundResource(R.drawable
						 * .check_it); btn_legal_back_photos.setText("");
						 * imgPath[2]=localSelectPath;
						 */
						break;
					case 4:
						try {
							imgLocalPath[3] = localSelectPath;
							uploadFile(localSelectPath, tag, btn_tax_regist);

							// uploadFile(localSelectPath);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						/*
						 * btn_tax_regist.setBackgroundResource(R.drawable.check_it
						 * ); btn_tax_regist.setText("");
						 * imgPath[3]=localSelectPath;
						 */
						break;
					case 5:
						try {
							imgLocalPath[4] = localSelectPath;
							uploadFile(localSelectPath, tag,
									btn_person_photograph);

							// uploadFile(localSelectPath);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						/*
						 * btn_person_photograph.setBackgroundResource(R.drawable
						 * .check_it); btn_person_photograph.setText("");
						 * imgPath[4]=localSelectPath;
						 */
						break;
					case 6:
						try {
							imgLocalPath[5] = localSelectPath;
							uploadFile(localSelectPath, tag,
									btn_organization_code_photos);

							// uploadFile(localSelectPath);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						/*
						 * btn_organization_code_photos.setBackgroundResource(R.
						 * drawable.check_it);
						 * btn_organization_code_photos.setText("");
						 * imgPath[5]=localSelectPath;
						 */
						break;
					case 7:
						try {
							imgLocalPath[6] = localSelectPath;
							uploadFile(localSelectPath, tag,
									btn_bank_license_photos);

							// uploadFile(localSelectPath);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						/*
						 * btn_bank_license_photos.setBackgroundResource(R.drawable
						 * .check_it); btn_bank_license_photos.setText("");
						 * imgPath[6]=localSelectPath;
						 */
						break;
					default:
						break;
					}
					if (localSelectPath == null
							|| localSelectPath.equals("null")) {
						Toast.makeText(CreatMerchant.this, "未取到图片！", 1000)
								.show();
					}
					return;
				}
			}
			break;
		default:
			break;
		}
	}

	private void uploadFile(String path, final int tag, final Button btn)
			throws Exception {
		File file = new File(path);
		if (file.exists() && file.length() > 0) {
			File fileImg = new File(localSelectPath);
			RequestParams params = new RequestParams();
			params.put("fileImg", fileImg);
			MyApplication
					.getInstance()
					.getClient()
					.post(API.UPDATE_FILE, params,
							new AsyncHttpResponseHandler() {
								private Dialog loadingDialog;

								@Override
								public void onStart() {
									super.onStart();
									loadingDialog = DialogUtil
											.getLoadingDialg(CreatMerchant.this);
									loadingDialog.show();
								}

								@Override
								public void onFinish() {
									super.onFinish();
									loadingDialog.dismiss();
								}

								@Override
								public void onSuccess(int statusCode,
										Header[] headers, byte[] responseBody) {
									String responseMsg = new String(
											responseBody).toString();
									String code = null;
									try {
										JSONObject jsonobject = new JSONObject(
												responseMsg);
										code = jsonobject.getString("code");
										int a = jsonobject.getInt("code");
										if (a == Config.CODE) {
											CommonUtil.toastShort(
													getApplicationContext(),
													"图片上传成功");
											btn.setBackgroundResource(R.drawable.check_it);
											btn.setText("");
											// String
											// str=jsonobject.getJSONObject("result").getString("filePath");
											String str = jsonobject
													.getString("result");
											// imgPath[tag]=jsonobject.getJSONObject("result").getString("filePath");
											int cc = tag;
											cc = cc - 1;
											imgPath[cc] = jsonobject
													.getString("result");
											Log.e("tag", String.valueOf(tag));
											Log.e("imgPath", imgPath[cc]);
										} else {
											code = jsonobject
													.getString("message");
											Toast.makeText(
													getApplicationContext(),
													code, 1000).show();
										}
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								}

								@Override
								public void onFailure(int statusCode,
										Header[] headers, byte[] responseBody,
										Throwable error) {
									System.out.println("-onFailure---");
									Log.e("print", "-onFailure---" + error);
									Toast.makeText(CreatMerchant.this,
											String.valueOf(error), 1000).show();

								}

								@Override
								public void onRetry(int retryNo) {
									// TODO Auto-generated method stub
									super.onRetry(5);
								}
							});
		} else {
			CommonUtil.toastShort(getApplicationContext(), "文件不存在");
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_adress:
			Intent intent = new Intent(CreatMerchant.this,
					CityProvinceActivity.class);
			startActivityForResult(
					intent,
					com.example.zf_pad.fragment.Constants.ApplyIntent.REQUEST_CHOOSE_CITY);
			break;
		case R.id.btn_creat:
			if (EditMerchant.isEdit) {

				/*
				 * if(isEdit){ //initData(); //isEdit=false;
				 * //CommonUtil.toastShort(getApplicationContext(), "开始编辑");
				 * //return; }
				 */
				changeMerchantInfo();
			} else {

				/*
				 * for(int i=0;i<imgPath.length;i++){ if(imgPath[i].equals("")){
				 * Toast.makeText(getApplicationContext(), "有图片未上传",
				 * Toast.LENGTH_SHORT).show(); return; } }
				 */
				// handler.sendEmptyMessage(0);
				sumbitMerchantInfo();
			}

			break;
		case R.id.btn_legal_photo:
			tag = 1;
			/*
			 * if(isEdit){ openimg(tag); } else{
			 */
			showchooseDialog(btn_legal_photo, tag);
			// }

			break;
		case R.id.btn_license_photos:
			tag = 2;
			/*
			 * if(isEdit){ openimg(tag); } else{
			 */
			showchooseDialog(btn_license_photos, tag);
			// }
			break;
		case R.id.btn_legal_back_photos:
			tag = 3;
			/*
			 * if(isEdit){ openimg(tag); } else{
			 */
			showchooseDialog(btn_legal_back_photos, tag);
			// }
			break;
		case R.id.btn_tax_regist:
			tag = 4;
			/*
			 * if(isEdit){ openimg(tag); } else{
			 */
			showchooseDialog(btn_tax_regist, tag);
			// }
			break;
		case R.id.btn_person_photograph:
			tag = 5;
			/*
			 * if(isEdit){ openimg(tag); } else{
			 */
			showchooseDialog(btn_person_photograph, tag);
			// }
			break;
		case R.id.btn_organization_code_photos:
			tag = 6;
			/*
			 * if(isEdit){ openimg(tag); } else{
			 */
			showchooseDialog(btn_organization_code_photos, tag);
			// }
			break;
		case R.id.btn_bank_license_photos:
			tag = 7;
			/*
			 * if(isEdit){ openimg(tag); } else{
			 */
			showchooseDialog(btn_bank_license_photos, tag);
			// }
			break;

		default:
			break;
		}

	}

	private void showchooseorseeDialog() {
		// TODO Auto-generated method stub

	}

	private void changeMerchantInfo() {
		if (!Tools.isConnect(getApplicationContext())) {
			CommonUtil.toastShort(getApplicationContext(), "网络异常");
			return;
		}
		String title = et_shopname.getText().toString();
		String legalPersonName = et_name.getText().toString();
		String legalPersonCardId = et_id_number.getText().toString();
		String businessLicenseNo = et_license_code.getText().toString();
		String taxRegisteredNo = et_tax_id_number.getText().toString();
		String organizationCodeNo = et_certificate_no.getText().toString();

		String accountBankName = et_bank.getText().toString();
		String bankOpenAccount = et_licencenum_bank.getText().toString();
		String cardIdFrontPhotoPath = imgPath[0];
		String cardIdBackPhotoPath = imgPath[1];
		String bodyPhotoPath = imgPath[2];
		String licenseNoPicPath = imgPath[3];
		String taxNoPicPath = imgPath[4];
		String orgCodeNoPicPath = imgPath[5];
		String accountPicPath = imgPath[6];
		API.updatemerchant(CreatMerchant.this, title, legalPersonName,
				legalPersonCardId, businessLicenseNo, taxRegisteredNo,
				organizationCodeNo, cityId, accountBankName, bankOpenAccount,
				cardIdFrontPhotoPath, cardIdBackPhotoPath, bodyPhotoPath,
				licenseNoPicPath, taxNoPicPath, orgCodeNoPicPath,
				accountPicPath, id, new HttpCallback(CreatMerchant.this) {

					@Override
					public void onSuccess(Object data) {
						Toast.makeText(getApplicationContext(), "修改成功", 1000)
								.show();
						finish();
					}

					@Override
					public TypeToken getTypeToken() {
						// TODO Auto-generated method stub
						return null;
					}
				});

	}

	private void showchooseDialog(Button btn, final int tag) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(
				CreatMerchant.this);
		LayoutInflater factory = LayoutInflater.from(this);
		final View textEntryView = factory.inflate(R.layout.choosedialog, null);
		// builder.setTitle("自定义输入框");
		builder.setView(textEntryView);
		final Button seeimg = (Button) textEntryView.findViewById(R.id.seeimg);
		final View line_one = textEntryView.findViewById(R.id.line_one);
		final Button choosealbum = (Button) textEntryView
				.findViewById(R.id.choosealbum);
		final Button choosecamera = (Button) textEntryView
				.findViewById(R.id.choosecamera);

		// final AlertDialog dialog = builder.show();
		// dialog=builder.show();
		if (btn.getText().toString().equals("")) {
			seeimg.setVisibility(View.VISIBLE);
			line_one.setVisibility(View.VISIBLE);
		}

		seeimg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isdown) {
					
					openimg(tag);
				} else {
				
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(
							Uri.parse("file://" + imgLocalPath[tag - 1]),
							"image/*");
					startActivity(intent);
				}
				// dialog.dismiss();
				// builder.show().dismiss();
			}
		});
		choosealbum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				iscamera = false;
				choosealbum.setBackgroundColor(Color.GRAY);
				choosecamera.setBackgroundColor(Color.WHITE);
				// dialog.dismiss();
			}
		});
		choosecamera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				iscamera = true;
				choosecamera.setBackgroundColor(Color.GRAY);
				choosealbum.setBackgroundColor(Color.WHITE);
				// dialog.dismiss();
			}
		});
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				if (iscamera) {
					opencamera();
				} else {
					openAlbum();
				}
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		builder.create().show();
	}

	protected void openimg(int tag) {
		AlertDialog.Builder build = new AlertDialog.Builder(CreatMerchant.this);
		LayoutInflater factory = LayoutInflater.from(this);
		final View textEntryView = factory.inflate(R.layout.img, null);
		// build.setTitle("自定义输入框");
		build.setView(textEntryView);
		final ImageView view = (ImageView) textEntryView
				.findViewById(R.id.imag);
		int ppp = tag - 1;
		ImageCacheUtil.IMAGE_CACHE.get(imgPath[ppp], view);
		build.create().show();
	}

	protected void openAlbum() {
		Intent intent;
		if (Build.VERSION.SDK_INT < 19) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
		} else {
			intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		startActivityForResult(intent, 1002);

	}

	protected void opencamera() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File dir = new File(Environment.getExternalStorageDirectory()
				+ "/uploadFile/user/8/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		file = new File(dir, String.valueOf(System.currentTimeMillis())
				+ ".jpg");
		localCameraPath = file.getPath();
		Log.e("", "getPath:" + file.getPath());
		// Log.e("localCameraPath22222", String.valueOf(file.getPath()));
		Uri imageUri = Uri.fromFile(file);
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(openCameraIntent, 1001);

	}

	private void sumbitMerchantInfo() {

		String title = et_shopname.getText().toString();
		String legalPersonName = et_name.getText().toString();
		String legalPersonCardId = et_id_number.getText().toString();
		String businessLicenseNo = et_license_code.getText().toString();
		String taxRegisteredNo = et_tax_id_number.getText().toString();
		String organizationCodeNo = et_certificate_no.getText().toString();
		String accountBankName = et_bank.getText().toString();
		String bankOpenAccount = et_licencenum_bank.getText().toString();
		cardIdFrontPhotoPath = imgPath[0];
		cardIdBackPhotoPath = imgPath[1];
		bodyPhotoPath = imgPath[2];
		licenseNoPicPath = imgPath[3];
		taxNoPicPath = imgPath[4];
		orgCodeNoPicPath = imgPath[5];
		accountPicPath = imgPath[6];
		if (title.equals("")) {
			CommonUtil.toastShort(getApplicationContext(), "请输入店铺名称");
			return;
		}
		if (legalPersonName.equals("")) {
			CommonUtil.toastShort(getApplicationContext(), "请输入商户法人姓名");
			return;
		}
		if (legalPersonCardId.equals("")) {
			CommonUtil.toastShort(getApplicationContext(), "请输入法人身份证号");
			return;
		}
		if (businessLicenseNo.equals("")) {
			CommonUtil.toastShort(getApplicationContext(), "请输入营业执照登记号");
			return;
		}
		if (taxRegisteredNo.equals("")) {
			CommonUtil.toastShort(getApplicationContext(), "请输入税务证号");
			return;
		}
		if (organizationCodeNo.equals("")) {
			CommonUtil.toastShort(getApplicationContext(), "请输入组织机构代码证号");
			return;
		}
		if (tv_adress.getText().toString().equals("")) {
			CommonUtil.toastShort(getApplicationContext(), "请选择商户所在地");
			return;
		}
		if (accountBankName.equals("")) {
			CommonUtil.toastShort(getApplicationContext(), "请输入开户银行");
			return;
		}
		if (bankOpenAccount.equals("")) {
			CommonUtil.toastShort(getApplicationContext(), "请输入银行开户许可证号");
			return;
		}
		if (cardIdFrontPhotoPath == null) {
			CommonUtil.toastShort(getApplicationContext(), "请上传商户法人身份证正面照片");
			return;
		}
		if (cardIdBackPhotoPath == null) {
			CommonUtil.toastShort(getApplicationContext(), "请上传营业执照照片");
			return;
		}
		if (bodyPhotoPath == null) {
			CommonUtil.toastShort(getApplicationContext(), "请上传商户法人身份证背面照片");
			return;
		}
		if (licenseNoPicPath == null) {
			CommonUtil.toastShort(getApplicationContext(), "请上传税务登记证照片");
			return;
		}
		if (taxNoPicPath == null) {
			CommonUtil.toastShort(getApplicationContext(), "请上传商户法人上半身照片");
			return;
		}
		if (orgCodeNoPicPath == null) {
			CommonUtil.toastShort(getApplicationContext(), "请上传组织机构代码证照片");
			return;
		}
		if (accountPicPath == null) {
			CommonUtil.toastShort(getApplicationContext(), "请上传开户银行许可证照片");
			return;
		}
		int customerId = MyApplication.NewUser.getId();
		API.insertmerchant(CreatMerchant.this, title, legalPersonName,
				legalPersonCardId, businessLicenseNo, taxRegisteredNo,
				organizationCodeNo, cityId, accountBankName, bankOpenAccount,
				cardIdFrontPhotoPath, cardIdBackPhotoPath, bodyPhotoPath,
				licenseNoPicPath, taxNoPicPath, orgCodeNoPicPath,
				accountPicPath, customerId,
				new HttpCallback(CreatMerchant.this) {

					@Override
					public void onSuccess(Object data) {
						// finish();
						Toast.makeText(getApplicationContext(), "创建商户成功", 1000)
								.show();
						handler.sendEmptyMessage(0);
					}

					@Override
					public TypeToken getTypeToken() {
						// TODO Auto-generated method stub
						return null;
					}
				});
	}

}
