package com.example.zf_pad.activity;

import com.example.zf_pad.R;
import com.example.zf_pad.util.TitleMenuUtil;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class MyWebView extends Activity {
	private String Url="";
	private String Title="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.webview);
		Title=getIntent().getStringExtra("title");
		Url=getIntent().getStringExtra("url");
		new TitleMenuUtil(this,Title ).show();
		WebView webview=(WebView)findViewById(R.id.webview);
		if(!Url.contains("www")){
			Url=Url.replace("//", "//www.");
		}
		Log.i("www",Url);
		webview.loadUrl(Url); 
	}

}
