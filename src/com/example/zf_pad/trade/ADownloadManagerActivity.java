package com.example.zf_pad.trade;

import static com.example.zf_pad.fragment.Constants.AfterSaleIntent.MATERIAL_URL;
import static com.example.zf_pad.fragment.Constants.AfterSaleIntent.RECORD_TYPE;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.epalmpay.userPad.R;
import com.example.zf_pad.util.TitleMenuUtil;

public class ADownloadManagerActivity extends Activity{
	private long enqueue;
	private DownloadManager dm;
	private int mRecordType;
	private String mUrl;
	private WebView webview;
	private ProgressDialog mDialog;  
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_downloadmanager);
		mRecordType = getIntent().getIntExtra(RECORD_TYPE, 0);
		mUrl = getIntent().getStringExtra(MATERIAL_URL);
		new TitleMenuUtil(this, "�鿴�ļ�").show();
		webview=(WebView) findViewById(R.id.after_sale_material_web_view);  
		webview.getSettings().setJavaScriptEnabled(true);  
		//webview.setWebChromeClient(new MyWebChromeClient());  
		webview.requestFocus();  
		//      webview.loadUrl("file:///android_asset/risktest.html");  
		mUrl = mUrl.replaceAll(" ", "%20");
		webview.loadUrl(mUrl);  
		// ����web��ͼ�ͻ���  
		webview.setWebViewClient(new MyWebViewClient());  
		webview.setDownloadListener(new MyWebViewDownLoadListener());  
	}
	//�ڲ���  
	public class MyWebViewClient extends WebViewClient {  
		// ���ҳ�������ӣ����ϣ��������Ӽ����ڵ�ǰbrowser����Ӧ��  
		// �������¿�Android��ϵͳbrowser����Ӧ�����ӣ����븲�� webview��WebViewClient����  
		public boolean shouldOverviewUrlLoading(WebView view, String url) {  
			view.loadUrl(url);  
			return true;  
		}  

		public void onPageStarted(WebView view, String url, Bitmap favicon) {  
			showProgressDialog();  
		}  

		public void onPageFinished(WebView view, String url) {  
			closeProgressDialog();  
		}  

		public void onReceivedError(WebView view, int errorCode,  
				String description, String failingUrl) {  
			closeProgressDialog();  
		}  
	}  

	//��������κδ��������ҳ�����ϵͳ��Back����������Browser�����finish()����������  
	// ���ϣ��������� ҳ���˶������Ƴ����������Ҫ�ڵ�ǰActivity�д������ѵ���Back�¼���  
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
		// if((keyCode==KeyEvent.KEYCODE_BACK)&&webview.canGoBack()){  
		// webview.goBack();  
		// return true;  
		// }  
		return false;  
	}  
	//�ڲ���  
	private class MyWebViewDownLoadListener implements DownloadListener {  

		@Override  
		public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,  
				long contentLength) {  
			if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){  
				Toast t=Toast.makeText(ADownloadManagerActivity.this, "��ҪSD����", Toast.LENGTH_LONG);  
				t.setGravity(Gravity.CENTER, 0, 0);  
				t.show();  
				return;  
			}  
			DownloaderTask task=new DownloaderTask();  
			task.execute(url);  
		}  

	}  
	//�ڲ���  
	private class DownloaderTask extends AsyncTask<String, Void, String> {   

		public DownloaderTask() {   
		}  

		@Override  
		protected String doInBackground(String... params) {  
			// TODO Auto-generated method stub  
			String url=params[0];  
			//	          Log.i("tag", "url="+url);  
			String fileName=url.substring(url.lastIndexOf("/")+1);  
			fileName=URLDecoder.decode(fileName);  

			File directory=Environment.getExternalStorageDirectory();  
			File file=new File(directory,fileName);  
			if(file.exists()){  
				return fileName;  
			}  
			try {    
				HttpClient client = new DefaultHttpClient();    
				//	                client.getParams().setIntParameter("http.socket.timeout",3000);//���ó�ʱ  
				URL url1 = new URL(url);
				URI uri2;
				if ("".equals(url1.getPort())) {
					uri2 = new URI(url1.getProtocol(), url1.getHost(), url1.getPath(), url1.getQuery(), null);
				}else {
					uri2 = new URI(url1.getProtocol(), url1.getHost()+":"+url1.getPort(), url1.getPath(), url1.getQuery(), null);
				}
				HttpGet get = new HttpGet(uri2);    
				HttpResponse response = client.execute(get);  
				if(HttpStatus.SC_OK==response.getStatusLine().getStatusCode()){  
					HttpEntity entity = response.getEntity();  
					InputStream input = entity.getContent();  

					writeToSDCard(fileName,input);  

					input.close();  
					//	                  entity.consumeContent();  
					return fileName;    
				}else{  
					return null;  
				}  
			} catch (Exception e) {    
				e.printStackTrace();  
				return null;  
			}  
		}  

		@Override  
		protected void onCancelled() {  
			// TODO Auto-generated method stub  
			super.onCancelled();  
		}  

		@Override  
		protected void onPostExecute(String result) {  
			// TODO Auto-generated method stub  
			super.onPostExecute(result);  
			closeProgressDialog();  
			if(result==null){  
				Toast t=Toast.makeText(ADownloadManagerActivity.this, "���Ӵ������Ժ����ԣ�", Toast.LENGTH_LONG);  
				t.setGravity(Gravity.CENTER, 0, 0);  
				t.show();  
				return;  
			}  

			Toast t=Toast.makeText(ADownloadManagerActivity.this, "�ѱ��浽SD����", Toast.LENGTH_LONG);  
			t.setGravity(Gravity.CENTER, 0, 0);  
			t.show();  
			File directory=Environment.getExternalStorageDirectory();  
			File file=new File(directory,result);  

			Intent intent = getFileIntent(file);  

			startActivity(intent);  
			finish();   
		}  

		@Override  
		protected void onPreExecute() {  
			// TODO Auto-generated method stub  
			super.onPreExecute();  
			showProgressDialog();  
		}  

		@Override  
		protected void onProgressUpdate(Void... values) {  
			super.onProgressUpdate(values);  
		}   

	} 

	private void showProgressDialog(){   
		if(mDialog==null){   
			mDialog = new ProgressDialog(ADownloadManagerActivity.this);     
			mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//���÷��ΪԲ�ν�����     
			mDialog.setMessage("���ڼ��� ����ȴ�...");     
			mDialog.setIndeterminate(false);//���ý������Ƿ�Ϊ����ȷ     
			mDialog.setCancelable(true);//���ý������Ƿ���԰��˻ؼ�ȡ��     
			mDialog.setCanceledOnTouchOutside(false);   
			mDialog.setOnDismissListener(new OnDismissListener() {   

				@Override  
				public void onDismiss(DialogInterface dialog) {   
					// TODO Auto-generated method stub   
					mDialog=null;   
				}   
			});   
			mDialog.show();   

		}   
	}   
	private void closeProgressDialog(){   
		if(mDialog!=null){   
			mDialog.dismiss();   
			mDialog=null;   
		}   
	}   
	public Intent getFileIntent(File file){   
		//	       Uri uri = Uri.parse("http://m.ql18.com.cn/hpf10/1.pdf");   
		Uri uri = Uri.fromFile(file);   
		String type = getMIMEType(file);   
		Intent intent = new Intent("android.intent.action.VIEW");   
		intent.addCategory("android.intent.category.DEFAULT");   
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
		intent.setDataAndType(uri, type);   
		return intent;   
	}   

	public void writeToSDCard(String fileName,InputStream input){   

		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){   
			File directory=Environment.getExternalStorageDirectory();   
			File file=new File(directory,fileName);   
			//	          if(file.exists()){   
			//	              Log.i("tag", "The file has already exists.");   
			//	              return;   
			//	          }   
			try {   
				FileOutputStream fos = new FileOutputStream(file);   
				byte[] b = new byte[2048];   
				int j = 0;   
				while ((j = input.read(b)) != -1) {   
					fos.write(b, 0, j);   
				}   
				fos.flush();   
				fos.close();   
			} catch (FileNotFoundException e) {   
				// TODO Auto-generated catch block   
				e.printStackTrace();   
			} catch (IOException e) {   
				// TODO Auto-generated catch block   
				e.printStackTrace();   
			}   
		}else{   
		}   
	}   

	private String getMIMEType(File f){      
		String type="";     
		String fName=f.getName();     
		/* ȡ����չ�� */     
		String end=fName.substring(fName.lastIndexOf(".")+1,fName.length()).toLowerCase();   

		/* ����չ�������;���MimeType */  
		if(end.equals("pdf")){   
			type = "application/pdf";//   
		}   
		else if(end.equals("m4a")||end.equals("mp3")||end.equals("mid")||     
				end.equals("xmf")||end.equals("ogg")||end.equals("wav")){     
			type = "audio/*";      
		}     
		else if(end.equals("3gp")||end.equals("mp4")){     
			type = "video/*";     
		}     
		else if(end.equals("jpg")||end.equals("gif")||end.equals("png")||     
				end.equals("jpeg")||end.equals("bmp")){     
			type = "image/*";     
		}     
		else if(end.equals("apk")){      
			/* android.permission.INSTALL_PACKAGES */      
			type = "application/vnd.android.package-archive";    
		}   
		else if(end.equals("pptx")||end.equals("ppt")){   
			type = "application/vnd.ms-powerpoint";    
		}else if(end.equals("docx")||end.equals("doc")){   
			type = "application/vnd.ms-word";   
		}else if(end.equals("xlsx")||end.equals("xls")){   
			type = "application/vnd.ms-excel";   
		}   
		else{   
			//	        /*����޷�ֱ�Ӵ򿪣�����������б���û�ѡ�� */     
			type="*/*";   
		}   
		return type;   
	} 
}
