package com.example.zf_pad.popwindow;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.zf_pad.Config;
import com.example.zf_pad.R;
import com.example.zf_pad.entity.VersionEntity;
import com.example.zf_pad.trade.API;
import com.example.zf_pad.trade.common.HttpCallback;
import com.example.zf_pad.trade.common.NetworkUtil;
import com.example.zf_pad.util.DownloadUtils;
import com.example.zf_pad.util.Tools;
import com.google.gson.reflect.TypeToken;

public class SetPopWindow extends PopupWindow implements OnClickListener {
	private View conentView;
	private ImageView img_on_off;
	private Boolean isOpen_mineset;
	private SharedPreferences mySharedPreferences;
	private Editor editor;
	private LinearLayout ll_new, ll_clean;
	private TextView tv_clean;
	private Activity context;

	private Dialog versionCheckingDialog;
	private Handler handler;
	
	public SetPopWindow(final Activity context) {
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		conentView = inflater.inflate(R.layout.popwin_setting, null);
		int h = context.getWindowManager().getDefaultDisplay().getHeight();
		int w = context.getWindowManager().getDefaultDisplay().getWidth();
		// 设置SelectPicPopupWindow的View
		this.setContentView(conentView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		// 刷新状态
		this.update();
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0000000000);
		// 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
		this.setBackgroundDrawable(dw);
		// mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		// this.setAnimationStyle(R.style.AnimationPreview);
		initView();

	}

	private void initView() {
		Button bt_close = (Button) conentView.findViewById(R.id.close);
		bt_close.setOnClickListener(this);
		mySharedPreferences = context.getSharedPreferences("zf_set",
				Activity.MODE_PRIVATE);
		editor = mySharedPreferences.edit();
		isOpen_mineset = mySharedPreferences.getBoolean("isOpen_mineset", true);
		img_on_off = (ImageView) conentView.findViewById(R.id.img_on_off);
		if (!isOpen_mineset) {
			img_on_off.setBackgroundResource(R.drawable.pos_off);

		}
		img_on_off.setOnClickListener(this);
		ll_new = (LinearLayout) conentView.findViewById(R.id.ll_new);
		ll_new.setOnClickListener(this);
		ll_clean = (LinearLayout) conentView.findViewById(R.id.ll_clean);
		ll_clean.setOnClickListener(this);

		tv_clean = (TextView) conentView.findViewById(R.id.tv_clean);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.close:
			this.dismiss();
			break;
		case R.id.ll_new:
			// 检测更新
			checkVersion();
			break;
		case R.id.tv_clean:

			tv_clean.setText("");

			break;
		case R.id.img_on_off:

			if (isOpen_mineset) {
				isOpen_mineset = false;
				// img_on_off.setBackgroundDrawable(getResources().getDrawable(R.drawable.pos_off));
				img_on_off.setBackgroundResource(R.drawable.pos_off);
				editor.putBoolean("isOpen_mineset", false);
				editor.commit();

			} else {
				isOpen_mineset = true;
				img_on_off.setBackgroundResource(R.drawable.pos_on);
				editor.putBoolean("isOpen_mineset", true);
				editor.commit();
			}

			break;

		default:
			break;
		}
	}
	
	private void checkVersion() {
		if (NetworkUtil.isNetworkAvailable(context)) {
			// 检查版本dialog
			showVersionCheckingDialog();
		}
		API.getVersion(context,7,new HttpCallback<VersionEntity> (context) {
			@Override
			public void onSuccess(VersionEntity data) {
				String version = data.getVersions();
				String url = data.getDown_url();
				Integer nowVersion = Tools.getVerCode(context);
				Message msg = null;
				if(Integer.parseInt(version) > nowVersion){
					msg = handler.obtainMessage(VersionHandler.HAS_NEW_VERSION);
					Bundle bundle = new Bundle();
					bundle.putString("url", url);
					msg.setData(bundle);
				} else {
					msg = handler.obtainMessage(VersionHandler.NO_NEW_VERSION);
				}
				if (versionCheckingDialog != null) {
					versionCheckingDialog.dismiss();
				}
				handler.sendMessage(msg);
			}

			@Override
			public TypeToken<VersionEntity> getTypeToken() {
				return  new TypeToken<VersionEntity>() {
				};
			}
		} );
	}
	void showVersionCheckingDialog() {
		if (versionCheckingDialog == null) {
			versionCheckingDialog = new Dialog(context, R.style.mydialog);
			versionCheckingDialog
			.setContentView(R.layout.dialog_version_checking);
			TextView textView = (TextView) versionCheckingDialog
					.findViewById(R.id.version_checking_textView);
			textView.setText(R.string.version_check_tip);
		}
		versionCheckingDialog.show();
	}
	private class VersionHandler extends Handler {
		public static final int NO_NEW_VERSION = 0; // 无更新
		public static final int HAS_NEW_VERSION = 1; // 更新
		private Context context;

		public VersionHandler(Context context) {
			super();
			this.context = context;
		}

		public void handleMessage(Message msg) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			switch (msg.what) {
			case NO_NEW_VERSION:
				builder.setCancelable(false);
				builder.setTitle(context.getResources().getString(
						R.string.check_version));
				builder.setMessage(context.getResources().getString(
						R.string.no_check_version));
				builder.setPositiveButton(R.string.update_i_know,
						new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						dialog.dismiss();
					}
				});
				builder.create().show();
				break;
			case HAS_NEW_VERSION:
				 final String url = msg.getData().getString("url");
				builder.setCancelable(true);
				String string = context.getResources().getString(
						R.string.version_check_true);
				if (msg.getData().containsKey("updateContent")) {
					string = msg.getData().getString("updateContent");
				}
				builder.setTitle(context.getResources().getString(
						R.string.version_update_title));
				builder.setMessage(string);
				builder.setPositiveButton(R.string.update_imde,
						new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						upgrading(url);
						dialog.dismiss();
					}
				});
				builder.create().show();
				break;
			}
		}
	}
	private void upgrading(String apkUrl) {
		 ProgressDialog pd = new ProgressDialog(context);
		pd.setCancelable(true);
		pd.setCanceledOnTouchOutside(false);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage(context.getResources().getString(R.string.updata_check));
		fileDownLoad(pd, apkUrl);
		pd.show();
	}
	private void fileDownLoad(ProgressDialog dialog, final String url) {
		try {
			final DownloadListener listener = new DownloadListener(context, dialog);
			final File file = new File(context.getExternalFilesDir(null), "last.apk");
			new Thread() {
				@Override
				public void run() {
					super.run();
					try {
						DownloadUtils.download(url,
								file, false, listener);
					} catch (Exception e) {
					}
				}
			}.start();
		} catch (Exception e) {
			dialog.dismiss();
		}
	}
	private static class DownloadListener implements
	DownloadUtils.DownloadListener {
		private Context sa;
		private ProgressDialog pd;

		public DownloadListener(Context context, ProgressDialog pd) {
			super();
			this.sa = context;
			pd.setMax(100);
			pd.setProgress(0);
			this.pd = pd;
		}

		@Override
		public void downloading(int progress) {
			pd.setProgress(progress);
		}

		@Override
		public void downloaded(File dest) {
			pd.dismiss();
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			intent.setDataAndType(Uri.fromFile(dest),
					"application/vnd.android.package-archive");
			sa.startActivity(intent);
		}

		@Override
		public void exception(Exception e) {
		}
	}
}
