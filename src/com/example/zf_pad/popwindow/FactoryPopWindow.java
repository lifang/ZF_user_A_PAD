package com.example.zf_pad.popwindow;

import com.epalmpay.userPad.R;
import com.example.zf_pad.util.ImageCacheUtil;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class FactoryPopWindow extends PopupWindow implements OnClickListener{
	private View conentView;
	private Activity context;
	private ImageView im;
	private String URl, t_title, t_text;
	private Button close;
	public FactoryPopWindow(final Activity context,String URl,String t_title,String t_text) {
		this.t_title=t_title;
		this.URl=URl;
		this.t_text=t_text;
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		conentView = inflater.inflate(R.layout.popwin_factory, null);
		int h = context.getWindowManager().getDefaultDisplay().getHeight();
		int w = context.getWindowManager().getDefaultDisplay().getWidth();
		this.setContentView(conentView);
		this.setWidth(LayoutParams.FILL_PARENT);
		this.setHeight(LayoutParams.FILL_PARENT);
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		this.update();
		ColorDrawable dw = new ColorDrawable(0000000000);
		this.setBackgroundDrawable(dw);
		initView();
	}
	private void initView() {
		im = (ImageView)conentView.findViewById(R.id.im);
		TextView title=(TextView)conentView.findViewById(R.id.title);
		TextView text=(TextView)conentView.findViewById(R.id.text);
		title.setText(t_title);
		text.setText(t_text);
		close = (Button)conentView.findViewById(R.id.close);
		close.setOnClickListener(this);
		ImageCacheUtil.IMAGE_CACHE.get(URl, im);
		
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.close:
			this.dismiss();
			break;
		default:
			break;
		}
		
	}

}
