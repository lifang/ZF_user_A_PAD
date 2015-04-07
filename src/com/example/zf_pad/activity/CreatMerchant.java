package com.example.zf_pad.activity;

import java.io.File;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.zf_pad.BaseActivity;
import com.example.zf_pad.MyApplication;
import com.example.zf_pad.R;
import com.example.zf_pad.trade.CityProvinceActivity;
import com.example.zf_pad.trade.entity.City;
import com.example.zf_pad.util.TitleMenuUtil;

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
	 private int tag=0;
	 private String[] imgPath=new String[7];
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.creatdetail);
	new TitleMenuUtil(CreatMerchant.this, "创建商户").show();
}
@Override
protected void onStart() {
	// TODO Auto-generated method stub
	super.onStart();
	init();
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
		City mMerchantCity = (City) data.getSerializableExtra(com.example.zf_pad.fragment.Constants.CityIntent.SELECTED_CITY);
		cityId=mMerchantCity.getId() ;
		tv_adress.setText(mMerchantCity.getName());
		break;
	case 1001:
		Log.e("localCameraPath", String.valueOf(localCameraPath));
		switch (tag) {
		case 1:
			btn_legal_photo.setBackgroundResource(R.drawable.check_it);
			btn_legal_photo.setText("");
			imgPath[0]=localCameraPath;
			break;
		case 2:
			btn_license_photos.setBackgroundResource(R.drawable.check_it);
			btn_license_photos.setText("");
			imgPath[1]=localCameraPath;
			break;
		case 3:
			btn_legal_back_photos.setBackgroundResource(R.drawable.check_it);
			btn_legal_back_photos.setText("");
			imgPath[2]=localCameraPath;
			break;
		case 4:
			btn_tax_regist.setBackgroundResource(R.drawable.check_it);
			btn_tax_regist.setText("");
			imgPath[3]=localCameraPath;
			break;
		case 5:
			btn_person_photograph.setBackgroundResource(R.drawable.check_it);
			btn_person_photograph.setText("");
			imgPath[4]=localCameraPath;
			break;
		case 6:
			btn_organization_code_photos.setBackgroundResource(R.drawable.check_it);
			btn_organization_code_photos.setText("");
			imgPath[5]=localCameraPath;
			break;
		case 7:
			btn_bank_license_photos.setBackgroundResource(R.drawable.check_it);
			btn_bank_license_photos.setText("");
			imgPath[6]=localCameraPath;
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
        			btn_legal_photo.setBackgroundResource(R.drawable.check_it);
        			btn_legal_photo.setText("");
        			imgPath[0]=localCameraPath;
        			break;
        		case 2:
        			btn_license_photos.setBackgroundResource(R.drawable.check_it);
        			btn_license_photos.setText("");
        			imgPath[1]=localCameraPath;
        			break;
        		case 3:
        			btn_legal_back_photos.setBackgroundResource(R.drawable.check_it);
        			btn_legal_back_photos.setText("");
        			imgPath[2]=localCameraPath;
        			break;
        		case 4:
        			btn_tax_regist.setBackgroundResource(R.drawable.check_it);
        			btn_tax_regist.setText("");
        			imgPath[3]=localCameraPath;
        			break;
        		case 5:
        			btn_person_photograph.setBackgroundResource(R.drawable.check_it);
        			btn_person_photograph.setText("");
        			imgPath[4]=localCameraPath;
        			break;
        		case 6:
        			btn_organization_code_photos.setBackgroundResource(R.drawable.check_it);
        			btn_organization_code_photos.setText("");
        			imgPath[5]=localCameraPath;
        			break;
        		case 7:
        			btn_bank_license_photos.setBackgroundResource(R.drawable.check_it);
        			btn_bank_license_photos.setText("");
        			imgPath[6]=localCameraPath;
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
@Override
public void onClick(View v) {
	switch (v.getId()) {
	case R.id.tv_adress:
		Intent intent = new Intent(CreatMerchant.this,
				CityProvinceActivity.class);
		startActivityForResult(intent, com.example.zf_pad.fragment.Constants.ApplyIntent.REQUEST_CHOOSE_CITY);
		break;
	case R.id.btn_creat:
		sumbitMerchantInfo();
		break;
	case R.id.btn_legal_photo:
		tag=1;
		showchooseDialog();
		break;
	case R.id.btn_license_photos:
		tag=2;
		showchooseDialog();
		break;
	case R.id.btn_legal_back_photos:
		tag=3;
		showchooseDialog();
		break;
	case R.id.btn_tax_regist:
		tag=4;
		showchooseDialog();
		break;
	case R.id.btn_person_photograph:
		tag=5;
		showchooseDialog();
		break;
	case R.id.btn_organization_code_photos:
		tag=6;
		showchooseDialog();
		break;
	case R.id.btn_bank_license_photos:
		tag=7;
		showchooseDialog();
		break;
	
	default:
		break;
	}
	
}
private void showchooseDialog() {
	AlertDialog.Builder builder = new AlertDialog.Builder(CreatMerchant.this);
	LayoutInflater factory = LayoutInflater.from(this);
	final View textEntryView = factory.inflate(R.layout.choosedialog, null);
	 builder.setTitle("自定义输入框");
     builder.setView(textEntryView);
     final Button choosealbum=(Button) textEntryView.findViewById(R.id.choosealbum);
     final Button choosecamera=(Button) textEntryView.findViewById(R.id.choosecamera);
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
}

}
