package com.example.zf_pad.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zf_pad.BaseActivity;
import com.example.zf_pad.Config;
import com.example.zf_pad.MyApplication;
import com.example.zf_pad.R;
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

public class CreatMerchant extends BaseActivity implements OnClickListener{
	private TextView tv_adress;
	private int cityId;
	private EditText et_shopname,et_name,et_id_number,et_license_code,et_tax_id_number,
	et_certificate_no,et_bank,et_licencenum_bank;
	private Button btn_creat,btn_legal_photo,btn_license_photos,btn_legal_back_photos,
	btn_tax_regist,btn_person_photograph,btn_organization_code_photos,btn_bank_license_photos;
	private boolean iscamera=false;
	private File file;
	private String localCameraPath = "";
	public static final String IMAGE_UNSPECIFIED = "image/*";
	 private String imageDir;
	 private String localSelectPath;
	 private int tag;
	 private String[] imgPath=new String[7];
	 private List<City> mCities = new ArrayList<City>();
	 private int id;
	 private String[] imgLocalPath=new String[7];
	 public static boolean isdown=false;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.creatdetail);
	
	
}
@Override
protected void onStart() {
	super.onStart();
	if(Mine_MyMerChant.isFromItem){
		new TitleMenuUtil(CreatMerchant.this, "修改商户").show();
		getmerchantInfo();
	}
	else{
		new TitleMenuUtil(CreatMerchant.this, "创建商户").show();
	}
	init();
}
@Override
protected void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
	isdown=false;
	Log.e("isdown", String.valueOf(isdown));
}
private void getmerchantInfo() {
	isdown=true;
	if(!Tools.isConnect(getApplicationContext())){
		CommonUtil.toastShort(getApplicationContext(), "网络异常");
		return;
	}
	Intent intent=getIntent();
	id=intent.getIntExtra("position", 0);
	MyApplication.getInstance().getClient().post(Config.MERCHANTINFO+id, new AsyncHttpResponseHandler() {
		private Dialog loadingDialog;

		@Override
		public void onStart() {	
			super.onStart();
			loadingDialog = DialogUtil.getLoadingDialg(CreatMerchant.this);
			loadingDialog.show();
		}
		@Override
		public void onFinish() {
			super.onFinish();
			loadingDialog.dismiss();
		}
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			String responseMsg = new String(responseBody)
			.toString();
			Log.e("responseMsg", responseMsg);
			Gson gson = new Gson();
			
			JSONObject jsonobject = null;
			String code = null;
			try {
				jsonobject = new JSONObject(responseMsg);
				code = jsonobject.getString("code");
				int a =jsonobject.getInt("code");
				if(a==Config.CODE){
					JSONObject result=jsonobject.getJSONObject("result");
					/*if(result.length()!=17){
						CommonUtil.toastShort(getApplicationContext(), "服务器返回数据不完全");
						return;
					}*/
					et_shopname.setText(result.getString("title"));
					et_name.setText(result.getString("legal_person_name"));
					et_id_number.setText(result.getString("legal_person_card_id"));
					et_license_code.setText(result.getString("business_license_no"));
					et_tax_id_number.setText(result.getString("tax_registered_no"));
					et_certificate_no.setText(result.getString("organization_code_no"));
					tv_adress.setText(findcity(result.getInt("id")));
					et_bank.setText(result.getString("account_bank_name"));
					et_licencenum_bank.setText(result.getString("bank_open_account"));
					
					imgPath[2]=result.getString("body_photo_path");
					imgPath[3]=result.getString("license_no_pic_path");
					imgPath[4] =result.getString("tax_no_pic_path");
					imgPath[5]=result.getString("org_code_no_pic_path");
					imgPath[6]=result.getString("account_pic_path");
					//imgPath[0]=result.getString("card_id_front_photo_path");
					//imgPath[1] =result.getString("card_id_back_photo_path");
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
					btn_creat.setText("编辑");
					}
				else{
				    code = jsonobject.getString("message");
				    Toast.makeText(getApplicationContext(), code, 1000).show();
					
				}
				}
			catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			
		}
	});
	
}
protected String findcity(int id) {
	// TODO Auto-generated method stub
	String a="苏州";
    List<Province> provinces = CommonUtil.readProvincesAndCities(CreatMerchant.this);
    for (Province province : provinces) {
        List<City> cities = province.getCities();
      
        mCities.addAll(cities);
         
    }
	 for(City cc:mCities ){
		 if(cc.getId()==id){
			 a=cc.getName();
		 }
	 }
	 return a;
}
private void init() {
	btn_legal_photo=(Button) findViewById(R.id.btn_legal_photo);
	btn_license_photos=(Button) findViewById(R.id.btn_license_photos);
	btn_legal_back_photos=(Button) findViewById(R.id.btn_legal_back_photos);
	btn_tax_regist=(Button) findViewById(R.id.btn_tax_regist);
	btn_person_photograph=(Button) findViewById(R.id.btn_person_photograph);
	btn_organization_code_photos=(Button) findViewById(R.id.btn_organization_code_photos);
	btn_bank_license_photos=(Button) findViewById(R.id.btn_bank_license_photos);
	btn_creat=(Button) findViewById(R.id.btn_creat);
	et_shopname=(EditText) findViewById(R.id.et_shopname);
	et_name=(EditText) findViewById(R.id.et_name);
	et_id_number=(EditText) findViewById(R.id.et_id_number);
	et_license_code=(EditText) findViewById(R.id.et_license_code);
	et_tax_id_number=(EditText) findViewById(R.id.et_tax_id_number);
	et_certificate_no=(EditText) findViewById(R.id.et_certificate_no);
	et_bank=(EditText) findViewById(R.id.et_bank);
	et_licencenum_bank=(EditText) findViewById(R.id.et_licencenum_bank);
	tv_adress=(TextView) findViewById(R.id.tv_adress);
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
		if(CityProvinceActivity.isClickconfirm){
			City mMerchantCity = (City) data.getSerializableExtra(com.example.zf_pad.fragment.Constants.CityIntent.SELECTED_CITY);
			cityId=mMerchantCity.getId() ;
			tv_adress.setText(mMerchantCity.getName());
			CityProvinceActivity.isClickconfirm=false;
		}
		
		break;
	case 1001:
		Log.e("localCameraPath", String.valueOf(localCameraPath));
		switch (tag) {
		case 1:
			try {
				imgLocalPath[0]=localCameraPath;
				uploadFile(localCameraPath,tag,btn_legal_photo);
				
				//uploadFile(localSelectPath);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*btn_legal_photo.setBackgroundResource(R.drawable.check_it);
			btn_legal_photo.setText("");
			imgPath[0]=localCameraPath;*/
			
			break;
		case 2:
			try {
				imgLocalPath[1]=localCameraPath;
				uploadFile(localCameraPath,tag,btn_license_photos);
				
				//uploadFile(localSelectPath);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*btn_license_photos.setBackgroundResource(R.drawable.check_it);
			btn_license_photos.setText("");
			imgPath[1]=localCameraPath;*/
			
			break;
		case 3:
			try {
				imgLocalPath[2]=localCameraPath;
				uploadFile(localCameraPath,tag,btn_legal_back_photos);
				
				//uploadFile(localSelectPath);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*btn_legal_back_photos.setBackgroundResource(R.drawable.check_it);
			btn_legal_back_photos.setText("");
			imgPath[2]=localCameraPath;*/
			
			break;
		case 4:
			try {
				imgLocalPath[3]=localCameraPath;
				uploadFile(localCameraPath,tag,btn_tax_regist);
				
				//uploadFile(localSelectPath);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*btn_tax_regist.setBackgroundResource(R.drawable.check_it);
			btn_tax_regist.setText("");
			imgPath[3]=localCameraPath;*/
			
			break;
		case 5:
			try {
				imgLocalPath[4]=localCameraPath;
				uploadFile(localCameraPath,tag,btn_person_photograph);
				
				//uploadFile(localSelectPath);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*btn_person_photograph.setBackgroundResource(R.drawable.check_it);
			btn_person_photograph.setText("");
			imgPath[4]=localCameraPath;*/
		
			break;
		case 6:
			try {
				imgLocalPath[5]=localCameraPath;
				uploadFile(localCameraPath,tag,btn_organization_code_photos);
				
				//uploadFile(localSelectPath);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*btn_organization_code_photos.setBackgroundResource(R.drawable.check_it);
			btn_organization_code_photos.setText("");
			imgPath[5]=localCameraPath;*/
			
			break;
		case 7:
			try {
				imgLocalPath[6]=localCameraPath;
				uploadFile(localCameraPath,tag,btn_bank_license_photos);
				
				//uploadFile(localSelectPath);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*btn_bank_license_photos.setBackgroundResource(R.drawable.check_it);
			btn_bank_license_photos.setText("");
			imgPath[6]=localCameraPath;*/
			
			break;
		default:
			break;
		}
		break;
	case 1002:
		if (data != null) {  
            Uri selectedImage = data.getData();  
            if (selectedImage != null) {  
                Cursor cursor = getContentResolver().query(  
                        selectedImage, null, null, null, null);  
                cursor.moveToFirst();  
                int columnIndex = cursor.getColumnIndex("_data");  
                localSelectPath = cursor.getString(columnIndex);  
                Log.e("localSelectPath", localSelectPath);
                cursor.close();
                switch (tag) {
        		case 1:
        			try {
        				imgLocalPath[0]=localSelectPath;
        				uploadFile(localSelectPath,tag,btn_legal_photo);
        				
						//uploadFile(localSelectPath);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        			break;
        		case 2:
        			try {
        				imgLocalPath[1]=localSelectPath;
        				uploadFile(localSelectPath,tag,btn_license_photos);
        				
						//uploadFile(localSelectPath);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        			/*btn_license_photos.setBackgroundResource(R.drawable.check_it);
        			btn_license_photos.setText("");
        			imgPath[1]=localSelectPath;*/
        			break;
        		case 3:
        			try {
        				imgLocalPath[2]=localSelectPath;
        				uploadFile(localSelectPath,tag,btn_legal_back_photos);
        				
						//uploadFile(localSelectPath);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        			/*btn_legal_back_photos.setBackgroundResource(R.drawable.check_it);
        			btn_legal_back_photos.setText("");
        			imgPath[2]=localSelectPath;*/
        			break;
        		case 4:
        			try {
        				imgLocalPath[3]=localSelectPath;
        				uploadFile(localSelectPath,tag,btn_tax_regist);
        				
						//uploadFile(localSelectPath);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        			/*btn_tax_regist.setBackgroundResource(R.drawable.check_it);
        			btn_tax_regist.setText("");
        			imgPath[3]=localSelectPath;*/
        			break;
        		case 5:
        			try {
        				imgLocalPath[4]=localSelectPath;
        				uploadFile(localSelectPath,tag,btn_person_photograph);
        				
						//uploadFile(localSelectPath);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        			/*btn_person_photograph.setBackgroundResource(R.drawable.check_it);
        			btn_person_photograph.setText("");
        			imgPath[4]=localSelectPath;*/
        			break;
        		case 6:
        			try {
        				imgLocalPath[5]=localSelectPath;
        				uploadFile(localSelectPath,tag,btn_organization_code_photos);
        				
						//uploadFile(localSelectPath);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        			/*btn_organization_code_photos.setBackgroundResource(R.drawable.check_it);
        			btn_organization_code_photos.setText("");
        			imgPath[5]=localSelectPath;*/
        			break;
        		case 7:
        			try {
        				imgLocalPath[6]=localSelectPath;
        				uploadFile(localSelectPath,tag,btn_bank_license_photos);
        				
						//uploadFile(localSelectPath);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        			/*btn_bank_license_photos.setBackgroundResource(R.drawable.check_it);
        			btn_bank_license_photos.setText("");
        			imgPath[6]=localSelectPath;*/
        			break;
        		default:
        			break;
        		}
                if (localSelectPath == null || localSelectPath.equals("null")) { 
                	Toast.makeText(CreatMerchant.this, "未取到图片！", 1000).show();
                }
                    return;  
                }  
            }  
		break;
	default:
		break;
	}
}
private void uploadFile(String path,final int tag,final Button btn)throws Exception {
	File file=new File(path);
	if (file.exists() && file.length() > 0) {
		File fileImg = new File(localSelectPath);
		RequestParams params=new RequestParams();
		params.put("fileImg", fileImg);
		MyApplication.getInstance().getClient().post(API.UPDATE_FILE,
				params, new AsyncHttpResponseHandler() {
			private Dialog loadingDialog;

			@Override
			public void onStart() {	
				super.onStart();
				loadingDialog = DialogUtil.getLoadingDialg(CreatMerchant.this);
				loadingDialog.show();
			}
			@Override
			public void onFinish() {
				super.onFinish();
				loadingDialog.dismiss();
			}
					@Override
					public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
						String responseMsg = new String(responseBody)
						.toString();
						String code = null;
						try {
							JSONObject jsonobject = new JSONObject(responseMsg);
							code = jsonobject.getString("code");
							int a =jsonobject.getInt("code");
							if(a==Config.CODE){ 
								CommonUtil.toastShort(getApplicationContext(), "图片上传成功");
								btn.setBackgroundResource(R.drawable.check_it);
								btn.setText("");
								String str=jsonobject.getJSONObject("result").getString("filePath");
			        			imgPath[tag]=jsonobject.getJSONObject("result").getString("filePath");
			        			Log.e("tag", String.valueOf(tag));
			        			Log.e("imgPath", imgPath[tag]);
							}
							else{
								code = jsonobject.getString("message");
								Toast.makeText(getApplicationContext(), code, 1000).show();
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
					}
					
					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						System.out.println("-onFailure---");
						Log.e("print", "-onFailure---" + error);
						Toast.makeText(CreatMerchant.this, String.valueOf(error), 1000)
						.show();
						
					}
					@Override
					public void onRetry(int retryNo) {
						// TODO Auto-generated method stub
						super.onRetry(5);
					}
				});
	}
	else{
		CommonUtil.toastShort(getApplicationContext(), "文件不存在");
	}
	
}
@Override
public void onClick(View v) {
	switch (v.getId()) {
	case R.id.tv_adress:
		Intent intent = new Intent(CreatMerchant.this,
				CityProvinceActivity.class);
		startActivityForResult(intent, com.example.zf_pad.fragment.Constants.ApplyIntent.REQUEST_CHOOSE_CITY);
		break;
	case R.id.btn_creat:
		if(Mine_MyMerChant.isFromItem){
			changeMerchantInfo();
		}
		else{
			/*for(int i=0;i<imgPath.length;i++){
				if(imgPath[i].equals("")){
					Toast.makeText(getApplicationContext(), "有图片未上传", Toast.LENGTH_SHORT).show();
					return;
				}
			}*/
			sumbitMerchantInfo();
		}
		
		break;
	case R.id.btn_legal_photo:
		tag=1;
		showchooseDialog(btn_legal_photo,tag);
		break;
	case R.id.btn_license_photos:
		tag=2;
		showchooseDialog(btn_license_photos,tag);
		break;
	case R.id.btn_legal_back_photos:
		tag=3;
		showchooseDialog(btn_legal_back_photos,tag);
		break;
	case R.id.btn_tax_regist:
		tag=4;
		showchooseDialog(btn_tax_regist,tag);
		break;
	case R.id.btn_person_photograph:
		tag=5;
		showchooseDialog(btn_person_photograph,tag);
		break;
	case R.id.btn_organization_code_photos:
		tag=6;
		showchooseDialog(btn_organization_code_photos,tag);
		break;
	case R.id.btn_bank_license_photos:
		tag=7;
		showchooseDialog(btn_bank_license_photos,tag);
		break;
	
	default:
		break;
	}
	
}
private void showchooseorseeDialog() {
	// TODO Auto-generated method stub
	
}
private void changeMerchantInfo() {
	if(!Tools.isConnect(getApplicationContext())){
		CommonUtil.toastShort(getApplicationContext(), "网络异常");
		return;
	}
	String title=et_shopname.getText().toString();
	String legalPersonName=et_name.getText().toString();
	String legalPersonCardId=et_id_number.getText().toString();
	String businessLicenseNo=et_license_code.getText().toString();
	String taxRegisteredNo=et_tax_id_number.getText().toString();
	String organizationCodeNo=et_certificate_no.getText().toString();
	
	String accountBankName=et_bank.getText().toString();
	String bankOpenAccount=et_licencenum_bank.getText().toString();
	String cardIdFrontPhotoPath=imgPath[0];
	String cardIdBackPhotoPath =imgPath[1];
	String bodyPhotoPath=imgPath[2];
	String licenseNoPicPath=imgPath[3];
	String taxNoPicPath =imgPath[4];
	String orgCodeNoPicPath=imgPath[5];
	String accountPicPath=imgPath[6];
	API.updatemerchant(
			CreatMerchant.this, 
			title, 
			legalPersonName, 
			legalPersonCardId, 
			businessLicenseNo, 
			taxRegisteredNo, 
			organizationCodeNo, 
			cityId, 
			accountBankName, 
			bankOpenAccount, 
			cardIdFrontPhotoPath, 
			cardIdBackPhotoPath, 
			bodyPhotoPath, 
			licenseNoPicPath, 
			taxNoPicPath, 
			orgCodeNoPicPath, 
			accountPicPath, 
			id, new HttpCallback(CreatMerchant.this) {

				@Override
				public void onSuccess(Object data) {
					Toast.makeText(getApplicationContext(), "修改成功", 1000).show();
					
				}

				@Override
				public TypeToken getTypeToken() {
					// TODO Auto-generated method stub
					return null;
				}
			});
	
}
private void showchooseDialog(Button btn,final int tag) {
	final AlertDialog.Builder builder = new AlertDialog.Builder(CreatMerchant.this);
	LayoutInflater factory = LayoutInflater.from(this);
	final View textEntryView = factory.inflate(R.layout.choosedialog, null);
	// builder.setTitle("自定义输入框");
     builder.setView(textEntryView);
     final Button seeimg=(Button) textEntryView.findViewById(R.id.seeimg);
     final View line_one=textEntryView.findViewById(R.id.line_one);
     final Button choosealbum=(Button) textEntryView.findViewById(R.id.choosealbum);
     final Button choosecamera=(Button) textEntryView.findViewById(R.id.choosecamera);
     if(btn.getText().toString().equals("")){
    	 seeimg.setVisibility(View.VISIBLE);
    	 line_one.setVisibility(View.VISIBLE);
     }
     
     seeimg.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(isdown){
				openimg(tag);
				builder.create().cancel();
			}
			else{
				Intent intent = new Intent(Intent.ACTION_VIEW);    
	            intent.setDataAndType(Uri.parse("file://"+imgLocalPath[tag-1]), "image/*"); 
	            startActivity(intent);
			}
			
			
		}
	});
     choosealbum.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			iscamera=false;
			choosealbum.setBackgroundColor(Color.BLUE);
			choosecamera.setBackgroundColor(Color.WHITE);
		}
	});
     choosecamera.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			iscamera=true;
			choosecamera.setBackgroundColor(Color.BLUE);
			choosealbum.setBackgroundColor(Color.WHITE);
		}
	});
     builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
         public void onClick(DialogInterface dialog, int whichButton) {
        	 if(iscamera){
        		 opencamera();
        	 }
        	 else{
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
	 //build.setTitle("自定义输入框");
     build.setView(textEntryView);
     final ImageView view=(ImageView) textEntryView.findViewById(R.id.imag);
     int ppp=tag-1;
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
	 File dir = new File(Environment.getExternalStorageDirectory()+"/uploadFile/user/8/");  
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
    startActivityForResult(openCameraIntent,  
            1001);
	
}
private void sumbitMerchantInfo() {
	String title=et_shopname.getText().toString();
	String legalPersonName=et_name.getText().toString();
	String legalPersonCardId=et_id_number.getText().toString();
	String businessLicenseNo=et_license_code.getText().toString();
	String taxRegisteredNo=et_tax_id_number.getText().toString();
	String organizationCodeNo=et_certificate_no.getText().toString();
	
	String accountBankName=et_bank.getText().toString();
	String bankOpenAccount=et_licencenum_bank.getText().toString();
	String cardIdFrontPhotoPath=imgPath[0];
	String cardIdBackPhotoPath =imgPath[1];
	String bodyPhotoPath=imgPath[2];
	String licenseNoPicPath=imgPath[3];
	String taxNoPicPath =imgPath[4];
	String orgCodeNoPicPath=imgPath[5];
	String accountPicPath=imgPath[6];
	int customerId=MyApplication.NewUser.getId();
	API.insertmerchant(CreatMerchant.this, 
			title, 
			legalPersonName, 
			legalPersonCardId, 
			businessLicenseNo, 
			taxRegisteredNo, 
			organizationCodeNo,
			cityId, 
			accountBankName,
			bankOpenAccount,
			cardIdFrontPhotoPath,
			cardIdBackPhotoPath,
			bodyPhotoPath,
			licenseNoPicPath,
			taxNoPicPath,
			orgCodeNoPicPath,
			accountPicPath,
			customerId, new HttpCallback(CreatMerchant.this) {

				@Override
				public void onSuccess(Object data) {
					Toast.makeText(getApplicationContext(), "创建商户成功", 1000).show();
					
				}

				@Override
				public TypeToken getTypeToken() {
					// TODO Auto-generated method stub
					return null;
				}
			});
}

}
